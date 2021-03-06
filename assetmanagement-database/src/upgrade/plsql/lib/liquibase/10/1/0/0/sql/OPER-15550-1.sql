--liquibase formatted sql

--changeSet OPER-15550-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_REFSENSYS_EQPASSMBLBOMSSYS FOREIGN KEY ( SENSITIVE_SYSTEM_CD) REFERENCES REF_SENSITIVE_SYSTEM ( SENSITIVE_SYSTEM_CD ) NOT DEFERRABLE
	');
END;
/