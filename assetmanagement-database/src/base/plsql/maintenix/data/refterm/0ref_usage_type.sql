--liquibase formatted sql


--changeSet 0ref_usage_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_USAGE_TYPE"
** 0-Level
** DATE: 30-SEP-2010
*********************************************/
INSERT INTO ref_usage_type ( usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('MXACCRUAL', 'ACCRUAL', 'Usage Record', 'Usage Record', 100, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0ref_usage_type:2 stripComments:false
INSERT INTO ref_usage_type ( usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('MXADJUSTMENT', 'ADJUSTMENT', 'Usage Adjustment', 'Usage Adjustment', 200, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0ref_usage_type:3 stripComments:false
INSERT INTO ref_usage_type ( usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('MXCORRECTION', 'CORRECTION', 'Edit Inventory', 'Edit Inventory', 300, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0ref_usage_type:4 stripComments:false
INSERT INTO ref_usage_type ( usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id, 	bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('MXFLIGHT', 'FLIGHT', 'Flight', 'Flight', 400, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI');