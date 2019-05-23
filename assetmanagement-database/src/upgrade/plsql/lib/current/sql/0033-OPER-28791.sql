--liquibase formatted sql

--changeSet OPER-28791:1 stripComments:false
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id  )
SELECT 'BULK_LOAD_DATA', 'com.mxi.mx.core.services.dataservices.BulkLoadDataWorker', 'wm/Maintenix-DefaultWorkManager', 1, 0, 500, 0
FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'BULK_LOAD_DATA' );