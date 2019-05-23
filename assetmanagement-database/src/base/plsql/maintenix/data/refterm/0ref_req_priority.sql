--liquibase formatted sql


--changeSet 0ref_req_priority:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REQ_PRIORITY"
** 0-Level
** DATE: 06/21/01 TIME: 11:17:07
*********************************************/
insert into ref_req_priority(req_priority_db_id, req_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user,default_bool)
values (0, 'AOG', 0, 1, 'Aircraft on Ground', 'Urgent demand since it is currently grounding the aircraft.', 10, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI',0);

--changeSet 0ref_req_priority:2 stripComments:false
insert into ref_req_priority(req_priority_db_id, req_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,priority_ord,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user,default_bool)
values (0, 'NORMAL', 0, 87, 'Normal', 'Can take more than one week; often 4-6 weeks',  100,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI',1);

--changeSet 0ref_req_priority:3 stripComments:false
insert into REF_REQ_PRIORITY (REQ_PRIORITY_DB_ID, REQ_PRIORITY_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, PRIORITY_ORD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER,default_bool)
values (0, 'BLKOUT', 'N/A', 'N/A', 0, 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI',0);