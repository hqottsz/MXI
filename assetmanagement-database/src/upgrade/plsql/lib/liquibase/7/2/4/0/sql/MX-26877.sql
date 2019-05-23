--liquibase formatted sql


--changeSet MX-26877:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************************
 * Create new configuration parameterS SHOW_PART_AVAILABILITY, SHOW_PART_BIN_LEVELS
 * SHOW_LOCATION_STOCK_LEVELS, SHOW_LOCATION_BIN_LEVELS, SHOW_STOCK_PAGE, 
 * SHOW_AUTHORITY_PAGE
 **********************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_PART_AVAILABILITY', 
      'SECURED_RESOURCE',
      'Permission to access availability in part details page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Parts - Part Numbers',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_PART_BIN_LEVELS', 
      'SECURED_RESOURCE',
      'Permission to access bin levels in part details page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Parts - Part Numbers',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_LOCATION_STOCK_LEVELS', 
      'SECURED_RESOURCE',
      'Permission to access stock levels in location details page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Org - Locations',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_LOCATION_BIN_LEVELS', 
      'SECURED_RESOURCE',
      'Permission to access bin levels in location details page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Org - Locations',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_STOCK_PAGE', 
      'SECURED_RESOURCE',
      'Permission to access stock page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Parts - Stock Numbers',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  UTL_MIGR_DATA_PKG.config_parm_insert(
      'SHOW_AUTHORITY_PAGE', 
      'SECURED_RESOURCE',
      'Permission to access authority page.',
      'USER',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Org - Authorities',
      '8.0',
      0
   );
END;
/

--changeSet MX-26877:7 stripComments:false
UPDATE UTL_CONFIG_PARM 
SET DEFAULT_VALUE = 'FALSE' 
WHERE (PARM_NAME = 'SHOW_PART_AVAILABILITY'     OR 
       PARM_NAME = 'SHOW_PART_BIN_LEVELS'       OR
       PARM_NAME = 'SHOW_LOCATION_STOCK_LEVELS' OR
       PARM_NAME = 'SHOW_LOCATION_BIN_LEVELS'   OR
       PARM_NAME = 'SHOW_STOCK_PAGE'            OR
       PARM_NAME = 'SHOW_AUTHORITY_PAGE'        
       );    