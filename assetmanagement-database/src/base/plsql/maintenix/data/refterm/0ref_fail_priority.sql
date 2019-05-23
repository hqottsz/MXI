--liquibase formatted sql


--changeSet 0ref_fail_priority:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_PRIORITY"
** 0-Level
** DATE: 01-JAN-04 TIME: 16:56:27
*********************************************/
insert into ref_fail_priority(fail_priority_db_id, fail_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, calc_priority_qt, rgb_color_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ANALYZE', 0, 1,  'Analyze', 'Analyze the fault', '2', '-1','black', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_fail_priority:2 stripComments:false
insert into ref_fail_priority(fail_priority_db_id, fail_priority_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, priority_ord, calc_priority_qt, rgb_color_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'HI', 0, 1,  'High', 'High', '6', '15','red', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');