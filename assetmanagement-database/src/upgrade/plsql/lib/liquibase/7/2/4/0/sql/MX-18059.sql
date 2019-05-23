--liquibase formatted sql


--changeSet MX-18059:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds shift specific pl/sql functions and procedures.
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE OIL_CONSUMPTION_PKG
IS

/*********************        PACKAGE VARIABLES              *******************/

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
);

/*********************        FUNCTIONS                     ********************/

/********************************************************************************
*	Function:    calcOcRate
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

) RETURN FLOAT;


END OIL_CONSUMPTION_PKG;
/

--changeSet MX-18059:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

   
END OIL_CONSUMPTION_PKG;
/

--changeSet MX-18059:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION calcOcReportRawAvg
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
/