--liquibase formatted sql


--changeSet OPER-9613:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--update file upload security config params to match whole string instead of substrings
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'FILE_UPLOAD_ALLOWED_TYPES',
      p_parm_type        =>  'MXCOMMONWEB',
      p_parm_desc        =>  'The list of allowable file extensions for files uploaded to Maintenix, where each file extension is separated by the pipe character ("|").',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'A Java regular expression pattern (java.util.regex.Pattern) that will match the file extension of files that Maintenix should accept for upload. A value of ".*" will accept any file extension.',
      p_default_value    =>  '(?i)^(bmp|csv|doc|docx|gif|jpeg|jpg|msg|pdf|png|ppt|pptx|rtf|txt|xls|xlsx)$',
      p_mand_config_bool =>  1,
      p_category         =>  'HTML Parameter',
      p_modified_in      =>  '8.2-SP2',
      p_utl_id           =>  0
   );

END;
/