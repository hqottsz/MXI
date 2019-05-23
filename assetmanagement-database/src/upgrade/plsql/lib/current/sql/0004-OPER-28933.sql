--liquibase formatted sql
--Add the param API_FINANCE_ACCOUNT_PARM

--changeSet OPER-28933:1 stripComments:false
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'API_FINANCE_ACCOUNT_PARM',
      'Permission to access the Finance Account API.',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'API - FINANCE',
      '8.3-SP2',
      '0',
      '0'
   );

END;
/

