--liquibase formatted sql

--changeSet OPER-22985:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_QUANTITY_UNIT_PARM',
         'Permission to access the quantity unit refterms REST API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - SYSTEM',
         '8.3',
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/