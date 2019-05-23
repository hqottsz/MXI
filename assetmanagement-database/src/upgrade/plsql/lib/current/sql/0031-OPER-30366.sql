--Delete API_LABOUR_SKILL_PARM parameter from utl_action_config_parm table
BEGIN
     utl_migr_data_pkg.action_parm_delete('API_LABOUR_SKILL_PARM');
END;
/