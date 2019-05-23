--liquibase formatted sql


--changeSet DEV-2470:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.view_drop('AOPR_2DIGIT_ATA_CODE_V1');
   utl_migr_schema_pkg.view_drop('AOPR_ACFT_WP_V1');
   utl_migr_schema_pkg.view_drop('AOPR_ACFT_WP_WORKSCOPE_V1');
END;
/ 