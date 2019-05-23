--liquibase formatted sql


--changeSet shift_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds shift specific pl/sql functions and procedures.
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE SHIFT_PKG
IS

/*********************        PACKAGE VARIABLES              *******************/

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
)  RETURN DATE;

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
)  RETURN DATE;

END SHIFT_PKG;
/