--liquibase formatted sql


--changeSet MTX-486:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*** Add CREATION_DT column to ARC_RESULT table. ***/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ARC_RESULT add (
		"CREATION_DT" Timestamp(6) NOT NULL DEFERRABLE
)
');
END;
/