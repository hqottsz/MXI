--liquibase formatted sql
 

--changeSet DEV-1354:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************
 * Create TAX_LOG table
 **************************************************************/
 BEGIN
 utl_migr_schema_pkg.table_create('
 Create table "CHARGE_LOG" (
	"CHARGE_ID" Raw(16) NOT NULL DEFERRABLE ,
	"CHARGE_LOG_ORDER" Number NOT NULL DEFERRABLE ,
	"CHARGE_LOG_MESSAGE" Varchar2 (4000),
	"CHARGE_LOG_DATE" Date,
	"HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
        Constraint "PK_CHARGE_LOG" primary key ("CHARGE_ID","CHARGE_LOG_ORDER") 
)   
');
 END;
/ 

--changeSet DEV-1354:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
 * Add constraints
 ***********************************************************/
 BEGIN
 utl_migr_schema_pkg.table_constraint_add('
 Alter table "CHARGE_LOG" add Constraint "FK_MIMDB_CHARGELOGCTRL" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
 ');
 END;
/

--changeSet DEV-1354:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_LOG" add Constraint "FK_MIMDB_CHARGELOGCREATION" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1354:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_LOG" add Constraint "FK_MIMDB_CHARGELOGREVISION" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1354:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_LOG" add Constraint "FK_ORGHR_CHARGELOG" foreign key ("HR_DB_ID","HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID") DEFERRABLE
');
END;
/

--changeSet DEV-1354:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_LOG" add Constraint "FK_MIMRSTAT_CHARGELOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1354:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_LOG" add Constraint "FK_CHARGE_CHARGELOG" foreign key ("CHARGE_ID") references "CHARGE" ("CHARGE_ID")   DEFERRABLE
');
END;
/   

--changeSet DEV-1354:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_CHARGELOG" ON "CHARGE_LOG" ("HR_DB_ID","HR_ID")
');
END;
/   

--changeSet DEV-1354:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_CHARGE_CHARGELOG" ON "CHARGE_LOG" ("CHARGE_ID")
 ');
 END;
/   

--changeSet DEV-1354:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_CHARGE_LOG" BEFORE INSERT
   ON "CHARGE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeSet DEV-1354:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CHARGE_LOG" BEFORE UPDATE
   ON "CHARGE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/