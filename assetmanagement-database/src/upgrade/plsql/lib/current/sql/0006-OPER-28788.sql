--liquibase formatted sql

--changeSet OPER-28788:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
	CREATE TABLE REF_BULK_LOAD_STATUS
  	(
	STATUS_CD      VARCHAR2 (20) NOT NULL ,
    STATUS_DB_ID   NUMBER (10) NOT NULL ,
    STATUS_SDESC   VARCHAR2 (80) ,
    STATUS_LDESC   VARCHAR2 (4000) ,
    RSTAT_CD       NUMBER (3) NOT NULL ,
    REVISION_NO    NUMBER (10) NOT NULL ,
    CTRL_DB_ID     NUMBER (10) NOT NULL ,
    CREATION_DT    DATE NOT NULL ,
    REVISION_DT    DATE NOT NULL ,
    REVISION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_USER  VARCHAR2 (30) NOT NULL
  	)
   ');
END;
/

--changeSet OPER-28788:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_BULK_LOAD_STATUS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-28788:3 stripComments:false
COMMENT ON TABLE REF_BULK_LOAD_STATUS
IS
  'New Ref table to store the 0-level status codes for Bulk Data Load.' ;

--changeSet OPER-28788:4 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.STATUS_CD
IS
  'Unique status code' ;

--changeSet OPER-28788:5 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.STATUS_DB_ID
IS
  'Db id of the status' ;

--changeSet OPER-28788:6 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.STATUS_SDESC
IS
  'Short description of the status of bulk data loading' ;

--changeSet OPER-28788:7 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.STATUS_LDESC
IS
  'Long description of the status of bulk data loading' ;

--changeSet OPER-28788:8 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-28788:9 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-28788:10 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-28788:11 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-28788:12 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-28788:13 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-28788:14 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_STATUS.REVISION_USER
IS
  'The name of the user that last updated the record' ;

--changeSet OPER-28788:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_STATUS ADD CONSTRAINT PK_REF_BULK_LOAD_STATUS PRIMARY KEY ( STATUS_CD, STATUS_DB_ID )
   ');
END;
/

--changeSet OPER-28788:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_STATUS ADD CONSTRAINT FK_MIMDB_REFBULKSTATUS_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_STATUS ADD CONSTRAINT FK_MIMDB_REFBULKSTATUS_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_STATUS ADD CONSTRAINT FK_MIMRSTAT_REFBULKSTATUS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_BULK_LOAD_STATUS" BEFORE UPDATE
   ON "REF_BULK_LOAD_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_BULK_LOAD_STATUS" BEFORE INSERT
   ON "REF_BULK_LOAD_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
	CREATE TABLE REF_BULK_LOAD_FILE_ACTION
	(
    FILE_ACTION_TYPE_CD    VARCHAR2 (30) NOT NULL ,
    FILE_ACTION_TYPE_DB_ID NUMBER (10) NOT NULL ,
    ACTION_SDESC           VARCHAR2 (80) ,
    ACTION_LDESC           VARCHAR2 (4000) ,
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

--changeSet OPER-28788:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_BULK_LOAD_FILE_ACTION ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-28788:23 stripComments:false
COMMENT ON TABLE REF_BULK_LOAD_FILE_ACTION
IS
  'New Ref table to store the 0-level status codes for Bulk Load File Action' ;

--changeSet OPER-28788:24 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.FILE_ACTION_TYPE_CD
IS
  'Unique code for the action that should be executed when data in a particular file is loaded' ;

--changeSet OPER-28788:25 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.FILE_ACTION_TYPE_DB_ID
IS
  'Db id of the file action type' ;

--changeSet OPER-28788:26 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.ACTION_SDESC
IS
  'Short description of the file action code' ;

--changeSet OPER-28788:27 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.ACTION_LDESC
IS
  'Long description of the file action code' ;

--changeSet OPER-28788:28 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-28788:29 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-28788:30 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-28788:31 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-28788:32 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-28788:33 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-28788:34 stripComments:false
COMMENT ON COLUMN REF_BULK_LOAD_FILE_ACTION.REVISION_USER
IS
  'The name of the user that last updated the record' ;

--changeSet OPER-28788:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_FILE_ACTION ADD CONSTRAINT PK_REF_BULK_LOAD_ACTION PRIMARY KEY ( FILE_ACTION_TYPE_CD, FILE_ACTION_TYPE_DB_ID )
   ');
END;
/

--changeSet OPER-28788:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_FILE_ACTION ADD CONSTRAINT FK_MIMDB_REFBULKTYPE_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_FILE_ACTION ADD CONSTRAINT FK_MIMDB_REFBULKTYPE_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_BULK_LOAD_FILE_ACTION ADD CONSTRAINT FK_MIMRSTAT_REFBULKTYPE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_BULK_LOAD_FILE_ACTION" BEFORE UPDATE
   ON "REF_BULK_LOAD_FILE_ACTION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_BULK_LOAD_FILE_ACTION" BEFORE INSERT
   ON "REF_BULK_LOAD_FILE_ACTION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
	CREATE TABLE UTL_FILE_IMPORT
	(
    FILE_IMPORT_ID         NUMBER (10) NOT NULL ,
    FILE_IMPORT_DB_ID      NUMBER (10) NOT NULL ,
    FILE_NAME              VARCHAR2 (255) ,
    FILE_ACTION_TYPE_CD    VARCHAR2 (30) NOT NULL ,
    FILE_ACTION_TYPE_DB_ID NUMBER (10) NOT NULL ,
    STATUS_CD              VARCHAR2 (20) NOT NULL ,
    STATUS_DB_ID           NUMBER (10) NOT NULL ,
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

--changeSet OPER-28788:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-28788:43 stripComments:false
COMMENT ON TABLE UTL_FILE_IMPORT
IS
  'This table contain the informaion about the imported file.' ;

--changeSet OPER-28788:44 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.FILE_IMPORT_ID
IS
  'Unique id of the imported file' ;

--changeSet OPER-28788:45 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.FILE_IMPORT_DB_ID
IS
  'Db id of the imported file ' ;

--changeSet OPER-28788:46 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.FILE_NAME
IS
  'Name of the imported file' ;

--changeSet OPER-28788:47 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.FILE_ACTION_TYPE_CD
IS
  'Designated action type for the file. This will uniquely identify the action to be performed on the file content.  Foreign key reference to REF_BULK_LOAD_FILE_ACTION' ;

--changeSet OPER-28788:48 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.FILE_ACTION_TYPE_DB_ID
IS
  'The file action type db id. Foreign key reference to REF_BULK_LOAD_FILE_ACTION' ;

--changeSet OPER-28788:49 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.STATUS_CD
IS
  'Current state of the file being imported. Valid States are NEW/PROCESSING/ERROR/FINISHED. Foreign key reference to REF_BULK_LOAD_STATUS' ;

--changeSet OPER-28788:50 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.STATUS_DB_ID
IS
  'The file action type db id. Foreign key reference to REF_BULK_LOAD_STATUS' ;

--changeSet OPER-28788:51 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-28788:52 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-28788:53 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-28788:54 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-28788:55 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-28788:56 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-28788:57 stripComments:false
COMMENT ON COLUMN UTL_FILE_IMPORT.REVISION_USER
IS
  'The name of the user that last updated the record' ;

--changeSet OPER-28788:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT PK_UTL_FILE_IMPORT PRIMARY KEY ( FILE_IMPORT_ID, FILE_IMPORT_DB_ID )
   ');
END;
/

--changeSet OPER-28788:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_BULKACT_UTLFILEIMPORT FOREIGN KEY ( FILE_ACTION_TYPE_CD, FILE_ACTION_TYPE_DB_ID ) REFERENCES REF_BULK_LOAD_FILE_ACTION ( FILE_ACTION_TYPE_CD, FILE_ACTION_TYPE_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_BULKSTATUS_UTLFILEIMPORT FOREIGN KEY ( STATUS_CD, STATUS_DB_ID ) REFERENCES REF_BULK_LOAD_STATUS ( STATUS_CD, STATUS_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_MIMDB_UTLFILEIMPORT_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_MIMDB_UTLFILEIMPORT_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE UTL_FILE_IMPORT ADD CONSTRAINT FK_MIMRSTAT_UTLFILEIMPORT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_FILE_IMPORT" BEFORE UPDATE
   ON "UTL_FILE_IMPORT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_FILE_IMPORT" BEFORE INSERT
   ON "UTL_FILE_IMPORT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
	CREATE TABLE BULK_LOAD_ELEMENT
	(
    FILE_IMPORT_ID         NUMBER (10) NOT NULL ,
    FILE_IMPORT_DB_ID      NUMBER (10) NOT NULL ,
    FILE_ELEMENT           NUMBER (10) NOT NULL ,
    FILE_ACTION_TYPE_CD    VARCHAR2 (30) NOT NULL ,
    FILE_ACTION_TYPE_DB_ID NUMBER (10) NOT NULL ,
    STATUS_CD              VARCHAR2 (20) NOT NULL ,
    STATUS_DB_ID           NUMBER (10) NOT NULL ,
    C0                     VARCHAR2 (4000) ,
    C1                     VARCHAR2 (4000) ,
    C2                     VARCHAR2 (4000) ,
    C3                     VARCHAR2 (4000) ,
    C4                     VARCHAR2 (4000) ,
    C5                     VARCHAR2 (4000) ,
    C6                     VARCHAR2 (4000) ,
    C7                     VARCHAR2 (4000) ,
    C8                     VARCHAR2 (4000) ,
    C9                     VARCHAR2 (4000) ,
    C10                    VARCHAR2 (4000) ,
    C11                    VARCHAR2 (4000) ,
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

--changeSet OPER-28788:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-28788:68 stripComments:false
COMMENT ON TABLE BULK_LOAD_ELEMENT
IS
  'Staging table where bulk data rows inserted and later processed by work item jobs.' ;

--changeSet OPER-28788:69 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.FILE_IMPORT_ID
IS
  'Unique id of the imported file. Foreign key reference to UTL_FILE_IMPORT.' ;

--changeSet OPER-28788:70 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.FILE_IMPORT_DB_ID
IS
  'Db id of the imported file. Foreign key reference to UTL_FILE_IMPORT.' ;

--changeSet OPER-28788:71 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.FILE_ELEMENT
IS
  'Row number of the imported row from file.' ;

--changeSet OPER-28788:72 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.FILE_ACTION_TYPE_CD
IS
  'Designated action type for the file. This will uniquely identify the action to be performed on the file content.  Foreign key reference to REF_BULK_LOAD_FILE_ACTION' ;

--changeSet OPER-28788:73 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.FILE_ACTION_TYPE_DB_ID
IS
  'The file action type db id. Foreign key reference to REF_BULK_LOAD_FILE_ACTION' ;

--changeSet OPER-28788:74 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.STATUS_CD
IS
  'Current state of the file being imported. Valid States are NEW/PROCESSING/ERROR/FINISHED. Foreign key reference to REF_BULK_LOAD_STATUS' ;

--changeSet OPER-28788:75 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.STATUS_DB_ID
IS
  'The file action type db id. Foreign key reference to REF_BULK_LOAD_STATUS' ;

--changeSet OPER-28788:76 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C0
IS
  'Maps with the 1st component of the imported file.' ;

--changeSet OPER-28788:77 stripComments:false
  COMMENT ON COLUMN BULK_LOAD_ELEMENT.C1
IS
  'Maps with the 2nd component of the imported file.' ;

--changeSet OPER-28788:78 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C2
IS
  'Maps with the 3rd component of the imported file.' ;

--changeSet OPER-28788:79 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C3
IS
  'Maps with the 4th component of the imported file.' ;

--changeSet OPER-28788:80 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C4
IS
  'Maps with the 5th component of the imported file.' ;

--changeSet OPER-28788:81 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C5
IS
  'Maps with the 6th component of the imported file.' ;

--changeSet OPER-28788:82 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C6
IS
  'Maps with the 7th component of the imported file.' ;

--changeSet OPER-28788:83 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C7
IS
  'Maps with the 8th component of the imported file.' ;

--changeSet OPER-28788:84 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C8
IS
  'Maps with the 9th component of the imported file.' ;

--changeSet OPER-28788:85 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C9
IS
  'Maps with the 10th component of the imported file.' ;

--changeSet OPER-28788:86 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-28788:87 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-28788:88 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-28788:89 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-28788:90 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-28788:91 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-28788:92 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.REVISION_USER
IS
  'The name of the user that last updated the record' ;

--changeSet OPER-28788:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT PK_BULK_LOAD_ELEMENT PRIMARY KEY ( FILE_IMPORT_ID, FILE_IMPORT_DB_ID, FILE_ELEMENT )
   ');
END;
/

--changeSet OPER-28788:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_BULKACT_BULKLDELMNT FOREIGN KEY ( FILE_ACTION_TYPE_CD, FILE_ACTION_TYPE_DB_ID ) REFERENCES REF_BULK_LOAD_FILE_ACTION ( FILE_ACTION_TYPE_CD, FILE_ACTION_TYPE_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_BULKSTTUS_BULKLDELMNT FOREIGN KEY ( STATUS_CD, STATUS_DB_ID ) REFERENCES REF_BULK_LOAD_STATUS ( STATUS_CD, STATUS_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_MIMDB_BULKLDELMNT_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_MIMDB_BULKLDELMNT_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_MIMRSTAT_BULKLDELMNT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
    ALTER TABLE BULK_LOAD_ELEMENT ADD CONSTRAINT FK_UTLFILEIMPORT_STAGINGBULK FOREIGN KEY ( FILE_IMPORT_ID, FILE_IMPORT_DB_ID ) REFERENCES UTL_FILE_IMPORT ( FILE_IMPORT_ID, FILE_IMPORT_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-28788:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_BULK_LOAD_ELEMENT" BEFORE UPDATE
   ON "BULK_LOAD_ELEMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_BULK_LOAD_ELEMENT" BEFORE INSERT
   ON "BULK_LOAD_ELEMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-28788:102 stripComments:false 
INSERT INTO 
	REF_BULK_LOAD_STATUS 
	(
	STATUS_CD,
    STATUS_DB_ID,
    STATUS_SDESC,
    STATUS_LDESC,
    RSTAT_CD,
    REVISION_NO,
    CTRL_DB_ID,
    CREATION_DT,
    REVISION_DT,
    REVISION_DB_ID,
    REVISION_USER)
	SELECT
		'QUEUED',0, 'Queued','In the UTL_FILE_IMPORT table, QUEUED is the default status of an unprocessed file, with all its rows in the QUEUED status. All rows of a QUEUED file in the BULK_LOAD_ELEMENT table will also be in this state by default.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_STATUS WHERE STATUS_CD = 'QUEUED' AND STATUS_DB_ID =0);

--changeSet OPER-28788:103 stripComments:false
INSERT INTO
	REF_BULK_LOAD_STATUS
	(
	STATUS_CD,
    STATUS_DB_ID,
    STATUS_SDESC,
    STATUS_LDESC,
    RSTAT_CD,
    REVISION_NO,
    CTRL_DB_ID,
    CREATION_DT,
    REVISION_DT,
    REVISION_DB_ID,
    REVISION_USER)
	SELECT
		'ERROR',0, 'Errors Logged','The status of a file with one or more erroneous rows in the UTL_FILE_IMPORT table. If processing a row results in an error, the status of the row will change to ERROR in the BULK_LOAD_ELEMENT table.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_STATUS WHERE STATUS_CD = 'ERROR' AND STATUS_DB_ID =0);

--changeSet OPER-28788:104 stripComments:false
INSERT INTO
	REF_BULK_LOAD_STATUS
	(
	STATUS_CD,
    STATUS_DB_ID,
    STATUS_SDESC,
    STATUS_LDESC,
    RSTAT_CD,
    REVISION_NO,
    CTRL_DB_ID,
    CREATION_DT,
    REVISION_DT,
    REVISION_DB_ID,
    REVISION_USER)
	SELECT
		'PROCESSING',0, 'Processing','The status of a file being processed in Maintenix.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_STATUS WHERE STATUS_CD = 'PROCESSING' AND STATUS_DB_ID =0);

--changeSet OPER-28788:105 stripComments:false
INSERT INTO
	REF_BULK_LOAD_STATUS
	(
	STATUS_CD,
    STATUS_DB_ID,
    STATUS_SDESC,
    STATUS_LDESC,
    RSTAT_CD,
    REVISION_NO,
    CTRL_DB_ID,
    CREATION_DT,
    REVISION_DT,
    REVISION_DB_ID,
    REVISION_USER)
	SELECT
		'FINISHED',0, 'Imported','The status of a file which has been processed successfully.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_STATUS WHERE STATUS_CD = 'FINISHED' AND STATUS_DB_ID =0);

--changeSet OPER-28788:106 stripComments:false
INSERT INTO
	REF_BULK_LOAD_FILE_ACTION
	(
	FILE_ACTION_TYPE_CD,
	FILE_ACTION_TYPE_DB_ID,
	ACTION_SDESC,
	ACTION_LDESC,
	RSTAT_CD,
	REVISION_NO,
	CTRL_DB_ID,
	CREATION_DT,
	REVISION_DT,
	REVISION_DB_ID,
	REVISION_USER)
	SELECT
		'WAREHOUSE_STOCK_LEVEL',0,'Warehouse locations stock levels','This action code is used to identify and process warehouse location stock level data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_FILE_ACTION WHERE FILE_ACTION_TYPE_CD = 'WAREHOUSE_STOCK_LEVEL' AND FILE_ACTION_TYPE_DB_ID =0);

--changeSet OPER-28788:107 stripComments:false
INSERT INTO
	REF_BULK_LOAD_FILE_ACTION
	(
	FILE_ACTION_TYPE_CD,
	FILE_ACTION_TYPE_DB_ID,
	ACTION_SDESC,
	ACTION_LDESC,
	RSTAT_CD,
	REVISION_NO,
	CTRL_DB_ID,
	CREATION_DT,
	REVISION_DT,
	REVISION_DB_ID,
	REVISION_USER)
	SELECT
		'SUPPLY_LOC_STOCK_LEVEL',0,'Supply locations stock levels','This action code is used to identify and process supply location stock level data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_FILE_ACTION WHERE FILE_ACTION_TYPE_CD = 'SUPPLY_LOC_STOCK_LEVEL' AND FILE_ACTION_TYPE_DB_ID =0);

--changeSet OPER-28788:108 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'FILE_IMPORT_ID_SEQ', 1, 'UTL_FILE_IMPORT', 'FILE_IMPORT_ID' , 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'FILE_IMPORT_ID_SEQ');

 
--changeSet OPER-28788:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('FILE_IMPORT_ID_SEQ', 1);
END;
/

--changeSet OPER-28788:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table BULK_LOAD_ELEMENT add (
   ERROR_INFO             VARCHAR2 (4000)
)
');
END;
/

--changeSet OPER-28788:111 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.ERROR_INFO
IS
  'Errors while processing data.' ;

--changeSet OPER-28788:112 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C10
IS
  'Maps with the 10th component of the imported file.' ;


--changeSet OPER-28788:113 stripComments:false
COMMENT ON COLUMN BULK_LOAD_ELEMENT.C11
IS
  'Maps with the 11th component of the imported file.' ;



