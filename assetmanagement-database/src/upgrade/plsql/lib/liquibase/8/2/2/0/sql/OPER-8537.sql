--liquibase formatted sql

--changeSet OPER-8537:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

--comment Create the ref_deferral_request_status table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE REF_DEFERRAL_REQUEST_STATUS
      (
	    DEFERRAL_REQUEST_STATUS_CD VARCHAR2 (16 CHAR) NOT NULL ,
	    DESC_LDESC                 VARCHAR2 (4000 CHAR) ,
	    BITMAP_DB_ID               NUMBER (10) NOT NULL ,
	    BITMAP_TAG                 NUMBER (10) NOT NULL ,
	    CTRL_DB_ID                 NUMBER (10) NOT NULL ,
	    REVISION_NO                NUMBER (10) NOT NULL ,
	    CREATION_DB_ID             NUMBER (10) NOT NULL ,
	    RSTAT_CD                   NUMBER (3) NOT NULL ,
	    CREATION_DT                DATE NOT NULL ,
	    REVISION_DT                DATE NOT NULL ,
	    REVISION_DB_ID             NUMBER (10) NOT NULL ,
	    REVISION_USER              VARCHAR2 (30) NOT NULL
      )
   ');
END;
/

--changeSet OPER-8537:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CHECK ( BITMAP_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-8537:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/


--changeSet OPER-8537:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-8537:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-8537:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-8537:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CONSTRAINT PK_REF_DEFERRAL_REQUEST_STATUS PRIMARY KEY ( DEFERRAL_REQUEST_STATUS_CD )
   ');
END;
/

--changeSet OPER-8537:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CONSTRAINT FK_MIMRSTAT_REFDEFERREQSTATUS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-8537:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE REF_DEFERRAL_REQUEST_STATUS ADD CONSTRAINT FK_REFBITMAP_REFDEFERREQSTATUS FOREIGN KEY ( BITMAP_DB_ID, BITMAP_TAG ) REFERENCES REF_BITMAP ( BITMAP_DB_ID, BITMAP_TAG ) NOT DEFERRABLE
   ');
END;
/

--comment Insert 0-level data
--changeSet OPER-8537:10 stripComments:false
INSERT INTO ref_deferral_request_status ( deferral_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'PENDING', 'Pending', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_deferral_request_status WHERE deferral_request_status_cd = 'PENDING');

--changeSet OPER-8537:11 stripComments:false
INSERT INTO ref_deferral_request_status ( deferral_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'APPROVED', 'Approved', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_deferral_request_status WHERE deferral_request_status_cd = 'APPROVED');

--changeSet OPER-8537:12 stripComments:false
INSERT INTO ref_deferral_request_status ( deferral_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'REJECTED', 'Rejected', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_deferral_request_status WHERE deferral_request_status_cd = 'REJECTED');

--changeSet OPER-8537:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Create the sd_fault_deferral_request table
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE SD_FAULT_DEFERRAL_REQUEST
      (
        DEFERRAL_REQUEST_ID RAW (16) NOT NULL ,
        DEFERRAL_REQUEST_STATUS_CD VARCHAR2 (16) NOT NULL ,
        FAULT_ID RAW (16) NOT NULL ,
        FAIL_DEFER_REF_ID RAW (16) NOT NULL ,
        HR_ID RAW (16) NOT NULL ,
        DATE_REQUESTED DATE NOT NULL ,
        RSTAT_CD       NUMBER (3) NOT NULL ,
        REVISION_NO    NUMBER (10) NOT NULL ,
        CREATION_DB_ID NUMBER (10) NOT NULL ,
        CREATION_DT    DATE NOT NULL ,
        REVISION_DT    DATE NOT NULL ,
        REVISION_DB_ID NUMBER (10) NOT NULL ,
        REVISION_USER  VARCHAR2 (30) NOT NULL ,
        CTRL_DB_ID     NUMBER (10) NOT NULL
      )
   ');
END;
/

--changeSet OPER-8537:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-8537:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-8537:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/
--changeSet OPER-8537:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-8537:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT PK_SD_FAULT_DEFERRAL_REQUEST PRIMARY KEY ( DEFERRAL_REQUEST_ID )
   ');
END;
/

--changeSet OPER-8537:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_FAILDEFREF_SDFAULTDEFREQ FOREIGN KEY ( FAIL_DEFER_REF_ID ) REFERENCES FAIL_DEFER_REF ( ALT_ID ) NOT DEFERRABLE
   ');
END;
/                                   

--changeSet OPER-8537:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_MIMDB_SDFAULTDEFERREQ FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/      

--changeSet OPER-8537:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_MIMRSTAT_SDFAULTDEFERREQ FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-8537:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_ORGHR_SDFAULTDEFERREQ FOREIGN KEY ( HR_ID ) REFERENCES ORG_HR ( ALT_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-8537:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_REFDEFREQSTAT_SDFAULTDEFREQ FOREIGN KEY ( DEFERRAL_REQUEST_STATUS_CD ) REFERENCES REF_DEFERRAL_REQUEST_STATUS ( DEFERRAL_REQUEST_STATUS_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-8537:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE SD_FAULT_DEFERRAL_REQUEST ADD CONSTRAINT FK_SDFAULT_SDFAULTDEFERREQ FOREIGN KEY ( FAULT_ID ) REFERENCES SD_FAULT ( ALT_ID ) NOT DEFERRABLE
   ');
END;
/

--comment Add the auditing triggers
--changeSet OPER-8537:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_DEFER_REQ_STAT" BEFORE INSERT
   ON "REF_DEFERRAL_REQUEST_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8537:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_DEFER_REQ_STAT" BEFORE UPDATE
   ON "REF_DEFERRAL_REQUEST_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8537:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SD_FAULT_DEFER_REQ" BEFORE INSERT
   ON "SD_FAULT_DEFERRAL_REQUEST" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8537:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SD_FAULT_DEFER_REQ" BEFORE UPDATE
   ON "SD_FAULT_DEFERRAL_REQUEST" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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