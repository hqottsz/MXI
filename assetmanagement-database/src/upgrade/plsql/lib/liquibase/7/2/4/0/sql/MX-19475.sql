--liquibase formatted sql


--changeSet MX-19475:1 stripComments:false
UPDATE utl_job SET
  start_delay = 120
WHERE
   job_cd = 'MX_COMMON_ALERT_NOTIFY' AND
   start_delay = 30
;

--changeSet MX-19475:2 stripComments:false
UPDATE utl_job SET
  start_delay = 140
WHERE
   job_cd = 'MX_COMMON_CACHE_RESET' AND
   start_delay = 30
;

--changeSet MX-19475:3 stripComments:false
UPDATE utl_job SET
  start_delay = 130
WHERE
   job_cd = 'MX_COMMON_DATA_ENCRYPT' AND
   start_delay = 30
;

--changeSet MX-19475:4 stripComments:false
UPDATE utl_job SET
  start_time =  '0:01'
WHERE
   job_cd = 'MX_CORE_DB_ANALYZE' AND
   start_time =  '2:40'
;

--changeSet MX-19475:5 stripComments:false
UPDATE utl_job SET
  start_time =  '1:10'
WHERE
  job_cd = 'MX_CORE_DELETE_ORPHANED_FORECASTED_TASKS' AND
  start_time =  '3:30'
;

--changeSet MX-19475:6 stripComments:false
UPDATE utl_job SET
  start_time = null,
  start_delay = 140
WHERE
   job_cd = 'MX_CORE_DELETE_OLD_ASYNC_ACTIONS' AND
  start_time = '3:00' AND
  start_delay IS NULL
;

--changeSet MX-19475:7 stripComments:false
UPDATE utl_job SET
  start_time =  '0:20'
WHERE
   job_cd = 'MX_CORE_EXPIREDINVENTORY' AND
  start_time =  '4:10'
;

--changeSet MX-19475:8 stripComments:false
UPDATE utl_job SET
  start_delay = 230
WHERE
   job_cd = 'MX_CORE_GENERATE_PART_REQUESTS' AND
  start_delay = 30
;

--changeSet MX-19475:9 stripComments:false
UPDATE utl_job SET
  start_time =  '1:20'
WHERE
   job_cd = 'MX_CORE_GENERATEFORECASTEDTASKS' AND
  start_time =  '3:20'
;

--changeSet MX-19475:10 stripComments:false
UPDATE utl_job SET
  start_time = null,
  start_delay = 250
WHERE
   job_cd = 'MX_CORE_GENERATEISSUETRANSFERS' AND
  start_time = '4:00' AND
  start_delay IS NULL
;

--changeSet MX-19475:11 stripComments:false
UPDATE utl_job SET
  start_delay = 240,
  repeat_interval = 15
WHERE
   job_cd = 'MX_CORE_PROCESS_AUTO_RSRV_CONTROLLER' AND
  start_delay = 30 AND
  repeat_interval = 5
;

--changeSet MX-19475:12 stripComments:false
UPDATE utl_job SET
  start_time =  '1:30'
WHERE
   job_cd = 'MX_CORE_UPDATETASKDEADLINES' AND
  start_time =  '3:10'
;

--changeSet MX-19475:13 stripComments:false
UPDATE utl_job SET
  start_time =  '0:30'
WHERE
   job_cd = 'MX_CORE_UPDATEVENDORSTATUS' AND
  start_time =  '4:00'
;

--changeSet MX-19475:14 stripComments:false
UPDATE utl_job SET
  start_time = '1:50',
  repeat_interval = 2592000
WHERE
   job_cd = 'MX_CORE_ABCRECALCULATION' AND
  start_time = '4:30' AND
  repeat_interval = 86400
;

--changeSet MX-19475:15 stripComments:false
UPDATE utl_job SET
  start_delay = 90,
  active_bool =1
WHERE
   job_cd = 'MX_CORE_DATA_CLEAN_UP' AND
  start_delay = 30 AND
  active_bool =0
;

--changeSet MX-19475:16 stripComments:false
UPDATE utl_job SET
  start_time = '1:40',
  start_delay = null,
  repeat_interval = 2592000
WHERE
   job_cd = 'MX_CORE_UPDATETASKLABOURSUMMARY' AND
  start_time = '1:00' AND
  start_delay = 20 AND
  repeat_interval = 86400
;

--changeSet MX-19475:17 stripComments:false
UPDATE utl_job SET
  start_delay = 70,
  repeat_interval = 86400
WHERE
   job_cd = 'MX_CORE_STATUS_BOARD_REFRESH_ASSET_LIST' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:18 stripComments:false
UPDATE utl_job SET
  start_delay = 80
WHERE
   job_cd = 'MX_CORE_STATUS_BOARD_REFRESH_QUERY_DATA' AND
  start_delay = 60
;

--changeSet MX-19475:19 stripComments:false
UPDATE utl_job SET
  start_delay = 150,
  repeat_interval = 1800
WHERE
   job_cd = 'MX_CORE_BASELINE_SYNC' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:20 stripComments:false
UPDATE utl_job SET
  start_delay = 280,
  repeat_interval = 21600
WHERE
   job_cd = 'MX_CORE_MVIEW_TASK_MATERIAL_REQUEST_STATUS' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:21 stripComments:false
UPDATE utl_job SET
  start_delay = 260,
  repeat_interval = 30
WHERE
   job_cd = 'MX_CORE_MVIEW_OPEN_PART_REQ' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:22 stripComments:false
UPDATE utl_job SET
  start_delay = 270,
  repeat_interval = 30
WHERE
   job_cd = 'MX_CORE_MVIEW_DEFER_FAULT_PART_REQ' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:23 stripComments:false
UPDATE utl_job SET
  start_delay = 200,
  repeat_interval = 300
WHERE
   job_cd = 'MX_CORE_MVIEW_FLEET_LIST' AND
  start_delay = 60 AND
  repeat_interval = 60
;

--changeSet MX-19475:24 stripComments:false
UPDATE utl_job SET
  start_time =  '0:40'
WHERE
   job_cd = 'MX_CORE_DEACTIVATE_WARRANTY_CONTRACT' AND
  start_time =  '1:30'
;

--changeSet MX-19475:25 stripComments:false
UPDATE utl_job SET
  start_delay = 100
WHERE
   job_cd = 'MX_CORE_CONVERT_TO_COMPONENT' AND
  start_delay = 30
;

--changeSet MX-19475:26 stripComments:false
UPDATE utl_job SET
  start_delay = 110
WHERE
   job_cd = 'MX_CORE_REOPEN_PART_REQUEST' AND
  start_delay = 30
;

--changeSet MX-19475:27 stripComments:false
UPDATE utl_job SET
  start_time =  '0:10'
WHERE
   job_cd = 'MX_CORE_LICENSEEXPIRY' AND
  start_time =  '9:30'
;

--changeSet MX-19475:28 stripComments:false
UPDATE utl_job SET
  start_time =  '0:50'
WHERE
   job_cd = 'MX_CORE_AUT_EXP_WARRANTY' AND
  start_time =  '1:00'
;

--changeSet MX-19475:29 stripComments:false
UPDATE utl_job SET
  start_delay = 280
WHERE
   job_cd = 'MX_CORE_WARRANTY_EVALUATION' AND
  start_delay = 10
;

--changeSet MX-19475:30 stripComments:false
UPDATE utl_job SET
  start_delay = 180,
  repeat_interval = 300
WHERE
   job_cd = 'MX_CORE_UPDATE_CONFIG_POSITION_DESC' AND
  start_delay = 60 AND
  repeat_interval = 600
;

--changeSet MX-19475:31 stripComments:false
UPDATE utl_job SET
  start_delay = 210,
  repeat_interval = 300
WHERE
   job_cd = 'MX_CORE_SYNCHRONIZEKITATTRIBUTES' AND
  start_delay = 60 AND
  repeat_interval = 600
;

--changeSet MX-19475:32 stripComments:false
UPDATE utl_job SET
  start_delay = 220,
  repeat_interval = 43200
WHERE
   job_cd = 'MX_CORE_MVIEW_POSSIBLE_KITS_BY_LOCATION' AND
  start_delay = 60 AND
  repeat_interval = 3600
;

--changeSet MX-19475:33 stripComments:false
UPDATE utl_job SET
  start_time =  '0:00',
  active_bool = 1
WHERE
   job_cd = 'MX_COMMON_PURGE_RECORDS' AND
  start_time =  '00:00' AND
  active_bool = 0
;

--changeSet MX-19475:34 stripComments:false
UPDATE utl_job SET
  start_delay = 30,
  repeat_interval = 30
WHERE
   job_cd = 'MX_CORE_GERONIMO_GENERATE_WORK_ITEMS' AND
  start_delay = 120 AND
  repeat_interval = 86400
;