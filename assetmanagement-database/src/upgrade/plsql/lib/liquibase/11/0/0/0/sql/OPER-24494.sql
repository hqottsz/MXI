--liquibase formatted sql

--changeSet OPER-24494:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE FAIL_DEFER_REF ADD MOC_APPROVAL_BOOL NUMBER(1) DEFAULT 1 NOT NULL
');
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FAIL_DEFER_REF.MOC_APPROVAL_BOOL IS ''True if the MOC needs to approve this deferral reference.''';
END;
/

--changeSet OPER-24494:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE FAIL_DEFER_REF ADD CHECK ( MOC_APPROVAL_BOOL IN (0, 1))
');
END;
/