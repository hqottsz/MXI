--liquibase formatted sql


--changeSet MX-28308:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  -- create the new INT_MSG_PUBLISH table
  utl_migr_schema_pkg.table_create('
  	Create table "INT_MSG_PUBLISH" (
  		"MSG_ID" Varchar2(80) NOT NULL DEFERRABLE,
  		"CREATION_DT" Date NOT NULL DEFERRABLE 
  	)
  ');
END;
/ 

--changeSet MX-28308:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN   
   utl_migr_data_pkg.config_parm_insert (
      'INT_MSG_WAIT_TIMEOUT', 
      'INTEGRATION',
      'This parameter is used set a time limit for waiting for a database commit before delivering an integration message.',
      'GLOBAL', 
      'Number', 
      '10', 
      1, 
      'Integration', 
      '8.1-SP1', 
      0
   );      
END;
/