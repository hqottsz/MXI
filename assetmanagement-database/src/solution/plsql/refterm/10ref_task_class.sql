/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_CLASS"
** 10-Level
** DATE: 04-AUG-2005 TIME: 11:15:54
*********************************************/

/** BLOCK Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BLOCK', 0, 01,  'Block', 'Block of Requirements', 1, 0, 1, 'BLOCK', 0, '24-JUL-04', '24-JUL-04', 100, 'MXI');

/** REQ Classifications **/

/** JIC Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'SRVC', 0, 76,  'Servicing', 'This task indicates an operational service against equipment',0, 1, 0, 'JIC',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'OPEN', 0, 75,  'Open', 'A task to open up a panel or area for work.', 0, 1, 0, 'JIC',0, '23-SEP-05', '23-SEP-05', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLOSE', 0, 74,  'Close', 'A task to close up a panel or area after work.', 0, 1, 0, 'JIC',0, '23-SEP-05', '23-SEP-05', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'REP', 0, 83,  'Approved Repair', 'A repair task identifies that the work needed to bring component in service will require physical alteration of a component.',0, 1, 0, 'JIC', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TEST', 0, 59,  'Test/Adjustment', 'A testing task identifies that a component needs to be tested before it is identified as serviceable.', 0, 1, 0, 'JIC',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLEAN', 0, 76,  'Cleaning', 'A task to clean equipment.', 0, 1, 0, 'JIC',0, '23-SEP-05', '23-SEP-05', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'RMVL', 0, 75,  'Removal', 'A task to remove an item from an aircraft or module.', 0, 1, 0, 'JIC',0, '23-SEP-05', '23-SEP-05', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'INST', 0, 74,  'Install', 'A task to install an item onto an aircraft or module.', 0, 1, 0, 'JIC',0, '23-SEP-05', '23-SEP-05', 100, 'MXI');
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'REF', 0, 77,  'Reference Document', 'Reference Document', 1, 0, 1, 'REF', 0, 0, '03-JAN-08', '03-JAN-08', 100, 'MXI');
