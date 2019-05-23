--liquibase formatted sql


--changeSet OPER-4640-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_schema_pkg.index_create( 'CREATE INDEX ix_mv_mc ON MV_MATERIALS_REQUEST_STATUS(SCHED_DB_ID, SCHED_ID)');
END;
/  