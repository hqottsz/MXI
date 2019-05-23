--liquibase formatted sql

--changeSet AEB-1193:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'API_REQ_PRIORITY_PARM',
      p_parm_desc         => 'Permission to access the priority refterms REST API.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'API - SYSTEM',
      p_modified_in       => '8.3',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/