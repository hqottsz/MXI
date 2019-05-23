--liquibase formatted sql

/*
 *  Add new configuration parameter ENABLE_MANDATORY_MEASUREMENT_VALUES.
 */

--changeSet OPER-18604:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'ENABLE_MANDATORY_MEASUREMENT_VALUES',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'When enabled, measurements values on tasks are mandatory.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'FALSE',
      p_mand_config_bool =>  1,
      p_category         =>  'LOGIC',
      p_modified_in      =>  '8.3',
      p_utl_id           =>  0
   );

END;
/