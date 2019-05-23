--liquibase formatted sql

--changeSet OPER-21861:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add RELEASE_TO_SERVICE_BOOL column
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
      'ALTER TABLE SCHED_WP add
      (
         RELEASE_TO_SERVICE_BOOL  NUMBER (1) DEFAULT 0 NOT NULL
      )'
   );
END;
/

--changeSet OPER-21861:2 stripComments:false
COMMENT ON COLUMN SCHED_WP.RELEASE_TO_SERVICE_BOOL
IS
  'Specifies whether the work package has released the aircraft back into service or not. ';


--changeSet OPER-21861:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    upg_migr_schema_v1_pkg.table_constraint_add('
    	ALTER TABLE SCHED_WP ADD CHECK ( RELEASE_TO_SERVICE_BOOL IN (0,1))
    ');
END;
/
