--liquibase formatted sql

--changeSet OPER-28776:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE REF_CONTROL_METHOD
		  (
			CONTROL_METHOD_DB_ID NUMBER (10) NOT NULL ,
			CONTROL_METHOD_CD    VARCHAR2 (8) NOT NULL ,
			DESC_SDESC           VARCHAR2 (80) NOT NULL ,
			DESC_LDESC           VARCHAR2 (4000) ,
			RSTAT_CD             NUMBER (3) NOT NULL ,
			CREATION_DT          DATE NOT NULL ,
			REVISION_DT          DATE NOT NULL ,
			REVISION_DB_ID       NUMBER (10) NOT NULL ,
			REVISION_USER        VARCHAR2 (30) NOT NULL
		  )
	'	);
END;
/

--changeSet OPER-28776:2 stripComments:false 
COMMENT ON TABLE REF_CONTROL_METHOD IS   'A control method to identify how the entities are created (for example from MANUALLY/BASELINE/EXTERNAL). This table will contain 0 data level only.' ;
--changeSet OPER-28776:3 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.CONTROL_METHOD_DB_ID IS  'Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the record''s primary key.' ;
--changeSet OPER-28776:4 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.CONTROL_METHOD_CD IS  'Unique identifier serving as part of the primary key.' ;
--changeSet OPER-28776:5 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.DESC_SDESC IS 'Short description of the control mehod.' ;
--changeSet OPER-28776:6 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.DESC_LDESC IS 'Long description of the control method.' ;
--changeSet OPER-28776:7 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.RSTAT_CD IS 'Status of the record. ' ;
--changeSet OPER-28776:8 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.CREATION_DT IS 'The date and time at which the record was inserted.' ;
--changeSet OPER-28776:9 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.REVISION_DT IS 'The identifier of the database that inserted the record.' ;
--changeSet OPER-28776:10 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.REVISION_DB_ID IS 'The identifier of the database that last updated the record.' ;
--changeSet OPER-28776:11 stripComments:false 
COMMENT ON COLUMN REF_CONTROL_METHOD.REVISION_USER IS  'The name of the user that last updated the record.' ;


--changeSet OPER-28776:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_CONTROL_METHOD ADD CHECK ( RSTAT_CD IN (0,1,2,3)) 
   ');
END;
/

--changeSet OPER-28776:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_CONTROL_METHOD ADD CONSTRAINT PK_REF_CONTROL_METHOD PRIMARY KEY ( CONTROL_METHOD_DB_ID, CONTROL_METHOD_CD ) 
	');
END;
/

--changeSet OPER-28776:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_CONTROL_METHOD ADD CONSTRAINT FK_MIMDB_REFCONTROLMETHOD FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
	');
END;
/

--changeSet OPER-28776:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_CONTROL_METHOD ADD CONSTRAINT FK_MIMRSTAT_REFCONTROLMETHOD FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/


--changeSet OPER-28776:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_CONTROL_METHOD" BEFORE UPDATE
 ON "REF_CONTROL_METHOD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
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
end;
/

--changeSet OPER-28776:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_CONTROL_METHOD" BEFORE INSERT
   ON "REF_CONTROL_METHOD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
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
end;
/
  
--changeSet OPER-28776:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SCHED_PART ADD (CONTROL_METHOD_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-28776:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SCHED_PART ADD (CONTROL_METHOD_CD VARCHAR2 (8))
   ');
END;
/


--changeSet OPER-28776:20 stripComments:false 
COMMENT ON COLUMN SCHED_PART.CONTROL_METHOD_DB_ID IS 'FK to REF_CONTROL_METHOD. Method indicating how the part requirement was created (MANUAL/BASELINE/EXTERNAL).' ;
--changeSet OPER-28776:21 stripComments:false 
COMMENT ON COLUMN SCHED_PART.CONTROL_METHOD_CD IS 'FK to REF_CONTROL_METHOD. Method indicating how the part requirement was created (MANUAL/BASELINE/EXTERNAL).' ;
  

--changeSet OPER-28776:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
		CREATE INDEX IX_CONTROLMETHOD_SCHEDPART ON SCHED_PART
		(
		  CONTROL_METHOD_DB_ID ASC ,
		  CONTROL_METHOD_CD ASC
		)
   ');
END;
/

--changeSet OPER-28776:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE SCHED_PART ADD CONSTRAINT FK_REFCONTROLMETHOD_SCHEDPART FOREIGN KEY ( CONTROL_METHOD_DB_ID, CONTROL_METHOD_CD ) REFERENCES REF_CONTROL_METHOD ( CONTROL_METHOD_DB_ID, CONTROL_METHOD_CD ) NOT DEFERRABLE
	');
END;
/	

--changeSet OPER-28776:24 stripComments:false 
INSERT INTO 
	REF_CONTROL_METHOD 
	(
		CONTROL_METHOD_DB_ID,
		CONTROL_METHOD_CD,
		DESC_SDESC,
		RSTAT_CD, 
		CREATION_DT, 
		REVISION_DT, 
		REVISION_DB_ID, 
		REVISION_USER) 
	SELECT 	
		0,'MANUAL','Record created manually', 0, TO_DATE( '2019-01-15','YYYY-MM-DD'), TO_DATE( '2019-01-15','YYYY-MM-DD'), 0, 'MXI'
	FROM 
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_CONTROL_METHOD WHERE CONTROL_METHOD_DB_ID = 0 AND CONTROL_METHOD_CD = 'MANUAL');
	
--changeSet OPER-28776:25 stripComments:false 
INSERT INTO 
	REF_CONTROL_METHOD 
	(
		CONTROL_METHOD_DB_ID,
		CONTROL_METHOD_CD,
		DESC_SDESC,
		RSTAT_CD, 
		CREATION_DT, 
		REVISION_DT, 
		REVISION_DB_ID, 
		REVISION_USER) 
	SELECT 	
		0,'BASELINE','Record created by baseline', 0, TO_DATE( '2019-01-15','YYYY-MM-DD'), TO_DATE( '2019-01-15','YYYY-MM-DD'), 0, 'MXI'
	FROM 
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_CONTROL_METHOD WHERE CONTROL_METHOD_DB_ID = 0 AND CONTROL_METHOD_CD = 'BASELINE');

--changeSet OPER-28776:26 stripComments:false 
INSERT INTO 
	REF_CONTROL_METHOD 
	(
		CONTROL_METHOD_DB_ID,
		CONTROL_METHOD_CD,
		DESC_SDESC,
		RSTAT_CD, 
		CREATION_DT, 
		REVISION_DT, 
		REVISION_DB_ID, 
		REVISION_USER) 
	SELECT 	
		0,'EXTERNAL','Record created by external system', 0, TO_DATE( '2019-01-15','YYYY-MM-DD'), TO_DATE( '2019-01-15','YYYY-MM-DD'), 0, 'MXI'
	FROM 
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_CONTROL_METHOD WHERE CONTROL_METHOD_DB_ID = 0 AND CONTROL_METHOD_CD = 'EXTERNAL');


