--liquibase formatted sql

--changeSet OPER-18441:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create ('
		CREATE TABLE EQP_BOM_PART_SENS
        (
		  BOM_PART_DB_ID      NUMBER (10) NOT NULL ,
          BOM_PART_ID         NUMBER (10) NOT NULL ,
          SENSITIVITY_CD      VARCHAR2 (8) NOT NULL ,
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

--changeSet OPER-18441:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT PK_EQP_BOM_PART_SENS PRIMARY KEY ( BOM_PART_DB_ID, BOM_PART_ID, SENSITIVITY_CD )
	');
END;
/

--changeSet OPER-18441:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295 )
    ');
END;
/

--changeSet OPER-18441:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-18441:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-18441:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-18441:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_EQPBOMPART_BOMPARTSENS FOREIGN KEY ( BOM_PART_DB_ID, BOM_PART_ID ) REFERENCES EQP_BOM_PART ( BOM_PART_DB_ID, BOM_PART_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_MIMDB_EQPBOMPARTSENS_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_MIMDB_EQPBOMPARTSENS_CRE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_MIMDB_EQPBOMPARTSENS_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-18441:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE EQP_BOM_PART_SENS ADD CONSTRAINT FK_MIMRSTAT_EQPBOMPARTSENS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/


--changeSet OPER-18441:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQP_BOM_PART_SENS" BEFORE UPDATE
   ON "EQP_BOM_PART_SENS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-18441:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_BOM_PART_SENS" BEFORE INSERT
   ON "EQP_BOM_PART_SENS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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