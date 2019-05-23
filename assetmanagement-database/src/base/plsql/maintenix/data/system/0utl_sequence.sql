--liquibase formatted sql


--changeSet 0utl_sequence:1 stripComments:false
/********************************************
* INSERT SCRIPT FOR TABLE "UTL_SEQUENCE"
* The default next_value is 100000.
* This is the value for Maintenix 'out of box' data.
* This value should not be modified in this script to increment a 'live' database.
* NOTE: The column name field is the ID field on the table in question.
*********************************************/
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ALERT_ID', 100000, 'UTL_ALERT', 'ALERT_ID',1 ,0);

--changeSet 0utl_sequence:2 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ALERT_LOG_ID_SEQ', 100000, 'UTL_ALERT_LOG', 'ALERT_LOG_ID',1 ,0);

--changeSet 0utl_sequence:3 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq,  utl_id )
   VALUES ( 'ERROR_ID', 100000,1, 0);

--changeSet 0utl_sequence:4 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('EQP_STOCK_NO_ID', 100000, 'EQP_STOCK_NO', 'STOCK_NO_ID',1 ,0);

--changeSet 0utl_sequence:5 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('EQP_PART_VENDR_PRI_SEQ', 100000, 'EQP_PART_VENDOR_PRICE', 'PART_VENDOR_PRICE_ID',1 ,0);

--changeSet 0utl_sequence:6 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('ORG_VENDOR_SEQ', 100000, 'ORG_VENDOR', 'VENDOR_ID',1 ,0);

--changeSet 0utl_sequence:7 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('UTL_ROLE_ID', 100000, 'UTL_ROLE', 'ROLE_ID',1 ,0);

--changeSet 0utl_sequence:8 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('SHIP_SHIPMENT_LINE_ID', 100000, 'SHIP_SHIPMENT_LINE', 'SHIPMENT_LINE_ID',1 ,0);

--changeSet 0utl_sequence:9 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('UTL_MENU_GROUP_ID', 100000, 'UTL_MENU_GROUP', 'GROUP_ID',1 ,0);

--changeSet 0utl_sequence:10 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID )
   VALUES ( 'UTL_MENU_ITEM_ID', 200000, 'UTL_MENU_ITEM', 'MENU_ID',1, 0 );

--changeSet 0utl_sequence:11 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('UTL_TODO_LIST_ID', 100000, 'UTL_TODO_LIST', 'TODO_LIST_ID',1 ,0);

--changeSet 0utl_sequence:12 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('UTL_USER_ID', 100000, 'UTL_USER', 'USER_ID',1 ,0);

--changeSet 0utl_sequence:13 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('ORG_HR_ID', 100000, 'ORG_HR', 'HR_ID',1 ,0);

--changeSet 0utl_sequence:14 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('ORG_HR_SHIFT_PLAN_ID_SEQ', 100000, 'ORG_HR_SHIFT_PLAN', 'HR_SHIFT_PLAN_ID',1 ,0);

--changeSet 0utl_sequence:15 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('INV_LOC_ID', 100000, 'INV_LOC', 'LOC_ID',1 ,0);

--changeSet 0utl_sequence:16 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('ORG_AUTHORITY_ID', 100000, 'ORG_AUTHORITY', 'AUTHORITY_ID',1 ,0);

--changeSet 0utl_sequence:17 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('ORG_WORK_DEPT_ID', 100000, 'ORG_WORK_DEPT', 'DEPT_ID',1 ,0);

--changeSet 0utl_sequence:18 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, oracle_seq,UTL_ID)
   VALUES ( 'STOCK_NO_CODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:19 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'BATCH_CONTROL_NO', 100000,1 ,0);

--changeSet 0utl_sequence:20 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'TRANSFER_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:21 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'RFQ_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:22 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'SHIPMENT_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:23 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ( 'PART_REQUEST_ID', 100000, NULL, NULL,1 ,0);

--changeSet 0utl_sequence:24 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ('XACTION_ID', 100000, 'FNC_XACTION_LOG', 'XACTION_ID',1 ,0);

--changeSet 0utl_sequence:25 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, oracle_seq, UTL_ID)
   VALUES ( 'PO_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:26 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, TABLE_NAME, COLUMN_NAME, oracle_seq, UTL_ID)
   VALUES ( 'FNC_ACCOUNT_ID', 100000, 'FNC_ACCOUNT', 'ACCOUNT_ID',1 ,0);

--changeSet 0utl_sequence:27 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'AUTO_RSRV_ID', 100000, 'AUTO_RSRV_QUEUE', 'AUTO_RSRV_ID',1 ,0);

--changeSet 0utl_sequence:28 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_PANEL_ID', 100000, 'EQP_TASK_PANEL', 'PANEL_ID',1 ,0);

--changeSet 0utl_sequence:29 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_ZONE_ID', 100000, 'EQP_TASK_ZONE', 'ZONE_ID',1 ,0);

--changeSet 0utl_sequence:30 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_DEP_ID', 100000, 'TASK_TASK_DEP', 'TASK_DEP_ID' ,1,0);

--changeSet 0utl_sequence:31 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_IETM_ID', 100000, 'TASK_TASK_IETM', 'TASK_IETM_ID' ,1,0);

--changeSet 0utl_sequence:32 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_PART_ID', 100000, 'TASK_PART_LIST', 'TASK_PART_ID' ,1,0);

--changeSet 0utl_sequence:33 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_DEFN_ID', 100000, 'TASK_DEFN', 'TASK_DEFN_ID' ,1,0);

--changeSet 0utl_sequence:34 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_ZONE_ID', 100000, 'TASK_ZONE', 'TASK_ZONE_ID' ,1,0);

--changeSet 0utl_sequence:35 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_PANEL_ID', 100000, 'TASK_PANEL', 'TASK_PANEL_ID' ,1,0);

--changeSet 0utl_sequence:36 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_ZONE_ID', 100000, 'SCHED_ZONE', 'SCHED_ZONE_ID' ,1,0);

--changeSet 0utl_sequence:37 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_PANEL_ID', 100000, 'SCHED_PANEL', 'SCHED_PANEL_ID' ,1,0);

--changeSet 0utl_sequence:38 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_TOOL_ID', 100000, 'TASK_TOOL_LIST', 'TASK_TOOL_ID' ,1,0);

--changeSet 0utl_sequence:39 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SERIAL_NO_SEQ', 1, NULL, NULL, 1 ,0);

--changeSet 0utl_sequence:40 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'VENDOR_CODE_SEQ', 1, NULL, NULL, 1 ,0);

--changeSet 0utl_sequence:41 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PART_COUNT_ID', 100000, 'INV_LOC_PART_COUNT', 'PART_COUNT_ID', 1 ,0);

--changeSet 0utl_sequence:42 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_RECOUNT_ID', 100000, 'INV_LOC_INV_RECOUNT', 'INV_RECOUNT_ID', 1 ,0);

--changeSet 0utl_sequence:43 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FNC_TCODE_ID', 100000, 'FNC_TCODE', 'TCODE_ID', 1 ,0);

--changeSet 0utl_sequence:44 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INT_MSG_ID', 100000, 'INT_INBOUND_QUEUE_LOG', NULL, 1 ,0);

--changeSet 0utl_sequence:45 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INT_STEP_ID', 100000, 'INT_STEP_LOG', 'STEP_ID', 1 ,0);

--changeSet 0utl_sequence:46 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FAIL_MODE_IETM_ID_SEQ', 100000, 'FAIL_MODE_IETM', 'FAIL_MODE_IETM_ID' ,1,0);

--changeSet 0utl_sequence:47 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ESIG_DOC_ID', 100000, 'ESIG_DOC', 'DOC_ID', 1, 0);

--changeSet 0utl_sequence:48 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ESIG_DOC_SIGN_ID', 100000, 'ESIG_DOC_SIGN', 'SIGN_ID', 1, 0);

--changeSet 0utl_sequence:49 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ASYNC_ACTION_ID', 100000, 'UTL_ASYNC_ACTION', 'ACTION_ID', 1, 0);

--changeSet 0utl_sequence:50 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FC_MODEL_SEQ', 100000, 'FC_MODEL', 'MODEL_ID', 1, 0);

--changeSet 0utl_sequence:51 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_HR_CERT_ID', 100000, 'ORG_HR_CERT', 'CERT_ID', 1, 0);

--changeSet 0utl_sequence:52 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_ACTION_ID', 100000, 'SCHED_ACTION', 'ACTION_ID', 1 ,0);

--changeSet 0utl_sequence:53 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_STEP_ID', 100000, 'TASK_STEP', 'STEP_ID', 1 ,0);

--changeSet 0utl_sequence:54 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'BLOB_ID_SEQ', 100000, 'COR_BLOB_INFO', 'BLOB_ID', 1, 0);

--changeSet 0utl_sequence:55 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PERM_BLOB_ID_SEQ', 100000, 'COR_PERM_BLOB', 'BLOB_ID', 1, 0);

--changeSet 0utl_sequence:56 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'MAINT_PRGM_DEFN_SEQ', 100000, 'MAINT_PRGM_DEFN', 'MAINT_PRGM_DEFN_ID', 1,0);

--changeSet 0utl_sequence:57 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'MAINT_PRGM_SEQ', 100000, 'MAINT_PRGM', 'MAINT_PRGM_ID', 1,0);

--changeSet 0utl_sequence:58 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, oracle_seq, UTL_ID)
   VALUES ( 'CF_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:59 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TAG_ID_SEQ', 100000, 'TAG_TAG', 'TAG_ID' ,1,0);

--changeSet 0utl_sequence:60 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_INIT_ID', 100000, 'WARRANTY_INIT', 'WARRANTY_INIT_ID',1 ,0);

--changeSet 0utl_sequence:61 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_DEFN_ID', 100000, 'WARRANTY_DEFN', 'WARRANTY_DEFN_ID',1 ,0);

--changeSet 0utl_sequence:62 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_EVAL_ID_SEQ', 100000, 'WARRANTY_EVAL', 'WARRANTY_EVAL_ID',1 ,0);

--changeSet 0utl_sequence:63 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_EVAL_QUEUE_ID_SEQ', 100000, 'WARRANTY_EVAL_QUEUE', 'EVAL_QUEUE_ID',1 ,0);

--changeSet 0utl_sequence:64 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_EVAL_LABOUR_ID', 100000, 'WARRANTY_EVAL_LABOUR', 'WARRANTY_EVAL_LABOUR_ID',1 ,0);

--changeSet 0utl_sequence:65 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_EVAL_PART_ID', 100000, 'WARRANTY_EVAL_PART', 'WARRANTY_EVAL_PART_ID',1 ,0);

--changeSet 0utl_sequence:66 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WARRANTY_EVAL_TASK_ID', 100000, 'WARRANTY_EVAL_TASK', 'WARRANTY_EVAL_TASK_ID',1 ,0);

--changeSet 0utl_sequence:67 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ZIP_QUEUE_SEQ', 100000, 'ZIP_QUEUE', 'ZIP_ID', 1 ,0);

--changeSet 0utl_sequence:68 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_DEFN_ID', 100000, 'WF_DEFN', 'WF_DEFN_ID',1 ,0);

--changeSet 0utl_sequence:69 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_DEFN_STEP_ID', 100000, 'WF_DEFN_STEP', 'WF_DEFN_STEP_ID',1 ,0);

--changeSet 0utl_sequence:70 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_LEVEL_DEFN_ID', 100000, 'WF_LEVEL_DEFN', 'WF_LEVEL_DEFN_ID',1 ,0);

--changeSet 0utl_sequence:71 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_ID', 100000, 'WF_WF', 'WF_ID',1 ,0);

--changeSet 0utl_sequence:72 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_LEVEL_ID', 100000, 'WF_LEVEL', 'WF_LEVEL_ID',1 ,0);

--changeSet 0utl_sequence:73 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'WF_STEP_ID', 100000, 'WF_STEP', 'WF_STEP_ID',1 ,0);

--changeSet 0utl_sequence:74 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PLANNING_TYPE_SEQ', 100000, 'EQP_PLANNING_TYPE', 'PLANNING_TYPE_ID', 1 ,0);

--changeSet 0utl_sequence:75 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ASSMBL_BOM_LOG_ID_SEQ', 100000, 'EQP_ASSMBL_BOM_LOG', 'ASSMBL_BOM_LOG_ID',1 ,0);

--changeSet 0utl_sequence:76 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PART_NO_LOG_ID_SEQ', 100000, 'EQP_PART_NO_LOG', 'PART_NO_LOG_ID',1 ,0);

--changeSet 0utl_sequence:77 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'BOM_PART_LOG_ID_SEQ', 100000, 'EQP_BOM_PART_LOG', 'BOM_PART_LOG_ID',1 ,0);

--changeSet 0utl_sequence:78 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_RELIABILITY_NOTE_ID_SEQ', 100000, 'INV_RELIABILITY_NOTE', 'RELIABILITY_NOTE_ID',1 ,0);

--changeSet 0utl_sequence:79 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_ADVSRY_ID_SEQ', 100000, 'EQP_ADVSRY', 'ADVSRY_ID',1 ,0);

--changeSet 0utl_sequence:80 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_ADVSRY_ATTACH_ID_SEQ', 100000, 'EQP_ADVSRY_ATTACH', 'ADVSRY_ATTACH_ID',1 ,0);

--changeSet 0utl_sequence:81 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'BITMAP_TAG_SEQ', 100000, 'REF_BITMAP', 'BITMAP_TAG' ,1,0);

--changeSet 0utl_sequence:82 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'BOM_PART_ID_SEQ', 100000, 'EQP_BOM_PART', 'BOM_PART_ID' ,1,0);

--changeSet 0utl_sequence:83 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'CALC_ID_SEQ', 100000, 'MIM_CALC', 'CALC_ID' ,1,0);

--changeSet 0utl_sequence:84 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'DATA_TYPE_ID_SEQ', 100000, 'MIM_DATA_TYPE', 'DATA_TYPE_ID' ,1,0);

--changeSet 0utl_sequence:85 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EVENT_ID_SEQ', 100000, 'EVT_EVENT', 'EVENT_ID' ,1,0);

--changeSet 0utl_sequence:86 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FAIL_EFFECT_ID_SEQ', 100000, 'FAIL_EFFECT', 'FAIL_EFFECT_ID' ,1,0);

--changeSet 0utl_sequence:87 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FAIL_MODE_ID_SEQ', 100000, 'FAIL_MODE', 'FAIL_MODE_ID' ,1,0);

--changeSet 0utl_sequence:88 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'IETM_ID_SEQ', 100000, 'IETM_IETM', 'IETM_ID' ,1,0);

--changeSet 0utl_sequence:89 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_NO_ID_SEQ', 100000, 'INV_INV', 'INV_NO_ID' ,1,0);

--changeSet 0utl_sequence:90 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'OWNER_ID_SEQ', 100000, 'INV_OWNER', 'OWNER_ID' ,1,0);

--changeSet 0utl_sequence:91 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PART_NO_ID_SEQ', 100000, 'EQP_PART_NO', 'PART_NO_ID' ,1,0);

--changeSet 0utl_sequence:92 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_ID_SEQ', 100000, 'TASK_TASK', 'TASK_ID' ,1,0);

--changeSet 0utl_sequence:93 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TRIGGER_ID_SEQ', 200000, 'UTL_TRIGGER', 'TRIGGER_ID' ,1,0);

--changeSet 0utl_sequence:94 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'IETM_TOPIC_ID_SEQ', 100000, 'IETM_TOPIC', 'IETM_TOPIC_ID' ,1,0);

--changeSet 0utl_sequence:95 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'TASK_BARCODE_SEQ', 100000, 1,0);

--changeSet 0utl_sequence:96 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'INV_BARCODE_SEQ', 100000, 1,0);

--changeSet 0utl_sequence:97 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'INT_QUEUE_ID_SEQ', 100000, 1,0);

--changeSet 0utl_sequence:98 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'TASK_QUEUE_ID_SEQ', 100000, 1,0);

--changeSet 0utl_sequence:99 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FILTER_ID_SEQ', 100000, 'UTL_PB_FILTER', 'FILTER_ID', 1,0);

--changeSet 0utl_sequence:100 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_STEP_ID', 100000, 'SCHED_STEP', 'STEP_ID', 1, 0);

--changeSet 0utl_sequence:101 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EVT_STAGE_ID_SEQ', 100000, 'EVT_STAGE', 'STAGE_ID',1 ,0);

--changeSet 0utl_sequence:102 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'STATUS_BOARD_ID_SEQ', 100000, 'SB_STATUS_BOARD', 'STATUS_BOARD_ID', 1,0);

--changeSet 0utl_sequence:103 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'KIT_PART_GROUP_ID_SEQ', 100000, 'EQP_KIT_PART_GROUPS', 'EQP_KIT_PART_GROUP_ID', 1,0);

--changeSet 0utl_sequence:104 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PO_LINE_KIT_LINE_ID_SEQ', 100000, 'PO_LINE_KIT_LINE', 'PO_LINE_KIT_LINE_ID', 1,0);

--changeSet 0utl_sequence:105 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INSTALL_KIT_MAP_ID_SEQ', 100000, 'EQP_INSTALL_KIT_MAP', 'EQP_INSTALL_KIT_MAP_ID', 1,0);

--changeSet 0utl_sequence:106 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TODO_TAB_ID_SEQ', 100000, 'UTL_TODO_TAB', 'TODO_TAB_ID', 1,0);

--changeSet 0utl_sequence:107 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'SCHED_COST_LINE_ITEM_ID' , 1 , 'SCHED_COST_LINE_ITEM' , 'COST_LINE_ITEM_ID' , 1 ,0);

--changeSet 0utl_sequence:108 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EQP_FINDING_ID' , 1 , 'EQP_FINDING' , 'FINDING_ID' , 1 ,0);

--changeSet 0utl_sequence:109 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EVT_FINDING_ID' , 1 , 'EVT_FINDING' , 'FINDING_ID' , 1 ,0);

--changeSet 0utl_sequence:110 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EVT_BAND_GROUP_ID' , 1 , 'EVT_BAND_GROUP' , 'BAND_GROUP_ID' , 1 ,0);

--changeSet 0utl_sequence:111 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EVT_BAND_ID' , 1 , 'EVT_BAND' , 'BAND_ID' , 1 ,0);

--changeSet 0utl_sequence:112 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EVT_BAND_FIELD_ID' , 1 , 'EVT_BAND_FIELD' , 'FIELD_ID' , 1 ,0);

--changeSet 0utl_sequence:113 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ('ORG_CARRIER_SEQ', 100000, 'ORG_CARRIER', 'CARRIER_ID',1 ,0);

--changeSet 0utl_sequence:114 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LIC_DEFN_ID' , 1 , 'LIC_DEFN' , 'LIC_ID' , 1 ,0);

--changeSet 0utl_sequence:115 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'GRP_DEFN_LIC_ID' , 1 , 'GRP_DEFN_LIC' , 'GRP_DEFN_LIC_ID' , 1 ,0);

--changeSet 0utl_sequence:116 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LIC_DEFN_PREREQ_ID' , 1 , 'LIC_DEFN_PREREQ' , 'LIC_PREREQ_ID' , 1 ,0);

--changeSet 0utl_sequence:117 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'TASK_LIC_ID' , 1 , 'TASK_LIC' , 'TASK_LIC_ID' , 1 ,0);

--changeSet 0utl_sequence:118 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'ORG_HR_LIC_ID' , 1 , 'ORG_HR_LIC' , 'HR_LIC_ID' , 1 ,0);

--changeSet 0utl_sequence:119 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'ORG_HR_ATTACHMENT_ID' , 1 , 'ORG_HR_ATTACHMENT' , 'ORG_HR_ATTACHMENT_ID' , 1 ,0);

--changeSet 0utl_sequence:120 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'GRP_DEFINITION_ID' , 1 , 'GRP_DEFN' , 'GRP_DEFN_ID' , 1 ,0);

--changeSet 0utl_sequence:121 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_PLAN_ID' , 1 , 'LRP_PLAN' , 'LRP_ID' , 1 ,0);

--changeSet 0utl_sequence:122 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'FAULT_THRESHOLD_ID_SEQ', 100000, 'EQP_ASSMBL_BOM_THRESHOLD', 'FAULT_THRESHOLD_ID', 1 ,0);

--changeSet 0utl_sequence:123 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'FAIL_DEFER_REF_ID_SEQ', 100000, 'FAIL_DEFER_REF', 'FAIL_DEFER_REF_ID', 1 ,0);

--changeSet 0utl_sequence:124 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_ID', 100000, 'ORG_ORG', 'ORG_ID',1 ,0);

--changeSet 0utl_sequence:125 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_INV_INV_ID' , 1 , 'LRP_INV_INV' , 'LRP_INV_INV_ID' , 1 ,0);

--changeSet 0utl_sequence:126 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_EVENT_ID' , 1 , 'LRP_EVENT' , 'LRP_EVENT_ID' , 1 ,0);

--changeSet 0utl_sequence:127 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_WORKSCOPE_ID' , 1 , 'LRP_EVENT_WORKSCOPE' , 'LRP_WORKSCOPE_ID' , 1 ,0);

--changeSet 0utl_sequence:128 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'NR_EST_ID' , 1 , 'DEFN_NR_EST' , 'NR_EST_ID' , 1 ,0);

--changeSet 0utl_sequence:129 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'SEGMENT_ID' , 1 , 'SHIP_SEGMENT' , 'SEGMENT_ID' , 1 ,0);

--changeSet 0utl_sequence:130 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'CLAIM_PART_LINE_SEQ', 100000, 'CLAIM_PART_LINE', 'CLAIM_PART_LINE_ID' ,1,0);

--changeSet 0utl_sequence:131 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'CLAIM_LABOUR_LINE_SEQ', 100000, 'CLAIM_LABOUR_LINE', 'CLAIM_LABOUR_LINE_ID' ,1,0);

--changeSet 0utl_sequence:132 stripComments:false
INSERT INTO UTL_SEQUENCE (SEQUENCE_CD, NEXT_VALUE, oracle_seq, UTL_ID)
   VALUES ( 'CL_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:133 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'EQP_ASSMBL_SUBTYPE_ID_SEQ', 1, 'EQP_ASSMBL_SUBTYPE', 'ASSMBL_SUBTYPE_ID', 1 , 0);

--changeSet 0utl_sequence:134 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'ER_HEADER_ID_SEQ', 1, 'ER_HEADER', 'RULE_ID', 1 , 0);

--changeSet 0utl_sequence:135 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_ME_RULE_INTERVAL_SEQ', 100000, 'TASK_ME_RULE_INTERVAL', 'ME_ID' ,1,0);

--changeSet 0utl_sequence:136 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'FLIGHT_DISRUPTION_BARCODE_SEQ', 100000, 1, 0);

--changeSet 0utl_sequence:137 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_TASK_LOG_SEQ', 100000, 'TASK_TASK_LOG', 'TASK_LOG_ID', 1, 0);

--changeSet 0utl_sequence:138 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'QUAR_QUAR_ID_SEQ', 100000, 'QUAR_QUAR', 'QUAR_ID' , 1, 0);

--changeSet 0utl_sequence:139 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'QUAR_BARCODE_SEQ', 100000, 1, 0);

--changeSet 0utl_sequence:140 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'QUAR_ACTION_ID_SEQ', 100000, 'QUAR_ACTION', 'QUAR_ACTION_ID' ,1,0);

--changeSet 0utl_sequence:141 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'QUAR_ACTION_BARCODE_SEQ', 100000, 1, 0);

--changeSet 0utl_sequence:142 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'QUAR_ACTION_ASSIGNMENT_ID_SEQ', 100000, 'QUAR_ACTION_ASSIGNMENT', 'QUAR_ACTION_ASSIGNMENT_ID' ,1,0);

--changeSet 0utl_sequence:143 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'DPO_EXPORT_LOG_ID_SEQ', 1, 'DPO_EXPORT_LOG', 'EXPORT_LOG_ID' ,1,0);

--changeSet 0utl_sequence:144 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'DPO_IMPORT_LOG_ID_SEQ', 1, 'DPO_IMPORT_LOG', 'IMPORT_LOG_ID' ,1,0);

--changeSet 0utl_sequence:145 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'XFER_EXPORT_FILE_ID_SEQ', 1, 'DPO_XFER_EXPORT_FILE', 'XFER_EXPORT_FILE_ID' ,1,0);

--changeSet 0utl_sequence:146 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'XFER_EXPORT_INV_ID_SEQ', 1, 'DPO_XFER_EXPORT_INV', 'XFER_EXPORT_INV_ID',1,0);

--changeSet 0utl_sequence:147 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'XFER_IMPORT_FILE_ID_SEQ', 1, 'DPO_XFER_IMPORT_FILE', 'XFER_IMPORT_FILE_ID' ,1,0);

--changeSet 0utl_sequence:148 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'XFER_IMPORT_INV_ID_SEQ', 1, 'DPO_XFER_IMPORT_INV', 'XFER_IMPORT_INV_ID',1,0);

--changeSet 0utl_sequence:149 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
         VALUES ( 'DPO_XFER_RLM_ERROR_ID_SEQ', 1, 'DPO_XFER_RLM_ERROR', 'XFER_RLM_ERROR_ID',1,0);

--changeSet 0utl_sequence:150 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'DPO_XFER_BARCODE_NUM', 100000,1 ,0);

--changeSet 0utl_sequence:151 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_LABOUR_SEQ', 100000, 'SCHED_LABOUR', 'LABOUR_ID', 1, 0);

--changeSet 0utl_sequence:152 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_LABOUR_ROLE_SEQ', 100000, 'SCHED_LABOUR_ROLE', 'LABOUR_ROLE_ID', 1, 0);

--changeSet 0utl_sequence:153 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_LABOUR_ROLE_STATUS_SEQ', 100000, 'SCHED_LABOUR_ROLE_STATUS', 'STATUS_ID', 1, 0);

--changeSet 0utl_sequence:154 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_OIL_THR_ASSMBL_ID_SEQ', 100000, 'EQP_OIL_THRESHOLD_ASSMBL', 'THRESHOLD_ID' , 1, 0);

--changeSet 0utl_sequence:155 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_OIL_THR_PART_ID_SEQ', 100000, 'EQP_OIL_THRESHOLD_PART', 'THRESHOLD_ID' , 1, 0);

--changeSet 0utl_sequence:156 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_OIL_THR_CARRIER_ID_SEQ', 100000, 'EQP_OIL_THRESHOLD_CARRIER', 'THRESHOLD_ID' , 1, 0);

--changeSet 0utl_sequence:157 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EQP_OIL_THR_INV_ID_SEQ', 100000, 'EQP_OIL_THRESHOLD_INV', 'THRESHOLD_ID' , 1, 0);

--changeSet 0utl_sequence:158 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_OIL_STATUS_LOG_ID_SEQ', 100000, 'INV_OIL_STATUS_LOG', 'OIL_STATUS_LOG_ID' , 1, 0);

--changeSet 0utl_sequence:159 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_WP_SIGN_REQ_SEQ', 100000, 'SCHED_WP_SIGN_REQ', 'SIGN_REQ_ID', 1, 0);

--changeSet 0utl_sequence:160 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'DIM_INV_ID_SEQ', 1, 'DIM_INV', 'DIM_INV_ID' , 1, 0);

--changeSet 0utl_sequence:161 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'DIM_OIL_STATUS_ID_SEQ', 100000, 'DIM_OIL_STATUS', 'DIM_OIL_STATUS_ID' , 1, 0);

--changeSet 0utl_sequence:162 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FCT_INV_OIL_ID_SEQ', 1, 'FCT_INV_OIL', 'FCT_INV_OIL_ID' , 1, 0);

--changeSet 0utl_sequence:163 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SHIFT_ID_SEQ', 1, 'SHIFT_SHIFT', 'SHIFT_ID' , 1, 0);

--changeSet 0utl_sequence:164 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'USER_SHIFT_PATTERN_ID_SEQ', 1, 'USER_SHIFT_PATTERN', 'USER_SHIFT_PATTERN_ID' , 1, 0);

--changeSet 0utl_sequence:165 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'CAPACITY_PATTERN_ID_SEQ', 1, 'CAPACITY_PATTERN', 'CAPACITY_PATTERN_ID' , 1, 0);

--changeSet 0utl_sequence:166 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_CONTACT_ID_SEQ', 100000, 'ORG_CONTACT', 'CONTACT_ID' , 1, 0);

--changeSet 0utl_sequence:167 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_ADDRESS_ID_SEQ', 100000, 'ORG_ADDRESS', 'ADDRESS_ID' , 1, 0);

--changeSet 0utl_sequence:168 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'UTL_WORK_ITEM_ID', 1, 'UTL_WORK_ITEM', 'ID' , 1, 0);

--changeSet 0utl_sequence:169 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'CAPACITY_STD_ID_SEQ' , 1 , 'LRP_LOC_CAP_STD' , 'CAPACITY_STD_ID' , 1 ,0);

--changeSet 0utl_sequence:170 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'CAPACITY_EXCEPT_ID_SEQ' , 1 , 'LRP_LOC_CAP_EXCEPT' , 'CAPACITY_EXCEPT_ID' , 1 ,0);

--changeSet 0utl_sequence:171 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_PLAN_TYPE_ID_SEQ' , 1 , 'LRP_PLAN_TYPE' , 'LRP_PLAN_TYPE_ID' , 1 ,0);

--changeSet 0utl_sequence:172 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'MAINT_PRGM_LOG_SEQ', 100000, 'MAINT_PRGM_LOG', 'MAINT_LOG_ID', 1, 0);

--changeSet 0utl_sequence:173 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   VALUES ( 'LRP_PLAN_RANGE_ID_SEQ' , 1 , 'LRP_TASK_PLAN_RANGE' , 'TASK_PLAN_RANGE_ID' , 1 ,0);

--changeSet 0utl_sequence:174 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_EXT_PART_ID_SEQ', 100000, 'SCHED_EXT_PART', 'SCHED_EXT_PART_ID' , 1, 0);

--changeSet 0utl_sequence:175 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TAX_LOG_SEQ', 100000, 'TAX_LOG', 'TAX_LOG_ORDER', 1, 0);

--changeSet 0utl_sequence:176 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'CHARGE_LOG_SEQ', 100000, 'CHARGE_LOG', 'CHARGE_LOG_ORDER', 1, 0);

--changeSet 0utl_sequence:177 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'LPA_RUN_ID_SEQ', 100000, 'LPA_RUN', 'RUN_ID' , 1, 0);

--changeSet 0utl_sequence:178 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'TASK_WEIGHT_BALANCE_ID_SEQ', 100000, 'TASK_WEIGHT_BALANCE', 'TASK_WEIGHT_BALANCE_ID' , 1, 0);

--changeSet 0utl_sequence:179 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ACFT_GROUP_ID_SEQ', 100000, 'ACFT_GROUP', 'ID' , 1, 0);

--changeSet 0utl_sequence:180 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SCHED_STEP_APPL_LOG_ID_SEQ', 1, 'SCHED_STEP_APPL_LOG', 'LOG_ID' , 1, 0);

 --changeSet 0utl_OPER-11936:1 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'DEF_AUTH_CD_SEQ', 10000, 1, 0);

--changeSet 0utl_sequence:181 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_CREW_SCHEDULE_ID_SEQ', 1, 'ORG_CREW_SCHEDULE', 'CREW_SCHEDULE_ID' , 1, 0);

--changeSet 0utl_sequence:182 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'ORG_CREW_SHIFT_PLAN_ID_SEQ', 1, 'ORG_CREW_SHIFT_PLAN', 'CREW_SHIFT_PLAN_ID' , 1, 0);

--changeSet 0utl_sequence:183 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_ASSOCIATION_ID_SEQ', 1, 'INV_ASSOCIATION', 'ASSOCIATION_ID' , 1, 0);

--changeSet 0utl_sequence:184 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'INV_DAMAGE_ID_SEQ', 1, 'INV_DAMAGE', 'DAMAGE_ID' , 1, 0);

--changeSet 0utl_sequence:185 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'UTL_QUICKTEXT_ID_SEQ', 1, 'UTL_QUICKTEXT', 'QUICKTEXT_ID' , 1, 0 );
      
--changeSet 0utl_sequence:186 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'STOCK_DIST_REQ_ID_SEQ', 1, 'STOCK_DIST_REQ', 'STOCK_DIST_REQ_ID' , 1, 0);

--changeSet 0utl_sequence:187 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SD_FAULT_REFERENCE_SEQ', 1, 'SD_FAULT_REFERENCE', 'FAULT_REF_ID' , 1, 0);
   
--changeSet 0utl_sequence:188 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'SD_FAULT_REFERENCE_REQUEST_SEQ', 1, 'SD_FAULT_REFERENCE_REQUEST', 'FAULT_REF_REQ_ID' , 1, 0);
   
--changeSet 0utl_sequence:189 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'STOCK_DIST_REQ_LOG_ID_SEQ', 1, 'STOCK_DIST_REQ_LOG', 'STOCK_DIST_REQ_LOG_ID',1 ,0);
   
--changeSet 0utl_sequence:190 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
   VALUES ( 'STOCK_DISTREQ_BARCODE_SEQ', 100000, 1, 0);
  
--changeSet 0utl_sequence:191 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PRINT_DOC_ID_SEQ', 1, 'COR_BLOB_PRINT', 'DOC_ID',1 ,0);
  
--changeSet 0utl_sequence:192 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'PRINT_JOB_ID_SEQ', 1, 'UTL_WORK_ITEM', 'KEY',1 ,0);
  
--changeSet 0utl_sequence:193 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'AXON_DOMAIN_EVENT_ENTRY_SEQ', 1, 'AXON_DOMAIN_EVENT_ENTRY', 'globalIndex' , 1, 0);
   
--changeSet 0utl_sequence:194 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'AXON_ASSOC_VALUE_ENTRY_SEQ', 1, 'AXON_ASSOC_VALUE_ENTRY', 'id' , 1, 0);
   
--changeSet 0utl_sequence:195 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'EXT_REF_ITEM_ID_SEQ', 1, 'EXT_REF_ITEM', 'EXT_REF_ITEM_ID' , 1, 0);
   
--changeSet 0utl_sequence:196 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
   VALUES ( 'FILE_IMPORT_ID_SEQ', 1, 'UTL_FILE_IMPORT', 'FILE_IMPORT_ID' , 1, 0);
   
--changeSet 0utl_sequence:197 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
      VALUES ( 'BATCH_COMPLETE_TASKS_ID_SEQ', 1, 'UTL_WORK_ITEM', 'KEY',1 ,0);

--changeSet 0utl_sequence:198 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
      VALUES ( 'INV_LOC_BIN_LOG_ID_SEQ', 1, 'INV_LOC_BIN_LOG', 'INV_LOC_BIN_LOG_ID',1 ,0);
	  
--changeSet 0utl_sequence:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create Sequences in Oracle
DECLARE
   CURSOR lcur_Sequence IS
      SELECT
         sequence_cd,
         next_value
      FROM
         utl_sequence
      WHERE
         oracle_seq=1;
   lrec_Sequence lcur_Sequence%ROWTYPE;
BEGIN
   FOR lrec_Sequence IN lcur_Sequence LOOP
      EXECUTE IMMEDIATE ( 'CREATE SEQUENCE '|| lrec_Sequence.sequence_cd ||' INCREMENT BY 1 START WITH '|| lrec_Sequence.next_value ||' MINVALUE 1 MAXVALUE 4294967295 CACHE 5 NOCYCLE NOORDER' );
   END LOOP;
END;
/