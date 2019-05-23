--liquibase formatted sql


--changeSet oil_consumption_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

/********************************************************************************
*	
* Function:     checkForOCAssemblyMeasurement 
*
* Arguments:    aDataTypeDbId 	number  the data type db id
*               aDataTypeId   	number  the data type id
*
* Return:       boolean, 1 if it is oil consumption Assembly Measurement,
*		 other wise 0
* Description:  This function returns the boolean, 1 if it is oil consumption
*		 Assembly Measurement, other wise 0.
*
* Orig Coder:   srengasamy
* Recent Date:  Apr 01,2010
*********************************************************************************/
FUNCTION checkForOCAssemblyMeasurement(
   aDataTypeDbId     ref_data_type_assmbl_class.data_type_db_id%TYPE,
   aDataTypeId       ref_data_type_assmbl_class.data_type_id%TYPE
) RETURN NUMBER
IS
   /** DECLARE LOCAL VARIABLES  */
   ln_count  		NUMBER;
   
BEGIN
   /** Get the Oil Consumption Definition Assigned Measurement count*/
   SELECT
       COUNT(*) INTO ln_count
   FROM
       eqp_assmbl_bom_oil
   WHERE
       eqp_assmbl_bom_oil.oil_data_type_db_id = aDataTypeDbId
       AND
       eqp_assmbl_bom_oil.oil_data_type_id = aDataTypeId;
            
   IF ln_count > 0 THEN
       return 1;
   ELSE
       return 0;
   END IF;    
     
END checkForOCAssemblyMeasurement;



/********************************************************************************
*	Function:    getOilRateSnapshot
*
*	Arguments:   aInvInvDbId      number  the inv db id
*                    aInvInvId        number  the inv no
*
*	Return:	     oil consumption rate snapshot
*	Description: This function returns the oil consumption rate snapshot.
*
*	Orig Coder:  srengasamy
*	Date:        Aug 13, 2010
*********************************************************************************/
FUNCTION getOilRateSnapshot(
   aInvInvDbId       inv_oil_status_rate.inv_no_db_id%TYPE,
   aInvInvId         inv_oil_status_rate.inv_no_id%TYPE,
   aOilStatusLogId   inv_oil_status_log.oil_status_log_id%TYPE
) RETURN inv_oil_status_log.oil_status_log_id%TYPE
IS
/** DECLARE LOCAL VARIABLES  */
   ln_oil_rate_snapshot_qt	inv_oil_status_log.oil_status_log_id%TYPE;
BEGIN
    SELECT
       oil_rate_snapshot_qt INTO ln_oil_rate_snapshot_qt
    FROM
       inv_oil_status_log
    WHERE
       inv_oil_status_log.inv_no_db_id = aInvInvDbId
       AND
       inv_oil_status_log.inv_no_id = aInvInvId
       AND
       inv_oil_status_log.oil_status_log_id = aOilStatusLogId;
       
    return ln_oil_rate_snapshot_qt;
END getOilRateSnapshot;



/********************************************************************************
*	Function:    getNextNonZeroUptake
*
*	Arguments:   aInvInvDbId      number  the inv db id
*                    aInvInvId        number  the inv no
*		     aDataTypeDbId    number  the datatype db id
*		     aDataTypeId      number  the datatype id
*
*	Return:	     A non zero oil uptake.
*	Description: This function returns the latest non zero oil uptake.
*
*	Orig Coder:  krangaswamy
*	Date:        Oct 12, 2010
*********************************************************************************/

FUNCTION getNextNonZeroUptake(

   aInvInvDbId       inv_oil_status_rate.inv_no_db_id%TYPE,
   aInvInvId         inv_oil_status_rate.inv_no_id%TYPE,
   aDataTypeDbId     inv_oil_status_rate.data_type_db_id%TYPE,
   aDataTypeId       inv_oil_status_rate.data_type_id%TYPE,
   aEventGdt         evt_event.event_gdt%TYPE

) RETURN FLOAT
IS

/** DECLARE LOCAL VARIABLES  */
   iUptake FLOAT;
   iDelta FLOAT;
   iRate FLOAT;
   iDate evt_event.event_gdt%TYPE;
   
 /* cursor declarations */
   CURSOR lcur_Uptake (
         aInvInvDbId       inv_oil_status_rate.inv_no_db_id%TYPE,
                 aInvInvId         inv_oil_status_rate.inv_no_id%TYPE,
                 aDataTypeDbId     inv_oil_status_rate.data_type_db_id%TYPE,
                 aDataTypeId       inv_oil_status_rate.data_type_id%TYPE
      )IS
      /* This query will return oil uptakes prior to the passed in zero uptake */
      SELECT
           inv_parm_data.parm_qt AS uptake,
           inv_oil_status_rate.delta_time_qt AS delta,
           evt_event.event_gdt AS uptake_date

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
            evt_event.event_id    = inv_oil_status_rate.event_id

         WHERE
            inv_oil_status_rate.inv_no_db_id    = aInvInvDbId AND
            inv_oil_status_rate.inv_no_id       = aInvInvId AND
            inv_oil_status_rate.data_type_db_id = aDataTypeDbId AND
            inv_oil_status_rate.data_type_id    = aDataTypeId

         ORDER BY
            evt_event.event_gdt DESC;

BEGIN

   iUptake :=0;
   iDelta :=0;
   iRate :=0;
   iDate := NULL;

   /* Get the first non zero uptake, the corresponding delta and uptake date */
   
   FOR lrec_Uptake IN lcur_Uptake ( aInvInvDbId,aInvInvId,aDataTypeDbId,aDataTypeId ) LOOP

      IF lrec_Uptake.uptake > 0 THEN
         iUptake := lrec_Uptake.uptake;
         iDelta := lrec_Uptake.delta;
         iDate := lrec_Uptake.uptake_date;
         EXIT;
      END IF;

   END LOOP;
   
  /* Get the relevant rate based on the uptake,delta and uptake date */

  iRate:= CalcOcRate(
   iUptake,
   iDelta,
   aInvInvDbId,
   aInvInvId,
   aDataTypeDbId,
   aDataTypeId,
   iDate);
 

RETURN iRate;

END getNextNonZeroUptake;

END OIL_CONSUMPTION_PKG;
/