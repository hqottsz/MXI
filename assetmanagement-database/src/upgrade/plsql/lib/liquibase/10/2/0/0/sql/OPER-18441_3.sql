--liquibase formatted sql

--changeSet OPER-18441_3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_rename('EQP_ASSMBL_BOM_SENS_SYSTEM', 'EQP_ASSMBL_BOM_SENS');
END;
/

--changeSet OPER-18441_3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'PK_EQPASSMBLBOM_SENSSYSTEM');
END;
/

--changeSet OPER-18441_3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.index_drop('PK_EQPASSMBLBOM_SENSSYSTEM');
END;
/

--changeSet OPER-18441_3:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  UTL_MIGR_SCHEMA_PKG.table_column_rename('EQP_ASSMBL_BOM_SENS', 'SENSITIVE_SYSTEM_CD', 'SENSITIVITY_CD');
END;
/

--changeSet OPER-18441_3:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT PK_EQP_ASSMBL_BOM_SENS PRIMARY KEY ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, SENSITIVITY_CD )
	');
END;
/

--changeSet OPER-18441_3:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'FK_MIMDB_ASSMBLBOMSENSYS_CRE');
END;
/

--changeSet OPER-18441_3:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'FK_MIMDB_ASSMBLBOMSENSYS_CTRL');
END;
/

--changeSet OPER-18441_3:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'FK_MIMDB_ASSMBLBOMSENSYS_REV');
END;
/

--changeSet OPER-18441_3:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'FK_MIMRSTAT_EQPASSMBLBOMSSYS');
END;
/

--changeSet OPER-18441_3:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS', 'FK_EQPASSMBLBOM_SENSSYSTEM');
END;
/

--changeSet OPER-18441_3:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_EQPASSMBLBOM_ASSMBLBOMSENS FOREIGN KEY ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID ) REFERENCES EQP_ASSMBL_BOM ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_3:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_MIMDB_EQPASSMBLBOMSENS_CRE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_3:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_MIMDB_EQPASSMBLBOMSENS_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_3:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_MIMDB_EQPASSMBLBOMSENS_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_3:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_MIMRSTAT_EQPASSMBLBOMSENS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_3:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS ADD CONSTRAINT FK_REFSENS_EQPASSMBLBOMSENS FOREIGN KEY ( SENSITIVITY_CD ) REFERENCES REF_SENSITIVITY ( SENSITIVITY_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_4:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.trigger_drop('TIBR_EQP_ASSMBL_BOM_SEN_SYS');
END;
/

--changeSet OPER-18441_4:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.trigger_drop('TUBR_EQP_ASSMBL_BOM_SEN_SYS');
END;
/

--changeSet audit_triggers:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQP_ASSMBL_BOM_SENS" BEFORE UPDATE
   ON "EQP_ASSMBL_BOM_SENS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
	:new.rstat_cd,
	:old.revision_no,
	:new.revision_no,
	:new.revision_dt,
	:new.revision_db_id,
	:new.revision_user );
end;
/

--changeSet audit_triggers:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_BOM_SENS" BEFORE INSERT
   ON "EQP_ASSMBL_BOM_SENS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
	:new.revision_no,
    :new.ctrl_db_id,
	:new.creation_dt,
	:new.creation_db_id,
	:new.revision_dt,
	:new.revision_db_id,
	:new.revision_user );
end;
/
