--liquibase formatted sql
-- OPER-15451: Add a functional index for deferral reference names in lowercase format to support
-- case insensitive partial matches in search queries.

--changeSet OPER-15451:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_DEFER_REF_SDESC_LOWER ON fail_defer_ref ( LOWER(defer_ref_sdesc) )
   ');
END;
/