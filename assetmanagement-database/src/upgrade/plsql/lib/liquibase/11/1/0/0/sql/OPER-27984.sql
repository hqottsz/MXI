--liquibase formatted sql

--changeSet OPER-27984:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm for adding job card steps to baseline tasks.
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_REFERENCE_REQUEST_PARM',
         'Permission to access the Reference Request API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - MAINTENANCE',
         '8.3-SP1',
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/