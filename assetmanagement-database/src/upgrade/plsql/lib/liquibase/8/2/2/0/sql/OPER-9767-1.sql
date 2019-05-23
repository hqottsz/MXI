--liquibase formatted sql


--changeSet OPER-9767:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************************
*
* OPER-9767 Create a mapping table for Adding Job Step Applicability Logic
*
***************************************************************************************/
BEGIN

  -- create the new mapping table
  utl_migr_schema_pkg.table_create('
   CREATE TABLE SCHED_STEP_APPL_LOG(
      SCHED_DB_ID    NUMBER (10) NOT NULL ,
      SCHED_ID       NUMBER (10) NOT NULL ,
      STEP_ID        NUMBER (10) NOT NULL ,
      LOG_ID         NUMBER (10) NOT NULL ,
      NOTES_LDESC    VARCHAR2 (4000) ,
      STEP_STATUS_CD VARCHAR2 (20) ,
      HR_DB_ID       NUMBER (10) ,
      HR_ID          NUMBER (10) ,
      RSTAT_CD       NUMBER (3) NOT NULL CHECK ( RSTAT_CD IN (0, 1, 2, 3)) ,
      CREATION_DT    DATE NOT NULL ,
      REVISION_DT    DATE NOT NULL ,
      REVISION_DB_ID NUMBER (10) NOT NULL CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295),
      REVISION_USER  VARCHAR2 (30) NOT NULL
   )
  ');

END;
/

--changeSet OPER-9767:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- PK_SCHED_STEP_APPL_LOG PK
BEGIN

  utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT PK_SCHED_STEP_APPL_LOG PRIMARY KEY ( SCHED_DB_ID, SCHED_ID, STEP_ID, LOG_ID )
  ');

END;
/


--changeSet OPER-9767:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- SCHED_STEP FK
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_SSTEP_APPL_LOG_SCHED_STEP FOREIGN KEY ( SCHED_DB_ID, SCHED_ID, STEP_ID ) REFERENCES SCHED_STEP ( SCHED_DB_ID, SCHED_ID, STEP_ID ) NOT DEFERRABLE
   ');

END;
/

--changeSet OPER-9767:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- REF_STEP_STATUS FK
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_SSTEP_APPL_LOG_REF_STATUS FOREIGN KEY ( STEP_STATUS_CD ) REFERENCES REF_STEP_STATUS ( STEP_STATUS_CD ) NOT DEFERRABLE
   ');

END;
/

--changeSet OPER-9767:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ORG_HR FK
BEGIN

  utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_SSTEP_APPL_LOG_ORG_HR FOREIGN KEY ( HR_DB_ID, HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID ) NOT DEFERRABLE
  ');

END;
/

--changeSet OPER-9767:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MIM_RSTAT FK
BEGIN

  utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_STEP_APPL_LOG ADD CONSTRAINT FK_SSTEP_APPL_LOG_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
  ');

END;
/

--changeSet OPER-9767:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_STEP_APPL_LOG" BEFORE INSERT
 ON "SCHED_STEP_APPL_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet OPER-9767:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_STEP_APPL_LOG" BEFORE UPDATE
 ON "SCHED_STEP_APPL_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet OPER-9767:9 stripComments:false
-- prepare SCHED_STEP_APPL_LOG_ID_SEQ in utl_sequence
INSERT INTO
   utl_sequence (
      sequence_cd,
      next_value,
      table_name,
      column_name,
      oracle_seq,
      utl_id
   )
SELECT
   'SCHED_STEP_APPL_LOG_ID_SEQ',
   1,
   'SCHED_STEP_APPL_LOG',
   'LOG_ID' ,
   1,
   0
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'SCHED_STEP_APPL_LOG_ID_SEQ' );


--changeSet OPER-9767:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- create sequence SCHED_STEP_APPL_LOG_ID_SEQ
BEGIN
   utl_migr_schema_pkg.sequence_create('SCHED_STEP_APPL_LOG_ID_SEQ', 1);
END;
/

--changeSet OPER-9767:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--create index IX_ORGHR_SCHEDSTEPAPPLLOG
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_ORGHR_SCHEDSTEPAPPLLOG ON SCHED_STEP_APPL_LOG
        (
		  HR_DB_ID ASC ,
		  HR_ID ASC
		)
   ');

END;
/



