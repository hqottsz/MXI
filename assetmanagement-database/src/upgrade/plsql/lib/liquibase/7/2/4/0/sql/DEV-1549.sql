--liquibase formatted sql


--changeSet DEV-1549:1 stripComments:false
-- add LRP_TIME_UNIT_THRESHOLD config parameter
INSERT INTO utl_config_parm 
	(
           PARM_NAME, 
           PARM_TYPE, 
           PARM_VALUE, 
           PARM_DESC, 
           CONFIG_TYPE, 
           ALLOW_VALUE_DESC, 
           DEFAULT_VALUE, 
           MAND_CONFIG_BOOL, 
           CATEGORY, 
           MODIFIED_IN, 
           UTL_ID
	)
SELECT 
           'LRP_TIME_UNIT_THRESHOLD', 
           'LONG_RANGE_PLANNER', 
           'THIRTY_MINUTES', 
           'The date threshold for rounding date value in automation and forecasting. Supported values: ONE_DAY, ONE_HOUR, THIRTY_MINUTES, ONE_MINUTE, VOID',
           'GLOBAL', 
           'String', 
           'THIRTY_MINUTES', 
           0,
           'Maint - Long Range Planning', 
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
			utl_config_parm.parm_name = 'LRP_TIME_UNIT_THRESHOLD'
	)
; 