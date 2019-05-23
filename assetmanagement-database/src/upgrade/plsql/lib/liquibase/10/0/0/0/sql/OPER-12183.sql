--liquibase formatted sql



--changeSet OPER-12390:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE WPL_WORK_PACKAGE_SETUP
  (
    ID RAW (16) NOT NULL ,
    WP_NAME     VARCHAR2 (80) ,
    TAIL_NO     VARCHAR2 (20) ,
    LOCATION_CD VARCHAR2 (2000) ,
    STATUS_CD   VARCHAR2 (8) NOT NULL ,
    ACCOUNT_ID RAW (16) ,
    SCHED_START_DATE DATE ,
    SCHED_END_DATE   DATE ,
    WORK_PACKAGE_ID RAW (16) ,
    BLOB_ID RAW (16) ,
    FILE_NAME         VARCHAR2 (255) ,
    FILE_UPLOAD_DATE  DATE ,
    RECORDS_PROCESSED INTEGER ,
    LAST_EDIT_DATE    DATE NOT NULL
  )
');
END;
/


--changeSet OPER-12390:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 
BEGIN
utl_migr_schema_pkg.index_create(' 
CREATE INDEX "IX_FK_WORK_PACKAGE_SETUP_BLOB" ON "WPL_WORK_PACKAGE_SETUP" ("BLOB_ID")
');
END;
/


--changeSet OPER-12390:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "WPL_WORK_PACKAGE_SETUP" ADD CONSTRAINT "PK_WORK_PACKAGE_SETUP" PRIMARY KEY ("ID")
');
END;
/


--changeSet OPER-12390:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE WPL_WORK_PACKAGE_SETUP_BLOB
  (
    ID RAW (16) NOT NULL ,
    BLOB BLOB NOT NULL
  )
');
END;
/


--changeSet OPER-12390:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "WPL_WORK_PACKAGE_SETUP_BLOB" ADD CONSTRAINT "PK_WORK_PACKAGE_SETUP_BLOB" PRIMARY KEY ("ID")
');
END;
/


--changeSet OPER-12390:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "WPL_WORK_PACKAGE_SETUP" ADD CONSTRAINT "FK_WORK_PACKAGE_SETUP_BLOB" FOREIGN KEY ("BLOB_ID") REFERENCES "WPL_WORK_PACKAGE_SETUP_BLOB" ("ID") NOT DEFERRABLE
');
END;
/


--changeSet OPER-12390:7 stripComments:false
 /**************************************************************************************
 * API Permission parameter for PPC induction plan request
 ***************************************************************************************/
 INSERT INTO 
  utl_action_config_parm
  (
     PARM_NAME,PARM_VALUE,ENCRYPT_BOOL,PARM_DESC,DEFAULT_VALUE,ALLOW_VALUE_DESC,MAND_CONFIG_BOOL,CATEGORY,MODIFIED_IN,UTL_ID
  )
 SELECT
  'API_WORK_PACKAGE_LOADER_REQUEST','FALSE',0,'Permission to allow API work package setup retrieval call.','FALSE','TRUE/FALSE',1,'API - WPL','8.2-SP3',0
 FROM
  DUAL
 WHERE 
  NOT EXISTS (
     SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_WORK_PACKAGE_LOADER_REQUEST'
  );
