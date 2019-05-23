--liquibase formatted sql


--changeSet 0utl_purge_policy:1 stripComments:false
/*************** Alerts **************/
INSERT INTO utl_purge_policy (purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ('ALUNARCHIVED', 'ALERTS', 'Unarchived Alerts', 'Purges any alerts that exceed the retention period and do not have a status of ARCHIVE', 30, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:2 stripComments:false
/*************** Integration **************/
INSERT INTO utl_purge_policy (purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ('INT_LOG_SUCCESS', 'INT_LOG', 'Successfully processed Integration Msg', 'Purges any successfully processed integration messages, which have not been archived, from the inbound log and the outbound log that exceed the retention period.', 30, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:3 stripComments:false
INSERT INTO utl_purge_policy (purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ('INT_LOG_ERROR', 'INT_LOG', 'Unsuccessfully Processed Integration Msg', 'Purges any unsuccessfully processed integration messages, which have not been archived, from the inbound log and error log that exceed the retention period.', 90, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:4 stripComments:false
/*************** ASB Logging **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ASB_SUCCESS', 'ASB', 'Successful ASB Transactions', 'Purges the transactions that contain no errors', 10, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:5 stripComments:false
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ASB_FAILED', 'ASB', 'Failed ASB Transactions', 'Purges the transactions that contain errors', 30, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:6 stripComments:false
/*************** ARC **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ARC_RECORDS', 'ARC', 'ARC message records', 'Purges the message records that exceed the retention period', 90, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:7 stripComments:false
/*************** Work Items **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('WORK_ITEM_FAILED', 'WORK_ITEM', 'Failed Work Items', 'Purges any failed work items which exceed the retention period', 120, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:8 stripComments:false
/*************** Auto Reservation **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('AUTO_RSRV_FAILED', 'AUTO_RESERVATION', 'Failed Auto Reservation Attempts', 'Purges any failed auto reservation attempts which exceed the retention period', 30, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:9 stripComments:false
/*************** Zip Task **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ZIP_TASK_ORPH', 'ZIP_QUEUE', 'Orphan zip task items', 'Purges any orphaned zip task items which exceed the retention period', 30, 1, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0utl_purge_policy:10 stripComments:false
/*************** Zip Queue **************/
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ZIP_QUEUE_ORPH', 'ZIP_QUEUE', 'Orphan zip queue items', 'Purges any orphaned zip queue items which exceed the retention period', 30, 1, 0, 0, sysdate, sysdate, 0, 'MXI');