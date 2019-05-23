--liquibase formatted sql
-- changeSet OPER-5329:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment addded a new session parameter sHideComponentChecks for the Open Work Packages page
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sHideComponentChecks', 											-- parameter name
      'SESSION',        										-- parameter type
      'Determines if Component Work Packages are shown in the open Work package list on the Aircraft Details page.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'TRUE/FALSE',              									-- allowed values for the parameter
      'FALSE',                   									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP5',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/