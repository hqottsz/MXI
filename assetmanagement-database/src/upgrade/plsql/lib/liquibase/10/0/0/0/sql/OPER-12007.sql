--liquibase formatted sql

--changeSet OPER-12007:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- insert PAGE_LEVEL_SECURITY_ENABLED to utl_config_parm table
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'PAGE_LEVEL_SECURITY_ENABLED',
      p_parm_type        => 'PERMISSION',
      p_parm_desc        => 'Enable/disable the ability to configure page-level security through the Role Permissions editior. NOTE: This features is a TECH DEMONSTRATOR only. Not ready for production use.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Permissions',
      p_modified_in      => '8.2-SP3',
      p_utl_id           => 0
   );
END;
/