--liquibase formatted sql

--changeSet OPER-26414:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_COMPLETE_ALL_STEP_SKILLS_FOR_CURRENT_LABOUR_SKILL',
      p_parm_desc         => 'When the same skill is listed in multiple steps for a labor row, permission to bulk complete the skill in all steps.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Maint - Tasks',
      p_modified_in       => '8.3-SP1',
	  p_session_auth_bool => 0,
      p_utl_id            => 0,
	  p_db_types          => utl_migr_data_pkg.dbtypecdlist('OPER','MASTER')
   );
END;
/

