--liquibase formatted sql


--changeSet MTX-698:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*** Add WORKFLOW_CD column to ARC_INV_MAP table. ***/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ARC_INV_MAP add (
		"WORKFLOW_CD" Varchar2 (8)
)
');
END;
/