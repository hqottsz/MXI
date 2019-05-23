--liquibase formatted sql


--changeSet OPER-9735:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 
BEGIN

   utl_migr_schema_pkg.index_create('
      Create Index "IX_ACFTGROUP_ACFTGROUPASS" ON "ACFT_GROUP_ASSIGNMENT" ("GROUP_ID")
   ');
   
END;
/  
