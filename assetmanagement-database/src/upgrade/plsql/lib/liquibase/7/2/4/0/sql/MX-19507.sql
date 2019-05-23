--liquibase formatted sql


--changeSet MX-19507:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove the security parameter ACTION_CREATE_ADHOC_TURNIN
BEGIN
   utl_migr_data_pkg.config_parm_delete('ACTION_CREATE_ADHOC_TURNIN');
END;
/