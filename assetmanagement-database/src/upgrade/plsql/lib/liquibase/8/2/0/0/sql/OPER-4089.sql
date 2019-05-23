--liquibase formatted sql


--changeSet OPER-4089:1 stripComments:false
/* Update all PRAWAITISSUE to PROPEN if AUTO_ISSUE_INVENTORY = TRUE */
UPDATE evt_event
SET 
	evt_event.event_status_cd = 'PROPEN'
WHERE
     evt_event.event_type_db_id = 0 AND
     evt_event.event_type_cd = 'PR'
     AND
     evt_event.event_status_db_id = 0 AND
     evt_event.event_status_cd = 'PRAWAITISSUE'
     AND 
	EXISTS
     	(
           SELECT 1 FROM utl_config_parm
           WHERE
             	utl_config_parm.parm_name = 'AUTO_ISSUE_INVENTORY'
           	AND
           	utl_config_parm.parm_value = 'TRUE'
     	);