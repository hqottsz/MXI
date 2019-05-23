--liquibase formatted sql


--changeSet OPER-2239:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'ENFORCE_LAST_LABOUR_REQ_ESIG',
 'LOGIC',
 'Controls enforce the E-signature requirement when a user is finishing the last labor requirement in a task.',
 'GLOBAL',
 'TRUE/FALSE',
 'FALSE',
 1,
 'Esigner',
 '8.2',
 0
);
 
END;
/