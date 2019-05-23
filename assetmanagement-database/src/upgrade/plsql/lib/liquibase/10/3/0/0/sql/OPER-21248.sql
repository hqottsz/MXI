--liquibase formatted sql

/*
 *  Add configuration parameter EXPECTED_TOOL_CHECKOUT_DURATION_HRS.
 */

--changeSet OPER-21248:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'EXPECTED_TOOL_CHECKOUT_DURATION_HRS',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'When a tool is checked out, the hour value specified for this parameter determines the expected return date for the tool. The return date is calculated by adding the specified hour value to the system date and time.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'Number',
      p_default_value    =>   0,
      p_mand_config_bool =>   0,
      p_category         =>  'Core Logic',
      p_modified_in      =>  '8.3',
      p_utl_id           =>  0
   );

END;
/