--liquibase formatted sql

--changeSet OPER-28125:1 stripComments:false
-- insert ACDMCH as a ref_event_status
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
	SELECT 0,
		'TPBYPROXY',
		0,
		'PAUSE',
		0,
		80,
		'Paused By Proxy',
		'Job Stop was performed by proxy.',
		'BYPROXY',
		0,
		TO_DATE('2018-12-18', 'YYYY-MM-DD'),
		TO_DATE('2018-12-18', 'YYYY-MM-DD'),
		100,
		'MXI'
	FROM
		dual
	WHERE NOT EXISTS (SELECT 1 FROM ref_stage_reason WHERE stage_reason_db_id = 0 AND stage_reason_cd = 'TPBYPROXY');
