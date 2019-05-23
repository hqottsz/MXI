--liquibase formatted sql


--changeSet MX-20868:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

-- create index
utl_migr_schema_pkg.index_create('Create Index "FNC_ACCOUNT_ACCOUNTCD" ON "FNC_ACCOUNT" ("ACCOUNT_CD") ');

END;
/

--changeSet MX-20868:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

utl_migr_data_pkg.config_parm_insert (
      'SELECT_WINDOW_POPUP_MAX_ACCOUNTS', 
      'MXWEB',
      'Indicates the maximum number of accounts that may be displayed in the Select Account popup; if this value is exceeded, only the number of accounts set with this parameter will be displayed, together with a warning message for a user to refine the search.',
      'GLOBAL', 
      'Number', 
      '1000', 
      1, 
      'MXWEB', 
      '7.1-SP4', 
      0,
      utl_migr_data_pkg.DbTypeCdList( '' )
   );

END;
/