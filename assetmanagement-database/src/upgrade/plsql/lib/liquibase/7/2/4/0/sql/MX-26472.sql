--liquibase formatted sql


--changeSet MX-26472:1 stripComments:false
-- insert a UPDOPSRES as a ref_event_status  
INSERT INTO 
	ref_event_status 
	(
		event_status_db_id, 
		event_status_cd, 
		event_type_db_id, 
		event_type_cd, 
		bitmap_db_id, 
		bitmap_tag, 
		desc_sdesc, 
		desc_ldesc, 
		user_status_cd, 
		status_ord, 
		auth_reqd_bool, 
		rstat_cd, 
		creation_dt, 
		revision_dt,
		revision_db_id, 
		revision_user
	)
	SELECT 	0, 
		'UPDOPSRES', 
		0, 
		'CF', 
		0, 
		1, 
		'Update', 
		'Update Operational Restriction', 
		'UPD', 
		'1', 
		'0',  
		0,
		to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
	        to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
		100, 
		'MXI'
	FROM 
		dual
	WHERE NOT EXISTS (SELECT 1 FROM ref_event_status WHERE event_status_db_id = 0 AND event_status_cd = 'UPDOPSRES'); 

--changeSet MX-26472:2 stripComments:false
-- insert a UPDOPSRESNOT as a ref_stage_reason 
INSERT INTO 
	ref_stage_reason 
	(
		stage_reason_db_id, 
		stage_reason_cd, 
		event_status_db_id, 
		event_status_cd, 
		bitmap_db_id, 
		bitmap_tag,  
		desc_sdesc, 
		desc_ldesc, 
		user_reason_cd,
		rstat_cd,
		creation_dt,
		revision_dt,
		revision_db_id, 
		revision_user
	)	
      SELECT 	0, 
      		'UPDOPSRESNOTE', 
      		0, 
      		'UPDOPSRES', 
      		0, 
      		01,  
      		'Update Operational Restriction Note', 
      		'Update Operational Restriction Note',
      		'NA',
      		0,  
      		to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
	        to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
      		100, 
      		'MXI'
     FROM 
     		dual
     WHERE NOT EXISTS (SELECT 1 FROM ref_stage_reason WHERE stage_reason_db_id = 0 AND stage_reason_cd = 'UPDOPSRESNOTE');  