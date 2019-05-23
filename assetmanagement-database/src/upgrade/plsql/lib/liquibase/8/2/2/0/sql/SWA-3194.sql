--liquibase formatted sql

--changeSet SWA-3914:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_create('
  CREATE TABLE PUBSUB_CONFIG(
    PUBSUB_CONFIG_ID RAW (16) NOT NULL,
    KEY            VARCHAR2 (128),
    VALUE          VARCHAR2 (255),
    CREATION_DT    DATE NOT NULL,
    RSTAT_CD       NUMBER (3) NOT NULL,
    REVISION_NO    NUMBER (10) NOT NULL,
    CTRL_DB_ID     NUMBER (10) NOT NULL,
    CREATION_DB_ID NUMBER (10) NOT NULL,
    REVISION_DT    DATE NOT NULL,
    REVISION_DB_ID NUMBER (10) NOT NULL,
    REVISION_USER  VARCHAR2 (30) NOT NULL,
    CHECK ( RSTAT_CD IN (0, 1, 2, 3)),
    CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295),
    CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295),
    CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295),
    CONSTRAINT PK_PUBSUB_CONFIG_PK PRIMARY KEY ( PUBSUB_CONFIG_ID ),
    CONSTRAINT UK_PUBSUB_CONFIG_KEY UNIQUE ( KEY ),
    CONSTRAINT FK_PUBSUB_CONFIG_MIM_DB FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE,
    CONSTRAINT FK_PUBSUB_CONFIG_MIM_DB_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE,
    CONSTRAINT FK_PUBSUB_CONFIG_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
  )');
END;
/

--changeSet SWA-2539:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PUBSUB_CONFIG" BEFORE UPDATE
   ON "PUBSUB_CONFIG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet SWA-2539:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PUBSUB_CONFIG" BEFORE INSERT
   ON "PUBSUB_CONFIG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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