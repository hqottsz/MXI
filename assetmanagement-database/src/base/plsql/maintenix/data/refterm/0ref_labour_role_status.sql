--liquibase formatted sql


--changeSet 0ref_labour_role_status:1 stripComments:false
/**************************************************
** INSERT SCRIPT FOR TABLE "REF_LABOUR_ROLE_STATUS"
** 0-Level
** DATE: 31-AUG-09
***************************************************/
INSERT INTO REF_LABOUR_ROLE_STATUS( LABOUR_ROLE_STATUS_DB_ID, LABOUR_ROLE_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ACTV', 'Active', 'This status indicates that the labour role is active', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );

--changeSet 0ref_labour_role_status:2 stripComments:false
INSERT INTO REF_LABOUR_ROLE_STATUS( LABOUR_ROLE_STATUS_DB_ID, LABOUR_ROLE_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'PENDING', 'Pending', 'This status indicates that the labour role is pending.  It cannot be completed until another active labor role is completed.', 0, '08-SEP-09', '31-AUG-09', 100, 'MXI' );

--changeSet 0ref_labour_role_status:3 stripComments:false
INSERT INTO REF_LABOUR_ROLE_STATUS( LABOUR_ROLE_STATUS_DB_ID, LABOUR_ROLE_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'COMPLETE', 'Complete', 'This status indicates that the labour role is complete.', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );

--changeSet 0ref_labour_role_status:4 stripComments:false
INSERT INTO REF_LABOUR_ROLE_STATUS( LABOUR_ROLE_STATUS_DB_ID, LABOUR_ROLE_STATUS_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'CANCEL', 'Cancelled', 'This status indicates that the labor role is cancelled.', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );