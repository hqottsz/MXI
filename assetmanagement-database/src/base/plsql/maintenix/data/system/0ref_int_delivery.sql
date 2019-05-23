--liquibase formatted sql


--changeSet 0ref_int_delivery:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INT_DELIVERY"
** 0-Level
** DATE: 12/12/2006 TIME: 16:09:03
*********************************************/
insert into REF_INT_DELIVERY (DELIVERY_DB_ID, DELIVERY_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'DEFAULT', 'No restrictions on message processing', 0, '20-DEC-06', '20-DEC-06', 100, 'MXI');

--changeSet 0ref_int_delivery:2 stripComments:false
insert into REF_INT_DELIVERY (DELIVERY_DB_ID, DELIVERY_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'FIFO', 'Message must be processed serially in receipt order', 0, '20-DEC-06', '20-DEC-06', 100, 'MXI');

--changeSet 0ref_int_delivery:3 stripComments:false
insert into REF_INT_DELIVERY (DELIVERY_DB_ID, DELIVERY_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ORDERED', 'Message must be processed through the Message Ordering Processor', 0, '02-JUL-09', '02-JUL-09', 100, 'MXI' );