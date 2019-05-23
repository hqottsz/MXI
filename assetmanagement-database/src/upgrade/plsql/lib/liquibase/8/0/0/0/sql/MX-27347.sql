--liquibase formatted sql


--changeSet MX-27347:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      p_parm_name => 'WORK_TYPE_REQUIRED_FOR_WP',
      p_parm_type => 'LOGIC',
      p_parm_desc => 'Whether work type is mandatory for a work order.',
      p_config_type => 'GLOBAL',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value => 'FALSE' ,
      p_mand_config_bool => 1,
      p_category => 'Maint - Work Packages',
      p_modified_in => '8.0-SP1' ,
      p_utl_id => 0) ;
END;
/