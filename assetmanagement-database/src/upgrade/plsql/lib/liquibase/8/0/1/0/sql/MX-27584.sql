--liquibase formatted sql


--changeSet MX-27584:1 stripComments:false
-- part_definition_created v 1.1
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/matadapter/part_definition_created/1.1', 
   'part_definition_created', 
   'EJB', 
   'com.mxi.mx.core.ejb.MaterialAdapter', 
   'send', 
   'FULL',
   0, 
   to_date('25-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('25-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/part_definition_created/1.1'
         AND
         ROOT_NAME = 'part_definition_created'
   );

--changeSet MX-27584:2 stripComments:false
-- part_definition_created trigger
INSERT INTO UTL_TRIGGER (
	TRIGGER_ID, 
	TRIGGER_CD, 
	EXEC_ORDER, 
	TYPE_CD, 
	TRIGGER_NAME, 
	CLASS_NAME, 
	ACTIVE_BOOL, 
	UTL_ID
)
SELECT
	99953,
	'MX_MAT_PART_DEF_CREATED',
	1,
	'COMPONENT',
	'Send Part Definition Created',
	'com.mxi.mx.core.adapter.services.material.partdefinitioncreated.PartDefinitionCreatedService11',
	0,
	0
FROM 
	DUAL
WHERE
	NOT EXISTS (
		SELECT
			1
		FROM
			UTL_TRIGGER
		WHERE
			TRIGGER_CD = 'MX_MAT_PART_DEF_CREATED'
			AND
			TRIGGER_ID = 99953
	);