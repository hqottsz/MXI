--liquibase formatted sql


--changeSet DEV-1512:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
*
*  New tables for MULE ASB pass-through connector
*
************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_ADAPTER_DEST_LOOKUP" (
	"NAMESPACE" Varchar2 (100) NOT NULL DEFERRABLE ,
	"ROOT_NAME" Varchar2 (100) NOT NULL DEFERRABLE ,
	"URL" Varchar2 (100) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ASB_ADAPTER_DEST_LOOKUP" primary key ("NAMESPACE","ROOT_NAME","URL") 
) 
');
END;
/

--changeSet DEV-1512:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************
 *
 * Audit triggers
 *
/****************************************************/
CREATE OR REPLACE TRIGGER "TIBR_ASB_ADAPTER_DEST_LOOKUP" BEFORE INSERT
   ON "ASB_ADAPTER_DEST_LOOKUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1512:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ASB_ADAPTER_DEST_LOOKUP" BEFORE UPDATE
   ON "ASB_ADAPTER_DEST_LOOKUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1512:4 stripComments:false
/***************************************************
 *
 * utl_config_parm 
 *
/****************************************************/
INSERT INTO UTL_CONFIG_PARM   (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)  
SELECT 'ASB_ADAPTER_NOTIFICATION_MESSAGE_QUEUE','com.mxi.mx.jms.AsbAdapterNotificationQueue', 'ASB', 0,'This value represents the JNDI name of the asb adapter notifcation  message queue to be used.','GLOBAL', 'JNDI name', 'com.mxi.mx.jms.AsbAdapterNotificationQueue', 1, 'ASB', '8.0', 0
FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ASB_ADAPTER_NOTIFICATION_MESSAGE_QUEUE'  AND parm_type = 'ASB' ); 