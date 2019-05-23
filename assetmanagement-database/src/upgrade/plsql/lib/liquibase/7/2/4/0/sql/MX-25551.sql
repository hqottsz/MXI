--liquibase formatted sql


--changeSet MX-25551:1 stripComments:false
-- Update configuration parameter if value is old default value
UPDATE
  utl_config_parm
SET
  parm_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet'
WHERE
  parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
  AND
  parm_value = '.*((/servlet/report).*|Pdf)'
;

--changeSet MX-25551:2 stripComments:false
-- Update default value
UPDATE
  utl_config_parm
SET
  default_value = '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet'
WHERE
  parm_name = 'HTTP_COMPRESSION_EXCLUDE_URLS'
  AND
  default_value = '.*((/servlet/report).*|Pdf)'
;