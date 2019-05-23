--liquibase formatted sql

--changeSet OPER-18311:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_table ( purge_table_cd, utl_id, rstat_cd )
   VALUES ( 'UTL_WORK_ITEM', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_group ( purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd )
   VALUES ( 'WORK_ITEM', 'Work Item Purging', 'A set of purging policies related to Work Items', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_policy ( purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd )
   VALUES ( 'WORK_ITEM_FAILED', 'WORK_ITEM', 'Failed Work Items', 'Purges any failed work items which exceed the retention period', 120, 1, 0,0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_strategy ( purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, archive_table, utl_id, rstat_cd )
   VALUES ( 'WORK_ITEM_FAILED', 'UTL_WORK_ITEM', 10, 'UTL_WORK_ITEM.ERROR_MSG IS NOT NULL AND UTL_WORK_ITEM.END_DATE IS NOT NULL AND UTL_WORK_ITEM.END_DATE <=(TRUNC(SYSDATE) - :1)', NULL, 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_table ( purge_table_cd, utl_id, rstat_cd )
   VALUES ( 'UTL_ASYNC_ACTION', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_group ( purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd )
   VALUES ( 'ASYNC_ACTION', 'Asynchronous Action Purging', 'A set of purging policies related to Asynchronous Actions', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_policy ( purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd )
   VALUES ( 'ASYNC_FAILED', 'ASYNC_ACTION', 'Failed Async Actions', 'Purges any failed asynchronous actions which exceed the retention period', 120, 1, 0,0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_strategy ( purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, archive_table, utl_id, rstat_cd )
   VALUES ( 'ASYNC_FAILED', 'UTL_ASYNC_ACTION', 10, 'UTL_ASYNC_ACTION.ACTION_STATUS_CD = ''FAILED'' AND UTL_ASYNC_ACTION.ACTION_COMPLETE_DT IS NOT NULL AND UTL_ASYNC_ACTION.ACTION_COMPLETE_DT <=(TRUNC(SYSDATE) - :1)', NULL, 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_table ( purge_table_cd, utl_id, rstat_cd )
   VALUES ( 'AUTO_RSRV_QUEUE', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_group ( purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd )
   VALUES ( 'AUTO_RESERVATION', 'Part Request Auto Reservation Purging', 'A set of purging policies related to Auto Reservation', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_policy ( purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd )
   VALUES ( 'AUTO_RSRV_FAILED', 'AUTO_RESERVATION', 'Failed Auto Reservation Attempts', 'Purges any failed auto reservation attempts which exceed the retention period', 30, 1, 0,0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_strategy ( purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, archive_table, utl_id, rstat_cd )
   VALUES ( 'AUTO_RSRV_FAILED', 'AUTO_RSRV_QUEUE', 10, 'AUTO_RSRV_QUEUE.FAILED_BOOL = 1 AND AUTO_RSRV_QUEUE.CREATION_DT <=(TRUNC(SYSDATE) - :1)', NULL, 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_table ( purge_table_cd, utl_id, rstat_cd )
   VALUES ( 'ZIP_TASK', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_table ( purge_table_cd, utl_id, rstat_cd )
   VALUES ( 'ZIP_QUEUE', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_group ( purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd )
   VALUES ( 'ZIP_QUEUE', 'Baseline sync zip item Purging', 'A set of purging policies related to Baseline sync zipping', 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_policy ( purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd )
   VALUES ( 'ZIP_TASK_ORPH', 'ZIP_QUEUE', 'Orphan zip task items', 'Purges any orphaned zip task items which exceed the retention period', 30, 1, 0,0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_policy ( purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd )
   VALUES ( 'ZIP_QUEUE_ORPH', 'ZIP_QUEUE', 'Orphan zip queue items', 'Purges any orphaned zip queue items which exceed the retention period', 30, 1, 0,0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_strategy ( purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, archive_table, utl_id, rstat_cd )
   VALUES ( 'ZIP_TASK_ORPH', 'ZIP_TASK', 10, 'ZIP_ID NOT IN (SELECT TO_NUMBER(REPLACE(DATA,'';'','''')) FROM UTL_WORK_ITEM WHERE TYPE = ''ZIP_QUEUE'') AND ZIP_TASK.CREATION_DT <=(TRUNC(SYSDATE) - :1)', NULL, 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO utl_purge_strategy ( purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, archive_table, utl_id, rstat_cd )
   VALUES ( 'ZIP_QUEUE_ORPH', 'ZIP_QUEUE', 20, 'ZIP_ID NOT IN (SELECT TO_NUMBER(REPLACE(DATA,'';'','''')) FROM UTL_WORK_ITEM WHERE TYPE = ''ZIP_QUEUE'') AND ZIP_QUEUE.CREATION_DT <=(TRUNC(SYSDATE) - :1)', NULL, 0, 0 );    
EXCEPTION
   WHEN DUP_VAL_ON_INDEX THEN
       NULL;
END;
/

--changeSet OPER-18311:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UPDATE UTL_JOB SET JOB_NAME = 'Purges temporary data records according to the policies defined in the UTL_PURGE_POLICY table' WHERE JOB_CD = 'MX_COMMON_PURGE_RECORDS';
END;
/
