--liquibase formatted sql


--changeSet QC-4633:1 stripComments:false
UPDATE utl_purge_strategy
  SET predicate_sql = 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)'
WHERE purge_policy_cd = 'ALUNARCHIVED'
   AND purge_table_cd = 'UTL_ALERT_LOG';   

--changeSet QC-4633:2 stripComments:false
UPDATE utl_purge_strategy
  SET predicate_sql = 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)'
WHERE purge_policy_cd = 'ALUNARCHIVED'
   AND purge_table_cd = 'UTL_ALERT_PARM';   

--changeSet QC-4633:3 stripComments:false
UPDATE utl_purge_strategy
  SET predicate_sql = 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)'
WHERE purge_policy_cd = 'ALUNARCHIVED'
   AND purge_table_cd = 'UTL_ALERT_STATUS_LOG';   

--changeSet QC-4633:4 stripComments:false
UPDATE utl_purge_strategy
  SET predicate_sql = 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1)'
WHERE purge_policy_cd = 'ALUNARCHIVED'
   AND purge_table_cd = 'UTL_USER_ALERT';   

--changeSet QC-4633:5 stripComments:false
UPDATE utl_purge_strategy
  SET predicate_sql = 'alert_status_cd <> ''ARCHIVE'' AND alert_timestamp <= TRUNC( sysdate ) - :1'
WHERE purge_policy_cd = 'ALUNARCHIVED'
   AND purge_table_cd = 'UTL_ALERT';      