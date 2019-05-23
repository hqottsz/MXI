--liquibase formatted sql
 

--changeSet MX-25500:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Drop the 7x versions of the triggers
 **************************************************************************/
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_INV_LOC_WT_CAPABILITY');
END;
/

--changeSet MX-25500:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_INV_LOC_WT_CAPABILITY');
END;
/ 

--changeSet MX-25500:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create a new INV_LOC_WT_CAPABILITY table
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_create('
        Create table "INV_LOC_WT_CAPABILITY" (
		"LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"WORK_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (WORK_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"WORK_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
       Constraint "PK_INV_LOC_WT_CAPABILITY" primary key ("LOC_DB_ID","LOC_ID","WORK_TYPE_DB_ID","WORK_TYPE_CD") 
)
');
END;
/ 

--changeSet MX-25500:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add the REVISION_NO column since the table may already exist in 7x
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC_WT_CAPABILITY add (
		"REVISION_NO" Number(10,0) 
)
');
END;
/ 

--changeSet MX-25500:5 stripComments:false
UPDATE inv_loc_wt_capability SET revision_no = 1 WHERE revision_no IS NULL; 

--changeSet MX-25500:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_LOC_WT_CAPABILITY modify (
		"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE 
)
');
END;
/ 

--changeSet MX-25500:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add the CTRL_DB_ID column since the table may already exist in 7x
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC_WT_CAPABILITY add (
		"CTRL_DB_ID" Number(10,0) 
)
');
END;
/ 

--changeSet MX-25500:8 stripComments:false
UPDATE inv_loc_wt_capability SET ctrl_db_id = (SELECT db_id FROM mim_local_db) WHERE ctrl_db_id IS NULL; 

--changeSet MX-25500:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_LOC_WT_CAPABILITY modify (
		"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/ 

--changeSet MX-25500:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add the CREATION_DB_ID column since the table may already exist in 7x
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC_WT_CAPABILITY add (
		"CREATION_DB_ID" Number(10,0) 
)
');
END;
/ 

--changeSet MX-25500:11 stripComments:false
UPDATE inv_loc_wt_capability SET creation_db_id = loc_db_id WHERE creation_db_id IS NULL; 

--changeSet MX-25500:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_LOC_WT_CAPABILITY modify (
		"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/  

--changeSet MX-25500:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add constraint to MIM_RSTAT and MIM_DB 
 **********************************************************************/
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
     Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_MIMDB_INVLOCWTCAPCTRL" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-25500:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
     Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_MIMDB_INVLOCWTCAPCREATION" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-25500:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- The foreign key on the REVISION_DB_ID column was created in 7x with a different name, therefore, 
-- drop the 7x version of the foreign key if it exists so that it can be recreated with the new name.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INV_LOC_WT_CAPABILITY', 'FK_MIMDB_INVLOCWTCAPABILITY');
END;
/

--changeSet MX-25500:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_MIMDB_INVLOCWTCAPREVISION" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-25500:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_REFWORKTYPE_INVLOCWTCAPABIL" foreign key ("WORK_TYPE_DB_ID","WORK_TYPE_CD") references "REF_WORK_TYPE" ("WORK_TYPE_DB_ID","WORK_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-25500:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_MIMRSTAT_INVLOCWTCAPABILITY" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-25500:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "INV_LOC_WT_CAPABILITY" add Constraint "FK_INVLOC_INVLOCWTCAPABILITY" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/  

--changeSet MX-25500:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
  utl_migr_schema_pkg.index_create('
    Create Index "IX_INVLOC_INVLOCWTCAPABILITY" ON "INV_LOC_WT_CAPABILITY" ("LOC_DB_ID","LOC_ID")
');
END;
/  

--changeSet MX-25500:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
 * Add audit_triggers.sql
 **********************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_WT_CAPABILITY" BEFORE INSERT
   ON "INV_LOC_WT_CAPABILITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-25500:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_LOC_WT_CAPABILITY" BEFORE UPDATE
   ON "INV_LOC_WT_CAPABILITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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