--liquibase formatted sql


--changeSet MX-22765:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Drop existing index on INT_INBOUND_QUEUE_LOG
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_IDX_INTINBQUELOG_STREAMCD');
END;
/

--changeSet MX-22765:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index on INT_INBOUND_QUEUE_LOG
 **************************************************************************/ 
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INTINBQUELOG_STRMCD_MSGID" ON "INT_INBOUND_QUEUE_LOG" ("STREAM_CD","MSG_ID")
');
END;
/