--liquibase formatted sql


--changeSet OPER-9083:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_SEARCH_ASSEMBLY_CAPABILITY_REQUEST', 
      'Permission to search assembly capabilities',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'API - MAINTENANCE',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/