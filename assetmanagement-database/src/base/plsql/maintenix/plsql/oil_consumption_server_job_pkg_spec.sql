--liquibase formatted sql


--changeSet oil_consumption_server_job_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

/********************************************************************************
* Procedure:   populateDimTimeTable
* Arguments:   aEndDate  - the final date to be populated
*
* Description: This procedure populates the dim_time table with records
*              representing the days between the tables current last record
*              and the provided end day (up to and including this end day).
*
********************************************************************************/
PROCEDURE populateDimTimeTable
(
   aEndDate  DATE
);

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