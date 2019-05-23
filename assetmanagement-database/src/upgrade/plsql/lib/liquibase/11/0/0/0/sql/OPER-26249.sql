--liquibase formatted sql

--changeSet OPER-26249:1 stripComments:false
-- Add report configuration for Stock Distribution Pick List Report
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'stockdistribution.DistReqPickList', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/stockdistribution/DistReqPickList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'stockdistribution.DistReqPickList');
