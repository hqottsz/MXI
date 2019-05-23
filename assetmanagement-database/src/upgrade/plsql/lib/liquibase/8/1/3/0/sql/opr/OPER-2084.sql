--liquibase formatted sql


--changeSet OPER-2084:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'OPEN_TASKS_DUE_USAGES',
 'LOGIC',
 'DataTypes to display on the Open Tasks list.',
 'GLOBAL',
 'valid data_type_pk',
 '0:1,0:10',
 1,
 'MXWEB',
 '8.1-SP4',
 0
);
 
END;
/