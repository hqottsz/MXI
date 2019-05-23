--liquibase formatted sql


--changeSet MX-26787:1 stripComments:false
/***********************
** System Failure Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 238, 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_name', 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_description', 'ROLE', null, 'SYSTEM', 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_message', 1, 0, null, 1, 0
FROM dual
WHERE
  NOT EXISTS(
     SELECT 1
     FROM
        utl_alert_type
     WHERE
        alert_type_id = 238
  )
;

--changeSet MX-26787:2 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 239, 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_name', 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_description', 'ROLE', null, 'SYSTEM', 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_message', 1, 0, null, 1, 0
FROM dual
WHERE
  NOT EXISTS(
     SELECT 1
     FROM
        utl_alert_type
     WHERE
        alert_type_id = 239
  )
;

--changeSet MX-26787:3 stripComments:false
DELETE FROM
   utl_work_item_type
WHERE
   name IN (
      'INVENTORY_TRANSFER_EXPORT',
      'INVENTORY_TRANSFER_IMPORT',
      'INVENTORY_TRANSFER_INDUCT'
   )
;