--liquibase formatted sql


--changeSet DEV-1438:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add new check columns to PO_HEADER
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- add foreign key BUDGET_CHECK_STATUS_DB_ID and BUDGET_CHECK_STATUS_CD to PO_HEADER table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BUDGET_CHECK_STATUS_DB_ID Number(10,0) Check (BUDGET_CHECK_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1438:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BUDGET_CHECK_STATUS_CD Varchar2 (10) 
)
');
END;
/

--changeSet DEV-1438:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key constraint from ref_po_auth_lvl_status to po_header FK_REFPOAUTHLVLSTATUS_HEADER
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_HEADER add Constraint FK_REFPOAUTHLVLSTATUS_POHEADER
   foreign key (BUDGET_CHECK_STATUS_DB_ID,BUDGET_CHECK_STATUS_CD) 
   references REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID,PO_AUTH_LVL_STATUS_CD)  
   DEFERRABLE
');
END;
/

--changeSet DEV-1438:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Populate the initial values for the new columns
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER DISABLE';
END;
/

--changeSet DEV-1438:5 stripComments:false
UPDATE po_header
SET budget_check_status_db_id = 0,
    budget_check_status_cd = 'BLKOUT'
WHERE
    budget_check_status_db_id IS NULL;

--changeSet DEV-1438:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER ENABLE';
END;
/   

--changeSet DEV-1438:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Add the NOT NULL constraint to the columns now that they are populated
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   BUDGET_CHECK_STATUS_DB_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1438:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   BUDGET_CHECK_STATUS_CD Varchar2 (10) NOT NULL DEFERRABLE
)
');
END;
/