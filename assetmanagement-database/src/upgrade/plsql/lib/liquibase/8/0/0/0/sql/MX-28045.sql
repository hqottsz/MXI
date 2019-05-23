--liquibase formatted sql


--changeSet MX-28045:1 stripComments:false
-- Update configuration parameter if value is old default value
UPDATE
  utl_config_parm
SET
  parm_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet,.*/lrp,.*/ppc,.*/StationMonitoringServlet,.*/LoadPlanningViewer',
  modified_in = '8.0-SP1'
WHERE
  parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
  AND
  parm_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet'
;

--changeSet MX-28045:2 stripComments:false
-- Update default value
UPDATE
  utl_config_parm
SET
  default_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet,.*/lrp,.*/ppc,.*/StationMonitoringServlet,.*/LoadPlanningViewer',
  modified_in = '8.0-SP1'
WHERE
  parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
  AND
  default_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet'
;