--liquibase formatted sql


--changeSet MX-23921:1 stripComments:false
-- migration script for MX_23921
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99959, 'MX_EDIT_FAULT', 1, 'COMPONENT', 'EDIT FAULT', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99959);  

--changeSet MX-23921:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99958, 'MX_CF_SET_OP_RESTRICTION', 1, 'COMPONENT', 'SET OPER RESTRICTION', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99958);