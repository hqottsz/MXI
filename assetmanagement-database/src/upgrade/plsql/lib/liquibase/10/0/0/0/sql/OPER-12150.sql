--liquibase formatted sql
-- OPER-12150: Allow filtering of deferral references by status cd in APIs
-- In order to optimize performance of searches, an index will be added to 
-- the fail_defer_ref table.

--changeSet OPER-12150:1 stripComments:false
BEGIN
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_DEFER_REF_STATUS_CD ON FAIL_DEFER_REF (DEFER_REF_STATUS_CD)
   ');
END;
/