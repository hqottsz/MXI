--liquibase formatted sql


--changeSet DEV-1355:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create a new CHARGE_VENDOR table
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "CHARGE_VENDOR" (
	"CHARGE_ID" Raw(16) NOT NULL DEFERRABLE ,
	"VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PO_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PO_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_CHARGE_VENDOR" primary key ("CHARGE_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD") 
)  
');
END;
/

--changeSet DEV-1355:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_CHARGE_VENDOR" BEFORE INSERT
   ON "CHARGE_VENDOR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1355:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_CHARGE_VENDOR" BEFORE UPDATE
   ON "CHARGE_VENDOR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1355:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add constraint 
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_MIMRSTAT_CHARGEVENDOR" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1355:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_MIMDB_CHARGEVENDORCTRL" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1355:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_MIMDB_CHARGEVENDORCREATION" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1355:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_MIMDB_CHARGEVENDORRVS" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1355:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_ORGVENDOR_CHARGEVENDOR" foreign key ("VENDOR_DB_ID","VENDOR_ID")references "ORG_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1355:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_REFPOTYPE_CHARGEVENDOR" foreign key ("PO_TYPE_DB_ID","PO_TYPE_CD") references "REF_PO_TYPE" ("PO_TYPE_DB_ID","PO_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1355:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "CHARGE_VENDOR" add Constraint "FK_CHARGE_CHARGEVENDOR" foreign key ("CHARGE_ID") references "CHARGE" ("CHARGE_ID")  DEFERRABLE
');
END;
/   

--changeSet DEV-1355:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_CHARGEVENDOR" ON "CHARGE_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/    

--changeSet DEV-1355:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CHARGE_CHARGEVENDOR" ON "CHARGE_VENDOR" ("CHARGE_ID")
');
END;
/         

--changeSet DEV-1355:13 stripComments:false
/*********************************************************************
 * insert for each ref_charge_type.default_bool = 1, add
 * rows to charge_vendor linking the related tax to all org_vendor keys
 *********************************************************************/
  INSERT INTO
         charge_vendor
         ( CHARGE_ID, VENDOR_DB_ID, VENDOR_ID, PO_TYPE_DB_ID, PO_TYPE_CD, RSTAT_CD, CTRL_DB_ID, CREATION_DB_ID, REVISION_DB_ID )
    SELECT
         charge.charge_id,
         org_vendor.vendor_db_id,
         org_vendor.vendor_id,
         ref_po_type.po_type_db_id,
         ref_po_type.po_type_cd,
         charge.rstat_cd,
         charge.ctrl_db_id,
         charge.creation_db_id,
         charge.revision_db_id
    FROM 
        ref_charge_type,
        charge,
        org_vendor, 
        ref_po_type
    WHERE
         charge.charge_code = ref_charge_type.charge_type_cd AND
        ref_charge_type.default_bool = 1
        ; 