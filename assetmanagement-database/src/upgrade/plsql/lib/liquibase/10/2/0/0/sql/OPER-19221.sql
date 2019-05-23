--liquibase formatted sql
-- Delete the MX_ATA_SPARES_ORDER_MESSAGE_EVENT event 

--changeSet OPER-19221:1 stripComments:false
DELETE FROM int_event_config WHERE event_type_cd = 'MX_ATA_SPARES_ORDER_MESSAGE';