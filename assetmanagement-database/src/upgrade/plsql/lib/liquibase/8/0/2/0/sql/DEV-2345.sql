--liquibase formatted sql


--changeSet DEV-2345:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- adding asset_sdesc column to arc_asset 
BEGIN  
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ARC_ASSET ADD(
         ASSET_SDESC VARCHAR2(400)
      )
   ');
END;
/

--changeSet DEV-2345:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- modify desc_sdesc of arc_message 
BEGIN
  utl_migr_schema_pkg.table_column_modify('
     ALTER TABLE ARC_MESSAGE modify (
        DESC_SDESC VARCHAR2(4000)
     )
  ');
END;
/