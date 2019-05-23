--liquibase formatted sql


--changeSet OPER-18432:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert(
         'ACTION_SHOW_ASSEMBLY_SENSITIVITIES_TAB', 
         'Permission to see an assembly''s Sensitivities Tab.' ,
         'TRUE/FALSE', 
         'FALSE', 
         1, 
         'Assembly - Sensitivities', 
         '8.2-SP5', 
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
         );
   END;
/


--changeSet OPER-18432:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
         'ACTION_ASSIGN_ASSEMBLY_SENSITIVITIES',
         'Permission to assign sensitivities to an assembly.' ,
         'TRUE/FALSE',
         'FALSE',
         1,
         'Assembly - Sensitivities',
         '8.2-SP5',
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/