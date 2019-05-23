--liquibase formatted sql


--changeSet 0ref_reference_request_status:1 stripComments:false
/******************************************************
** INSERT SCRIPT FOR TABLE "REF_REFERENCE_REQUEST_STATUS"
** 0-Level
** DATE: 26-JAN-2017
*******************************************************/

INSERT INTO ref_reference_request_status ( reference_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('PENDING', 'Pending', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);

INSERT INTO ref_reference_request_status ( reference_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('APPROVED', 'Approved', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);

INSERT INTO ref_reference_request_status ( reference_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('REJECTED', 'Rejected', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);

INSERT INTO ref_reference_request_status ( reference_request_status_cd, desc_ldesc, bitmap_db_id, bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('CANCELLED', 'Cancelled', 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, user);