--liquibase formatted sql


--changeSet MX-18256:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove DEFAULT_CREATE_PART_REQUEST  configuration parameter
BEGIN
	utl_migr_data_pkg.config_parm_delete('DEFAULT_CREATE_PART_REQUEST');
END ;
/