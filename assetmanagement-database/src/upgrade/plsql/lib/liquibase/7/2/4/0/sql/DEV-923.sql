--liquibase formatted sql


--changeSet DEV-923:1 stripComments:false
-- conditional migration to add COMPANY type location in ref_loc_type table
INSERT INTO ref_loc_type 
	(
		loc_type_db_id, 
		loc_type_cd, 
		bitmap_db_id, 
		bitmap_tag, 
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
  'COMPANY', 
  0, 
  01, 
  'Company', 
  'This location type represents a company location. The company locations will be used to model the organizations that have location at an airport.',
  0, 
  to_date('28-02-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
  to_date('28-02-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
  0, 
  'MXI'
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			ref_loc_type 
		WHERE 
			ref_loc_type.loc_type_cd = 'COMPANY' AND
			ref_loc_type.loc_type_db_id = 0
	)
;