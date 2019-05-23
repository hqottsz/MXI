--liquibase formatted sql
	

--changeSet DEV-1240:1 stripComments:false
/********************************************************************************
* Update all existing eqp_part_vendor_price to have min_order_qt value is null.
* Min_order_qty now is mandatory field.
********************************************************************************/
UPDATE eqp_part_vendor_price
SET    min_order_qt   = 1.0
WHERE  min_order_qt is null;    

--changeSet DEV-1240:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PART_VENDOR_PRICE modify (
   "MIN_ORDER_QT" Float NOT NULL DEFERRABLE
)
');
END;
/