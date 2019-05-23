--liquibase formatted sql

--changeSet OPER-19053:1 stripComments:false
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
		'ACDMCH',
		NULL,
		NULL,
		0,
		80,
		'Reinducted',
		'Inventory was reinducted and record unarchived because part number and serial number match an archived record.',
		'DMCH',
		0,
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		100,
		'MXI'
	FROM
		dual
	WHERE NOT EXISTS (SELECT 1 FROM ref_stage_reason WHERE stage_reason_db_id = 0 AND stage_reason_cd = 'ACDMCH');

--changeSet OPER-19053:2 stripComments:false
-- insert ACINDMCH as a ref_stage_reason
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
		'ACINDMCH',
		NULL,
		NULL,
		0,
		80,
		'Reinducted with new Part Number',
		'Inventory was reinducted and record unarchived because serial number, part group, and manufacturer match an archived record.',
		'INDMCH',
		0,
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		100,
		'MXI'
	FROM
		dual
	WHERE NOT EXISTS (SELECT 1 FROM ref_stage_reason WHERE stage_reason_db_id = 0 AND stage_reason_cd = 'ACINDMCH');


--changeSet OPER-19053:3 stripComments:false
-- insert ACFLDMCH as a ref_stage_reason
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
		'ACFLDMCH',
		NULL,
		NULL,
		0,
		80,
		'Reinduction Attempted',
		'New inventory record was created because more than one archived record matched the serial number, part group, and manufacturer.',
		'FLDMCH',
		0,
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		TO_DATE('2018-02-07', 'YYYY-MM-DD'),
		100,
		'MXI'
	FROM
		dual
	WHERE NOT EXISTS (SELECT 1 FROM ref_stage_reason WHERE stage_reason_db_id = 0 AND stage_reason_cd = 'ACFLDMCH');