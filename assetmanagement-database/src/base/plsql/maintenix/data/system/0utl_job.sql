--liquibase formatted sql


--changeSet 0utl_job:1 stripComments:false
/************************************
** 0-Level INSERT SCRIPT FOR UTL_JOB
*************************************/
/*************** Common **************/
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_ALERT_NOTIFY', 'Alert notifier job', null, 120, 10, 1, 0);

--changeSet 0utl_job:2 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_CACHE_RESET', 'Cache reset job', null, 140, 120, 1, 0);

--changeSet 0utl_job:3 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_EXPIRED_WORK_ITEM_CLEANER', 'Removes expired entries from the UTL_WORK_ITEM table, as defined by the COMPLETED_WORK_ITEM_TTL config parm.', null, 30, 3600, 1, 0);

--changeSet 0utl_job:4 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_BLOB_SEGREGATE', 'BLOB Segregation job', null, 120, 604800, 0, 0);

--changeSet 0utl_job:5 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_BLOB_TABLE_CLEANING', 'Truncate the segregated BLOB tables', null, 120, 2419200, 0, 0);

--changeSet 0utl_job:6 stripComments:false
-- Core Logic Jobs
/*************** Core **************/
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_DB_ANALYZE', 'Gather schema stats.', '0:01', null, 86400, 1, 0);

--changeSet 0utl_job:7 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_DELETE_ORPHANED_FORECASTED_TASKS', 'Delete all orphaned forecasted tasks.', '1:10', null, 86400, 1, 0);

--changeSet 0utl_job:9 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_EXPIREDINVENTORY', 'Condemns all loose inventory that have expired.', '0:20', null, 86400, 1, 0);

--changeSet 0utl_job:10 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_GENERATE_PART_REQUESTS', 'Generate Part Requests Job Bean', null, 230, 3600, 1, 0);

--changeSet 0utl_job:11 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_GENERATEFORECASTEDTASKS', 'Generate the forecasted tasks within the task_task.forecast_range_dt for each task event', '1:20', null, 86400, 1, 0);

--changeSet 0utl_job:12 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_GENERATEISSUETRANSFERS', 'Automatically generates issue transfer records for upcoming issues within specified period of time (configurable setting).', null, 250, 3600, 1, 0);

--changeSet 0utl_job:13 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_PROCESS_AUTO_RSRV_CONTROLLER', 'Process autoreservations controller', null, 240, 15, 1, 0);

--changeSet 0utl_job:14 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_UPDATETASKDEADLINES', 'Update the Task Deadlines', '1:30', null, 86400, 1, 0);

--changeSet 0utl_job:15 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_UPDATEVENDORSTATUS', 'Set approved vendors with expired approval expiry dates to unapproved', '0:30', null, 86400, 1, 0);

--changeSet 0utl_job:16 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_ABCRECALCULATION', 'ABC Class Recalculation', '1:50', null, 2592000, 0, 0);

--changeSet 0utl_job:17 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_DATA_CLEAN_UP', 'Cleans up bad data on start up', null, 90, null, 1, 0);

--changeSet 0utl_job:18 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_UPDATETASKLABOURSUMMARY', 'Update the task labour summary table.', '1:40', null, 2592000, 1, 0);

--changeSet 0utl_job:19 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_RFQ_RESPOND_BY_DATE', 'RFQ Respond By Date Alert', '1:00', null, 86400, 1, 0);

--changeSet 0utl_job:20 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_STATUS_BOARD_REFRESH_ASSET_LIST', 'Update the asset list for the Configurable Status Boards', null, 70, 86400, 0, 0);

--changeSet 0utl_job:21 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_STATUS_BOARD_REFRESH_QUERY_DATA', 'Update the query data for the Configurable Status Boards', null, 80, 5, 0, 0);

--changeSet 0utl_job:22 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_BASELINE_SYNC', 'Run the baseline synchronization', null, 150, 1800, 1, 0);

--changeSet 0utl_job:23 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_MVIEW_TASK_MATERIAL_REQUEST_STATUS', 'Refresh the Task Material Request Status MView', null, 280, 21600, 1, 0);

--changeSet 0utl_job:24 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_MVIEW_OPEN_PART_REQ', 'Refresh the Open Part Request MView', null, 260, 30, 1, 0);

--changeSet 0utl_job:25 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_MVIEW_DEFER_FAULT_PART_REQ', 'Refresh the Deferred Fault Part Request MView', null, 270, 30, 1, 0);

--changeSet 0utl_job:26 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_MVIEW_FLEET_LIST', 'Refresh the Fleet List MView', null, 200, 300, 1, 0);

--changeSet 0utl_job:27 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_DEACTIVATE_WARRANTY_CONTRACT', 'De-Activate Warranty Contracts', '0:40', null, 86400, 0, 0);

--changeSet 0utl_job:28 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_CONVERT_TO_COMPONENT', 'Converts all off wing tasks on installed inventory to component replacements.', null, 100, null, 0, 0);

--changeSet 0utl_job:29 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_REOPEN_PART_REQUEST', 'Re-opens all part requests on completed installations.', null, 110, null, 0, 0);

--changeSet 0utl_job:30 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_LICENSEEXPIRY', 'Send alert to user for expired license', '0:10', null, 86400, 0, 0);

--changeSet 0utl_job:31 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_AUT_EXP_WARRANTY', 'Automatic Expire Warranty', '0:50', null, 86400, 1, 0);

--changeSet 0utl_job:32 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_WARRANTY_EVALUATION', 'evaluate task for warranties', null, 280, 120, 1, 0);

--changeSet 0utl_job:33 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_UPDATE_CONFIG_POSITION_DESC', 'Update the config position description for every modified inventory item in the system', null, 180, 300, 1, 0);

--changeSet 0utl_job:34 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_SYNCHRONIZEKITATTRIBUTES', 'Update the attributes of the kit inventory that are queued for synchronization', null, 210, 300, 1, 0);

--changeSet 0utl_job:35 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_MVIEW_POSSIBLE_KITS_BY_LOCATION', 'Refresh the Possible Kits By Location MView', null, 220, 43200, 1, 0);

--changeSet 0utl_job:36 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_OIL_CONSUMPTION_SERVER_JOB', 'Populate the Oil Consumption data warehouse', null, 10, 86400, 0, 0);

--changeSet 0utl_job:37 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_COMMON_PURGE_RECORDS', 'Purges temporary data records according to the policies defined in the UTL_PURGE_POLICY table', '0:00', null, 86400, 1, 0);

--changeSet 0utl_job:38 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id )
VALUES ('MX_CORE_VALIDATE_OPEN_INVOICE', 'Evaluates all open invoices and when applicable marks them as ready for payment.', '00:00', null, 86400, 0, 0);

--changeSet 0utl_job:39 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_OBSOLESCE_VENDOR_PART_PRICE', 'Sets vendor part prices as historic if their effective to date have passed.', '1:00', null, 86400, 1, 0);

--changeSet 0utl_job:44 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_REFRESH_MAT_COMP_AD_VIEWS', 'Refresh AD Compliance Materialized Views', '3:20', NULL, 86400, 0, 0);

--changeSet 0utl_job:45 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_REFRESH_TEMP_ROLE_ASSIGNMENT', 'Refresh Temporary Role Assignments for all users', NULL, 0, 86400, 0, 0);

--changeSet 0utl_job:46 stripComments:false
-- Maintenix Job to generate index for enhanced part search
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_GENERATE_PART_SEARCH_INDEX', 'Updates indexes for enhanced part search', null, 30, 60, 1, 0);

--changeSet 0utl_job:48 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_CONFIG_PARM_ENCRYPT', 'Encrypt sensitive configuration parameter values', null, 130, 60, 1, 0);
--changeSet 0utl_job:49 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
VALUES ('MX_CORE_USER_PASS_HASH', 'Generate hashes for user sql authentication passwords', null, 130, 60, 1, 0);
