--liquibase formatted sql

--changeSet OPER-24567:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'ACTION_CANCEL_DISTREQ',
      'Permission to see the Cancel Request button that''s required to cancel the stock distribution request.',
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

