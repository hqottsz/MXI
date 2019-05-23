--liquibase formatted sql

--changeSet OPER-22555:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE REF_QUICKTEXT_TYPE
		(
			QUICKTEXT_TYPE_CD VARCHAR2 (16) NOT NULL ,
			DESC_SDESC        VARCHAR2 (80) NOT NULL ,
			DESC_LDESC        VARCHAR2 (4000) NOT NULL ,
			RSTAT_CD          NUMBER (3) NOT NULL ,
			REVISION_NO       NUMBER (10) NOT NULL ,
			CTRL_DB_ID        NUMBER (10) NOT NULL ,
			CREATION_DT       DATE NOT NULL ,
			REVISION_DT       DATE NOT NULL ,
			REVISION_DB_ID    NUMBER (10) NOT NULL ,
			REVISION_USER     VARCHAR2 (30) NOT NULL
		)
	');
END;
/

--changeSet OPER-22555:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_QUICKTEXT_TYPE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-22555:3 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.QUICKTEXT_TYPE_CD IS 'Primary key for the reference term.' ;

--changeSet OPER-22555:4 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.DESC_SDESC IS 'Name for the term.' ;

--changeSet OPER-22555:5 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.DESC_LDESC IS 'Description for the term, useful for tooltips and help text.' ;

--changeSet OPER-22555:6 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.RSTAT_CD IS 'Status of the record.' ;

--changeSet OPER-22555:7 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.REVISION_NO IS 'A number incremented each time the record is modified.' ;

--changeSet OPER-22555:8 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.CTRL_DB_ID IS 'The identifier of the database that owns the record.' ;

--changeSet OPER-22555:9 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.CREATION_DT IS 'The date and time at which the record was inserted.' ;

--changeSet OPER-22555:10 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.REVISION_DT IS 'The date and time at which the record was last updated.' ;

--changeSet OPER-22555:11 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.REVISION_DB_ID IS 'The identifier of the database that last updated the record.' ;

--changeSet OPER-22555:12 stripComments:false
COMMENT ON COLUMN REF_QUICKTEXT_TYPE.REVISION_USER IS 'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;

--changeSet OPER-22555:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.index_create('
		CREATE UNIQUE INDEX IX_REF_QUICKTEXT_TYPE_NK ON REF_QUICKTEXT_TYPE
		(
			QUICKTEXT_TYPE_CD ASC , CTRL_DB_ID ASC
		)
		LOGGING
	');
END;
/

--changeSet OPER-22555:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_QUICKTEXT_TYPE ADD CONSTRAINT PK_REF_QUICKTEXT_TYPE PRIMARY KEY ( QUICKTEXT_TYPE_CD )
	');
END;
/

--changeSet OPER-22555:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_QUICKTEXT_TYPE ADD CONSTRAINT FK_REFQUICKTEXTTYPE_MIMDB_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22555:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_QUICKTEXT_TYPE ADD CONSTRAINT FK_REFQUICKTEXTTYPE_MIMDB_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22555:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE REF_QUICKTEXT_TYPE ADD CONSTRAINT FK_REFQUICKTEXTTYPE_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/

--changeSet OPER-22555:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE UTL_QUICKTEXT
		(
			QUICKTEXT_ID    NUMBER (10) NOT NULL ,
			QUICKTEXT_DB_ID NUMBER (10) NOT NULL ,
			TYPE_CD         VARCHAR2 (16) NOT NULL ,
			VALUE_MDESC     VARCHAR2 (200 CHAR) NOT NULL ,
			RSTAT_CD        NUMBER (3) NOT NULL ,
			REVISION_NO     NUMBER (10) NOT NULL ,
			CTRL_DB_ID      NUMBER (10) NOT NULL ,
			CREATION_DT     DATE NOT NULL ,
			REVISION_DT     DATE NOT NULL ,
			REVISION_DB_ID  NUMBER (10) NOT NULL ,
			REVISION_USER   VARCHAR2 (30) NOT NULL
		)
	');
END;
/

--changeSet OPER-22555:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-22555:20 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.QUICKTEXT_ID IS 'Unique identifier serving as part of the primary key, and assigned from the sequence QUICKTEXT_ID_SEQ.' ;

--changeSet OPER-22555:21 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.QUICKTEXT_DB_ID IS 'Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the record''s primary key.' ;

--changeSet OPER-22555:22 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.TYPE_CD IS 'The type code referring to the type of Quick Text entry as specified in REF_QUICKTEXT_TYPE.' ;

--changeSet OPER-22555:23 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.VALUE_MDESC IS 'The Quick Text value entry.' ;

--changeSet OPER-22555:24 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.RSTAT_CD IS 'Status of the record.' ;

--changeSet OPER-22555:25 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.REVISION_NO IS 'A number incremented each time the record is modified.' ;

--changeSet OPER-22555:26 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.CTRL_DB_ID IS 'The identifier of the database that owns the record.' ;

--changeSet OPER-22555:27 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.CREATION_DT IS 'The date and time at which the record was inserted.' ;

--changeSet OPER-22555:28 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.REVISION_DT IS 'The date and time at which the record was last updated.' ;

--changeSet OPER-22555:29 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.REVISION_DB_ID IS 'The identifier of the database that last updated the record.' ;

--changeSet OPER-22555:30 stripComments:false
COMMENT ON COLUMN UTL_QUICKTEXT.REVISION_USER IS 'The user that last modified the record. The user is either a) the user that logged into Maintenix - pushed into the database from the client or b) if the record update is not via a Maintenix application, the user is the Oracle user name that is processing the transaction.' ;

--changeSet OPER-22555:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.index_create('
		CREATE UNIQUE INDEX IX_UTL_QUICKTEXT_NK ON UTL_QUICKTEXT
		(
			VALUE_MDESC ASC , TYPE_CD ASC
		)
		LOGGING
	');
END;
/

--changeSet OPER-22555:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	upg_migr_schema_v1_pkg.index_create('
		CREATE INDEX IX_UTL_QUICKTEXT_TYPE_FK ON UTL_QUICKTEXT
		(
			TYPE_CD ASC
		)
		LOGGING
	');
END;
/

--changeSet OPER-22555:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT PK_UTL_QUICKTEXT PRIMARY KEY ( QUICKTEXT_ID, QUICKTEXT_DB_ID )
	');
END;
/

--changeSet OPER-22555:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT FK_UTLQUICKTEXT_MIMDB_CTRL FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22555:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT FK_UTLQUICKTEXT_MIMDB_DBID FOREIGN KEY ( QUICKTEXT_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22555:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT FK_UTLQUICKTEXT_MIMDB_REV FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-22555:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT FK_UTLQUICKTEXT_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/

--changeSet OPER-22555:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE UTL_QUICKTEXT ADD CONSTRAINT FK_UTLQUICKTEXT_REFTYPE FOREIGN KEY ( TYPE_CD ) REFERENCES REF_QUICKTEXT_TYPE ( QUICKTEXT_TYPE_CD )
	');
END;
/

--changeSet OPER-22555:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_QUICKTEXT_TYPE" BEFORE INSERT
   ON "REF_QUICKTEXT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
END;
/

--changeSet OPER-22555:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_QUICKTEXT_TYPE" BEFORE UPDATE
   ON "REF_QUICKTEXT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/

--changeSet OPER-22555:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_QUICKTEXT" BEFORE INSERT
   ON "UTL_QUICKTEXT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
END;
/

--changeSet OPER-22555:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_QUICKTEXT" BEFORE UPDATE
   ON "UTL_QUICKTEXT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/

--changeset OPER-22555:43 stripComments:false 
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
	SELECT 'UTL_QUICKTEXT_ID_SEQ', 1, 'UTL_QUICKTEXT', 'QUICKTEXT_ID' , 1, 0
	FROM dual WHERE NOT EXISTS (SELECT 1 FROM utl_sequence WHERE sequence_cd = 'UTL_QUICKTEXT_ID_SEQ');

--changeset OPER-22555:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('UTL_QUICKTEXT_ID_SEQ', 1);
END;
/

--changeset OPER-22555:45 stripComments:false 
INSERT INTO ref_quicktext_type (quicktext_type_cd, desc_sdesc, desc_ldesc, rstat_cd, ctrl_db_id, creation_dt, revision_no, revision_dt, revision_db_id, revision_user)
	SELECT 'MX_ENG_STEPDESC', 'Engineering Step Description', 'Step descriptions that can be used to quickly add steps to tasks.', 0, 0, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 1, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 0, 'MXI'
	FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_quicktext_type WHERE quicktext_type_cd = 'MX_ENG_STEPDESC');

--changeset OPER-22555:46 stripComments:false 
INSERT INTO ref_quicktext_type (quicktext_type_cd, desc_sdesc, desc_ldesc, rstat_cd, ctrl_db_id, creation_dt, revision_no, revision_dt, revision_db_id, revision_user)
	SELECT 'MX_MTC_ACTION', 'Maintenance Action', 'Action notes that can be used to quickly add actions to work capture.', 0, 0, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 1, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 0, 'MXI'
	FROM dual WHERE NOT EXISTS (SELECT 1 FROM ref_quicktext_type WHERE quicktext_type_cd = 'MX_MTC_ACTION');