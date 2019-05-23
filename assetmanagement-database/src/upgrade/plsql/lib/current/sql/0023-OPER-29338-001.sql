--liquibase formatted sql

--changeSet OPER-29338-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_create ('
    CREATE TABLE REF_USG_SNAPSHOT_SRC_TYPE
    (
    SOURCE_DB_ID   NUMBER (10) NOT NULL ,
    SOURCE_CD      VARCHAR2 (20) NOT NULL ,
    SOURCE_SDESC   VARCHAR2 (80) ,
    SOURCE_LDESC   VARCHAR2 (4000) ,
    RSTAT_CD       NUMBER (3) NOT NULL ,
    REVISION_NO    NUMBER (10) NOT NULL ,
    CTRL_DB_ID     NUMBER (10) NOT NULL ,
    CREATION_DB_ID NUMBER (10) NOT NULL ,
    CREATION_DT    DATE NOT NULL ,
    REVISION_DT    DATE NOT NULL ,
    REVISION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_USER  VARCHAR2 (30) NOT NULL
    )
   ');
END;
/

--changeset OPER-29338-1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
    ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3) )
   ');
END;
/

--changeset OPER-29338-1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
    ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CONSTRAINT PK_REF_USG_SNAPSHOT_SRC_TYPE PRIMARY KEY ( SOURCE_DB_ID, SOURCE_CD )
   ');
END;
/

--changeset OPER-29338-1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CONSTRAINT FK_USGSNAPSHOTSRC_MIMDB FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-29338-1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CONSTRAINT FK_USGSNAPSHOTSRC_MIMDBCR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-29338-1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
    ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CONSTRAINT FK_USGSNAPSHOTSRC_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-29338-1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
    ALTER TABLE REF_USG_SNAPSHOT_SRC_TYPE ADD CONSTRAINT FK_USGSNAPSHOTSRC_MIMDBCT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-29338-1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_USG_SNAPSHOT_SRC_TYPE" BEFORE INSERT
   ON "REF_USG_SNAPSHOT_SRC_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-29338-1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_USG_SNAPSHOT_SRC_TYPE" BEFORE UPDATE
   ON "REF_USG_SNAPSHOT_SRC_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-29338-1:12 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.SOURCE_DB_ID IS 'PK to the REF_USG_SNAPSHOT_SRC_TYPE table.';

--changeset OPER-29338-1:13 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.SOURCE_CD IS 'PK to the REF_USG_SNAPSHOT_SRC_TYPE table.';

--changeset OPER-29338-1:14 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.SOURCE_SDESC IS 'A short description of the usage snapshot source.';

--changeset OPER-29338-1:15 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.SOURCE_LDESC IS 'A long description of the usage snapshot source.';

--changeset OPER-29338-1:16 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.RSTAT_CD IS 'The physical attribute that defines the read and write access of the record.';

--changeset OPER-29338-1:17 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.REVISION_NO IS 'A number incremented each time the record is modified.';

--changeset OPER-29338-1:18 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.CTRL_DB_ID IS 'The identifier of the database that owns the record.';

--changeset OPER-29338-1:19 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.CREATION_DB_ID IS 'The identifier of the database that inserted the record.';

--changeset OPER-29338-1:20 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.CREATION_DT IS 'The database server timestamp captured at the time the record was inserted.';

--changeset OPER-29338-1:21 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.REVISION_DT IS 'The date and time at which the record was updated.';

--changeset OPER-29338-1:22 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.REVISION_DB_ID IS 'The identifier of the database that last updated the record.';

--changeset OPER-29338-1:23 stripComments:false
COMMENT ON COLUMN REF_USG_SNAPSHOT_SRC_TYPE.REVISION_USER IS 'The name of the user that last updated the record.';

--changeset OPER-29338-1:24 stripComments:false
COMMENT ON TABLE REF_USG_SNAPSHOT_SRC_TYPE IS 'Usage Snapshot Source Type: This table identifies the two sources of usage snapshots: automatically calculated by IFS Maintenix or manually entered by a user.';