--liquibase formatted sql


--changeSet 0ref_int_log_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INT_LOG_STATUS"
** 0-Level
** DATE: 09/30/1998 TIME: 16:09:03
*********************************************/
insert into REF_INT_LOG_STATUS (STATUS_DB_ID, STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'AWAIT', 'Processing is not yet complete', 0, '20-DEC-06', '20-DEC-06', 0, 'ADMIN');

--changeSet 0ref_int_log_status:2 stripComments:false
insert into REF_INT_LOG_STATUS (STATUS_DB_ID, STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ERROR', 'Processing failed because an error occurred', 0, '20-DEC-06', '20-DEC-06', 0, 'ADMIN');

--changeSet 0ref_int_log_status:3 stripComments:false
insert into REF_INT_LOG_STATUS (STATUS_DB_ID, STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'COMPLETE', 'Processing is complete', 0, '20-DEC-06', '20-DEC-06', 0, 'ADMIN');

--changeSet 0ref_int_log_status:4 stripComments:false
insert into REF_INT_LOG_STATUS (STATUS_DB_ID, STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'NOTRUN', 'Processing has not been performed', 0, '20-DEC-06', '20-DEC-06', 0, 'ADMIN');

--changeSet 0ref_int_log_status:5 stripComments:false
insert into REF_INT_LOG_STATUS (STATUS_DB_ID, STATUS_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'SUCCESS', 'Processing is complete and no errors were encountered', 0, '3-NOV-08', '3-NOV-08', 0, 'ADMIN');