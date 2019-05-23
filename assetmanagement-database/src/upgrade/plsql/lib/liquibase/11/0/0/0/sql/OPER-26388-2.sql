--liquibase formatted sql

--changeSet OPER-26388-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE QUAR_QUAR ADD AC_EVENT_DB_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN QUAR_QUAR.AC_EVENT_DB_ID IS ''FK to INV_CND_CHG_EVENT.  The AC event associated with the quarantine of the inventory.''';
END;
/

--changeSet OPER-26388-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE QUAR_QUAR ADD AC_EVENT_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN QUAR_QUAR.AC_EVENT_ID IS ''FK to INV_CND_CHG_EVENT.  The AC event associated with the quarantine of the inventory.''';
END;
/

--changeSet OPER-26388-2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_INVCNDCHGEVENT_QUARQUAR ON QUAR_QUAR
   (
     AC_EVENT_DB_ID ASC ,
     AC_EVENT_ID ASC
   )
');
END;
/

--changeSet OPER-26388-2:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE QUAR_QUAR ADD CONSTRAINT FK_INVCNDCHGEVENT_QUARQUAR FOREIGN KEY ( AC_EVENT_DB_ID, AC_EVENT_ID ) REFERENCES INV_CND_CHG_EVENT ( event_db_id, event_id ) DEFERRABLE'
);
END;
/

--changeSet OPER-26388-2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE QUAR_QUAR ADD CHECK ( AC_EVENT_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388-2:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE QUAR_QUAR ADD CHECK ( AC_EVENT_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388-2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE FNC_XACTION_LOG ADD AC_EVENT_DB_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FNC_XACTION_LOG.AC_EVENT_DB_ID IS ''FK to INV_CND_CHG_EVENT. If applicable, the associated AC event.''';
END;
/

--changeSet OPER-26388-2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE FNC_XACTION_LOG ADD AC_EVENT_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FNC_XACTION_LOG.AC_EVENT_ID IS ''FK to INV_CND_CHG_EVENT. If applicable, the associated AC event.''';
END;
/

--changeSet OPER-26388-2:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_INVCNDCHGEVENT_FNCACTIONLG ON FNC_XACTION_LOG
   (
     AC_EVENT_DB_ID ASC ,
     AC_EVENT_ID ASC
   )
');
END;
/

--changeSet OPER-26388-2:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE FNC_XACTION_LOG ADD CONSTRAINT FK_INVCNDCHGEVENT_FNCXACTIONLG FOREIGN KEY ( AC_EVENT_DB_ID, AC_EVENT_ID ) REFERENCES INV_CND_CHG_EVENT ( event_db_id, event_id ) DEFERRABLE'
);
END;
/

--changeSet OPER-26388-2:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE FNC_XACTION_LOG ADD CHECK ( AC_EVENT_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388-2:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE FNC_XACTION_LOG ADD CHECK ( AC_EVENT_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/