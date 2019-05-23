--liquibase formatted sql


--changeSet OPER-8880:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_EDIT_AIRCRAFT_GROUP_DETAILS', 
      'Permission to edit aircraft group details.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Aircraft Grouping',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/