--liquibase formatted sql


--changeSet MTX-483:1 stripComments:false
--
-- Insert new int_bp_lookup and utl_alert_type entries.
-- Addresses MTX-483.
--
-- Insert createPartDefinitionV1_2
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt
   )
SELECT
   'http://xml.mxi.com/xsd/core/matadapter/create-part-definition/1.1',         
   'create-part-definition',
   'EJB',
   'com.mxi.mx.core.ejb.MaterialAdapter',
   'createPartDefinitionV1_2',
   'FULL',
   0,
   sysdate
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/create-part-definition/1.1' AND 
      root_name = 'create-part-definition'
   );

--changeSet MTX-483:2 stripComments:false
-- Insert updatePartDefinitionStatusV1_2
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt
   )
SELECT
   'http://xml.mxi.com/xsd/core/matadapter/update-part-definition-status/1.1',         
   'update-part-definition-status',
   'EJB',
   'com.mxi.mx.core.ejb.MaterialAdapter',
   'updatePartDefinitionStatusV1_2',
   'FULL',
   0,
   sysdate
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/update-part-definition-status/1.1' AND 
      root_name = 'update-part-definition-status'
   );

--changeSet MTX-483:3 stripComments:false
-- Insert part-request-request
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt
   )
SELECT
   'http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.1',         
   'part-request-request',
   'JAVA',
   'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV1_2',
   'process',
   'FULL',
   0,
   sysdate
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.1' AND 
      root_name = 'part-request-request'
   );      

--changeSet MTX-483:4 stripComments:false
-- Insert update-part-request-request
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt
   )
SELECT
   'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.1',         
   'update-part-request-request',
   'JAVA',
   'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestEntryPointV1_2',
   'process',
   'FULL',
   0,
   sysdate
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.1' AND 
      root_name = 'update-part-request-request'
   );

--changeSet MTX-483:5 stripComments:false
-- Insert part-request-status
INSERT INTO 
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt
   )
SELECT
   'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.2',         
   'part-request-status',
   'EJB',
   'com.mxi.mx.core.ejb.MaterialAdapter',
   'partRequestStatusV1_2',
   'FULL',
   0,
   sysdate
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      int_bp_lookup
   WHERE 
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.2' AND 
      root_name = 'part-request-status'
   );      

--changeSet MTX-483:6 stripComments:false
-- Insert OBSOLETE_PART Alert
INSERT INTO 
	utl_alert_type (
	alert_type_id,
	alert_name,
	alert_ldesc,
	notify_cd,
	notify_class,
	category,
	message,
	key_bool,
	priority,
	priority_calc_class,
	active_bool,
	utl_id 
	)
SELECT
   243,
   'core.alert.OBSOLETE_PART_name',
   'core.alert.OBSOLETE_PART_description',
   'ROLE',
   null,
   'PART',
   'core.alert.OBSOLETE_PART_message',
   1,
   0,
   null,
   1,
   0 
FROM 
   dual
WHERE 
   NOT EXISTS 
   (
   SELECT *
   FROM 
      utl_alert_type
   WHERE 
      alert_type_id = 243 AND 
      alert_name = 'core.alert.OBSOLETE_PART_name'
   );   

--changeSet MTX-483:7 stripComments:false
-- insert MX_TS_SCHEDULE_INTERNAL trigger for PartRequestPublishTrigger
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
	99950,
	'MX_TS_SCHEDULE_INTERNAL',
	1,
	'COMPONENT',
	'part request by work order after internally schedule work package ',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99950 AND
		trigger_cd = 'MX_TS_SCHEDULE_INTERNAL'
	);	

--changeSet MTX-483:8 stripComments:false
-- Insert MX_TS_SCHEDULE_EXTERNAL trigger for PartRequestPublishTrigger
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
	99949,
	'MX_TS_SCHEDULE_EXTERNAL',
	1,
	'COMPONENT',
	'part request by work order after externally schedule work package',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99949 AND
		trigger_cd = 'MX_TS_SCHEDULE_EXTERNAL'
	);	

--changeSet MTX-483:9 stripComments:false
-- Insert MX_TS_COMMIT trigger for PartRequestPublishTrigger
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
	99948,
	'MX_TS_COMMIT',
	1,
	'COMPONENT',
	'part request by work order after commit work work package',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99948 AND
		trigger_cd = 'MX_TS_COMMIT'
	);	

--changeSet MTX-483:10 stripComments:false
-- Insert MX_TS_UNCOMMIT trigger for PartRequestPublishTrigger
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
	99947,
	'MX_TS_UNCOMMIT',
	1,
	'COMPONENT',
	'part request by work order after uncommit work package',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99947 AND
		trigger_cd = 'MX_TS_UNCOMMIT'
	);	

--changeSet MTX-483:11 stripComments:false
-- Insert MX_TS_REQUEST_PARTS trigger for PartRequestPublishTrigger
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
	99946,
	'MX_TS_REQUEST_PARTS',
	1,
	'COMPONENT',
	'part request by work order after request parts',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99946 AND
		trigger_cd = 'MX_TS_REQUEST_PARTS'
	);	

--changeSet MTX-483:12 stripComments:false
-- Insert MX_TS_CREATE trigger for PartRequestPublishTrigger
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
	99945,
	'MX_TS_CREATE',
	1,
	'COMPONENT',
	'part request by task after initialize task definition which creats task on inventory',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99945 AND
		trigger_cd = 'MX_TS_CREATE'
	);	

--changeSet MTX-483:13 stripComments:false
-- Insert MX_TS_TASKASSIGN trigger for PartRequestPublishTrigger
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
	99944,
	'MX_TS_TASKASSIGN',
	1,
	'COMPONENT',
	'part request by task after assign task to or unassign task from work package',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99944 AND
		trigger_cd = 'MX_TS_TASKASSIGN'
	);	

--changeSet MTX-483:14 stripComments:false
-- Insert MX_TS_ADD_PART_REQUIREMENT trigger for PartRequestPublishTrigger
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
	99943,
	'MX_TS_ADD_PART_REQUIREMENT',
	1,
	'COMPONENT',
	'part request by part requirement after add part requirement to task',
	'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2',
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
		trigger_id = 99943 AND
		trigger_cd = 'MX_TS_ADD_PART_REQUIREMENT'
	);	

--changeSet MTX-483:15 stripComments:false
-- Insert MX_TS_CANCEL trigger for UpdatePartRequestPublishTrigger
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
	99942,
	'MX_TS_CANCEL',
	1,
	'COMPONENT',
	'update part request by part requirement after cancel task',
	'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2',
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
		trigger_id = 99942 AND
		trigger_cd = 'MX_TS_CANCEL'
	);	

--changeSet MTX-483:16 stripComments:false
-- Insert MX_PR_CANCEL trigger for UpdatePartRequestPublishTrigger
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
	99941,
	'MX_PR_CANCEL',
	1,
	'COMPONENT',
	'update part request by part requirement after cancel part requirement',
	'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2',
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
		trigger_id = 99941 AND
		trigger_cd = 'MX_PR_CANCEL'
	);	

--changeSet MTX-483:17 stripComments:false
-- Insert MX_PR_PRIORITY trigger for UpdatePartRequestPublishTrigger
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
	99940,
	'MX_PR_PRIORITY',
	1,
	'COMPONENT',
	'update part request by part requirement after edit request priority',
	'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2',
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
		trigger_id = 99940 AND
		trigger_cd = 'MX_PR_PRIORITY'
	);

--changeSet MTX-483:18 stripComments:false
-- Insert MX_PR_EXTERNAL_REFERENCE trigger for UpdatePartRequestPublishTrigger
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
	99939,
	'MX_PR_EXTERNAL_REFERENCE',
	1,
	'COMPONENT',
	'update part request by part requirement after edit external reference',
	'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2',
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
		trigger_id = 99939 AND
		trigger_cd = 'MX_PR_EXTERNAL_REFERENCE'
	);	

--changeSet MTX-483:19 stripComments:false
-- Insert MX_PR_NEEDED_BY_DATE trigger for UpdatePartRequestPublishTrigger
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
	99938,
	'MX_PR_NEEDED_BY_DATE',
	1,
	'COMPONENT',
	'update part request by part requirement after edit needed by date',
	'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2',
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
		trigger_id = 99938 AND
		trigger_cd = 'MX_PR_NEEDED_BY_DATE'
	);