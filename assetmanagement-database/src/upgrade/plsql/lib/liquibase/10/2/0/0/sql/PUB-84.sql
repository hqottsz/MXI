--liquibase formatted sql

--changeSet PUB-84:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_drop (
        'PUBSUB_SUBSCRIBER', 'URL'
   );
END;
/  

--changeSet PUB-84:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_drop (
        'PUBSUB_SUBSCRIBER', 'CONFIG'
   );
END;
/  

--changeSet PUB-84:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
     'ALTER TABLE PUBSUB_CHANNEL ADD CONSTRAINT IX_PUBSUB_CHANNEL_UNQ UNIQUE ( CHANNEL_NAME )'
   );
END;
/

--changeset PUB-84:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PUBSUB_SUBSCRIBER MODIFY APP_ID VARCHAR2 (80) NOT NULL
   ');
END;
/


--changeSet PUB-84:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
     'ALTER TABLE PUBSUB_SUBSCRIBER ADD CONSTRAINT IX_PUBSUB_SUBSCRIBER_UNQ UNIQUE ( PUBSUB_CHANNEL_ID , APP_ID )'
   );
END;
/
