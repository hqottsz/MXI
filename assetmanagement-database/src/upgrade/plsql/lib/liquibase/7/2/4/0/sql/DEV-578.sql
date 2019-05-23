--liquibase formatted sql


--changeSet DEV-578:1 stripComments:false
INSERT INTO ref_log_reason (log_reason_db_id,log_reason_cd,log_action_db_id,log_action_cd,desc_sdesc,desc_ldesc,user_cd,rstat_cd,revision_db_id,revision_user)
SELECT 0,'TDMISSJIC',0,'TDPREVENTEXE','Requirement missing job card(s).','Requirement missing job card(s).','TDMISSJIC',0,4650,'MX1012_MASTER'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_log_reason WHERE ref_log_reason.log_reason_cd = 'TDMISSJIC');

--changeSet DEV-578:2 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT 'false', 'ACTION_DO_NOT_PERFORM_MISSING_JIC_FOR_EXTERNAL_WP', 'SECURED_RESOURCE','Prevent the check for missing job card on work packages that are scheduled to be done by an external vendor','USER', 'TRUE/FALSE', 'TRUE', 1,'Maint - Work Packages', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_DO_NOT_PERFORM_MISSING_JIC_FOR_EXTERNAL_WP');

--changeSet DEV-578:3 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_DO_NOT_PERFORM_MISSING_JIC_FOR_EXTERNAL_WP', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (
SELECT 1 FROM db_type_config_parm WHERE 
db_type_config_parm.parm_name = 'ACTION_DO_NOT_PERFORM_MISSING_JIC_FOR_EXTERNAL_WP');