-- activate trigger for send-order-information-40 message testing
UPDATE utl_trigger
SET active_bool = 1
WHERE
trigger_cd  = 'MX_FNC_SEND_ORDER_INFORMATION'
AND
class_name LIKE '%TrgOrderInformationById40%';
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
--activate MX_FNC_SEND_DETAILED_FINANCIAL_LOG trigger, needed for journal entry testing
UPDATE utl_trigger
   SET active_bool = 1
WHERE trigger_cd = 'MX_FNC_SEND_DETAILED_FINANCIAL_LOG';