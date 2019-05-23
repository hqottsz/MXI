--liquibase formatted sql

--changeSet OPER-30936:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PURCHASE_ORDER_LINE_PARM',
      'Permission to access the purchase order line API.',
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