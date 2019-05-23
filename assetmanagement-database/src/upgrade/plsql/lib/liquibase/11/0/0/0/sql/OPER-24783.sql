--liquibase formatted sql

--changeSet OPER-24783:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'API_MAINTENANCE_RELEASE_STATUS_PARM',
      p_parm_desc         => 'Permission to access the MaintenanceReleaseStatus REST API.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'API - MAINTENANCE',
      p_modified_in       => '8.3-SP1',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/