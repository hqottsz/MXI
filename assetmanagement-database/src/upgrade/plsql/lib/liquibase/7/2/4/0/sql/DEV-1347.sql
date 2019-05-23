--liquibase formatted sql


--changeSet DEV-1347:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create a new TAX table
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
Create table "TAX" ( 
	"TAX_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TAX_CODE" Varchar2 (40) NOT NULL DEFERRABLE UNIQUE DEFERRABLE ,
	"TAX_NAME" Varchar2 (4000),
	"TAX_RATE" Float,
	"ACCOUNT_ID" Number(10,0) Check (ACCOUNT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ACCOUNT_DB_ID" Number(10,0) Check (ACCOUNT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RECOVERABLE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (RECOVERABLE_BOOL IN (0, 1) ) DEFERRABLE ,
	"COMPOUND_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (COMPOUND_BOOL IN (0, 1) ) DEFERRABLE ,
	"ARCHIVE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ARCHIVE_BOOL IN (0, 1) ) DEFERRABLE ,
	"EXTERNAL_ID" Varchar2 (40),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
        Constraint "PK_TAX" primary key ("TAX_ID") 
)  
');
END;
/  

--changeSet DEV-1347:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMDB_TAXCTRLDBID" ON "TAX" ("CTRL_DB_ID")
 ');
 END;
/

--changeSet DEV-1347:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMDB_TAXCREATIONBID" ON "TAX" ("CREATION_DB_ID")
 ');
 END;
/

--changeSet DEV-1347:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_FNCACCOUNT_TAX" ON "TAX" ("ACCOUNT_DB_ID","ACCOUNT_ID")
 ');
 END;
/

--changeSet DEV-1347:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_TAX" BEFORE INSERT
   ON "TAX" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1347:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TAX" BEFORE UPDATE
   ON "TAX" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1347:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add constraint to MIM_RSTAT and MIM_DB
 **********************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX" add Constraint "FK_MIMRSTAT_TAX" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1347:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX" add Constraint "FK_MIMDB_TAXCTRLDBID" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1347:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX" add Constraint "FK_MIMDB_TAXCREATIONBID" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1347:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX" add Constraint "FK_MIMDB_TAXREVISIONDBID" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1347:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TAX" add Constraint "FK_FNCACCOUNT_TAX" foreign key ("ACCOUNT_DB_ID","ACCOUNT_ID") references "FNC_ACCOUNT" ("ACCOUNT_DB_ID","ACCOUNT_ID" )  DEFERRABLE
');
END;
/  

--changeSet DEV-1347:12 stripComments:false
/********************************************************************
 * ref_tax_type table is to be migrated to the new tax table
 ********************************************************************/
  INSERT INTO tax
  (
        tax_id,
        tax_code,
        tax_name,  
        recoverable_bool,
        compound_bool,
        archive_bool,
        rstat_cd,
        ctrl_db_id,
        creation_db_id,
        revision_db_id
   )
   SELECT
         mx_key_pkg.new_uuid(),
         tax_type_cd,
         desc_ldesc, 
         0,
         0,
         0,
         DECODE(rstat_cd,0,0,1) AS rstat_cd,
         tax_type_db_id,
         tax_type_db_id,
         revision_db_id
   FROM
      ref_tax_type 
 ;   

--changeSet DEV-1347:13 stripComments:false
 /*********************************************************************
  * Correct tax.ctrl_db_id and tax.creation_db_id value
  *********************************************************************/
  UPDATE
         tax
  SET
     (
         ctrl_db_id,
         creation_db_id
     )
     =
     (
         SELECT 
                 db_id,
                 db_id
         FROM 
              mim_local_db
     ) ;       