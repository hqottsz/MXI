--liquibase formatted sql


--changeSet 0ref_wp_sign_req_status:1 stripComments:false
/************************************************
** INSERT SCRIPT FOR TABLE "REF_WP_SIGN_REQ_STATUS"
** 0-Level
** DATE: 07-SEP-09
*************************************************/
INSERT INTO REF_WP_SIGN_REQ_STATUS( WP_SIGN_REQ_STATUS_DB_ID, WP_SIGN_REQ_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ACTV', 'Active', 'This status indicates that the signoff has not yet occurred.', 0, '07-SEP-09', '07-SEP-09', 100, 'MXI' );

--changeSet 0ref_wp_sign_req_status:2 stripComments:false
INSERT INTO REF_WP_SIGN_REQ_STATUS( WP_SIGN_REQ_STATUS_DB_ID, WP_SIGN_REQ_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'COMPLETE', 'Complete', 'This status indicates that the signoff has been completed.', 0, '07-SEP-09', '07-SEP-09', 100, 'MXI' );