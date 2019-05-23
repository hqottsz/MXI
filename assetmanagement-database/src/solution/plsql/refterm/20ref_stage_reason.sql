insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values 
(20, 'TDMEL', 0, 'DELAY', 0, 100, 'MEL deferral', 'Task delayed by MEL.  Use this if NOT using a Task Class of MEL.', 'MEL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI')
;
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values 
(20, 'TDSRM', 0, 'DELAY', 0, 100, 'SRM Delay', 'Task Delayed by SRM', 'SRM',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI')
;

