--liquibase formatted sql


--changeSet 0ref_wf_defn_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WF_DEFN_TYPE"
** 0-Level
** DATE: 21-OCT-08
*********************************************/
INSERT INTO REF_WF_DEFN_TYPE(WF_DEFN_TYPE_DB_ID, WF_DEFN_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'TASK', 'Task', 'Task Authorization Workflow', 0, '21-OCT-08', '21-OCT-08', 0, 'MXI');