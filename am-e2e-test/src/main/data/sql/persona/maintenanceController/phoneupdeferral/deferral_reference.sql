-- Deferral Refererence PUD-DS-1-DEF
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
        'PUD-DS-1-DEF',
        0,
        'MEL',
        0,
        'MEL A',
        'A deferral reference for PUD-DS.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;
--Operators for Deferral Refererence PUD-DS-1-DEF
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-DS-1-DEF'),
        0,
        1002
    FROM
        DUAL;
--Associated Deferral Reference PUD-DS-DEF-1-ASSOCIATED
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
            OP_SYSTEMS_QT
        )
    SELECT
        4650,
        FAIL_DEFER_REF_ID_SEQ.nextval,
        4650,
        'ACFTMOC1',
		(SELECT alt_id FROM EQP_ASSMBL_BOM WHERE ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND ASSMBL_BOM_ID = 1),
        'PUD-DS-1-DEF-ASSOCIATED',
        0,
        'MEL',
        0,
        'MEL A',
        'Associated deferral reference for PUD-DS.',
        '123-900',
        0,
        0
  FROM
        DUAL;

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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-DS-1-DEF-ASSOCIATED'),
        0,
        1002
    FROM
        DUAL;

  INSERT
    INTO
        fail_defer_ref_rel_def (
            FAIL_DEFER_REF_ID,
            REL_FAIL_DEFER_REF_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'PUD-DS-1-DEF'),
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'PUD-DS-1-DEF-ASSOCIATED')
    FROM
       DUAL;


--Conflicting Deferral Reference PUD-DS-1-DEF-CONFLICTED
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
            OP_SYSTEMS_QT
        )
    SELECT
        4650,
        FAIL_DEFER_REF_ID_SEQ.nextval,
        4650,
        'ACFTMOC1',
		(SELECT alt_id FROM EQP_ASSMBL_BOM WHERE ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND ASSMBL_BOM_ID = 1),
        'PUD-DS-1-DEF-CONFLICTED',
        0,
        'MEL',
        0,
        'MEL A',
        'Conflicted deferral reference for PUD-DS.',
        '123-900',
        0,
        0
  FROM
        DUAL;

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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-DS-1-DEF-CONFLICTED'),
        0,
        1002
    FROM
        DUAL;

   INSERT
    INTO
        fail_defer_ref_conflict_def(
            FAIL_DEFER_REF_ID,
            CONFLICT_FAIL_DEFER_REF_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'PUD-DS-1-DEF'),
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'PUD-DS-1-DEF-CONFLICTED')
    FROM
       DUAL;

 --Degraded capabilities for Deferral Refererence PUD-DS-1-DEF
INSERT
    INTO
        fail_defer_ref_degrad_cap(
            FAIL_DEFER_REF_ID,
            CAP_CD,
            CAP_DB_ID,
            CAP_LEVEL_CD,
            CAP_LEVEL_DB_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-DS-1-DEF'),
        'WIFI',
        10,
        'NO',
        10
    FROM
        DUAL;

 INSERT
    INTO
        fail_defer_ref_degrad_cap(
            FAIL_DEFER_REF_ID,
            CAP_CD,
            CAP_DB_ID,
            CAP_LEVEL_CD,
            CAP_LEVEL_DB_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-DS-1-DEF'),
        'ETOPS',
        10,
        'NO_ETOPS',
        10
    FROM
        DUAL;

-- Deferral Refererence PUD-AUTH-1-DEF
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
        'PUD-AUTH-1-DEF',
        0,
        'MEL',
        0,
        'MEL A',
        'A deferral reference for PUD-AUTH-1-DEF.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Operators for Deferral Refererence PUD-AUTH-1-DEF
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        0,
        1002
    FROM
        DUAL;

--Degraded capabilities for Deferral Refererence PUD-AUTH-1-DEF
INSERT
    INTO
        fail_defer_ref_degrad_cap(
            FAIL_DEFER_REF_ID,
            CAP_CD,
            CAP_DB_ID,
            CAP_LEVEL_CD,
            CAP_LEVEL_DB_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        'WIFI',
        10,
        'NO',
        10
    FROM
        DUAL;

INSERT
    INTO
        fail_defer_ref_degrad_cap(
            FAIL_DEFER_REF_ID,
            CAP_CD,
            CAP_DB_ID,
            CAP_LEVEL_CD,
            CAP_LEVEL_DB_ID
        )
    SELECT
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        'ETOPS',
        10,
        'NO_ETOPS',
        10
    FROM
        DUAL;

--Custom deadlines for Deferral Reference PUD-AUTH-1-DEF

INSERT
    INTO
        fail_defer_ref_dead(
			DATA_TYPE_DB_ID,
			DATA_TYPE_ID,
			FAIL_DEFER_REF_ID,
			DEAD_QT
        )
    SELECT
        0,
        21,-- DAYS
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        2
    FROM
        DUAL;

INSERT
    INTO
        fail_defer_ref_dead(
			DATA_TYPE_DB_ID,
			DATA_TYPE_ID,
			FAIL_DEFER_REF_ID,
			DEAD_QT
        )
    SELECT
        0,
        10,-- CYCLES
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        200
    FROM
        DUAL;

INSERT
    INTO
        fail_defer_ref_dead(
			DATA_TYPE_DB_ID,
			DATA_TYPE_ID,
			FAIL_DEFER_REF_ID,
			DEAD_QT
        )
    SELECT
        0,
        1,-- HOURS
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-AUTH-1-DEF'),
        100
    FROM
        DUAL;

-- Deferral Refererence PUD-OPEN-1-DEF
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
        'PUD-OPEN-1-DEF',
        0,
        'MEL',
        0,
        'MEL B',
        'A deferral reference for PUD-OPEN-1-DEF.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Operators for Deferral Refererence PUD-OPEN-1-DEF
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'PUD-OPEN-1-DEF'),
        0,
        1002
    FROM
        DUAL;

