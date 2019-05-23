--liquibase formatted sql

--changeSet OPER-19087:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment increased the max lenght of the column to 4
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FAIL_DEFER_REF modify (
   INST_SYSTEMS_QT Number(4,0) DEFAULT 0 NOT NULL
)
');
END;
/

--changeSet OPER-19087:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment increased the max lenght of the column to 4
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FAIL_DEFER_REF modify (
   OP_SYSTEMS_QT Number(4,0) DEFAULT 0 NOT NULL
)
');
END;
/