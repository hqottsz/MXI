--liquibase formatted sql

--changeSet OPER-19264:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

--comment drop package MX_ADM_PKG
BEGIN

   upg_migr_schema_v1_pkg.package_drop('MX_ADM_PKG');

END;
/