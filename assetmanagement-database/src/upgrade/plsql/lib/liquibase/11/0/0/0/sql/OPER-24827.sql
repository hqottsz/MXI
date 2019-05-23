--liquibase formatted sql

--changeSet OPER-24827:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--insert new action config parm
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PART_CHANGE_UNIT_OF_MEASURE',
      'Permission to change unit of measure.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Parts - Part Numbers',
      '8.3-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/