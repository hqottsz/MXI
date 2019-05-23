-- Create labor rows for testing OPER-20986

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
	event_sdesc = 'BM_SCH_TSK2 (BM_SCH_TSK2)'),
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

-- Create labor rows for testing OPER-20986 

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
	event_sdesc = 'BM_SCH_TSK4 (BM_SCH_TSK4)'),
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

-- Create labor rows for testing OPER-20986 

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
	event_sdesc = 'BM_SCH_TSK5 (BM_SCH_TSK5)'),
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


-- Create labor rows for testing OPER-20986 

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
	event_sdesc = 'BM_SCH_TSK6 (BM_SCH_TSK6)'),
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

-- Create labor rows for testing OPER-20986 

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
	event_sdesc = 'BM_SCH_TSK7 (BM_SCH_TSK7)'),
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

-- Create labor rows for testing OPER-20986 

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
	event_sdesc = 'BM_SCH_TSK8 (BM_SCH_TSK8)'),
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

-- Create labor rows for testing OPER-24838

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
	event_sdesc = 'BM_SCH_TSK19 (BM_SCH_TSK19)'),
	0,
	'COMPLETE',
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
	'COMPLETE',
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
	event_sdesc = 'BM_SCH_TSK20 (BM_SCH_TSK20)'),
	0,
	'COMPLETE',
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
	'COMPLETE',
	1,
	1
);