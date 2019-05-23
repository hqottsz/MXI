--liquibase formatted sql

--changeSet OPER-29362:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_EXPORT_FILE_WRK_LOCATION');
END;
/

--changeSet OPER-29362:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_EXPORT_FILE_LOCATION');
END;
/

--changeSet OPER-29362:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_EXPORT_FILE_BKP_LOCATION');
END;
/

--changeSet OPER-29362:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_IMPORT_FILE_WRK_LOCATION');
END;
/

--changeSet OPER-29362:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_IMPORT_FILE_LOCATION');
END;
/

--changeSet OPER-29362:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_IMPORT_FILE_BKP_LOCATION');
END;
/

--changeSet OPER-29362:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_INV_XFER_LOG_LOCATION');
END;
/

--changeSet OPER-29362:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.config_parm_delete('DPO_INV_XFER_MAX_PROCESS');
END;
/


--changeSet OPER-29362:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_BACKUP_IMPORT_FILE');
END;
/

--changeSet OPER-29362:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_EXPORT_AC_FILE');
END;
/

--changeSet OPER-29362:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_EXPORT_SHIP_FILE');
END;
/

--changeSet OPER-29362:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_IMPORT_FILE');
END;
/

--changeSet OPER-29362:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_IMPORT_INDUCT_INVENTORY');
END;
/

--changeSet OPER-29362:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_DPO_SET_LOCATION');
END;
/


--changeSet OPER-29362:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_EXP_SUCCESS_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_EXP_SUCCESS_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_EXP_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_EXP_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_IMP_SUCCESS_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_IMP_SUCCESS_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_IMP_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_IMP_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_IND_SUCCESS_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_IND_SUCCESS_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.DPO_INV_XFER_IND_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.DPO_INV_XFER_IND_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.VALIDATE_STREAMS_OBJECTS_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.VALIDATE_STREAMS_OBJECTS_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.EXPORT_LCR_FILE_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.EXPORT_LCR_FILE_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.IMPORT_LCR_FILE_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.IMPORT_LCR_FILE_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/

--changeSet OPER-29362:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from UTL_ALERT_TYPE where ALERT_NAME = 'core.alert.APPLY_LCR_PROCESS_ERROR_name';
DECLARE 
  id utl_alert_type.alert_type_id%TYPE;
BEGIN
  select alert_type_id into id from utl_alert_type where alert_name = 'core.alert.APPLY_LCR_PROCESS_ERROR_name';
  UTL_MIGR_DATA_PKG.alert_type_delete(id);
END;
/


--changeSet OPER-29362:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from all_scheduler_jobs where job_name = 'MX_DPO_DBMS_CAPTURE_BUILD';
BEGIN
      JOBS_ADMIN_PKG.drop_job_and_schedule('MX_DPO_DBMS_CAPTURE_BUILD',TRUE);
END;
/

--changeSet OPER-29362:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from all_scheduler_jobs where job_name = 'MX_DPO_EXPORT_BASELINE_FILES';
BEGIN
      JOBS_ADMIN_PKG.drop_job_and_schedule('MX_DPO_EXPORT_BASELINE_FILES',TRUE);
END;
/

--changeSet OPER-29362:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from all_scheduler_jobs where job_name = 'MX_DPO_IMPORT_BASELINE_FILES';
BEGIN
      JOBS_ADMIN_PKG.drop_job_and_schedule('MX_DPO_IMPORT_BASELINE_FILES',TRUE);
END;
/

--changeSet OPER-29362:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 select count(1) from all_scheduler_jobs where job_name = 'MX_DPO_CONSOL_DISPATCHER';
BEGIN
      JOBS_ADMIN_PKG.drop_job_and_schedule('MX_DPO_CONSOL_DISPATCHER',TRUE);
END;
/


--changeSet OPER-29362:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB DISABLE';
	  DELETE FROM UTL_JOB WHERE job_cd = 'MX_DPO_DBMS_CAPTURE_BUILD';
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB ENABLE';
END;
/

--changeSet OPER-29362:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB DISABLE';
	  DELETE FROM UTL_JOB WHERE job_cd = 'MX_DPO_EXPORT_BASELINE_FILES';
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB ENABLE';
END;
/

--changeSet OPER-29362:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB DISABLE';
	  DELETE FROM UTL_JOB WHERE job_cd = 'MX_DPO_IMPORT_BASELINE_FILES';
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB ENABLE';
END;
/

--changeSet OPER-29362:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB DISABLE';
	  DELETE FROM UTL_JOB WHERE job_cd = 'MX_DPO_CONSOL_DISPATCHER';
	  EXECUTE IMMEDIATE 'ALTER TRIGGER TAUR_UTL_JOB ENABLE';
END;
/
