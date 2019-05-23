--liquibase formatted sql

--changeSet 0ref_fail_defer_ref_status:1 stripComments:false
/*****************************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_DEFER_REF_STATUS"
** 0-Level
** DATE: 11-OCT-2016
******************************************************/
INSERT INTO ref_fail_defer_ref_status ( defer_ref_status_cd, desc_ldesc, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ACTV', 'Active', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);

--changeSet 0ref_fail_defer_ref_status:2 stripComments:false
INSERT INTO ref_fail_defer_ref_status ( defer_ref_status_cd, desc_ldesc, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INACTV', 'Inactive', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);