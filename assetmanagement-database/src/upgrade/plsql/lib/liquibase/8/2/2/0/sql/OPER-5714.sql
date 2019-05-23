--liquibase formatted sql


--changeSet OPER-5714:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
 	      'ACTION_ASSIGN_CAPABILITIES',
 	      'Permission to assign capabilities to a fleet.' ,
 	      'TRUE/FALSE',
 	      'FALSE',
 	      1,
	      'Assembly - Capabilities',
 	      '8.2-SP3',
 	      0,
 	      0,
              UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
 	   );
END;
/

--changeSet OPER-5714:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
      'CREATE TABLE ASSMBL_CAP_LEVELS
      (
         ASSMBL_DB_ID         NUMBER (10) NOT NULL ,
         ASSMBL_CD            VARCHAR2 (8) NOT NULL ,
         ACFT_CAP_DB_ID       NUMBER (10) NOT NULL ,
         ACFT_CAP_CD          VARCHAR2 (8) NOT NULL ,
         ACFT_CAP_LEVEL_DB_ID NUMBER (10) NOT NULL ,
         ACFT_CAP_LEVEL_CD    VARCHAR2 (8) NOT NULL ,
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

--changeSet OPER-5714:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
END;
/

--changeSet OPER-5714:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5714:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5714:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
END;
/

--changeSet OPER-5714:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT PK_ASSMBL_CAP_LEVELS PRIMARY KEY ( ASSMBL_DB_ID, ASSMBL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD, ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD )'
   );
END;
/   

--changeSet OPER-5714:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_EQPASSMBL_ASSMBLCAPLEVELS FOREIGN KEY ( ASSMBL_DB_ID, ASSMBL_CD ) REFERENCES EQP_ASSMBL ( ASSMBL_DB_ID, ASSMBL_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5714:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_REFACFTCAPL_ASSEMBLCAPLS FOREIGN KEY ( ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD ) REFERENCES REF_ACFT_CAP_LEVEL ( ACFT_CAP_LEVEL_DB_ID, ACFT_CAP_LEVEL_CD, ACFT_CAP_DB_ID, ACFT_CAP_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5714:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ASSMBLCAPLEVELS FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5714:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_MIMRSTAT_ASSMBLCAPLEVELS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5714:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ASSMBLCAPLEVELS_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/

--changeSet OPER-5714:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ASSMBL_CAP_LEVELS ADD CONSTRAINT FK_MIMDB_ASSMBLCAPLEVELS_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE'
   );
END;
/    

--changeSet OPER-5714:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- triggers (insertupdate)    
CREATE OR REPLACE TRIGGER "TIBR_ASSMBL_CAP_LEVELS" BEFORE INSERT
   ON "ASSMBL_CAP_LEVELS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-5714:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ASSMBL_CAP_LEVELS" BEFORE UPDATE
   ON "ASSMBL_CAP_LEVELS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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