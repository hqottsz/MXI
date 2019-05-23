--liquibase formatted sql


--changeSet DEV-1903:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ALLOW_FAILED_SYS_ON_HISTORIC_FAULT
**************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_EDIT_FAILED_SYS_ON_HISTORIC_FAULT',
      'This parameter is used to allow/disallows the edition of the Failed System field on Raise Fault page for a historic fault.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Faults',
      '8.0 SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/ 

--changeSet DEV-1903:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************
 * Remove the security parameter ACTION_CREATE_ADHOC_TURNIN
 ******************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_delete('ALLOW_FAILED_SYS_ON_HISTORIC_FAULT');
END;
/ 