--liquibase formatted sql

--changeSet OPER-24564:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PICK_ITEMS_FOR_DISTREQ',
      'Permission to pick items for Stock Distribution Requests.',
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


--changeSet OPER-24564:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PICK_ITEMS_FOR_DISTREQ',
   'Permission to access the Pick Items For Stock Distribution Request page that''s required to select the inventory items that are to be trasnferred.',
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


--changeSet OPER-24564:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_DISTREQ_GET_PICKED_ITEMS_INFO',
   'Permission to invoke the GetDistReqPickedItemsInfo servlet.',
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
