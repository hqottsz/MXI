--liquibase formatted sql


--changeSet EXAMPLE:1 stripComments:false
-- Conditional insert
INSERT INTO utl_alert_type 
	( 
		alert_type_id, 
		alert_name, 
		alert_ldesc, 
		notify_cd, 
		notify_class, 
		category, 
		message, 
		key_bool, 
		priority, 
		priority_calc_class, 
		active_bool, 
		utl_id 
	)
SELECT 
	161, 
	'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_name', 
	'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_description', 
	'ROLE', 
	null, 
	'FLIGHT', 
	'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_message', 
	1, 
	0, 
	null, 
	1, 
	0
FROM 
	dual 
WHERE 
	NOT EXISTS 
	( 
		SELECT 
			1 
		FROM 
			utl_alert_type 
		WHERE 
			alert_type_id = 161 
	)
;