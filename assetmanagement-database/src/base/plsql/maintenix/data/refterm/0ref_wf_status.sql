--liquibase formatted sql


--changeSet 0ref_wf_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WF_STATUS"
** 0-Level
** DATE: 21-OCT-08
*********************************************/
INSERT INTO REF_WF_STATUS(WF_STATUS_DB_ID, WF_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'COMPLETE', 'Complete', 'Workflow Completed', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_status:2 stripComments:false
INSERT INTO REF_WF_STATUS(WF_STATUS_DB_ID, WF_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'RESTART', 'Restarted', 'Workflow Restarted', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_status:3 stripComments:false
INSERT INTO REF_WF_STATUS(WF_STATUS_DB_ID, WF_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'REJECT', 'Rejected', 'Workflow Rejected', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');

--changeSet 0ref_wf_status:4 stripComments:false
INSERT INTO REF_WF_STATUS(WF_STATUS_DB_ID, WF_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'INPROGRESS', 'In Progress', 'Workflow In Progress', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');