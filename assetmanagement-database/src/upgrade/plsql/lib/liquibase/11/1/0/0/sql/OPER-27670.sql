--liquibase formatted sql

--changeSet OPER-27670:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_REMOVE_REASON_PARM',
         'Permission to utilize the Remove Reason API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - SYSTEM',
         '8.3-SP1',
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/