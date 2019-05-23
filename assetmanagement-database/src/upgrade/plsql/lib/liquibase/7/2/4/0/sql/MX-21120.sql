--liquibase formatted sql


--changeSet MX-21120:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_DATA_PKG.config_parm_insert(p_parm_name => 'TASK_USAGE_SNAPSHOT_EXCEEDS_CURRENT_USAGE',
                                     p_parm_type => 'LOGIC',
                                     p_parm_desc => 'Validation severity level.  Task usage snapshot edited to values exceeding inventory''s current usage.',
                                     p_config_type => 'USER',
                                     p_allow_value_desc => 'WARNING/ERROR/INFO',
                                     p_default_value => 'INFO',
                                     p_mand_config_bool =>1 ,
                                     p_category => 'Maint - Tasks',
                                     p_modified_in => '8.0',
                                     p_utl_id => 0);
END;
/