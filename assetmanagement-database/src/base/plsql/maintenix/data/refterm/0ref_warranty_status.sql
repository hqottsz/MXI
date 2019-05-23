--liquibase formatted sql


--changeSet 0ref_warranty_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WARRANTY_STATUS"
** 0-Level
** DATE: 29-APR-08
*********************************************/
INSERT INTO REF_WARRANTY_STATUS(WARRANTY_STATUS_DB_ID, WARRANTY_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'BUILD', 'BUILD', 'Build', 'Warranty Contact is in Build', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_status:2 stripComments:false
INSERT INTO REF_WARRANTY_STATUS(WARRANTY_STATUS_DB_ID, WARRANTY_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ACTV', 'ACTIVE', 'Active', 'Warranty Contract is Active', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_status:3 stripComments:false
INSERT INTO REF_WARRANTY_STATUS(WARRANTY_STATUS_DB_ID, WARRANTY_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'DEACTV', 'DEACTIVATED', 'Deactivated', 'Warranty Contract has been Deactivated', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');