--liquibase formatted sql

/*
 *  Add configuration parameter ENABLE_READY_FOR_BUILD.
 */
 
--changeSet OPER-17635:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'ENABLE_READY_FOR_BUILD',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'When enabled, inventory with a serviceable core or main body can be received and inspected as serviceable, even though there are missing, mandatory, tracked sub-components which must be added.  When inventory is marked Ready For Build, component work packages for the inventory can be closed and the inventory can be installed on an aircraft. (The aircraft cannot be released until the inventory condition is RFI).',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'FALSE',
      p_mand_config_bool =>  0,
      p_category         =>  'Core Logic',
      p_modified_in      =>  '8.2-SP5',
      p_utl_id           =>  0
   );

END;
/