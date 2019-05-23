--liquibase formatted sql

--changeSet OPER-19518-3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
INSERT INTO ref_log_action
(
    log_action_db_id,
    log_action_cd,
    desc_sdesc,
    desc_ldesc,
    user_cd,
    rstat_cd,
    creation_dt,
    revision_dt,
    revision_db_id,
    revision_user
)
SELECT
    0,
    'TDMANUALPREVENT',
    'Prevent manual initialization of task definitions.',
    'Prevent manual initialization of task definitions.',
    'TDMANUALPREVENT',
    0,
    TO_DATE('2018-05-08', 'YYYY-MM-DD'),
    TO_DATE('2018-05-08', 'YYYY-MM-DD'),
    0,
    'MXI'
FROM
    dual
WHERE
    NOT EXISTS ( SELECT 1 FROM ref_log_action WHERE log_action_db_id = 0 and log_action_cd = 'TDMANUALPREVENT' )
/

--changeSet OPER-19518-3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
INSERT INTO ref_log_action
(
    log_action_db_id,
    log_action_cd,
    desc_sdesc,
    desc_ldesc,
    user_cd,
    rstat_cd,
    creation_dt,
    revision_dt,
    revision_db_id,
    revision_user
)
SELECT
    0,
    'TDMANUALALLOW',
    'Allow manual initialization of task definitions.',
    'Allow manual initialization of task definitions.',
    'TDMANUALALLOW',
    0,
    TO_DATE('2018-05-08', 'YYYY-MM-DD'),
    TO_DATE('2018-05-08', 'YYYY-MM-DD'),
    0,
    'MXI'
FROM
    dual
WHERE
    NOT EXISTS ( SELECT 1 FROM ref_log_action WHERE log_action_db_id = 0 and log_action_cd = 'TDMANUALALLOW' )
/