--liquibase formatted sql

--changeSet OPER-30733:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
      (
         'API_SHIPMENT_HISTORY_NOTE_PARM',
         'Permission to access the Shipment History Note API.',
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