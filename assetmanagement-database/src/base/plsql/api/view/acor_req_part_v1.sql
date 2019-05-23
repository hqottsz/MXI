--liquibase formatted sql


--changeSet acor_req_part_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_part_v1
AS
SELECT
   task.alt_id              AS task_id,
   eqp_part_no.part_no_oem  AS part_number
FROM
   (
      SELECT
         alt_id,
         assmbl_db_id,
         assmbl_cd,
         assmbl_bom_id
      FROM
         task_task
         INNER JOIN ref_task_class ON
            task_task.task_class_db_id = ref_task_class.task_class_db_id AND
            task_task.task_class_cd    = ref_task_class.task_class_cd
      WHERE
         ref_task_class.class_mode_cd = 'REQ'
         AND
         task_def_status_cd IN ('BUILD','ACTV','OBSOLETE')
   ) task
   INNER JOIN eqp_assmbl_bom ON
      task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id  AND
      task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd     AND
      task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
      AND
      eqp_assmbl_bom.bom_class_cd = 'TRK'
      AND
      eqp_assmbl_bom.cfg_slot_status_cd <> 'OBSOLETE'
   INNER JOIN eqp_bom_part ON
      eqp_assmbl_bom.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
      eqp_assmbl_bom.assmbl_cd     = eqp_bom_part.assmbl_cd     AND
      eqp_assmbl_bom.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
   INNER JOIN eqp_part_baseline ON
      eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
      eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
   INNER JOIN eqp_part_no ON
      eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
      eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
      AND
      eqp_part_no.part_status_cd = 'ACTV'
GROUP BY
   task.alt_id,
   eqp_part_no.part_no_oem;