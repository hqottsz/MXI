-- Deferral Refererence RTD-DEFER-1-REF
INSERT
    INTO
        fail_defer_ref(
            FAIL_DEFER_REF_DB_ID,
            FAIL_DEFER_REF_ID,
            ASSMBL_DB_ID,
            ASSMBL_CD,
			ASSMBL_BOM_ID,
            DEFER_REF_SDESC,
            FAIL_SEV_DB_ID,
            FAIL_SEV_CD,
            FAIL_DEFER_DB_ID,
            FAIL_DEFER_CD,
            DEFER_REF_LDESC,
            APPL_LDESC,
            INST_SYSTEMS_QT,
            OP_SYSTEMS_QT,
            OPER_RESTRICTIONS_LDESC,
            MAINT_ACTIONS_LDESC,
            PERF_PENALTIES_LDESC
        )
    SELECT
        4650,
        FAIL_DEFER_REF_ID_SEQ.nextval,
        4650,
        'ACFTMOC1',
		(SELECT alt_id FROM EQP_ASSMBL_BOM WHERE ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND ASSMBL_BOM_ID = 1),
        'RTD-DEFER-1-DEF',
        0,
        'MEL',
        0,
        'MEL B',
        'A deferral reference for RTD-DEFER-1-DEF.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Operators for Deferral Refererence RTD-DEFER-1-REF
INSERT
    INTO
        fail_defer_carrier(
            FAIL_DEFER_REF_DB_ID,
            FAIL_DEFER_REF_ID,
            CARRIER_DB_ID,
            CARRIER_ID
        )
    SELECT
        4650,
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'RTD-DEFER-1-DEF'),
        0,
        1002
    FROM
        DUAL;

