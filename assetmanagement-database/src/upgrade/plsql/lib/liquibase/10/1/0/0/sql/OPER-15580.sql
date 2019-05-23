--liquibase formatted sql
--changeSet OPER-15580:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add new session parameter for the Open Defferred Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sDFPRTPartRequestStatus', 					-- parameter name
      'SESSION',        										-- parameter type
      'Filter by deferred faults part requests status', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'STRING',              									-- allowed values for the parameter
      '*',                   									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP4',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/