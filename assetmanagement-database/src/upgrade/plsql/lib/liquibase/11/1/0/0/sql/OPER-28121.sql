--liquibase formatted sql

--changeSet OPER-28121:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_INTERNAL_LABOURS_ACCESS', 
      'Permission to access the internal rest api for labours.', 
      'TRUE/FALSE', 
      'TRUE', 
      1, 
      'Maint - Tasks', 
      '8.3-SP1', 
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/
