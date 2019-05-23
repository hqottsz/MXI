--liquibase formatted sql


--changeSet part_no_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PART_NO_PKG
IS

/********************************************************************************
*
* Function:      getMonthlyDemandForPart
* Arguments:     aPartDbId, aPartId - pk for the part
* Description:   This function calculates the monthly demand for the given part by
*                counting the number of installed inventories for that part within
*                the past year and dividing the sum by 12 months. If a part has not been
*                issued within the past year, we assign it 1 issue for that year.
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getMonthlyDemandForPart(
    aPartDbId IN eqp_part_no.part_no_db_id%TYPE,
    aPartId   IN eqp_part_no.part_no_id%TYPE
)  RETURN FLOAT
IS
    lDemand  FLOAT;
BEGIN
         SELECT
                SUM( inv_xfer.xfer_qt )/12  AS montly_demand
           INTO
                lDemand

           FROM
                inv_inv,
                inv_xfer,
                req_part,
                evt_event
          WHERE
                inv_inv.part_no_db_id = aPartDbId AND
                inv_inv.part_no_id    = aPartId
                AND
                inv_inv.rstat_cd	= 0
                AND
                req_part.inv_no_db_id = inv_inv.inv_no_db_id AND
                req_part.inv_no_id    = inv_inv.inv_no_id
                AND
                inv_xfer.init_event_db_id = req_part.req_part_db_id AND
                inv_xfer.init_event_id    = req_part.req_part_id AND
                inv_xfer.xfer_type_db_id  = 0 AND
                inv_xfer.xfer_type_cd     = 'ISSUE'
                AND
                -- Ensure the transfer date is within the past year
                evt_event.event_db_id      = inv_xfer.xfer_db_id AND
                evt_event.event_id         = inv_xfer.xfer_id AND
                evt_event.event_type_cd    = 'LX' AND
                evt_event.hist_bool        = 1 AND
                evt_event.event_status_cd  = 'LXCMPLT' AND
                evt_event.event_dt        >= SYSDATE -365 AND
                evt_event.event_dt        <= SYSDATE;

    IF lDemand IS NULL OR lDemand = 0 THEN
       lDemand := 1/12;
    END IF;
    RETURN lDemand;
END getMonthlyDemandForPart;


/********************************************************************************
*
* Function:      getPartValue
* Arguments:     aPartDbId, aPartId - pk for the part
* Description:   This function calculates the part's value by multiplying
*                the average price times the monthly demand for the given part
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getPartValue
(
    aPartDbId IN eqp_part_no.part_no_db_id%TYPE,
    aPartId   IN eqp_part_no.part_no_id%TYPE
) RETURN FLOAT
IS
    lValue  FLOAT;
BEGIN
           SELECT
               eqp_part_no.avg_unit_price * part_no_pkg.getMonthlyDemandForPart(aPartDbId, aPartId)
           INTO
               lValue
           FROM
               eqp_part_no
           WHERE
               eqp_part_no.part_no_db_id = aPartDbId  AND
               eqp_part_no.part_no_id    = aPartId
               AND
               eqp_part_no.rstat_cd	= 0;
    IF lValue IS NULL THEN
       lValue := 0;
    END IF;
    RETURN lValue;
END getPartValue;



/********************************************************************************
*
* Function:      getTotalPartValue
* Description:   This function calculates the total part value for all part's in the table
*
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
FUNCTION getTotalPartValue
RETURN FLOAT
IS
    lTotalValue  FLOAT;
BEGIN
           SELECT
               SUM (all_parts.part_value)
           INTO
                lTotalValue
           FROM
               (
                 SELECT
                    part_no_pkg.getPartValue(eqp_part_no.part_no_db_id, eqp_part_no.part_no_id) as part_value
                 FROM
                    eqp_part_no
                 WHERE
                    eqp_part_no.avg_unit_price > 0 AND
         	    eqp_part_no.rstat_cd	= 0
               )all_parts;

    IF lTotalValue IS NULL THEN
       lTotalValue := 0;
    END IF;
    RETURN  lTotalValue;
END getTotalPartValue;

/********************************************************************************
*
* Procedure:     recalculateABCClass
* Description:   This procedure calculates the abc class value for every part in eqp_part_no.
*                If a new ABC class value is calculated, and the calc_abc_class_bool is true,
*                the procedure updates the class key and updates the inv_loc_part_count.next_count_dt
*                value.
*                ol_Return (OUT NUMBER): return 1 if success, <0 if error
* Orig.Coder:    Julie Primeau
* Recent Coder:
* Recent Date:   June 04, 2006
*
*********************************************************************************/
PROCEDURE recalculateABCClass IS

   ln_Sum                      NUMBER;
   ln_TotalValue               NUMBER;
   ln_Percent                  FLOAT;
   ln_AbcClassDbId             ref_abc_class.abc_class_db_id%TYPE;
   ln_AbcClassCd               ref_abc_class.abc_class_cd%TYPE;
   ln_AbcClassOrd              ref_abc_class.value_ord%TYPE;
   ln_AbcValuePct              ref_abc_class.value_pct%TYPE;
   ln_AbcCountIntervalMonths   NUMBER;

   -- all parts, cheepest first
   CURSOR lcur_AllParts IS
          SELECT
              eqp_part_no.part_no_db_id,
              eqp_part_no.part_no_id,
              part_no_pkg.getPartValue(eqp_part_no.part_no_db_id,  eqp_part_no.part_no_id) as part_value,
              calc_abc_class_bool,
              abc_class_cd
          FROM
              eqp_part_no
          WHERE rstat_cd = 0
          ORDER BY
              part_value ASC;
   lrec_AllParts lcur_AllParts%ROWTYPE;


   BEGIN

          ln_Sum  := 0;
          ln_TotalValue := part_no_pkg.getTotalPartValue();

          --set the abc class ord
          SELECT
              MAX(value_ord)
          INTO
              ln_AbcClassOrd
          FROM
              ref_abc_class
          WHERE rstat_cd = 0;

          --set the abc class code
          SELECT
              abc_class_db_id,
              abc_class_cd,
              value_pct,
              count_interval_months
          INTO
              ln_AbcClassDbId,
              ln_AbcClassCd,
              ln_AbcValuePct,
              ln_AbcCountIntervalMonths
          FROM
              ref_abc_class
          WHERE
              value_ord = ln_AbcClassOrd	AND
              ref_abc_class.rstat_cd	= 0;


          FOR lrec_AllParts IN lcur_AllParts LOOP

              ln_Sum := ln_Sum + lrec_AllParts.part_value;
              ln_Percent := ln_Sum / ln_TotalValue;

              -- if our sum is in the next bracket, move to the next class
              IF ln_Percent > ln_AbcValuePct THEN
                   ln_Sum := 0;
                 WHILE   (ln_Percent > ln_AbcValuePct AND ln_AbcClassOrd > 1) LOOP
                     ln_AbcClassOrd := ln_AbcClassOrd -1;
                     --set the abc class code
                     SELECT
                         abc_class_db_id,
                         abc_class_cd,
                         value_pct,
                         count_interval_months
                     INTO
                         ln_AbcClassDbId,
                         ln_AbcClassCd,
                         ln_AbcValuePct,
                         ln_AbcCountIntervalMonths
                     FROM
                         ref_abc_class
                     WHERE
                         value_ord = ln_AbcClassOrd	AND
              		 ref_abc_class.rstat_cd	= 0;
                 END LOOP;
              END IF;

              -- if our part is getting a new class, set it and update the next count date
              IF (  (lrec_AllParts.abc_class_cd != ln_AbcClassCd
                  OR lrec_AllParts.abc_class_cd IS NULL)
                    AND
                    lrec_AllParts.calc_abc_class_bool = 1) THEN
                    -- update the table
                    UPDATE
                        eqp_part_no
                    SET
                        abc_class_db_id = ln_AbcClassDbId,
                        abc_class_cd    = ln_AbcClassCd
                    WHERE
                        eqp_part_no.part_no_db_id = lrec_AllParts.part_no_db_id AND
                        eqp_part_no.part_no_id    = lrec_AllParts.part_no_id
                        AND
         		eqp_part_no.rstat_cd	= 0;

                    --update the next calc date
                    UPDATE
                        inv_loc_part_count
                    SET
                        next_count_dt = CASE WHEN last_count_dt IS NOT NULL THEN ADD_MONTHS(last_count_dt, ln_AbcCountIntervalMonths)
                                             WHEN last_count_dt IS NULL AND next_count_dt IS NULL THEN SYSDATE
                                             WHEN last_count_dt IS NULL AND next_count_dt IS NOT NULL THEN LEAST(next_count_dt, ADD_MONTHS(SYSDATE, ln_AbcCountIntervalMonths))
                                             ELSE ADD_MONTHS(SYSDATE, ln_AbcCountIntervalMonths)
                                             END
                    WHERE
                        part_no_db_id = lrec_AllParts.part_no_db_id AND
                        part_no_id    = lrec_AllParts.part_no_id AND
                        hist_bool     = 0;
              END IF;
          END LOOP;

 END recalculateABCClass;
END PART_NO_PKG;
/