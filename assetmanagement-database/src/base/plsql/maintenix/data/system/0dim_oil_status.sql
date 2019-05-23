--liquibase formatted sql


--changeSet 0dim_oil_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "DIM_OIL_STATUS"
** DATE: Dec 11 2009
*********************************************/
INSERT into DIM_OIL_STATUS (DIM_OIL_STATUS_ID, OIL_STATUS_DB_ID, OIL_STATUS_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (1, 0, 'NORMAL', 0, '11-DEC-09', '11-DEC-09', 0, 'MXI');