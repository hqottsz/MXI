--liquibase formatted sql

--changeSet OPER-23438:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add Weblogic Authentication Provider Cache Invalidation Interval
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'AUTH_PROVIDER_CACHE_INVALIDATION_INTERVAL',
      p_parm_type        => 'LOGIC',
      p_parm_desc        => 'Defines how often to check Weblogic Authentication Provider Configuration',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => 'Number',
      p_default_value    => '600',
      p_mand_config_bool => 1,
      p_category         => 'System',
      p_modified_in      => '8.3',
      p_utl_id           => 0
   );
END;
/