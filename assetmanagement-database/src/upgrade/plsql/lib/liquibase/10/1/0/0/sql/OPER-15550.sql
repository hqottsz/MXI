--liquibase formatted sql

--changeSet OPER-15550:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create ('
		CREATE TABLE EQP_ASSMBL_BOM_SENS_SYSTEM
        (
          ASSMBL_DB_ID        NUMBER (10) NOT NULL ,
          ASSMBL_CD           VARCHAR2 (8) NOT NULL ,
          ASSMBL_BOM_ID       NUMBER (10) NOT NULL ,
          SENSITIVE_SYSTEM_CD VARCHAR2 (8) NOT NULL ,
          CTRL_DB_ID          NUMBER (10) NOT NULL ,
          RSTAT_CD            NUMBER (3) NOT NULL ,
          CREATION_DT         DATE NOT NULL ,
          CREATION_DB_ID      NUMBER (10) NOT NULL ,
          REVISION_NO         NUMBER (10) NOT NULL ,
          REVISION_DT         DATE NOT NULL ,
          REVISION_DB_ID      NUMBER (10) NOT NULL ,
          REVISION_USER       VARCHAR2 (30) NOT NULL
        )
	');
END;
/

--changeSet OPER-15550:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT PK_EQPASSMBLBOM_SENSSYSTEM PRIMARY KEY ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, SENSITIVE_SYSTEM_CD ) 
');
END;
/

--changeSet OPER-15550:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295 )
    ');
END;
/

--changeSet OPER-15550:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3)) 
	');
END;
/

--changeSet OPER-15550:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15550:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295) 
	');
END;
/

--changeSet OPER-15550:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_EQPASSMBLBOM_SENSSYSTEM FOREIGN KEY ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID ) REFERENCES EQP_ASSMBL_BOM ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15550:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_MIMDB_ASSMBLBOMSENSYS_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15550:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_MIMDB_ASSMBLBOMSENSYS_CRE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
	');
END;
/

--changeSet OPER-15550:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_MIMDB_ASSMBLBOMSENSYS_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15550:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_ASSMBL_BOM_SENS_SYSTEM ADD CONSTRAINT FK_MIMRSTAT_EQPASSMBLBOMSSYS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15550:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQP_ASSMBL_BOM_SEN_SYS" BEFORE UPDATE
   ON "EQP_ASSMBL_BOM_SENS_SYSTEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15550:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_BOM_SEN_SYS" BEFORE INSERT
   ON "EQP_ASSMBL_BOM_SENS_SYSTEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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