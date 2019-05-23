--liquibase formatted sql


--changeSet getQtyUnitCdForBomPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getQtyUnitCdForBomPart
* Arguments:     aBomPartDbId, aBomPartId - pk for the bom part
* Description:   This function retrieves the quantity unit code associated with
*                a bom part. It does this by using the quantity unit code
*                from the bom parts standard part.
*
* Orig.Coder:    nso
* Recent Coder:  
* Recent Date:   Apr 25, 2005
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getQtyUnitCdForBomPart
(
    aBomPartDbId NUMBER,
    aBomPartId NUMBER
) RETURN VARCHAR2
IS
    lUnitCd  VARCHAR2(8);
BEGIN
     SELECT
         eqp_part_no.qty_unit_cd
     INTO
         lUnitCd
     FROM
         eqp_part_no,
         eqp_part_baseline
     WHErE
          eqp_part_baseline.bom_part_db_id = aBomPartDbId AND
          eqp_part_baseline.bom_part_id    = aBomPartId
          AND
          eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
          eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id AND
          eqp_part_baseline.standard_bool = 1
          AND
          eqp_part_no.rstat_cd	= 0;

    RETURN lUnitCd;
END getQtyUnitCdForBomPart;
/