--liquibase formatted sql


--changeSet shift_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY SHIFT_PKG IS

/*********************        Local Types                    *******************/


/*********************        PROCEDURES                     *******************/


/*********************        FUNCTIONS                     ********************/
/********************************************************************************
*
* Function:      calcShiftPlanStart
*
* Arguments:     aHrDbId        - HR db id
*                aHrId          - HR id
*                aHrShiftPlanId - HR shift plan id
*
* Description:   This function returns the start date (with time) of the shift
*                plan given an HR key and shift plan id.
*
* Returns:       start date of the hr shift plan
*
* Created: ahogan
* Date:    Feb 05, 2010
*
*********************************************************************************/
FUNCTION calcShiftPlanStart
(
   aHrDbId         org_hr_shift_plan.hr_db_id%TYPE,
   aHrId           org_hr_shift_plan.hr_id%TYPE,
   aHrShiftPlanId  org_hr_shift_plan.hr_shift_plan_id%TYPE
)  RETURN DATE
IS
   lStartDate DATE;
   lStartDay  DATE;
   lStartHour INT;
BEGIN
   SELECT
      org_hr_shift_plan.day_dt,
      org_hr_shift_plan.start_hour
   INTO
      lStartDay,
      lStartHour
   FROM
      org_hr_shift_plan
   WHERE
      org_hr_shift_plan.hr_db_id = aHrDbId AND
      org_hr_shift_plan.hr_id = aHrId AND
      org_hr_shift_plan.hr_shift_plan_id = aHrShiftPlanId
   ;

   lStartDate := lStartDay + (lStartHour/24);

   RETURN lStartDate;
END calcShiftPlanStart;

/********************************************************************************
*
* Function:      calcShiftPlanEnd
*
* Arguments:     aHrDbId        - HR db id
*                aHrId          - HR id
*                aHrShiftPlanId - HR shift plan id
*
* Description:   This function returns the end date (with time) of the shift
*                plan given an HR key and shift plan id.
*
* Returns:       end date of the hr shift plan
*
* Created: ahogan
* Date:    Feb 05, 2010
*
*********************************************************************************/
FUNCTION calcShiftPlanEnd
(
   aHrDbId         org_hr_shift_plan.hr_db_id%TYPE,
   aHrId           org_hr_shift_plan.hr_id%TYPE,
   aHrShiftPlanId  org_hr_shift_plan.hr_shift_plan_id%TYPE
)  RETURN DATE
IS
   lEndDate   DATE;
   lStartDay  DATE;
   lStartHour INT;
   lDuration  FLOAT;
BEGIN
   SELECT
      org_hr_shift_plan.day_dt,
      org_hr_shift_plan.start_hour,
      org_hr_shift_plan.duration_qt
   INTO
      lStartDay,
      lStartHour,
      lDuration
   FROM
      org_hr_shift_plan
   WHERE
      org_hr_shift_plan.hr_db_id = aHrDbId AND
      org_hr_shift_plan.hr_id = aHrId AND
      org_hr_shift_plan.hr_shift_plan_id = aHrShiftPlanId
   ;

   lEndDate := lStartDay + (lStartHour/24) + (lDuration/24);

   RETURN lEndDate;
END calcShiftPlanEnd;


END SHIFT_PKG;
/