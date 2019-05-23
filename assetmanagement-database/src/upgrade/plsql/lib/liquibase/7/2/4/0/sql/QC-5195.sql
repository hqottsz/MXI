--liquibase formatted sql


--changeSet QC-5195:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for QC-5195
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Author: Karan Mehta
-- Date:   October 20, 2010
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Objective: Remove table REF_PLANNING_TYPE and all associated database 
--            entities
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN

utl_migr_schema_pkg.table_constraint_drop('REF_PLANNING_TYPE', 'FK_MIMDB_REFPLANTYPE');
utl_migr_schema_pkg.table_constraint_drop('REF_PLANNING_TYPE', 'FK_MIMRSTAT_REFPLANTYPE');
utl_migr_schema_pkg.trigger_drop('TIBUR_REF_PLANNING_TYPE');
utl_migr_schema_pkg.trigger_drop('TUBR_REF_PLANNING_TYPE');
utl_migr_schema_pkg.table_drop('REF_PLANNING_TYPE');

END;
/