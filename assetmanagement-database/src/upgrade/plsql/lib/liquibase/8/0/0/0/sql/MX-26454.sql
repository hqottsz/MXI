--liquibase formatted sql


--changeSet MX-26454:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert( 
      p_parm_name => 'AUTO_COMPLETE_TASK_WHEN_CANCEL_LAST_JIC',
      p_parm_type => 'LOGIC',
      p_parm_desc => 'Permission to auto complete the task when cancel the last JIC and there is at least one complete JIC.', 
      p_config_type => 'GLOBAL', 
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value => 'FALSE', 
      p_mand_config_bool => 1, 
      p_category => 'Maint - Tasks', 
      p_modified_in => '8.0-SP1', 
      p_utl_id => 0);
END;
/