--liquibase formatted sql


--changeSet dev-2470:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Dropping package for refreshing materialized view. 
-- Incorporated this functionality in OPR_RBL_UTILITY_PKG
BEGIN

   utl_migr_schema_pkg.package_drop('OPR_MVIEW_UTILITY_PKG');
   
END;
/