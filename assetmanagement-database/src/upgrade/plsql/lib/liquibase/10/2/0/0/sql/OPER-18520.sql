--liquibase formatted sql
-- changeSet OPER-18520:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sShowRepreq for the Repair Routing to do list
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sShowRepreq', 											-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the Repreq checkbox on the repair routing to do list.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      '1',                   									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP5',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/

-- changeSet OPER-18520:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sShowRfb for the Repair Routing to do list
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sShowRfb', 												-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the Rfb checkbox on the repair routing to do list.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      '1',                  									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP5',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/