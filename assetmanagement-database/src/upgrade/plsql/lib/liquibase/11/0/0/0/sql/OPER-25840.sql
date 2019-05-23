--liquibase formatted sql

--changeSet OPER-25840:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_VALIDATE_DISTREQ_BARCODE',
   'Permission to invoke the ValidateStockDistReqBarcode servlet.',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.3-SP1',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/