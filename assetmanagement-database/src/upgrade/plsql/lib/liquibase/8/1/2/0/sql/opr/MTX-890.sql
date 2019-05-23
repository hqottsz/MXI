--liquibase formatted sql


--changeSet MTX-890:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet MTX-890:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY OIL_CONSUMPTION_SERVER_JOB_PKG IS

/*********************        Local Types                    *******************/

/*********************        PROCEDURES                     *******************/

PROCEDURE populateDimTimeTable
(
   aEndDate  DATE
)
IS
   lLastDate  DATE;
   lDate      DATE;
   
   TYPE TimeListType IS TABLE OF dim_time%ROWTYPE;
   lTimeList  TimeListType := TimeListType();
   
   lTimeRec   dim_time%ROWTYPE;
     
BEGIN

   -- get the date from the latest dim_time row
   SELECT
      to_date( MAX( day_timestamp ), 'YYYY-MM-DD HH24:MI:SS' )
   INTO
      lLastDate
   FROM
      dim_time
   ;

   IF ( lLastDate IS NULL ) THEN
      -- if table is empty then start populating from feature start date (Jan 1, 2009)
      -- (should never be the case)
      lLastDate := to_date( '2008-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS' );
   END IF;
   
   -- start with the day after the last one in the table
   lDate := lLastDate + 1;

   -- create records for all days until the end date
   WHILE ( lDate <= aEndDate ) LOOP

      lTimeRec.dim_time_id        := to_number( to_char( lDate, 'YYYYMMDD' ), '99999999' );
      lTimeRec.day_timestamp      := to_char( lDate, 'YYYY-MM-DD' ) || ' 00:00:00';
      lTimeRec.day_numberinweek   := to_number( to_char( lDate, 'D' ), '9' );
      lTimeRec.day_numberinmonth  := to_number( to_char( lDate, 'DD' ), '99' );
      lTimeRec.day_numberinyear   := to_number( to_char( lDate, 'DDD' ), '999' );
      lTimeRec.week_numberinyear  := to_number( to_char( lDate, 'IW' ), '99' );
      lTimeRec.month_numberinyear := to_number( to_char( lDate, 'MM' ), '99' );
      lTimeRec.year_key           := to_number( to_char( lDate, 'YYYY' ), '9999' );
      lTimeRec.rstat_cd           := 0;
      lTimeRec.creation_dt        := SYSDATE;
      lTimeRec.revision_dt        := SYSDATE;
      lTimeRec.revision_db_id     := 0;
      lTimeRec.revision_user      := 'MXI';
      
      lTimeList.extend;
      lTimeList( lTimeList.count ) := lTimeRec;
     
      lDate := lDate + 1;
      
   END LOOP;
   
   -- perform a bulk insert (better performance when called from plsql)
   FORALL i IN lTimeList.FIRST .. lTimeList.LAST
      INSERT INTO dim_time VALUES lTimeList(i);
   

END populateDimTimeTable;


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