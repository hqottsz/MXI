--liquibase formatted sql


--changeSet 0ref_step_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_STEP_STATUS"
** 0-Level
** DATE: 16-JUN-2011
*********************************************/
INSERT INTO REF_STEP_STATUS ( STEP_STATUS_CD, DISPLAY_NAME, HISTORIC_BOOL, ORD_ID, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('MXPENDING', 'Pending', 0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0ref_step_status:2 stripComments:false
INSERT INTO REF_STEP_STATUS ( STEP_STATUS_CD, DISPLAY_NAME, HISTORIC_BOOL, ORD_ID, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('MXPARTIAL', 'Partial', 0, 2, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0ref_step_status:3 stripComments:false
INSERT INTO REF_STEP_STATUS ( STEP_STATUS_CD, DISPLAY_NAME, HISTORIC_BOOL, ORD_ID, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('MXCOMPLETE', 'Complete', 1, 3, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0ref_step_status:4 stripComments:false
INSERT INTO REF_STEP_STATUS ( STEP_STATUS_CD, DISPLAY_NAME, HISTORIC_BOOL, ORD_ID, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('MXNA', 'N/A', 1, 4, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');