--liquibase formatted sql


--changeSet 0utl_purge_strategy:1 stripComments:false
/*************** Alerts **************/
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALUNARCHIVED', 'UTL_ALERT_LOG',10, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:2 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALUNARCHIVED', 'UTL_ALERT_PARM',20, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:3 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALUNARCHIVED', 'UTL_ALERT_STATUS_LOG',30, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:4 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALUNARCHIVED', 'UTL_USER_ALERT',40, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:5 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ALUNARCHIVED', 'UTL_ALERT',50, 'alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1',0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:6 stripComments:false
/************ Integration log purging *************/
/**ERROR PURGING**/
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'INT_STEP_LOG', 10,
     'EXISTS(SELECT 1
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_step_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                        Int_Inbound_Queue_Log.Status_Cd = ''ERROR''
             )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:7 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'UTL_ALERT_LOG', 20,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''ERROR''
                          WHERE
                                utl_alert.alert_id = utl_alert_log.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:8 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'UTL_ALERT_PARM', 30,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''ERROR''
                          WHERE
                                utl_alert.alert_id = utl_alert_parm.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:9 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'UTL_ALERT_STATUS_LOG', 40,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''ERROR''
                          WHERE
                                utl_alert.alert_id = utl_alert_status_log.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:10 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'UTL_USER_ALERT', 50,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''ERROR''
                          WHERE
                                utl_alert.alert_id = utl_user_alert.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:11 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'UTL_ALERT', 60,
         'utl_alert.queue_id IS NOT NULL AND
             EXISTS
              (SELECT 1
               FROM
                   int_inbound_queue_log
               WHERE
                   int_inbound_queue_log.queue_id =  utl_alert.queue_id AND
                   Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                   Int_Inbound_Queue_Log.Status_Cd = ''ERROR''
               )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:12 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'INT_ERROR_LOG', 70,
     'EXISTS(SELECT 1
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_error_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1)
            )',
        0, 0, sysdate, sysdate, 0, 'MXI');                

--changeSet 0utl_purge_strategy:13 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_ERROR', 'INT_INBOUND_QUEUE_LOG', 80, 'Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND int_inbound_queue_log.status_cd = ''ERROR''', 0, 0, sysdate, sysdate, 0, 'MXI');       

--changeSet 0utl_purge_strategy:14 stripComments:false
/**SUCCESS LOG PURGING**/    
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS','INT_STEP_LOG', 10,
     'EXISTS(SELECT 1
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_step_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                        Int_Inbound_Queue_Log.Status_Cd = ''COMPLETE''
             )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:15 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'UTL_ALERT_LOG', 20,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''COMPLETE''
                          WHERE
                                utl_alert.alert_id = utl_alert_log.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:16 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'UTL_ALERT_PARM', 30,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''COMPLETE''
                          WHERE
                                utl_alert.alert_id = utl_alert_parm.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:17 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'UTL_ALERT_STATUS_LOG', 40,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''COMPLETE''
                          WHERE
                                utl_alert.alert_id = utl_alert_status_log.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:18 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'UTL_USER_ALERT', 50,
        'EXISTS (SELECT 1
                     FROM
                          utl_alert
                          INNER JOIN int_inbound_queue_log ON
                                int_inbound_queue_log.queue_id = utl_alert.queue_id AND
                                Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                                int_inbound_queue_log.status_cd =''COMPLETE''
                          WHERE
                                utl_alert.alert_id = utl_user_alert.alert_id
                  )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:19 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'UTL_ALERT', 60,
         'utl_alert.queue_id IS NOT NULL AND
             EXISTS
              (SELECT 1
               FROM
                   int_inbound_queue_log
               WHERE
                   int_inbound_queue_log.queue_id =  utl_alert.queue_id AND
                   Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND
                   Int_Inbound_Queue_Log.Status_Cd = ''COMPLETE''
               )',
        0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:20 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'INT_NOTIFICATION_LOG', 70,
       'EXISTS(
               SELECT 1
               FROM int_inbound_queue_log
               WHERE int_inbound_queue_log.queue_id = Int_Notification_Log.queue_id AND
                     int_inbound_queue_log.status_cd = ''COMPLETE'' AND
                     Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) -  :1)
               )',
        0, 0, sysdate, sysdate, 0, 'MXI');        

--changeSet 0utl_purge_strategy:21 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'INT_OUTBOUND_QUEUE_LOG', 80,
     'EXISTS(SELECT 1
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_outbound_queue_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1)
             )',
        0, 0, sysdate, sysdate, 0, 'MXI');                

--changeSet 0utl_purge_strategy:22 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INT_LOG_SUCCESS', 'INT_INBOUND_QUEUE_LOG', 90, 'Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND int_inbound_queue_log.status_cd = ''COMPLETE''', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:23 stripComments:false
/************ ASB Logging *************/
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_INBOUND_LOG', 10,
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:24 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_NOTIFICATION_LOG', 20,
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:25 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_OUTBOUND_LOG', 30,
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:26 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_REQUEST_LOG', 40,
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:27 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_RESPONSE_LOG',50 ,
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:28 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_EXCEPTION_LOG', 60,
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_EXCEPTION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:29 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_TRANSACTION_LOG', 70,
 'NOT EXISTS' || chr(10) || '(' || chr(10) || '   SELECT' || chr(10) || '      :1' || chr(10) || '   FROM' || chr(10) || '      vw_asb_connector_messages' || chr(10) || '   WHERE' || chr(10) || '      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id' || chr(10) || ')' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:30 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_INBOUND_LOG', 10,
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:31 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_NOTIFICATION_LOG', 20,
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:32 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_OUTBOUND_LOG', 30,
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:33 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_REQUEST_LOG', 40,
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:34 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_RESPONSE_LOG',50 ,
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:35 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_EXCEPTION_LOG', 60,
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000)', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:36 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_TRANSACTION_LOG', 70,
'NOT EXISTS' || chr(10) || '(' || chr(10) || '   SELECT' || chr(10) || '      :1' || chr(10) || '   FROM' || chr(10) || '      vw_asb_connector_messages' || chr(10) || '   WHERE' || chr(10) || '      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id' || chr(10) || ')' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:37 stripComments:false
/** ARC MESSAGE RECORDS PURGING**/
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_RESULT', 10,
       'ARC_RESULT.MSG_ID IN
        (SELECT ARC_MESSAGE.ID
         FROM ARC_MESSAGE
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:38 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_CONFIG', 20,
       'ARC_CONFIG.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:39 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_INV_DETAILS', 30,
       'ARC_INV_DETAILS.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:40 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_TASK_USAGE', 40,
       'ARC_TASK_USAGE.TASK_ID IN
        (SELECT ARC_TASK.ID
         FROM ARC_TASK
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_TASK.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:41 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_TASK', 50,
       'ARC_TASK.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:42 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_FAULT_USAGE', 60,
       'ARC_FAULT_USAGE.FAULT_ID IN
        (SELECT ARC_FAULT.ID
         FROM ARC_FAULT
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_FAULT.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:43 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_FAULT_DUE_VALUE', 70,
       'ARC_FAULT_DUE_VALUE.FAULT_ID IN
        (SELECT ARC_FAULT.ID
         FROM ARC_FAULT
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_FAULT.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:44 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_FAULT', 80,
       'ARC_FAULT.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:45 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_INV_USAGE', 90,
       'ARC_INV_USAGE.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:46 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_INV_MAP', 100,
       'ARC_INV_MAP.ASSET_ID IN
        (SELECT ARC_ASSET.ID
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:47 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_ASSET', 110,
       'ARC_ASSET.MSG_ID IN
        (SELECT ID
         FROM ARC_MESSAGE
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
      0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_strategy:48 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ARC_RECORDS', 'ARC_MESSAGE', 120,
       'ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       ',
      0, 0, sysdate, sysdate, 0, 'MXI');
	  
--changeSet 0utl_purge_strategy:49 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('WORK_ITEM_FAILED', 'UTL_WORK_ITEM', 10,
       'UTL_WORK_ITEM.ERROR_MSG IS NOT NULL AND UTL_WORK_ITEM.END_DATE IS NOT NULL AND UTL_WORK_ITEM.END_DATE <=(TRUNC(SYSDATE) - :1)',
      0, 0, sysdate, sysdate, 0, 'MXI');
	  
--changeSet 0utl_purge_strategy:50 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('AUTO_RSRV_FAILED', 'AUTO_RSRV_QUEUE', 10,
       'AUTO_RSRV_QUEUE.FAILED_BOOL = 1 AND AUTO_RSRV_QUEUE.CREATION_DT <=(TRUNC(SYSDATE) - :1)',
      0, 0, sysdate, sysdate, 0, 'MXI');
	  
--changeSet 0utl_purge_strategy:51 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ZIP_TASK_ORPH', 'ZIP_TASK', 10,
       'ZIP_ID NOT IN (SELECT TO_NUMBER(REPLACE(DATA,'';'','''')) FROM UTL_WORK_ITEM WHERE TYPE = ''ZIP_QUEUE'') AND ZIP_TASK.CREATION_DT <=(TRUNC(SYSDATE) - :1)',
      0, 0, sysdate, sysdate, 0, 'MXI');
	  
--changeSet 0utl_purge_strategy:52 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('ZIP_QUEUE_ORPH', 'ZIP_QUEUE', 20,
       'ZIP_ID NOT IN (SELECT TO_NUMBER(REPLACE(DATA,'';'','''')) FROM UTL_WORK_ITEM WHERE TYPE = ''ZIP_QUEUE'') AND ZIP_QUEUE.CREATION_DT <=(TRUNC(SYSDATE) - :1)',
      0, 0, sysdate, sysdate, 0, 'MXI');
