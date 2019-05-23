--liquibase formatted sql

/*
 *  Add new configuration parameter USE_BASELINE_USAGE_SORT_ORDER.
 */

--changeSet OPER-21654:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'USE_BASELINE_USAGE_SORT_ORDER',
      p_parm_type        =>  'MXWEB',
      p_parm_desc        =>  'When enabled, usage parameters displayed on certain pages are sorted by the order defined in their usage definition. When disabled, parameters are sorted alphanumerically.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'FALSE',
      p_mand_config_bool =>  1,
      p_category         =>  'MXWEB',
      p_modified_in      =>  '8.3',
      p_utl_id           =>  0
   );

END;
/