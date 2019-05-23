--liquibase formatted sql


--changeSet OPER-450:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'CHECK_DUPLICATE_SERIAL_NO',
 'MXWEB',
 'Checks for serial numbers similar to the one being created.',
 'GLOBAL',
 'TRUE/FALSE',
 'FALSE',
 1,
 'MXWEB',
 '8.1-SP2',
 0
);
 
END;
/