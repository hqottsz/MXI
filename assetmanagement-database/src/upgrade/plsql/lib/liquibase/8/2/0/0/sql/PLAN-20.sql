--liquibase formatted sql


--changeSet PLAN-20:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new configuration parameter Send Work Package to MRO
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SEND_WORK_PACKAGE_TO_MRO',
      'Permission to send Work Package message to MRO.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.1-SP3',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet PLAN-20:2 stripComments:false
-- Insert MX_TS_SEND_WP_TO_MRO trigger for SendWPToMROTrigger
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
	99923,
	'MX_TS_SEND_WP_TO_MRO',
	1,
	'COMPONENT',
	'request to send work package message to MRO for integration',
	'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendWPToMROTrigger',
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
		trigger_id = 99923
	);

--changeSet PLAN-20:3 stripComments:false
-- insert work-package-coordination-request
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
   'http://xml.mxi.com/xsd/core/integration/work-package-coordination-request/1.0',         
   'work-package-coordination-request',
   'JAVA',
   'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendWPToMROEntryPoint',
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
      namespace = 'http://xml.mxi.com/xsd/core/integration/work-package-coordination-request/1.0' AND 
      root_name = 'work-package-coordination-request'
   );