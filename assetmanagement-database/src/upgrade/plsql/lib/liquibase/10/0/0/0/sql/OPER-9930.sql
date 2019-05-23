--liquibase formatted sql


--changeSet OPER-9930:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- remove primary key for ( FAIL_DEFER_REF_ID, CAP_CD, CAP_LEVEL_CD ) combination
BEGIN
utl_migr_schema_pkg.table_constraint_drop('FAIL_DEFER_REF_DEGRAD_CAP','PK_FAIL_DEFER_REF_DEGRAD_CAP');
END;
/

--changeSet OPER-9930:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add primary key for ( FAIL_DEFER_REF_ID, CAP_CD ) combination
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF_DEGRAD_CAP ADD CONSTRAINT PK_FAIL_DEFER_REF_DEGRAD_CAP PRIMARY KEY ( FAIL_DEFER_REF_ID, CAP_CD, CAP_DB_ID )
');
END;
/

--changeSet OPER-9930:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove NOT NULL constraint on CAP_LEVEL_CD
BEGIN
utl_migr_schema_pkg.table_column_cons_nn_drop('FAIL_DEFER_REF_DEGRAD_CAP','CAP_LEVEL_CD');
END;
/

--changeSet OPER-9930:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove NOT NULL constraint on CAP_LEVEL_DB_ID
BEGIN
utl_migr_schema_pkg.table_column_cons_nn_drop('FAIL_DEFER_REF_DEGRAD_CAP','CAP_LEVEL_DB_ID');
END;
/