--liquibase formatted sql

--changeSet OPER-10552:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT',
      'LOGIC',
      'When false, you can receive a shipment of existing inventory from an internal location (not new inventory
received from a vendor) and the inventory will not be quarantined based on how much shelf life remains for the
item. When true, the percentage of remaining shelf life for an item is assessed and items can be quarantined
based on the value specified by the PERCENT_SHELF_LIFE_WARNING parameter.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Purchasing - Purchase Orders',
      '8.2-SP4',
      0
   );
END;
/