--liquibase formatted sql


--changeSet MX-17958:1 stripComments:false
INSERT INTO ref_rel_type
	(
		rel_type_db_id, 
		rel_type_cd, 
		desc_sdesc, 
		desc_ldesc,  
		rstat_cd, 
		creation_dt, 
		revision_dt, 
		revision_db_id, 
		revision_user
	) 
SELECT 
	0, 
	'TRNSFER', 
	'Related Transfer Event', 
	'Related Transfer Event', 
	0, 
	TO_DATE('2010-08-11', 'YYYY-MM-DD'), 
	TO_DATE('2010-08-11', 'YYYY-MM-DD'), 
	100, 
	'MXI'
FROM 
	dual 
WHERE 
	NOT EXISTS
	(
		SELECT 
			1 
		FROM 
			ref_rel_type 
		WHERE 
			ref_rel_type.rel_type_db_id = 0 AND
			ref_rel_type.rel_type_cd = 'TRNSFER'
	)
;