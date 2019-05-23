--liquibase formatted sql

--changeSet OPER-18417:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_add('
   ALTER TABLE SCHED_LABOUR_STEP ADD STEP_SKILL_ID RAW (16)
   ');
END;
/

--changeSet OPER-18417:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
     CREATE INDEX IX_SCHEDLBRSTEP_TASKSTEPSKILL ON SCHED_LABOUR_STEP
       (
         STEP_SKILL_ID ASC
       )
   ');
END;
/

--changeSet OPER-18417:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 
BEGIN
  utl_migr_schema_pkg.table_constraint_add(' 
     ALTER TABLE SCHED_LABOUR_STEP ADD CONSTRAINT FK_SCHEDLBRSTEP_TASKSTEPSKILL FOREIGN KEY ( STEP_SKILL_ID ) REFERENCES TASK_STEP_SKILL ( STEP_SKILL_ID ) NOT DEFERRABLE
  ');
END;
/  
