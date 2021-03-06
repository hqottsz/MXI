--liquibase formatted sql


--changeSet DEV-113:1 stripComments:false
insert into DPO_XFER_FILE_GRP_CFG (CFG_DB_ID, TABLE_NAME, ORIG_TABLE_NAME, FILE_GROUP_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'S_SD_FAULT_RESULT', 'SD_FAULT_RESULT', 3, 0, to_date('08-07-2009', 'dd-mm-yyyy'), to_date('08-07-2009', 'dd-mm-yyyy'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM DPO_XFER_FILE_GRP_CFG WHERE CFG_DB_ID = 0 AND TABLE_NAME = 'S_SD_FAULT_RESULT' AND FILE_GROUP_ID = 3);