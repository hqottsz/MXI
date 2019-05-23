--liquibase formatted sql


--changeSet SWA-232:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

UTL_MIGR_SCHEMA_PKG.table_create ('
    CREATE TABLE INT_CACHE
  (
    KEY RAW (16) NOT NULL ,
    CACHE_BLOB BLOB ,
    CONSTRAINT PK_INT_CACHE PRIMARY KEY ( KEY )
  )
  ORGANIZATION INDEX 
    ');

END;
/