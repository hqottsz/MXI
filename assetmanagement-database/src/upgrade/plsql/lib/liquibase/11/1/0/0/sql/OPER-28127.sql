--liquibase formatted sql

--changeSet OPER-28127:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'SPEC2000_ORDER_NUM_LIMIT',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'The order number must not exceed 9 characters to comply with Spec2000 standards.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'FALSE',
      p_mand_config_bool =>  1,
      p_category         =>  'Core Logic',
      p_modified_in      =>  '8.3-SP1',
      p_utl_id           =>  0
   );

END;
/