--liquibase formatted sql

--changeSet OPER-4577:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isReadyForShipping
(
 aShipmentDbId   NUMBER,
 aShipmentId    NUMBER
 ) RETURN NUMBER
 IS
   ln_IsReadyForShipping NUMBER;
   ln_ShipmentLineCount NUMBER;
 BEGIN

 -- check if there exist shipment lines
 SELECT
   COUNT(*)
 INTO
   ln_ShipmentLineCount
 FROM
   ship_shipment_line
 WHERE
   ship_shipment_line.shipment_db_id = aShipmentDbId AND
   ship_shipment_line.shipment_id    = aShipmentId
   AND
   ship_shipment_line.rstat_cd	= 0;

 IF ln_ShipmentLineCount = 0 THEN
   RETURN 0;
 END IF;

 -- check if shipment lines are associated with inventory and location is DOCK type
 SELECT
   COUNT(*)
 INTO
   ln_IsReadyForShipping
 FROM
   ship_shipment_line
   INNER JOIN inv_inv ON
   ship_shipment_line.inv_no_db_id = inv_inv.inv_no_db_id AND
   ship_shipment_line.inv_no_id = inv_inv.inv_no_id
   INNER JOIN inv_loc ON
   inv_inv.loc_db_id = inv_loc.loc_db_id AND
   inv_inv.loc_id = inv_loc.loc_id
 WHERE
   ship_shipment_line.shipment_db_id = aShipmentDbId AND
   ship_shipment_line.shipment_id    = aShipmentId
   AND
   ship_shipment_line.rstat_cd	= 0
   AND
   ship_shipment_line.inv_no_db_id IS NOT NULL AND
   ship_shipment_line.inv_no_id IS NOT NULL
   AND
   inv_loc.loc_type_db_id = 0 AND
   inv_loc.loc_type_cd = 'DOCK';


  IF ln_IsReadyForShipping = ln_ShipmentLineCount THEN
   RETURN 1;
  END IF;

  RETURN 0;

END isReadyForShipping;
/