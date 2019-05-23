--liquibase formatted sql


--changeSet OPER-9085:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
      'CREATE TABLE FL_REQUIREMENT
      (         
         FL_LEG_ID            RAW (16) NOT NULL ,
         CAP_DB_ID            NUMBER (10) NOT NULL ,
         CAP_CD               VARCHAR2 (8) NOT NULL ,
         LEVEL_DB_ID          NUMBER (10) NOT NULL ,
         LEVEL_CD             VARCHAR2 (8) NOT NULL ,
		 ALT_ID	          	  RAW (16) NOT NULL ,
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

--changeSet OPER-9085:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/

--changeSet OPER-9085:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-9085:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-9085:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-9085:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT IX_FLREQUIREMENTALTID_UNQ UNIQUE ( ALT_ID )'
   );
END;
/

--changeSet OPER-9085:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT PK_FL_REQ PRIMARY KEY (FL_LEG_ID, CAP_CD, CAP_DB_ID, LEVEL_CD, LEVEL_DB_ID )'
   );
END;
/   

--changeSet OPER-9085:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_FL_LEG FOREIGN KEY ( FL_LEG_ID ) REFERENCES FL_LEG ( LEG_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-9085:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_REF_ACFT_CAP_LEVEL FOREIGN KEY ( LEVEL_DB_ID, LEVEL_CD, CAP_DB_ID, CAP_CD ) REFERENCES REF_ACFT_CAP_LEVEL ( ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-9085:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_MIM_DB FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-9085:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-9085:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_MIM_DB_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-9085:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE FL_REQUIREMENT ADD CONSTRAINT FK_FL_REQ_MIM_DB_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/    

--changeSet OPER-9085:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- triggers (insertupdate)    
CREATE OR REPLACE TRIGGER "TIBR_FL_REQUIREMENT" BEFORE INSERT
   ON "FL_REQUIREMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-9085:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_REQUIREMENT" BEFORE UPDATE
   ON "FL_REQUIREMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-9085:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_REQUIREMENT_ALT_ID" BEFORE INSERT
   ON "FL_REQUIREMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/