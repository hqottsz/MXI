@sql/task_taskACTV.sql
@sql/ref_qty_unit.sql
@sql/eqp_part_alt_unit.sql
@sql/eqp_part_no.sql
@sql/utl_alert_type_role.sql
@sql/persona/maintenanceController/create_moc_airport.baseline.sql
@sql/persona/maintenanceController/deferralreference/deferral_reference.sql
@sql/persona/maintenanceController/phoneupdeferral/deferral_reference.sql
@sql/persona/maintenanceController/pendingdeferralrequests/deferral_reference.sql
@sql/persona/lineTechnician/selectreference/deferral_reference.sql
@sql/persona/materialController/WarehouseStockLevels/AddSuppliertoWarehouse.sql
@sql/persona/materialController/PickInvStockRequest/InsertStockDistREQ.sql

BEGIN
  dbms_utility.compile_schema(SCHEMA => USER);
END;
/