--liquibase formatted sql

--changeSet OPER-17925:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- insert ALLOW_REPL_SUPPRESSION to utl_config_parm table
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'ALLOW_REPL_SUPPRESSION',
      p_parm_type        => 'LOGIC',
      p_parm_desc        => 'Enables task suppression on duplicate subtasks of REPL tasks that have different Replacement Config Slots. Requires SUPPRESS_DUPLICATE_TASKS to also be enabled.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Core Logic',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/