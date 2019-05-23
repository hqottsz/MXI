/********************************************
** INSERT SCRIPT FOR TABLE "REF_REQ_PRIORITY"
** 10-Level
** DATE: 06/21/01 TIME: 11:17:07
*********************************************/
insert into ref_req_priority(req_priority_db_id, req_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user,default_bool) 
values (10, 'CRITICAL', 0, 89, 'Critical', 'Should be fulfilled within 24 hours', 20, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI',0);
insert into ref_req_priority(req_priority_db_id, req_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user,default_bool) 
values (10, 'DE-RATE', 0, 89, 'De-rated', 'Urgent demand since it is currently de-rating the capability of the aircraft or is a MEL impact item.', 30,0, '23-MAR-01', '23-MAR-01', 100, 'MXI',0);
insert into ref_req_priority(req_priority_db_id, req_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user,default_bool) 
values (10, 'EXPEDITE', 0, 88, 'Expedite', 'Should be fulfilled within 7 days', 40, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI',0);
