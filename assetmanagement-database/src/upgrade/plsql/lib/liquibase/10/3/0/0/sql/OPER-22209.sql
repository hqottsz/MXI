--liquibase formatted sql

--changeSet OPER-22209:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('
      Alter table INV_PARM_DATA add (
         NA_NOTE VARCHAR2 (1000)
      )
   ');
END;
/

--changeSet OPER-22209:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'ENABLE_MARK_MEASUREMENT_NA',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'When enabled, allows a user to explicitly mark a measurement as NA on the Work Capture page.',
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