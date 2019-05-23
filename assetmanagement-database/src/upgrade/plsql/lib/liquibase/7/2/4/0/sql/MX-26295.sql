--liquibase formatted sql


--changeSet MX-26295:1 stripComments:false
-- Worker to handle StockLevelCheck event
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, utl_id )
SELECT
   'STOCK_LEVEL_CHECK', 'com.mxi.mx.core.services.stocklevel.StockLevelCheckWorker', 'wm/Maintenix-DefaultWorkManager', 1, 0, 0
FROM
   dual
WHERE
   NOT EXISTS(
     SELECT 1 FROM
        utl_work_item_type
     WHERE
        name = 'STOCK_LEVEL_CHECK'
   )
;

--changeSet MX-26295:2 stripComments:false
-- Stock Level Check Failed alert
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT
   237, 'core.alert.STOCK_LEVEL_CHECK_FAILED_name', 'core.alert.STOCK_LEVEL_CHECK_FAILED_description', 'ROLE', null, 'STOCK_LEVEL', 'core.alert.STOCK_LEVEL_CHECK_FAILED_message', 1, 0, null, 1, 0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM
         utl_alert_type
      WHERE
         alert_type_id = 237
   )
;