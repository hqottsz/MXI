--liquibase formatted sql


--changeSet DEV-102:1 stripComments:false
-- Migration script for 1003 LRP concept: Section 3 (Extraction Rules)
-- Author: Karan Mehta
-- Date  : January 13, 2010
-- Set the EFFECTIVE_FROM_DT equal to YYYY-01-01 00:00:00, where ‘YYYY’ is the value in the EFFECTIVE_FROM_YEAR column. 
-- Set the ER_HEADER.ER_TYPE_CD to ‘DATE’ for all rows.
UPDATE 
 er_header
SET
 er_header.effective_from_dt = to_date( er_header.effective_from_year || '-01-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss' ),
 er_header.er_type_cd = 'DATE'; 

--changeSet DEV-102:2 stripComments:false
-- If the EFFECTIVE_TO_YEAR is not null then
--   Set the EFFECTIVE_TO_DT equal to YYYY-12-31 23:59:59, where ‘YYYY’ is the value in the EFFECTIVE_TO_YEAR column. 
UPDATE
 er_header
SET
 er_header.effective_to_dt =  to_date( er_header.effective_to_year || '-12-31 23:59:59', 'yyyy-mm-dd hh24:mi:ss' )
WHERE
 er_header.effective_to_year is not null; 

--changeSet DEV-102:3 stripComments:false
-- Migration changes for 0-level data in column WORK_TYPE_ORD in table REF_WORK_TYPE done in the 0-level REF files.
-- Migration changes for non-0-level data in the WORK_TYPE_ORD column in the REF_WORK_TYPE table
UPDATE
 ref_work_type
SET
 ref_work_type.work_type_ord = 10
WHERE
 ref_work_type.work_type_ord IS NULL;

--changeSet DEV-102:4 stripComments:false
UPDATE
 ref_work_type
SET
 ref_work_type.work_type_ord = 10
WHERE
 ref_work_type.work_type_db_id != 0; 

--changeSet DEV-102:5 stripComments:false
-- Specific migration for work terms HEAVY, HANGAR and LINE. 
UPDATE
 ref_work_type
SET
 ref_work_type.work_type_ord = 50
WHERE
 ref_work_type.work_type_cd = 'HEAVY' 
 AND
 ref_work_type.work_type_db_id != 0; 

--changeSet DEV-102:6 stripComments:false
UPDATE
 ref_work_type
SET
 ref_work_type.work_type_ord = 30
WHERE
 ref_work_type.work_type_cd = 'HANGAR' 
 AND
 ref_work_type.work_type_db_id != 0; 

--changeSet DEV-102:7 stripComments:false
UPDATE
 ref_work_type
SET
 ref_work_type.work_type_ord = 20
WHERE
 ref_work_type.work_type_cd = 'LINE' 
 AND
 ref_work_type.work_type_db_id != 0;

--changeSet DEV-102:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table REF_WORK_TYPE modify (
     "WORK_TYPE_ORD" Number(4,0) NOT NULL DEFERRABLE
  )
  ');
 END;
 /  

--changeSet DEV-102:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ER_HEADER', 'EFFECTIVE_FROM_YEAR');
END;
/

--changeSet DEV-102:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ER_HEADER', 'EFFECTIVE_TO_YEAR');
END;
/

--changeSet DEV-102:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UQ_ERHEADER_SDESC');
END;
/

--changeSet DEV-102:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Create UNIQUE Index "UQ_ERHEADER_SDESC" ON "ER_HEADER" ("RULE_DB_ID","DESC_SDESC","EFFECTIVE_FROM_DT","EFFECTIVE_TO_DT") 
');
END;
/