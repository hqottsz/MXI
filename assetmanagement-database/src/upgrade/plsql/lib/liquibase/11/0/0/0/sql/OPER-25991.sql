--liquibase formatted sql

--changeSet OPER-25991:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm for the import sub locations permission

BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_IMPORT_SUB_LOCATIONS',
      'Permission required to import sub-locations of a SRVSTORE.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Org - Locations',
      '8.3-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER', 'MASTER')
   );

END;
/

--changeSet OPER-25991:2 stripComments:false
--updates config parm for the import sub locations permission
UPDATE
   UTL_ACTION_CONFIG_PARM
SET
   parm_desc = 'Disabled - Do Not Use || Permission required to import sub-locations of a SRVSTORE.'
WHERE
   parm_name = 'ACTION_IMPORT_SUB_LOCATIONS';


