--liquibase formatted sql
-- changeSet OPER-30083:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_data_pkg.config_parm_delete(
		'ENABLE_CACHE_CONTROL_HEADER'
	);
END;
/
