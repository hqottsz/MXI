--changeSet OPER-30942:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Find open part requests that have a needed location and add them to the auto reservation queue for reprocessing.
DECLARE
   CURSOR c_res_entries IS
      SELECT DISTINCT
         req_part.req_spec_part_no_db_id,
         req_part.req_spec_part_no_id,
         req_part.req_bom_part_db_id,
         req_part.req_bom_part_id,
         inv_loc.supply_loc_db_id,
         inv_loc.supply_loc_id
      FROM 
         req_part
         INNER JOIN evt_event ON
            req_part.req_part_db_id = evt_event.event_db_id AND
            req_part.req_part_id    = evt_event.event_id
         INNER JOIN inv_loc ON
            req_part.req_loc_db_id = inv_loc.loc_db_id AND
            req_part.req_loc_id    = inv_loc.loc_id
      WHERE
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'PROPEN'
         AND
         inv_loc.supply_loc_id IS NOT NULL AND
         inv_loc.rstat_cd = 0 
         AND
         req_part.req_loc_id IS NOT NULL;

   TYPE t_res_entries IS TABLE OF c_res_entries%ROWTYPE INDEX BY BINARY_INTEGER;
   l_res_entries t_res_entries;

BEGIN

   OPEN c_res_entries;
   FETCH c_res_entries BULK COLLECT INTO l_res_entries;
   CLOSE c_res_entries;

   FORALL i IN 1..l_res_entries.COUNT
      INSERT INTO auto_rsrv_queue (
         auto_rsrv_db_id,
         auto_rsrv_id,
         part_no_db_id,
         part_no_id,
         bom_part_db_id,
         bom_part_id,
         sup_loc_db_id,
         sup_loc_id
         )
      VALUES (
         application_object_pkg.getdbid(),
         auto_rsrv_id.nextval,
         l_res_entries(i).req_spec_part_no_db_id,
         l_res_entries(i).req_spec_part_no_id,
         l_res_entries(i).req_bom_part_db_id,
         l_res_entries(i).req_bom_part_id,
         l_res_entries(i).supply_loc_db_id,
         l_res_entries(i).supply_loc_id
      );
END;
/
