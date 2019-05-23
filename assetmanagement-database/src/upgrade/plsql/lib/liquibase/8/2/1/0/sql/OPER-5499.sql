--liquibase formatted sql


--changeSet OPER-5499:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create three new configuration parameter for securing file upload
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'FILE_UPLOAD_FILENAME_LENGTH',
      p_parm_type        =>  'MXCOMMONWEB',
      p_parm_desc        =>  'Determines the valid length for uploaded file names.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  '80',
      p_default_value    =>  '80',
      p_mand_config_bool =>  1,
      p_category         =>  'HTML Parameter',
      p_modified_in      =>  '8.2-SP2',
      p_utl_id           =>  0
   );

END;
/

--changeSet OPER-5499:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'FILE_UPLOAD_ALLOWED_CHARACTERS',
      p_parm_type        =>  'MXCOMMONWEB',
      p_parm_desc        =>  'Determines the valid characters for uploaded file names.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  '[a-zA-Z0-9_()+\\-\\s]',
      p_default_value    =>  '[a-zA-Z0-9_()+\\-\\s]',
      p_mand_config_bool =>  1,
      p_category         =>  'HTML Parameter',
      p_modified_in      =>  '8.2-SP2',
      p_utl_id           =>  0
   );

END;
/

--changeSet OPER-5499:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'FILE_UPLOAD_ALLOWED_TYPES',
      p_parm_type        =>  'MXCOMMONWEB',
      p_parm_desc        =>  'Determines the valid types for uploaded files.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'png|gif|jpg|jpeg|pdf|doc|docx|txt',
      p_default_value    =>  'png|gif|jpg|jpeg|pdf|doc|docx|txt',
      p_mand_config_bool =>  1,
      p_category         =>  'HTML Parameter',
      p_modified_in      =>  '8.2-SP2',
      p_utl_id           =>  0
   );

END;
/