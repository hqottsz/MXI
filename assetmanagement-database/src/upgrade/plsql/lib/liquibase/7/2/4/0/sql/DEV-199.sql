--liquibase formatted sql


--changeSet DEV-199:1 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER', 'MASTER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER' and db_type_cd = 'MASTER' );      

--changeSet DEV-199:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ', 'MASTER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ' and db_type_cd = 'MASTER' );      

--changeSet DEV-199:3 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ENFORCE_WORKSCOPE_ORDER', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER' and db_type_cd = 'OPER' );      

--changeSet DEV-199:4 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_EDIT_WORKSCOPE_ORDER', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_WORKSCOPE_ORDER' and db_type_cd = 'OPER' );      

--changeSet DEV-199:5 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ASSIGN_WS_TASK_LOCATION', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_WS_TASK_LOCATION' and db_type_cd = 'OPER' );      

--changeSet DEV-199:6 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_MOVE_UP_WORKSCOPE_ORDER', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_MOVE_UP_WORKSCOPE_ORDER' and db_type_cd = 'OPER' );      

--changeSet DEV-199:7 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_MOVE_DOWN_WORKSCOPE_ORDER', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_MOVE_DOWN_WORKSCOPE_ORDER' and db_type_cd = 'OPER' );      

--changeSet DEV-199:8 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 390,1,'REF_TASK_MUST_REMOVE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 390);

--changeSet DEV-199:9 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 1141, 2, 'SCHED_KIT_MAP', 1, null, null, 0, to_date('09-09-2009', 'dd-mm-yyyy'), to_date('09-09-2009 17:22:45', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 1141);

--changeSet DEV-199:10 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4390,5,'REF_TASK_MUST_REMOVE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 4390);

--changeSet DEV-199:11 stripComments:false
update DPO_RLM_OBJ_TYPE
set REVISION_DB_ID = 0
where REVISION_DB_ID = 4650;

--changeSet DEV-199:12 stripComments:false
update DPO_RLM_OBJ_TYPE
set REVISION_USER = 'MXI'
where REVISION_USER = 'M906';

--changeSet DEV-199:13 stripComments:false
insert into DPO_RLM_OBJ_TYPE (RULE_OBJ_TYPE_CD, CONSUMPTION, TABLE_ALIAS_OF, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'PE_TA_S_SCHED_KIT_MAP', 'SHARED', 'S_SCHED_KIT_MAP', 0, to_date('10-06-2009 00:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 03:56:14', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_OBJ_TYPE WHERE RULE_OBJ_TYPE_CD = 'PE_TA_S_SCHED_KIT_MAP');

--changeSet DEV-199:14 stripComments:false
update DPO_RLM_OBJ_TYPE
set REVISION_DB_ID = 0
where REVISION_DB_ID = 4650;

--changeSet DEV-199:15 stripComments:false
update DPO_RLM_OBJ_TYPE
set REVISION_USER = 'MXI'
where REVISION_USER = 'M906';

--changeSet DEV-199:16 stripComments:false
insert into DPO_RLM_RULE_CLASS (RULE_CLASS_CD, RULE_CLASS_TYPE, TABLE_NAME, RULE_CLASS_LDESC, COMPOSITE_BOOL, DURATION, SEQUENCING_BOOL, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'IC', 'S_SCHED_KIT_MAP', 'validating table constraints within the staging area for S_SCHED_KIT_MAP', 1, 'TRANSACTION', 0, 0, null, 0, to_date('15-05-2009 20:21:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 04:03:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE_CLASS WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP');

--changeSet DEV-199:17 stripComments:false
insert into DPO_RLM_RULE_CLASS (RULE_CLASS_CD, RULE_CLASS_TYPE, TABLE_NAME, RULE_CLASS_LDESC, COMPOSITE_BOOL, DURATION, SEQUENCING_BOOL, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SCHED_KIT_MAP', 'EC', 'S_SCHED_KIT_MAP', 'validating table constraints from staging to target for S_SCHED_KIT_MAP', 1, 'TRANSACTION', 0, 0, null, 0, to_date('15-05-2009 20:51:40', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-06-2009 04:03:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE_CLASS WHERE RULE_CLASS_CD = 'EC_S_SCHED_KIT_MAP');

--changeSet DEV-199:18 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SCHED_KIT_MAP', 'EO_TA_S_SCHED_KIT_MAP', 'PE_TA_S_SCHED_KIT_MAP', 1, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_TA_S_SCHED_KIT_MAP');

--changeSet DEV-199:19 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SCHED_KIT_MAP', 'EO_RM_CONS_ELEMENTS', 'RM_CONS_ELEMENTS', 2, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_RM_CONS_ELEMENTS');

--changeSet DEV-199:20 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'EC_S_SCHED_KIT_MAP', 'EO_RM_XFER_KEY', 'RM_XFER_KEY', 3, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'EC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_RM_XFER_KEY');

--changeSet DEV-199:21 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'EO_TA_S_INV_OIL_STATUS_LOG', 'PE_TA_S_INV_OIL_STATUS_LOG', 1, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_TA_S_INV_OIL_STATUS_LOG');

--changeSet DEV-199:22 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'EO_RM_CONS_ELEMENTS', 'RM_CONS_ELEMENTS', 2, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_RM_CONS_ELEMENTS');

--changeSet DEV-199:23 stripComments:false
insert into DPO_RLM_EVT_STRUCT (RULE_CLASS_CD, RULE_EVT_STRUCT_CD, RULE_OBJ_TYPE_CD, POSITION, AUTOCOMMIT_BOOL, ORDERING, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'EO_RM_XFER_KEY', 'RM_XFER_KEY', 3, 0, null, 0, to_date('04-06-2009 14:50:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-06-2009 00:51:11', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_EVT_STRUCT WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_EVT_STRUCT_CD = 'EO_RM_XFER_KEY');

--changeSet DEV-199:24 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_1', 'NOT_NULL', 'SCHED_DB_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' SCHED_DB_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_1');

--changeSet DEV-199:25 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_2', 'NOT_NULL', 'SCHED_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' SCHED_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_2');

--changeSet DEV-199:26 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_3', 'NOT_NULL', 'INV_NO_DB_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' INV_NO_DB_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_3');

--changeSet DEV-199:27 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_4', 'NOT_NULL', 'INV_NO_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' INV_NO_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_4');

--changeSet DEV-199:28 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_5', 'NOT_NULL', 'KIT_INV_NO_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' KIT_INV_NO_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_5');

--changeSet DEV-199:29 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_6', 'NOT_NULL', 'KIT_INV_NO_DB_ID IS NULL', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' KIT_INV_NO_DB_ID IS NULL ' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:27:17', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_6');

--changeSet DEV-199:30 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_7', 'CHECK_RSTAT_CD', 'RSTAT_CD NOT IN (0,1,2,3)', '<condition>' || chr(10) || ' <object name="EO_TA_S_SCHED_KIT_MAP"> ' || chr(10) || ' RSTAT_CD NOT IN (0,1,2,3)' || chr(10) || ' </object>' || chr(10) || '</condition>', null, 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_7');

--changeSet DEV-199:31 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_8', 'FOREIGN_KEY', 'FOREIGN KEY FK_INVINV_SCHEDKITMAP', '<condition>' || chr(10) || ' <and join="RM_UTL_RC_FUNCTION_PKG.VALID_FOREIGN_KEY(EO_RM_XFER_KEY, EO_RM_CONS_ELEMENTS) != 1">' || chr(10) || ' <object name="EO_RM_XFER_KEY" >' || chr(10) || '  xfer_inv_id is not null and xfer_transfer_mode is not null' || chr(10) || ' </object>' || chr(10) || ' <object name="EO_RM_CONS_ELEMENTS"> ' || chr(10) || ' ( PARENT_TAB_NAME = ''''S_INV_INV'''' AND ORIGIN_TAB_NAME = ''''SCHED_KIT_MAP'''' AND SELF_TAB_NAME = ''''S_SCHED_KIT_MAP'''' AND CONSTRAINT_NAME = ''''FK_INVINV_SCHEDKITMAP'''' )' || chr(10) || ' </object>' || chr(10) || ' </and>' || chr(10) || '</condition>', 'dbms_rlmgr.add_event(rule_class => v_rule_class,' || chr(10) || ' event_inst => sys.anydata.convertobject(rm_cons_elements(parent_tab_name => ''S_INV_INV'',' || chr(10) || ' origin_tab_name => ''SCHED_KIT_MAP'',' || chr(10) || ' self_tab_name => ''S_SCHED_KIT_MAP'',' || chr(10) || ' constraint_name => ''FK_INVINV_SCHEDKITMAP'',' || chr(10) || ' rlm$crttime => NULL)));', 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_8');

--changeSet DEV-199:32 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_9', 'FOREIGN_KEY', 'FOREIGN KEY FK_INVKIT_SCHEDKITMAP', '<condition>' || chr(10) || ' <and join="RM_UTL_RC_FUNCTION_PKG.VALID_FOREIGN_KEY(EO_RM_XFER_KEY, EO_RM_CONS_ELEMENTS) != 1">' || chr(10) || ' <object name="EO_RM_XFER_KEY" >' || chr(10) || '  xfer_inv_id is not null and xfer_transfer_mode is not null' || chr(10) || ' </object>' || chr(10) || ' <object name="EO_RM_CONS_ELEMENTS"> ' || chr(10) || ' ( PARENT_TAB_NAME = ''''S_INV_KIT'''' AND ORIGIN_TAB_NAME = ''''SCHED_KIT_MAP'''' AND SELF_TAB_NAME = ''''S_SCHED_KIT_MAP'''' AND CONSTRAINT_NAME = ''''FK_INVKIT_SCHEDKITMAP'''' )' || chr(10) || ' </object>' || chr(10) || ' </and>' || chr(10) || '</condition>', 'dbms_rlmgr.add_event(rule_class => v_rule_class,' || chr(10) || ' event_inst => sys.anydata.convertobject(rm_cons_elements(parent_tab_name => ''S_INV_KIT'',' || chr(10) || ' origin_tab_name => ''SCHED_KIT_MAP'',' || chr(10) || ' self_tab_name => ''S_SCHED_KIT_MAP'',' || chr(10) || ' constraint_name => ''FK_INVKIT_SCHEDKITMAP'',' || chr(10) || ' rlm$crttime => NULL)));', 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_9');

--changeSet DEV-199:33 stripComments:false
insert into DPO_RLM_RULE (RULE_CLASS_CD, RULE_ID, RULE_TYPE, RULE_LDESC, CONDITION, EVENT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'IC_S_SCHED_KIT_MAP', 'RULE_10', 'FOREIGN_KEY', 'FOREIGN KEY FK_SCHEDSTASK_SCHEDKITMAP', '<condition>' || chr(10) || ' <and join="RM_UTL_RC_FUNCTION_PKG.VALID_FOREIGN_KEY(EO_RM_XFER_KEY, EO_RM_CONS_ELEMENTS) != 1">' || chr(10) || ' <object name="EO_RM_XFER_KEY" >' || chr(10) || '  xfer_inv_id is not null and xfer_transfer_mode is not null' || chr(10) || ' </object>' || chr(10) || ' <object name="EO_RM_CONS_ELEMENTS"> ' || chr(10) || ' ( PARENT_TAB_NAME = ''''S_SCHED_STASK'''' AND ORIGIN_TAB_NAME = ''''SCHED_KIT_MAP'''' AND SELF_TAB_NAME = ''''S_SCHED_KIT_MAP'''' AND CONSTRAINT_NAME = ''''FK_SCHEDSTASK_SCHEDKITMAP'''' )' || chr(10) || ' </object>' || chr(10) || ' </and>' || chr(10) || '</condition>', 'dbms_rlmgr.add_event(rule_class => v_rule_class,' || chr(10) || ' event_inst => sys.anydata.convertobject(rm_cons_elements(parent_tab_name => ''S_SCHED_STASK'',' || chr(10) || ' origin_tab_name => ''SCHED_KIT_MAP'',' || chr(10) || ' self_tab_name => ''S_SCHED_KIT_MAP'',' || chr(10) || ' constraint_name => ''FK_SCHEDSTASK_SCHEDKITMAP'',' || chr(10) || ' rlm$crttime => NULL)));', 0, to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-11-2009 22:52:06', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_RULE WHERE RULE_CLASS_CD = 'IC_S_SCHED_KIT_MAP' AND RULE_ID = 'RULE_10');

--changeSet DEV-199:34 stripComments:false
insert into DPO_RLM_VERSION (RULE_VERSION_ID, PARENT_RULE_VERSION_ID, RULE_VERSION_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 7000, 'MX0912', 0, to_date('26-09-2009 00:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-09-2009 20:04:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_VERSION WHERE RULE_VERSION_ID = 7100);

--changeSet DEV-199:35 stripComments:false
insert into DPO_RLM_VERSION (RULE_VERSION_ID, PARENT_RULE_VERSION_ID, RULE_VERSION_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7500, 7100, 'MX1006', 0, to_date('26-09-2009 00:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-09-2009 20:04:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_RLM_VERSION WHERE RULE_VERSION_ID = 7500);

--changeSet DEV-199:36 stripComments:false
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_1', 1, 0, to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_1' AND ADDED_BOOL = 1);

--changeSet DEV-199:37 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_2', 1, 0, to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 17:43:01', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_2' AND ADDED_BOOL = 1);

--changeSet DEV-199:38 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_3', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_3' AND ADDED_BOOL = 1);

--changeSet DEV-199:39 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_4', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_4' AND ADDED_BOOL = 1);

--changeSet DEV-199:40 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_5', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_5' AND ADDED_BOOL = 1);

--changeSet DEV-199:41 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_6', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_6' AND ADDED_BOOL = 1);

--changeSet DEV-199:42 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_7', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_7' AND ADDED_BOOL = 1);

--changeSet DEV-199:43 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_8', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_8' AND ADDED_BOOL = 1);

--changeSet DEV-199:44 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_9', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_9' AND ADDED_BOOL = 1);

--changeSet DEV-199:45 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
insert into DPO_RLM_VERSION_LOG (RULE_VERSION_ID, RLM_TABLE_NAME, RLM_ROW_CD1, RLM_ROW_CD2, ADDED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 7100, 'DPO_RLM_RULE', 'IC_S_SCHED_KIT_MAP', 'RULE_10', 1, 0, to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-11-2009 21:40:29', 'dd-mm-yyyy hh24:mi:ss'), 0, 'M0912'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM dpo_rlm_version_log WHERE RULE_VERSION_ID = 7100 AND RLM_TABLE_NAME = 'DPO_RLM_RULE' AND RLM_ROW_CD1 = 'IC_S_SCHED_KIT_MAP' AND RLM_ROW_CD2 = 'RULE_10' AND ADDED_BOOL = 1);

--changeSet DEV-199:46 stripComments:false
insert into DPO_XFER_FILE_GRP_CFG (CFG_DB_ID, TABLE_NAME, ORIG_TABLE_NAME, FILE_GROUP_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'S_SCHED_KIT_MAP', 'SCHED_KIT_MAP', 5, 0, to_date('08-07-2009', 'dd-mm-yyyy'), to_date('08-07-2009', 'dd-mm-yyyy'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_XFER_FILE_GRP_CFG WHERE CFG_DB_ID = 0 AND TABLE_NAME = 'S_SCHED_KIT_MAP' AND FILE_GROUP_ID = 5);