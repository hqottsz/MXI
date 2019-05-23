--liquibase formatted sql


--changeSet 0utl_purge_table:1 stripComments:false
/*************** Alerts **************/
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_ALERT', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:2 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_ALERT_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:3 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_ALERT_PARM', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:4 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_ALERT_STATUS_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:5 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_USER_ALERT', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:6 stripComments:false
/*************** Integration **************/
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_INBOUND_QUEUE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:7 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_OUTBOUND_QUEUE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:8 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_ERROR_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:9 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_STEP_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:10 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_NOTIFICATION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:11 stripComments:false
/*************** ASB Logging **************/
INSERT ALL
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_INBOUND_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_EXCEPTION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_NOTIFICATION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_OUTBOUND_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_REQUEST_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_RESPONSE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_TRANSACTION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
SELECT * FROM DUAL;

--changeSet 0utl_purge_table:12 stripComments:false
/*************** ARC **************/
INSERT ALL
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_RESULT', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_CONFIG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_INV_DETAILS', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_TASK_USAGE', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_TASK', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_FAULT_USAGE', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_FAULT_DUE_VALUE', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_FAULT', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_INV_USAGE', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_INV_MAP', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_ASSET', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ARC_MESSAGE', 0, 0, sysdate, sysdate, 0, 'MXI')
SELECT * FROM DUAL;

--changeSet 0utl_purge_table:13 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('UTL_WORK_ITEM', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:15 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('AUTO_RSRV_QUEUE', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:16 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ZIP_TASK', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_table:17 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ZIP_QUEUE', 0, 0, sysdate, sysdate, 0, 'MXI');