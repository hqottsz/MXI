--liquibase formatted sql


--changeSet DEV-375:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for 1012 AFKLM concept (section 3.2)
-- Author: Karan Mehta
-- Date:   October 1, 2010
--
-- Conditional migration script to remove columns that were removed 
-- as part of DEV-375
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Drop the now obsolete columns LOCKED_HR_DB_ID, LOCKED_HR_ID and LOCKED_DT from the MAINT_PRGM table
BEGIN
utl_migr_schema_pkg.table_column_drop('LRP_TASK_DEFN', 'MAN_HOURS');
utl_migr_schema_pkg.table_column_drop('LRP_TASK_DEFN', 'PLANNED_MAN_HOURS');
utl_migr_schema_pkg.table_column_drop('LRP_TASK_DEFN', 'DURATION_BUFFER_DAYS');
utl_migr_schema_pkg.table_column_drop('LRP_EVENT', 'ADDITIONAL_EFFORT_HRS');
END;
/