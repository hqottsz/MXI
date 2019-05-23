--liquibase formatted sql


--changeSet OPER-24479:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN
 
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SELECT_REFERENCE',
      'Permission to select a reference for a fault',
      'TRUE/FALSE',
      'FALSE',
      '1',
      'Maint - Faults',
      '8.3-SP1',
      '0',
      '0',
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );

END;
/

--changeSet OPER-24479:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment copy the existing settings and child records from the old action parm to the new action parm
BEGIN
 
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_SELECT_REFERENCE', -- new parameter name
      'ACTION_REQUEST_DEFERRAL'   -- old parameter name
   );
 
END;
/

--changeSet OPER-24479:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete old config parm
BEGIN
 
   utl_migr_data_pkg.action_parm_delete(
      'ACTION_REQUEST_DEFERRAL' 
   );
 
END;
/  