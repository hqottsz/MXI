--liquibase formatted sql

--changeSet OPER-18063:1 stripComments:false
BEGIN
UTL_MIGR_SCHEMA_PKG.table_column_modify('ALTER TABLE ref_sensitivity MODIFY WARNING_LDESC VARCHAR2(4000) DEFAULT ''This system is compliance sensitive - the aircraft may require recertification.''
NOT NULL');
END;
/