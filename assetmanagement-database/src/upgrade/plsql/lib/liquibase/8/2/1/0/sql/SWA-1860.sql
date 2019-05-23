--liquibase formatted sql


--changeSet SWA-1860:1 stripComments:false
-- Add bp lookup for v1.2 of flight usage service
INSERT INTO 
	INT_BP_LOOKUP 
		(
			NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
			)
SELECT 
	'http://xml.mxi.com/xsd/core/flights/usage/1.2', 'usages', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.usage.adapter.UsageIntegrationInterface12', 'process', 'FULL', 0, to_date('09-08-2016 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-08-2016 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
	dual
WHERE
	NOT EXISTS ( SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/flights/usage/1.2' );