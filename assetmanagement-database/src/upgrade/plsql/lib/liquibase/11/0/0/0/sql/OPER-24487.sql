--liquibase formatted sql

--changeset OPER-24487:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_rename('REF_DEFERRAL_REQUEST_STATUS', 'REF_REFERENCE_REQUEST_STATUS');
END;
/

--changeSet OPER-24487:2 stripComments:false
   COMMENT ON TABLE REF_REFERENCE_REQUEST_STATUS
IS
   'Contains system values for the status of a reference approval  request. Values should not be added or changed as there is logic around the existing states. ';

--changeset OPER-24487:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_rename('REF_REFERENCE_REQUEST_STATUS', 'DEFERRAL_REQUEST_STATUS_CD', 'REFERENCE_REQUEST_STATUS_CD');
END;
/

--changeset OPER-24487:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
   ALTER TABLE REF_REFERENCE_REQUEST_STATUS MODIFY "REFERENCE_REQUEST_STATUS_CD" VARCHAR2 (16) NOT NULL
');
END;
/

--changeset OPER-24487:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_drop('REF_REFERENCE_REQUEST_STATUS','FK_MIMRSTAT_REFDEFERREQSTATUS');
END;
/

--changeset OPER-24487:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_drop('REF_REFERENCE_REQUEST_STATUS','FK_REFBITMAP_REFDEFERREQSTATUS');
END;
/

--changeset OPER-24487:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_drop('SD_FAULT_DEFERRAL_REQUEST','FK_REFDEFREQSTAT_SDFAULTDEFREQ');
END;
/

--changeset OPER-24487:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_drop('REF_REFERENCE_REQUEST_STATUS','PK_REF_DEFERRAL_REQUEST_STATUS');
END;
/

--changeset OPER-24487:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_drop('PK_REF_DEFERRAL_REQUEST_STATUS');
END;
/

--changeset OPER-24487:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_REFERENCE_REQUEST_STATUS ADD CONSTRAINT PK_REF_REFERENCE_REQ_STATUS PRIMARY KEY ( REFERENCE_REQUEST_STATUS_CD )
   ');
END;
/

--changeset OPER-24487:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_REFERENCE_REQUEST_STATUS ADD CONSTRAINT FK_MIMRSTAT_REFREFREQSTATUS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-24487:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE REF_REFERENCE_REQUEST_STATUS ADD CONSTRAINT FK_REFBITMAP_REFREFREQSTATUS FOREIGN KEY ( BITMAP_DB_ID, BITMAP_TAG ) REFERENCES REF_BITMAP ( BITMAP_DB_ID, BITMAP_TAG ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-24487:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DEFER_REQ_STAT');
END;
/

--changeset OPER-24487:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DEFER_REQ_STAT');
END;
/

--changeset OPER-24487:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_REF_REQ_STAT" BEFORE INSERT
   ON "REF_REFERENCE_REQUEST_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeset OPER-24487:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_REF_REQ_STAT" BEFORE UPDATE
   ON "REF_REFERENCE_REQUEST_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
