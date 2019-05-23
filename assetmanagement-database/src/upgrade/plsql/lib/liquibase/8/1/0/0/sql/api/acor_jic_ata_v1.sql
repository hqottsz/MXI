--liquibase formatted sql


--changeSet acor_jic_ata_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_ata_v1
AS 
SELECT
   task_task.alt_id                  AS task_id,
   eqp_assmbl_bom.alt_id             AS bom_id,
   eqp_assmbl_bom.assmbl_cd          AS assembly_code,
   eqp_assmbl_bom.assmbl_bom_cd      AS config_slot_code,
   task_task.revision_ord            AS revision_order
FROM
   task_task
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_id  = eqp_assmbl_bom.Assmbl_Db_Id AND
      task_task.assmbl_cd     = eqp_assmbl_bom.Assmbl_Cd    AND
      task_task.assmbl_bom_id = eqp_assmbl_bom.Assmbl_Bom_Id
WHERE
   class_mode_cd = 'JIC'
   AND
   NOT (ref_task_class.task_class_db_id = 0 AND
        ref_task_class.task_class_cd   IN ('OPENPANEL','CLOSEPANEL')
       )
;