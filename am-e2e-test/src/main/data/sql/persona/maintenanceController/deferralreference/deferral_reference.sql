-- Deferral Reference DR-1-SEARCH
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
        'DR-1-SEARCH',
        0,
        'MEL',
        0,
        'MEL B',
        'A deferral reference for DR-1-SEARCH.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Conflicting Deferral Reference DR-1-SEARCH-CONFLICTED
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
        'DR-1-SEARCH-CONFLICTED',
        0,
        'MEL',
        0,
        'MEL B',
        'Conflicted deferral reference for DR-1-SEARCH.',
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DR-1-SEARCH-CONFLICTED'),
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'DR-1-SEARCH'),
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'DR-1-SEARCH-CONFLICTED')
    FROM
       DUAL;


--Associated Deferral Reference DR-1-SEARCH-ASSOCIATED
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
        'DR-1-SEARCH-ASSOCIATED',
        0,
        'MEL',
        0,
        'MEL B',
        'Associated deferral reference for DR-1-SEARCH.',
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DR-1-SEARCH-ASSOCIATED'),
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'DR-1-SEARCH'),
        (SELECT ALT_ID FROM fail_defer_ref WHERE DEFER_REF_SDESC = 'DR-1-SEARCH-ASSOCIATED')
    FROM
       DUAL;

--Operators for Deferral Refererence DR-1-SEARCH
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DR-1-SEARCH'),
        0,
        1002
    FROM
        DUAL;

--Degraded capabilities for Deferral Refererence DR-1-SEARCH
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DR-1-SEARCH'),
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DR-1-SEARCH'),
        'ETOPS',
        10,
        'NO_ETOPS',
        10
    FROM
        DUAL;

-- Deferral Reference DRC-EDIT-1
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
        'DRC-EDIT-1',
        0,
        'MEL',
        0,
        'MEL B',
        'A deferral reference for DRC-EDIT-1.',
        '123-900',
        0,
        0,
        'An operational restriction.',
        'Some maintenance actions.',
		'Some performance penalties.'
    FROM
        DUAL;

--Operators for Deferral Refererence DRC-EDIT-1
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DRC-EDIT-1'),
        0,
        1002
    FROM
        DUAL;
--Degraded capabilities for Deferral Refererence DRC-EDIT-1
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DRC-EDIT-1'),
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
        (SELECT ALT_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'DRC-EDIT-1'),
        'ETOPS',
        10,
        'NO_ETOPS',
        10
    FROM
        DUAL;
		
-- Deferral Reference OPER-22784-1
INSERT INTO fail_defer_ref 
            (fail_defer_ref_db_id, 
             fail_defer_ref_id, 
             assmbl_db_id, 
             assmbl_cd, 
             defer_ref_sdesc, 
             fail_sev_db_id, 
             fail_sev_cd, 
             fail_defer_db_id, 
             fail_defer_cd, 
             rstat_cd, 
             creation_dt, 
             revision_dt, 
             revision_db_id, 
             revision_user, 
             defer_ref_status_cd, 
             inst_systems_qt, 
             op_systems_qt,
             defer_ref_ldesc) 
VALUES      (4650, 
             FAIL_DEFER_REF_ID_SEQ.nextval, 
             4650, 
             'ACFTMOC1', 
             'OPER-22784-1', 
             0, 
             'MEL', 
             0, 
             'MEL B', 
             0, 
             sysdate, 
             sysdate, 
             4650, 
             'mxi', 
             'ACTV', 
             0, 
             0,
             'First' ); 
             
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'OPER-22784-1' and defer_ref_ldesc = 'First'),
        0,
        1001
    FROM
        DUAL;
        
		
-- Deferral Reference OPER-22784-1 second assembly

INSERT INTO fail_defer_ref 
            (fail_defer_ref_db_id, 
             fail_defer_ref_id, 
             assmbl_db_id, 
             assmbl_cd, 
             defer_ref_sdesc, 
             fail_sev_db_id, 
             fail_sev_cd, 
             fail_defer_db_id, 
             fail_defer_cd, 
             rstat_cd, 
             creation_dt, 
             revision_dt, 
             revision_db_id, 
             revision_user, 
             defer_ref_status_cd, 
             inst_systems_qt, 
             op_systems_qt,
             defer_ref_ldesc) 
VALUES      (4650, 
             FAIL_DEFER_REF_ID_SEQ.nextval, 
             4650, 
             'ACFTMOC1', 
             'OPER-22784-1', 
             0, 
             'MEL', 
             0, 
             'MEL C', 
             0, 
             sysdate, 
             sysdate, 
             4650, 
             'mxi', 
             'ACTV', 
             0, 
             0,
             'Second' ); 
            
        
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
        (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = 'ACFTMOC1' AND ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = 'OPER-22784-1'  and defer_ref_ldesc = 'Second'),
        0,
        1002
    FROM
        DUAL;