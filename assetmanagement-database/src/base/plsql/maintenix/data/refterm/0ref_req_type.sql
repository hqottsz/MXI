--liquibase formatted sql


--changeSet 0ref_req_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REQ_TYPE"
** 0-Level
** DATE: 03-MARCH-2005 TIME: 00:00:00
*********************************************/
INSERT INTO REF_REQ_TYPE ( REQ_TYPE_DB_ID, REQ_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'TASK', 'TASK', 'This is a part request to fill a tasks part requirement', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_req_type:2 stripComments:false
INSERT INTO REF_REQ_TYPE ( REQ_TYPE_DB_ID, REQ_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ADHOC', 'ADHOC', 'This is an ad-hoc part request (created by hand)', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_req_type:3 stripComments:false
INSERT INTO REF_REQ_TYPE ( REQ_TYPE_DB_ID, REQ_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'STOCK', 'STOCK', 'This is a part request to replenish a stock low', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_req_type:4 stripComments:false
INSERT INTO REF_REQ_TYPE ( REQ_TYPE_DB_ID, REQ_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'CSNSTOCK', 'CSNSTOCK', 'This is a consignment stock request to replenish a stock low', 0, '20-SEP-05', '20-SEP-05', 0, 'MXI');

--changeSet 0ref_req_type:5 stripComments:false
insert into REF_REQ_TYPE (REQ_TYPE_DB_ID, REQ_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');