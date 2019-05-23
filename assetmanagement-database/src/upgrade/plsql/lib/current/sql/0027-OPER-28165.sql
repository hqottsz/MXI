--liquibase formatted sql

--changeSet OPER-28165:1 stripComments:false
BEGIN

   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADD_EDIT_HISTORIC_DAMAGE_RECORD',
      'Permission to add or edit the damage record on a historic fault.',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'Maint - Faults',
      '8.3-SP1',
      '0',
      '0'
   );

END;
/