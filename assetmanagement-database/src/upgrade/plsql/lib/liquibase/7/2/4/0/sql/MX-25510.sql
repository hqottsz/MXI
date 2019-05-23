--liquibase formatted sql


--changeSet MX-25510:1 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
       SELECT 99957, 'MX_FL_DEADLINE_UPDATED', 1, 
              'COMPONENT', 'Inventory Deadlines Updated', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.DeadlinesUpdatedTrigger', 
              0, 0
       FROM dual
       WHERE NOT EXISTS (SELECT * FROM utl_trigger WHERE TRIGGER_ID = 99957 AND TRIGGER_CD = 'MX_FL_DEADLINE_UPDATED');	   

--changeSet MX-25510:2 stripComments:false
INSERT INTO int_bp_lookup (namespace, root_name, ref_type, ref_name, method_name, int_logging_type_cd, rstat_cd)
       SELECT 'http://xml.mxi.com/xsd/core/flight/inventory-deadlines-updated-request/1.0', 'inventory-deadlines-updated-request',
              'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.FlightPublishingService',
              'generateDeadlinesUpdated', 'FULL', 0
       FROM dual
       WHERE NOT EXISTS (SELECT * FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/flight/inventory-deadlines-updated-request/1.0' AND root_name = 'inventory-deadlines-updated-request');