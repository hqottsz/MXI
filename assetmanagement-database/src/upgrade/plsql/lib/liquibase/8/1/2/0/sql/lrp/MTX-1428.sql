--liquibase formatted sql


--changeSet MTX-1428:1 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'lrp.Schedule', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/lrp/ScheduleReport', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'lrp.Schedule'
);