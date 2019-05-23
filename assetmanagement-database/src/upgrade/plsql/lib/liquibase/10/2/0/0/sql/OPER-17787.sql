--liquibase formatted sql
-- changeSet OPER-17787:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Deleting sDeferredPartRequestDueDateFromCount
BEGIN
	utl_migr_data_pkg.config_parm_delete(
		'sDeferredPartRequestDueDateFromCount' 			-- parameter name
	);
END;
/

-- changeSet OPER-17787:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Deleting sDeferredPartRequestDueDateToCount
BEGIN
	utl_migr_data_pkg.config_parm_delete(
		'sDeferredPartRequestDueDateToCount' 			-- parameter name
	);
END;
/



-- changeSet OPER-17787:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sDeferredPartRequestNeededByDateFromCount for the Open Defferred Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sDeferredPartRequestNeededByDateFromCount', 				-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the "Needed by" options filter on the Deferred Fault Part Requests tab  - From date.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      null,                   									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP5',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/

-- changeSet OPER-17787:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- addded a new session parameter sDeferredPartRequestNeededByDateToCount for the Open Defferred Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sDeferredPartRequestNeededByDateToCount', 				-- parameter name
      'SESSION',        										-- parameter type
      'Stores the last value entered in the "Needed by" options filter on the Deferred Fault Part Requests tab - To date.', 		-- parameter description
      'USER',                    								-- configuration type (GLOBAL or USER)
      'Number',              									-- allowed values for the parameter
      null,                  									-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP5',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/