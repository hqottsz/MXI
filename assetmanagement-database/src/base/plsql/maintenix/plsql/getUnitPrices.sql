--liquibase formatted sql


--changeSet getUnitPrices:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getUnitPrices
* Arguments:     aParGroupDbId - part group db id
*                aParGroupId   - part group id
* Description:   This function gets the unit prices (preferred vendor price,   
*                average unit price) of standard part in the specific part group
*
* Created: Libin Cai
* Date:    July 22, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getUnitPrices
(
    aParGroupDbId      IN eqp_part_baseline.bom_part_db_id%type,
    aParGroupId        IN eqp_part_baseline.bom_part_id%type
)  RETURN VARCHAR2
IS
   ls_UnitPrices       VARCHAR2(101);
   lf_PrefVendorPrice  FLOAT;
   lf_AverageUnitPrice FLOAT;

   /* find the standard part in the specific part group. */
   CURSOR lCur_StdPart IS
      SELECT
         eqp_part_no.part_no_db_id,
         eqp_part_no.part_no_id,
         eqp_part_no.avg_unit_price
      FROM
         eqp_part_baseline
         INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id       = eqp_part_baseline.part_no_db_id AND
                                   eqp_part_no.part_no_id          = eqp_part_baseline.part_no_id
      WHERE
         eqp_part_baseline.bom_part_db_id = aParGroupDbId AND
         eqp_part_baseline.bom_part_id    = aParGroupId
         AND
         eqp_part_baseline.standard_bool = 1;

    lRecStdPart lCur_StdPart%ROWTYPE;

BEGIN

    OPEN lCur_StdPart;
    FETCH lCur_StdPart INTO lRecStdPart;

    -- If no standard part found, return ''
    IF ( NOT lCur_StdPart%FOUND ) THEN
       ls_UnitPrices := 'null';
    ELSE

       /* find the preferred part vendor price of the standard part in the specific part group. */
       SELECT
          -- if not use SUM here, there maybe no record for this query. Because only one pref vendor price in one part, use SUM get the same result
          SUM (eqp_part_vendor_price.unit_price)
       INTO
          lf_PrefVendorPrice
       FROM
          eqp_part_vendor
          INNER JOIN eqp_part_vendor_price ON eqp_part_vendor_price.part_no_db_id = eqp_part_vendor.part_no_db_id AND
                                              eqp_part_vendor_price.part_no_id    = eqp_part_vendor.part_no_id
       WHERE
          eqp_part_vendor.part_no_db_id = lRecStdPart.part_no_db_id AND
          eqp_part_vendor.part_no_id    = lRecStdPart.part_no_id
          AND
          eqp_part_vendor.pref_bool = 1
          AND
          eqp_part_vendor_price.hist_bool = 0;

       -- if the preferred part vendor price is NULL, use 'null'
       IF ( lf_PrefVendorPrice IS NULL )  THEN
          ls_UnitPrices := 'null' || ',';
       ELSE
          ls_UnitPrices := lf_PrefVendorPrice || ',' ;
       END IF;

       -- if AUP is 0, use 'null'
       lf_AverageUnitPrice := lRecStdPart.avg_unit_price;
       IF ( lf_AverageUnitPrice IS NULL ) THEN
          ls_UnitPrices := ls_UnitPrices || 'null';
       ELSE
          ls_UnitPrices := ls_UnitPrices || lf_AverageUnitPrice;
       END IF;

    END IF;

    CLOSE lCur_StdPart;

    RETURN ls_UnitPrices;

END getUnitPrices;
/