--liquibase formatted sql


--changeSet getDecimalPlacesQtForBomPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getDecimalPlacesQtForBomPart
* Arguments:     aBomPartDbId, aBomPartId - pk for the bom part
* Description:   This function retrieves the decimal places qt associated with
*                a bom part. It does this by using the decimal places qt
*                from the bom parts standard part.
*
* Orig.Coder:    Jonathan Clarkin
* Recent Coder:  
* Recent Date:   July 7, 2005
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getDecimalPlacesQtForBomPart
(
    aBomPartDbId NUMBER,
    aBomPartId NUMBER
) RETURN FLOAT
IS
    lDecimal  FLOAT;
BEGIN
     SELECT
         ref_qty_unit.decimal_places_qt
     INTO
         lDecimal
     FROM
         eqp_part_no,
         eqp_part_baseline,
         ref_qty_unit
     WHErE
          eqp_part_baseline.bom_part_db_id = aBomPartDbId AND
          eqp_part_baseline.bom_part_id    = aBomPartId
          AND
          eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
          eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id AND
          eqp_part_baseline.standard_bool = 1
          AND
          eqp_part_no.qty_unit_db_id = ref_qty_unit.qty_unit_db_id AND
          eqp_part_no.qty_unit_cd    = ref_qty_unit.qty_unit_cd	   AND
          eqp_part_no.rstat_cd	     = 0;
    RETURN lDecimal;
END getDecimalPlacesQtForBomPart;
/