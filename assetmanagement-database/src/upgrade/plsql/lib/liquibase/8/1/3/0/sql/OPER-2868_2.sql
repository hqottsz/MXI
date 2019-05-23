--liquibase formatted sql


--changeSet OPER-2868_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'MESSAGE_INVENTORY_IS_NOT_INSTALLED', 
 'SECURED_RESOURCE',        
 'Permission to allow user to accept a warning when the inventory to be issued is different than the one installed on the task.', 
 'USER',                    
 'TRUE/FALSE',              
 'false',                   
 1,                         
 'Supply - Inventory',         
 '8.2',                     
 0                         
);
 
END;
/