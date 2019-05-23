--liquibase formatted sql


--changeSet MX-22448:1 stripComments:false
-- HTTP Compression parameters
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
	'HTTP_COMPRESSION_EXCLUDE_URLS', 
	'.*((/servlet/report).*|Pdf)', 
	'HTTP', 
	'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs that need to be excluded from compression.', 
	'GLOBAL', 
	'Java Regex List', 
	'.*((/servlet/report).*|Pdf)', 
	1, 
	'HTTP COMPRESSION', 
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
			utl_config_parm.parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
	)
;