--liquibase formatted sql
-- changeSet OPER-1110:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
-- Remove Alert Type
-- core.alert.GENERATE_FLIGHT_PLAN_FOR_AIRCRAFT_JOB_FAILED_name
UTL_MIGR_DATA_PKG.alert_type_delete(p_alert_type => 7);
END;
/
--changeSet OPER-1110:2 stripComments:false
-- Remove Job
DELETE 
FROM UTL_JOB 
WHERE JOB_CD = 'MX_CORE_GENERATEFLIGHTPLANFORAIRCRAFT';