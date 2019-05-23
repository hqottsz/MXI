--liquibase formatted sql


--changeSet LMOC-691:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

-- Create the ref_fail_defer_ref_status table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE REF_FAIL_DEFER_REF_STATUS
      (
         DEFER_REF_STATUS_CD VARCHAR2 (16 CHAR) NOT NULL ,
         DESC_LDESC          VARCHAR2 (4000 CHAR) ,
         BITMAP_DB_ID        NUMBER (10) NOT NULL ,
         BITMAP_TAG          NUMBER (10) NOT NULL ,
         CTRL_DB_ID          NUMBER (10) NOT NULL ,
         REVISION_NO         NUMBER (10) NOT NULL,
         CREATION_DB_ID      NUMBER (10) NOT NULL,
         RSTAT_CD            NUMBER (3) NOT NULL ,
         CREATION_DT         DATE NOT NULL ,
         REVISION_DT         DATE NOT NULL ,
         REVISION_DB_ID      NUMBER (10) NOT NULL ,
         REVISION_USER       VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeset LMOC-691:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CONSTRAINT PK_REF_FAIL_DEFER_REF_STATUS PRIMARY KEY ( DEFER_REF_STATUS_CD )
   ');
END;
/

--changeset LMOC-691:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CONSTRAINT FK_DEFERREFSTATUS_REFBITMAP FOREIGN KEY ( BITMAP_DB_ID, BITMAP_TAG ) REFERENCES REF_BITMAP ( BITMAP_DB_ID, BITMAP_TAG )
   ');
END;
/

--changeset LMOC-691:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CONSTRAINT FK_DEFERREFSTATUS_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
   ');
END;
/

--changeset LMOC-691:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( BITMAP_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( BITMAP_TAG BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeset LMOC-691:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE REF_FAIL_DEFER_REF_STATUS ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet LMOC-691:11 stripComments:false
-- Insert the 0 level data
INSERT INTO ref_fail_defer_ref_status ( defer_ref_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'ACTV', 'Active', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_fail_defer_ref_status WHERE defer_ref_status_cd = 'ACTV');

--changeSet LMOC-691:12 stripComments:false
INSERT INTO ref_fail_defer_ref_status ( defer_ref_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INACTV', 'Inactive', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_fail_defer_ref_status WHERE defer_ref_status_cd = 'INACTV');

--changeset LMOC-691:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the fail_defer_ref_conflict_def table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE FAIL_DEFER_REF_CONFLICT_DEF
      (
         FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
         CONFLICT_FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
         CTRL_DB_ID     NUMBER (10) NOT NULL ,
         REVISION_NO    NUMBER (10) NOT NULL ,
         CREATION_DB_ID NUMBER (10) NOT NULL ,
         RSTAT_CD       NUMBER (3) NOT NULL ,
         CREATION_DT    DATE NOT NULL ,
         REVISION_DT    DATE NOT NULL ,
         REVISION_DB_ID NUMBER (10) NOT NULL ,
         REVISION_USER  VARCHAR2 (30) NOT NULL
      ) 
   ');
END;
/

--changeset LMOC-691:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CONSTRAINT PK_FAIL_DEFER_REF_CONF_DEF PRIMARY KEY ( FAIL_DEFER_REF_ID, CONFLICT_FAIL_DEFER_REF_ID )
   ');
END;
/

--changeset LMOC-691:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CONSTRAINT FK_CONF_FAILDEFERREF FOREIGN KEY ( CONFLICT_FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID )
   ');
END;
/

--changeset LMOC-691:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CONSTRAINT FK_CONF_FAILDEFERREF_ORIGIN FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID )
   ');
END;
/

--changeset LMOC-691:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CONSTRAINT FK_CONF_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
   ');
END;
/

--changeset LMOC-691:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeset LMOC-691:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_CONFLICT_DEF ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the fail_defer_ref_rel_def table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE FAIL_DEFER_REF_REL_DEF
      (
         FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
         REL_FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
         CTRL_DB_ID     NUMBER (10) NOT NULL ,
         REVISION_NO    NUMBER (10) NOT NULL ,
         CREATION_DB_ID NUMBER (10) NOT NULL ,
         RSTAT_CD       NUMBER (3) NOT NULL ,
         CREATION_DT    DATE NOT NULL ,
         REVISION_DT    DATE NOT NULL ,
         REVISION_DB_ID NUMBER (10) NOT NULL ,
         REVISION_USER  VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeset LMOC-691:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CONSTRAINT PK_FAIL_DEFER_REF_REL_DEF PRIMARY KEY ( FAIL_DEFER_REF_ID, REL_FAIL_DEFER_REF_ID )
   ');
END;
/

--changeset LMOC-691:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CONSTRAINT FK_REL_FAILDEFERREF FOREIGN KEY ( REL_FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID )
   ');
END;
/

--changeset LMOC-691:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CONSTRAINT FK_REL_FAILDEFERREF_ORIGIN FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID )
   ');
END;
/

--changeset LMOC-691:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CONSTRAINT FK_REL_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
   ');
END;
/

--changeset LMOC-691:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeset LMOC-691:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_REL_DEF ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeset LMOC-691:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add columns to fail_defer_ref
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD DEFER_REF_STATUS_CD VARCHAR2 (16 CHAR)
   ');
END;
/

--changeset LMOC-691:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD ASSMBL_BOM_ID RAW (16)
   ');
END;
/

--changeset LMOC-691:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD INST_SYSTEMS_QT NUMBER (2)
   ');
END;
/

--changeset LMOC-691:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD OP_SYSTEMS_QT NUMBER (2)
   ');
END;
/

--changeset LMOC-691:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD APPL_LDESC VARCHAR2 (4000)
   ');
END;
/

--changeset LMOC-691:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD RECUR_INSPECTIONS_LDESC VARCHAR2 (4000)
   ');
END;
/

--changeset LMOC-691:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD OPER_ACTIONS_LDESC VARCHAR2 (4000)
   ');
END;
/

--changeset LMOC-691:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD MAINT_ACTIONS_LDESC VARCHAR2 (4000)
   ');
END;
/

--changeset LMOC-691:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE FAIL_DEFER_REF ADD PERF_PENALTIES_LDESC VARCHAR2 (4000)
   ');
END;
/

--changeset LMOC-691:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Disable the trigger
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_FAIL_DEFER_REF DISABLE';
END;
/

--changeSet LMOC-691:41 stripComments:false
-- Update all empty columns to their defaults
UPDATE fail_defer_ref SET defer_ref_status_cd = 'ACTV' WHERE defer_ref_status_cd IS NULL;

--changeSet LMOC-691:42 stripComments:false
UPDATE fail_defer_ref SET inst_systems_qt = 0 WHERE inst_systems_qt IS NULL;

--changeSet LMOC-691:43 stripComments:false
UPDATE fail_defer_ref SET op_systems_qt = 0 WHERE op_systems_qt IS NULL;


--changeset LMOC-691:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Enable the trigger
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_FAIL_DEFER_REF ENABLE';
END;
/

--changeset LMOC-691:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the column constraints
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE FAIL_DEFER_REF MODIFY DEFER_REF_STATUS_CD VARCHAR2 (16 CHAR) DEFAULT ''ACTV'' NOT NULL
   ');
END;
/

--changeset LMOC-691:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE FAIL_DEFER_REF MODIFY INST_SYSTEMS_QT NUMBER (2) DEFAULT 0 NOT NULL
   ');
END;
/

--changeset LMOC-691:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE FAIL_DEFER_REF MODIFY OP_SYSTEMS_QT NUMBER (2) DEFAULT 0 NOT NULL
   ');
END;
/

--changeset LMOC-691:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add constraints to existing fail_defer_ref table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF ADD CONSTRAINT FK_EQPASSMBLBOM FOREIGN KEY ( ASSMBL_BOM_ID ) REFERENCES EQP_ASSMBL_BOM ( ALT_ID )
   ');
END;
/

--changeset LMOC-691:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF ADD CONSTRAINT FK_REFFAILDEFER_REFSTATUS FOREIGN KEY ( DEFER_REF_STATUS_CD ) REFERENCES REF_FAIL_DEFER_REF_STATUS ( DEFER_REF_STATUS_CD )
   ');
END;
/

--changeset LMOC-691:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the auditing triggers
CREATE OR REPLACE TRIGGER "TIBR_REF_FAIL_DEFER_REF_STAT" BEFORE INSERT
   ON "REF_FAIL_DEFER_REF_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeset LMOC-691:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_FAIL_DEFER_REF_STAT" BEFORE UPDATE
   ON "REF_FAIL_DEFER_REF_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset LMOC-691:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_CONF_DEF" BEFORE INSERT
   ON "FAIL_DEFER_REF_CONFLICT_DEF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeset LMOC-691:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_REF_CONF_DEF" BEFORE UPDATE
   ON "FAIL_DEFER_REF_CONFLICT_DEF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset LMOC-691:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_REL_DEF" BEFORE INSERT
   ON "FAIL_DEFER_REF_REL_DEF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeset LMOC-691:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_REF_REL_DEF" BEFORE UPDATE
   ON "FAIL_DEFER_REF_REL_DEF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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