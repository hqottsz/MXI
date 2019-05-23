--liquibase formatted sql


--changeSet drop_rbl:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_RBL_FAULT_FLEET_SUMMARY');   
end;
/

--changeSet drop_rbl:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_MAINTENANCE');
end;
/

--changeSet drop_rbl:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_FLEET_MOVEMENT');
end;
/

--changeSet drop_rbl:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_USAGE');
end;
/

--changeSet drop_rbl:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_FAULT_INCIDENT');
end;
/

--changeSet drop_rbl:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_FLIGHT_DISRUPTION');
end;
/

--changeSet drop_rbl:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_FLIGHT');
end;
/

--changeSet drop_rbl:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_FAULT');
end;
/

--changeSet drop_rbl:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_MONTHLY_INCIDENT');
end;
/

--changeSet drop_rbl:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_MONTHLY_DELAY');
end;
/

--changeSet drop_rbl:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_MONTHLY_RELIABILITY');
end;
/

--changeSet drop_rbl:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_DELAY_CATEGORY');
end;
/

--changeSet drop_rbl:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_drop('OPR_TECHNICAL_INCIDENT');
end;
/

--changeSet drop_rbl:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
begin
   utl_migr_schema_pkg.table_drop('OPR_RBL_FAULT_SUMMARY');
end;
/

--changeSet drop_rbl:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
begin
   utl_migr_schema_pkg.table_drop('OPR_RBL_FAULT_FLEET_SUMMARY');
end;
/

--changeSet drop_rbl:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
begin
   utl_migr_schema_pkg.table_column_rename('OPR_RBL_FAULT_MONTHLY','FLIGHT_CYCLES','CYCLES');
end;
/

--changeSet drop_rbl:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- OPR_REPORT_REGEN
begin
   utl_migr_schema_pkg.table_column_drop('OPR_REPORT_REGEN','EXTRACTED_DATE'); 
end;
/

--changeSet drop_rbl:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_drop('OPR_REPORT_REGEN','TRANSFORMED_DATE'); 
end;
/

--changeSet drop_rbl:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_add  ('
       ALTER TABLE OPR_REPORT_REGEN ADD EXEC_START_DATE Date
   '); 
end;
/

--changeSet drop_rbl:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_add  ('
       ALTER TABLE OPR_REPORT_REGEN ADD EXEC_END_DATE Date
   '); 
end;
/

--changeSet drop_rbl:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_add  ('
       ALTER TABLE OPR_REPORT_REGEN ADD STATUS_CODE Varchar2 (20)
   '); 
end;
/

--changeSet drop_rbl:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_add  ('
       ALTER TABLE OPR_REPORT_REGEN ADD ERROR Varchar2 (4000)          
   '); 
end;
/

--changeSet drop_rbl:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_REPORT_REGEN add Constraint pk_OPR_REPORT_REGEN primary key (REPORT_CODE,START_DATE,END_DATE) 
');
END;
/

--changeSet drop_rbl:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- OPR_REPORT_MVIEW
begin
   utl_migr_schema_pkg.table_drop('OPR_REPORT_MVIEW');
end;
/

--changeSet drop_rbl:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- OPR_REPORT
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('OPR_REPORT','OPR_PROGRAMTYPECD_CHK');
END;
/

--changeSet drop_rbl:26 stripComments:false
-- update the existing record if exist
UPDATE
   opr_report
SET
   report_code = REPLACE(report_code,'XTRACK','XTRACT'),
   program_type_code = REPLACE(program_type_code,'XTRACK','XTRACT')
WHERE
   program_type_code = 'XTRACK';   

--changeSet drop_rbl:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_REPORT add Constraint OPR_PROGRAMTYPECD_CHK Check (PROGRAM_TYPE_CODE IN (''XTRACT'',''XFORM'',''OTHER''))
');
END;
/

--changeSet drop_rbl:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_add  ('
       ALTER TABLE OPR_REPORT ADD CUTOFF_DATE Date
   '); 
end;
/