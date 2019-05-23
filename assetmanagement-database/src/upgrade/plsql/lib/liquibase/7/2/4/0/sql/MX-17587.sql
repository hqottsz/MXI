--liquibase formatted sql


--changeSet MX-17587:1 stripComments:false
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7000, 'DPO_RLM_RULE', 'IC_S_INV_PARM_DATA', 'RULE_8', 0, 0, to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7000 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_INV_PARM_DATA' AND RLM_ROW_CD2 = 'RULE_8' AND ADDED_BOOL = 0);

--changeSet MX-17587:2 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7000, 'DPO_RLM_RULE', 'IC_S_SCHED_ACTION', 'RULE_10', 0, 0, to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7000 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_ACTION' AND RLM_ROW_CD2 = 'RULE_10' AND ADDED_BOOL = 0);

--changeSet MX-17587:3 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7000, 'DPO_RLM_RULE', 'IC_S_SCHED_INST_PART', 'RULE_6', 0, 0, to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7000 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_INST_PART' AND RLM_ROW_CD2 = 'RULE_6' AND ADDED_BOOL = 0);

--changeSet MX-17587:4 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7000, 'DPO_RLM_RULE', 'IC_S_SCHED_RMVD_PART', 'RULE_6', 0, 0, to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7000 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_RMVD_PART' AND RLM_ROW_CD2 = 'RULE_6' AND ADDED_BOOL = 0);

--changeSet MX-17587:5 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7000, 'DPO_RLM_RULE', 'EC_S_INV_INV2', 'RULE_6', 0, 0, to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2010 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7000 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'EC_S_INV_INV2' AND RLM_ROW_CD2 = 'RULE_6' AND ADDED_BOOL = 0);