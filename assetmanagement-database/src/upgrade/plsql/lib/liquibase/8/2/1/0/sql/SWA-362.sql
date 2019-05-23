--liquibase formatted sql


--changeSet SWA-362:1 stripComments:false
/**************************************************************************************
* 
* SWA-362: Insert scripts for the alerts concerning failure to send 
*            order-inventory-returned and order-inventory-received messages.
*
***************************************************************************************/
INSERT INTO 
  utl_alert_type 
SELECT 615, 
       'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_name', 
       'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_description', 
       'ROLE', 
       null, 
       'SHIPMENT', 
       'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_message', 
       1, 
       0, 
       null, 
       1, 
       0
FROM
  dual
WHERE NOT EXISTS
 (SELECT 1
  FROM utl_alert_type
  WHERE alert_type_id = 615);  

--changeSet SWA-362:2 stripComments:false
INSERT INTO 
  utl_alert_type 
SELECT 616, 
       'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_name', 
       'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_description', 
       'ROLE', 
       null, 
       'INVENTORY', 
       'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_message', 
       1, 
       0, 
       null, 
       1, 
       0
FROM
  dual
WHERE NOT EXISTS
 (SELECT 1
  FROM utl_alert_type
  WHERE alert_type_id = 616); 