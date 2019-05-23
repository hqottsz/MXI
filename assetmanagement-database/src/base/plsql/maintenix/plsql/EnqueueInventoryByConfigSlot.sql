--liquibase formatted sql


--changeSet EnqueueInventoryByConfigSlot:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE EnqueueInventoryByConfigSlot(aAssmblDbId  NUMBER,
                                                         aAssmblCd    VARCHAR2,
                                                         aAssmblBomId NUMBER) IS

BEGIN
   -- This query inserts all inventory (excluding SCRAP inventory) with given config slot into the queue.
   -- If the config slot class is SUBASSY, then inserts all the sub-inventory as well

   INSERT INTO inv_pos_desc_queue
      (inv_no_db_id,
       inv_no_id)

      (
       -- Get installed or loose inventory with the given config slot,
       -- where the config slot class is ROOT, TRK or SYS
       SELECT inv_inv.inv_no_db_id,
               inv_inv.inv_no_id
         FROM inv_inv
        INNER JOIN eqp_assmbl_bom ON inv_inv.assmbl_db_id =
                                     eqp_assmbl_bom.assmbl_db_id
                                 AND inv_inv.assmbl_cd =
                                     eqp_assmbl_bom.assmbl_cd
                                 AND inv_inv.assmbl_bom_id =
                                     eqp_assmbl_bom.assmbl_bom_id
        WHERE inv_inv.assmbl_db_id = aassmbldbid
          AND inv_inv.assmbl_cd = aassmblcd
          AND inv_inv.assmbl_bom_id = aassmblbomid
          AND eqp_assmbl_bom.bom_class_cd IN ('ROOT', 'TRK', 'SYS')
          AND inv_inv.inv_cond_cd != 'SCRAP'
          AND inv_inv.inv_class_cd IN ('ASSY', 'TRK', 'SYS')

       UNION

       -- Get inventory with the given config slot where config slot is SUBASSY
       SELECT inv_inv.inv_no_db_id,
              inv_inv.inv_no_id
         FROM inv_inv
        INNER JOIN eqp_assmbl_bom ON inv_inv.assmbl_db_id =
                                     eqp_assmbl_bom.assmbl_db_id
                                 AND inv_inv.assmbl_cd =
                                     eqp_assmbl_bom.assmbl_cd
                                 AND inv_inv.assmbl_bom_id =
                                     eqp_assmbl_bom.assmbl_bom_id
        WHERE inv_inv.inv_class_cd IN ('ASSY', 'TRK', 'SYS')
          AND inv_inv.inv_cond_cd != 'SCRAP'
          AND eqp_assmbl_bom.bom_class_cd IN ('SUBASSY', 'ROOT', 'TRK', 'SYS')
        START WITH inv_inv.assmbl_db_id = aassmbldbid
               AND inv_inv.assmbl_cd = aassmblcd
               AND inv_inv.assmbl_bom_id = aassmblbomid
               AND inv_inv.nh_inv_no_db_id IS NOT NULL
               AND eqp_assmbl_bom.bom_class_cd = 'SUBASSY'
       CONNECT BY PRIOR inv_inv.inv_no_db_id = inv_inv.nh_inv_no_db_id
              AND PRIOR inv_inv.inv_no_id = inv_inv.nh_inv_no_id);

END;
/