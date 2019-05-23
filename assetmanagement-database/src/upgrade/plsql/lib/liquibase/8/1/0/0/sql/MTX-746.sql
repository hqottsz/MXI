--liquibase formatted sql


--changeSet MTX-746:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_REMOVE_PART_REQUIREMENT_FOR_ISSUED_PART_REQUEST',
      'Permission to remove a part requirement with an issued part request.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Supply - Part Requests',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/