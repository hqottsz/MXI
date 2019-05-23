--liquibase formatted sql
--changeSet OPER-29268:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_PURCHASE_ORDER_HISTORY_NOTE',
         'Permission to utilize the Purchase Order History Note API.',
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