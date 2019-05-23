--liquibase formatted sql


--changeSet MX-28935:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN   
   utl_migr_data_pkg.config_parm_insert (
      'MEASUREMENT_UPDATE_MODE', 
      'LOGIC',
      'This parameter is used to determine how measurements are processed: SYNCHRONIZE which is a flush and fill operation or ADDITIVE which adds measurements that do not exist and updates existing measurements.',
      'GLOBAL', 
      'SYNCHRONIZE/ADDITIVE', 
      'SYNCHRONIZE', 
      1, 
      'Integration', 
      '8.1-SP1', 
      0
   );      
END;
/