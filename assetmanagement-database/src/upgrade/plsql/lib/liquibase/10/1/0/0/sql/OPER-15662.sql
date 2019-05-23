--liquibase formatted sql

--changeSet OPER-15662:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE ORG_CREW_SCHEDULE
	  (
	    CREW_DB_ID                NUMBER (10) NOT NULL ,
	    CREW_ID                   NUMBER (10) NOT NULL ,
	    CREW_SCHEDULE_ID          NUMBER (10) NOT NULL ,
	    CREW_SHIFT_PATTERN_DB_ID NUMBER (10) NOT NULL ,
	    CREW_SHIFT_PATTERN_ID    NUMBER (10) NOT NULL ,
	    START_DT                  DATE ,
	    END_DT                    DATE ,
	    ALT_ID RAW (16) NOT NULL ,
	    RSTAT_CD       NUMBER (3) NOT NULL ,
	    CREATION_DT    DATE NOT NULL ,
	    CREATION_DB_ID NUMBER (10) NOT NULL ,
	    REVISION_DT    DATE NOT NULL ,
	    CTRL_DB_ID     NUMBER (10) NOT NULL ,
	    REVISION_DB_ID NUMBER (10) NOT NULL ,
	    REVISION_USER  VARCHAR2 (30) NOT NULL ,
	    REVISION_NO    NUMBER (10) NOT NULL
	  ) 
   ');
END;
/

--changeSet OPER-15662:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE ORG_CREW_SHIFT_PLAN
	  (
	    CREW_DB_ID         NUMBER (10) NOT NULL ,
	    CREW_ID            NUMBER (10) NOT NULL ,
	    CREW_SHIFT_PLAN_ID NUMBER (10) NOT NULL ,
	    SHIFT_DB_ID        NUMBER (10) ,
	    SHIFT_ID           NUMBER (10) ,   
        SCHEDULE_ID RAW (16) NOT NULL ,
    	DAY_DT     DATE NOT NULL ,
    	START_HOUR NUMBER (6,2) ,
        DURATION_QT FLOAT ,
    	WORK_HOURS_QT FLOAT ,
	    ALT_ID RAW (16) NOT NULL ,
	    RSTAT_CD       NUMBER (3) NOT NULL ,
	    CREATION_DT    DATE NOT NULL ,
	    CREATION_DB_ID NUMBER (10) NOT NULL ,
	    REVISION_DT    DATE NOT NULL ,
	    CTRL_DB_ID     NUMBER (10) NOT NULL ,
	    REVISION_DB_ID NUMBER (10) NOT NULL ,
	    REVISION_USER  VARCHAR2 (30) NOT NULL ,
	    REVISION_NO    NUMBER (10) NOT NULL
	  ) 
   ');
END;
/

--changeSet OPER-15662:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.index_create('
  CREATE INDEX IX_USP_ORGCREWSCHEDULE ON ORG_CREW_SCHEDULE
  (
    CREW_SHIFT_PATTERN_DB_ID ASC ,
    CREW_SHIFT_PATTERN_ID ASC
  )
');
END;
/

--changeSet OPER-15662:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.index_create('
  CREATE INDEX IX_ORGWORKDEPT_ORGCREWSCHEDULE ON ORG_CREW_SCHEDULE
  (
    CREW_DB_ID ASC ,
    CREW_ID ASC
  )
');
END;
/

--changeSet OPER-15662:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.index_create('
  CREATE INDEX IX_SHIFTSHIFT_OCSP ON ORG_CREW_SHIFT_PLAN
  (
    SHIFT_DB_ID ASC ,
    SHIFT_ID ASC
  )
');
END;
/

--changeSet OPER-15662:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SCHEDULE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3)) 
');
END;
/

--changeSet OPER-15662:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SCHEDULE ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295) 
');
END;
/

--changeSet OPER-15662:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SCHEDULE ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-15662:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SCHEDULE ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-15662:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
');
END;
/

--changeSet OPER-15662:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-15662:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-15662:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
');
END;
/

--changeSet OPER-15662:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (' 
      ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT PK_ORG_CREW_SCHEDULE PRIMARY KEY ( CREW_DB_ID, CREW_ID, CREW_SCHEDULE_ID )
  ');
END;
/  

--changeSet OPER-15662:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (' 
      ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT IX_ORGCREWSCHEDULEALTID_UNQ UNIQUE ( ALT_ID )
  ');
END;
/ 

--changeSet OPER-15662:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (' 
      ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT PK_ORG_CREW_SHIFT_PLAN PRIMARY KEY ( CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID )
  ');
END;
/
--changeSet OPER-15662:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('  
      ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT IX_ORGCREWSHIFTPLANALTID_UNQ UNIQUE ( ALT_ID ) 
   ');
END;
/

--changeSet OPER-15662:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_MIMDB_OGRCREWSHIFTPLAN FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_MIMDB_OGRCREWSHIFTPLAN_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_MIMDB_OGRCREWSHIFTPLAN_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT FK_MIMDB_ORGCREWSCHEDULE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT FK_MIMDB_ORGCREWSCHEDULE_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add ('
	  ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT FK_MIMDB_ORGCREWSCHEDULE_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_MIMRSTAT_OCSP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
  ');
END;
/

--changeSet OPER-15662:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('  
      ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT FK_MIMRSTAT_ORGCREWSCHEDULE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
	  ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_ORGWKDEPT_OCSP FOREIGN KEY ( CREW_DB_ID, CREW_ID ) REFERENCES ORG_WORK_DEPT ( DEPT_DB_ID, DEPT_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_SHIFTSHIFT_OCSP FOREIGN KEY ( SHIFT_DB_ID, SHIFT_ID ) REFERENCES SHIFT_SHIFT ( SHIFT_DB_ID, SHIFT_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ORG_CREW_SCHEDULE ADD CONSTRAINT FK_USP_ORGCREWSHEDULE FOREIGN KEY ( CREW_SHIFT_PATTERN_DB_ID, CREW_SHIFT_PATTERN_ID ) REFERENCES USER_SHIFT_PATTERN ( USER_SHIFT_PATTERN_DB_ID, USER_SHIFT_PATTERN_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-15662:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ORG_CREW_SHIFT_PLAN ADD CONSTRAINT FK_OCS_OCSP FOREIGN KEY ( SCHEDULE_ID ) REFERENCES ORG_CREW_SCHEDULE ( ALT_ID ) NOT DEFERRABLE
   ');
END;
/
                                     
                                     
--changeSet OPER-15662:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_CREW_SHIFT_PLAN" BEFORE UPDATE
   ON "ORG_CREW_SHIFT_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15662:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_CREW_SHIFT_PLAN" BEFORE INSERT
   ON "ORG_CREW_SHIFT_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15662:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_CREW_SCHEDULE" BEFORE UPDATE
   ON "ORG_CREW_SCHEDULE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15662:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_CREW_SCHEDULE" BEFORE INSERT
   ON "ORG_CREW_SCHEDULE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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