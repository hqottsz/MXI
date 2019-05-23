--liquibase formatted sql


--changeSet lpa_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE LPA_PKG
IS



/********************************************************************************
*
* Function:      is_task_defn_service_check
* Arguments:     an_TaskTaskDbId (IN NUMBER): task definition primary key
*                an_TaskTaskId (IN NUMBER):   task definition primary key
*
* Description:  Tests if a task definition revision is applicable to be considered a Service Check:
*        	- It is active
*      		- Is defined against an aircraft assembly
*      		- Has a task class with a CLASS MODE of 'BLOCK'
*     	       	- Is not part of a block chain
*      		- Is recurring
*      		- Does not have a task class of 'CHECK' or 'RO'
*      		- Does not have an applicability rule
*      	 	- Has exactly one scheduling rule which is calendar based
*
*
* Orig.Coder:     Natasa Subotic
* Recent Date:    December 14,2011
*
*********************************************************************************
*
* Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION is_task_defn_service_check(
                             an_TaskTaskDbId IN task_task.task_db_id%TYPE,
                             an_TaskTaskId   IN task_task.task_id%TYPE) RETURN NUMBER;




/********************************************************************************
*
* Function:      is_task_defn_turn_check
* Arguments:     an_TaskTaskDbId (IN NUMBER): task definition primary key
*                an_TaskTaskId (IN NUMBER):   task definition primary key
*
* Description:  Tests if a task definition revision is applicable to be considered a Turn Check:
*        	- It is active
*      		- Is defined against an aircraft assembly
*      		- Has a task class with a CLASS MODE of 'BLOCK'
*      		- Is not part of a block chain
*      		- Is not recurring
*      		- Does not have a task class of 'CHECK' or 'RO'
*      		- Does not have an applicability rule
*      		- Has no scheduling rules
*               - Is on condition
*
* Orig.Coder:     Natasa Subotic
* Recent Date:    December 14,2011
*
*********************************************************************************
*
* Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION is_task_defn_turn_check(
                             an_TaskTaskDbId IN task_task.task_db_id%TYPE,
                             an_TaskTaskId   IN task_task.task_id%TYPE) RETURN NUMBER;


/********************************************************************************
*
* Function:     is_task_service_check
*
* Arguments:    an_SchedDbId (IN NUMBER): scheduled task primary key
*               an_SchedId (IN NUMBER):   scheduled task primary key
*
* Description:  Tests if the scheduled task's task definition is stored in
*               LPA_FLEET table as service block definition. If it is, this task
*               is considered service block.
*
*
*
* Orig.Coder:     Natasa Subotic
* Recent Date:    December 14,2011
*
*********************************************************************************
*
* Copyright 2012 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION is_task_service_check(
                              an_SchedDbId IN sched_stask.task_db_id%TYPE,
                              an_SchedId   IN sched_stask.task_id%TYPE) RETURN NUMBER;



/********************************************************************************
*
* Function:     is_task_parent_service_check
*
* Arguments:    an_SchedDbId (IN NUMBER): scheduled task primary key
*               an_SchedId (IN NUMBER):   scheduled task primary key
*
* Description:  Tests if the scheduled task's parent is service check.
*               Task is considered service check if the task's task definition is stored in
*               LPA_FLEET table as service block definition.
*
*
*
* Orig.Coder:     Natasa Subotic
* Recent Date:    January 19, 2011
*
*********************************************************************************
*
* Copyright 2012 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION is_task_parent_service_check(
                              an_SchedDbId IN sched_stask.task_db_id%TYPE,
                              an_SchedId   IN sched_stask.task_id%TYPE) RETURN NUMBER;


/********************************************************************************
*
* Function:      get_service_check_line_loc
*
* Arguments:     aLocDbId (IN NUMBER): location primary key
*                aLocId (IN NUMBER):   location primary key
*                aFleetDbId (IN NUMBER): the DbId of the fleet assembly
*                aFleedCd (IN STRING) : the code of the fleet assembly
*
* Description:  This function returns the line location  that has the capability to
*               perform the service checks for given fleet and given location(airport)
*
*
*
* Orig.Coder:     sdevi
* Recent Date:    January 23,2012
*
*********************************************************************************
*
* Copyright 2012 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION get_service_check_line_loc(
                             aLocDbId   IN inv_loc.loc_db_id%TYPE,
                             aLocId     IN inv_loc.loc_id%TYPE,
                             aFleetDbId IN lpa_fleet.fleet_db_id%TYPE,
                             aFleetCd   IN lpa_fleet.fleet_cd%TYPE) RETURN STRING;


/********************************************************************************
*
* Function:      get_turn_check_line_loc
*
* Arguments:     aLocDbId (IN NUMBER): location primary key
*                aLocId (IN NUMBER):   location primary key
*                aFleetDbId (IN NUMBER): the DbId of the fleet assembly
*                aFleedCd (IN STRING) : the code of the fleet assembly
*
* Description:  This function returns the line location  that has the capability to
*               perform the turn checks for given fleet and given location(airport)
*
*
*
* Orig.Coder:     sdevi
* Recent Date:    January 23,2012
*
*********************************************************************************
*
* Copyright 2012 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION get_turn_check_line_loc(
                             aLocDbId   IN inv_loc.loc_db_id%TYPE,
                             aLocId     IN inv_loc.loc_id%TYPE,
                             aFleetDbId IN lpa_fleet.fleet_db_id%TYPE,
                             aFleetCd   IN lpa_fleet.fleet_cd%TYPE) RETURN STRING;


END LPA_PKG;
/