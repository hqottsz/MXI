--liquibase formatted sql

--changeset OPER-29338-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE EVT_INV_USAGE ADD (SOURCE_DB_ID NUMBER (10))
	');
END;
/

--changeset OPER-29338-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE EVT_INV_USAGE ADD (SOURCE_CD VARCHAR2 (20))
	');
END;
/

--changeset OPER-29338-2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
	ALTER TABLE EVT_INV_USAGE ADD CONSTRAINT FK_USGSNAPSHOTSRC_EVTINVUSAGE FOREIGN KEY ( SOURCE_DB_ID, SOURCE_CD ) REFERENCES REF_USG_SNAPSHOT_SRC_TYPE ( SOURCE_DB_ID, SOURCE_CD ) NOT DEFERRABLE
	');
END;
/

--changeset OPER-29338-2:4 stripComments:false
COMMENT ON COLUMN EVT_INV_USAGE.SOURCE_DB_ID IS 'FK to the REF_USG_SNAPSHOT_SRC_TYPE table. Determines the source of the usage snapshot.';

--changeset OPER-29338-2:5 stripComments:false
COMMENT ON COLUMN EVT_INV_USAGE.SOURCE_CD IS 'FK to the REF_USG_SNAPSHOT_SRC_TYPE table. Determines the source of the usage snapshot.';