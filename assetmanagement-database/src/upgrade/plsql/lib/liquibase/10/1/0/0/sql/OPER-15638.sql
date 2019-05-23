--liquibase formatted sql
--changeSet OPER-15638:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--addd a new session user parameter sShowAllPartRequestsForDeferredFaultPR for the Defferred Fault Part Requests tab
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'sShowAllPartRequestsForDeferredFaultPR', 				-- parameter name
      'SESSION',        										-- parameter type
      'When this parameter is false, users see deferred fault part requests that are unassigned or assigned to themselves. But the value is overwritten by the true value stored in the UTL_USER_PARM table for individual users if they select the Show All Part Requests filter.', 										
																-- parameter description
      'USER',                    								-- configuration type
      'TRUE/FALSE',              								-- allowed values for the parameter
      'FALSE',                   								-- default value of the parameter
       0,                         								-- whether or not the parameter is mandatory
      'SESSION',         										-- the parameter category
      '8.2-SP4',                     							-- the version in which the parm was modified
       0                          								-- the utl_id
   );

END;
/

