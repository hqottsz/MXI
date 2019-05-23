--liquibase formatted sql


--changeSet oil_consumption_report_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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