--liquibase formatted sql


--changeSet OPER-5244:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
 	      'ACTION_REMOVE_CAPABILITIES',
 	      'Permission to remove capabilities from an assembly.' ,
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