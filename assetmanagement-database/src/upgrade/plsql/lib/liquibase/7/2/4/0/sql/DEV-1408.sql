--liquibase formatted sql


--changeSet DEV-1408:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- ORDER AUTHORIZATION
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Section 3.2.3.1
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Create REF_PO_AUTH_LVL_STATUS table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
utl_migr_schema_pkg.table_create('
  Create table "REF_PO_AUTH_LVL_STATUS" (
  	"PO_AUTH_LVL_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_AUTH_LVL_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) 

DEFERRABLE ,
  	"PO_AUTH_LVL_STATUS_CD" Varchar2 (10) NOT NULL DEFERRABLE ,
  	"DISPLAY_CODE" Varchar2 (80) NOT NULL DEFERRABLE ,
  	"DISPLAY_NAME" Varchar2 (80),
  	"DISPLAY_DESC" Varchar2 (4000),
  	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
  	"CREATION_DT" Date NOT NULL DEFERRABLE ,
  	"REVISION_DT" Date NOT NULL DEFERRABLE ,
  	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
  	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
   Constraint "PK_REF_PO_AUTH_LVL_STATUS" primary key ("PO_AUTH_LVL_STATUS_DB_ID","PO_AUTH_LVL_STATUS_CD") 
  ) 
');
END;
/

--changeSet DEV-1408:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_PO_AUTH_LVL_STATUS" add Constraint "FK_MIMDB_REFPOAUTHLVLSTATUS" foreign key ("PO_AUTH_LVL_STATUS_DB_ID") references 

"MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1408:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_PO_AUTH_LVL_STATUS" add Constraint "FK_MIMRSTAT_REFPOAUTHLVLSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" 

("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1408:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add audit triggers
CREATE OR REPLACE TRIGGER "TIBR_REF_PO_AUTH_LVL_STATUS" BEFORE INSERT
   ON "REF_PO_AUTH_LVL_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1408:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_PO_AUTH_LVL_STATUS" BEFORE UPDATE
   ON "REF_PO_AUTH_LVL_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1408:6 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Insert 0-level data to REF_PO_AUTH_LVL_STATUS table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'BLKOUT', 'BLKOUT', 'N/A', 'N/A', 3, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'BLKOUT');

--changeSet DEV-1408:7 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'PENDING', 'PENDING', 'Pending', 'Authorization has not yet been requested',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'PENDING');

--changeSet DEV-1408:8 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'REQUESTED', 'REQUESTED', 'Requested', 'Authorization has been requested',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'REQUESTED');

--changeSet DEV-1408:9 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'APPROVED', 'APPROVED', 'Approved', 'Authorization has been given',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'APPROVED');

--changeSet DEV-1408:10 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'REJECTED', 'REJECTED', 'Rejected', 'Authorization has been denied',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'REJECTED');

--changeSet DEV-1408:11 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'OVERRIDDEN', 'OVERRIDDEN', 'Overridden', 'Authorization has been overridden',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'OVERRIDDEN');

--changeSet DEV-1408:12 stripComments:false
INSERT INTO REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID, PO_AUTH_LVL_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'BYPASSED', 'BYPASSED', 'Bypassed', 'Authorization has been bypassed',  0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL_STATUS WHERE PO_AUTH_LVL_STATUS_DB_ID = 0 AND PO_AUTH_LVL_STATUS_CD = 'BYPASSED');

--changeSet DEV-1408:13 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Section 3.2.3.2
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Insert 0-level data to REF_PO_AUTH_LVL table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'BUDGET', 0, 'BLKOUT', 'Budget', 'Requires an external budgeting system to authorize this order.', 'BUDGET', 0, 0, TO_DATE('2012-01-05', 'YYYY-MM-DD'), TO_DATE('2012-01-05', 'YYYY-MM-DD'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_PO_AUTH_LVL WHERE PO_AUTH_LVL_DB_ID = 0 AND PO_AUTH_LVL_CD = 'BUDGET');

--changeSet DEV-1408:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Section 3.2.3.3
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Add a new authorization status columns to PO_AUTH
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add foreign key AUTH_LVL_STATUS_DB_ID and BUDGET_CHECK_STATUS_CD to PO_AUTH table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_AUTH add (
   AUTH_LVL_STATUS_DB_ID Number(10,0) Check (AUTH_LVL_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1408:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_AUTH add (
   AUTH_LVL_STATUS_CD Varchar2 (10) 
)
');
END;
/

--changeSet DEV-1408:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key constraint from ref_po_auth_lvl_status to po_auth FK_REFPOAUTHLVLSTATUS_POAUTH
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_AUTH add Constraint FK_REFPOAUTHLVLSTATUS_POAUTH 
   foreign key (AUTH_LVL_STATUS_DB_ID,AUTH_LVL_STATUS_CD) 
   references REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID,PO_AUTH_LVL_STATUS_CD)  
   DEFERRABLE
');
END;
/

--changeSet DEV-1408:17 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Populate the initial values for the new authorization status columns in PO_AUTH
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
UPDATE po_auth
SET auth_lvl_status_db_id = 0,
    auth_lvl_status_cd = 
    ( CASE
          WHEN aog_override_bool = 1 THEN  'BYPASSED'
          ELSE
              CASE 
                   WHEN auth_dt IS NULL THEN 'REQUESTED'
                   ELSE 'APPROVED'
              END
       END                   
    )
WHERE
     auth_lvl_status_db_id IS NULL;       

--changeSet DEV-1408:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Add the NOT NULL constraint to the columns now that they are populated
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_AUTH modify (
   AUTH_LVL_STATUS_DB_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1408:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_AUTH modify (
   AUTH_LVL_STATUS_CD Varchar2 (10) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1408:20 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Additional item:
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Insert config parm in UTL_CONFIG_PARM
-- for deployed ops, Insert config parm in DB_TYPE_CONFIG_PARM
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- insert into UTL_CONFIG_PARM
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ENABLE_ORDER_BUDGET_INTEGRATION', 'FALSE', 'LOGIC', 0,
      'Controls whether the order budget integration feature is enabled. When true, the order approval workflow will include a budget check prior to allowing individual authorizations, and Maintenix will emit and receive order budget related messages.',
      'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Orders - Budget', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENABLE_ORDER_BUDGET_INTEGRATION' )
;

--changeSet DEV-1408:21 stripComments:false
-- insert into UTL_ACTION_CONFIG_PARM
INSERT INTO
   UTL_ACTION_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, DEFAULT_VALUE, ALLOW_VALUE_DESC,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_OVERRIDE_ORDER_BUDGET', 'FALSE', 0,
      'Permission to override a rejected budget for an order.',
      'FALSE', 'TRUE/FALSE', 1, 'Orders - Budget', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_OVERRIDE_ORDER_BUDGET' )
;

--changeSet DEV-1408:22 stripComments:false
INSERT INTO
   DB_TYPE_CONFIG_PARM
   (
      PARM_NAME, DB_TYPE_CD
   )
   SELECT 'ACTION_OVERRIDE_ORDER_BUDGET', 'MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_OVERRIDE_ORDER_BUDGET' AND db_type_cd = 'MASTER' )
;

--changeSet DEV-1408:23 stripComments:false
INSERT INTO
   UTL_ACTION_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, DEFAULT_VALUE, ALLOW_VALUE_DESC,
      MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_REJECT_ORDER', 'FALSE', 0,
      'Permission to reject an order.',
      'FALSE', 'TRUE/FALSE', 1, 'Purchasing - Purchase Orders', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_REJECT_ORDER' )
;

--changeSet DEV-1408:24 stripComments:false
INSERT INTO
   DB_TYPE_CONFIG_PARM
   (
      PARM_NAME, DB_TYPE_CD
   )
   SELECT 'ACTION_REJECT_ORDER', 'MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_REJECT_ORDER' AND db_type_cd = 'MASTER' )
;