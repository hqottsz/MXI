--liquibase formatted sql


--changeSet DEV-117:1 stripComments:false
insert into DPO_RLM_RULE_CLASS (RULE_CLASS_CD, RULE_CLASS_TYPE, TABLE_NAME, RULE_CLASS_LDESC, COMPOSITE_BOOL, DURATION, SEQUENCING_BOOL, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'IC', 'S_SD_FAULT_RESULT', 'validating table constraints within the staging area for S_SD_FAULT_RESULT', 1, 'TRANSACTION', 0, 0, null, 0, to_date('15-05-2009 20:21:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 04:03:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE_CLASS WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT');

--changeSet DEV-117:2 stripComments:false
insert into DPO_RLM_RULE_CLASS (RULE_CLASS_CD, RULE_CLASS_TYPE, TABLE_NAME, RULE_CLASS_LDESC, COMPOSITE_BOOL, DURATION, SEQUENCING_BOOL, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SD_FAULT_RESULT', 'EC', 'S_SD_FAULT_RESULT', 'validating table constraints from staging to target for S_SD_FAULT_RESULT', 1, 'TRANSACTION', 0, 0, null, 0, to_date('15-05-2009 20:51:40', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 04:03:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE_CLASS WHERE RULE_CLASS_CD = 'EC_S_SD_FAULT_RESULT');

--changeSet DEV-117:3 stripComments:false
insert into DPO_RLM_OBJ_TYPE (RULE_OBJ_TYPE_CD, CONSUMPTION, TABLE_ALIAS_OF, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'PE_TA_S_SD_FAULT_RESULT', 'SHARED', 'S_SD_FAULT_RESULT', 0, to_date('10-06-2009 00:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 03:56:14', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_OBJ_TYPE WHERE RULE_OBJ_TYPE_CD = 'PE_TA_S_SD_FAULT_RESULT');

--changeSet DEV-117:4 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SD_FAULT_RESULT', 'EO_TA_S_SD_FAULT_RESULT', 'PE_TA_S_SD_FAULT_RESULT', 1, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_TA_S_SD_FAULT_RESULT');

--changeSet DEV-117:5 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SD_FAULT_RESULT', 'EO_RM_CONS_ELEMENTS', 'RM_CONS_ELEMENTS', 2, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_RM_CONS_ELEMENTS');

--changeSet DEV-117:6 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SD_FAULT_RESULT', 'EO_RM_XFER_KEY', 'RM_XFER_KEY', 3, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_RM_XFER_KEY');

--changeSet DEV-117:7 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'EO_TA_S_INV_OIL_STATUS_LOG', 'PE_TA_S_INV_OIL_STATUS_LOG', 1, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_TA_S_INV_OIL_STATUS_LOG');

--changeSet DEV-117:8 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'EO_RM_CONS_ELEMENTS', 'RM_CONS_ELEMENTS', 2, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_RM_CONS_ELEMENTS');

--changeSet DEV-117:9 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'EO_RM_XFER_KEY', 'RM_XFER_KEY', 3, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_EVT_STRUCT_CD = 'EO_RM_XFER_KEY');

--changeSet DEV-117:10 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_1', 'NOT_NULL', 'FAULT_DB_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SD_FAULT_RESULT"> ' || chr(10) || ' FAULT_DB_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_1');

--changeSet DEV-117:11 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_2', 'NOT_NULL', 'FAULT_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SD_FAULT_RESULT"> ' || chr(10) || ' FAULT_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_2');

--changeSet DEV-117:12 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_3', 'NOT_NULL', 'RESULT_EVENT_DB_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SD_FAULT_RESULT"> ' || chr(10) || ' RESULT_EVENT_DB_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_3');

--changeSet DEV-117:13 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_4', 'NOT_NULL', 'RESULT_EVENT_CD IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SD_FAULT_RESULT"> ' || chr(10) || ' RESULT_EVENT_CD IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_4');

--changeSet DEV-117:14 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_5', 'CHECK_RSTAT_CD', 'RSTAT_CD NOT IN (0,1,2,3)', '<condition>' || chr(10) || ' <object name="EO_TA_S_SD_FAULT_RESULT"> ' || chr(10) || ' RSTAT_CD NOT IN (0,1,2,3)' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_5');

--changeSet DEV-117:15 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SD_FAULT_RESULT', 'RULE_6', 'FOREIGN_KEY', 'FOREIGN KEY FK_SDFAULT_SDFAULTRESULT', '<condition>' || chr(10) || ' <and join="RM_UTL_RC_FUNCTION_PKG.VALID_FOREIGN_KEY(EO_RM_XFER_KEY, EO_RM_CONS_ELEMENTS) != 1">' || chr(10) || ' <object name="EO_RM_XFER_KEY" >' || chr(10) || '  xfer_inv_id is not null and xfer_transfer_mode is not null' || chr(10) || ' </object>' || chr(10) || ' <object name="EO_RM_CONS_ELEMENTS"> ' || chr(10) || ' ( PARENT_TAB_NAME = ''''S_SD_FAULT'''' AND ORIGIN_TAB_NAME = ''''SD_FAULT_RESULT'''' AND SELF_TAB_NAME = ''''S_SD_FAULT_RESULT'''' AND CONSTRAINT_NAME = ''''FK_INVINV_SCHEDKITMAP'''' )' || chr(10) || ' </object>' || chr(10) || ' </and>' || chr(10) || '</condition>', 'dbms_rlmgr.add_event(rule_class => v_rule_class,' || chr(10) || ' event_inst => sys.anydata.convertobject(rm_cons_elements(parent_tab_name => ''S_INV_INV'',' || chr(10) || ' origin_tab_name => ''SD_FAULT_RESULT'',' || chr(10) || ' self_tab_name => ''S_SD_FAULT_RESULT'',' || chr(10) || ' constraint_name => ''FK_SDFAULT_SDFAULTRESULT'',' || chr(10) || ' rlm$crttime => NULL)));', 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SD_FAULT_RESULT' AND RULE_ID = 'RULE_6');

--changeSet DEV-117:16 stripComments:false
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_1', 1, 0, to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_1' AND ADDED_BOOL = 1);

--changeSet DEV-117:17 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_2', 1, 0, to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_2' AND ADDED_BOOL = 1);

--changeSet DEV-117:18 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_3', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_3' AND ADDED_BOOL = 1);

--changeSet DEV-117:19 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_4', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_4' AND ADDED_BOOL = 1);

--changeSet DEV-117:20 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_5', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_5' AND ADDED_BOOL = 1);

--changeSet DEV-117:21 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 'DPO_RLM_RULE', 'IC_S_SD_FAULT_RESULT', 'RULE_6', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7500 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SD_FAULT_RESULT' AND RLM_ROW_CD2 = 'RULE_6' AND ADDED_BOOL = 1);