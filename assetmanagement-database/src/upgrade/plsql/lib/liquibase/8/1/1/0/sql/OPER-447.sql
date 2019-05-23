--liquibase formatted sql


--changeSet OPER-447:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'SHOW_PART_NAME',
 'MXWEB',
 'Controls display of the Part Name column on the Task Details page, Task Execution tab, Part Requirements area. The default value is FALSE. Set to TRUE to display the Part Name column.',
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