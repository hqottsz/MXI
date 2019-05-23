--liquibase formatted sql


--changeSet oil_consumption_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
) RETURN NUMBER;


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
) RETURN inv_oil_status_log.oil_status_log_id%TYPE;

/********************************************************************************
*	Function:    getNextNonZeroUptake
*
*	Arguments:   aInvInvDbId      number  the inv db id
*                    aInvInvId        number  the inv no
*		     aDataTypeDbId    number  the datatype db id
*		     aDataTypeId      number  the datatype id
*
*	Return:	     A non zero oil uptake.
*	Description: This function returns an non zero oil uptake.
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
   
) RETURN FLOAT;

END OIL_CONSUMPTION_PKG;
/