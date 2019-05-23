--liquibase formatted sql


--changeSet PUB-148:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.table_create ('
 CREATE TABLE PUBSUB_EVENT_PROPERTIES
  (
    PUBSUB_EVENT_ID RAW (16) NOT NULL ,
    KEY  VARCHAR2 (100) NOT NULL ,
    VALUE VARCHAR2 (1000) NOT NULL,
    CONSTRAINT IX_PUBSUB_EVENT_PROPERTIES_UNQ UNIQUE ( PUBSUB_EVENT_ID , KEY )  NOT DEFERRABLE ,
    CONSTRAINT FK_PUBS_EVT_PROPS_PUBS_EVT FOREIGN KEY ( PUBSUB_EVENT_ID ) REFERENCES PUBSUB_EVENT ( PUBSUB_EVENT_ID ) ON DELETE CASCADE NOT DEFERRABLE
  )');
END;
/

--changeSet PUB-148:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_SCHEMA_PKG.index_create('
 CREATE INDEX IX_PUBSUB_EVENT_PROPERTIES ON PUBSUB_EVENT_PROPERTIES
    (
      PUBSUB_EVENT_ID ASC
    )');
END;
/

--changeSet PUB-148:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_drop (
        'PUBSUB_EVENT', 'PROPERTIES'
   );
END;
/

