--liquibase formatted sql
--changeSet OPER-29373:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_ORGANIZATION_PARM',
         'Permission to utilize the Organization API data.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - ORGANIZATION',
         '8.3-SP2',
         0,
         0
      );
END;
/