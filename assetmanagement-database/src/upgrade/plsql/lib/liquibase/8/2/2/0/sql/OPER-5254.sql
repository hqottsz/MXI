--liquibase formatted sql


--changeSet OPER-5254:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
 	   utl_migr_data_pkg.action_parm_insert(
 	      'ACTION_SHOW_CAPABILITIES_TAB',
 	      'Determines whether a user is allowed to see the Capabilities Tab.' ,
 	      'TRUE/FALSE',
 	      'FALSE',
 	      1,
 	      'Assembly - Capabilities',
 	      '8.2-SP3',
 	      0,
 	      0,
 	      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
 	      );
 	END;
/