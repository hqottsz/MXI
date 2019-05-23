--liquibase formatted sql
-- 
-- OPER-29530: Remove column SCHED_WP.MANUAL_USAGE_BOOL
--changeSet OPER-29530:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 

BEGIN

	upg_migr_schema_v1_pkg.table_column_drop(
		'SCHED_WP',
		'MANUAL_USAGE_BOOL'
	);
   
END;
/