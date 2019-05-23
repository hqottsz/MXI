--liquibase formatted sql
--Replace the alert category of MESSAGE_IS_NOT_DELIVERED from PO to SYSTEM

--changeSet OPER-24644:1 stripComments:false
UPDATE
   utl_alert_type
SET
   category = 'SYSTEM'
WHERE
   alert_type_id = 602 AND
   category = 'PO';