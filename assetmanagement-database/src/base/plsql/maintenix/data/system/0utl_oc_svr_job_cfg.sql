--liquibase formatted sql


--changeSet 0utl_oc_svr_job_cfg:1 stripComments:false
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE UTL_OC_SVR_JOB_CFG
**************************************************/
INSERT INTO UTL_OC_SVR_JOB_CFG ( CONFIG_NAME, CONFIG_VALUE, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('COLLECTION_START_DATE', to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'), 0, 0, '08-DEC-09', '08-DEC-09', 0, 'MXI');