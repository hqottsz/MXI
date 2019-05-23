--liquibase formatted sql

--changeSet OPER-30165:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
      (
         'API_REF_TERM_PARM',
         'Permission to utilize the Ref Term Search APIs.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - SYSTEM',
         '8.3-SP2',
         0,
         0
      );
END;
/


