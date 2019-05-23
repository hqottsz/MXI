--liquibase formatted sql

--changeSet OPER-13105:1 stripComments:false 
UPDATE 
   utl_config_parm 
SET 
   parm_desc = 'Allow the auto-completion of open tasks, faults, and work packages that are associated with an inventory item when a user inspects the inventory item as serviceable. If set to FALSE, users cannot inspect the inventory as serviceable until the tasks and work package are manually completed. Auto-completion is not applicable to inspecting inventory as unserviceable.'
WHERE 
   parm_name = 'ALLOW_AUTO_COMPLETION';

--changeSet OPER-11371:1 stripComments:false 
DELETE FROM 
   ref_xaction_type 
WHERE 
   xaction_type_db_id = 0 AND
   xaction_type_cd    = 'CLOSEPO';