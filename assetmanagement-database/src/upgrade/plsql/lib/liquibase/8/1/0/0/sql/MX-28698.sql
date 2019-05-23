--liquibase formatted sql


--changeSet MX-28698:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Role based security parameter to control referece document on
* the Edit Inventory Step 4: Task List page
****************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_REF_DOC_EDIT_INVENTORY_TASK_LIST', 
      'SECURED_RESOURCE',
      'Permission to show reference document task definition on the Edit Inventory Step 4: Task List page.',
      'USER',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Supply - Inventory',
      '8.1-SP1',
      0
   );
END;
/