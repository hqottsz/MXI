--liquibase formatted sql

--changeSet OPER-21116:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_FAIL_DEFER_REF_STATUS modify (
   DESC_LDESC VARCHAR2 (4000 CHAR) NOT NULL
)
');
END;
/