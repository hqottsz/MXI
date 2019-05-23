--liquibase formatted sql

--changeSet OPER-26250:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PRINT_DISTREQ_PICK_LIST',
      'Permission to see the Print Pick List button that''s required to print the pick list of items that must be collected for a stock distribution request.',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'Supply - Transfers',
      '8.3-SP1',
      '0',
      '0',
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );

END;
/


--changeSet OPER-26250:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_DISTREQ_PICK_LIST_PDF',
   'Permission to invoke the PrintDistReqPickListPdf servlet.',
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

