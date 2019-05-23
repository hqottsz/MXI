--liquibase formatted sql

--changeSet OPER-25976:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_create('
        CREATE TABLE COR_BLOB_PRINT
          (
            DOC_DB_ID      NUMBER(10) NOT NULL,
            DOC_ID         NUMBER(10) NOT NULL,
            TYPE_CD        VARCHAR2(8) NOT NULL,
            FILENAME_DESC  VARCHAR2(255) NOT NULL,
            PATH_DESC      VARCHAR2(255),
            ORD            NUMBER(10) NOT NULL,
            DATA           BLOB,
            CREATION_DT    DATE NOT NULL,
            REVISION_DT    DATE NOT NULL,
            REVISION_DB_ID NUMBER(10) NOT NULL,
            REVISION_USER  VARCHAR2(30) NOT NULL
          )
    ');
END;
/

--changeSet OPER-25976:2 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.DOC_DB_ID
IS
  'primary key to identify a printable document.' ;

--changeSet OPER-25976:3 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.DOC_ID
IS
  'primary key to identify a printable document. Unique key generated from PRINT_DOC_ID_SEQ' ;

--changeSet OPER-25976:4 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.TYPE_CD
IS
  'describes the type of document byte data found in the DATA column. ex: PDF' ;

--changeSet OPER-25976:5 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.FILENAME_DESC
IS
  'the complete filename of the document. This is useful if printing to a filesystem' ;

--changeSet OPER-25976:6 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.PATH_DESC
IS
  'an identifier to allow grouping and organization of print jobs. This is useful if printing to a filesystem' ;

--changeSet OPER-25976:7 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.ORD
IS
  'the order of this document in relation to other documents in the same print job' ;

--changeSet OPER-25976:8 stripComments:false
  COMMENT ON COLUMN COR_BLOB_PRINT.DATA
IS
  'the raw byte payload of the document' ;

--changeSet OPER-25976:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE COR_BLOB_PRINT ADD CONSTRAINT PK_COR_BLOB_PRINT PRIMARY KEY (DOC_DB_ID, DOC_ID)
');
END;
/
--changeSet OPER-25976:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   CREATE UNIQUE INDEX IX_CORBLOBPRINTORD ON COR_BLOB_PRINT(DOC_DB_ID ASC , DOC_ID ASC , ORD ASC)
  ');
END;
/
--changeSet OPER-25976:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
  ALTER TABLE COR_BLOB_PRINT ADD CONSTRAINT FK_CORBLOBPRINT_MIMDB FOREIGN KEY ( DOC_DB_ID ) REFERENCES MIM_DB ( DB_ID )
');
END;
/

--changeSet OPER-25976:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_PRINT" BEFORE INSERT
   ON "COR_BLOB_PRINT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
  application_object_pkg.setinsertaudit(
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

--changeSet OPER-25976:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_PRINT" BEFORE UPDATE
   ON "COR_BLOB_PRINT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
  application_object_pkg.setupdateaudit(0,0,
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

--changeset OPER-25976:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('PRINT_DOC_ID_SEQ', 1);
   BEGIN
      INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
      VALUES ( 'PRINT_DOC_ID_SEQ', 1, 'COR_BLOB_PRINT', 'DOC_ID',1 ,0);
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;
END;
/

--changeset OPER-25976:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('PRINT_JOB_ID_SEQ', 1);
   BEGIN
      INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
      VALUES ( 'PRINT_JOB_ID_SEQ', 1, 'UTL_WORK_ITEM', 'KEY',1 ,0);
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;
END;
/

--changeSet OPER-25976:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  BEGIN
    INSERT INTO utl_work_item_type(name,worker_class,work_manager,enabled,utl_id,max_retry_ct,retry_interval,scheduled_buffer)
    VALUES('PRINT_JOB','com.mxi.mx.core.worker.print.PrintJobWorker','wm/Maintenix-PrintingWorkManager',1,0,0,2,500);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_work_item_type(name,worker_class,work_manager,enabled,utl_id,max_retry_ct,retry_interval,scheduled_buffer)
    VALUES('PRINT_ITEM','com.mxi.mx.core.worker.print.PrintItemWorker','wm/Maintenix-PrintingWorkManager',1,0,5,15,500);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
END;
/

--changeSet OPER-25976:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(270,'core.alert.PRINT_JOB_COMPLETED_name','core.alert.PRINT_JOB_COMPLETED_description','PRIVATE',NULL,'PRINT','core.alert.PRINT_JOB_COMPLETED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(271,'core.alert.PRINT_JOB_FAILED_name','core.alert.PRINT_JOB_FAILED_description','PRIVATE',NULL,'PRINT','core.alert.PRINT_JOB_FAILED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
END;
/

