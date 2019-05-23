--liquibase formatted sql

--changeSet OPER-9785:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add Deferral Reference Deadline
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_create('
      CREATE TABLE FAIL_DEFER_REF_DEAD
      (
         DEFER_REF_DEAD_ID RAW (16) NOT NULL ,
         DATA_TYPE_DB_ID   NUMBER (10) NOT NULL ,
         DATA_TYPE_ID      NUMBER (10) NOT NULL ,
         FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
         DEAD_QT           FLOAT NOT NULL ,
         CTRL_DB_ID        NUMBER (10) NOT NULL ,
         REVISION_NO       NUMBER (10) NOT NULL ,
         CREATION_DB_ID    NUMBER (10) NOT NULL ,
         RSTAT_CD          NUMBER (3) NOT NULL ,
         CREATION_DT       DATE NOT NULL ,
         REVISION_DT       DATE NOT NULL ,
         REVISION_DB_ID    NUMBER (10) NOT NULL ,
         REVISION_USER     VARCHAR2 (30) NOT NULL
      ) 
   ');

END;
/

--changeSet OPER-9785:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraint on ctrl_db_id
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-9785:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraint on ctrl_db_id
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-9785:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraint on revision_db_id
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CHECK ( REVISION_DB_ID  BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-9785:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraint on rstat code
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');

END;
/

--changeSet OPER-9785:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Index on deferral reference table's primary key
BEGIN

   UTL_MIGR_SCHEMA_PKG.index_create('
      CREATE INDEX IX_FAILDEFERREF_DEFERREFDEAD ON FAIL_DEFER_REF_DEAD
      (
         FAIL_DEFER_REF_ID ASC
      )
   ');

END;
/

--changeSet OPER-9785:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Index on foreign key for MIM_DATA_TYPE table
BEGIN

   UTL_MIGR_SCHEMA_PKG.index_create('
      CREATE INDEX IX_MIMDATATYPE_DEFERREFDEAD ON FAIL_DEFER_REF_DEAD
      (
         DATA_TYPE_DB_ID ASC ,
         DATA_TYPE_ID ASC
      )
   ');

END;
/

--changeSet OPER-9785:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add primary key to deferral reference deadline table
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CONSTRAINT PK_FAIL_DEFER_REF_DEAD PRIMARY KEY ( DEFER_REF_DEAD_ID )
   ');

END;
/

--changeSet OPER-9785:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add unique constraint on foreign key combination
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CONSTRAINT IX_FAILDEFERREF_DATATYPE_UNQ UNIQUE ( FAIL_DEFER_REF_ID , DATA_TYPE_DB_ID , DATA_TYPE_ID ) 
   ');

END;
/

--changeSet OPER-9785:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key for the deferral reference table
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CONSTRAINT FK_FAILDEFERREF_DEFERREFDEAD FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID ) DEFERRABLE 
   ');

END;
/

--changeSet OPER-9785:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key for the MIM_DATA_TYPE table
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CONSTRAINT FK_MIMDATATYPE_DEFERREFDEAD FOREIGN KEY ( DATA_TYPE_DB_ID, DATA_TYPE_ID ) REFERENCES MIM_DATA_TYPE ( DATA_TYPE_DB_ID, DATA_TYPE_ID ) DEFERRABLE 
   ');

END;
/

--changeSet OPER-9785:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key for the MIM_RSTAT table
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE FAIL_DEFER_REF_DEAD ADD CONSTRAINT FK_MIMRSTAT_FAILDEFERREFDEAD FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE 
   ');

END;
/

--changeSet OPER-9785:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Audit trigger on insertion for deferral reference deadline table
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_DEAD" BEFORE INSERT
   ON "FAIL_DEFER_REF_DEAD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-9785:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Audit trigger on update for deferral reference deadline table
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_REF_DEAD" BEFORE UPDATE
   ON "FAIL_DEFER_REF_DEAD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-9785:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_DEAD_ID" BEFORE INSERT
   ON "FAIL_DEFER_REF_DEAD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.defer_ref_dead_id IS NULL THEN
     :NEW.defer_ref_dead_id := mx_key_pkg.new_uuid();
  END IF;
END;
/