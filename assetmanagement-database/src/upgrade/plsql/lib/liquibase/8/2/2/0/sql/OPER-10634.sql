--liquibase formatted sql

/*
 *  Add configuration parameter ENABLE_ERROR_MESSAGE_LOGGING.
 */
 
--changeSet OPER-10634:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'ENABLE_ERROR_MESSAGE_LOGGING',
      p_parm_type        =>  'SYSTEM',
      p_parm_desc        =>  'Enable logging that records the following information in the UTL_ERROR_LOG table each time users hit error messages: the exception class name, error code, error message, servlet name, and time. Does not identify the user encountering the error.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'TRUE',
      p_mand_config_bool =>  1,
      p_category         =>  'SYSTEM',
      p_modified_in      =>  '8.2-SP3',
      p_utl_id           =>  0
   );

END;
/