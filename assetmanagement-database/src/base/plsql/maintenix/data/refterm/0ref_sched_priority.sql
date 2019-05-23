--liquibase formatted sql


--changeSet 0ref_sched_priority:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SCHED_PRIORITY"
** 0-Level
** DATE: 09/30/1998 TIME: 16:10:53
*********************************************/
insert into ref_sched_priority(sched_priority_db_id, sched_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'NONE', 0, 87, 'No Priority', 'No priority', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_priority:2 stripComments:false
insert into ref_sched_priority(sched_priority_db_id, sched_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LOW', 0, 88, 'Low Priority', 'Low priority', 2, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_priority:3 stripComments:false
insert into ref_sched_priority(sched_priority_db_id, sched_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'HIGH', 0, 89, 'High Priority', 'High Priority', 3, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_priority:4 stripComments:false
insert into ref_sched_priority(sched_priority_db_id, sched_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'O/D', 0, 90, 'Overdue', 'Overdue', 4, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');