--liquibase formatted sql


--changeSet 0ref_control_method:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_CONTROL_METHOD"
** 0-Level
** DATE: 19-JAN-15 00:00:00
*********************************************/
INSERT INTO REF_CONTROL_METHOD (CONTROL_METHOD_DB_ID,CONTROL_METHOD_CD,DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES (0,'MANUAL','Record created manually', 0, '15-Jan-19', '15-Jan-19', 0, 'MXI');
--changeSet 0ref_control_method:2 stripComments:false
INSERT INTO REF_CONTROL_METHOD (CONTROL_METHOD_DB_ID,CONTROL_METHOD_CD,DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES (0,'BASELINE','Record created by baseline', 0, '15-Jan-19', '15-Jan-19', 0, 'MXI');
--changeSet 0ref_control_method:3 stripComments:false
INSERT INTO REF_CONTROL_METHOD (CONTROL_METHOD_DB_ID,CONTROL_METHOD_CD,DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES (0,'EXTERNAL','Record created by external system', 0, '15-Jan-19', '15-Jan-19', 0, 'MXI');