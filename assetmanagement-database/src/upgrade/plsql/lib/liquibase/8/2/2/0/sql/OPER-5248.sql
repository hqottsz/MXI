--liquibase formatted sql


--changeSet OPER-5248:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
 	      'ACTION_EDIT_CURRENT_CAPABILITY_LEVEL',
 	      'Permission to update current capability levels to an aircraft.' ,
 	      'TRUE/FALSE',
 	      'FALSE',
 	      1,
	      'Aircraft - Capabilities',
 	      '8.2-SP3',
 	      0,
 	      0,
              UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
 	   );
END;
/