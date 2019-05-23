--liquibase formatted sql


--changeSet MX-26591:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*****************************************************************************
 * Create new actions configurator parameter 
 *****************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_VALIDATE_INPUT',
      'Permission to Validate Input for the Configurator.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Configurator', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/

--changeSet MX-26591:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_IMPORT_TO_MAINTENIX',
      'Permission to Import to Maintenix for the Configurator.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Configurator', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/

--changeSet MX-26591:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_VIEW_REPORT',
      'Permission to View Report for the Configurator.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Configurator', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/

--changeSet MX-26591:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_EXPORT_TO_STAGING_AREA',
      'Permission to Export to Staging Area for the Configurator.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Configurator', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/

--changeSet MX-26591:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_DELETE_SELECTED',
      'Permission to Delete Selected for the Configurator.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Configurator', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/  