--liquibase formatted sql

--changeSet OPER-27728:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE EXT_REF_ITEM
			  (
				  EXT_REF_ITEM_DB_ID NUMBER (10) NOT NULL ,
				  EXT_REF_ITEM_ID    NUMBER (10) NOT NULL ,
				  REFERENCE_ITEM_NAME     VARCHAR2 (40),
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

--changeSet OPER-27728:2 stripComments:false
COMMENT ON COLUMN EXT_REF_ITEM.EXT_REF_ITEM_DB_ID IS 'Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the record''s primary key.';

--changeSet OPER-27728:3 stripComments:false
COMMENT ON COLUMN EXT_REF_ITEM.EXT_REF_ITEM_ID IS 'Unique identifier serving as part of the primary key. Identifies the reference code.' ;

--changeSet OPER-27728:4 stripComments:false
COMMENT ON COLUMN EXT_REF_ITEM.REFERENCE_ITEM_NAME IS 'Identifies the reference item.' ;


--changeSet OPER-27728:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE EXT_REF_ITEM ADD CONSTRAINT PK_EXT_REF_ITEM PRIMARY KEY (EXT_REF_ITEM_ID, EXT_REF_ITEM_DB_ID )
	');
END;
/

--changeSet OPER-27728:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE EXT_REF_ITEM ADD CONSTRAINT IX_EXTREFITEMNAME_UNQ UNIQUE ( REFERENCE_ITEM_NAME ) 
	');
END;
/

--changeSet OPER-27728:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE EXT_REF_ITEM ADD CONSTRAINT FK_MIMDB_EXTREFITEM_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
	');
END;
/

--changeSet OPER-27728:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE EXT_REF_ITEM ADD CONSTRAINT FK_MIMDB_EXTREFITEM_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-27728:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE EXT_REF_ITEM ADD CONSTRAINT FK_MIMRSTAT_EXTREFITEM FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
	');
END;
/

--changeSet OPER-27728:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE EXT_REF_ITEM ADD CHECK ( RSTAT_CD IN (0,1,2,3))
   ');
END;
/

--changeSet OPER-27728:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SCHED_PART ADD (EXT_REF_ITEM_DB_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-27728:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE SCHED_PART ADD (EXT_REF_ITEM_ID NUMBER(10))
   ');
END;
/

--changeSet OPER-27728:13 stripComments:false
COMMENT ON COLUMN SCHED_PART.EXT_REF_ITEM_DB_ID IS 'FK to EXT_REF_ITEM. The reference associated to the part request.';

--changeSet OPER-27728:14 stripComments:false
COMMENT ON COLUMN SCHED_PART.EXT_REF_ITEM_ID IS 'FK to EXT_REF_ITEM. The reference associated to the part request.' ;

--changeSet OPER-27728:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE SCHED_PART ADD CONSTRAINT FK_EXTREFITEM_SCHEDPART FOREIGN KEY ( EXT_REF_ITEM_ID, EXT_REF_ITEM_DB_ID ) REFERENCES EXT_REF_ITEM ( EXT_REF_ITEM_ID, EXT_REF_ITEM_DB_ID )
	');
END;
/

--changeSet OPER-27728:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_EXTREFITEM_SCHEDPART ON SCHED_PART
        (
          EXT_REF_ITEM_ID ASC ,
          EXT_REF_ITEM_DB_ID ASC
        )
   ');
END;
/

--changeSet OPER-27728:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EXT_REF_ITEM" BEFORE UPDATE
   ON "EXT_REF_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-27728:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EXT_REF_ITEM" BEFORE INSERT
   ON "EXT_REF_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-27728:19 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
SELECT 'EXT_REF_ITEM_ID_SEQ', 1, 'EXT_REF_ITEM', 'EXT_REF_ITEM_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'EXT_REF_ITEM_ID_SEQ');
 
--changeSet OPER-27728:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('EXT_REF_ITEM_ID_SEQ', 1);
END;
/

