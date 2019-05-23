--liquibase formatted sql

--changeSet OPER-8831:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'sVisibleGroups',
      p_parm_type        =>  'SESSION',
      p_parm_desc        =>  'Groups that are visible on the Fleet List page.',
      p_config_type      =>  'USER',
      p_allow_value_desc =>  'STRING',
      p_default_value    =>  null,
      p_mand_config_bool =>  0,
      p_category         =>  'SESSION',
      p_modified_in      =>  '8.1',
      p_utl_id           =>  0
   );

END;
/