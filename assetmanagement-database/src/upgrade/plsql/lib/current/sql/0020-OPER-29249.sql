--liquibase formatted sql

--changeset OPER-29249:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column SCHED_STEP.TASK_DB_ID
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('
      Alter table SCHED_STEP add (
         TASK_DB_ID Number(10,0)
      )
   ');
END;
/

--changeset OPER-29249:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column SCHED_STEP.TASK_ID
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('
      Alter table SCHED_STEP add (
         TASK_ID Number(10,0)
      )
   ');
END;
/

--changeset OPER-29249:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column SCHED_STEP.TASK_STEP_ID
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('
      Alter table SCHED_STEP add (
         TASK_STEP_ID Number(10,0)
      )
   ');
END;
/

--changeset OPER-29249:4 stripComments:false
COMMENT ON COLUMN SCHED_STEP.TASK_DB_ID IS 'Part of the key to the task_step table that indicates which baseline step this step was created from.' ;
  
--changeset OPER-29249:5 stripComments:false  
COMMENT ON COLUMN SCHED_STEP.TASK_ID IS 'Part of the key to the task_step table that indicates which baseline step this step was created from.' ;

--changeset OPER-29249:6 stripComments:false
COMMENT ON COLUMN SCHED_STEP.TASK_STEP_ID IS 'Part of the key to the task_step table that indicates which baseline step this step was created from.' ;

--changeset OPER-29249:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_TASKSTEP_SCHEDSTEP to table SCHED_STEP
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP ADD CONSTRAINT FK_TASKSTEP_SCHEDSTEP FOREIGN KEY ( TASK_DB_ID, TASK_ID, TASK_STEP_ID ) REFERENCES TASK_STEP ( TASK_DB_ID, TASK_ID, STEP_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-29249:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_TASK_STEP_SCHEDSTEP
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_TASKSTEP_SCHEDSTEP ON SCHED_STEP (TASK_DB_ID ASC, TASK_ID ASC, TASK_STEP_ID ASC)
   ');
END;
/