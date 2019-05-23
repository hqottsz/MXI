--liquibase formatted sql


--changeSet oil_consumption_report_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds oil consumption report specific pl/sql functions and
*	procedures.
*********************************************************************************
*
*  Copyright 2010-03-18 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE BODY OIL_CONSUMPTION_REPORT_PKG IS

/*********************        Local Types                    *******************/

/*********************        PROCEDURES                     *******************/

/*********************        FUNCTIONS                      *******************/

/********************************************************************************
*
* Function:      calcOcReportRawAvg
* Arguments:     aFctInvOilId  - integer representing a inventory oil id.
* Returns:       Raw average.
* Description:   This function returns the raw average of oil consumption.
*
* Orig.Coder:    Al Hogan
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
*
*********************************************************************************/
FUNCTION calcOcReportRawAvg
(
   aFctInvOilId   INTEGER

) RETURN FLOAT
IS
   lRawAvg FLOAT;

   lOilQt FLOAT;
   lDeltaTimeQt FLOAT;
   lDimInvId INTEGER;
   lEventGdt DATE;

   CURSOR lcur_PrevUptakes (
         cl_DimInvId   fct_inv_oil.dim_inv_id%TYPE,
         cl_EventGdt   fct_inv_oil.event_gdt%TYPE
      ) IS
         SELECT
         fct_inv_oil.fct_inv_oil_id AS fct_inv_oil_id, fct_inv_oil.event_gdt AS event_gdt,
            fct_inv_oil.oil_qt AS oil_qt,
            fct_inv_oil.delta_time_qt AS delta_time_qt
         FROM
            fct_inv_oil
         WHERE
            fct_inv_oil.dim_inv_id = cl_DimInvId AND
            fct_inv_oil.event_gdt <= cl_EventGdt
         ORDER BY
            fct_inv_oil.event_gdt DESC;

BEGIN
   lRawAvg := NULL;

   SELECT
      fct_inv_oil.oil_qt,
      fct_inv_oil.delta_time_qt,
      fct_inv_oil.dim_inv_id,
      fct_inv_oil.event_gdt
   INTO
      lOilQt,
      lDeltaTimeQt,
      lDimInvId,
      lEventGdt
   FROM
      fct_inv_oil
   WHERE
      fct_inv_oil.fct_inv_oil_id = aFctInvOilId
   ;

   IF lDeltaTimeQt <= 0 THEN
      -- the delta time quantity is zero or negative thus we have to calculate the average using
      -- the first previous uptake with a non-zero delta time and the sum of the oil quantities
      -- from that uptake until this uptake

      lOilQt := 0;

      FOR lrec_Uptake IN lcur_PrevUptakes( lDimInvId, lEventGdt ) LOOP

         lOilQt := lOilQt + lrec_Uptake.oil_qt;

         IF lrec_Uptake.delta_time_qt > 0 THEN
            lDeltaTimeQt := lrec_Uptake.delta_time_qt;
         END IF;

         EXIT WHEN lrec_Uptake.delta_time_qt > 0;

      END LOOP;
   END IF;

   IF nvl( lOilQt, 0 ) <= 0 THEN
      -- if the oil quantity is null, zero, or negative then return 0 as raw average
      lRawAvg := 0;
   ELSIF lDeltaTimeQt <= 0 THEN
      -- if the delta time quantity is still zero or negative then return null as raw average
      lRawAvg := NULL;
   ELSE
      -- since the oil quantity and delta time quantity are positive, non zero values
      -- then calculate the average
      lRawAvg := lOilQt / lDeltaTimeQt;
   END IF;

   RETURN lRawAvg;

END calcOcReportRawAvg;


/********************************************************************************
*
* Function:      calcRollingAvg
* Arguments:     aSerialNo       - serial no. of the inventory
*                aDate           - end date of the calculation period
*                aNumOfDaysPrior - num of days prior to end date (start of calc period)
* Returns:       Average oil conumption rate.
* Description:   This function calculates the average oil conumption rate for
*                an inventory with the given Serial No.
*                The time period will end on the given end date and
*                will start on this date minus the given num of days prior.
*
* Orig.Coder:    Al Hogan
* Recent Coder:
* Recent Date:   Nov 29, 2009
*
*********************************************************************************/

FUNCTION calcRollingAvg
(
   aRollingAvgName    STRING,
   aRollingAvgValue   STRING,
   aAcAssemblyCd      STRING,
   aAssemblyCd        STRING,
   aDate              Date,
   aNumOfDaysPrior    INTEGER,
   aCurrentUserId     INTEGER

) RETURN FLOAT
IS
   lTotalOilQt FLOAT;
   lTotalDeltaTimeQt FLOAT;
   lAvg FLOAT;

BEGIN
   lAvg := NULL;

   SELECT
      SUM(fct_inv_oil.oil_qt),
      SUM(fct_inv_oil.delta_time_qt)
   INTO
      lTotalOilQt,
      lTotalDeltaTimeQt
   FROM
      fct_inv_oil
   INNER JOIN dim_inv ON
      dim_inv.dim_inv_id = fct_inv_oil.dim_inv_id
      AND
      dim_inv.ac_assmbl_cd = aAcAssemblyCd AND
      dim_inv.assmbl_cd    = aAssemblyCd
   INNER JOIN org_hr ON
      org_hr.user_id = aCurrentUserId
   LEFT OUTER JOIN org_org ON
         org_org.org_sdesc = dim_inv.carrier_name
      LEFT OUTER JOIN org_carrier ON
         org_carrier.org_db_id = org_org.org_db_id and
         org_carrier.org_id = org_org.org_id
      WHERE
         fct_inv_oil.dim_time_id IN (
            SELECT
               dim_time.dim_time_id
            FROM
               dim_time
            WHERE
               dim_time.day_timestamp >= to_char(aDate - aNumOfDaysPrior, 'YYYY-MM-DD') || ' 00:00:00' AND
               dim_time.day_timestamp <= to_char(aDate, 'YYYY-MM-DD') || ' 00:00:00'
         )
         AND
         aRollingAvgValue =
            CASE
               WHEN aRollingAvgName = 'Assembly' THEN aRollingAvgValue
               WHEN aRollingAvgName = 'SerialNo' THEN dim_inv.serial_no_oem
               WHEN aRollingAvgName = 'Operator' THEN dim_inv.carrier_name
               WHEN aRollingAvgName = 'PartNo'   THEN dim_inv.part_no_oem
            END
         AND
         isAuthorizedForAssembly(org_hr.hr_db_id, org_hr.hr_id, dim_inv.ac_assmbl_db_id, dim_inv.ac_assmbl_cd) = 1
         AND
         isAuthorizedForInv(org_hr.hr_db_id, org_hr.hr_id, dim_inv.inv_no_db_id, dim_inv.inv_no_id) = 1
         AND
         isAuthorizedForOperator(org_hr.hr_db_id, org_hr.hr_id, org_carrier.carrier_db_id, org_carrier.carrier_id) = 1
   ;

   IF ( lTotalDeltaTimeQt > 0 ) THEN
      lAvg := ( lTotalOilQt / lTotalDeltaTimeQt );
   END IF;

   RETURN lAvg;

END calcRollingAvg;


/********************************************************************************
*
* Function:      calcWeeklyAvgOCRate
* Arguments:     aDimInvId  - id of dim_inv record to determine the inventory
*                aDimTimeId - id of dim_time record to determine the week
* Returns:       the average oil uptake rage for the week
* Description:   This function returns the average oil update rate for the given
*                inventory over the week in which the given time id falls.
*
* TODO: make work for weeks that extend into other years
*       maybe have 2 more dim_week_records and only populate them if
*       (count of week_numberinyear is < 7) and week is either 1 or 52.
*       if week 1, use previous year's last week or if week 52, use next years week 1
*       then do an AND in the where cluase using all 3 dim_week_records
*
* Orig.Coder:    Al Hogan
* Recent Coder:
* Recent Date:   Nov 29, 2009
*
*********************************************************************************/

FUNCTION calcWeeklyAvgOCRate
(
   aDimInvId          Integer,
   aDimTimeId         Integer

) RETURN FLOAT
IS
   lAvgRate FLOAT;

BEGIN
   lAvgRate := NULL;

   SELECT
      decode(SUM(fct_inv_oil.delta_time_qt), 0, 0, (SUM(fct_inv_oil.oil_qt) / SUM(fct_inv_oil.delta_time_qt))) AS avg_rate
   INTO
      lAvgRate
   FROM
      fct_inv_oil,
      dim_time,
      (
        SELECT
           *
        FROM
           dim_time
        WHERE
           dim_time.dim_time_id = aDimTimeId
      ) dim_week_record

   WHERE
      fct_inv_oil.dim_inv_id = aDimInvId
      AND
      dim_time.dim_time_id = fct_inv_oil.dim_time_id
      AND
      dim_time.year_key = dim_week_record.year_key AND
      dim_time.week_numberinyear = dim_week_record.week_numberinyear
   ;

   RETURN lAvgRate;

END calcWeeklyAvgOCRate;


/********************************************************************************
*
* Function:      convAvgPeriodToNumDays
* Arguments:     aAvgPeriod  - string representing a rolling avg period
* Returns:       Number of days the period represents.
* Description:   This function returns the number of days that the provided
*                rolling average period represents.
*                   '3-months'  =  91 days (default)
*                   '6-months'  = 182 days
*                   '12-months' = 365 days
*
* Orig.Coder:    Al Hogan
* Recent Coder:
* Recent Date:   Nov 29, 2009
*
*********************************************************************************/

FUNCTION convAvgPeriodToNumDays
(
   aAvgPeriod   STRING

) RETURN INTEGER
IS
   lNumDays INTEGER;

BEGIN
   lNumDays :=
      CASE aAvgPeriod
         WHEN '6-months' THEN 182
         WHEN '12-months' THEN 365
         ELSE 91
      END;

   RETURN lNumDays;

END convAvgPeriodToNumDays;


END OIL_CONSUMPTION_REPORT_PKG;
/