-- activate MX_PO_ISSUE trigger for Spec 2000 message testing
UPDATE utl_trigger
SET active_bool = 1
WHERE
class_name = 'com.mxi.mx.integration.procurement.atasparesorder.triggers.IssueSparesOrderMessageTriggerV1_0';
-- activate trigger for send_order_information message testing
UPDATE utl_trigger
SET active_bool = 1
WHERE
trigger_cd  = 'MX_FNC_SEND_ORDER_INFORMATION';
-- activate trigger for order_inventory_received message testing
UPDATE utl_trigger
SET active_bool = 1
WHERE
trigger_cd = 'MX_IN_INSPSRV'
AND trigger_name = 'Send Order Inventory Received';
-- activate trigger for order_inventory_returned message testing
UPDATE utl_trigger
SET active_bool = 1
WHERE
trigger_cd = 'MX_IX_SEND'
AND trigger_name = 'Send Order Inventory Returned';
-- activate MX_PO_CANCEL trigger, neded for Spec 2000 message E2E Testing only
UPDATE utl_trigger
SET active_bool = 1
WHERE
class_name = 'com.mxi.mx.integration.procurement.atasparesorder.triggers.CancelSparesOrderMessageTriggerV1_0';
--activate MX_FNC_SEND_DETAILED_FINANCIAL_LOG trigger, needed for journal entry testing
UPDATE utl_trigger
   SET active_bool = 1
WHERE trigger_cd = 'MX_FNC_SEND_DETAILED_FINANCIAL_LOG';