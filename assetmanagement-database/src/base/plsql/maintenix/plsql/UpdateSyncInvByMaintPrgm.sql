--liquibase formatted sql


--changeSet UpdateSyncInvByMaintPrgm:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE UpdateSyncInvByMaintPrgm(p_aMaintPrgmDbId IN NUMBER,
                                                     p_aMaintPrgmId   IN NUMBER)

 IS
BEGIN
      MERGE INTO inv_sync_queue
      USING
      (
         SELECT DISTINCT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id
         FROM
            (
               SELECT
                  maint_prgm_task.task_db_id,
                  maint_prgm_task.task_id
               FROM
                  maint_prgm,
                  maint_prgm_carrier_map,
                  maint_prgm_task
               WHERE
                  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
                  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
                  AND
                  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
                  AND
                  maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
                  AND
                  maint_prgm_task.unassign_bool = 0
                  AND NOT EXISTS
                  (
                     SELECT
                        1
                     FROM
                        maint_prgm_task prev_maint_prgm_task,
                        maint_prgm prev_maint_prgm,
                        maint_prgm_carrier_map prev_maint_prgm_carrier
                     WHERE
                        prev_maint_prgm_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
                        prev_maint_prgm_task.task_defn_id    = maint_prgm_task.task_defn_id
                        AND
                        prev_maint_prgm_task.unassign_bool = 0
                        AND
                        prev_maint_prgm.maint_prgm_db_id = prev_maint_prgm_task.maint_prgm_db_id AND
                        prev_maint_prgm.maint_prgm_id    = prev_maint_prgm_task.maint_prgm_id
                        AND
                        prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
                        prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
                        AND
                        prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
                        prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
                        AND
                        prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                        prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
                        AND
                        prev_maint_prgm_carrier.latest_revision_bool = 1
                  )
               UNION ALL
               SELECT
                  prev_maint_prgm_task.task_db_id,
                  prev_maint_prgm_task.task_id
               FROM
                  maint_prgm,
                  maint_prgm_carrier_map,
                  maint_prgm prev_maint_prgm,
                  maint_prgm_carrier_map prev_maint_prgm_carrier,
                  maint_prgm_task prev_maint_prgm_task
               WHERE
                  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
                  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
                  AND
                  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
                  AND
                  prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
                  prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
                  AND
                  prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
                  prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
                  AND
                  prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                  prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
                  AND
                  prev_maint_prgm_carrier.latest_revision_bool = 1
                  AND
                  prev_maint_prgm_task.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
                  prev_maint_prgm_task.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
                  AND
                  prev_maint_prgm_task.unassign_bool = 0
                  AND NOT EXISTS
                  (
                     SELECT
                        1
                     FROM
                        maint_prgm_task
                     WHERE
                        maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                        maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
                        AND
                        maint_prgm_task.task_defn_db_id = prev_maint_prgm_task.task_defn_db_id AND
                        maint_prgm_task.task_defn_id    = prev_maint_prgm_task.task_defn_id
                        AND
                        maint_prgm_task.unassign_bool = 0
                  )
               UNION ALL
               SELECT
                  maint_prgm_task.task_db_id,
                  maint_prgm_task.task_id
               FROM
                  maint_prgm,
                  maint_prgm_carrier_map,
                  maint_prgm_task,
                  maint_prgm_task prev_maint_prgm_task,
                  maint_prgm prev_maint_prgm,
                  maint_prgm_carrier_map prev_maint_prgm_carrier
               WHERE
                  maint_prgm.maint_prgm_db_id = p_aMaintPrgmDbId AND
                  maint_prgm.maint_prgm_id    = p_aMaintPrgmId
                  AND
                  maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
                  AND
                  maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
                  maint_prgm_task.maint_prgm_id    = maint_prgm.maint_prgm_id
                  AND
                  maint_prgm_task.unassign_bool = 0
                  AND
                  prev_maint_prgm_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
                  prev_maint_prgm_task.task_defn_id    = maint_prgm_task.task_defn_id
                  AND
                  prev_maint_prgm_task.unassign_bool = 0
                  AND
                  prev_maint_prgm.maint_prgm_db_id = prev_maint_prgm_task.maint_prgm_db_id AND
                  prev_maint_prgm.maint_prgm_id    = prev_maint_prgm_task.maint_prgm_id
                  AND
                  prev_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
                  prev_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
                  AND
                  prev_maint_prgm_carrier.maint_prgm_db_id = prev_maint_prgm.maint_prgm_db_id AND
                  prev_maint_prgm_carrier.maint_prgm_id    = prev_maint_prgm.maint_prgm_id
                  AND
                  prev_maint_prgm_carrier.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                  prev_maint_prgm_carrier.carrier_id    = maint_prgm_carrier_map.carrier_id
                  AND
                  prev_maint_prgm_carrier.latest_revision_bool = 1
                  AND NOT
                  (
                     maint_prgm_task.task_db_id = prev_maint_prgm_task.task_db_id AND
                     maint_prgm_task.task_id    = prev_maint_prgm_task.task_id
                  )
            ) maint_prgm_task,
            maint_prgm_carrier_map,
            inv_inv,
            inv_inv assmbl_inv,
            eqp_assmbl_bom,
            task_task
         WHERE
            maint_prgm_carrier_map.maint_prgm_db_id = p_aMaintPrgmDbId AND
            maint_prgm_carrier_map.maint_prgm_id    = p_aMaintPrgmId
            AND
            assmbl_inv.inv_no_db_id (+)= inv_inv.assmbl_inv_no_db_id AND
            assmbl_inv.inv_no_id    (+)= inv_inv.assmbl_inv_no_id
            AND
            assmbl_inv.rstat_cd(+) = 0
            AND
            maint_prgm_carrier_map.carrier_db_id =
              CASE WHEN inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
                 THEN inv_inv.carrier_db_id
                 ELSE assmbl_inv.carrier_db_id
              END
            AND
            maint_prgm_carrier_map.carrier_id =
              CASE WHEN inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
                 THEN inv_inv.carrier_id
                 ELSE assmbl_inv.carrier_id
              END
            AND
            (
               (
                  eqp_assmbl_bom.assmbl_db_id  = inv_inv.assmbl_db_id AND
                  eqp_assmbl_bom.assmbl_cd     = inv_inv.assmbl_cd AND
                  eqp_assmbl_bom.assmbl_bom_id = inv_inv.assmbl_bom_id
                  AND
                  (
                     (
                        task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
                        task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
                        task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                     )
                     OR
                     (
                        task_task.repl_assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
                        task_task.repl_assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
                        task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                     )
                  )
               )
               OR
               (
                  eqp_assmbl_bom.assmbl_db_id  = inv_inv.orig_assmbl_db_id AND
                  eqp_assmbl_bom.assmbl_cd     = inv_inv.orig_assmbl_cd AND
                  eqp_assmbl_bom.assmbl_bom_id = 0
                  AND
                  (
                     (
                        task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
                        task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
                        task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                     )
                     OR
                     (
                        task_task.repl_assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
                        task_task.repl_assmbl_cd     = eqp_assmbl_bom.assmbl_cd AND
                        task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                     )
                  )
               )
            )
            AND
            maint_prgm_task.task_db_id = task_task.task_db_id AND
            maint_prgm_task.task_id    = task_task.task_id
            AND
            inv_inv.rstat_cd = 0
   ) maint_inv
   ON
   (
      inv_sync_queue.inv_no_db_id = maint_inv.inv_no_db_id AND
      inv_sync_queue.inv_no_id = maint_inv.inv_no_id
   )
   WHEN NOT MATCHED THEN
      INSERT(inv_no_db_id, inv_no_id, queue_date) VALUES (maint_inv.inv_no_db_id, maint_inv.inv_no_id, SYSDATE);

END;
/