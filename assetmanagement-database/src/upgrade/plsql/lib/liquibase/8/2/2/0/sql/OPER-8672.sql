--liquibase formatted sql


--changeSet OPER-8672:1 stripComments:false
--An action config parm to add permission for ACTION_INSPECT_AS_UNSERVICEABLE

BEGIN
   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INSPECT_AS_UNSERVICEABLE', 
   'Permission to inspect an Inventory item as unserviceable',
   'TRUE/FALSE',    
   'FALSE',  
   1, 
   'Supply - Inventory', 
   '8.2-SP3', 
   0, 
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/