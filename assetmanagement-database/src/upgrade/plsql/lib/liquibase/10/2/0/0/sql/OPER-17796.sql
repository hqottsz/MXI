--liquibase formatted sql

--changeSet OPER-17796:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add create accumulated action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
       'ACTION_CREATE_ACCUMULATED_PARMS',
       'Permission to create accumulated parameters.',
       'TRUE/FALSE',
       'FALSE',
       1,
       'Assembly - Usage Definitions',
       '8.2-SP5',
       0,
       0,
       UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/
