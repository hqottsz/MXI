--liquibase formatted sql


--changeSet OPER-5618:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add permission set to wrap the permissions for the Enhanced Part Search feature.
BEGIN
   utl_perm_set_v1_pkg.CreatePermissionSet('UTL_PART_SEARCH', 'Maintenance', 'Enhanced Part Search', 'This allows the user to access the enhanced part search feature.');
END;
/

--changeSet OPER-5618:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the action parms to the permission set.
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'ACTION_ADD_PART_REQUIREMENT_ADVANCED');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'ACTION_SELECT_ALTERNATE_PART');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_PART_DEFINITION_REQUEST');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_AIRCRAFT_REQUEST');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_PART_REQUEST_REQUEST');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'ACTION_GETINVENTORY');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_WORK_PACKAGE_REQUEST');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_TASK_REQUEST');
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_PART_SEARCH', 'API_LOCATION_REQUEST');
END;
/