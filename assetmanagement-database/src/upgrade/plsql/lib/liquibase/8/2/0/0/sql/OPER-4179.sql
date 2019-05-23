--liquibase formatted sql


--changeSet OPER-4179:1 stripComments:false
DELETE FROM 
   UTL_TRIGGER 
WHERE 
			TRIGGER_CD = 'MX_FAULT_DEADLINE_EXTENDED'
			AND
			TRIGGER_ID IN ( 99936, 99923 );

--changeSet OPER-4179:2 stripComments:false
MERGE INTO 
   utl_trigger
USING 
  dual
ON ( utl_trigger.trigger_cd = 'MX_FAULT_DEADLINE_EXTENDED')
WHEN MATCHED THEN 
   UPDATE SET trigger_id = 99921
WHEN NOT MATCHED THEN 
   INSERT (
      trigger_id, 
      trigger_cd, 
      exec_order, 
      type_cd, 
      trigger_name, 
      class_name, 
      active_bool, 
      utl_id      
   )  
   VALUES (
      99921, 
      'MX_FAULT_DEADLINE_EXTENDED', 
      1, 
      'COMPONENT', 
      'EXTEND FAULT DEADLINES', 
      'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 
      0, 
      0
   );