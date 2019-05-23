--liquibase formatted sql


--changeSet DEV-626:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- DEV-626: Conditional migration script for the following new columns added
--          to the LRP_PLAN_CONFIG table: OVERRUNBKTS_SEV_DB_ID & 
--          OVERRUNBKTS_SEV_CD.
-- Changed to use 0-level db_id vide. QC-546364
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Author(s): Abhishek Sur & Karan Mehta
-- Date:      October 1, 2010
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- 1) Create the new columns without their constraints
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_PLAN_CONFIG add (
    "OVERRUNBKTS_SEV_DB_ID" Number(10,0) 
)
');
END;
/

--changeSet DEV-626:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_PLAN_CONFIG add (
    "OVERRUNBKTS_SEV_CD" Varchar2 (8) 
)
');
END;
/

--changeSet DEV-626:3 stripComments:false
-- 2) Populate the new non-null columns with default values
UPDATE
  lrp_plan_config
SET
  overrunbkts_sev_db_id = 0,
  overrunbkts_sev_cd = 'NONCRITC'
WHERE
  overrunbkts_sev_db_id IS NULL;

--changeSet DEV-626:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 3) Add the constraints to the new columns 
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_PLAN_CONFIG modify (
    "OVERRUNBKTS_SEV_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (OVERRUNBKTS_SEV_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-626:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_PLAN_CONFIG modify (
    "OVERRUNBKTS_SEV_CD" Varchar2 (8) NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-626:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
-- 4) Add the associated constraint in the database
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_PLAN_CONFIG" add Constraint "FK_OVERRUNBKTS_LRPPLANCONFIG" foreign key ("OVERRUNBKTS_SEV_DB_ID","OVERRUNBKTS_SEV_CD") references "REF_LRP_CONFIG_SEV" ("LRP_CONFIG_SEV_DB_ID","LRP_CONFIG_SEV_CD")  DEFERRABLE
');
END;
/