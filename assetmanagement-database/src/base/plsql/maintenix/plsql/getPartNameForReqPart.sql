--liquibase formatted sql


--changeSet getPartNameForReqPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getPartNameForReqPart
* Arguments:     aReqPartDbId, aReqPartId - pk for the part request.
* Description:   This function retrieves the name of a part request. The name can
*                can come from one of 3 sources, depending on how the part
*                request was constructed. It can either come from the specific 
*                part, the bom part, or the stock.
*
* Orig.Coder:    nso
* Recent Coder:  
* Recent Date:   Apr 25, 2005
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getPartNameForReqPart
(
    aReqPartDbId number,
    aReqPartId number
) RETURN VARCHAR2
IS
       lPartName     VARCHAR2(2000);
       lBomPartName  VARCHAR2(2000);
       lStockName    VARCHAR2(2000);
BEGIN
     SELECT
         decode( eqp_part_no.part_no_oem, null, null, eqp_part_no.part_no_oem || ' (' || eqp_part_no.part_no_sdesc || ')' ),
         decode( eqp_bom_part.bom_part_cd, null, null, eqp_bom_part.bom_part_cd || ' (' || eqp_bom_part.bom_part_name || ')' ),
         decode( eqp_stock_no.stock_no_cd, null, null, eqp_stock_no.stock_no_cd || ' (' || eqp_stock_no.stock_no_name || ')' )
     INTO
         lPartName,
         lBomPartName,
         lStockName
     FROM
         req_part,
         eqp_part_no,
         eqp_bom_part,
         eqp_stock_no
     WHERE
         req_part.req_part_db_id = aReqPartDbId AND
         req_part.req_part_id    = aReqPartId	AND
         req_part.rstat_cd	= 0
         AND
         eqp_part_no.part_no_db_id (+)= req_part.req_spec_part_no_db_id AND
         eqp_part_no.part_no_id    (+)= req_part.req_spec_part_no_id
         AND
         eqp_bom_part.bom_part_db_id (+)= req_part.req_bom_part_db_id AND
         eqp_bom_part.bom_part_id    (+)= req_part.req_bom_part_id
         AND
         eqp_stock_no.stock_no_db_id (+)= req_part.req_stock_no_db_id AND
         eqp_stock_no.stock_no_id    (+)= req_part.req_stock_no_id;

	  /* use the specific part first if possible */
     IF lPartName IS NOT NULL THEN
        RETURN lPartName;
     END IF;

     /* next check the bom part name */
     IF lBomPartName IS NOT NULL THEN
        RETURN lBomPartName;
     END IF;

	  /* default to the stock name if nothing else was found */
     RETURN lStockName;
END getPartNameForReqPart;
/