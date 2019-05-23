--liquibase formatted sql


--changeSet DEV-1262:1 stripComments:false
-- Maintenance Program details - REQs Filter options
INSERT INTO utl_config_parm 
	(
		parm_name, 
		parm_value, 
		parm_type, 
		parm_desc, 
		config_type, 
		allow_value_desc, 
		default_value, 
		mand_config_bool, 
		category, 
		modified_in, 
		utl_id
	)
SELECT 
	'sMaintProgramReqTabFilterGroupCode', 
	null, 
	'SESSION', 
	'Group Code. Used to store the filter option on Maintenance Program Details, Requirements tab.', 
	'USER', 
	'STRING', 
	null, 
	0, 
	'Maint Program - Maint Program', 
	'8.0', 
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			utl_config_parm 
		WHERE 
			utl_config_parm.parm_name = 'sMaintProgramReqTabFilterGroupCode' AND
			utl_config_parm.parm_type = 'SESSION'
	)
; 

--changeSet DEV-1262:2 stripComments:false
INSERT INTO utl_config_parm 
	(
		parm_name, 
		parm_value, 
		parm_type, 
		parm_desc, 
		config_type, 
		allow_value_desc, 
		default_value, 
		mand_config_bool, 
		category, 
		modified_in, 
		utl_id
	)
SELECT 
	'sMaintProgramReqTabFilterConfigSlot', 
	null, 
	'SESSION', 
	'Config Slot. Used to store the filter option on Maintenance Program Details, Requirements tab.', 
	'USER', 
	'STRING', 
	null, 
	0, 
	'Maint Program - Maint Program', 
	'8.0', 
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			utl_config_parm 
		WHERE 
			utl_config_parm.parm_name = 'sMaintProgramReqTabFilterConfigSlot' AND
			utl_config_parm.parm_type = 'SESSION'
	)
; 

--changeSet DEV-1262:3 stripComments:false
INSERT INTO utl_config_parm 
	(
		parm_name, 
		parm_value, 
		parm_type, 
		parm_desc, 
		config_type, 
		allow_value_desc, 
		default_value, 
		mand_config_bool, 
		category, 
		modified_in, 
		utl_id
	)
SELECT 
	'sMaintProgramReqTabFilterRevisedOnly', 
	'FALSE', 
	'SESSION', 
	'Revized Only flag. Used to store the filter option on Maintenance Program Details, Requirements tab.', 
	'USER', 
	'TRUE/FALSE', 
	'FALSE', 
	0, 
	'Maint Program - Maint Program', 
	'8.0', 
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			utl_config_parm 
		WHERE 
			utl_config_parm.parm_name = 'sMaintProgramReqTabFilterRevisedOnly' AND
			utl_config_parm.parm_type = 'SESSION'
	)
; 

--changeSet DEV-1262:4 stripComments:false
INSERT INTO utl_config_parm 
	(
		parm_name, 
		parm_value, 
		parm_type, 
		parm_desc, 
		config_type, 
		allow_value_desc, 
		default_value, 
		mand_config_bool, 
		category, 
		modified_in, 
		utl_id
	)
SELECT 
	'sMaintProgramReqTabFilterApplicableOnly', 
	'FALSE', 
	'SESSION', 
	'Applicable Only flag. Used to store the filter option on Maintenance Program Details, Requirements tab.', 
	'USER', 
	'TRUE/FALSE', 
	'FALSE', 
	0, 
	'Maint Program - Maint Program', 
	'8.0', 
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			utl_config_parm 
		WHERE 
			utl_config_parm.parm_name = 'sMaintProgramReqTabFilterApplicableOnly' AND
			utl_config_parm.parm_type = 'SESSION'
	)
; 