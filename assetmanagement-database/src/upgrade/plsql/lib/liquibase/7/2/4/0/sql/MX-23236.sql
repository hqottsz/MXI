--liquibase formatted sql


--changeSet MX-23236:1 stripComments:false
/*
 * This view determines the replacement inventory from the context of a task.
 * The FIRST_VALUE calls are needed in case there are multiple tracked part
 * requirements on one of the sub-tasks of the REPL task. This should not 
 * normally be the case though.
 */
CREATE OR REPLACE VIEW vw_stask_repl_inv(
      sched_db_id,
      sched_id,
      inv_no_db_id, 
      inv_no_id, 
      config_pos_sdesc
  ) AS
-- in this select we assume the given task must be a REPL and the removal has an inventory key
SELECT DISTINCT
   repl_stask.sched_db_id, 
   repl_stask.sched_id, 
   FIRST_VALUE(inv_inv.inv_no_db_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_db_id, 
   FIRST_VALUE(inv_inv.inv_no_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_id, 
   FIRST_VALUE(inv_inv.config_pos_sdesc) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS config_pos_sdesc
FROM
   sched_stask repl_stask
   -- get children of the REPL and the REPL itself
   INNER JOIN evt_event jic_event ON
      (
      jic_event.nh_event_db_id = repl_stask.sched_db_id AND
      jic_event.nh_event_id    = repl_stask.sched_id
      )
      OR
      (
      jic_event.event_db_id = repl_stask.sched_db_id AND
      jic_event.event_id    = repl_stask.sched_id
      )
   INNER JOIN sched_part ON
      sched_part.sched_db_id = jic_event.event_db_id AND
      sched_part.sched_id    = jic_event.event_id
   INNER JOIN sched_rmvd_part ON
      sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_part.sched_part_id
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = sched_rmvd_part.inv_no_db_id AND
      inv_inv.inv_no_id    = sched_rmvd_part.inv_no_id
      AND
      inv_inv.assmbl_db_id  = sched_part.assmbl_db_id AND
      inv_inv.assmbl_cd     = sched_part.assmbl_cd AND
      inv_inv.assmbl_bom_id = sched_part.assmbl_bom_id AND
      inv_inv.assmbl_pos_id = sched_part.assmbl_pos_id
WHERE
   repl_stask.task_class_db_id = 0 AND
   repl_stask.task_class_cd = 'REPL'
UNION ALL
-- in this select we assume the given task must be a REPL and the removal has no inventory key
SELECT DISTINCT
   repl_stask.sched_db_id, 
   repl_stask.sched_id, 
   FIRST_VALUE(inv_inv.inv_no_db_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_db_id, 
   FIRST_VALUE(inv_inv.inv_no_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_id, 
   FIRST_VALUE(
         inv_inv.config_pos_sdesc || ' ->' || repl_slot.assmbl_bom_cd || 
         CASE WHEN ( repl_slot.pos_ct > 1 ) OR ( repl_pos.eqp_pos_cd != '1' ) THEN 
            ' (' || repl_pos.eqp_pos_cd  || ') ' ELSE '' END
      ) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS config_pos_sdesc
FROM
   sched_stask repl_stask
   -- get children of the REPL and the REPL itself
   INNER JOIN evt_event jic_event ON
      (
      jic_event.nh_event_db_id = repl_stask.sched_db_id AND
      jic_event.nh_event_id    = repl_stask.sched_id
      )
      OR
      (
      jic_event.event_db_id = repl_stask.sched_db_id AND
      jic_event.event_id    = repl_stask.sched_id
      )
   INNER JOIN sched_part ON
      sched_part.sched_db_id = jic_event.event_db_id AND
      sched_part.sched_id    = jic_event.event_id
   INNER JOIN sched_rmvd_part ON
      sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_part.sched_part_id
      AND
      sched_rmvd_part.inv_no_db_id IS NULL
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id  = repl_stask.main_inv_no_db_id AND
      inv_inv.inv_no_id     = repl_stask.main_inv_no_id
   INNER JOIN eqp_assmbl_bom repl_slot ON
      repl_slot.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_slot.assmbl_cd     = sched_part.assmbl_cd AND
      repl_slot.assmbl_bom_id = sched_part.assmbl_bom_id
   INNER JOIN eqp_assmbl_pos repl_pos ON
      repl_pos.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_pos.assmbl_cd     = sched_part.assmbl_cd AND
      repl_pos.assmbl_bom_id = sched_part.assmbl_bom_id AND
      repl_pos.assmbl_pos_id = sched_part.assmbl_pos_id
WHERE
   repl_stask.task_class_db_id = 0 AND
   repl_stask.task_class_cd = 'REPL'
UNION ALL
--in this select we assume that the given task is a sub-task of a REPL and the removal has an inventory key
SELECT DISTINCT
   evt_event.event_db_id AS sched_db_id, 
   evt_event.event_id AS sched_id,
   FIRST_VALUE(inv_inv.inv_no_db_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_db_id, 
   FIRST_VALUE(inv_inv.inv_no_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_id, 
   FIRST_VALUE(inv_inv.config_pos_sdesc) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS config_pos_sdesc
FROM
   evt_event
   INNER JOIN sched_stask repl_stask ON
      repl_stask.sched_db_id = evt_event.nh_event_db_id AND
      repl_stask.sched_id    = evt_event.nh_event_id
   -- get children of the REPL
   INNER JOIN evt_event jic_event ON
      jic_event.nh_event_db_id = repl_stask.sched_db_id AND
      jic_event.nh_event_id    = repl_stask.sched_id
   INNER JOIN sched_part ON
      sched_part.sched_db_id = jic_event.event_db_id AND
      sched_part.sched_id    = jic_event.event_id
   INNER JOIN sched_rmvd_part ON
      sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_part.sched_part_id
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = sched_rmvd_part.inv_no_db_id AND
      inv_inv.inv_no_id    = sched_rmvd_part.inv_no_id
      AND
      inv_inv.assmbl_db_id  = sched_part.assmbl_db_id AND
      inv_inv.assmbl_cd     = sched_part.assmbl_cd AND
      inv_inv.assmbl_bom_id = sched_part.assmbl_bom_id AND
      inv_inv.assmbl_pos_id = sched_part.assmbl_pos_id
WHERE
   repl_stask.task_class_db_id = 0 AND
   repl_stask.task_class_cd = 'REPL'
UNION ALL
--in this select we assume that the given task is a sub-task of a REPL and the removal has no inventory key
SELECT DISTINCT
   evt_event.event_db_id AS sched_db_id, 
   evt_event.event_id AS sched_id,
   FIRST_VALUE(inv_inv.inv_no_db_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_db_id, 
   FIRST_VALUE(inv_inv.inv_no_id) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS inv_no_id, 
   FIRST_VALUE(
         inv_inv.config_pos_sdesc || ' ->' || repl_slot.assmbl_bom_cd || 
         CASE WHEN ( repl_slot.pos_ct > 1 ) OR ( repl_pos.eqp_pos_cd != '1' ) THEN 
            ' (' || repl_pos.eqp_pos_cd  || ') ' ELSE '' END
      ) OVER (PARTITION BY repl_stask.sched_db_id, repl_stask.sched_id ORDER BY sched_part.creation_dt) AS config_pos_sdesc
FROM
   evt_event
   INNER JOIN sched_stask repl_stask ON
      repl_stask.sched_db_id = evt_event.nh_event_db_id AND
      repl_stask.sched_id    = evt_event.nh_event_id
   -- get children of the REPL
   INNER JOIN evt_event jic_event ON
      jic_event.nh_event_db_id = repl_stask.sched_db_id AND
      jic_event.nh_event_id    = repl_stask.sched_id
   INNER JOIN sched_part ON
      sched_part.sched_db_id = jic_event.event_db_id AND
      sched_part.sched_id    = jic_event.event_id
   INNER JOIN sched_rmvd_part ON
      sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_part.sched_part_id
      AND
      sched_rmvd_part.inv_no_db_id IS NULL
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id  = repl_stask.main_inv_no_db_id AND
      inv_inv.inv_no_id     = repl_stask.main_inv_no_id
   INNER JOIN eqp_assmbl_bom repl_slot ON
      repl_slot.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_slot.assmbl_cd     = sched_part.assmbl_cd AND
      repl_slot.assmbl_bom_id = sched_part.assmbl_bom_id
   INNER JOIN eqp_assmbl_pos repl_pos ON
      repl_pos.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_pos.assmbl_cd     = sched_part.assmbl_cd AND
      repl_pos.assmbl_bom_id = sched_part.assmbl_bom_id AND
      repl_pos.assmbl_pos_id = sched_part.assmbl_pos_id
WHERE
   repl_stask.task_class_db_id = 0 AND
   repl_stask.task_class_cd = 'REPL';