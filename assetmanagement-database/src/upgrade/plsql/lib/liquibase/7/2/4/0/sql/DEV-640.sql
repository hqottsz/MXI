--liquibase formatted sql


--changeSet DEV-640:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table REF_INT_LOGGING_TYPE (
	"INT_LOGGING_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"DESC_SDESC" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DESC_LDESC" Varchar2 (4000),
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,	
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_INT_LOGGING_TYPE" primary key ("INT_LOGGING_TYPE_CD")
)
');
END;
/

--changeSet DEV-640:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_INT_LOGGING_TYPE add constraint "FK_MIMDB_REFINTLOGTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-640:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_INT_LOGGING_TYPE add constraint "FK_MIMRSTAT_REFINTLOGTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-640:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INT_BP_LOOKUP add (
   "INT_LOGGING_TYPE_CD" Varchar2(8)
)
');
END;
/

--changeSet DEV-640:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table INT_BP_LOOKUP add constraint "FK_REF_INT_LOGTYP_INT_BP_LUP" foreign key ("INT_LOGGING_TYPE_CD") references "REF_INT_LOGGING_TYPE" ("INT_LOGGING_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-640:6 stripComments:false
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
select 'OFF', 'No Integration message logging', 'The integration message logging is disabled.', 0,0, TO_DATE('2010-10-13', 'YYYY-MM-DD'), TO_DATE('2010-10-13', 'YYYY-MM-DD'), 0, 'ADMIN'
from DUAL where not exists (select 1 from REF_INT_LOGGING_TYPE where INT_LOGGING_TYPE_CD = 'OFF');

--changeSet DEV-640:7 stripComments:false
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
select 'HEADER', 'Partial integration message logging (header only)', 'The integration message logging will only record the messages header information.', 0,0, TO_DATE('2010-10-13', 'YYYY-MM-DD'), TO_DATE('2010-10-13', 'YYYY-MM-DD'), 0, 'ADMIN'
from DUAL where not exists (select 1 from REF_INT_LOGGING_TYPE where INT_LOGGING_TYPE_CD = 'HEADER');

--changeSet DEV-640:8 stripComments:false
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
select 'FULL', 'Complete integration message logging', 'The integration message logging will record the header information and store copies of the inbound and outbound messages', 0,0, TO_DATE('2010-10-13', 'YYYY-MM-DD'), TO_DATE('2010-10-13', 'YYYY-MM-DD'), 0, 'ADMIN'
from DUAL where not exists (select 1 from REF_INT_LOGGING_TYPE where INT_LOGGING_TYPE_CD = 'FULL');

--changeSet DEV-640:9 stripComments:false
insert into UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
select 'false', 'INTEGRATION_ENABLE_PROCESS_LOGGING', 'INTEGRATION', 'Indicates whether the detailed process step logging will be recorded to the database (i.e. INT_PROCESS_STEP)', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Integration', '7.5', 0
from DUAL where not exists (select 1 from UTL_CONFIG_PARM where PARM_NAME = 'INTEGRATION_ENABLE_PROCESS_LOGGING');

--changeSet DEV-640:10 stripComments:false
UPDATE int_bp_lookup SET int_logging_type_cd = 'FULL';

--changeSet DEV-640:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INT_BP_LOOKUP modify (
   "INT_LOGGING_TYPE_CD" Varchar2(8) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-640:12 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM utl_user_parm WHERE utl_user_parm.parm_name = 'INTEGRATION_MSG_LOGGING_LEVEL' AND utl_user_parm.parm_type = 'INTEGRATION';

--changeSet DEV-640:13 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM utl_role_parm WHERE utl_role_parm.parm_name = 'INTEGRATION_MSG_LOGGING_LEVEL' AND utl_role_parm.parm_type = 'INTEGRATION';

--changeSet DEV-640:14 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'INTEGRATION_MSG_LOGGING_LEVEL' AND db_type_config_parm.parm_type = 'INTEGRATION';

--changeSet DEV-640:15 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'INTEGRATION_MSG_LOGGING_LEVEL' AND utl_config_parm.parm_type = 'INTEGRATION';

--changeSet DEV-640:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_INT_LOGGING_TYPE" BEFORE INSERT
   ON "REF_INT_LOGGING_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-640:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_INT_LOGGING_TYPE" BEFORE UPDATE
   ON "REF_INT_LOGGING_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-640:18 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_REFRESH_LOGGING_CACHE', 'SECURED_RESOURCE','Permission refresh integration logging type cache.','USER', 'TRUE/FALSE', 'FALSE', 1,'Common - Integration', '7.5',0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_REFRESH_LOGGING_CACHE' );