--liquibase formatted sql


--changeSet 0ref_warranty_eval_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WARRANTY_EVAL_STATUS"
** 0-Level
** DATE: 29-APR-08
*********************************************/
INSERT INTO REF_WARRANTY_EVAL_STATUS(WARRANTY_EVAL_STATUS_DB_ID, WARRANTY_EVAL_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'PENDING', 'PENDING', 'PENDING', 'Warranty Evaluation is pending ', 0, '23-JULY-08', '23-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_eval_status:2 stripComments:false
INSERT INTO REF_WARRANTY_EVAL_STATUS(WARRANTY_EVAL_STATUS_DB_ID, WARRANTY_EVAL_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'CLAIM', 'CLAIM', 'CLAIM', 'Warranty Evaluation has been claimed against.', 0, '23-JULY-08', '23-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_eval_status:3 stripComments:false
INSERT INTO REF_WARRANTY_EVAL_STATUS(WARRANTY_EVAL_STATUS_DB_ID, WARRANTY_EVAL_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'REJECTED', 'REJECTED', '', 'Warranty Evaluation has been refjected', 0, '23-JULY-08', '23-JULY-08', 0, 'MXI');