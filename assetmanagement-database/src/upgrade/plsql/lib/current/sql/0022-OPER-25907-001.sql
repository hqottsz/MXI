--liquibase formatted sql
--comment insert new action config parm

--changeSet OPER-25907:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'ACTION_VIEW_ROLE_SECURITY',
      'Permission to view role security settings.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'HR - Roles',
      '8.3-SP2',
      0,
      0
   );

END;
/

--changeSet OPER-25907:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.action_parm_copy('ACTION_VIEW_ROLE_SECURITY', 'ACTION_EDIT_SECURITY_PARM');

END;
/

--changeSet OPER-25907:6 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET PARM_DESC = 'Permission to edit security parameter. Requires: ACTION_VIEW_ROLE_SECURITY.' WHERE PARM_NAME='ACTION_EDIT_SECURITY_PARM';
