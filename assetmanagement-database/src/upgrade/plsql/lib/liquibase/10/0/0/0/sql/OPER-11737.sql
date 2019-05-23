--liquibase formatted sql
-- 
-- OPER-11737: Removal of the Recurring Inspections long description column from the FAIL_DEFER_REF table.

--changeSet OPER-11737:1 stripComments:false
BEGIN
   utl_migr_schema_pkg.table_column_drop(
      'fail_defer_ref', 
      'recur_inspections_ldesc'
   );
END;
/