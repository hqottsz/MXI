--liquibase formatted sql

--changeSet OPER-5434:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_data_pkg.action_parm_insert(
      'ACTION_ARCHIVE_ASSEMBLY_INVENTORY',
      'Permission to archive an assembly.',
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