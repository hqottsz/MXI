--liquibase formatted sql


--changeSet OPER-23190:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   upg_migr_schema_v1_pkg.index_drop('IX_USGUSREC_MIMDB');
   upg_migr_schema_v1_pkg.index_drop('IX_USGUSDATA_MIMDB_FK');

END;
/
