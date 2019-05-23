--liquibase formatted sql


--changeSet 0ref_wf_step_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WF_STEP_STATUS"
** 0-Level
** DATE: 21-OCT-08
*********************************************/
INSERT INTO REF_WF_STEP_STATUS(WF_STEP_STATUS_DB_ID, WF_STEP_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'PENDING', 'Pending', 'Workflow Step is pending to be completed', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_step_status:2 stripComments:false
INSERT INTO REF_WF_STEP_STATUS(WF_STEP_STATUS_DB_ID, WF_STEP_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'APPROVED', 'Approved', 'Workflow Step is approved', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_step_status:3 stripComments:false
INSERT INTO REF_WF_STEP_STATUS(WF_STEP_STATUS_DB_ID, WF_STEP_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'REJECTED', 'Rejected', 'Workflow Step has been rejected', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_step_status:4 stripComments:false
INSERT INTO REF_WF_STEP_STATUS(WF_STEP_STATUS_DB_ID, WF_STEP_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'INPROGRESS', 'In Progess', 'Workflow Step is in progress', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_step_status:5 stripComments:false
INSERT INTO REF_WF_STEP_STATUS(WF_STEP_STATUS_DB_ID, WF_STEP_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'RESTART', 'Restarted', 'Workflow Step was restarted', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');