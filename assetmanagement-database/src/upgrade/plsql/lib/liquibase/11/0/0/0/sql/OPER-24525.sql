--liquibase formatted sql

--changeSet OPER-24525:1 stripComments:false 
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id  )
SELECT 'WAREHOUSE_STOCK_LEVEL_CHECK', 'com.mxi.mx.core.services.stocklevel.WarehouseStockLevelCheckWorker', 'wm/Maintenix-DefaultWorkManager', 1, 0, 500, 0
FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'WAREHOUSE_STOCK_LEVEL_CHECK' );