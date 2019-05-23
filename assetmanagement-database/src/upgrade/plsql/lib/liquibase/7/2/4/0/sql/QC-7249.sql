--liquibase formatted sql


--changeSet QC-7249:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_delete(
 'sShowButtonText'
);
 
END;
/