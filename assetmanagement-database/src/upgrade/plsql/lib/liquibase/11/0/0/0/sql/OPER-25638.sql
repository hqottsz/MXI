--liquibase formatted sql

--changeSet OPER-25638:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'API_AUTHORIZE_REJECT_REFERENCE_REQUEST',
      'Permission to authorize or reject a reference request',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'API - MAINTENANCE',
      '8.3-SP1',
      '0',
      '0',
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );

END;
/


--changeSet OPER-25638:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment copy the existing settings and child records from the old action parm to the new action parm
BEGIN

   utl_migr_data_pkg.action_parm_copy(
      'API_AUTHORIZE_REJECT_REFERENCE_REQUEST', -- new parameter name
      'API_AUTHORIZE_REJECT_DEFERRAL_REQUEST'   -- old parameter name
   );

END;
/


--changeSet OPER-25638:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete old config parm
BEGIN

   utl_migr_data_pkg.action_parm_delete(
      'API_AUTHORIZE_REJECT_DEFERRAL_REQUEST'
   );

END;
/
