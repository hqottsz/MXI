--liquibase formatted sql


--changeSet IsInShiftWithDate:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     isInShiftWithDate
* Arguments:    aEventStartGdt   - event start date
*               aEventEndGdt     - event end date
*               aDate 	         - date of the shift
*               aTimeZone        - time zone of the shift
*               aShiftStartHr    - shift start hour
*               aShiftDurationHr - shift duration
* Return:       (VARCHAR2)       - 1 - event overlaps with the shift, 0 - otherwise
*
* Description:  Determines if the provided event overlaps with the provided shift 
*               information. It is assumed that the event dates are expressed in 
*               the default timezone, and the shift hour is defined in the provided 
*               timezone. 
* Orig.Coder:   A. Smolko
* Recent Coder: 
* Recent Date:  May 24, 2012
*
*********************************************************************************
*
* Copyright © 2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isInShiftWithDate
  (
     aEventStartGdt   DATE,
     aEventEndGdt     DATE,
     aDate 	      DATE,
     aTimeZone        utl_timezone.timezone_cd%TYPE,
     aShiftStartHr    NUMBER,
     aShiftDurationHr NUMBER

  ) RETURN NUMBER IS

   lShiftStartHr    VARCHAR2 (10);
   lShiftStart      TIMESTAMP;
   lShiftEnd        TIMESTAMP;
   lDefaultTimeZone utl_timezone.timezone_cd%TYPE;

BEGIN
   /* Convert the float hour value to string representation */
   SELECT lpad(trunc(aShiftStartHr), 2, '0')||':'||lpad(round((aShiftStartHr-trunc(aShiftStartHr))*60), 2, '0')
      INTO lShiftStartHr
   FROM dual;

   /* Find out shift start date based on the provided information */
   lShiftStart := to_timestamp( to_char(aDate,'DD-Mon-YYYY' )||lShiftStartHr||':00','DD-Mon-YYYY HH24:Mi:SS');

   /* Get the default timezone, the timezone of the provided event dates */
   SELECT timezone_cd
      INTO lDefaultTimeZone
      FROM utl_timezone
   WHERE default_bool=1;

   /* convert the provided date and shift start hr values into a date adjusted from the specified timezone to the server timezone */
   lShiftStart:= cast(FROM_TZ(lShiftStart, aTimeZone) AT TIME ZONE lDefaultTimeZone as timestamp) ;


   /* find the shift end timestamp */
   lShiftEnd := lShiftStart + ( aShiftDurationHr / 24 ) ;

   /* compare the provided event dates with the shift date */
   IF aEventStartGdt <= lShiftEnd AND
      aEventEndGdt   >= lShiftStart THEN
      RETURN 1;
   END IF;

   RETURN 0;

END isInShiftWithDate;
/