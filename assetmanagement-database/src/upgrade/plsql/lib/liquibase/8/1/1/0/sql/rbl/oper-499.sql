--liquibase formatted sql


--changeSet oper-499:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_schema_pkg.view_drop('AOPR_RELIABILITY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_TDRL_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_SERVICE_DIFFICULTY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_STATION_DELAY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_TECHNICAL_DELAY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_AIRCRAFT_DELAY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_ATA_DELAY_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_DISPATCH_METRIC_V1');   
   utl_migr_schema_pkg.view_drop('AOPR_DISPATCH_RELIABILITY_V1');
   utl_migr_schema_pkg.view_drop('AOPR_DELAY_V1');
   utl_migr_schema_pkg.view_drop('AOPR_MONTHLY_OPERATOR_USAGE_V1');
   utl_migr_schema_pkg.type_drop('ARGV');
   
END;
/