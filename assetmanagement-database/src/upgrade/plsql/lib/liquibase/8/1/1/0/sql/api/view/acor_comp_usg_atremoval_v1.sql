--liquibase formatted sql


--changeSet acor_comp_usg_atremoval_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_comp_usg_atremoval_v1 
AS
SELECT 
   inv_inv.alt_id       AS inventory_id,
   sched_stask.alt_id   AS sched_id,
   Cycles.tsn_qt        AS CSN,
   Cycles.Tso_Qt        AS CSO,
   Cycles.Tsi_Qt        AS CSI,
   Hours.Tsn_Qt         AS TSN,
   Hours.Tso_Qt         AS TSO,
   Hours.Tsi_Qt         AS TSI,
   Cycles.Assmbl_Tsn_Qt AS AssemblyCSN,
   Cycles.Assmbl_Tso_Qt AS AssemblyCSO,
   Hours.Assmbl_Tsn_Qt  AS AssemblyTSN,
   Hours.Assmbl_Tso_Qt  AS AssemblyTSO
FROM
   sched_rmvd_part srp
   INNER JOIN evt_inv ON 
      srp.inv_no_db_id = evt_inv.inv_no_db_id AND
      srp.inv_no_id    = evt_inv.inv_no_id AND 
      srp.sched_db_id  = evt_inv.event_db_id AND 
      srp.sched_id = evt_inv.event_id
   -- hours
   INNER JOIN evt_inv_usage Hours ON 
      Hours.event_db_id  = evt_inv.event_db_id AND
      Hours.event_id     = evt_inv.event_id AND
      hours.event_inv_id = evt_inv.event_inv_id
      AND
      Hours.data_type_id = 1
   -- cycle
   INNER JOIN evt_inv_usage Cycles ON 
      Cycles.event_db_id  = evt_inv.event_db_id AND
      Cycles.event_id     = evt_inv.event_id AND
      cycles.event_inv_id = evt_inv.event_inv_id
      AND
      Cycles.data_type_id = 10
   --
   INNER JOIN inv_inv ON 
      srp.inv_no_db_id = inv_inv.inv_no_db_id AND
      srp.inv_no_id    = inv_inv.inv_no_id
   INNER JOIN sched_stask ON 
      evt_inv.event_db_id = sched_stask.sched_db_id AND
      evt_inv.event_id    = sched_stask.sched_id
;