--liquibase formatted sql

--changeSet SWA-3419:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE PUBSUB_SUBSCRIBER ADD CIRCUIT_STATUS  VARCHAR2 (10) DEFAULT ''OPEN''' 
   );
END;
/  

--changeSet SWA-3419:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE PUBSUB_SUBSCRIBER ADD CIRCUIT_CLOSE_DT TIMESTAMP (0)' 
   );
END;
/  

--changeSet SWA-3419:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE PUBSUB_SUBSCRIBER ADD FAILED_ATTEMPTS NUMBER DEFAULT 0' 
   );
END;
/  
