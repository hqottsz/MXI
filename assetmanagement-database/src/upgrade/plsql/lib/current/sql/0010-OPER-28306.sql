--liquibase formatted sql

--changeSet OPER-28306:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.table_column_modify('
		ALTER TABLE MT_INV_AC_AUTHORITY
		MODIFY (
			INV_NO_DB_ID    NUMBER (10) NOT NULL
		)
	');
END;
/

--changeSet OPER-28306:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    upg_migr_schema_v1_pkg.table_column_modify('
        ALTER TABLE MT_INV_AC_AUTHORITY
        MODIFY (
            INV_NO_ID       NUMBER (10) NOT NULL
        )
    ');
END;
/

--changeSet OPER-28306:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    upg_migr_schema_v1_pkg.table_column_modify('
        ALTER TABLE MT_INV_AC_AUTHORITY
        MODIFY (
            HR_DB_ID        NUMBER (10) NOT NULL
        )
    ');
END;
/

--changeSet OPER-28306:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    upg_migr_schema_v1_pkg.table_column_modify('
        ALTER TABLE MT_INV_AC_AUTHORITY
        MODIFY (
            HR_ID           NUMBER (10) NOT NULL
        )
    ');
END;
/
