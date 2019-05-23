--liquibase formatted sql


--changeSet DEV-1422:1 stripComments:false
INSERT INTO
 utl_work_item_type
(
  name, worker_class, work_manager, enabled, utl_id
)
SELECT 'Tax_Charge_Vendor_Modified', 'com.mxi.mx.core.worker.procurement.TaxChargeVendorModifiedWorker', 'wm/Maintenix-DefaultWorkManager', 1, 0
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'Tax_Charge_Vendor_Modified' );