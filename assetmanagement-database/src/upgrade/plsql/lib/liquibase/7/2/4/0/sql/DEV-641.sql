--liquibase formatted sql


--changeSet DEV-641:1 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_INBOUND_QUEUE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'INT_INBOUND_QUEUE_LOG');

--changeSet DEV-641:2 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_OUTBOUND_QUEUE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'INT_OUTBOUND_QUEUE_LOG');

--changeSet DEV-641:3 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_ERROR_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'INT_ERROR_LOG');

--changeSet DEV-641:4 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_STEP_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'INT_STEP_LOG');

--changeSet DEV-641:5 stripComments:false
INSERT INTO utl_purge_table(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_NOTIFICATION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'INT_NOTIFICATION_LOG');

--changeSet DEV-641:6 stripComments:false
INSERT INTO utl_purge_group (purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG', 'Integration Log Purging', 'A set of purging policies related to Integration Log Tables', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_group WHERE purge_group_cd = 'INT_LOG');

--changeSet DEV-641:7 stripComments:false
INSERT INTO utl_purge_policy (purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
SELECT 'INT_LOG_SUCCESS', 'INT_LOG', 'Successfully processed Integration Msg', 'Purges any successfully processed integration messages, which have not been archived, from the inbound log and the outbound log that exceed the retention period.', 30, 0, 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_policy WHERE purge_policy_cd = 'INT_LOG_SUCCESS');

--changeSet DEV-641:8 stripComments:false
INSERT INTO utl_purge_policy (purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
SELECT 'INT_LOG_ERROR', 'INT_LOG', 'Unsuccessfully Processed Integration Msg', 'Purges any unsuccessfully processed integration messages, which have not been archived, from the inbound log and error log that exceed the retention period.', 90, 0, 0, 0, sysdate, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_purge_policy WHERE purge_policy_cd = 'INT_LOG_ERROR');

--changeSet DEV-641:9 stripComments:false
/************ Integration log purging strategy *************/
DELETE utl_purge_strategy WHERE purge_policy_cd IN ('INT_LOG_ERROR','INT_LOG_SUCCESS');

--changeSet DEV-641:10 stripComments:false
/*ERROR PURGING*/
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_ERROR', 'INT_STEP_LOG', 10, 
     'EXISTS(SELECT 1 
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_step_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND 
                        Int_Inbound_Queue_Log.Status_Cd = ''ERROR'' 
             )',
        0, 0, sysdate, sysdate, 0, 'MXI'         
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'INT_STEP_LOG');        

--changeSet DEV-641:11 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT'INT_LOG_ERROR', 'UTL_ALERT_LOG', 20, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'UTL_ALERT_LOG');

--changeSet DEV-641:12 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT'INT_LOG_ERROR', 'UTL_ALERT_PARM', 30, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'UTL_ALERT_PARM');        

--changeSet DEV-641:13 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_ERROR', 'UTL_ALERT_STATUS_LOG', 40, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'UTL_ALERT_STATUS_LOG');        

--changeSet DEV-641:14 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT'INT_LOG_ERROR', 'UTL_USER_ALERT', 50, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'UTL_USER_ALERT') ;

--changeSet DEV-641:15 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_ERROR', 'UTL_ALERT', 60, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'UTL_ALERT') ;        

--changeSet DEV-641:16 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_ERROR', 'INT_ERROR_LOG', 70, 
     'EXISTS(SELECT 1 
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_error_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1)
            )',
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'INT_ERROR_LOG') ;                  

--changeSet DEV-641:17 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT'INT_LOG_ERROR', 'INT_INBOUND_QUEUE_LOG', 80, 'Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND int_inbound_queue_log.status_cd = ''ERROR''', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_ERROR' AND purge_table_Cd = 'INT_INBOUND_QUEUE_LOG') ;               

--changeSet DEV-641:18 stripComments:false
/*SUCCESS LOG PURGING*/    
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'INT_STEP_LOG', 10, 
     'EXISTS(SELECT 1 
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_step_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND 
                        Int_Inbound_Queue_Log.Status_Cd = ''COMPLETE'' 
             )',
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'INT_STEP_LOG');                

--changeSet DEV-641:19 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'UTL_ALERT_LOG', 20, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'UTL_ALERT_LOG');                

--changeSet DEV-641:20 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'UTL_ALERT_PARM', 30, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'UTL_ALERT_PARM');                

--changeSet DEV-641:21 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'UTL_ALERT_STATUS_LOG', 40, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'UTL_ALERT_STATUS_LOG');                

--changeSet DEV-641:22 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'UTL_USER_ALERT', 50, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'UTL_USER_ALERT');                        

--changeSet DEV-641:23 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'UTL_ALERT', 60, 
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
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'UTL_ALERT');                                

--changeSet DEV-641:24 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'INT_NOTIFICATION_LOG', 70,
       'EXISTS(
               SELECT 1 
               FROM int_inbound_queue_log 
               WHERE int_inbound_queue_log.queue_id = Int_Notification_Log.queue_id AND 
                     int_inbound_queue_log.status_cd = ''COMPLETE'' AND 
                     Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) -  :1)
               )',
        0, 0, sysdate, sysdate, 0, 'MXI'     
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'INT_NOTIFICATION_LOG');                                

--changeSet DEV-641:25 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'INT_OUTBOUND_QUEUE_LOG', 80, 
     'EXISTS(SELECT 1 
             FROM
                   int_inbound_queue_log
                   WHERE
                        int_inbound_queue_log.queue_id = int_outbound_queue_log.queue_id AND
                        Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1)
             )',
        0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'INT_OUTBOUND_QUEUE_LOG');                                                

--changeSet DEV-641:26 stripComments:false
INSERT INTO utl_purge_strategy(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INT_LOG_SUCCESS', 'INT_INBOUND_QUEUE_LOG', 90, 'Int_Inbound_Queue_Log.End_Dt <=(TRUNC(SYSDATE) - :1) AND int_inbound_queue_log.status_cd = ''COMPLETE''', 0, 0, sysdate, sysdate, 0, 'MXI'
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'INT_LOG_SUCCESS' AND purge_table_Cd = 'INT_INBOUND_QUEUE_LOG');        