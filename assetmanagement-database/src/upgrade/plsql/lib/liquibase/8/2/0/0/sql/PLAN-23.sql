--liquibase formatted sql


--changeSet PLAN-23:1 stripComments:false
-- Insert MX_TS_SEND_CANCEL_WP_TO_MRO trigger for SendCancelWPToMROTrigger
/***************************************************************
* Create new configuration parameter Send Cancel Work Package to MRO
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
	99922,
	'MX_TS_SEND_CANCEL_WP_TO_MRO',
	1,
	'COMPONENT',
	'request to send cancel work package message to MRO for integration',
	'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendCancelWPToMROTrigger',
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
		trigger_id = 99922
	);

--changeSet PLAN-23:2 stripComments:false
-- insert cancel-work-package-request
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
   'http://xml.mxi.com/xsd/core/integration/cancel-work-package-request/1.0',         
   'cancel-work-package-request',
   'JAVA',
   'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendCancelWPToMROEntryPoint',
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
      namespace = 'http://xml.mxi.com/xsd/core/integration/cancel-work-package-request/1.0' AND 
      root_name = 'cancel-work-package-request'
   );

--changeSet PLAN-23:3 stripComments:false
-- cancel work package outbound message sending to MRO for integration
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/integration/cancel-work-package/1.0', 'cancel-work-package', 'jms://com.mxi.mx.jms.integration.mro.IntegrationInboundRequestQueue?connector=mroJmsConnector', 0, to_date('29-07-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-07-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1 
            FROM 
               ASB_ADAPTER_DEST_LOOKUP 
            WHERE 
               NAMESPACE = 'http://xml.mxi.com/xsd/core/integration/cancel-work-package/1.0'
            AND 
               ROOT_NAME = 'cancel-work-package');