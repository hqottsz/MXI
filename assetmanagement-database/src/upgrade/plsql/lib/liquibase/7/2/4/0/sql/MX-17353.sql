--liquibase formatted sql


--changeSet MX-17353:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('CALCOCREPORTRAWAVG');
END;
/

--changeSet MX-17353:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('CALCROLLINGAVG');
END;
/

--changeSet MX-17353:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('CALCWEEKLYAVGOCRATE');
END;
/

--changeSet MX-17353:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('CONVAVGPERIODTONUMDAYS');
END;
/

--changeSet MX-17353:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('GETDIMINVINSTALLEDPRIORTO');
END;
/

--changeSet MX-17353:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('GETDIMTIMEID');
END;
/

--changeSet MX-17353:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('ISREPEATHIGHOILCONSUMPTION');
END;
/

--changeSet MX-17353:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds oil consumption specific pl/sql functions and
*	procedures.
*********************************************************************************
*
*  Copyright 2010-03-18 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE OIL_CONSUMPTION_PKG
IS

/*********************        PACKAGE VARIABLES              *******************/

/*********************        PROCEDURES                     *******************/

/********************************************************************************
* Procedure:	CalcUptakeForZeroDelta
*
* Arguments:	an_InvInvDbId      number  the inv db id
*           	an_InvInvId        number  the inv no
*           	on_TotalUptakeQty  number  the calculated total update quantity
*           	on_DeltaTimeQty    number  the delta time quantity of fist non zero delta
*           	on_Return          number  the number indicating if the operation successful or not
*
* Description: 	This procedures loops through the oil status uptakes in reverse
*               chronilogical order, summing up the uptake quantities until if
*               finds a non-zero delta time quantity (the sum includes the uptake 
*               from this non-zero delta time).
*        
* Orig Coder:  	ahogan
* Date:        	Feb 22, 2010
*
*********************************************************************************/
PROCEDURE CalcUptakeForZeroDelta(
        an_InvInvDbId        IN  inv_oil_status_rate.inv_no_db_id%TYPE,
        an_InvInvId          IN  inv_oil_status_rate.inv_no_id%TYPE,
        an_DataTypeDbId      IN  inv_oil_status_rate.data_type_db_id%TYPE,
        an_DataTypeId        IN  inv_oil_status_rate.data_type_id%TYPE,
        an_EventGdt          IN  evt_event.event_gdt%TYPE,
        on_TotalUptakeQty    OUT inv_parm_data.parm_qt%TYPE,
        on_DeltaTimeQty      OUT inv_oil_status_rate.delta_time_qt%TYPE,
        on_Return            OUT NUMBER
);

/*********************        FUNCTIONS                     ********************/

/********************************************************************************
* 
* Function:   	calcOcRate
* Arguments:  	aInvInvDbId  number  the inv db id
*               aInvInvId        number  the inv no
*
* Return:	oil consumption rate, 0 if uptake is 0, null if no able to determine delta time
* Description:	This function returns the oil consumption rate based on the provided  
*               uptake and delta time values.  If the delta time is zero or negative
*               then the last non zero delta time will be retrieved and the uptake 
*               will be the sum of the uptakes since that last non zero delta time.
*
* Orig Coder:	ahogan
* Date:      	Feb 22, 2010
*
*********************************************************************************/
FUNCTION CalcOcRate(
   aUptakeQt         inv_parm_data.parm_qt%TYPE,
   aDeltaTimeQty     inv_oil_status_rate.delta_time_qt%TYPE,
   aInvInvDbId       inv_oil_status_rate.inv_no_db_id%TYPE,
   aInvInvId         inv_oil_status_rate.inv_no_id%TYPE,
   aDataTypeDbId     inv_oil_status_rate.data_type_db_id%TYPE,
   aDataTypeId       inv_oil_status_rate.data_type_id%TYPE,
   aEventGdt         evt_event.event_gdt%TYPE

) RETURN FLOAT;


/********************************************************************************
*
* Function:	isRepeatHighOilConsumption
* Arguments:	aInvDbId     - the Inventory db id
*		aInvId	     - the Inventory id
*		aStartDate   - the oil consumption date
* Description:  Given an inventory key and a start date this function checks if
*		the inventory has had multiple non-normal status between the 
*		start date and the now.
*
*		We know there has been a repeat if there is a non-normal state
*		log between the start date and the date of the latest normal
*		state log.
*
* Orig Coder:  	ahogan
* Recent Coder: srengasamy
* Recent Date:  Mar 18, 2010
*
*********************************************************************************/
FUNCTION isRepeatHighOilConsumption
(
   aInvDbId    inv_inv.inv_no_db_id%TYPE,
   aInvId      inv_inv.inv_no_id%TYPE,
   aStartDate  DATE
   
) RETURN NUMBER;


END OIL_CONSUMPTION_PKG;
/

--changeSet MX-17353:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY OIL_CONSUMPTION_PKG IS

/*********************        Local Types                    *******************/

/*********************        PROCEDURES                     *******************/

/********************************************************************************
*	Procedure:  CalcUptakeForZeroDelta
*	
*	Arguments:  an_InvInvDbId      number  the inv db id
*              an_InvInvId        number  the inv no
*              on_TotalUptakeQty  number  the calculated total update quantity
*              on_DeltaTimeQty    number  the delta time quantity of fist non zero delta
*              on_Return          number  the number indicating if the operation successful or not
*
*	Description: This procedures loops through the oil status uptakes in reverse
*               chronilogical order, summing up the uptake quantities until if
*               finds a non-zero delta time quantity (the sum includes the uptake 
*               from this non-zero delta time).
*        
*	Orig Coder:  ahogan
*	Date:        Feb 22, 2010
*********************************************************************************
*
*  Copyright 2009-09-30 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CalcUptakeForZeroDelta(
        an_InvInvDbId        IN  inv_oil_status_rate.inv_no_db_id%TYPE,
        an_InvInvId          IN  inv_oil_status_rate.inv_no_id%TYPE,
        an_DataTypeDbId      IN  inv_oil_status_rate.data_type_db_id%TYPE,
        an_DataTypeId        IN  inv_oil_status_rate.data_type_id%TYPE,
        an_EventGdt          IN  evt_event.event_gdt%TYPE,
        on_TotalUptakeQty    OUT inv_parm_data.parm_qt%TYPE,
        on_DeltaTimeQty      OUT inv_oil_status_rate.delta_time_qt%TYPE,
        on_Return            OUT NUMBER
) IS

   /* cursor declarations */
   CURSOR lcur_OcRates (
         cl_InvInvDbId   inv_inv.inv_no_id%TYPE,
         cl_InvInvId     inv_inv.inv_no_id%TYPE
      ) IS
         SELECT 
            inv_oil_status_rate.delta_time_qt AS delta_time_qt,
            inv_parm_data.parm_qt             AS uptake_parm_qt
         FROM
            inv_oil_status_rate
         INNER JOIN inv_parm_data ON
            inv_parm_data.event_db_id     = inv_oil_status_rate.event_db_id AND
            inv_parm_data.event_id        = inv_oil_status_rate.event_id AND
            inv_parm_data.event_inv_id    = inv_oil_status_rate.event_inv_id AND
            inv_parm_data.data_type_db_id = inv_oil_status_rate.data_type_db_id AND
            inv_parm_data.data_type_id    = inv_oil_status_rate.data_type_id AND
            inv_parm_data.inv_no_db_id    = inv_oil_status_rate.inv_no_db_id AND
            inv_parm_data.inv_no_id       = inv_oil_status_rate.inv_no_id
         INNER JOIN evt_event ON
            evt_event.event_db_id = inv_oil_status_rate.event_db_id AND
				evt_event.event_id    = inv_oil_status_rate.event_id AND
				evt_event.event_gdt  <= an_EventGdt
         WHERE
            inv_oil_status_rate.inv_no_db_id    = an_InvInvDbId AND
            inv_oil_status_rate.inv_no_id       = an_InvInvId AND
            inv_oil_status_rate.data_type_db_id = an_DataTypeDbId AND
            inv_oil_status_rate.data_type_id    = an_DataTypeId
         ORDER BY  
            evt_event.event_gdt DESC;
             
BEGIN

   on_Return := 0;
   
   on_TotalUptakeQty := 0;
   on_DeltaTimeQty   := 0;
   
   /* retrieve a list of all oil status uptakes for the 
      provided inventory in reverse cronological order */
   FOR lrec_Rate IN lcur_OcRates( an_InvInvDbId, an_InvInvId ) LOOP

      /* keep a running total of the uptakes 
        (for all the zero deltas and the first non zero delta) */
      on_TotalUptakeQty := on_TotalUptakeQty + lrec_Rate.uptake_parm_qt;
      
      /* we want to return the first non zero delta time */
      IF lrec_Rate.delta_time_qt > 0 THEN
         on_DeltaTimeQty := lrec_Rate.delta_time_qt;
         on_Return := 1;
      END IF;
      
      EXIT WHEN lrec_Rate.delta_time_qt > 0;
      
   END LOOP;

END CalcUptakeForZeroDelta;



/*********************        FUNCTIONS                     ********************/

/********************************************************************************
*	Function:    CalcOcRate
*
*	Arguments:   aInvInvDbId      number  the inv db id
*               aInvInvId        number  the inv no
*
*	Return:	    oil consumption rate, 0 if uptake is 0, null if no able to determine delta time
*	Description: This function returns the oil consumption rate based on the provided  
*               uptake and delta time values.  If the delta time is zero or negative
*               then the last non zero delta time will be retrieved and the uptake 
*               will be the sum of the uptakes since that last non zero delta time.
*
*	Orig Coder:  ahogan
*	Date:        Feb 22, 2010
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION CalcOcRate(
   aUptakeQt         inv_parm_data.parm_qt%TYPE,
   aDeltaTimeQty     inv_oil_status_rate.delta_time_qt%TYPE,
   aInvInvDbId       inv_oil_status_rate.inv_no_db_id%TYPE,
   aInvInvId         inv_oil_status_rate.inv_no_id%TYPE,
   aDataTypeDbId     inv_oil_status_rate.data_type_db_id%TYPE,
   aDataTypeId       inv_oil_status_rate.data_type_id%TYPE,
   aEventGdt         evt_event.event_gdt%TYPE

) RETURN FLOAT
IS
   iRate       FLOAT;
   iUptake     inv_parm_data.parm_qt%TYPE;
   iDeltaTime  inv_oil_status_rate.delta_time_qt%TYPE;
   iRetVal     NUMBER;
   
BEGIN
   iRate := NULL;
   
   IF aDeltaTimeQty <= 0 OR aDeltaTimeQty IS NULL THEN
      CalcUptakeForZeroDelta( 
         aInvInvDbId,
         aInvInvId, 
         aDataTypeDbId, 
         aDataTypeId, 
         aEventGdt,
         iUptake,
         iDeltaTime,
         iRetVal
      );
      IF NOT iRetVal = 1 THEN
         RETURN NULL;
      END IF;
   ELSE 
      iUptake := aUptakeQt;
      iDeltaTime := aDeltaTimeQty;
   END IF;
   
   IF iUptake IS NULL OR iUptake <= 0 THEN
      iRate := 0;
   ELSIF iDeltaTime IS NULL OR iDeltaTime <= 0 THEN
      iRate := NULL;
   ELSE
      iRate := iUptake / iDeltaTime;
   END IF;
      
   RETURN iRate;
END calcOcRate;


/********************************************************************************
*
* Function:	isRepeatHighOilConsumption
* Arguments:	aInvDbId     - the Inventory db id
*		aInvId	     - the Inventory id
*		aStartDate   - the oil consumption date
* Description:  Given an inventory key and a start date this function checks if
*		the inventory has had multiple non-normal status between the 
*		start date and the now.
*
*		We know there has been a repeat if there is a non-normal state
*		log between the start date and the date of the latest normal
*		state log.
*
* Orig Coder:	ahogan
* Recent Coder: srengasamy
* Recent Date:  Mar 18, 2010
*
********************************************************************************/
FUNCTION isRepeatHighOilConsumption
(
   aInvDbId    inv_inv.inv_no_db_id%TYPE,
   aInvId      inv_inv.inv_no_id%TYPE,
   aStartDate  DATE
   
) RETURN NUMBER
IS
   lIsRepeat NUMBER;
  
   
BEGIN
   lIsRepeat := 1;
   
   SELECT 
      COUNT(1)
   INTO
      lIsRepeat
   FROM 
      inv_oil_status_log
   WHERE
      inv_oil_status_log.inv_no_db_id =  aInvDbId AND
      inv_oil_status_log.inv_no_id    =  aInvId AND
      inv_oil_status_log.log_dt       >= aStartDate AND
      inv_oil_status_log.log_dt       < 
      (
         SELECT 
            log_dt AS latest_normal_log_dt
         FROM
            (
               SELECT
                  log_dt
               FROM
                  inv_oil_status_log
               WHERE
                  inv_oil_status_log.inv_no_db_id =  aInvDbId AND
                  inv_oil_status_log.inv_no_id    =  aInvId AND
                  inv_oil_status_log.log_dt >= aStartDate AND
                  inv_oil_status_log.oil_status_db_id =  0 AND
                  inv_oil_status_log.oil_status_cd    =  'NORMAL' AND
                  inv_oil_status_log.current_status_bool = 0
               ORDER BY
                  inv_oil_status_log.log_dt DESC
            )
         WHERE
            ROWNUM = 1
      )
      AND
      NOT (
         inv_oil_status_log.oil_status_db_id = 0 AND
         inv_oil_status_log.oil_status_cd    = 'NORMAL'
      )
   ;
   
   IF lIsRepeat > 1 THEN
   	lIsRepeat := 1;
   END IF;
   	
 
   RETURN lIsRepeat;

END isRepeatHighOilConsumption;


END OIL_CONSUMPTION_PKG;
/

--changeSet MX-17353:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
CREATE OR REPLACE PACKAGE OIL_CONSUMPTION_REPORT_PKG
IS

/*********************        PACKAGE VARIABLES              *******************/

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
   
) RETURN FLOAT;

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
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
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

) RETURN FLOAT;


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
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
*
*********************************************************************************/

FUNCTION calcWeeklyAvgOCRate
(
   aDimInvId          Integer,
   aDimTimeId         Integer
   
) RETURN FLOAT;


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
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
*
*********************************************************************************/

FUNCTION convAvgPeriodToNumDays
(
   aAvgPeriod   STRING
   
) RETURN INTEGER;

END OIL_CONSUMPTION_REPORT_PKG;
/

--changeSet MX-17353:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   LEFT OUTER JOIN org_carrier ON
      org_carrier.carrier_name = dim_inv.carrier_name
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

--changeSet MX-17353:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds oil consumption server job specific pl/sql functions and
*	procedures.
*********************************************************************************
*
*  Copyright 2010-03-18 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE OIL_CONSUMPTION_SERVER_JOB_PKG
IS

/*********************        PACKAGE VARIABLES              *******************/

/*********************        PROCEDURES                     *******************/

/*********************        FUNCTIONS                      *******************/

/********************************************************************************
* Function:	getDimInvInstalledPriorTo
* Arguments:	aInvDbId  - the inv db id
*           	aInvId   - the inv no
*           	aDate  - the date
* Return:	dim_inv_id
*
* Description: 	This function returns the dim_inv_id for the provided Date.
*        
* Orig Coder:  	Al Hogan
* Recent Coder: srengasamy
* Recent Date: 	Mar 18, 2010
*
********************************************************************************/
FUNCTION getDimInvInstalledPriorTo
(
   aInvDbId   Integer,
   aInvId     Integer,
   aDate      DATE
   
) RETURN FLOAT;


/********************************************************************************
*
* Function:      getDimTimeId
* Arguments:     aDate - date to look up in the dim_time table
* Returns:       dim_time key
* Description:   This function returns the corresponding dim_time key
*                (dim_time_id) for the provided Date.
*
* Orig.Coder:    Al Hogan
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
*
********************************************************************************/

FUNCTION getDimTimeId
(
   aDate    DATE
   
) RETURN INTEGER;


END OIL_CONSUMPTION_SERVER_JOB_PKG;
/

--changeSet MX-17353:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY OIL_CONSUMPTION_SERVER_JOB_PKG IS

/*********************        Local Types                    *******************/

/*********************        PROCEDURES                     *******************/

/*********************        FUNCTIONS                      *******************/

/********************************************************************************
* Function:	getDimInvInstalledPriorTo
* Arguments:	aInvDbId  - the inv db id
*           	aInvId    - the inv no
*           	aDate     - the date
* Return:	dim_inv_id
*
* Description: 	This function returns the dim_inv_id for the provided Date.
*        
* Orig Coder:  	Al Hogan
* Recent Coder: srengasamy
* Recent Date: 	Mar 18, 2010
*
********************************************************************************/
FUNCTION getDimInvInstalledPriorTo
(
   aInvDbId   Integer,
   aInvId     Integer,
   aDate      DATE
   
) RETURN FLOAT
IS
   lDimInvId Integer;
  
BEGIN
   lDimInvId := NULL;
   
   SELECT 
      dim_inv_id
   INTO
      lDimInvId
   FROM 
      (
         SELECT 
            dim_inv.dim_inv_id
         FROM
            dim_inv 
         WHERE
            dim_inv.inv_no_db_id = aInvDbId AND
            dim_inv.inv_no_id    = aInvId
            AND
            dim_inv.install_gdt <= aDate
         ORDER BY 
            dim_inv.install_gdt DESC
      ) 
   WHERE 
      ROWNUM = 1        
   ;

   RETURN lDimInvId;
   
END getDimInvInstalledPriorTo;


/********************************************************************************
*
* Function:      getDimTimeId
* Arguments:     aDate - date to look up in the dim_time table
* Returns:       dim_time key
* Description:   This function returns the corresponding dim_time key
*                (dim_time_id) for the provided Date.
*
* Orig.Coder:    Al Hogan
* Recent Coder:  srengasamy
* Recent Date:   Mar 18, 2010
*
********************************************************************************/

FUNCTION getDimTimeId
(
   aDate    DATE
   
) RETURN INTEGER
IS
   lDimeTimeId INTEGER;
  
BEGIN
   lDimeTimeId := NULL;
   
   SELECT
      dim_time.dim_time_id
   INTO
      lDimeTimeId
   FROM
      dim_time
   WHERE
      dim_time.day_timestamp = to_char(aDate, 'YYYY-MM-DD') || ' 00:00:00'
   ;

   RETURN lDimeTimeId;
   
END getDimTimeId;


END OIL_CONSUMPTION_SERVER_JOB_PKG;
/