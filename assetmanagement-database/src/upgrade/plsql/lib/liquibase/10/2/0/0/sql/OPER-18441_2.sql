--liquibase formatted sql

--changeSet OPER-18441_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_rename('REF_SENSITIVE_SYSTEM', 'REF_SENSITIVITY');
END;
/

--changeSet OPER-18441_2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_BOM_SENS_SYSTEM', 'FK_REFSENSYS_EQPASSMBLBOMSSYS');
END;
/

--changeSet OPER-18441_2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('EQP_ASSMBL_SENS_SYSTEM', 'FK_REFSENSYS_EQPASSMBLSSYS');
END;
/

--changeSet OPER-18441_2:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('REF_SENSITIVITY', 'PK_REF_SENSITIVE_SYSTEM');
END;
/

--changeSet OPER-18441_2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.index_drop('PK_REF_SENSITIVE_SYSTEM');
END;
/

-- changeSet OPER-18441_2:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  UTL_MIGR_SCHEMA_PKG.table_column_rename('REF_SENSITIVITY', 'SENSITIVE_SYSTEM_CD', 'SENSITIVITY_CD');
END;
/

-- changeSet OPER-18441_2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('REF_SENSITIVITY', 'FK_MIMDB_SENSITIVESYSTEM');
END;
/
-- changeSet OPER-18441_2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('REF_SENSITIVITY', 'FK_MIMDB_SENSITIVESYSTEM_CR');
END;
/
-- changeSet OPER-18441_2:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('REF_SENSITIVITY', 'FK_MIMDB_SENSITIVESYSTEM_CT');
END;
/
-- changeSet OPER-18441_2:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_drop('REF_SENSITIVITY', 'FK_MIMRSTAT_SENSITIVESYSTEM');
END;
/

--changeSet OPER-18441_2:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVITY ADD CONSTRAINT FK_MIMDB_SENSITIVITY_CRE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/
--changeSet OPER-18441_2:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVITY ADD CONSTRAINT FK_MIMDB_SENSITIVITY_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/
--changeSet OPER-18441_2:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVITY ADD CONSTRAINT FK_MIMDB_SENSITIVITY_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/
--changeSet OPER-18441_2:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVITY ADD CONSTRAINT FK_MIMRSTAT_SENSITIVITY FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/
--changeSet OPER-18441_2:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVITY ADD CONSTRAINT PK_REF_SENSITIVITY PRIMARY KEY ( SENSITIVITY_CD )
	');
END;
/
--changeSet OPER-18441_2:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_REFSENS_EQPBOMPARTSENS FOREIGN KEY ( SENSITIVITY_CD ) REFERENCES REF_SENSITIVITY ( SENSITIVITY_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441_4:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.trigger_drop('TIBR_REF_SENSITIVE_SYSTEM');
END;
/

--changeSet OPER-18441_4:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.trigger_drop('TUBR_REF_SENSITIVE_SYSTEM');
END;
/

--changeSet audit_triggers:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_SENSITIVITY" BEFORE UPDATE
   ON "REF_SENSITIVITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
CREATE OR REPLACE TRIGGER "TIBR_REF_SENSITIVITY" BEFORE INSERT
   ON "REF_SENSITIVITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
