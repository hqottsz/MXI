--liquibase formatted sql

--changeSet OPER-25015:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE REF_STOCK_DIST_REQ_STATUS
	  (
		STATUS_CD      VARCHAR2 (20) NOT NULL ,
		STATUS_DB_ID   NUMBER (10) NOT NULL ,
		DESC_SDESC     VARCHAR2 (80) ,
		DESC_LDESC     VARCHAR2 (4000) ,
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

--changeSet OPER-25015:2 stripComments:false
COMMENT ON TABLE REF_STOCK_DIST_REQ_STATUS
IS
  '[NOT EXTENDABLE] New Ref table to store the 0-level status codes for Stock Distribution Request such as OPEN/INPROGRESS/PICKED/COMPLETED/CANCELLED.' ;

--changeSet OPER-25015:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_STOCK_DIST_REQ_STATUS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-25015:4 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.STATUS_CD
IS
  'A unique value to indicate the status code for stock distribution request. ' ;

--changeSet OPER-25015:5 stripComments:false
  COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.STATUS_DB_ID
IS
  'Identifies the creation database for the status record and forms part of primary key.' ;

--changeSet OPER-25015:6 stripComments:false
  COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.DESC_SDESC
IS
  'This is the short description of possible statuses for the stock distribution request.' ;

--changeSet OPER-25015:7 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.DESC_LDESC
IS
  'This is the long  description of possible statuses for the stock distribution request.' ;

--changeSet OPER-25015:8 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-25015:9 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-25015:10 stripComments:false
  COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-25015:11 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-25015:12 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-25015:13 stripComments:false
COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-25015:14 stripComments:false
  COMMENT ON COLUMN REF_STOCK_DIST_REQ_STATUS.REVISION_USER
IS
  'The name of the user that last updated the record' ;

--changeSet OPER-25015:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_STOCK_DIST_REQ_STATUS ADD CONSTRAINT PK_REF_STOCK_DIST_REQ_STATUS PRIMARY KEY ( STATUS_CD, STATUS_DB_ID )
   ');
END;
/

--changeSet OPER-25015:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_STOCK_DIST_REQ_STATUS ADD CONSTRAINT FK_MIMDB_STKDISTREQSTATUS_CTR FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE REF_STOCK_DIST_REQ_STATUS ADD CONSTRAINT FK_MIMDB_STKDISTREQSTATUS_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE REF_STOCK_DIST_REQ_STATUS ADD CONSTRAINT FK_MIMRSTAT_STKDISTREQSTATUS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_STOCK_DIST_REQ_STATUS" BEFORE INSERT
   ON "REF_STOCK_DIST_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_STOCK_DIST_REQ_STATUS" BEFORE UPDATE
   ON "REF_STOCK_DIST_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE STOCK_DIST_REQ
	  (
		STOCK_DIST_REQ_ID    NUMBER (10) NOT NULL ,
		STOCK_DIST_REQ_DB_ID NUMBER (10) NOT NULL ,
		REQUEST_ID           VARCHAR2 (80) NOT NULL ,
		STOCK_NO_DB_ID       NUMBER (10) NOT NULL ,
		STOCK_NO_ID          NUMBER (10) NOT NULL ,
 		OWNER_ID             NUMBER (10) ,
		OWNER_DB_ID          NUMBER (10) ,
		STATUS_CD            VARCHAR2 (20) NOT NULL ,
		STATUS_DB_ID         NUMBER (10) NOT NULL ,
		NEEDED_QTY FLOAT NOT NULL ,
		NEEDED_LOC_DB_ID   NUMBER (10) NOT NULL ,
		NEEDED_LOC_ID      NUMBER (10) NOT NULL ,
		SUPPLIER_LOC_DB_ID NUMBER (10) NOT NULL ,
		SUPPLIER_LOC_ID    NUMBER (10) NOT NULL ,
		ASSIGNED_HR_DB_ID  NUMBER (10) ,
		ASSIGNED_HR_ID     NUMBER (10) ,
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

--changeSet OPER-25015:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-25015:23 stripComments:false
COMMENT ON TABLE STOCK_DIST_REQ
IS
  'This table stores the stock distribution request data.' ;

--changeSet OPER-25015:24 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STOCK_DIST_REQ_ID
IS
  'Unique idenifier assigned from Sequence STOCK_DIST_REQ_ID_SEQ. Primary key of the stock distribution request.' ;

--changeSet OPER-25015:25 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STOCK_DIST_REQ_DB_ID
IS
  'Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the primary key.' ;

--changeSet OPER-25015:26 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.REQUEST_ID
IS
  'Unique identifier for stock distribution request.' ;

--changeSet OPER-25015:27 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STOCK_NO_DB_ID
IS
  'The stock number for the stock distribution request. Foreign key reference to eqp_stock_no table.' ;

--changeSet OPER-25015:28 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STOCK_NO_ID
IS
  'The stock number for the stock distribution request. Foreign key reference to eqp_stock_no table.' ;

--changeSet OPER-25015:29 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STATUS_CD
IS
  'Current state of the distribution request such as OPEN/COMPLETED. Foreign key reference to REF_STOCK_DIST_REQ_STATUS.' ;

--changeSet OPER-25015:30 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.STATUS_DB_ID
IS
  'Current state of the distribution request such as OPEN/COMPLETED. Foreign key reference to REF_STOCK_DIST_REQ_STATUS.' ;

--changeSet OPER-25015:31 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.NEEDED_QTY
IS
  'Quantity to transfer for the distribution request.' ;

--changeSet OPER-25015:32 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.NEEDED_LOC_DB_ID
IS
  'Requesting srvstore for the stock. foreign key reference to INV_LOC table.' ;

--changeSet OPER-25015:33 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.NEEDED_LOC_ID
IS
  'Requesting srvstore for the stock. foreign key reference to INV_LOC table.' ;

--changeSet OPER-25015:34 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.SUPPLIER_LOC_DB_ID
IS
  'srvstore supplier location with in the same supply location. Foreign key reference to INV_LOC table.' ;

--changeSet OPER-25015:35 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.SUPPLIER_LOC_ID
IS
  'srvstore supplier location with in the same supply location. Foreign key reference to INV_LOC table.' ;

--changeSet OPER-25015:36 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.ASSIGNED_HR_DB_ID
IS
  'The user working on the stock distribution request.' ;

--changeSet OPER-25015:37 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.ASSIGNED_HR_ID
IS
  'The user working on the stock distribution request.' ;

--changeSet OPER-25015:38 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-25015:39 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-25015:40 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-25015:41 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-25015:42 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-25015:43 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-25015:44 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.REVISION_USER
IS
  'The name of the user that last updated the record.' ;

--changeSet OPER-25015:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_EQPSTKNO_STKDISTREQ ON STOCK_DIST_REQ
		(
		  STOCK_NO_DB_ID ASC ,
		  STOCK_NO_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_STKDISTREQ_UNQ ON STOCK_DIST_REQ
		(
		  REQUEST_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_INVLOCREQ_STKDISTREQ ON STOCK_DIST_REQ
		(
		  NEEDED_LOC_ID ASC ,
		  NEEDED_LOC_DB_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_INVLOCSUP_STKDISTREQ ON STOCK_DIST_REQ
		(
		  SUPPLIER_LOC_ID ASC ,
		  SUPPLIER_LOC_DB_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT PK_STOCK_DIST_REQ PRIMARY KEY ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID )
   ');
END;
/

--changeSet OPER-25015:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_DISTREQSTATUS_STKDISTREQ FOREIGN KEY ( STATUS_CD, STATUS_DB_ID ) REFERENCES REF_STOCK_DIST_REQ_STATUS ( STATUS_CD, STATUS_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_EQPSTKNO_STKDISTREQ FOREIGN KEY ( STOCK_NO_DB_ID, STOCK_NO_ID ) REFERENCES EQP_STOCK_NO ( STOCK_NO_DB_ID, STOCK_NO_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_INVLOCREQ_STKDISTREQ FOREIGN KEY ( NEEDED_LOC_DB_ID, NEEDED_LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_INVLOCSUP_STKDISTREQ FOREIGN KEY ( SUPPLIER_LOC_DB_ID, SUPPLIER_LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_ORGHR_STKDISTREQ FOREIGN KEY ( ASSIGNED_HR_DB_ID, ASSIGNED_HR_ID ) REFERENCES ORG_HR ( HR_DB_ID, HR_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_MIMRSTAT_STKDISTREQ FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_MIMDB_STKDISTREQ_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_MIMDB_STKDISTREQ_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_STOCK_DIST_REQ" BEFORE INSERT
   ON "STOCK_DIST_REQ" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_STOCK_DIST_REQ" BEFORE UPDATE
   ON "STOCK_DIST_REQ" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE STOCK_DIST_REQ_LOG
	  (
		STOCK_DIST_REQ_ID     NUMBER (10) NOT NULL ,
		STOCK_DIST_REQ_DB_ID  NUMBER (10) NOT NULL ,
		STOCK_DIST_REQ_LOG_ID NUMBER (10) NOT NULL ,
		STATUS_CD             VARCHAR2 (20) NOT NULL ,
		STATUS_DB_ID          NUMBER (10) NOT NULL ,
		NOTES                 VARCHAR2 (4000) ,
		AUTO_GENERATED_BOOL   NUMBER (1) DEFAULT 0 ,
		RSTAT_CD              NUMBER (3) NOT NULL ,
		REVISION_NO           NUMBER (10) NOT NULL ,
		CTRL_DB_ID            NUMBER (10) NOT NULL ,
		CREATION_DT           DATE NOT NULL ,
		REVISION_DT           DATE NOT NULL ,
		REVISION_DB_ID        NUMBER (10) NOT NULL ,
		REVISION_USER         VARCHAR2 (30) NOT NULL
	  )
   ');
END;
/

--changeSet OPER-25015:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_LOG ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/


--changeSet OPER-25015:63 stripComments:false
COMMENT ON TABLE STOCK_DIST_REQ_LOG
IS
  'This table stores the history log information for stock distribution request.' ;

--changeSet OPER-25015:64 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.STOCK_DIST_REQ_ID
IS
  'Foreign key to the stock distribution request table. Part of primary key.' ;

--changeSet OPER-25015:65 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.STOCK_DIST_REQ_DB_ID
IS
  'Foreign key to the stock distribution request table.Part of primary key.' ;

--changeSet OPER-25015:66 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.STOCK_DIST_REQ_LOG_ID
IS
  'Unique id of the stock distribution request log record. Part of Primary Key.' ;

--changeSet OPER-25015:67 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.STATUS_CD
IS
  'Status of the stock distribution request at the time history was logged.' ;

--changeSet OPER-25015:68 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.STATUS_DB_ID
IS
  'Status of the stock distribution request at the time history was logged.' ;

--changeSet OPER-25015:69 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.NOTES
IS
  'System or user note explaining the change.' ;

--changeSet OPER-25015:70 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.AUTO_GENERATED_BOOL
IS
  'Boolean flag to indicate whether the change was auto-generated.' ;


--changeSet OPER-25015:71 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-25015:72 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-25015:73 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-25015:74 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-25015:75 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-25015:76 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-25015:77 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_LOG.REVISION_USER
IS
  'The name of the user that last updated the record.' ;


--changeSet OPER-25015:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_STKDISTREQ_STKDISTREQLOG ON STOCK_DIST_REQ_LOG
		(
		  STOCK_DIST_REQ_ID ASC ,
		  STOCK_DIST_REQ_DB_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT PK_STOCK_DIST_REQ_LOG PRIMARY KEY ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID, STOCK_DIST_REQ_LOG_ID )
   ');
END;
/

--changeSet OPER-25015:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT FK_DISTREQSTS_STKDISTREQLOG FOREIGN KEY ( STATUS_CD, STATUS_DB_ID ) REFERENCES REF_STOCK_DIST_REQ_STATUS ( STATUS_CD, STATUS_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT FK_STKDISTREQ_STKDISTREQLOG FOREIGN KEY ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID ) REFERENCES STOCK_DIST_REQ ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID ) NOT DEFERRABLE
   ');
END;
/


--changeSet OPER-25015:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT FK_MIMRSTAT_STKDISTREQLOG FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT FK_MIMDB_STKDISTREQLOG_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_LOG ADD CONSTRAINT FK_MIMDB_STKDISTREQLOG_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_STOCK_DIST_REQ_LOG" BEFORE INSERT
   ON "STOCK_DIST_REQ_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_STOCK_DIST_REQ_LOG" BEFORE UPDATE
   ON "STOCK_DIST_REQ_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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


--changeSet OPER-25015:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE STOCK_DIST_REQ_PICKED_ITEM
	  (
		STOCK_DIST_REQ_ID    NUMBER (10) NOT NULL ,
		STOCK_DIST_REQ_DB_ID NUMBER (10) NOT NULL ,
		XFER_DB_ID           NUMBER (10) NOT NULL ,
		XFER_ID              NUMBER (10) NOT NULL ,
		RSTAT_CD             NUMBER (3) NOT NULL ,
		REVISION_NO          NUMBER (10) NOT NULL ,
		CTRL_DB_ID           NUMBER (10) NOT NULL ,
		CREATION_DT          DATE NOT NULL ,
		REVISION_DT          DATE NOT NULL ,
		REVISION_DB_ID       NUMBER (10) NOT NULL ,
		REVISION_USER        VARCHAR2 (30) NOT NULL
	  )
   ');
END;
/

--changeSet OPER-25015:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-25015:89 stripComments:false
COMMENT ON TABLE STOCK_DIST_REQ_PICKED_ITEM
IS
  'This table stores the mapping for the picked items. The link between stock distribution request and inventory transfer record is one to many, meaning for one stock distribution request there could be multiple inventory transfers but not other way around.' ;

--changeSet OPER-25015:90 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.STOCK_DIST_REQ_ID
IS
  'Foreign key reference to the stock distribution request.' ;

--changeSet OPER-25015:91 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.STOCK_DIST_REQ_DB_ID
IS
  'Foreign key reference to the stock distribution request.' ;

--changeSet OPER-25015:92 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.XFER_DB_ID
IS
  'Foreign key reference to the inventory transfer record.' ;

--changeSet OPER-25015:93 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.XFER_ID
IS
  'Foreign key reference to the inventory transfer record.' ;

--changeSet OPER-25015:94 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.RSTAT_CD
IS
  'A physical attribute that defines the read/write access of the record.' ;

--changeSet OPER-25015:95 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-25015:96 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.CTRL_DB_ID
IS
  'The identifier of the database that owns the record. The meaning of this column may be specific to the entity.' ;

--changeSet OPER-25015:97 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-25015:98 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-25015:99 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-25015:100 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ_PICKED_ITEM.REVISION_USER
IS
  'The name of the user that last updated the record.' ;


--changeSet OPER-25015:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_STKDSTREQ_DISTREQPICKITEM ON STOCK_DIST_REQ_PICKED_ITEM
		(
		  STOCK_DIST_REQ_ID ASC ,
		  STOCK_DIST_REQ_DB_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_INVXFER_DISTREQPICKITEM ON STOCK_DIST_REQ_PICKED_ITEM
		(
		  XFER_ID ASC ,
		  XFER_DB_ID ASC
		)
  ');
END;
/

--changeSet OPER-25015:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT PK_STOCK_DIST_REQ_PICKED_ITEM PRIMARY KEY ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID, XFER_DB_ID, XFER_ID )
   ');
END;
/

--changeSet OPER-25015:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT FK_STKDISTREQ_DISTREQPICKITEM FOREIGN KEY ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID ) REFERENCES STOCK_DIST_REQ ( STOCK_DIST_REQ_ID, STOCK_DIST_REQ_DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT FK_INVXFER_DISTREQPICKITEM FOREIGN KEY ( XFER_DB_ID, XFER_ID ) REFERENCES INV_XFER ( XFER_DB_ID, XFER_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT FK_MIMRSTAT_DISTREQPICKITEM FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT FK_MIMDB_DISTREQPICKITEM_CTR FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE STOCK_DIST_REQ_PICKED_ITEM ADD CONSTRAINT FK_MIMDB_DISTREQPICKITEM_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_STK_DIST_REQ_PICKED_ITEM" BEFORE INSERT
   ON "STOCK_DIST_REQ_PICKED_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_STK_DIST_REQ_PICKED_ITEM" BEFORE UPDATE
   ON "STOCK_DIST_REQ_PICKED_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-25015:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_ORGHR_STKDISTREQ ON STOCK_DIST_REQ
		(
		  ASSIGNED_HR_ID ASC ,
		  ASSIGNED_HR_DB_ID ASC
		)
  ');
END;
/


--changeSet OPER-25015:112 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'STOCK_DIST_REQ_ID_SEQ', 1, 'STOCK_DIST_REQ', 'STOCK_DIST_REQ_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'STOCK_DIST_REQ_ID_SEQ');


--changeSet OPER-25015:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('STOCK_DIST_REQ_ID_SEQ', 1);
END;
/

--changeSet OPER-25015:120 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.OWNER_ID
IS
  'The inventory owner for the stock distribution request. Foreign key reference to inv_owner table.' ;

--changeSet OPER-25015:121 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.OWNER_DB_ID
IS
  'The inventory owner for the stock distribution request. Foreign key reference to inv_owner table.' ;

--changeSet OPER-25015:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--add FK FK_INVOWNER_STKDISTREQ
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_INVOWNER_STKDISTREQ FOREIGN KEY ( OWNER_DB_ID, OWNER_ID ) REFERENCES INV_OWNER ( OWNER_DB_ID, OWNER_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25015:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--add index index IX_INVOWNER_STKDISTREQ
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
      CREATE INDEX IX_INVOWNER_STKDISTREQ ON STOCK_DIST_REQ
       (
         OWNER_ID ASC ,
         OWNER_DB_ID ASC
       )
  ');
END;
/

