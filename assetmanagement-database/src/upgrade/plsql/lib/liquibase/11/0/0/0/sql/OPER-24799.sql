--liquibase formatted sql

--changeSet OPER-24799:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
   ALTER TABLE SCHED_LABOUR ADD SOURCE_JOB_STOP_LABOUR_DB_ID NUMBER(10) DEFAULT NULL
');
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCHED_LABOUR.SOURCE_JOB_STOP_LABOUR_DB_ID IS ''FK to SCHED_LABOUR (recursive). When not null, represents the associated labour row that was job stopped resulting in this labour row being created.''';
END;
/
--changeSet OPER-24799:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
   ALTER TABLE SCHED_LABOUR ADD SOURCE_JOB_STOP_LABOUR_ID NUMBER(10) DEFAULT NULL
');
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCHED_LABOUR.SOURCE_JOB_STOP_LABOUR_ID IS ''FK to SCHED_LABOUR (recursive). When not null, represents the associated labour row that was job stopped resulting in this labour row being created.''';
END;
/

--changeSet OPER-24799:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE SCHED_LABOUR ADD CONSTRAINT FK_SCHEDLABOUR_SOURCE_JOB_STOP FOREIGN KEY (SOURCE_JOB_STOP_LABOUR_DB_ID,SOURCE_JOB_STOP_LABOUR_ID) REFERENCES SCHED_LABOUR (LABOUR_DB_ID,LABOUR_ID)
');
END;
/

--changeSet OPER-24799:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_SCHDLBR_SRCJOBSTOPSCHDLBR ON SCHED_LABOUR (SOURCE_JOB_STOP_LABOUR_DB_ID ASC,SOURCE_JOB_STOP_LABOUR_ID ASC)
');
END;
/
