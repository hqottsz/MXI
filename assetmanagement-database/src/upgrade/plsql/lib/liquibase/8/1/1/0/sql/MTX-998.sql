--liquibase formatted sql


--changeSet MTX-998:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'ENFORCE_MIN_PO_QTY',
 'LOGIC',
 'When set to true, will automatically set purchase order quantity for new orders to the minimum quantity defined in the vendor prices.',
 'GLOBAL',
 'TRUE/FALSE',
 'FALSE',
 1,
 'Core Logic',
 '8.1-SP2',
 0
);
 
END;
/