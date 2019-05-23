--liquibase formatted sql


--changeSet 0ref_task_advisory_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_ADVISORY_TYPE"
** 0-Level
** DATE: 21-JUL-09 TIME: 00:00:00
*********************************************/
insert into ref_task_advisory_type (task_advisory_type_db_id, task_advisory_type_cd, desc_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'EXECUTE', 'Per Execution', 'This type is used to send an alert per execution of the task.', 'EXECUTE',  0, '21-JUL-09', '21-JUL-09', 0, 'MXI');

--changeSet 0ref_task_advisory_type:2 stripComments:false
insert into ref_task_advisory_type (task_advisory_type_db_id, task_advisory_type_cd, desc_sdesc, desc_ldesc, user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FLEET', 'Fleet Wide', 'The type is used to send an alert upon completion of the task across the entire fleet.', 'FLEET',  0, '21-JUL-09', '21-JUL-09', 0, 'MXI');