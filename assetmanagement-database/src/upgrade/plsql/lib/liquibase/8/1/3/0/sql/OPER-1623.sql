--liquibase formatted sql


--changeSet OPER-1623:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
   ALTER TABLE ARC_RESULT MODIFY "RESULT_LDESC" Varchar2 (4000 CHAR)
');
END;
/

--changeSet OPER-1623:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
   ALTER TABLE ARC_MESSAGE MODIFY "DESC_SDESC" Varchar2 (4000 CHAR)
');
END;
/