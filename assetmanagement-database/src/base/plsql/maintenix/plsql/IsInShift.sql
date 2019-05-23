--liquibase formatted sql


--changeSet IsInShift:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isInShift
* Arguments:     aEventStartGdt   - event start date
*                aEventEndGdt     - event end date
*                aEventTimeZone   - event time zone to be applied to shift start hr
*		 aShiftStartHr    - shift start hour
*                aShiftDurationHr - shift duration
* Description:   This function determines if the event with the specified start and 
*                end dates falls within the shift with the provided start hour and 
*                duration. It is assumed that the event dates are in default time zone.
*                The shift start hour is provided in event's timezone and has to be 
*                converted to the default timezone
*
* Orig.Coder:    Andrei Smolko
* Recent Coder:  
* Recent Date:   May 17, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isInShift
(
   aEventStartGdt   DATE,
   aEventEndGdt     DATE,
   aEventTimeZone   utl_timezone.timezone_cd%TYPE,
   aShiftStartHr    NUMBER,
   aShiftDurationHr NUMBER

) RETURN NUMBER
IS
  lEventStartDate TIMESTAMP;
  lResult         NUMBER;
BEGIN
  lEventStartDate := TRUNC( aEventStartGdt );

  lResult := isInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate, aEventTimeZone, aShiftStartHr, aShiftDurationHr);
  IF lResult = 1 THEN
     RETURN 1;
  END IF;

  -- Move the shift start date by one day and check again to handle the case when workpackage spans two days and the workpackage
  -- start date is different than the start date of the shift that the workpackage overlaps with
  -- (i.e. 1:00-3:00 shift and the workpackage is 22:00-6:00)
  lResult := isInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate+1, aEventTimeZone, aShiftStartHr, aShiftDurationHr);
  IF lResult = 1 THEN
     RETURN 1;
  END IF;

  -- Move the shift by one day and check again to handle the case when shift spans two days and the workpackage start date is
  -- different than the start date of the shift that the workpackage overlaps with
  -- (i.e. 22:00-6:00 shift and the workpackage is 1:00-3:00)
  RETURN isInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate-1, aEventTimeZone, aShiftStartHr, aShiftDurationHr);

END isInShift;
/