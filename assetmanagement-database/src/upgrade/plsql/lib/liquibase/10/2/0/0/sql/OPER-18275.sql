--liquibase formatted sql

--changeSet OPER-18275:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE TASK_STEP_SKILL
      (
		TASK_DB_ID         NUMBER (10) NOT NULL ,
		TASK_ID            NUMBER (10) NOT NULL ,
		STEP_ID            NUMBER (10) NOT NULL ,
		LABOUR_SKILL_DB_ID NUMBER (10) NOT NULL ,
		LABOUR_SKILL_CD    VARCHAR2 (8) NOT NULL ,
		INSP_BOOL          NUMBER (1) NOT NULL ,
		STEP_SKILL_ID RAW (16) NOT NULL ,
		RSTAT_CD       NUMBER (3) NOT NULL ,
		CREATION_DT    DATE NOT NULL ,
		CREATION_DB_ID NUMBER (10) NOT NULL ,
		CTRL_DB_ID     NUMBER (10) NOT NULL ,
		REVISION_DT    DATE NOT NULL ,
		REVISION_NO    NUMBER (10) NOT NULL ,
		REVISION_DB_ID NUMBER (10) NOT NULL ,
		REVISION_USER  VARCHAR2 (30) NOT NULL
      ) 
   ');
END;
/

--changeSet OPER-18275:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-18275:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-18275:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-18275:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CHECK ( REVISION_NO BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-18275:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-18275:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
     CREATE INDEX IX_TSKSTEPSKILL_TASKSTEP ON TASK_STEP_SKILL
       (
	     TASK_DB_ID ASC ,
	     TASK_ID ASC ,
	     STEP_ID ASC
       )
   ');
END;
/   

--changeSet OPER-18275:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
     CREATE INDEX IX_TSKLBRLIST_TSKSTEPSKILL ON TASK_STEP_SKILL
       (
         TASK_DB_ID ASC ,
         TASK_ID ASC ,
         LABOUR_SKILL_DB_ID ASC ,
         LABOUR_SKILL_CD ASC
       )
   ');
END;
/   
   
--changeSet OPER-19226:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT UK_TASKSTEPSKILL_STEPSKILLID UNIQUE ( STEP_SKILL_ID )
   ');
END;
/

--changeSet OPER-19226:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT PK_TASK_STEP_SKILL PRIMARY KEY ( TASK_DB_ID, TASK_ID, STEP_ID, LABOUR_SKILL_DB_ID, LABOUR_SKILL_CD )
');
END;
/

--changeSet OPER-18275:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_MIMDB_TSKSTEPSKILLCREATE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/


--changeSet OPER-18275:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_MIMDB_TSKSTEPSKILLREV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-18275:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_MIMDB_TSKSTEPSKILLCTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-18275:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_MIMRSTAT_TASKSTEPSKILL FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE
');
END;
/

--changeSet OPER-18275:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_TSKLBRLIST_TSKSTEPSKILL FOREIGN KEY ( TASK_DB_ID, TASK_ID, LABOUR_SKILL_DB_ID, LABOUR_SKILL_CD ) REFERENCES TASK_LABOUR_LIST ( TASK_DB_ID, TASK_ID, LABOUR_SKILL_DB_ID, LABOUR_SKILL_CD ) DEFERRABLE
');
END;
/

--changeSet OPER-19226:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE TASK_STEP_SKILL ADD CONSTRAINT FK_TSKSTEPSKILL_TASKSTEP FOREIGN KEY ( TASK_DB_ID, TASK_ID, STEP_ID ) REFERENCES TASK_STEP ( TASK_DB_ID, TASK_ID, STEP_ID ) NOT DEFERRABLE 
');
END;
/

--changeSet OPER-18275:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TASK_STEP_SKILL" BEFORE UPDATE
   ON "TASK_STEP_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-18275:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_STEP_SKILL" BEFORE INSERT
   ON "TASK_STEP_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
--changeSet OPER-18275:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_STEP_SKILL_ID" BEFORE INSERT
   ON "TASK_STEP_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.STEP_SKILL_ID IS NULL THEN
     :NEW.STEP_SKILL_ID := mx_key_pkg.new_uuid();
  END IF;
END;
/
