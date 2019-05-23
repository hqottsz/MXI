--liquibase formatted sql

--changeSet OPER-15719:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP ADD (REVISION_NO NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP ADD (CREATION_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP ADD (CTRL_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP_APPL_LOG ADD (REVISION_NO NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP_APPL_LOG ADD (CREATION_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_STEP_APPL_LOG ADD (CTRL_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_LABOUR_STEP ADD (REVISION_NO NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_LABOUR_STEP ADD (CREATION_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add ('
     ALTER TABLE SCHED_LABOUR_STEP ADD (CTRL_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-15719:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_STEP" BEFORE UPDATE
   ON "SCHED_STEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet OPER-15719:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_STEP" BEFORE INSERT
   ON "SCHED_STEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet OPER-15719:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_STEP_APPL_LOG" BEFORE UPDATE
   ON "SCHED_STEP_APPL_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
   MX_TRIGGER_PKG.before_update(
      :old.rstat_cd,
      :new.rstat_cd,
      :old.revision_no,
      :new.revision_no,
      :new.revision_dt,
      :new.revision_db_id,
      :new.revision_user
   );
END;
/

--changeSet OPER-15719:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_STEP_APPL_LOG" BEFORE INSERT
   ON "SCHED_STEP_APPL_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet OPER-15719:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_LABOUR_STEP" BEFORE UPDATE
   ON "SCHED_LABOUR_STEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
   MX_TRIGGER_PKG.before_update(
      :old.rstat_cd,
      :new.rstat_cd,
      :old.revision_no,
      :new.revision_no,
      :new.revision_dt,
      :new.revision_db_id,
      :new.revision_user
   );
END;
/

--changeSet OPER-15719:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_LABOUR_STEP" BEFORE INSERT
   ON "SCHED_LABOUR_STEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet OPER-15719:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_STEP SET CREATION_DB_ID = defaultDbId WHERE CREATION_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_STEP SET CTRL_DB_ID = defaultDbId WHERE CTRL_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UPDATE SCHED_STEP SET REVISION_NO = 1 WHERE REVISION_NO IS NULL;
END;
/

--changeSet OPER-15719:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_STEP_APPL_LOG SET CREATION_DB_ID = defaultDbId WHERE CREATION_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_STEP_APPL_LOG SET CTRL_DB_ID = defaultDbId WHERE CTRL_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UPDATE SCHED_STEP_APPL_LOG SET REVISION_NO = 1 WHERE REVISION_NO IS NULL;
END;
/

--changeSet OPER-15719:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_LABOUR_STEP SET CREATION_DB_ID = defaultDbId WHERE CREATION_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   defaultDbId NUMBER(10) := APPLICATION_OBJECT_PKG.getdbid;
BEGIN
	UPDATE SCHED_LABOUR_STEP SET CTRL_DB_ID = defaultDbId WHERE CTRL_DB_ID IS NULL;
END;
/

--changeSet OPER-15719:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UPDATE SCHED_LABOUR_STEP SET REVISION_NO = 1 WHERE REVISION_NO IS NULL;
END;
/

--changeSet OPER-15719:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP MODIFY (CREATION_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP MODIFY (CTRL_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP MODIFY (REVISION_NO NOT NULL)
	');
END;
/

--changeSet OPER-15719:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP MODIFY (REVISION_NO DEFAULT 1)
	');
END;
/

--changeSet OPER-15719:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP_APPL_LOG MODIFY (CREATION_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP_APPL_LOG MODIFY (CTRL_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP_APPL_LOG MODIFY (REVISION_NO NOT NULL)
	');
END;
/

--changeSet OPER-15719:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_STEP_APPL_LOG MODIFY (REVISION_NO DEFAULT 1)
	');
END;
/

--changeSet OPER-15719:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_LABOUR_STEP MODIFY (CREATION_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_LABOUR_STEP MODIFY (CTRL_DB_ID NOT NULL)
	');
END;
/

--changeSet OPER-15719:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_LABOUR_STEP MODIFY (REVISION_NO NOT NULL)
	');
END;
/

--changeSet OPER-15719:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_column_modify('
	ALTER TABLE SCHED_LABOUR_STEP MODIFY (REVISION_NO DEFAULT 1)
	');
END;
/

--changeSet OPER-15719:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP ADD CONSTRAINT FK_MIMDB_SCHEDSTEP_CREATE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_STEP ADD CONSTRAINT SCHED_STEP_CHK_CREAT CHECK (CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15719:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP ADD CONSTRAINT FK_MIMDB_SCHEDSTEP_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_STEP ADD CONSTRAINT SCHED_STEP_CHK_CTRL CHECK (CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15719:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_MIMDB_SSTEP_APPL_LOG_CREATE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT SCHED_STEP_APPL_LOG_CHK_CREAT CHECK (CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15719:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_MIMDB_SSTEP_APPL_LOG_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT SCHED_STEP_APPL_LOG_CHK_CTRL CHECK (CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15719:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_LABOUR_STEP ADD CONSTRAINT FK_MIMDB_SCHEDLBRSTEP_CREATE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_LABOUR_STEP ADD CONSTRAINT SCHED_LABOUR_STEP_CHK_CREAT CHECK (CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15719:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_LABOUR_STEP ADD CONSTRAINT FK_MIMDB_SCHEDLBRSTEP_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
   ');
END;
/

--changeSet OPER-15719:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE SCHED_LABOUR_STEP ADD CONSTRAINT SCHED_LABOUR_STEP_CHK_CTRL CHECK (CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/