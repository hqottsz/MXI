--liquibase formatted sql


--changeSet acor_req_ata_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_req_ata_v1
AS
SELECT
   task_task.alt_id                    AS task_id,
   task_task.task_cd                   AS task_code,
   eqp_assmbl_bom.alt_id               AS bom_id,
   eqp_assmbl_bom.assmbl_cd            AS assembly_code,
   eqp_assmbl_bom.assmbl_bom_cd        AS config_slot_code,
   task_task.revision_ord              AS revision_order,
   eqp_assmbl_bom.logcard_form_cd      AS logcard_form_code,
   eqp_assmbl_bom.bom_class_cd         AS config_slot_class_code
FROM
   task_task
   -- assembly
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
      task_task.Assmbl_Cd     = eqp_assmbl_bom.Assmbl_Cd    AND
      task_task.Assmbl_Bom_Id = eqp_assmbl_bom.Assmbl_Bom_Id
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
WHERE
   class_mode_cd = 'REQ'
   AND
   task_task.workscope_bool = 0
;