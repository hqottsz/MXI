--liquibase formatted sql


--changeSet PLAN-30:1 stripComments:false
-- Insert MX_TS_SEND_LINE_NUMBER_TO_MRO trigger for SendAssignLineNumberToMROTrigger
/***************************************************************
* Create new configuration parameter Send assign line number to MRO
****************************************************************/
INSERT INTO 
	utl_trigger(
	trigger_id,
	trigger_cd,
	exec_order,
	type_cd,
	trigger_name,
	class_name,
	active_bool,
	utl_id
	)
SELECT
	99920,
	'MX_TS_SEND_LINE_NUMBER_TO_MRO',
	1,
	'COMPONENT',
	'request to send assign line number message to MRO for integration',
	'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.lineitem.SendAssignLineNumberToMROTrigger',
	0,
	0
FROM
	dual
WHERE
	NOT EXISTS
	(
	SELECT *
	FROM
		utl_trigger
	WHERE
		trigger_id = 99920
	);

--changeSet PLAN-30:2 stripComments:false
-- insert assign-line-number-request
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd
   )
SELECT
   'http://xml.mxi.com/xsd/core/integration/assign-line-number-request/1.0',         
   'assign-line-number-request',
   'JAVA',
   'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.lineitem.SendAssignLineNumberToMROEntryPoint',
   'process',
   'FULL',
   0
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/integration/assign-line-number-request/1.0' AND 
      root_name = 'assign-line-number-request'
   );

--changeSet PLAN-30:3 stripComments:false
-- assign line number outbound message sending to MRO for integration
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/integration/assign-line-number/1.0', 'assign-line-number', 'jms://com.mxi.mx.jms.integration.mro.IntegrationInboundRequestQueue?connector=mroJmsConnector', 0, to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1 
            FROM 
               ASB_ADAPTER_DEST_LOOKUP 
            WHERE 
               NAMESPACE = 'http://xml.mxi.com/xsd/core/integration/assign-line-number/1.0'
            AND 
               ROOT_NAME = 'assign-line-number');