--liquibase formatted sql


--changeSet OPER-5245:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
      'CREATE TABLE ACFT_CAP_LEVELS
      (
         acft_no_db_id      NUMBER (10) NOT NULL ,
         acft_no_id         NUMBER (10) NOT NULL ,
         cap_db_id          NUMBER (10) NOT NULL ,
         cap_cd             VARCHAR2 (8) NOT NULL ,
         level_db_id        NUMBER (10) ,
         level_cd           VARCHAR2 (8) ,
         custom_level       NUMBER (3) ,
         config_level_db_id NUMBER (10) ,
         config_level_cd    VARCHAR2 (8) ,
         rstat_cd           NUMBER (3) NOT NULL ,
         revision_no        NUMBER (10) NOT NULL ,
         ctrl_db_id         NUMBER (10) NOT NULL ,
         creation_dt        DATE NOT NULL ,
         creation_db_id     NUMBER (10) NOT NULL ,
         revision_dt        DATE NOT NULL ,
         revision_db_id     NUMBER (10) NOT NULL ,
         revision_user      VARCHAR2 (30) NOT NULL
      )
      LOGGING'
   );

END;
/

--changeSet OPER-5245:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/

--changeSet OPER-5245:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CHECK ( ctrl_db_id BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5245:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CHECK ( creation_db_id BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5245:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CHECK ( revision_db_id BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5245:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT PK_ACFT_CAP_LEVELS PRIMARY KEY ( acft_no_db_id, acft_no_id, cap_db_id, cap_cd )'
   );
END;
/   

--changeSet OPER-5245:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_INVINV_ACFTCAPL FOREIGN KEY ( acft_no_db_id, acft_no_id ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5245:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_REFACFTCAP_ACFTCAPLS FOREIGN KEY ( cap_db_id, cap_cd ) REFERENCES REF_ACFT_CAP ( ACFT_CAP_DB_ID, ACFT_CAP_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5245:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ACFTCAPLEVELS FOREIGN KEY ( revision_db_id ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
);
END;
/

--changeSet OPER-5245:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_MIMRSTAT_ACFTCAPLEVELS FOREIGN KEY ( rstat_cd ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE'
);
END;
/

--changeSet OPER-5245:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ACFTCAPLEVELS_CR FOREIGN KEY ( creation_db_id ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5245:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ACFTCAPLEVELS_CT FOREIGN KEY ( ctrl_db_id ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5245:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- triggers (insertupdate)    
CREATE OR REPLACE TRIGGER "TIBR_ACFT_CAP_LEVELS" BEFORE INSERT
   ON "ACFT_CAP_LEVELS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-5245:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ACFT_CAP_LEVELS" BEFORE UPDATE
   ON "ACFT_CAP_LEVELS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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