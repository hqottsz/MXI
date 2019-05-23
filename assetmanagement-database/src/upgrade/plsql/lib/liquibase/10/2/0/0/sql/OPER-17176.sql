--liquibase formatted sql

--changeSet OPER-17176:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE ORG_CREW_SHIFT_TASK
	  (
	    SCHED_DB_ID        NUMBER (10) NOT NULL ,
	    SCHED_ID           NUMBER (10) NOT NULL ,
	    CREW_DB_ID         NUMBER (10) NOT NULL ,
	    CREW_ID            NUMBER (10) NOT NULL ,
	    CREW_SHIFT_PLAN_ID NUMBER (10) NOT NULL ,
	    ALT_ID RAW (16) NOT NULL ,
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

--changeSet OPER-17176:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.index_create('
  CREATE INDEX IX_ORCRHPL_ORCRSHTASK ON ORG_CREW_SHIFT_TASK
  (
    CREW_DB_ID ASC ,
    CREW_ID ASC ,
    CREW_SHIFT_PLAN_ID ASC
  )
  LOGGING
');
END;
/

--changeSet OPER-17176:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-17176:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-17176:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-17176:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-17176:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT PK_ORG_CREW_SHIFT_TASK PRIMARY KEY ( SCHED_DB_ID, SCHED_ID, CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID )
');
END;
/

--changeSet OPER-17176:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT IX_ORGCREWSHIFTTASKALTID_UNQ UNIQUE ( ALT_ID )
');
END;
/

--changeSet OPER-17176:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_MIMDB_ORCRSHTASK FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-17176:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_MIMDB_ORCRSHTASK_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-17176:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_MIMDB_ORCRSHTASK_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-17176:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_ORCRSHPL_ORCRSHTASK FOREIGN KEY ( CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID ) REFERENCES ORG_CREW_SHIFT_PLAN ( CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID ) NOT DEFERRABLE
  ');
END;
/

--changeSet OPER-17176:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_SCHEDSTASK_ORCRSHTASK FOREIGN KEY ( SCHED_DB_ID, SCHED_ID ) REFERENCES SCHED_STASK ( SCHED_DB_ID, SCHED_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-17176:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_TASK ADD CONSTRAINT FK_MIMRSTAT_ORCRSHTASK FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-17176:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_CREW_SHIFT_TASK" BEFORE UPDATE
   ON "ORG_CREW_SHIFT_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-17176:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_CREW_SHIFT_TASK" BEFORE INSERT
   ON "ORG_CREW_SHIFT_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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