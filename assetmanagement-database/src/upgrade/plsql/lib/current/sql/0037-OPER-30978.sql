--liquibase formatted sql

--changeset OPER-30978:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_create('
    CREATE TABLE INV_LOC_BIN_LOG
    (
      INV_LOC_BIN_LOG_ID    NUMBER (10) NOT NULL ,
      LOC_DB_ID             NUMBER (10) NOT NULL ,
      LOC_ID                NUMBER (10) NOT NULL ,
      PART_NO_DB_ID         NUMBER (10) NOT NULL ,
      PART_NO_ID            NUMBER (10) NOT NULL ,
      LOG_TYPE_CD           VARCHAR2 (8) ,
      LOG_GROUP_CD          VARCHAR2 (8) ,
      LOG_GDT               DATE ,
      COUNT_EXPECTED_QT     FLOAT ,
      COUNT_ACTUAL_QT       FLOAT ,
      HR_DB_ID              NUMBER (10) NOT NULL ,
      HR_ID                 NUMBER (10) NOT NULL ,
      RSTAT_CD              NUMBER (3) NOT NULL ,
      CREATION_DT           DATE NOT NULL ,
      REVISION_DT           DATE NOT NULL ,
      REVISION_DB_ID        NUMBER (10) NOT NULL ,
      REVISION_USER         VARCHAR2 (30) NOT NULL
    )
   ');
END;
/

--changeset OPER-30978:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT PK_INV_LOC_BIN_LOG PRIMARY KEY ( INV_LOC_BIN_LOG_ID )
   ');
END;
/



--changeset OPER-30978:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT FK_EQPPARTNO_INVLOCBINLOG FOREIGN KEY ( PART_NO_DB_ID, PART_NO_ID ) REFERENCES EQP_PART_NO ( PART_NO_DB_ID, PART_NO_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-30978:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT FK_INVLOC_INVLOCBINLOG FOREIGN KEY ( LOC_DB_ID, LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-30978:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT FK_MIMDB_INVLOCBINLOG FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-30978:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
        ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT FK_MIMRSTAT_INVLOCBINLOG FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-30978:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
        ALTER TABLE INV_LOC_BIN_LOG ADD CONSTRAINT FK_ORGHR_INVLOCBINLOG FOREIGN KEY ( HR_DB_ID, HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-30978:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_ORGHR_INVLOCBINLOG ON INV_LOC_BIN_LOG
        (
          HR_DB_ID ASC ,
          HR_ID ASC
        )
   ');
END;
/

--changeset OPER-30978:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_INVLOC_INVLOCBINLOG ON INV_LOC_BIN_LOG
        (
          LOC_DB_ID ASC ,
          LOC_ID ASC
        )
   ');
END;
/

--changeset OPER-30978:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_EQPPARTNO_INVLOCBINLOG ON INV_LOC_BIN_LOG
        (
          PART_NO_DB_ID ASC ,
          PART_NO_ID ASC
        )
   ');
END;
/

--changeset OPER-30978:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_BIN_LOG" BEFORE INSERT
   ON "INV_LOC_BIN_LOG" REFERENCING NEW AS NEW FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeset OPER-30978:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_LOC_BIN_LOG" BEFORE UPDATE
   ON "INV_LOC_BIN_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeset OPER-30978:13 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.LOC_DB_ID IS 'FK to INV_LOC.';

--changeset OPER-30978:14 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.LOC_ID IS 'FK to INV_LOC.';

--changeset OPER-30978:15 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.PART_NO_DB_ID IS 'FK to EQP_PART_NO.';

--changeset OPER-30978:16 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.PART_NO_ID IS 'FK to EQP_PART_NO';

--changeset OPER-30978:17 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.LOG_TYPE_CD IS 'Type of the log';

--changeset OPER-30978:18 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.LOG_GROUP_CD IS 'Log group';

--changeset OPER-30978:19 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.LOG_GDT IS 'Date/time when the log is saved';

--changeset OPER-30978:20 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.COUNT_EXPECTED_QT IS 'the expected inventory count at the time the log is saved.';

--changeset OPER-30978:21 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.COUNT_ACTUAL_QT IS 'the actual  inventory count at the time the log is saved.';

--changeset OPER-30978:22 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.HR_DB_ID IS 'User who trigger the creation of this log.';

--changeset OPER-30978:23 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.HR_ID IS 'User who trigger the creation of this log.';

--changeset OPER-30978:24 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.RSTAT_CD IS 'Status of the record.';

--changeset OPER-30978:25 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.CREATION_DT IS 'The database server''s timestamp captured at the time the record was inserted.';

--changeset OPER-30978:26 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.REVISION_DT IS 'The database server''s timestamp captured at the time the record was last updated.' ;

--changeset OPER-30978:27 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.REVISION_DB_ID IS 'The database identifier (MIM_LOCAL_DB) captured at the time the record was last updated. Identifies where modifications are being made in distributed Maintenix.';

--changeset OPER-30978:28 stripComments:false
COMMENT ON COLUMN INV_LOC_BIN_LOG.REVISION_USER IS 'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.';

--changeSet OPER-30978:29 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'INV_LOC_BIN_LOG_ID_SEQ', 1, 'INV_LOC_BIN_LOG', 'INV_LOC_BIN_LOG_ID',1 ,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'INV_LOC_BIN_LOG_ID_SEQ');

--changeSet OPER-30978:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('INV_LOC_BIN_LOG_ID_SEQ', 1);
END;
/

--changeSet OPER-30978:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_INV_LOC_BIN_LOG_ID
/********************************************************************************
*
* Trigger:    TIBR_INV_LOC_BIN_LOG_ID
*
* Description:  Trigger for INV_LOC_BIN_LOG_ID.
*
********************************************************************************/
   BEFORE INSERT ON INV_LOC_BIN_LOG
   FOR EACH ROW
   BEGIN
      :new.inv_loc_bin_log_id := INV_LOC_BIN_LOG_ID_SEQ.nextval;
   END;
/