--liquibase formatted sql
--changeSet OPER-9639:1 stripComments:false
--comment update all the create/cancel instructions to be off for task definitions of SYS config slot 

MERGE INTO 
   task_task
USING 
   eqp_assmbl_bom
ON 
(
   task_task.assmbl_db_id      = eqp_assmbl_bom.assmbl_db_id AND 
   task_task.assmbl_cd         = eqp_assmbl_bom.assmbl_cd AND 
   task_task.assmbl_bom_id     = eqp_assmbl_bom.assmbl_bom_id AND
   eqp_assmbl_bom.bom_class_db_id = 0 AND
   eqp_assmbl_bom.bom_class_cd    = 'SYS' AND
   (
      task_task.create_on_ac_inst_bool  = 1 OR 
      task_task.create_on_any_inst_bool = 1 OR 
      task_task.create_on_ac_rmvl_bool  = 1 OR 
      task_task.create_on_any_rmvl_bool = 1 OR 
      task_task.cancel_on_ac_inst_bool  = 1 OR 
      task_task.cancel_on_any_inst_bool = 1 OR 
      task_task.cancel_on_ac_rmvl_bool  = 1 OR 
      task_task.cancel_on_any_rmvl_bool = 1
   )
)
WHEN MATCHED THEN
UPDATE SET  
   task_task.create_on_ac_inst_bool  = 0, 
   task_task.create_on_any_inst_bool = 0, 
   task_task.create_on_ac_rmvl_bool  = 0,  
   task_task.create_on_any_rmvl_bool = 0, 
   task_task.cancel_on_ac_inst_bool  = 0, 
   task_task.cancel_on_any_inst_bool = 0, 
   task_task.cancel_on_ac_rmvl_bool  = 0, 
   task_task.cancel_on_any_rmvl_bool = 0;