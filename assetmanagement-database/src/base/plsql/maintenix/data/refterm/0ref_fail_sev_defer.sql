--liquibase formatted sql


--changeSet 0ref_fail_sev_defer:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_SEV_DEFER"
** 0-Level
*********************************************/
insert into ref_fail_sev_defer(fail_sev_db_id, fail_sev_cd, fail_defer_db_id, fail_defer_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL', 0, 'MEL A', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev_defer:2 stripComments:false
insert into ref_fail_sev_defer(fail_sev_db_id, fail_sev_cd, fail_defer_db_id, fail_defer_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL', 0, 'MEL B', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev_defer:3 stripComments:false
insert into ref_fail_sev_defer(fail_sev_db_id, fail_sev_cd, fail_defer_db_id, fail_defer_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL', 0, 'MEL C', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_fail_sev_defer:4 stripComments:false
insert into ref_fail_sev_defer(fail_sev_db_id, fail_sev_cd, fail_defer_db_id, fail_defer_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MEL', 0, 'MEL D', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');