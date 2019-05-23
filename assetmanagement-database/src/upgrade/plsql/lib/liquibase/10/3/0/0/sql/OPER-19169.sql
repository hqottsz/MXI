--liquibase formatted sql

--changeSet OPER-19169:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_create('
CREATE TABLE INV_LOC_PREF_MAP
  (
    LOC_DB_ID      NUMBER (10) NOT NULL ,
    LOC_ID         NUMBER (10) NOT NULL ,
    PREF_LOC_DB_ID NUMBER (10) NOT NULL ,
    PREF_LOC_ID    NUMBER (10) NOT NULL ,
    PRIORITY_ORD       NUMBER (10) NOT NULL ,
    RSTAT_CD       NUMBER (3) DEFAULT 0 NOT NULL ,
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


--changeSet OPER-19169:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( LOC_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/


--changeSet OPER-19169:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( LOC_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-19169:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( PREF_LOC_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-19169:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( PREF_LOC_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-19169:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( PRIORITY_ORD BETWEEN 1 AND 2)
   ');
END;
/

--changeSet OPER-19169:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-19169:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-19169:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295) 
   ');
END;
/

--changeSet OPER-19169:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-19169:11 stripComments:false 
COMMENT ON TABLE INV_LOC_PREF_MAP
IS
  'Mapping of maintenance locations to preferred servicable store locations.';
  
--changeSet OPER-19169:12 stripComments:false 
COMMENT ON COLUMN INV_LOC_PREF_MAP.LOC_DB_ID
IS
  'FK to INV_LOC' ;

--changeSet OPER-19169:13 stripComments:false 
COMMENT ON COLUMN INV_LOC_PREF_MAP.LOC_ID
IS
  'FK to INV_LOC' ;


--changeSet OPER-19169:14 stripComments:false 
COMMENT ON COLUMN INV_LOC_PREF_MAP.PREF_LOC_DB_ID
IS
  'FK to preferred INV_LOC' ;


--changeSet OPER-19169:15 stripComments:false 
COMMENT ON COLUMN INV_LOC_PREF_MAP.PREF_LOC_ID
IS
  'FK to preferred INV_LOC' ;

  
--changeSet OPER-19169:16 stripComments:false 
  COMMENT ON COLUMN INV_LOC_PREF_MAP.PRIORITY_ORD
IS
  'Priority order of location PREF_LOC_ID given location LOC_ID' ;


--changeSet OPER-19169:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
     CREATE INDEX IX_INV_LOC_PREF_MAP ON INV_LOC_PREF_MAP
     (
        LOC_DB_ID ASC ,
        LOC_ID ASC
     )
');
END;
/

--changeSet OPER-19169:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
     CREATE INDEX IX_PREF_LOC_INV_LOC ON INV_LOC_PREF_MAP
     (
        PREF_LOC_DB_ID ASC ,
        PREF_LOC_ID ASC
     )
');
END;
/

--changeSet OPER-19169:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT PK_INV_LOC_PREF_MAP PRIMARY KEY ( LOC_DB_ID, LOC_ID, PREF_LOC_DB_ID, PREF_LOC_ID )
   ');
END;
/

--changeSet OPER-19169:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT IX_INV_LOC_PREF_MAP_UNQ UNIQUE ( LOC_DB_ID , LOC_ID , PRIORITY_ORD )
   ');
END;
/

--changeSet OPER-19169:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_INVLOC_INVLOCPREFMAP_LOC FOREIGN KEY ( LOC_DB_ID, LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-19169:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_INVLOC_INVLOCPREFMAP_PRE FOREIGN KEY ( PREF_LOC_DB_ID, PREF_LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-19169:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_MIMDB_INVLOCPREFMAP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-19169:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_MIMDB_INVLOCPREFMAP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-19169:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_MIMDB_INVLOCPREFMAP_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-19169:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE INV_LOC_PREF_MAP ADD CONSTRAINT FK_MIMRSTAT_INVLOCPREFMAP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/


--changeSet OPER-19169:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_LOC_PREF_MAP" BEFORE UPDATE
   ON "INV_LOC_PREF_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-19169:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_PREF_MAP" BEFORE INSERT
   ON "INV_LOC_PREF_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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