--liquibase formatted sql


--changeSet OPER-8096:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- insert SHOW_SCHED_LABOUR_HOURS to utl_config_parm table
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'SHOW_SCHED_LABOUR_HOURS',
      p_parm_type        => 'SECURED_RESOURCE',
      p_parm_desc        => 'Permission to view scheduled labor information.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Maint - Tasks',
      p_modified_in      => '8.2-SP3',
      p_utl_id           => 0
   );
END;
/   

--changeSet OPER-8096:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- delete HIDE_SCHED_LABOUR_HOURS from utl_config_parm table
   utl_migr_data_pkg.config_parm_delete(
      p_parm_name        => 'HIDE_SCHED_LABOUR_HOURS'
   );
END;
/