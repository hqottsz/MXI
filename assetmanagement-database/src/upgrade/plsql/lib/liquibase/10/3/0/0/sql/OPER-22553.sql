--liquibase formatted sql

--changeSet OPER-22553:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE INV_DAMAGE
			  (
				DAMAGE_DB_ID   NUMBER (10) NOT NULL ,
				DAMAGE_ID      NUMBER (10) NOT NULL ,
				INV_NO_DB_ID   NUMBER (10) NOT NULL ,
				INV_NO_ID      NUMBER (10) NOT NULL ,
				FAULT_DB_ID    NUMBER (10) ,
				FAULT_ID       NUMBER (10) ,
				LOCATION_MDESC VARCHAR2 (200) NOT NULL,
				ALT_ID RAW (16) NOT NULL ,
				RSTAT_CD       NUMBER (3) NOT NULL ,
				REVISION_NO    NUMBER (10) NOT NULL ,
				CTRL_DB_ID     NUMBER (10) NOT NULL ,
				CREATION_DT    DATE NOT NULL ,
				REVISION_DT    DATE NOT NULL ,
				REVISION_DB_ID NUMBER (10) NOT NULL ,
				REVISION_USER  VARCHAR2 (30) NOT NULL
			  )
	'	);
END;
/

--changeSet OPER-22553:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-22553:3 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.DAMAGE_DB_ID IS ' Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the record''s primary key.' ;

--changeSet OPER-22553:4 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.DAMAGE_ID IS 'Unique identifier serving as part of the primary key, and assigned from the sequence DAMAGE_LOC_ID_SEQ.' ;

--changeSet OPER-22553:5 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.INV_NO_DB_ID IS 'FK to inv_inv. the inventory that the damage location was recorded against.' ;

--changeSet OPER-22553:6 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.INV_NO_ID IS 'FK to inv_inv. the inventory that the damage location was recorded against.';

--changeSet OPER-22553:7 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.FAULT_DB_ID IS 'FK to sd_fault. the fault that the damage location was recorded against.';

--changeSet OPER-22553:8 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.FAULT_ID IS 'FK to sd_fault. the fault that the damage location was recorded against.';

--changeSet OPER-22553:9 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.LOCATION_MDESC IS 'Records location information.';

--changeSet OPER-22553:10 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.ALT_ID IS 'The alternate key is useful for identifying a specific row in the database when dealing with integrations and Maintenix front ends.  The alternate key can is unique to each entity and acts as a surrogate key. The alternate key is intended to be system generated.' ;

--changeSet OPER-22553:11 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.RSTAT_CD IS 'Status of the record.';

--changeSet OPER-22553:12 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.REVISION_NO IS 'A number incremented each time the record is modified.';

--changeSet OPER-22553:13 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.CTRL_DB_ID IS 'The identifier of the database that owns the record. The meaning of this column may be specific to the entity';

--changeSet OPER-22553:14 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.CREATION_DT IS 'The date and time at which the record was inserted.';

--changeSet OPER-22553:15 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.REVISION_DT IS 'The date and time at which the record was last updated.';

--changeSet OPER-22553:16 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.REVISION_DB_ID IS 'The identifier of the database that last updated the record.';

--changeSet OPER-22553:17 stripComments:false
COMMENT ON COLUMN INV_DAMAGE.REVISION_USER IS 'The name of the user that last updated the record.';

--changeSet OPER-22553:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT PK_INV_DAMAGE PRIMARY KEY ( DAMAGE_ID, DAMAGE_DB_ID )
	');
END;
/

--changeSet OPER-22553:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT FK_INVINV_INVDAMAGE FOREIGN KEY ( INV_NO_DB_ID, INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID )
	');
END;
/

--changeSet OPER-22553:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT FK_MIMDB_INVDAMAGE_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22553:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT FK_MIMDB_INVDAMAGE_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/
--changeSet OPER-22553:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT FK_MIMRSTAT_INVDAMAGE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/

--changeSet OPER-22553:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_DAMAGE ADD CONSTRAINT FK_SDFAULT_INVDAMAGE FOREIGN KEY ( FAULT_DB_ID, FAULT_ID ) REFERENCES SD_FAULT ( FAULT_DB_ID, FAULT_ID )
	');
END;
/

--changeset OPER-22553:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_INVINV_INVDAMAGE ON INV_DAMAGE
        (
          INV_NO_ID ASC ,
          INV_NO_DB_ID ASC
        )
        LOGGING
   ');
END;
/
 
 
--changeset OPER-22553:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE UNIQUE INDEX IX_SDFAULT_INVDAMAGE ON INV_DAMAGE
        (
          FAULT_ID ASC ,
		  FAULT_DB_ID ASC
        )
        LOGGING
   ');
END;
/

--changeSet OPER-22553:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_DAMAGE" BEFORE UPDATE
   ON "INV_DAMAGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-22553:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_DAMAGE" BEFORE INSERT
   ON "INV_DAMAGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin
 
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet OPER-22553:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_DAMAGE_ALT_ID" BEFORE INSERT
   ON "INV_DAMAGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet OPER-22553:29 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
SELECT 'INV_DAMAGE_ID_SEQ', 1, 'INV_DAMAGE', 'DAMAGE_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'INV_DAMAGE_ID_SEQ');
 
--changeset OPER-22553:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('INV_DAMAGE_ID_SEQ', 1);
END;
/