--liquibase formatted sql


--changeSet isSpecialHandling:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isSpecialHandling
* Arguments:     aShipmentDbId    - shipment db id
*                aShipmentId      - shipment id
* Description:   This function determines if any of the shipment lines of a shipment
*		 have special handling. A part number is considered to have special
*		 handling if any of these fields have a value:
*		 eqp_part_no.hazmat_cd,
*		 eqp_part_no.dg_ref_sdesc,
*		 eqp_part_no.shipping_instr_cd,
*		 eqp_part_no.shipping_ldesc,
*		 eqp_part_no.packaging_instr_cd,
*		 eqp_part_no.packaging_ldesc,
*		 eqp_part_no.storage_instr_cd,
*		 eqp_part_no.storage_ldesc
*
* Orig.Coder:    Elise Do
* Recent Coder:  
* Recent Date:   July 12, 2007
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isSpecialHandling
(
 aShipmentDbId   NUMBER,
 aShipmentId    NUMBER
 ) RETURN NUMBER
 IS
   ln_IsSpecialHandling NUMBER;
 BEGIN
SELECT
  COUNT (*)
INTO
  ln_IsSpecialHandling
 FROM
   ship_shipment_line,
   inv_inv,
   eqp_part_no
 WHERE
   ship_shipment_line.shipment_db_id = aShipmentDbId AND
   ship_shipment_line.shipment_id    = aShipmentId
   AND
   ship_shipment_line.rstat_cd	= 0
   AND
   inv_inv.inv_no_db_id (+)= ship_shipment_line.inv_no_db_id AND
   inv_inv.inv_no_id    (+)= ship_shipment_line.inv_no_id
   AND
         (
            (
               eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
               eqp_part_no.part_no_id    = inv_inv.part_no_id
            )
            OR
            (
               inv_inv.inv_no_id IS NULL AND
               eqp_part_no.part_no_db_id = ship_shipment_line.part_no_db_id AND
               eqp_part_no.part_no_id    = ship_shipment_line.part_no_id
            )
         )
   AND COALESCE(
               eqp_part_no.hazmat_cd,
               eqp_part_no.dg_ref_sdesc,
               eqp_part_no.shipping_instr_cd,
               eqp_part_no.shipping_ldesc,
               eqp_part_no.packaging_instr_cd,
               eqp_part_no.packaging_ldesc,
               eqp_part_no.storage_instr_cd,
               eqp_part_no.storage_ldesc
         )  IS NOT NULL;


  IF ln_IsSpecialHandling >= 1 THEN
   RETURN 1;
  END IF;

  RETURN 0;

END isSpecialHandling;
/