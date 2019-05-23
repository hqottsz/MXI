--liquibase formatted sql


--changeSet OPER-3703:1 stripComments:false
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
	99921,
	'MX_FAULT_DEADLINE_EXTENDED',
	1,
	'COMPONENT',
	'EXTEND FAULT DEADLINES',
	'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger',
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
			TRIGGER_CD = 'MX_FAULT_DEADLINE_EXTENDED'
			AND
			TRIGGER_ID = 99921
	);	 