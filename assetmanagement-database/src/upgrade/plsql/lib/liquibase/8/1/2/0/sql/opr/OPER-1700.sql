--liquibase formatted sql


--changeSet OPER-1700:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_STANDARD_AD_SB_MANAGEMENT', -- parameter name
      'LOGIC',                            -- parameter type
      'Controls whether the Standard AD/SB solution is enabled or not.  If a custom AD/SB solution is being used then this parameter must be set to FALSE (the Standard AD/SB solution and a custom AD/SB solution may not be used simultaneously).', -- parameter description
      'GLOBAL',                           -- configuration type (GLOBAL or USER)
      'TRUE/FALSE',                       -- allowed values for the parameter
      'FALSE',                            -- default value of the parameter
      1,                                  -- whether or not the parameter is mandatory
      'Core Logic',                       -- the parameter category
      '8.1-SP3',                          -- the version in which the parm was modified
      0                                   -- the utl_id
   ); 
END;
/