--liquibase formatted sql


--changeSet SWA-134:1 stripComments:false
-- insert trigger class for MX_IX_SEND
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99916, 'MX_IX_SEND', 1, 'COMPONENT', 'Send Order Inventory Returned', 'com.mxi.mx.integration.finance.order.trigger.SendOrderInventoryReturnedTrigger1_0', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99916 );