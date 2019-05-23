--liquibase formatted sql

--changeSet OPER-12495:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'APP_ADMIN_DASHBOARD', 
      p_parm_desc         => 'Permission to access the Admin Dashboard application',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Admin - Dashboard',
      p_modified_in       => '8.2-SP3',
      p_session_auth_bool => 0,
      p_utl_id            => 0, 
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/
