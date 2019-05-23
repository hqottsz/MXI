--liquibase formatted sql


--changeSet MX-17352:1 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 164, 'core.alert.BATCH_COMPLETE_ASYNC_SUCCESS_name', 'core.alert.BATCH_COMPLETE_ASYNC_SUCCESS_description', 'PRIVATE', null, 'TASK', 'core.alert.BATCH_COMPLETE_ASYNC_SUCCESS_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 164);

--changeSet MX-17352:2 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 165, 'core.alert.BATCH_COMPLETE_ASYNC_FAILURE_name', 'core.alert.BATCH_COMPLETE_ASYNC_FAILURE_description', 'PRIVATE', null, 'TASK', 'core.alert.BATCH_COMPLETE_ASYNC_FAILURE_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 165);

--changeSet MX-17352:3 stripComments:false
INSERT INTO UTL_ASYNC_ACTION_TYPE ( ASYNC_ACTION_TYPE_CD, USER_TYPE_CD, DESC_SDESC, DESC_LDESC, REF_NAME, METHOD_NAME, UTL_ID )
   SELECT 'BATCH_COMP_ALL', 'BATCH_COMP_ALL', 'Batch Complete All Tasks for a Given Check', '', 'com.mxi.mx.core.ejb.CoreAsyncAction', 'batchCompleteAll', 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM UTL_ASYNC_ACTION_TYPE WHERE ASYNC_ACTION_TYPE_CD = 'BATCH_COMP_ALL' );      

--changeSet MX-17352:4 stripComments:false
INSERT INTO UTL_ASYNC_ACTION_TYPE ( ASYNC_ACTION_TYPE_CD, USER_TYPE_CD, DESC_SDESC, DESC_LDESC, REF_NAME, METHOD_NAME, UTL_ID )
   SELECT 'BATCH_COMP_SELECTED', 'BATCH_COMP_SELECTED', 'Batch Complete Selected Tasks for a Given Check', '', 'com.mxi.mx.core.ejb.CoreAsyncAction', 'batchCompleteSelected', 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM UTL_ASYNC_ACTION_TYPE WHERE ASYNC_ACTION_TYPE_CD = 'BATCH_COMP_SELECTED' );      