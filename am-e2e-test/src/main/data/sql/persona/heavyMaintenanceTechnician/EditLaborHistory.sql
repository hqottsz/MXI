-- Create labor rows for testing OPER-20988
INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK14 (BM_SCH_TSK14)'),
	0,
	'ACTV',
	0,
	'ENG',
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1
);

INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK15 (BM_SCH_TSK15)'),
	0,
	'IN WORK',
	0,
	'ENG',
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1
);


INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK16 (BM_SCH_TSK16)'),
	0,
	'IN WORK',
	0,
	'ENG',
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1
);

INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	cert_bool,
	insp_bool,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK17 (BM_SCH_TSK17)'),
	0,
	'IN WORK',
	0,
	'ENG',
	1,
	0,
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'CERT',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'PENDING',
	1,
	1
);


INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK11 (BM_SCH_TSK11)'),
	0,
	'IN WORK',
	0,
	'ENG',
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1
);

INSERT INTO 
	sched_labour 
(
	labour_db_id,
	labour_id, 
	sched_db_id, 
	sched_id,
	labour_stage_db_id,
	labour_stage_cd,
	labour_skill_db_id,
	labour_skill_cd,
	current_status_ord 
 )
VALUES
(
	4650,
	sched_labour_seq.nextval,
	4650,
	(select sched_stask.sched_id
	from sched_stask
	inner join evt_event on
	evt_event.event_db_id = sched_stask.sched_db_id and
	evt_event.event_id = sched_stask.sched_id
	where
	event_sdesc = 'BM_SCH_TSK18 (BM_SCH_TSK18)'),
	0,
	'ACTV',
	0,
	'LBR',
	1
);

INSERT INTO 
	sched_labour_role
(
	labour_role_db_id,
	labour_role_id,
	labour_db_id,
	labour_id,
	labour_role_type_db_id,
	labour_role_type_cd,
	sched_hr
)
VALUES 
(
	4650,
	sched_labour_role_seq.nextval,
	4650,
	sched_labour_seq.currval,
	0,
	'TECH',
	0.00
);

INSERT INTO 
	sched_labour_role_status 
(
	status_db_id, status_id,
	labour_role_db_id,
	labour_role_id,
	labour_role_status_db_id,
	labour_role_status_cd,
	status_ord,
	pass_bool,
	hr_db_id,
	hr_id
)
VALUES 
(
	4650,
	sched_labour_role_status_seq.nextval,
	4650,
	sched_labour_role_seq.currval,
	0,
	'ACTV',
	1,
	1,
	(SELECT oh.HR_DB_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'mxi'),
	(SELECT oh.HR_ID FROM org_hr oh INNER JOIN utl_user uu ON oh.USER_ID = uu.USER_ID WHERE uu.USERNAME = 'mxi')
);