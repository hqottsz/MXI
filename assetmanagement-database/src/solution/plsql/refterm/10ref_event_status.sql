/********************************************
** INSERT SCRIPT FOR TABLE "REF_EVENT_STATUS"
** 10-Level
** DATE: 20-MAY-05 TIME: 16:24:40
*********************************************/

--Task
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'QA', 0, 'TS', 0, 83, 'Certified', 'Task has been checked by QA department', 'QA', '60', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--Change Aircraft Operating Status
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10,'COMEL', 0, 'CO', 0, 154, 'MEL No Restriction', 'Equipment under MEL but not restricted', 'MEL', '70', '0',  0, '13-NOV-01', '13-NOV-01', 100, 'MXI');

--Change Aircraft Capability
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10,'CCNORM', 0, 'CC', 0, 71, 'Normal', 'Normal', 'NORM', '10', '0',  0, '06-MAR-02', '06-MAR-02', 100, 'MXI');
