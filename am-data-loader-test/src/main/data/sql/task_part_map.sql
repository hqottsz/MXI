INSERT INTO task_part_map (task_db_id, task_id, part_no_db_id, part_no_id)
VALUES (4650,
	(SELECT t.task_id FROM task_task t WHERE t.task_cd = 'AL_RECVD_DT_COMP'),
	4650,
	(SELECT u.part_no_id FROM eqp_part_no u WHERE u.part_no_oem = 'A0000001REC')
	);
