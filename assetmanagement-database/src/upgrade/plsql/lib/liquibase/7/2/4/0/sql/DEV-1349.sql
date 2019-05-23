--liquibase formatted sql


--changeSet DEV-1349:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create a new TAX table
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "TAX_VENDOR" (
	"TAX_ID" Raw(16) NOT NULL DEFERRABLE ,
	"VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_TAX_VENDOR" primary key ("TAX_ID","VENDOR_DB_ID","VENDOR_ID") 
)  
');
END;
/ 

--changeSet DEV-1349:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_TAXVENDOR" ON "TAX_VENDOR"  ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet DEV-1349:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_TAX_VENDOR" BEFORE INSERT
   ON "TAX_VENDOR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1349:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TAX_VENDOR" BEFORE UPDATE
   ON "TAX_VENDOR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1349:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add constraint to MIM_RSTAT and MIM_DB
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_MIMRSTAT_TAXVENDOR" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1349:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_MIMDB_TAXVENDORCTRLDBID" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1349:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_MIMDB_TAXVENDORCREATIONDBID" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1349:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_MIMDB_TAXVENDORREVISIONDBID" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1349:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_ORGVENDOR_TAXVENDOR" foreign key ("VENDOR_DB_ID","VENDOR_ID")references "ORG_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1349:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX_VENDOR" add Constraint "FK_TAX_TAXVENDOR" foreign key ("TAX_ID")references "TAX" ("TAX_ID")   DEFERRABLE
');
END;
/   

--changeSet DEV-1349:11 stripComments:false
/*********************************************************************
 * insert for each ref_tax_type.default_bool = 1, add
 * rows to tax_vendor linking the related tax to all org_vendor keys
 *********************************************************************/
 INSERT INTO
        tax_vendor
        ( TAX_ID, VENDOR_DB_ID, VENDOR_ID, RSTAT_CD, CTRL_DB_ID, CREATION_DB_ID, REVISION_DB_ID )
   SELECT
        tax.tax_id,
        org_vendor.vendor_db_id,
        org_vendor.vendor_id,
        tax.rstat_cd,
        tax.ctrl_db_id,
        tax.creation_db_id,
        tax.revision_db_id
   FROM 
       ref_tax_type,
       tax,
       org_vendor 
   WHERE
        tax.tax_code = ref_tax_type.tax_type_cd AND
       ref_tax_type.default_bool = 1;