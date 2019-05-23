--liquibase formatted sql

--changeSet OPER-25988:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_create('
		CREATE TABLE INV_LOC_ZONE
		  (
			LOC_DB_ID      NUMBER (10) NOT NULL ,
			LOC_ID         NUMBER (10) NOT NULL ,
			ROUTE_ORDER    NUMBER (10) NOT NULL ,
			RSTAT_CD       NUMBER (3) NOT NULL ,
			REVISION_NO    NUMBER (10) NOT NULL ,
			CTRL_DB_ID     NUMBER (10) NOT NULL ,
			CREATION_DT    DATE NOT NULL ,
			CREATION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_DT    DATE NOT NULL ,
			REVISION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_USER  VARCHAR2 (30) NOT NULL
		  )
    ');
END;
/

--changeSet OPER-25988:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT PK_INV_LOC_ZONE PRIMARY KEY ( LOC_DB_ID, LOC_ID ) 
	');
END;
/

--changeSet OPER-25988:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3)) 
	');
END;
/

--changeSet OPER-25988:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT FK_INVLOC_INVLOCZONE FOREIGN KEY ( LOC_DB_ID, LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) DEFERRABLE 
	');
END;
/

--changeSet OPER-25988:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT FK_MIMDB_INVLOCZONE FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE 
	');
END;
/

--changeSet OPER-25988:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT FK_MIMDB_INVLOCZONEv2 FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE 
	');
END;
/

--changeSet OPER-25988:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT FK_MIMDB_INVLOCZONEv3 FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) DEFERRABLE 
	');
END;
/

--changeSet OPER-25988:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add ('
		ALTER TABLE INV_LOC_ZONE ADD CONSTRAINT FK_MIMRSTAT_INVLOCZONE FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) DEFERRABLE 
	');
END;
/

--changeSet OPER-25988:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_LOC_ZONE" BEFORE UPDATE
   ON "INV_LOC_ZONE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
	mx_trigger_pkg.before_update(
		:old.rstat_cd,
		:new.rstat_cd,
		:old.revision_no,
		:new.revision_no,
		:new.revision_dt,
		:new.revision_db_id,
		:new.revision_user 
	);
END;
/

--changeSet OPER-25988:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_ZONE" BEFORE INSERT
   ON "INV_LOC_ZONE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
	mx_trigger_pkg.before_insert(
		:new.rstat_cd,
		:new.revision_no,
		:new.ctrl_db_id,
		:new.creation_dt,
		:new.creation_db_id,
		:new.revision_dt,
		:new.revision_db_id,
		:new.revision_user
	);
END;
/