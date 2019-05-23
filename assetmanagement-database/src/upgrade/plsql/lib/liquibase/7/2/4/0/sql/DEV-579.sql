--liquibase formatted sql


--changeSet DEV-579:1 stripComments:false
-- HTTP Caching parameters
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
	'HTTP_CACHE_INCLUDE_URLS', 
	'.*\.css,.*\.gif,.*\.jpg,.*\.jpeg,.*\.png,.*\.html,.*\.js,.*/common/PleaseWaitPopup\.jsp,.*/common/resources/autosuggest_js\.jsp,.*/common/resources/CopyrightNotice\.jsp,.*/common/resources/formats_js\.jsp,.*/common/resources/calendar/ipopeng\.jsp', 		 
	'HTTP', 
	'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs to cache', 
	'GLOBAL', 
	'Java Regex List', 
	'.*\.css,.*\.gif,.*\.jpg,.*\.jpeg,.*\.png,.*\.html,.*\.js,.*/common/PleaseWaitPopup\.jsp,.*/common/resources/autosuggest_js\.jsp,.*/common/resources/CopyrightNotice\.jsp,.*/common/resources/formats_js\.jsp,.*/common/resources/calendar/ipopeng\.jsp', 
	1, 
	'HTTP CACHING', 
	'7.5', 
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
			utl_config_parm.parm_name = 'HTTP_CACHE_INCLUDE_URLS'
	)
;

--changeSet DEV-579:2 stripComments:false
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
	'HTTP_CACHE_EXCLUDE_URLS',
	'',	
	'HTTP', 
	'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs that should be excluded from caching', 
	'GLOBAL', 
	'Java Regex List', 
	'', 
	1, 
	'HTTP CACHING', 
	'7.5',
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
			utl_config_parm.parm_name = 'HTTP_CACHE_EXCLUDE_URLS'
	)
;

--changeSet DEV-579:3 stripComments:false
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
	'HTTP_CACHE_DEBUG', 
	'false', 
	'HTTP', 
	'Enables debug logging for HTTP caching operations', 
	'GLOBAL', 
	'TRUE/FALSE', 
	'FALSE', 
	1, 
	'HTTP CACHING', 
	'7.5', 
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
			utl_config_parm.parm_name = 'HTTP_CACHE_DEBUG'
	)
;

--changeSet DEV-579:4 stripComments:false
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
	'HTTP_CACHE_ENABLED', 
	'true', 
	'HTTP', 
	'Enables HTTP caching', 
	'GLOBAL', 
	'TRUE/FALSE', 
	'TRUE', 
	1, 
	'HTTP CACHING', 
	'7.5', 
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
			utl_config_parm.parm_name = 'HTTP_CACHE_ENABLED'
	)
;

--changeSet DEV-579:5 stripComments:false
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
	'HTTP_CACHE_EXPIRATION', 
	'86400', 
	'HTTP', 
	'How many seconds content should be cached on a browser', 
	'GLOBAL', 
	'Integer', 
	'86400', 
	1, 
	'HTTP CACHING',
	'7.5', 
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
			utl_config_parm.parm_name = 'HTTP_CACHE_EXPIRATION'
	)
;