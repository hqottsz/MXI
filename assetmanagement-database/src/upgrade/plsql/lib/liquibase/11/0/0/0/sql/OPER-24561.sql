--liquibase formatted sql


--changeSet OPER-24561:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_DISTREQ_DETAILS',
   'Permission to access the Stock Distribution Request Details page.',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.3-SP1',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/