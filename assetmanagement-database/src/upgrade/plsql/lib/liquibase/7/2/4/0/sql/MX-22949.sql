--liquibase formatted sql


--changeSet MX-22949:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX-22949 model number is no longer available on the Part Search Page as it was in 5.1.6.2.
-- Reverted changes for Part Model field under the Part Search page
-- Add configuration parameter: sPartSearchPartModel
BEGIN
   utl_migr_data_pkg.config_parm_insert (
      'sPartSearchPartModel', 
      'SESSION',
      'Part search parameters',
      'USER', 
      '', 
      '', 
      0, 
      'Part Search', 
      '8.0', 
      0,
      utl_migr_data_pkg.DbTypeCdList( '' )
   );
END;
/