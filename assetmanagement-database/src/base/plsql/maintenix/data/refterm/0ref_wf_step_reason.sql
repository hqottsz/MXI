--liquibase formatted sql


--changeSet 0ref_wf_step_reason:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WF_STEP_REASON"
** 0-Level
** DATE: 28-OCT-2008 TIME: 00:00:00
*********************************************/
INSERT INTO REF_WF_STEP_REASON (WF_STEP_REASON_DB_ID, WF_STEP_REASON_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'NONE', 'None', 'NONE', 'NO REASON', 0, '28-OCT-08', '28-OCT-08', 0, 'MXI');