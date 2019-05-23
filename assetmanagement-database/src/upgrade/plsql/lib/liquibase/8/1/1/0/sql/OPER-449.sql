--liquibase formatted sql


--changeSet OPER-449:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'SHOW_EXTRA_DUE_INFO',
 'MXWEB',
 'Controls display of the Driving, Calendar, Hours, and Cycles columns under the Due column on the Inventory Details page, Open tab, Open Tasks sub-tab.',
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