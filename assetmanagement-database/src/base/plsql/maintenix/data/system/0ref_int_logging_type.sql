--liquibase formatted sql


--changeSet 0ref_int_logging_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INT_LOGGING_TYPE"
** 0-Level
** DATE: 10/13/2010 TIME: 10:07:03
*********************************************/
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('OFF', 'No Integration message logging', 'The integration message logging is disabled.', 0,0, '13-OCT-10', '13-OCT-10', 0, 'ADMIN');

--changeSet 0ref_int_logging_type:2 stripComments:false
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('HEADER', 'Partial integration message logging (header only)', 'The integration message logging will only record the messages header information.', 0,0, '13-OCT-10', '13-OCT-10', 0, 'ADMIN');

--changeSet 0ref_int_logging_type:3 stripComments:false
insert into REF_INT_LOGGING_TYPE (INT_LOGGING_TYPE_CD, DESC_SDESC, DESC_LDESC, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('FULL', 'Complete integration message logging', 'The integration message logging will record the header information and store copies of the inbound and outbound messages', 0,0, '13-OCT-10', '13-OCT-10', 0, 'ADMIN');