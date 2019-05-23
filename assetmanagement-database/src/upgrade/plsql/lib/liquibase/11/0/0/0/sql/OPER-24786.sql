--liquibase formatted sql

--changeSet OPER-24786:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN
 
   utl_migr_data_pkg.action_parm_insert(
      'API_FLIGHT_MEASUREMENT_PARM',
      'Permission to access the FlightMeasurementRequirement REST API.',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'API - MATERIALS',
      '8.3-SP1',
      '0',
      '0',
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );

END;
/