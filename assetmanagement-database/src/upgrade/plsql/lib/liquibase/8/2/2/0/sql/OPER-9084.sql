--liquibase formatted sql


--changeSet OPER-9084:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- create table FAIL_DEFER_REF_DEGRAD_CAP
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE FAIL_DEFER_REF_DEGRAD_CAP
  (
    FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
    CAP_CD          VARCHAR2 (8) NOT NULL ,
    CAP_DB_ID       NUMBER (10) NOT NULL ,
    CAP_LEVEL_CD    VARCHAR2 (8) NOT NULL ,
    CAP_LEVEL_DB_ID NUMBER (10) NOT NULL ,
    ALT_ID RAW (16) NOT NULL ,
    RSTAT_CD       NUMBER (3) NOT NULL ,
    REVISION_NO    NUMBER (10) NOT NULL ,
    CTRL_DB_ID     NUMBER (10) NOT NULL ,
    CREATION_DT    DATE NOT NULL ,
    CREATION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_DT    DATE NOT NULL ,
    REVISION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_USER  VARCHAR2 (30) NOT NULL
  )
');
END;
/

--changeSet OPER-9084:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-9084:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-9084:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-9084:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-9084:6 stripComments:false
-- comments on FAIL_DEFER_REF_ID column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.FAIL_DEFER_REF_ID
IS
  'FK to defer ref table' ;

--changeSet OPER-9084:7 stripComments:false
-- comments on CAP_CD column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.CAP_CD
IS
  'The capability code.' ;

--changeSet OPER-9084:8 stripComments:false
-- comments on CAP_LEVEL_CD column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.CAP_LEVEL_CD
IS
  'The capability level code.' ;

--changeSet OPER-9084:9 stripComments:false
-- comments on RSTAT_CD column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.RSTAT_CD
IS
  'FK to MIM_RSTAT. A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-9084:10 stripComments:false
-- comments on REVISION_NO column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-9084:11 stripComments:false
-- comments on CTRL_DB_ID column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.CTRL_DB_ID
IS
  'The identifier of the database that owns the record.' ;

--changeSet OPER-9084:12 stripComments:false
-- comments on CREATION_DT column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.CREATION_DT
IS
  'The database server''s timestamp captured at the time the record was inserted.' ;

--changeSet OPER-9084:13 stripComments:false
-- comments on CREATION_DB_ID column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.CREATION_DB_ID
IS
  'The database identifer (MIM_LOCAL_DB) captured at the time the record was created.' ;

--changeSet OPER-9084:14 stripComments:false
-- comments on REVISION_DT column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.REVISION_DT
IS
  'The database server''s timestamp captured at the time the record was last updated.' ;

--changeSet OPER-9084:15 stripComments:false
-- comments on REVISION_DB_ID column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.REVISION_DB_ID
IS
  'The database identifer (MIM_LOCAL_DB) captured at the time the record was last updated. Identifies where modifications are being made in distributed Maintenix.' ;

--changeSet OPER-9084:16 stripComments:false
-- comments on REVISION_USER column
COMMENT ON COLUMN FAIL_DEFER_REF_DEGRAD_CAP.REVISION_USER
IS
  'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;

--changeSet OPER-9084:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add primary key for ( FAIL_DEFER_REF_ID, CAP_CD, CAP_LEVEL_CD ) combination
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT PK_FAIL_DEFER_REF_DEGRAD_CAP PRIMARY KEY ( FAIL_DEFER_REF_ID, CAP_CD, CAP_LEVEL_CD, CAP_DB_ID, CAP_LEVEL_DB_ID )
');
END;
/

--changeSet OPER-9084:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add unique to ALT_ID
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT IX_FAILDEFERREFDEGRADCAPID_UNQ UNIQUE ( ALT_ID )
');
END;
/

--changeSet OPER-9084:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for FAIL_DEFER_REF_ID
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT FK_FAILDEFERREF_DEGRADCAP FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID ) ON DELETE CASCADE NOT DEFERRABLE
');
END;
/

--changeSet OPER-9084:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for CTRL_DB_ID
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT FK_MIMDB_DEGRADCAP FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-9084:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for RSTAT_CD
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT FK_MIMRSTAT_DEGRADCAP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-9084:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for CAP_LEVEL_DB_ID
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT FK_REFACFTCAPLEVEL_DEGRADCAP FOREIGN KEY ( CAP_LEVEL_DB_ID, CAP_LEVEL_CD, CAP_DB_ID, CAP_CD ) REFERENCES REF_ACFT_CAP_LEVEL ( ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-9084:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for CAP_DB_ID
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT FK_REFACFTCAP_DEGRADCAP FOREIGN KEY ( CAP_DB_ID, CAP_CD ) REFERENCES REF_ACFT_CAP ( ACFT_CAP_DB_ID, ACFT_CAP_CD ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-9084:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_DEGRAD_CAP" BEFORE INSERT
  ON "FAIL_DEFER_REF_DEGRAD_CAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-9084:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_REF_DEGRAD_CAP" BEFORE UPDATE
  ON "FAIL_DEFER_REF_DEGRAD_CAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
 MX_TRIGGER_PKG.before_update(
   :old.rstat_cd,
   :new.rstat_cd,
   :old.revision_no,
   :new.revision_no,
   :new.revision_dt,
   :new.revision_db_id,
   :new.revision_user
    );
end;
/

--changeSet OPER-9084:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_DEGRAD_CAP_ALT_ID" BEFORE INSERT
   ON "FAIL_DEFER_REF_DEGRAD_CAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/