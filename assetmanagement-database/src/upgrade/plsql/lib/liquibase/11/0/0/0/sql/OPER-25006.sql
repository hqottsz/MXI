--liquibase formatted sql

--changeSet OPER-25006:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE SD_FAULT_REFERENCE
	  (
		FAULT_REF_DB_ID    NUMBER (10) NOT NULL ,
		FAULT_REF_ID       NUMBER (10) NOT NULL ,
		FAULT_DB_ID        NUMBER (10) NOT NULL ,
		FAULT_ID           NUMBER (10) NOT NULL ,
		REP_REF_DB_ID      NUMBER (10) ,
		REP_REF_ID         NUMBER (10) ,
		DEFER_REF_DB_ID    NUMBER (10) ,
		DEFER_REF_ID       NUMBER (10) ,
		STAGE_REASON_CD    VARCHAR2 (16) ,
		STAGE_REASON_DB_ID NUMBER (10) ,
		NOTES              VARCHAR2 (4000) ,
		CURRENT_BOOL       NUMBER (1) DEFAULT 0 ,
		RSTAT_CD           NUMBER (3) NOT NULL ,
		REVISION_NO        NUMBER (10) NOT NULL ,
		CTRL_DB_ID         NUMBER (10) NOT NULL ,
		CREATION_DT        DATE NOT NULL ,
		REVISION_DT        DATE NOT NULL ,
		REVISION_DB_ID     NUMBER (10) NOT NULL ,
		REVISION_USER      VARCHAR2 (30) NOT NULL
	  )
   ');
END;
/

--changeset OPER-25006:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CHECK ( CURRENT_BOOL IN (0,1))
   ');
END;
/

--changeset OPER-25006:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CHECK ( RSTAT_CD     IN (0, 1, 2, 3))
   ');
END;
/


--changeSet OPER-25006:4 stripComments:false
COMMENT ON COLUMN SD_FAULT_REFERENCE.FAULT_REF_DB_ID
IS
  'Primary key for SD_FAULT_REFERENCE table' ;
  
--changeSet OPER-25006:5 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE.FAULT_REF_ID
IS
  'Primary key for SD_FAULT_REFERENCE table' ;
 
--changeSet OPER-25006:6 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE.FAULT_DB_ID
IS
  'FK to SD_FAULT. identifies the fault associated to the reference' ;

--changeSet OPER-25006:7 stripComments:false 
  COMMENT ON COLUMN SD_FAULT_REFERENCE.FAULT_ID
IS
  'FK to SD_FAULT. identifies the fault associated to the reference' ;
  
--changeSet OPER-25006:8 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.REP_REF_DB_ID
IS
  'FK to TASK_TASK. identifies the repair reference' ;
  
--changeSet OPER-25006:9 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.REP_REF_ID
IS
  'FK to TASK_TASK. identifies the repair reference' ;
  
--changeSet OPER-25006:10 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.DEFER_REF_DB_ID
IS
  'FK to FAIL_DEFER_REF. identifies the deferral reference' ;
  
 --changeSet OPER-25006:11 stripComments:false 
  COMMENT ON COLUMN SD_FAULT_REFERENCE.DEFER_REF_ID
IS
  'FK to FAIL_DEFER_REF. identifies the deferral reference' ;
  
--changeSet OPER-25006:12 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.CURRENT_BOOL
IS
  'Boolean indicating the latest reference selected for a fault' ;
  
--changeSet OPER-25006:13 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.RSTAT_CD
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;
  
--changeSet OPER-25006:14 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.CREATION_DT
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;
  
--changeSet OPER-25006:15 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.REVISION_DT
IS
  'The database server''s timestamp captured at the time the record was last updated.' ;
  
--changeSet OPER-25006:16 stripComments:false 
  COMMENT ON COLUMN SD_FAULT_REFERENCE.REVISION_DB_ID
IS
  'The database identifer (MIM_LOCAL_DB) captured at the time the record was last updated. Identifies where modifications are being made in distributed Maintenix.' ;
  
--changeSet OPER-25006:17 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.REVISION_USER
IS
  'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;
  
--changeSet OPER-25006:18 stripComments:false  
  COMMENT ON COLUMN SD_FAULT_REFERENCE.CTRL_DB_ID
IS
  'The identifier of the database that owns the record.' ;

--changeSet OPER-25006:19 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE.STAGE_REASON_CD
IS
  'FK to REF_FAIL_SEV. This is the deferral reason associated with the fault.' ;
  
--changeSet OPER-25006:20 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE.STAGE_REASON_DB_ID
IS
  'FK to REF_FAIL_SEV. This is the deferral reason associated with the fault.' ;

--changeSet OPER-25006:21 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE.NOTES
IS
  'Deferral notes' ;
  
--changeset OPER-25006:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_SDFAULTREF_FAULT_CURRENT ON SD_FAULT_REFERENCE
		(
		  FAULT_ID ASC ,
		  CURRENT_BOOL DESC ,
		  FAULT_DB_ID ASC
		)
   ');
END;
/

--changeset OPER-25006:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT PK_SD_FAULT_REFERENCE PRIMARY KEY ( FAULT_REF_DB_ID, FAULT_REF_ID )
   ');
END;
/


--changeSet OPER-25006:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE SD_FAULT_REFERENCE_REQUEST
	  (
		FAULT_REF_REQ_DB_ID NUMBER (10) NOT NULL ,
		FAULT_REF_REQ_ID    NUMBER (10) NOT NULL ,
		HR_DB_ID            NUMBER (10) ,
		HR_ID               NUMBER (10) ,
		REQUEST_STATUS_CD   VARCHAR2 (16) NOT NULL ,
		DATE_REQUESTED      DATE NOT NULL ,
		DATE_RESOLVED       DATE ,
		RSTAT_CD            NUMBER (3) NOT NULL ,
		REVISION_NO         NUMBER (10) NOT NULL ,
		CTRL_DB_ID          NUMBER (10) NOT NULL ,
		CREATION_DT         DATE NOT NULL ,
		REVISION_DT         DATE NOT NULL ,
		REVISION_DB_ID      NUMBER (10) NOT NULL ,
		REVISION_USER       VARCHAR2 (30) NOT NULL
	  )
   ');
END;
/

--changeset OPER-25006:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-25006:26 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.REQUEST_STATUS_CD
IS
  'Foreign key to the request''s status (PENDING, APPROVED, REJECTED, and CANCELLED).' ;

--changeSet OPER-25006:27 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.RSTAT_CD
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;

--changeSet OPER-25006:28 stripComments:false
 COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.CREATION_DT
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;

--changeSet OPER-25006:29 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.REVISION_DT
IS
  'The database server''s timestamp captured at the time the record was last updated.' ;

--changeSet OPER-25006:30 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.REVISION_DB_ID
IS
  'The database identifer (MIM_LOCAL_DB) captured at the time the record was last updated. Identifies where modifications are being made in distributed Maintenix.' ;

--changeSet OPER-25006:31 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.REVISION_USER
IS
  'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;

--changeSet OPER-25006:32 stripComments:false
  COMMENT ON COLUMN SD_FAULT_REFERENCE_REQUEST.CTRL_DB_ID
IS
  'The identifier of the database that owns the record.' ;


--changeset OPER-25006:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT PK_SD_FAULT_REFER_REQ PRIMARY KEY ( FAULT_REF_REQ_DB_ID, FAULT_REF_REQ_ID )
   ');
END;
/


--changeset OPER-25006:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_MIMDB FOREIGN KEY ( FAULT_REF_REQ_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_ORGHR FOREIGN KEY ( HR_DB_ID, HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_REFREQ_STATUS FOREIGN KEY ( REQUEST_STATUS_CD ) REFERENCES REF_REFERENCE_REQUEST_STATUS ( REFERENCE_REQUEST_STATUS_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE_REQUEST ADD CONSTRAINT FK_FAULTREQUEST_SDFAULTREF FOREIGN KEY ( FAULT_REF_REQ_DB_ID, FAULT_REF_REQ_ID ) REFERENCES SD_FAULT_REFERENCE ( FAULT_REF_DB_ID, FAULT_REF_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT "FK_SDFAULTREF_SDFAULT" FOREIGN KEY ( FAULT_DB_ID, FAULT_ID ) REFERENCES SD_FAULT ( FAULT_DB_ID, FAULT_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT FK_SDFAULTREF_FAILDEFERREF FOREIGN KEY ( DEFER_REF_DB_ID, DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( FAIL_DEFER_REF_DB_ID, FAIL_DEFER_REF_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT FK_SDFAULTREF_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT FK_SDFAULTREF_MINDB FOREIGN KEY ( FAULT_REF_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT FK_SDFAULTREF_TASKTASK FOREIGN KEY ( REP_REF_DB_ID, REP_REF_ID ) REFERENCES TASK_TASK ( TASK_DB_ID, TASK_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-25006:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT_REFERENCE ADD CONSTRAINT FK_SDFAULTREF_REFSTAGEREA FOREIGN KEY ( STAGE_REASON_DB_ID, STAGE_REASON_CD ) REFERENCES REF_STAGE_REASON ( STAGE_REASON_DB_ID, STAGE_REASON_CD ) NOT DEFERRABLE 
   ');
END;
/

--changeset OPER-25006:45 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
SELECT 'SD_FAULT_REFERENCE_SEQ', 1, 'SD_FAULT_REFERENCE', 'FAULT_REF_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'SD_FAULT_REFERENCE_SEQ');
 
--changeset OPER-25006:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('SD_FAULT_REFERENCE_SEQ', 1);
END;
/

--changeset OPER-25006:47 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
SELECT 'SD_FAULT_REFERENCE_REQUEST_SEQ', 1, 'SD_FAULT_REFERENCE_REQUEST', 'FAULT_REF_REQ_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'SD_FAULT_REFERENCE_REQUEST_SEQ');
 
--changeset OPER-25006:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('SD_FAULT_REFERENCE_REQUEST_SEQ', 1);
END;
/

--changeSet OPER-25006:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SD_FAULT_REFERENCE" BEFORE UPDATE
   ON "SD_FAULT_REFERENCE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
 
--changeSet OPER-25006:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SD_FAULT_REFERENCE" BEFORE INSERT
   ON "SD_FAULT_REFERENCE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/


--changeSet OPER-25006:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SD_FAULT_REFERENCE_REQ" BEFORE UPDATE
   ON "SD_FAULT_REFERENCE_REQUEST" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
 
--changeSet OPER-25006:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SD_FAULT_REFERENCE_REQ" BEFORE INSERT
   ON "SD_FAULT_REFERENCE_REQUEST" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeset OPER-25006:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_SDFAULTREF_DEFER_REF_ID ON SD_FAULT_REFERENCE
 	    (
          DEFER_REF_DB_ID ASC ,
          DEFER_REF_ID ASC
      )
   ');
END;
/

--changeset OPER-25006:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
    CREATE INDEX IX_SDFAULTREF_REPAIR_REF_ID ON SD_FAULT_REFERENCE
      (
        REP_REF_DB_ID ASC ,
        REP_REF_ID ASC
      )
   ');
END;
/

--changeset OPER-25006:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
    CREATE INDEX IX_SDFAULTREFREQUEST_HRID ON SD_FAULT_REFERENCE_REQUEST
      (
        HR_DB_ID ASC ,
        HR_ID ASC
      )
   ');
END;
/

--changeset OPER-25006:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
    CREATE INDEX IX_SDFAULTREF_FAULT_ID ON SD_FAULT_REFERENCE
	  (
		FAULT_DB_ID ASC ,
		FAULT_ID ASC
	  )
   ');
END;
/
