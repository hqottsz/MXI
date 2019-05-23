--liquibase formatted sql


--changeSet MX-26457:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter BARCODE_SHORTCUT
**************************************************************************/
BEGIN
UTL_MIGR_DATA_PKG.config_parm_insert( 
   'BARCODE_SHORTCUT',
   'MXWEB', 
   'Indicates the barcode shortcut', 
   'GLOBAL', 
   'An uppercase alphanumeric character. No special characters are allowed.', 
   'L', 
   1, 
   'MXWEB', 
   '8.0', 
   0);
END;
/