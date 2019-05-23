--liquibase formatted sql
--changeSet OPER-29537:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_STOCK_LEVEL_PARM',
         'Permission to utilize the Stock Level API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - MATERIALS',
         '8.3-SP2',
         0,
         0
      );
END;
/