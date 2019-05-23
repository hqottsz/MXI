--liquibase formatted sql


--changeSet oper-236:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Dropping old package. This will be replaced with OPR_RBL_PIREPMAREP_PKG
BEGIN

   utl_migr_schema_pkg.package_drop('OPR_RBL_FAULT_PKG');
   
END;
/