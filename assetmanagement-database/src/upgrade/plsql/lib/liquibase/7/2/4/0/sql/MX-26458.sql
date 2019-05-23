--liquibase formatted sql


--changeSet MX-26458:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter FLIGHT_ENTRY_COPY_ACTUAL_DT
**************************************************************************/
BEGIN
UTL_MIGR_DATA_PKG.config_parm_insert( 
   'FLIGHT_ENTRY_COPY_ACTUAL_DT',
   'MXWEB', 
   'Copies the actual departure dates to all other date fields.', 
   'GLOBAL', 
   'TRUE/FALSE', 
   'FALSE', 
   1, 
   'MXWEB', 
   '1209', 
   0);
END;
/