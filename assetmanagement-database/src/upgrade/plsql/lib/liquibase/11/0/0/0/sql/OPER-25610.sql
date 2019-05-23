--liquibase formatted sql

--changeset OPER-25610:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.package_drop('pljson_helper');
   upg_migr_schema_v1_pkg.package_drop('pljson_util_pkg');
   upg_migr_schema_v1_pkg.package_drop('pljson_xml');
   upg_migr_schema_v1_pkg.package_drop('pljson_ml');
   upg_migr_schema_v1_pkg.package_drop('pljson_dyn');
   upg_migr_schema_v1_pkg.package_drop('pljson_ac');
   upg_migr_schema_v1_pkg.package_drop('pljson_ext');
   upg_migr_schema_v1_pkg.package_drop('pljson_printer');
   upg_migr_schema_v1_pkg.package_drop('pljson_parser');
   
END;
/

--changeset OPER-25610:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.type_drop('pljson_value');
   upg_migr_schema_v1_pkg.type_drop('pljson_value_array');
   upg_migr_schema_v1_pkg.type_drop('pljson_list');
   upg_migr_schema_v1_pkg.type_drop('pljson');
   upg_migr_schema_v1_pkg.type_drop('pljson_element');
   upg_migr_schema_v1_pkg.type_drop('pljson_narray');
   upg_migr_schema_v1_pkg.type_drop('pljson_table_impl');
   upg_migr_schema_v1_pkg.type_drop('pljson_varray');
   upg_migr_schema_v1_pkg.type_drop('pljson_vtab');
   
END;
/
