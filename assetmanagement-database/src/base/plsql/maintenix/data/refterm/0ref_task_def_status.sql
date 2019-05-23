--liquibase formatted sql


--changeSet 0ref_task_def_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_DEF_STATUS"
** 0-Level
** DATE: 11-AUG-2004 TIME: 16:53:48
*********************************************/
insert into ref_task_def_status(task_def_status_db_id, task_def_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'BUILD', 0, 81, 'Task definition Under Construction', 'Under Construction State for task definition details', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_def_status:2 stripComments:false
insert into ref_task_def_status(task_def_status_db_id, task_def_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACTV', 0, 80, 'Activated State', 'Task definition is active against applicable inventory', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_def_status:3 stripComments:false
insert into ref_task_def_status(task_def_status_db_id, task_def_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REVISION', 0, 01, 'Revision', 'Task definition is under revision', 0, '11-AUG-04', '11-AUG-04', 100, 'MXI');

--changeSet 0ref_task_def_status:4 stripComments:false
insert into ref_task_def_status(task_def_status_db_id, task_def_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SUPRSEDE', 0, 01, 'Superseded', 'This version of the Task definition has been superseded by a new version', 0, '11-AUG-04', '11-AUG-04', 100, 'MXI');

--changeSet 0ref_task_def_status:5 stripComments:false
insert into ref_task_def_status(task_def_status_db_id, task_def_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OBSOLETE', 0, 01, 'Obsoleted', 'The overall task definition has been made obsolete and is no longer applicable', 0, '11-AUG-04', '11-AUG-04', 100, 'MXI');