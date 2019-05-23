--liquibase formatted sql

--changeSet OPER-30302:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_data_pkg.action_parm_insert(
      'ACTION_UNARCHIVE_ASSEMBLY_INVENTORY',
      'Permission to unarchive an assembly.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Supply - Inventory',
      '8.3-SP2',
      0,
      0
   );
END;
/

--changeSet OPER-30302:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_JSP_WEB_INVENTORY_UNARCHIVE_INVENTORY');
END;
/
