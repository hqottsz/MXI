--liquibase formatted sql

--changeSet OPER-19718:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_LABOUR_PARM',
         'Permission to access the Labour API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP5',
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/
