--liquibase formatted sql


--changeSet MTX-1672:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- *** SCHEMA CHANGES *** --    
-- Create the LRP_REPORT_SILT Table --
BEGIN
   utl_migr_schema_pkg.table_create('
      Create table "LRP_REPORT_SILT" (
         "PLAN_DB_ID" Number(10,0) NOT NULL ,
         "PLAN_ID" Number(10,0) NOT NULL ,
         "PLAN_SDESC" Varchar2 (80) NOT NULL ,
         "DAY_DT" Date NOT NULL ,
         "AVAIL_QT" Number(10,0) NOT NULL ,
         "IN_MAINT_QT" Number(10,0) NOT NULL ,
         "OVERFLOW_QT" Number(10,0) NOT NULL ,
         "RETIRED_QT" Number(10,0) NOT NULL 
      ) 
   ');
END;
/

--changeSet MTX-1672:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Create indexes on the reporting tables to improve the performance */
BEGIN

   utl_migr_schema_pkg.index_create('
      Create Index "IX_PLANDESC_LRPREPPARTFCST" ON "LRP_REPORT_PART_FORECAST" ("PLAN_SDESC") 
   ');

   utl_migr_schema_pkg.index_create('
      Create Index "IX_PLANDESC_LRPREPSCHED" ON "LRP_REPORT_SCHEDULE" ("PLAN_SDESC") 
   ');

   utl_migr_schema_pkg.index_create('
      Create Index "IX_PLAN_LRPREPORTSILT" ON "LRP_REPORT_SILT" ("PLAN_DB_ID","PLAN_ID") 
   ');

   utl_migr_schema_pkg.index_create('
      Create Index "IX_PLANDESC_LRPREPORTSILT" ON "LRP_REPORT_SILT" ("PLAN_SDESC") 
   ');

   utl_migr_schema_pkg.index_create('
      Create Index "IX_DAY_LRPREPORTSILT" ON "LRP_REPORT_SILT" ("DAY_DT") 
   ');


END;
/