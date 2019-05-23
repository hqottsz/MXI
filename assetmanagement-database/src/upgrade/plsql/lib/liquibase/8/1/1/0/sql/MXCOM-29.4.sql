--liquibase formatted sql


--changeSet MXCOM-29.4:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_perm_set_v1_pkg.CreatePermissionSet('UTL_ROLE_EDIT', 'Administration', 'Edit Roles', 'This allows the role to be modified.');
END;
/

--changeSet MXCOM-29.4:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_ROLE_EDIT', 'API_ROLE_UPDATE_REQUEST');
END;
/

--changeSet MXCOM-29.4:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_perm_set_v1_pkg.CreatePermissionSet('UTL_ROLE_VIEW', 'Administration', 'View Roles', 'This allows the role pages to be accessible.');
END;
/

--changeSet MXCOM-29.4:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_ROLE_VIEW', 'API_ROLE_VIEW_REQUEST');
END;
/

--changeSet MXCOM-29.4:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter('UTL_ROLE_VIEW', 'API_PERM_SET_VIEW_REQUEST');
END;
/