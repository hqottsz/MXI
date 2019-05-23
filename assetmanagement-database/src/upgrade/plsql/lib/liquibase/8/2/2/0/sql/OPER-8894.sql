--liquibase formatted sql


--changeSet OPER-8894:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ASSIGN_AIRCRAFT', 
      'Permission to assign aircraft.',
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


--changeSet OPER-8894:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_REMOVE_AIRCRAFT', 
      'Permission to remove aircraft.',
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