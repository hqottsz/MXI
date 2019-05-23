--liquibase formatted sql


--changeSet MX-19197:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LIC_DEFN modify (
   LIC_TYPE_CD Varchar2 (16)
)
');
END;
/

--changeSet MX-19197:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LIC_TYPE modify (
   LIC_TYPE_CD Varchar2 (16) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MX-19197:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LIC_TYPE modify (
   DESC_SDESC Varchar2 (80) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MX-19197:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LIC_TYPE modify (
   DESC_LDESC Varchar2 (4000)
)
');
END;
/