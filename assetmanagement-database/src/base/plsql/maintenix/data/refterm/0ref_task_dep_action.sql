--liquibase formatted sql


--changeSet 0ref_task_dep_action:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_DEP_ACTION"
** 0-Level
*********************************************/
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CRT', 'Create New', 'Create a new Task(s) if none exist.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_dep_action:2 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'COMPLIES', 'Complies', 'Complies with (explicitly)', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_dep_action:3 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)  
values (0, 'COMPLETE', 'Complete Task', 'Another task marked complete when this task is completed.', 0, '06-FEB-06', '06-FEB-06', 100, 'MXI') ;

--changeSet 0ref_task_dep_action:4 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TERMINATE', 'Terminate Task', 'Terminate Task', 0, '31-Jan-08', '31-Jan-08', 100, 'MXI');

--changeSet 0ref_task_dep_action:5 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'POSTCRT', 'Post Create', 'Create a new Task(s) if none exist.', 0, '14-OCT-08', '14-OCT-08', 100, 'MXI');

--changeSet 0ref_task_dep_action:6 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'OPPRTNSTC', 'Opportunistic Task', 'Execute if time/labour permit.', 0, '07-APR-09', '07-APR-09', 100, 'MXI');

--changeSet 0ref_task_dep_action:7 stripComments:false
insert into ref_task_dep_action(task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'REPLACES', 'Replaces', 'Replaced by a different task (No logic)', 0, TO_DATE('01-05-2018','DD-MM-YYYY'), TO_DATE('01-05-2018','DD-MM-YYYY'), 100, 'MXI');