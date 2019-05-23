--liquibase formatted sql


--changeSet DEV-1103:1 stripComments:false
-- add 0 level data to DPO_REP_TABLE
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 401,1,'REF_STEP_STATUS',1,NULL,'OVERWRITE',0,to_date('06/01/2011 10:29','mm/dd/yyyy hh24:mi'),to_date('06/01/2011 10:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 401 AND RULESET_ID = 1 AND TABLE_NAME = 'REF_STEP_STATUS' );

--changeSet DEV-1103:2 stripComments:false
INSERT INTO DPO_REP_TABLE(REP_TABLE_ID,RULESET_ID,TABLE_NAME,ENABLED_BOOL,UPD_CONFLICT_RES_COLUMNS,UPD_CONFLICT_RES_TYPE,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER) 
SELECT 4401,5,'REF_STEP_STATUS',1,NULL,'OVERWRITE',0,to_date('06/01/2011 10:29','mm/dd/yyyy hh24:mi'),to_date('06/01/2011 10:29','mm/dd/yyyy hh24:mi'),0,'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_REP_TABLE WHERE REP_TABLE_ID = 4401 AND RULESET_ID = 5 AND TABLE_NAME = 'REF_STEP_STATUS' );