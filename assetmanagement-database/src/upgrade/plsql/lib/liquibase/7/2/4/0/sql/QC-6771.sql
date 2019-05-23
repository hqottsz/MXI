--liquibase formatted sql


--changeSet QC-6771:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ACTION_PPC_OPTIMIZE_PLAN
**************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_PPC_OPTIMIZE_PLAN',
      'Permission to allow Optimization action from Plan menu.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Production Planning and Control', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/

--changeSet QC-6771:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Ensure that configuration parameter PPC_OPTIMIZE_PLAN delete
**************************************************************************/
BEGIN
	utl_migr_data_pkg.config_parm_delete('PPC_OPTIMIZE_PLAN');
END ;
/