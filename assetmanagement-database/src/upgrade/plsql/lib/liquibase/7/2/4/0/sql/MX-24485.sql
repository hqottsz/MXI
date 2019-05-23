--liquibase formatted sql


--changeSet MX-24485:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create new action configuration parameter ACTION_EDIT_SIGNED_PART_REQUIREMENT
 **************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_EDIT_SIGNED_PART_REQUIREMENT',
      'Permission to edit signed part requirements on tasks', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks', 
      '8.0 alpha', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/ 

--changeSet MX-24485:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Copy parameter values from ACTION_EDIT_HISTORIC_PART_REQUIREMENT
 **************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_EDIT_SIGNED_PART_REQUIREMENT', 
      'ACTION_EDIT_HISTORIC_PART_REQUIREMENT');
END;
/