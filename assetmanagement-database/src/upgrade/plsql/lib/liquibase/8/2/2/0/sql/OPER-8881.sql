--liquibase formatted sql


--changeSet OPER-8881:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_REMOVE_AIRCRAFT_GROUP', 
      'Permission to remove an aircraft group',
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