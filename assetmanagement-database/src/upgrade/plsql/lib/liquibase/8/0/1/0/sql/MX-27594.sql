--liquibase formatted sql


--changeSet MX-27594:1 stripComments:false
-- potential_turnin 11
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
   'http://xml.mxi.com/xsd/core/matadapter/potential_turnin/1.1', 
   'potential_turnin', 
   'EJB', 
   'com.mxi.mx.core.ejb.MaterialAdapter', 
   'send', 
   'FULL',
   0, 
   to_date('04-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('04-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/potential_turnin/1.1'
         AND
         ROOT_NAME = 'potential_turnin'
   );

--changeSet MX-27594:2 stripComments:false
-- potential-turn-in trigger
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
	99954,
	'MX_MAT_POTENTIAL_TURN_IN',
	1,
	'COMPONENT',
	'Send Potential Turn In',
	'com.mxi.mx.core.adapter.services.material.potentialturnin.PotentialTurnInService11',
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
			TRIGGER_CD = 'MX_MAT_POTENTIAL_TURN_IN'
			AND
			TRIGGER_ID = 99954
	);