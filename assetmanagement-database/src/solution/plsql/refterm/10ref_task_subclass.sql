/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_SUBCLASS"
** 10-Level
** DATE: 02/03/03 TIME: 16:56:27
*********************************************/
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'FIP', 0, 'CORR', 0, 77, 'Fault Isolation Procedure', 'Fault Isolation Procedure', 'FIP', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CLEAN', 10, 'SRVC', 0, 74, 'Cleaning', 'Cleaning', 'CLEAN', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');	
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'PAINT', 10, 'SRVC', 0, 74, 'Painting', 'Painting', 'PAINT', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'LUBE', 10, 'SRVC', 0, 74, 'Lubrication', 'Lubrication', 'LUBE', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');	
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'POL', 10, 'SRVC', 0, 74, 'Fuelling and Lubrication', 'Fuelling and Lubrication', 'POL',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');	
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'COND', 0, 'INSP', 0, 77, 'Conditional Inspection', 'Conditional Inspection', 'COND', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'NDI', 0, 'INSP', 0, 77, 'Non-Destructive Inspection', 'Non-Destructive Inspection', 'NDI', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'VISUAL', 0, 'INSP', 0, 77, 'Visual Inspection/Check', 'Visual Inspection/Check','VISUAL', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'DEBRIEF', 0, 'INSP', 0, 77, 'Pilot/Crew Debrief', ' Pilot/Crew Debrief', 'DEBRIEF', 0, '14-DEC-01', '14-DEC-01', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'AMOC', 0, 'INSP', 0, 77, 'Alternate Means of Compliance', 'Alternate Means of Compliance','AMOC', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'FUNCTION', 10, 'TEST', 0, 77, 'Functional Test', 'Functional Test', 'FUNCTION', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'OPER', 10, 'TEST', 0, 77, 'Operational Test', 'Operational Test', 'OPER', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'NDT', 10, 'TEST', 0, 77, 'Non-Destructive Inspection', 'Non-Destructive Inspection','NDT', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'RESTORE', 10, 'REP', 0, 77, 'Restoration', 'Restoration', 'RESTORE', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'SWINST',  0, 'REPL', 0, 74, 'Software Installation', 'Software Installation',  'SWINST', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'A-CHECK', 0, 'CHECK', 0, 79, 'A-Check', 'A-Check', 'A-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'B-CHECK', 0, 'CHECK', 0, 79, 'B-Check', 'B-Check', 'B-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'C-CHECK', 0, 'CHECK', 0, 79, 'C-Check', 'C-Check', 'C-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'D-CHECK', 0, 'CHECK', 0, 79, 'D-Check', 'D-Check', 'D-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'H-CHECK', 0, 'CHECK', 0, 79, 'H-Check', 'H-Check',  'H-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'S-CHECK', 0, 'CHECK', 0, 79, 'S-Check', 'S-Check', 'S-CHECK',   0, '29-OCT-02', '29-OCT-02', 100, 'MXI');	
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'W-CHECK', 0, 'CHECK', 0, 79, 'W-Check', 'W-Check',  'W-CHECK', 0, '29-OCT-02', '29-OCT-02', 100, 'MXI');	
