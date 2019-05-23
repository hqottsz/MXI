--liquibase formatted sql

--changeset OPER-9390:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment rename column FAIL_DEFER_REF.OPER_ACTIONS_LDESC to OPER_RESTRICTIONS_LDESC
BEGIN
   upg_migr_schema_v1_pkg.table_column_rename('FAIL_DEFER_REF', 'OPER_ACTIONS_LDESC', 'OPER_RESTRICTIONS_LDESC');
END;
/