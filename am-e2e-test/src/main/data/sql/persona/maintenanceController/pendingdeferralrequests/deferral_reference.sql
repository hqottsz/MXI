-- Deferral Refererence PDR-AUTH-1-DEF
INSERT
    INTO
        fail_defer_ref(
            fail_defer_ref_db_id,
            fail_defer_ref_id,
            assmbl_db_id,
            assmbl_cd,
			assmbl_bom_id,
            defer_ref_sdesc,
            fail_sev_db_id,
            fail_sev_cd,
            fail_defer_db_id,
            fail_defer_cd,
            defer_ref_ldesc,
            appl_ldesc,
            inst_systems_qt,
            op_systems_qt,
            oper_restrictions_ldesc,
            maint_actions_ldesc,
            perf_penalties_ldesc
        )
    SELECT
        4650,
        fail_defer_ref_id_seq.nextval,
        4650,
        'ACFTMOC1',
		(SELECT alt_id FROM eqp_assmbl_bom WHERE assmbl_cd = 'ACFTMOC1' AND assmbl_db_id = 4650 AND assmbl_bom_id = 1),
        'PDR-AUTH-1-DEF',
        0,
        'MEL',
        0,
        'MEL C',
        'A deferral reference for PDR-AUTH-1.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Operators for Deferral Refererence PDR-AUTH-1-DEF
INSERT
    INTO
        fail_defer_carrier(
            fail_defer_ref_db_id,
            fail_defer_ref_id,
            carrier_db_id,
            carrier_id
        )
    SELECT
        4650,
        (SELECT fail_defer_ref_id FROM fail_defer_ref WHERE fail_defer_ref_db_id = 4650 AND assmbl_cd = 'ACFTMOC1' AND assmbl_db_id = 4650 AND defer_ref_sdesc = 'PDR-AUTH-1-DEF'),
        0,
        1002
    FROM
        DUAL;

--Degraded capabilities for Deferral Refererence PDR-AUTH-1-DEF
INSERT
    INTO
        fail_defer_ref_degrad_cap(
            fail_defer_ref_id,
            cap_cd,
            cap_db_id,
            cap_level_cd,
            cap_level_db_id
        )
    SELECT
        (SELECT alt_id FROM fail_defer_ref WHERE fail_defer_ref_db_id = 4650 AND assmbl_cd = 'ACFTMOC1' AND assmbl_db_id = 4650 AND defer_ref_sdesc = 'PDR-AUTH-1-DEF'),
        'WIFI',
        10,
        'NO',
        10
    FROM
        DUAL;

 INSERT
    INTO
        fail_defer_ref_degrad_cap(
            fail_defer_ref_id,
            cap_cd,
            cap_db_id,
            cap_level_cd,
            cap_level_db_id
        )
    SELECT
        (SELECT alt_id FROM fail_defer_ref WHERE fail_defer_ref_db_id = 4650 AND assmbl_cd = 'ACFTMOC1' AND assmbl_db_id = 4650 AND defer_ref_sdesc = 'PDR-AUTH-1-DEF'),
        'ETOPS',
        10,
        'NO_ETOPS',
        10
    FROM
        DUAL;