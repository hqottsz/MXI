--liquibase formatted sql


--changeSet 0ref_task_def_issue_by:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_DEF_ISSUE_BY"
** 0-Level
** DATE: 29-APR-08
*********************************************/
INSERT INTO REF_TASK_DEF_ISSUE_BY(TASK_DEF_ISSUE_BY_DB_ID, TASK_DEF_ISSUE_BY_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'MANUFACT', 'Manufacturer', 'This reference document was issued by a manufacturer. Typically these will be service bulletins (SBs).', 0, '18-JUNE-09', '18-JUNE-09', 0, 'MXI');

--changeSet 0ref_task_def_issue_by:2 stripComments:false
INSERT INTO REF_TASK_DEF_ISSUE_BY(TASK_DEF_ISSUE_BY_DB_ID, TASK_DEF_ISSUE_BY_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'REGBODY', 'Regulatory Body', 'This reference document was issued by a regulatory body. Typically these will be airworthiness directives (ADs).', 0, '18-JUNE-09', '18-JUNE-09', 0, 'MXI');

--changeSet 0ref_task_def_issue_by:3 stripComments:false
INSERT INTO REF_TASK_DEF_ISSUE_BY(TASK_DEF_ISSUE_BY_DB_ID, TASK_DEF_ISSUE_BY_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'OPER', 'Operator', 'This reference document was issued by an operator.', 0, '18-JUNE-09', '18-JUNE-09', 0, 'MXI');