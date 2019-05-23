--liquibase formatted sql


--changeSet 0ref_quar_action_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_QUAR_ACTION_STATUS"
** 0-Level
** DATE: 23-JUL-2009 TIME: 13:21:00
*********************************************/
insert into ref_quar_action_status ( QUAR_ACTION_STATUS_DB_ID, QUAR_ACTION_STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'OPEN',   'Open',   0, '23-JUL-09', '23-JUL-09', 0, 'MXI');

--changeSet 0ref_quar_action_status:2 stripComments:false
insert into ref_quar_action_status ( QUAR_ACTION_STATUS_DB_ID, QUAR_ACTION_STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CLOSED', 'Closed', 0, '23-JUL-09', '23-JUL-09', 0, 'MXI');