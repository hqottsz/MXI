--liquibase formatted sql


--changeSet SWA-4007:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create new INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE INT_EVENT_CONFIG
  (
    EVENT_TYPE_CD        VARCHAR2 (50) NOT NULL ,
    ENABLED_BOOL         NUMBER (1) DEFAULT 0 NOT NULL ,
    BEFORE_SNAPSHOT_BOOL NUMBER (1) DEFAULT 0 NOT NULL ,
    AFTER_SNAPSHOT_BOOL  NUMBER (1) DEFAULT 0 NOT NULL ,
    RSTAT_CD             NUMBER (3) DEFAULT 0 NOT NULL ,
    REVISION_NO          NUMBER (10) DEFAULT 1 NOT NULL ,
    CTRL_DB_ID           NUMBER (10) NOT NULL ,
    CREATION_DT          DATE NOT NULL ,
    CREATION_DB_ID       NUMBER (10) NOT NULL ,
    REVISION_DT          DATE NOT NULL ,
    REVISION_DB_ID       NUMBER (10) NOT NULL ,
    REVISION_USER        VARCHAR2 (30) NOT NULL
  ) 
  ');
  END;
/
--changeset SWA-4007:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( ENABLED_BOOL         IN (0,1)) 
');
END;
/

--changeset SWA-4007:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( BEFORE_SNAPSHOT_BOOL IN (0, 1)) 
');
END;
/

--changeset SWA-4007:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( AFTER_SNAPSHOT_BOOL  IN (0, 1)) 
');
END;
/

--changeset SWA-4007:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( RSTAT_CD             IN (0, 1, 2, 3)) 
');
END;
/

--changeset SWA-4007:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295) 
');
END;
/

--changeset SWA-4007:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295) 
');
END;
/

--changeset SWA-4007:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295) 
');
END;
/

--changeset SWA-4007:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraints INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CONSTRAINT PK_INT_EVENT_CONFIG PRIMARY KEY ( EVENT_TYPE_CD ) 
');
END;
/

--changeset SWA-4007:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FKs INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CONSTRAINT FK_INT_EVENT_CONFIG_MIM_DB FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
');
END;
/

--changeset SWA-4007:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FKs INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CONSTRAINT FK_INT_EVENT_CONFIG_MIM_DB_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
');
END;
/

--changeset SWA-4007:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FKs INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CONSTRAINT FK_INT_EVENT_CONFIG_MIM_DB_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
');
END;
/

--changeset SWA-4007:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add FKs INT_EVENT_CONFIG table
BEGIN
utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INT_EVENT_CONFIG ADD CONSTRAINT FK_INT_EVENT_CONFIG_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE 
');
END;
/

--changeset SWA-4007:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add audit triggers INT_EVENT_CONFIG table
CREATE OR REPLACE TRIGGER "TUBR_INT_EVENT_CONFIG" BEFORE UPDATE
   ON "INT_EVENT_CONFIG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
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

--changeset SWA-4007:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add audit triggers INT_EVENT_CONFIG table
CREATE OR REPLACE TRIGGER "TIBR_INT_EVENT_CONFIG" BEFORE INSERT
   ON "INT_EVENT_CONFIG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
  
 MX_TRIGGER_PKG.before_insert(
 :new.rstat_cd,
 :new.revision_no,
 :new.ctrl_db_id,
 :new.creation_dt,
 :new.creation_db_id,
 :new.revision_dt,
 :new.revision_db_id,
 :new.revision_user
 );
end;
/

--changeSet SWA-4007:16 stripComments:false
--comment insert into table int_event_config
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE INT_EVENT_CONFIG
**************************************************/
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
   SELECT
		'MX_AIRCRAFT_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_AIRCRAFT_UPDATED' );	


--changeSet SWA-4007:17 stripComments:false
--comment insert into table int_event_config
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
   SELECT
		'MX_WORK_PACKAGE_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_WORK_PACKAGE_UPDATED' );		

	

--changeSet SWA-4007:18 stripComments:false
--comment insert into table int_event_config
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
   SELECT
		'MX_TASK_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_TASK_UPDATED' );

--changeSet SWA-4007:19 stripComments:false
--comment insert into table int_event_config
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
   SELECT
		'MX_INVENTORY_DEADLINE_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_INVENTORY_DEADLINE_UPDATED' );

--changeSet SWA-4007:20 stripComments:false
--comment insert into table int_event_config
INSERT INTO 
	int_event_config 
	(
		event_type_cd,enabled_bool,before_snapshot_bool,after_snapshot_bool,rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
		'MX_FAULT_UPDATED',0,0,0,0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM int_event_config WHERE event_type_cd = 'MX_FAULT_UPDATED' );