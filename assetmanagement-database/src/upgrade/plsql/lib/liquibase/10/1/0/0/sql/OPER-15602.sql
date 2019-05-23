--liquibase formatted sql
-- changeSet OPER-15602:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sDeferredPartRequestDueDateFromCount for the Open Defferred Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sDeferredPartRequestDueDateFromCount', 				-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the "Show tasks due within the next" options filter on the Deferred Fault Part Requests tab  - From date.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      null,                   									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP4',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/

-- changeSet OPER-15602:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sDeferredPartRequestDueDateToCount for the Open Defferred Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sDeferredPartRequestDueDateToCount', 				-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the "Show tasks due within the next" options filter on the Deferred Fault Part Requests tab - To date.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      null,                  									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP4',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/

