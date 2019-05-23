--liquibase formatted sql


--changeSet 0ref_req_action:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REQ_ACTION"
** 0-Level
** DATE: June 17th, 2009
*********************************************/
INSERT INTO REF_REQ_ACTION ( REQ_ACTION_DB_ID, REQ_ACTION_CD, USER_CD, DESC_SDESC, DESC_LDESC, GENERATE_REQ_BOOL, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'REQ', 'REQ', 'REQUEST', 'Generate a part request', 1, 1, 0, '17-JUN-09', '17-JUN-09', 0, 'MXI');

--changeSet 0ref_req_action:2 stripComments:false
INSERT INTO REF_REQ_ACTION ( REQ_ACTION_DB_ID, REQ_ACTION_CD, USER_CD, DESC_SDESC, DESC_LDESC, GENERATE_REQ_BOOL, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'NOREQ', 'NOREQ', 'NO REQUEST', 'Never Generate a Part Request', 0, 0, 0, '17-JUN-09', '17-JUN-09', 0, 'MXI');

--changeSet 0ref_req_action:3 stripComments:false
INSERT INTO REF_REQ_ACTION ( REQ_ACTION_DB_ID, REQ_ACTION_CD, USER_CD, DESC_SDESC, DESC_LDESC, GENERATE_REQ_BOOL, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'INKIT', 'INKIT', 'IN A KIT', 'Retrieve part from kit.  No request is generated.', 0, 0, 0, '17-JUN-09', '17-JUN-09', 0, 'MXI');