--liquibase formatted sql

--changeSet OPER-26388:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE EQP_PART_ROTABLE_ADJUST ADD AC_EVENT_DB_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.AC_EVENT_DB_ID IS ''FK_INV_CND_CHG_EVENT. If applicable, the AC event  related to the adjustment.''';
END;
/

--changeSet OPER-26388:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add(
   'ALTER TABLE EQP_PART_ROTABLE_ADJUST ADD AC_EVENT_ID NUMBER(10)'
);
EXECUTE IMMEDIATE 'COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.AC_EVENT_ID IS ''FK_INV_CND_CHG_EVENT. If applicable, the AC event  related to the adjustment.''';
END;
/

--changeSet OPER-26388:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   CREATE INDEX IX_INVCNDCHGEVENT_EQPPARTROTAB ON EQP_PART_ROTABLE_ADJUST
   (
     AC_EVENT_DB_ID ASC ,
     AC_EVENT_ID ASC
   )
');
END;
/

--changeSet OPER-26388:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE EQP_PART_ROTABLE_ADJUST ADD CONSTRAINT FK_INVCNDCHGEVENT_EQPPARTROTAB FOREIGN KEY ( AC_EVENT_DB_ID, AC_EVENT_ID ) REFERENCES INV_CND_CHG_EVENT ( event_db_id, event_id ) DEFERRABLE'
);
END;
/

--changeSet OPER-26388:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE INV_CND_CHG_EVENT ADD CHECK ( EVENT_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE INV_CND_CHG_EVENT ADD CHECK ( EVENT_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE EQP_PART_ROTABLE_ADJUST ADD CHECK ( AC_EVENT_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/

--changeSet OPER-26388:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add(
   'ALTER TABLE EQP_PART_ROTABLE_ADJUST ADD CHECK ( AC_EVENT_ID BETWEEN 0 AND 4294967295) DEFERRABLE'
);
END;
/