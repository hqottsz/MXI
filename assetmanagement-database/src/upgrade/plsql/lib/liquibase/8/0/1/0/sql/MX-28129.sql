--liquibase formatted sql


--changeSet MX-28129:1 stripComments:false
/*
 * This view determines the replacement inventory from the context of a task.
 */
CREATE OR REPLACE VIEW vw_stask_repl_inv(
      sched_db_id,
      sched_id,
      inv_no_db_id, 
      inv_no_id, 
      config_pos_sdesc
  ) AS
SELECT
   sched_stask.sched_db_id, 
   sched_stask.sched_id, 
   sched_rmvd_part.inv_no_db_id AS inv_no_db_id, 
   sched_rmvd_part.inv_no_id AS inv_no_id,
   CASE WHEN nh_slot.bom_class_cd != 'SUBASSY' THEN 
            NULL 
         ELSE 
            nh_slot.assmbl_bom_cd || 
            CASE WHEN ( nh_slot.pos_ct > 1 ) OR ( nh_pos.eqp_pos_cd != '1' ) THEN 
               ' (' || nh_pos.eqp_pos_cd  || ') ' ELSE '' 
            END ||
            ' ->'
         END || 
         repl_slot.assmbl_bom_cd || 
         CASE WHEN ( repl_slot.pos_ct > 1 ) OR ( repl_pos.eqp_pos_cd != '1' ) THEN 
            ' (' || repl_pos.eqp_pos_cd  || ') ' ELSE '' 
         END
      AS config_pos_sdesc
FROM
   sched_stask
   INNER JOIN sched_part ON
      sched_part.sched_db_id   = sched_stask.repl_sched_db_id AND
      sched_part.sched_id      = sched_stask.repl_sched_id AND
      sched_part.sched_part_id = sched_stask.repl_sched_part_id
   INNER JOIN sched_rmvd_part ON
      sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
      sched_rmvd_part.sched_id      = sched_part.sched_id AND
      sched_rmvd_part.sched_part_id = sched_part.sched_part_id
      AND
      sched_rmvd_part.sched_rmvd_part_id = 1
   INNER JOIN eqp_assmbl_bom repl_slot ON
      repl_slot.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_slot.assmbl_cd     = sched_part.assmbl_cd AND
      repl_slot.assmbl_bom_id = sched_part.assmbl_bom_id
   INNER JOIN eqp_assmbl_pos repl_pos ON
      repl_pos.assmbl_db_id  = sched_part.assmbl_db_id AND
      repl_pos.assmbl_cd     = sched_part.assmbl_cd AND
      repl_pos.assmbl_bom_id = sched_part.assmbl_bom_id AND
      repl_pos.assmbl_pos_id = sched_part.assmbl_pos_id
   INNER JOIN eqp_assmbl_bom nh_slot ON
      nh_slot.assmbl_db_id  = sched_part.nh_assmbl_db_id AND
      nh_slot.assmbl_cd     = sched_part.nh_assmbl_cd AND
      nh_slot.assmbl_bom_id = sched_part.nh_assmbl_bom_id
   INNER JOIN eqp_assmbl_pos nh_pos ON
      nh_pos.assmbl_db_id  = sched_part.nh_assmbl_db_id AND
      nh_pos.assmbl_cd     = sched_part.nh_assmbl_cd AND
      nh_pos.assmbl_bom_id = sched_part.nh_assmbl_bom_id AND
      nh_pos.assmbl_pos_id = sched_part.nh_assmbl_pos_id
;