--liquibase formatted sql


--changeSet DEV-104:1 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 75 AND table_name = 'INV_LOC_SCHEDULE';

--changeSet DEV-104:2 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 76 AND table_name = 'INV_LOC_SCHEDULE_SHIFT';  

--changeSet DEV-104:3 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 77 AND table_name = 'INV_LOC_SHIFT';  

--changeSet DEV-104:4 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4075 AND table_name = 'INV_LOC_SCHEDULE';

--changeSet DEV-104:5 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4076 AND table_name = 'INV_LOC_SCHEDULE_SHIFT';  

--changeSet DEV-104:6 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4077 AND table_name = 'INV_LOC_SHIFT';  

--changeSet DEV-104:7 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 391,1,'REF_CAPACITY_PATTERN_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 391);

--changeSet DEV-104:8 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 392,1,'REF_PPC_PUBLISH_FAIL_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 392);

--changeSet DEV-104:9 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 393,1,'REF_RESULT_EVENT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 393);

--changeSet DEV-104:10 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 394,1,'USER_SHIFT_PATTERN',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 394);

--changeSet DEV-104:11 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 395,1,'USER_SHIFT_PATTERN_DAY',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 395);

--changeSet DEV-104:12 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 396,1,'USER_SHIFT_PATTERN_DAY_SHIFT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 396);

--changeSet DEV-104:13 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 397,1,'SHIFT_SHIFT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 397);

--changeSet DEV-104:14 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 398,1,'EQP_PLANNING_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 398);

--changeSet DEV-104:15 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 399,1,'EQP_PLANNING_TYPE_SKILL',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 399);

--changeSet DEV-104:16 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 400,1,'TASK_PLANNING_TYPE_SKILL',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 400);

--changeSet DEV-104:17 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 1142, 2, 'SD_FAULT_RESULT', 1, null, null, 0, to_date('09-09-2009', 'dd-mm-yyyy'), to_date('09-09-2009 17:22:45', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 1142);

--changeSet DEV-104:18 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4391,5,'REF_CAPACITY_PATTERN_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4391);

--changeSet DEV-104:19 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4392,5,'REF_PPC_PUBLISH_FAIL_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4392);

--changeSet DEV-104:20 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 4393,5,'REF_RESULT_EVENT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4393);

--changeSet DEV-104:21 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4394,5,'USER_SHIFT_PATTERN',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4394);

--changeSet DEV-104:22 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4395,5,'USER_SHIFT_PATTERN_DAY',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 4395);

--changeSet DEV-104:23 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4396,5,'USER_SHIFT_PATTERN_DAY_SHIFT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 4396);

--changeSet DEV-104:24 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4397,5,'SHIFT_SHIFT',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4397);

--changeSet DEV-104:25 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4398,5,'EQP_PLANNING_TYPE',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4398);

--changeSet DEV-104:26 stripComments:false
INSERT INTO DPO_REP_TABLE (REP_TABLE_ID, RULESET_ID, TABLE_NAME, ENABLED_BOOL, UPD_CONFLICT_RES_COLUMNS, UPD_CONFLICT_RES_TYPE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 4399,5,'EQP_PLANNING_TYPE_SKILL',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4399);

--changeSet DEV-104:27 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4400,5,'TASK_PLANNING_TYPE_SKILL',1,NULL,'OVERWRITE',0,to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),to_date('06/24/2009 22:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID =  4400);