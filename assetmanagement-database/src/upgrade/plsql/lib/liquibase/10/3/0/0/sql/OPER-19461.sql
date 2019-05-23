--liquibase formatted sql
--changeSet OPER-19461:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'ACTION_CHANGE_ADHOC_JOB_STEP_APPLICABILITY', 
         'Permission to toggle job step status between Pending and N/A on ad-hoc tasks and faults.' ,
         'TRUE/FALSE', 
         'FALSE', 
         1, 
         'Maint - Tasks', 
         '8.2-SP5', 
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/