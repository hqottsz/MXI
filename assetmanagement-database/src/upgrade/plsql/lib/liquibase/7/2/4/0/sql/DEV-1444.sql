--liquibase formatted sql


--changeSet DEV-1444:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create a new PO_LINE_ACCOUNT table
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "PO_LINE_ACCOUNT" (
	"PO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PO_LINE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_LINE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ACCOUNT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ACCOUNT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ACCOUNT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ACCOUNT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ACCRUED_VALUE" Number(15,5) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_PO_LINE_ACCOUNT" primary key ("PO_DB_ID","PO_ID","PO_LINE_ID","ACCOUNT_DB_ID","ACCOUNT_ID","CREATION_DB_ID") 
)  
');
END;
/

--changeSet DEV-1444:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_PO_LINE_ACCOUNT" BEFORE INSERT
   ON "PO_LINE_ACCOUNT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1444:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PO_LINE_ACCOUNT" BEFORE UPDATE
   ON "PO_LINE_ACCOUNT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1444:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add constraint to MIM_RSTAT and MIM_DB
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_MIMRSTAT_POLINEACCOUNT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1444:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_MIMDB_POLINEACCTCTRL" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1444:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_MIMDB_POLINEACCTCREATION" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1444:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_MIMDB_POLINEACCTREVISION" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1444:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add FK index and constraint to PO_LINE
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLINEACCOUNT" ON "PO_LINE_ACCOUNT" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet DEV-1444:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_POLINE_POLINEACCOUNT" foreign key ("PO_DB_ID","PO_ID","PO_LINE_ID") references "PO_LINE" ("PO_DB_ID","PO_ID","PO_LINE_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1444:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add FK index and constraint to FNC_ACCOUNT
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_POLINEACCOUNT" ON "PO_LINE_ACCOUNT" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet DEV-1444:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_ACCOUNT" add Constraint "FK_FNCACCOUNT_POLINEACCOUNT" foreign key ("ACCOUNT_DB_ID","ACCOUNT_ID") references "FNC_ACCOUNT" ("ACCOUNT_DB_ID","ACCOUNT_ID")  DEFERRABLE
');
END;
/