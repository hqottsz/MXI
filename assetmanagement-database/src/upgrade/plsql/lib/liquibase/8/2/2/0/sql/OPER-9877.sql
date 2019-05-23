--liquibase formatted sql


--changeSet OPER-9877:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_CHANGE_JOB_STEP_APPLICABILITY', 
      'Permission to change job step applicability. Includes permission for: the Mark As Applicable or Mark As Not Applicable link, the Change Job Step Applicability page, the Mark as Applicable/Mark as Not Applicable button, and the JobStepApplicability servlet.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/
