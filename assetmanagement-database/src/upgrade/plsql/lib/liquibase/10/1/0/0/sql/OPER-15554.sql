--liquibase formatted sql

--changeSet OPER-15554:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create ('
		CREATE TABLE REF_SENSITIVE_SYSTEM
		  (
			SENSITIVE_SYSTEM_CD VARCHAR2 (8) NOT NULL ,
			DESC_SDESC          VARCHAR2 (80) ,
			DESC_LDESC          VARCHAR2 (4000) ,
			ORD_ID              NUMBER (8) ,
			WARNING_LDESC       VARCHAR2 (4000) ,
			ACTIVE_BOOL         NUMBER (1) ,
			RSTAT_CD            NUMBER (3) NOT NULL ,
			CTRL_DB_ID          NUMBER (10) NOT NULL ,
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

--changeSet OPER-15554:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CONSTRAINT PK_REF_SENSITIVE_SYSTEM PRIMARY KEY ( SENSITIVE_SYSTEM_CD ) 
');
END;
/

--changeSet OPER-15554:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-15554:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15554:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15554:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-15554:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CONSTRAINT FK_MIMDB_SENSITIVESYSTEM FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15554:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CONSTRAINT FK_MIMDB_SENSITIVESYSTEM_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15554:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
ALTER TABLE REF_SENSITIVE_SYSTEM ADD CONSTRAINT FK_MIMDB_SENSITIVESYSTEM_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
');
END;
/

--changeSet OPER-15554:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_constraint_add('
		ALTER TABLE REF_SENSITIVE_SYSTEM ADD CONSTRAINT FK_MIMRSTAT_SENSITIVESYSTEM FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-15554:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_SENSITIVE_SYSTEM" BEFORE UPDATE
   ON "REF_SENSITIVE_SYSTEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15554:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_SENSITIVE_SYSTEM" BEFORE INSERT
   ON "REF_SENSITIVE_SYSTEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-15554:13 stripComments:false
INSERT INTO ref_sensitive_system (sensitive_system_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
SELECT 'CAT_III', 'CAT III', 'Category III Instrument Landing System', 10, 'This system is CAT III compliance sensitive - the aircraft may require recertification.', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_sensitive_system WHERE ref_sensitive_system.sensitive_system_cd = 'CAT_III');

--changeSet OPER-15554:14 stripComments:false
INSERT INTO ref_sensitive_system (sensitive_system_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
SELECT 'ETOPS', 'ETOPS', 'Extended Operations', 20, 'This system is ETOPS compliance sensitive - the aircraft may require recertification.', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_sensitive_system WHERE ref_sensitive_system.sensitive_system_cd = 'ETOPS');

--changeSet OPER-15554:15 stripComments:false
INSERT INTO ref_sensitive_system (sensitive_system_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
SELECT 'RII', 'RII', 'Required Inspection Item', 30, 'This system is RII compliance sensitive - the aircraft may require recertification.', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_sensitive_system WHERE ref_sensitive_system.sensitive_system_cd = 'RII');

--changeSet OPER-15554:16 stripComments:false
INSERT INTO ref_sensitive_system (sensitive_system_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
SELECT 'FCBS', 'FCBS', 'Fatigue Critical Baseline Structure', 40, 'This system is FCBS compliance sensitive - the aircraft may require recertification.', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_sensitive_system WHERE ref_sensitive_system.sensitive_system_cd = 'FCBS');

--changeSet OPER-15554:17 stripComments:false
INSERT INTO ref_sensitive_system (sensitive_system_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
SELECT 'RVSM', 'RVSM', 'Reduced Vertical Separation Minimum', 50, 'This system is RVSM compliance sensitive - the aircraft may require recertification.', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_sensitive_system WHERE ref_sensitive_system.sensitive_system_cd = 'RVSM');