--liquibase formatted sql
-- changeSet OPER-22908:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'SELECT_WINDOW_POPUP_MAX_MANUFACTURERS', 				-- parameter name
      'MXWEB',        										-- parameter type
      'Indicates the maximum number of manufacturers that may be displayed in the Select Manufacturer popup; if this value is exceeded, only the number of manufacturers set with this parameter will be displayed, together with a warning message for a user to refine the search.', 		-- parameter description
      'GLOBAL',                    							-- configuration type (GLOBAL or USER)
      'Number',              								-- allowed values for the parameter
      1000,                   								-- default value of the parameter
      1,                         							-- whether or not the parameter is mandatory
      'MXWEB',         										-- the parameter category
      '8.2-SP5u5',                     						-- the version in which the parm was modified
       0                          							-- the utl_id
   );

END;
/