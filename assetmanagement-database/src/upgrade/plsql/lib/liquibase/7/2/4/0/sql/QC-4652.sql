--liquibase formatted sql


--changeSet QC-4652:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UTL_ALERT_DUPLICATE');
END;
/

--changeSet QC-4652:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "STATUSTYPEHASH_UTLALERT" ON "UTL_ALERT" ("ALERT_STATUS_CD","ALERT_TYPE_ID","PARM_HASH") 
   ');
   END;
     /