--liquibase formatted sql


--changeSet 0ref_fail_defer:1 stripComments:false
-- Electronic Logbook
/****************************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_DEFER"
** 0-Level
*****************************************************/
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OTHER', 0, 1, 'OTHER - 365 Days', 'OTHER - 365 Days', 365, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_defer:2 stripComments:false
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CDL', 0, 1, 'CDL - 180 Days', 'Configuration Deviation List 180 Days', 180, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_defer:3 stripComments:false
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL A', 0, 75, 'High Priority MEL Deviation', 'High Priority MEL Deviation', 0, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_defer:4 stripComments:false
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL B', 0, 75, '3 day MEL deviation', '3 day MEL deviation', 3, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_defer:5 stripComments:false
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL C', 0, 75, '10 day MEL deviation', '10 day MEL deviation', 10, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_defer:6 stripComments:false
insert into ref_fail_defer (fail_defer_db_id, fail_defer_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, deadline_days_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL D', 0, 75, '120 day MEL deviation', '120 day MEL deviation', 120, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');