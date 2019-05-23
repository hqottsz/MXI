 -- Recurring Inspection 'Recurring Inspection for Deferrals' for Deferral Reference PUD-DS-1-DF
INSERT
    INTO
		fail_defer_ref_task_defn(
			FAIL_DEFER_REF_ID,
			TASK_DEFN_ID,
			CTRL_DB_ID,
			REVISION_NO,
			CREATION_DB_ID,
			RSTAT_CD,
			CREATION_DT,
			REVISION_DT,
			REVISION_DB_ID,
			REVISION_USER
	    )
	SELECT
		(SELECT ALT_ID FROM fail_defer_ref WHERE defer_ref_sdesc = 'PUD-DS-1-DEF'),
		(SELECT ALT_ID FROM task_defn WHERE task_defn_id = (SELECT task_id FROM task_task WHERE task_name = 'Recurring Inspection for Deferrals')),
		4650,
		1,
		4650,
		0,
		SYSDATE,
		SYSDATE,
		4650,
		'mxi'
    FROM
        DUAL;

 -- Recurring Inspection 'Recurring Inspection for Deferrals' for Deferral Reference PUD-AUTH-1-DEF
INSERT
    INTO
		fail_defer_ref_task_defn(
			FAIL_DEFER_REF_ID,
			TASK_DEFN_ID,
			CTRL_DB_ID,
			REVISION_NO,
			CREATION_DB_ID,
			RSTAT_CD,
			CREATION_DT,
			REVISION_DT,
			REVISION_DB_ID,
			REVISION_USER
	    )
	SELECT
		(SELECT ALT_ID FROM fail_defer_ref WHERE defer_ref_sdesc = 'PUD-AUTH-1-DEF'),
		(SELECT ALT_ID FROM task_defn WHERE task_defn_id = (SELECT task_id FROM task_task WHERE task_name = 'Recurring Inspection for Deferrals')),
		4650,
		1,
		4650,
		0,
		SYSDATE,
		SYSDATE,
		4650,
		'mxi'
    FROM
        DUAL;