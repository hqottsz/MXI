--liquibase formatted sql


--changeSet SWA-2491:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_create ('
      CREATE TABLE INT_CACHE_NEW
      (
         KEY RAW (16) NOT NULL ,
         CACHE_NAME VARCHAR2(20) NOT NULL,
         CACHE_BLOB BLOB,
         CONSTRAINT PK_INT_CACHE_NEW PRIMARY KEY ( KEY, CACHE_NAME )
      )
      ORGANIZATION INDEX
     ');
END;
/

--changeSet SWA-2491:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO
      INT_CACHE_NEW ( KEY, CACHE_NAME, CACHE_BLOB)
   SELECT
      KEY, 'AEROBUY', CACHE_BLOB
   FROM
      INT_CACHE;
END;
/

--changeSet SWA-2491:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_rename(
      'INT_CACHE',
      'INT_CACHE_OLD'
    );
END;
/

--changeSet SWA-2491:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_rename(
      'INT_CACHE_NEW',
      'INT_CACHE'
   );
END;
/

--changeSet SWA-2491:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_drop(
      'INT_CACHE_OLD'
   );
END;
/

--changeSet SWA-2491:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     EXECUTE IMMEDIATE 'ALTER TABLE "INT_CACHE" RENAME CONSTRAINT "PK_INT_CACHE_NEW" TO "PK_INT_CACHE"';
END;
/

--changeSet SWA-2491:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     EXECUTE IMMEDIATE 'ALTER INDEX "PK_INT_CACHE_NEW" RENAME TO "PK_INT_CACHE"';
END;
/