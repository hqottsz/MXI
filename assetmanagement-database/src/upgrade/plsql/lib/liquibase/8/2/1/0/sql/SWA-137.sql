--liquibase formatted sql


--changeSet SWA-137:1 stripComments:false
-- migration script for SWA-137
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99918, 'MX_IN_INSPSRV', 1, 'COMPONENT', 'Send Order Inventory Received', 'com.mxi.mx.integration.finance.order.trigger.SendOrderInventoryReceivedTrigger1_0', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99918 );  