--liquibase formatted sql


--changeSet UpdateSyncAllInvByTaskDefnRev:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE UpdateSyncAllInvByTaskDefnRev(p_aTaskDbId IN NUMBER,
                                                          p_aTaskId   IN NUMBER) IS

BEGIN

    MERGE INTO inv_sync_queue
      USING
      (
         SELECT DISTINCT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id
         FROM
            inv_inv,
            task_task,
            task_part_map,
            eqp_assmbl_bom
         WHERE
            task_task.task_db_id = p_aTaskDbId AND
            task_task.task_id    = p_aTaskId
            AND
            task_part_map.task_db_id (+)= task_task.task_db_id AND
            task_part_map.task_id    (+)= task_task.task_id
            AND
            (
              -- get all the possible inventory for the task definition against the assembly
              (
                eqp_assmbl_bom.assmbl_db_id  = inv_inv.assmbl_db_id AND
                eqp_assmbl_bom.assmbl_cd     = inv_inv.assmbl_cd AND
                eqp_assmbl_bom.assmbl_bom_id = inv_inv.assmbl_bom_id
                AND
                (
                   (
                      task_task.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
                      task_task.assmbl_cd = eqp_assmbl_bom.assmbl_cd AND
                      task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                   )
                   OR
                   (
                      task_task.repl_assmbl_db_id = eqp_assmbl_bom.assmbl_db_id AND
                      task_task.repl_assmbl_cd = eqp_assmbl_bom.assmbl_cd AND
                      task_task.repl_assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
                   )
                )
               )
              -- get all the possible inventory for the task definition against the original assembly
              OR
              (
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
              -- get all the possible inventory for the task definition against the task definition parts
              OR
              (
                 task_part_map.part_no_db_id = inv_inv.part_no_db_id AND
                 task_part_map.part_no_id    = inv_inv.part_no_id
              )
            )
            AND
            inv_inv.rstat_cd = 0
         ) inv_tasks
      ON
      (
         inv_sync_queue.inv_no_db_id = inv_tasks.inv_no_db_id AND
         inv_sync_queue.inv_no_id = inv_tasks.inv_no_id
      )
      WHEN NOT MATCHED THEN
         INSERT(inv_no_db_id, inv_no_id, queue_date) VALUES (inv_tasks.inv_no_db_id, inv_tasks.inv_no_id, SYSDATE);

END;
/