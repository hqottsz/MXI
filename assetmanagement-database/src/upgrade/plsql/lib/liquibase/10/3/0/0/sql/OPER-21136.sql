--liquibase formatted sql

--changeSet OPER-21136:1 stripComments:false
--comment Maintenix Job to generate index for enhanced part search
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT
  	'MX_CORE_GENERATE_PART_SEARCH_INDEX', 'Updates indexes for enhanced part search', null, 30, 60, 0, 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_job WHERE job_cd = 'MX_CORE_GENERATE_PART_SEARCH_INDEX' );


--changeSet OPER-21136:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of enhanced part search: MT_ENH_PART_SEARCH data catalog
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE MT_ENH_PART_SEARCH
		(
			EQP_PART_NO_PART_NO_DB_ID    NUMBER(10,0)   ,
			EQP_PART_NO_PART_NO_ID       NUMBER(10,0)   ,
			EQP_BOM_PART_BOM_PART_DB_ID  NUMBER(10,0)   ,
			EQP_BOM_PART_BOM_PART_ID     NUMBER(10,0)   ,
			EQP_ASSMBL_POS_ASSMBL_DB_ID  NUMBER(10,0)   ,
			EQP_ASSMBL_POS_ASSMBL_BOM_ID NUMBER(10,0)   ,
			EQP_ASSMBL_POS_ASSMBL_POS_ID NUMBER(10,0)   ,
			SUBASSY_POS_ASSMBL_POS_ID    NUMBER(10,0)   ,
			EQP_PART_NO_ALT_ID           VARCHAR2(64)   ,
			EQP_PART_NO_PART_NO_OEM      VARCHAR2(80)   ,
			EQP_PART_NO_PART_NO_SDESC    VARCHAR2(100)  ,
			EQP_BOM_PART_ALT_ID          VARCHAR2(64)   ,
			EQP_BOM_PART_BOM_PART_CD     VARCHAR2(128)   ,
			EQP_BOM_PART_BOM_PART_NAME   VARCHAR2(100)  ,
			EQP_ASSMBL_POS_ASSMBL_CD     VARCHAR2(8)    ,
			EQP_ASSMBL_POS_EQP_POS_CD    VARCHAR2(200)  ,
			SUBASSY_POS_POS              VARCHAR2(200)  ,
			EQP_BOM_PART_CONDITIONS      VARCHAR2(4000) ,
			REF_INV_CLASS_TRACKED        NUMBER(10,0)   ,
			EQP_ASSMBL_ASSMBL_CLASS_CD   VARCHAR2(8)    ,
			EQP_BOM_PART_APPL_EFF_LDESC  VARCHAR2(4000)
		)
	');
END;
/


--changeSet OPER-21136:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of enhanced part search: MT_ENH_APPLICABILITY data catalog
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE MT_ENH_APPLICABILITY
		(
			INV_NO_DB_ID         NUMBER(10,0),
			INV_NO_ID            NUMBER(10,0),
			BOM_PART_DB_ID       NUMBER(10,0),
			BOM_PART_ID          NUMBER(10,0),
			PART_NO_DB_ID        NUMBER(10,0),
			PART_NO_ID           NUMBER(10,0)
		)
	');
END;
/


--changeSet OPER-21136:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of enhanced part search: MT_ENH_BOM_APL_LOG logging table
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE MT_ENH_BOM_APL_LOG
		(
			BOM_PART_DB_ID NUMBER(10,0),
		    BOM_PART_ID    NUMBER(10,0),
		    UD_DT          DATE
		)
	');
END;
/


--changeSet OPER-21136:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of enhanced part search: MT_ENH_PRT_APL_LOG logging table
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE MT_ENH_PRT_APL_LOG
		(
			BOM_PART_DB_ID NUMBER(10,0),
			BOM_PART_ID    NUMBER(10,0),
			PART_NO_DB_ID  NUMBER(10,0),
			PART_NO_ID     NUMBER(10,0),
			UD_DT          DATE
		)
	');
END;
/

--changeSet OPER-21136:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of enhanced part search: MT_ENH_PART_NO_LOG logging table
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE MT_ENH_PART_NO_LOG
		(
			PART_NO_DB_ID  NUMBER(10,0),
			PART_NO_ID     NUMBER(10,0),
			UD_DT          DATE
		)
	');
END;
/


--changeSet OPER-21136:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment creation of the primary search index for MT_ENH_PART_SEARCH table
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
		CREATE INDEX IX_ENPARTSEARCH_SEARCHPARAM
		ON MT_ENH_PART_SEARCH
		(
			EQP_ASSMBL_POS_ASSMBL_CD,
			UPPER(EQP_PART_NO_PART_NO_SDESC),
			UPPER(EQP_PART_NO_PART_NO_OEM)
		)
	');
END;
/


--changeSet OPER-21136:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment index creation on MT_ENH_APPLICABILITY
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
		CREATE INDEX IX_MT_APPL_BOM
		ON MT_ENH_APPLICABILITY
		(
			INV_NO_DB_ID,
			INV_NO_ID,
			BOM_PART_DB_ID,
			BOM_PART_ID,
			PART_NO_DB_ID,
			PART_NO_ID
		)
	');
END;
/

--changeSet OPER-21136:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger to populate log of changes for enhanced part search
CREATE OR REPLACE TRIGGER TIUDAR_MT_ENH_BOM_APL_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_BOM_PART
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_BOM_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 UD_DT
		)
		VALUES
		(
			 :NEW.BOM_PART_DB_ID,
			 :NEW.BOM_PART_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_BOM_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 UD_DT
		)
		VALUES
		(
			 :OLD.BOM_PART_DB_ID,
			 :OLD.BOM_PART_ID,
			 SYSDATE
		);
	END IF;
END;
/

--changeSet OPER-21136:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment trigger to populate log of changes for enhanced part search
CREATE OR REPLACE TRIGGER TIUDAR_MT_ENH_PRT_APL_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_PART_BASELINE
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_PRT_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
		)
		VALUES
		(
			 :NEW.BOM_PART_DB_ID,
			 :NEW.BOM_PART_ID,
			 :NEW.PART_NO_DB_ID,
			 :NEW.PART_NO_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_PRT_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
		)
		VALUES
		(
			 :OLD.BOM_PART_DB_ID,
			 :OLD.BOM_PART_ID,
			 :OLD.PART_NO_DB_ID,
			 :OLD.PART_NO_ID,
			 SYSDATE
		);
	END IF;
END;
/

--changeSet OPER-21136:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment trigger to populate log of changes for enhanced part search
CREATE OR REPLACE TRIGGER TIUDAR_PRT_NO_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_PART_NO
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_PART_NO_LOG
		(
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
	    )
		VALUES
		(
			 :NEW.PART_NO_DB_ID,
			 :NEW.PART_NO_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_PART_NO_LOG
		(
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
	    )
		VALUES
		(
			 :OLD.PART_NO_DB_ID,
			 :OLD.PART_NO_ID,
			 SYSDATE
		 );
	END IF;
END;
/