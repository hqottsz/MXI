--liquibase formatted sql


--changeSet 0ref_task_subclass:1 stripComments:false
-- MPC Classifications
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_SUBCLASS"
** 0-Level
*********************************************/
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MPCOPEN', 0, 'MPC', 0, 77,  'Open Master Panel Card', 'Open Master Panel Card', 'OPEN', 0, '28-FEB-08', '28-FEB-08', 100, 'MXI');

--changeSet 0ref_task_subclass:2 stripComments:false
insert into ref_task_subclass(task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MPCCLOSE', 0, 'MPC', 0, 77,  'Close Master Panel Card', 'Close Master Panel Card', 'CLOSE', 0, '28-FEB-08', '28-FEB-08', 100, 'MXI');