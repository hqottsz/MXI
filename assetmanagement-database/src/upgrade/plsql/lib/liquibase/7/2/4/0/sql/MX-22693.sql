--liquibase formatted sql


--changeSet MX-22693:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "UTL_WORK_ITEM_KEY" ON "UTL_WORK_ITEM" ("KEY")
   ');
END;
/

--changeSet MX-22693:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "UTL_WORK_ITEM_SCHEDULED_DATE" ON "UTL_WORK_ITEM" ("SCHEDULED_DATE") 
   ');
END;
/

--changeSet MX-22693:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "UTL_WORK_ITEM_SERVER_ID" ON "UTL_WORK_ITEM" ("SERVER_ID")
   ');
END;
/

--changeSet MX-22693:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "UTL_WORK_ITEM_TYPE_SERVER_DT" ON "UTL_WORK_ITEM" ("TYPE","SERVER_ID","SCHEDULED_DATE") 
   ');
END;
/