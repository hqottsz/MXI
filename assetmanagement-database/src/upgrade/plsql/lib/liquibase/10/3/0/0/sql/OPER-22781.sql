--liquibase formatted sql

--changeSet OPER-22781:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.trigger_drop('TIBR_INV_CHR_DATA');
	upg_migr_schema_v1_pkg.trigger_drop('TUBR_INV_CHR_DATA');
END;
/

--changeSet OPER-22781:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.trigger_drop('TIBR_INV_NUM_DATA');
	upg_migr_schema_v1_pkg.trigger_drop('TUBR_INV_NUM_DATA');
END;
/

--changeSet OPER-22781:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_drop('INV_CHR_DATA');
END;
/

--changeSet OPER-22781:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_drop('INV_NUM_DATA');
END;
/
