--liquibase formatted sql
--changeSet OPER-15611:1 stripComments:false
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
    'CSCONFIGSS',
    'Configure Sensitive Systems',
    'Config slot configure sensitive systems',
    'CSCONFIGSS', 
    0,
    TO_DATE('2017-11-07', 'YYYY-MM-DD'),
    TO_DATE('2017-11-07', 'YYYY-MM-DD'),
    0, 
    'MXI' 
FROM
    dual
WHERE
    NOT EXISTS ( SELECT 1 FROM ref_log_action WHERE log_action_db_id = 0 and log_action_cd = 'CSCONFIGSS' ); 


--changeSet OPER-15611:2 stripComments:false
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
    'CSPROPAGATESS',
    'Propagate Sensitive Systems',
    'Config slot sensitive system propagation to sub config slots',
    'CSPROPAGATESS', 
    0, 
    TO_DATE('2017-11-07', 'YYYY-MM-DD'),
    TO_DATE('2017-11-07', 'YYYY-MM-DD'),
    0, 
    'MXI' 
FROM
    dual
WHERE
    NOT EXISTS ( SELECT 1 FROM ref_log_action WHERE log_action_db_id = 0 and log_action_cd = 'CSPROPAGATESS' );