--liquibase formatted sql


--changeSet OPER-5277:1 stripComments:false
-- add a new session parameter for the Unserviceable Staging Tab to filter on USSTG locations
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sUSSTGShowUSSTGLocations', 'SESSION','Whether to show items at USSTG locations on the Unserviceable Staging to-do list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGShowUSSTGLocations' );   
   
--changeSet OPER-5277:2 stripComments:false
-- add a new session parameter for the Unserviceable Staging Tab to filter on other, non-USSTG locations
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sUSSTGShowOtherLocations', 'SESSION','Whether to show items at non-USSTG locations on the Unserviceable Staging to-do list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGShowOtherLocations' );  

--changeSet OPER-5277:3 stripComments:false
-- add a new session parameter for the Unserviceable Staging Tab to filter for inventory
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sUSSTGShowInventory', 'SESSION','Whether to show inventory items on the Unserviceable Staging to-do list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGShowInventory' );  


--changeSet OPER-5277:4 stripComments:false
-- add a new session parameter for the Unserviceable Staging Tab to filter for tools
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sUSSTGShowTools', 'SESSION','Whether to show tools on the Unserviceable Staging to-do list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sUSSTGShowTools' );  
      
--changeSet OPER-5277:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_insert(
      'sUSSTGShowOnlyVendorOwnedParts',
      'SESSION',
      'Whether to show only vendor owned parts on the Unserviceable Staging todo list.',
      'USER',
      'Number',
      0,
      0,
      'SESSION',
      '8.2-SP3',
      0
   );

END;
/
--changeSet OPER-5277:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment copy the existing settings and child records from the old confirm parm to the new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_copy(
      'sUSSTGShowOnlyVendorOwnedParts', -- parameter name
      'SESSION', -- parameter type
      'USER', -- configuration type (GlOBAL or USER)
      'sShowOnlyVendorOwnedParts' -- the parameter from which we are migrating
   );
 
END;
/

--changeSet OPER-5277:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete old config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_delete(
      'sShowOnlyVendorOwnedParts' -- parameter name
   );
 
END;
/   