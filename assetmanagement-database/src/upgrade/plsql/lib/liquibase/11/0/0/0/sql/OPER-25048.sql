--liquibase formatted sql

--changeSet OPER-25048:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'IS_AC_EVT_CONVERSION_COMPLETE',
      p_parm_type        => 'LOGIC',
      p_parm_desc        => 'Indicates the completion of AC event conversion.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Maint - Events',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );

END;
/
