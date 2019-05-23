--liquibase formatted sql

--changeSet OPER-8779:1 stripComments:false
--added a new trigger code MX_AC_CAPABILITIES_UPDATED
INSERT INTO UTL_TRIGGER
    (
        TRIGGER_ID,
	    TRIGGER_CD,
	    EXEC_ORDER,
	    TYPE_CD,
	    TRIGGER_NAME,
	    CLASS_NAME,
	    ACTIVE_BOOL, UTL_ID
	)
SELECT
  	99914,
  	'MX_AC_CAPABILITIES_UPDATED',
  	1,
  	'COMPONENT',
  	'Aircraft Capabilities Updated',
  	'com.mxi.mx.core.adapter.flight.messages.flight.trigger.AircraftCapabilitiesUpdatedPublishTrigger',
  	0,
  	0
FROM dual
WHERE NOT EXISTS
    (
        SELECT 1
  	    FROM UTL_TRIGGER
  	    WHERE
            TRIGGER_CD = 'MX_AC_CAPABILITIES_UPDATED' AND
            TRIGGER_ID = 99914
    );

--changeSet OPER-8779:2 stripComments:false
INSERT INTO INT_BP_LOOKUP
    (
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
    'http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-updated-request/1.0',
    'aircraft-capabilities-updated-request',
    'JAVA',
    'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilitiesupdated.AircraftCapabilitiesUpdatedEntryPoint10',
    'process',
    'FULL',
    0,
    to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'),
    to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'),
    0,
    'MXI'
FROM DUAL
WHERE NOT EXISTS
    (
        SELECT *
        FROM INT_BP_LOOKUP
        WHERE
            NAMESPACE = 'http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-updated-request/1.0' AND
            ROOT_NAME = 'aircraft-capabilities-updated-request'
    );