--liquibase formatted sql

--changeSet OPER-30398:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_data_pkg.action_parm_insert(
      'ACTION_VIEW_PO_INVOICE_DETAILS',
      'Permission to view the PO invoice details.',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Purchasing - Invoices',
      '8.3-SP2',
      0,
      0
   );
END;
/

--changeSet OPER-30398:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_data_pkg.action_parm_insert(
      'ACTION_VIEW_ACCOUNT_DETAILS',
      'Permission to view account details.',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Org - Financials',
      '8.3-SP2',
      0,
      0
   );
END;
/

--changeSet OPER-30398:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_data_pkg.action_parm_insert(
      'ACTION_VIEW_TRANSACTION_DETAILS',
      'Permission to view transaction details.',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Org - Financials',
      '8.3-SP2',
      0,
      0
   );
END;
/
