--liquibase formatted sql


--changeSet verifyMonthDate:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      verifyMonthDate
* Arguments:     aMonth          The selected Month [1-12]
*                aDay            The selected Date  [1-31]
*                aErrorNum       The error number if an error occurs
*                aErrorMsg       The error message if an error occurs
*
* Description:   This function verifies that the Date provided is valid for the
*                given month. E.g. 2, 30 (February 30th) is invalid.
*                The error number and message is set to the aErrorNum and
*                aErrorMsg variables.
*
* Orig.Coder:    Jonathan Clarkin
* Recent Coder:
* Recent Date:   December 19, 2006
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE verifyMonthDate
(
   aMonth    IN OUT NUMBER,
   aDay      IN OUT NUMBER,
   aErrorNum IN OUT NUMBER,
   aErrorMsg IN OUT VARCHAR2
)
IS
  lError BOOLEAN := false;
BEGIN

  IF       aMonth = 2  AND aDay > 28 THEN lError := true;
     ELSIF aMonth = 4  AND aDay > 30 THEN lError := true;
     ELSIF aMonth = 6  AND aDay > 30 THEN lError := true;
     ELSIF aMonth = 9  AND aDay > 30 THEN lError := true;
     ELSIF aMonth = 11 AND aDay > 30 THEN lError := true;
     ELSIF                 aDay > 31 THEN lError := true;
  END IF;

  IF lError THEN
     aErrorNum := -20008;
     aErrorMsg := 'Date ' || aDay || ' is invalid for Month ' || aMonth || '.';
  END IF;

END verifyMonthDate;
/