--liquibase formatted sql


--changeSet OPER-5199:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   CREATE the tables required for the new Aircraft Capabilities feature.
*/
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
      'CREATE TABLE REF_ACFT_CAP
      (
         ACFT_CAP_DB_ID NUMBER (10) NOT NULL ,
         ACFT_CAP_CD    VARCHAR2 (8) NOT NULL ,
         DESC_SDESC     VARCHAR2 (80) ,
         CAP_ORDER      INTEGER ,
         RSTAT_CD       NUMBER (3) NOT NULL ,
         REVISION_NO    NUMBER (10) NOT NULL ,
         CTRL_DB_ID     NUMBER (10) NOT NULL ,
         CREATION_DT    DATE NOT NULL ,
         CREATION_DB_ID NUMBER (10) NOT NULL ,
         REVISION_DT    DATE NOT NULL ,
         REVISION_DB_ID NUMBER (10) NOT NULL ,
         REVISION_USER  VARCHAR2 (30) NOT NULL
      )
      LOGGING'
   );
END;
/

--changeSet OPER-5199:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_ACFT_CAP" BEFORE INSERT
 ON "REF_ACFT_CAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-5199:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_ACFT_CAP" BEFORE UPDATE
   ON "REF_ACFT_CAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-5199:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/

--changeSet OPER-5199:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CONSTRAINT PK_REF_ACFT_CAP PRIMARY KEY ( ACFT_CAP_DB_ID, ACFT_CAP_CD )'
   );
END;
/

--changeSet OPER-5199:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CONSTRAINT FK_MIMDB_REFACFTCAP FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CONSTRAINT FK_MIMRSTAT_REFACFTCAP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CONSTRAINT FK_MIMDB_REFACFTCAP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP ADD CONSTRAINT FK_MIMDB_REFACFTCAP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
      'CREATE TABLE REF_ACFT_CAP_LEVEL
      (
         ACFT_CAP_LEVEL_DB_ID NUMBER (10) NOT NULL ,
         ACFT_CAP_LEVEL_CD    VARCHAR2 (8) NOT NULL ,
         DESC_SDESC           VARCHAR2 (80) ,
         ACFT_CAP_DB_ID       NUMBER (10) NOT NULL ,
         ACFT_CAP_CD          VARCHAR2 (8) NOT NULL ,
         LEVEL_ORDER          INTEGER ,
         RSTAT_CD             NUMBER (3) NOT NULL ,
         REVISION_NO          NUMBER (10) NOT NULL ,
         CTRL_DB_ID           NUMBER (10) NOT NULL ,
         CREATION_DT          DATE NOT NULL ,
         CREATION_DB_ID       NUMBER (10) NOT NULL ,
         REVISION_DT          DATE NOT NULL ,
         REVISION_DB_ID       NUMBER (10) NOT NULL ,
         REVISION_USER        VARCHAR2 (30) NOT NULL
      )
      LOGGING'
   );

END;
/

--changeSet OPER-5199:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/

--changeSet OPER-5199:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5199:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT PK_REF_ACFT_CAP_LEVEL PRIMARY KEY ( ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD )'
   );
END;
/   

--changeSet OPER-5199:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_REFACFTCAPLEV_REFACFTCAP FOREIGN KEY ( ACFT_CAP_DB_ID, ACFT_CAP_CD ) REFERENCES REF_ACFT_CAP ( ACFT_CAP_DB_ID, ACFT_CAP_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_MIMRSTAT_REFACFTCAPLEVEL FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_MIMDB_REFACFTCAPLEVEL FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_MIMDB_REFACFTCAPLEVEL_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE REF_ACFT_CAP_LEVEL ADD CONSTRAINT FK_MIMDB_REFACFTCAPLEVEL_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5199:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_ACFT_CAP_LEVEL" BEFORE INSERT
 ON "REF_ACFT_CAP_LEVEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-5199:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_ACFT_CAP_LEVEL" BEFORE UPDATE
   ON "REF_ACFT_CAP_LEVEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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