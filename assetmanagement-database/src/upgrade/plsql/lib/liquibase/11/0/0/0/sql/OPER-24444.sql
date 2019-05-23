--liquibase formatted sql

--changeSet OPER-24444:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_create('
		CREATE TABLE TASK_REP_REF
		  (
		    TASK_ID                NUMBER (10) NOT NULL ,
		    TASK_DB_ID             NUMBER (10) NOT NULL ,
		    MOC_APPROVAL_BOOL      NUMBER (1) DEFAULT 0 NOT NULL ,
		    DAMAGE_RECORD_BOOL     NUMBER (1) DEFAULT 1 NOT NULL ,
		    DAMAGED_COMPONENT_BOOL NUMBER (1) DEFAULT 0 NOT NULL ,
		    OPS_RESTRICTIONS_LDESC VARCHAR2 (4000) ,
		    PERF_PENALTIES_LDESC   VARCHAR2 (4000) ,
		    RSTAT_CD               NUMBER (3) NOT NULL ,
		    REVISION_NO            NUMBER (10) NOT NULL ,
		    CTRL_DB_ID             NUMBER (10) NOT NULL ,
		    CREATION_DT            DATE NOT NULL ,
		    REVISION_DT            DATE NOT NULL ,
		    REVISION_DB_ID         NUMBER (10) NOT NULL ,
		    REVISION_USER          VARCHAR2 (30) NOT NULL
		  )
	');
END;
/

--changeSet OPER-24444:2 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.TASK_ID
IS
  'FK to task_task table.' ;

--changeSet OPER-24444:3 stripComments:false
COMMENT ON COLUMN TASK_REP_REF.TASK_DB_ID
IS
  'FK to task_task table.' ;

--changeSet OPER-24444:4 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.MOC_APPROVAL_BOOL
IS
  'Boolean indicating if the requirement needs to request MOC authorization.' ;

--changeSet OPER-24444:5 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.DAMAGE_RECORD_BOOL
IS
  'Boolean indicating if a damage record is required.' ;

--changeSet OPER-24444:6 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.DAMAGED_COMPONENT_BOOL
IS
  'Boolean indicating if the damage record is needed for a component or an aircraft. It''s set to 1 for a component and 0 for an aircraft.' ;

--changeSet OPER-24444:7 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.OPS_RESTRICTIONS_LDESC
IS
  'This column represents the description of operational restrictions on an aircraft if this repair reference has been applied.' ;

--changeSet OPER-24444:8 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.PERF_PENALTIES_LDESC
IS
  'This column represents the description of performance penalties on an aircraft if this repair reference has been applied.' ;

--changeSet OPER-24444:9 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-24444:10 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-24444:11 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.CTRL_DB_ID
IS
  'The identifier of the database that owns the record.' ;

--changeSet OPER-24444:12 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.CREATION_DT
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;

--changeSet OPER-24444:13 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.REVISION_DT
IS
  'The database server''s timestamp captured at the time the record was last updated.' ;

--changeSet OPER-24444:14 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.REVISION_DB_ID
IS
  'The database identifer (MIM_LOCAL_DB) captured at the time the record was last updated. Identifies where modifications are being made in distributed IFS Maintenix.' ;

--changeSet OPER-24444:15 stripComments:false
  COMMENT ON COLUMN TASK_REP_REF.REVISION_USER
IS
  'The user that last modified the record. The user is either a) the user that logged into IFS Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;

--changeSet OPER-24444:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CONSTRAINT PK_TASK_REP_REF PRIMARY KEY ( TASK_ID, TASK_DB_ID )
');
END;
/

--changeSet OPER-24444:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CHECK ( MOC_APPROVAL_BOOL      IN (0, 1))
');
END;
/

--changeSet OPER-24444:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CHECK ( DAMAGE_RECORD_BOOL     IN (0, 1))
');

END;
/

--changeSet OPER-24444:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CHECK ( DAMAGED_COMPONENT_BOOL IN (0, 1))
');

END;
/

--changeSet OPER-24444:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CHECK ( RSTAT_CD               IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-24444:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE TASK_REP_REF ADD CONSTRAINT FK_TASK_REP_REF_TASK_TASK FOREIGN KEY ( TASK_DB_ID, TASK_ID ) REFERENCES TASK_TASK ( TASK_DB_ID, TASK_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-24444:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
  ALTER TABLE TASK_REP_REF ADD CONSTRAINT FK_TASK_REP_REF_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-24444:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
  ALTER TABLE TASK_REP_REF ADD CONSTRAINT FK_TASK_REP_REF_MIM_DB FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-24444:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
  ALTER TABLE TASK_REP_REF ADD CONSTRAINT FK_TASK_REP_REF_MIM_DB_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-24444:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TIBR_TASK_REP_REF
CREATE OR REPLACE TRIGGER "TIBR_TASK_REP_REF" BEFORE INSERT
   ON "TASK_REP_REF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
	:new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/

--changeSet OPER-24444:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TUBR_TASK_REP_REF
CREATE OR REPLACE TRIGGER "TUBR_TASK_REP_REF" BEFORE UPDATE
   ON "TASK_REP_REF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/