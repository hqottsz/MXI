--liquibase formatted sql


--changeSet DEV-1445_3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet DEV-1445_3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY LPA_PKG
IS

/********************************************************************************
*
* Function:     is_task_defn_service_check
* Arguments:    an_TaskTaskDbId (IN NUMBER): task definition primary key
*               an_TaskTaskId (IN NUMBER):   task definition primary key
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
                              an_TaskTaskId   IN task_task.task_id%TYPE) RETURN NUMBER

IS

  -- Variable declaration
  v_count NUMBER;

BEGIN

  SELECT
        1
        INTO v_count
        FROM
           task_task INNER JOIN eqp_assmbl ON
               task_task.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
               task_task.assmbl_cd    = eqp_assmbl.assmbl_cd
           INNER JOIN ref_task_class ON
               task_task.task_class_db_id  = ref_task_class.task_class_db_id AND
               task_task.task_class_cd     = ref_task_class.task_class_cd
           INNER JOIN (
                 SELECT
                    task_sched_rule.task_db_id,
                    task_sched_rule.task_id,
                    COUNT(*) as rule_count,
                    MAX(ref_domain_type.domain_type_cd) AS domain_type_cd_max
                 FROM
                    task_sched_rule,
                    ref_domain_type,
                    mim_data_type
                 WHERE
                    task_sched_rule.data_type_db_id = mim_data_type.data_type_db_id AND
                    task_sched_rule.data_type_id    = mim_data_type.data_type_id
                    AND
                    mim_data_type.domain_type_db_id = ref_domain_type.domain_type_db_id AND
                    mim_data_type.domain_type_cd    = ref_domain_type.domain_type_cd
                 GROUP BY
                    task_sched_rule.task_db_id,
                    task_sched_rule.task_id
                  )sched_rules_count ON
               task_task.task_db_id = sched_rules_count.task_db_id AND
               task_task.task_id    = sched_rules_count.task_id
        WHERE
           task_task.task_db_id = an_TaskTaskDbId AND
           task_task.task_id    = an_TaskTaskId
           AND
           task_task.task_def_status_db_id = 0 AND
           task_task.task_def_status_cd    = 'ACTV'
           AND
           task_task.recurring_task_bool = 1
           AND
           task_task.block_chain_sdesc IS NULL
           AND
           task_task.on_condition_bool = 0
           AND
           task_task.task_appl_sql_ldesc IS NULL
           AND
           ref_task_class.class_mode_cd = 'BLOCK' AND
           ref_task_class.task_class_cd NOT IN( 'CHECK', 'RO' )
           AND
           eqp_assmbl.assmbl_class_db_id = 0 AND
           eqp_assmbl.assmbl_class_cd    = 'ACFT'
           AND
           task_task.assmbl_bom_id = 0
           AND
           sched_rules_count.rule_count = 1
           AND
           sched_rules_count.domain_type_cd_max = 'CA';

         RETURN v_count;
	       EXCEPTION
	             WHEN NO_DATA_FOUND THEN
               RETURN 0;

END is_task_defn_service_check;



/********************************************************************************
*
* Function:     is_task_defn_turn_check
* Arguments:    an_TaskTaskDbId (IN NUMBER): task definition primary key
*               an_TaskTaskId (IN NUMBER):   task definition primary key
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
                              an_TaskTaskId   IN task_task.task_id%TYPE) RETURN NUMBER

IS

  -- Variable declaration
  v_count NUMBER;

BEGIN

  SELECT
        1
        INTO v_count
        FROM
           task_task INNER JOIN eqp_assmbl ON
	               eqp_assmbl.assmbl_db_id = task_task.assmbl_db_id AND
	               eqp_assmbl.assmbl_cd    = task_task.assmbl_cd
	            INNER JOIN ref_task_class ON
	                ref_task_class.task_class_db_id = task_task.task_class_db_id AND
	                ref_task_class.task_class_cd    = task_task.task_class_cd
	            LEFT OUTER JOIN task_sched_rule ON
	                task_sched_rule.task_db_id = task_task.task_db_id AND
	                task_sched_rule.task_id    = task_task.task_id
       WHERE
          task_task.task_db_id = an_TaskTaskDbId AND
          task_task.task_id    = an_TaskTaskId
          AND
          task_task.task_def_status_db_id = 0 AND
          task_task.task_def_status_cd    = 'ACTV'
          AND
          task_task.recurring_task_bool = 0
          AND
          task_task.block_chain_sdesc IS NULL
          AND
          task_task.on_condition_bool = 1
          AND
          task_task.task_appl_sql_ldesc IS NULL
          AND
          ref_task_class.class_mode_cd = 'BLOCK' AND
          ref_task_class.task_class_cd NOT IN( 'CHECK', 'RO' )
          AND
          eqp_assmbl.assmbl_class_db_id = 0 AND
          eqp_assmbl.assmbl_class_cd    = 'ACFT'
          AND
          task_task.assmbl_bom_id  = 0
          AND
          task_sched_rule.task_db_id IS NULL AND
          task_sched_rule.task_id    IS NULL;

        RETURN v_count;
	      EXCEPTION
	             WHEN NO_DATA_FOUND THEN
               RETURN 0;

END is_task_defn_turn_check;


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
                              an_SchedId   IN sched_stask.task_id%TYPE) RETURN NUMBER

IS

  -- Variable declaration
  v_count NUMBER;

BEGIN

  SELECT
        1
        INTO v_count
        FROM
           sched_stask INNER JOIN task_task ON
               task_task.task_db_id = sched_stask.task_db_id AND
               task_task.task_id    = sched_stask.task_id
           INNER JOIN task_defn ON
               task_defn.task_defn_db_id = task_task.task_defn_db_id AND
               task_defn.task_defn_id    = task_task.task_defn_id
           WHERE
               sched_stask.sched_db_id = an_SchedDbId AND
               sched_stask.sched_id    = an_SchedId
               AND
               EXISTS(
                      SELECT 
                         1 
                      FROM
                          lpa_fleet
                      WHERE 
                          lpa_fleet.service_block_defn_db_id = task_defn.task_defn_db_id AND
                          lpa_fleet.service_block_defn_id    = task_defn.task_defn_id                                              
               );

         RETURN v_count;
	       EXCEPTION
	             WHEN NO_DATA_FOUND THEN
               RETURN 0;

END is_task_service_check;

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
                              an_SchedId   IN sched_stask.task_id%TYPE) RETURN NUMBER

IS

  -- Variable declaration
  v_count NUMBER;

BEGIN

  SELECT
        1
        INTO v_count
        FROM
          sched_stask subtask_task INNER JOIN evt_event subtask_event ON
              subtask_event.event_db_id = subtask_task.sched_db_id AND
              subtask_event.event_id    = subtask_task.sched_id 
          INNER JOIN task_task subtask_task_rev ON 
              subtask_task_rev.task_db_id = subtask_task.task_db_id  AND
              subtask_task_rev.task_id    = subtask_task.task_id 
          INNER JOIN task_defn subtask_taskdefn  ON
               subtask_taskdefn.task_defn_db_id =  subtask_task_rev.task_defn_db_id AND
               subtask_taskdefn.task_defn_id    =  subtask_task_rev.task_defn_id
          INNER JOIN task_block_req_map ON
              task_block_req_map.req_task_defn_db_id = subtask_taskdefn.task_defn_db_id AND
              task_block_req_map.req_task_defn_id    = subtask_taskdefn.task_defn_id
          INNER JOIN task_task parent_task_rev ON
              parent_task_rev.task_db_id = task_block_req_map.block_task_db_id AND
              parent_task_rev.task_id    = task_block_req_map.block_task_id
          INNER JOIN task_defn parent_taskdefn ON
             parent_taskdefn.task_defn_db_id = parent_task_rev.task_defn_db_id AND
             parent_taskdefn.task_defn_id    = parent_task_rev.task_defn_id         
           WHERE
               subtask_task.sched_db_id = an_SchedDbId AND
               subtask_task.sched_id    = an_SchedId
               AND
               parent_task_rev.task_def_status_cd = 'ACTV'
               AND
               EXISTS(
                      SELECT 
                         1 
                      FROM
                          lpa_fleet
                      WHERE 
                          lpa_fleet.service_block_defn_db_id = parent_taskdefn.task_defn_db_id AND
                          lpa_fleet.service_block_defn_id    = parent_taskdefn.task_defn_id);   
         RETURN v_count;
	       EXCEPTION
	             WHEN NO_DATA_FOUND THEN
               RETURN 0;

END is_task_parent_service_check;

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
                             aFleetCd   IN lpa_fleet.fleet_cd%TYPE ) RETURN STRING
IS 

	ls_line_loc_key string(16);

BEGIN
     SELECT 
          line_location_key
     INTO  
          ls_line_loc_key
     FROM 
        (
           SELECT
               line_loc.loc_db_id || ':' || line_loc.loc_id as line_location_key,
               line_loc.loc_cd
           FROM
               inv_loc line_loc
           WHERE
               line_loc.supply_loc_db_id = aLocDbId AND
               line_loc.supply_loc_id    = aLocId AND
               line_loc.loc_type_cd = 'LINE'
               AND
              --make sure the location is capable of doing the all service work types
              (
                SELECT  count(*)
                FROM
                   lpa_service_work_type
                WHERE
                   lpa_service_work_type.fleet_db_id = aFleetDbId AND
                   lpa_service_work_type.fleet_cd    = aFleetCd
                   AND NOT EXISTS
                   (
                     SELECT
                        1
                     FROM
                        inv_loc_capability
                     WHERE
                        inv_loc_capability.loc_db_id = line_loc.loc_db_id AND
                        inv_loc_capability.loc_id    = line_loc.loc_id
                        AND
                        inv_loc_capability.assmbl_db_id = lpa_service_work_type.fleet_db_id AND
                        inv_loc_capability.assmbl_cd    = lpa_service_work_type.fleet_cd
                        AND
                        inv_loc_capability.work_type_db_id = lpa_service_work_type.work_type_db_id AND
                        inv_loc_capability.work_type_cd    = lpa_service_work_type.work_type_cd
                   )
               ) = 0
           ORDER BY
               line_loc.loc_cd
         )
      WHERE
         ROWNUM = 1;

   RETURN ls_line_loc_key;

END get_service_check_line_loc;


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
                             aFleetCd   IN lpa_fleet.fleet_cd%TYPE ) RETURN STRING

IS 

	ls_line_loc_key string(16);

BEGIN
     SELECT 
          line_location_key
     INTO  
          ls_line_loc_key
     FROM 
        (
           SELECT
               line_loc.loc_db_id || ':' || line_loc.loc_id as line_location_key,
               line_loc.loc_cd
           FROM
               inv_loc line_loc
           WHERE
               line_loc.supply_loc_db_id = aLocDbId AND
               line_loc.supply_loc_id    = aLocId AND
               line_loc.loc_type_cd = 'LINE'
               AND
              --make sure the location is capable of doing the all turn work types
              (
                SELECT  count(*)
                FROM
                   lpa_turn_work_type
                WHERE
                   lpa_turn_work_type.fleet_db_id = aFleetDbId AND
                   lpa_turn_work_type.fleet_cd    = aFleetCd
                   AND NOT EXISTS
                   (
                     SELECT
                        1
                     FROM
                        inv_loc_capability
                     WHERE
                        inv_loc_capability.loc_db_id = line_loc.loc_db_id AND
                        inv_loc_capability.loc_id    = line_loc.loc_id
                        AND
                        inv_loc_capability.assmbl_db_id = lpa_turn_work_type.fleet_db_id AND
                        inv_loc_capability.assmbl_cd    = lpa_turn_work_type.fleet_cd
                        AND
                        inv_loc_capability.work_type_db_id = lpa_turn_work_type.work_type_db_id AND
                        inv_loc_capability.work_type_cd    = lpa_turn_work_type.work_type_cd
                   )
               ) = 0
           ORDER BY
               line_loc.loc_cd
         )
      WHERE
         ROWNUM = 1;

   RETURN ls_line_loc_key;

END get_turn_check_line_loc;

END LPA_PKG;
/

--changeSet DEV-1445_3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PREP_DEADLINE_PKG IS

/* Subtypes internal to the package. */
TYPE typrec_SchedulingRule IS RECORD (

   DataTypeDbId      NUMBER,
   DataTypeId        NUMBER,
   IntervalQt        FLOAT(22),
   InitialQt         FLOAT(22),
   DeviationQt       FLOAT(22),
   PrefixedQt        FLOAT(22),
   PostfixedQt       FLOAT(22)
);

TYPE typtabrec_SchedulingRuleTable IS TABLE OF typrec_SchedulingRule INDEX BY binary_integer;


TYPE typrec_ScheduleDetails IS RECORD (

   -- Stask details
   STaskDbId NUMBER,
   STaskId   NUMBER,
   FirstInstanceBool BOOLEAN,
   PreviousSTaskDbId NUMBER,
   PreviousSTaskId   NUMBER,
   HInvNoDbId        NUMBER,
   HInvNoId          NUMBER,
   PartNoDbId        NUMBER,
   PartNoId          NUMBER,

   -- Task definition details
   ActiveTaskTaskDbId   NUMBER,
   ActiveTaskTaskId     NUMBER,
   RevisionTaskTaskDbId NUMBER,
   RevisionTaskTaskId   NUMBER,
   EffectiveDt          DATE,
   ReschedFromCd        VARCHAR2(8),
   RelativeBool         NUMBER,
   SchedFromLatestBool  NUMBER,
   RecurringBool        NUMBER,
   SchedFromReceivedDtBool NUMBER,
   TaskClassCd          VARCHAR2(8),
   ScheduleToLast       NUMBER
);


/********************************************************************************
*
* Procedure: GetTwoLastRevisions
* Arguments:
*            an_TaskDbId task definition pk
*            an_TaskId   -- // --
*
* Return:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_PrevTaskDbId   - previous task definition
*            an_PrevTaskId     -- // --
*            on_Return         - 1 is success
*
* Description: This procedure returns two latest revisions for a task definition.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE GetTwoLastRevisions(
            an_TaskDbId IN task_task.task_db_id%TYPE,
            an_TaskId IN task_task.task_id%TYPE,
            an_LatestTaskDbId OUT task_task.task_db_id%TYPE,
            an_LatestTaskId OUT task_task.task_id%TYPE,
            an_PrevTaskDbId OUT task_task.task_db_id%TYPE,
            an_PrevTaskId OUT task_task.task_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


/* get the deadline */
CURSOR lcur_TwoLastRevisions  IS
 SELECT
       prev_task_def.task_db_id as prev_task_db_id,
       prev_task_def.task_id as prev_task_id,
       new_task_def.task_db_id as new_task_db_id,
       new_task_def.task_id as new_task_id,
       task_defn.task_defn_db_id,
       task_defn.task_defn_id

 FROM task_defn,
       task_task new_task_def,
       task_task prev_task_def,
       task_task start_task_def
 WHERE
      -- get main task definition
      start_task_def.task_db_id=an_TaskDbId AND
      start_task_def.task_id=an_TaskId
      AND
      task_defn.task_defn_db_id=start_task_def.task_defn_db_id AND
      task_defn.task_defn_id=start_task_def.task_defn_id
      AND
      -- get task with previous task definition
      prev_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      prev_task_def.task_defn_id=task_defn.task_defn_id AND
      prev_task_def.revision_ord = task_defn.last_revision_ord-1
      AND
      new_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      new_task_def.task_defn_id=task_defn.task_defn_id AND
      new_task_def.revision_ord = task_defn.last_revision_ord;
      lrec_TwoLastRevisions  lcur_TwoLastRevisions%ROWTYPE;
BEGIN


   on_Return := icn_NoProc;

   OPEN  lcur_TwoLastRevisions();
   FETCH lcur_TwoLastRevisions INTO lrec_TwoLastRevisions;
   CLOSE lcur_TwoLastRevisions;


   an_LatestTaskDbId:= lrec_TwoLastRevisions.new_task_db_id;
   an_LatestTaskId  := lrec_TwoLastRevisions.new_task_id ;
   an_PrevTaskDbId  := lrec_TwoLastRevisions.prev_task_db_id;
   an_PrevTaskId  := lrec_TwoLastRevisions.prev_task_id;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTwoLastRevisions@@@'||SQLERRM);
      RETURN;

END GetTwoLastRevisions;

/********************************************************************************
*
* Procedure:    GetActualDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
* Return:
*           as_Sched_FromCd (char) - scheduled from refterm
*           an_IntervalQt   (float) - deadline interval qt
*           an_NotifyQt     (float) - deadline notify qt
*           an_DeviationQt  (float) - deadline deviation qt
*           an_PrefixedQt   (float) - deadline prefixed qt
*           an_PostfixedQt  (float) - deadline postfixed qt
*           ad_StartDate    (date)  - start date
*           an_StartQt      (float) - start qt
*           an_InitialIntervalBool (boolean) -true if using def_inital_interval
*           on_Return       (long) - 1 Success/ Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetActualDeadline(
            an_DataTypeDbId        IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId          IN task_sched_rule.data_type_id%TYPE,
            an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_SchedId             IN sched_stask.sched_id%TYPE,
            an_TaskDbId            IN task_task.task_db_id%TYPE,
            an_TaskId              IN task_task.task_id%TYPE,
            ab_RefreshMe           IN BOOLEAN,
            as_Sched_FromCd        OUT evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt          OUT task_sched_rule.def_interval_qt%TYPE,
            an_NotifyQt            OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt         OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt          OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt         OUT task_sched_rule.def_postfixed_qt%TYPE,
            ad_StartDt             OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt             OUT evt_sched_dead.start_qt%TYPE,
            an_DeadlineExists      OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return              OUT typn_RetCode
   ) IS

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;


/* get the deadline */
CURSOR lcur_ActualsDeadlines  IS
      SELECT
           evt_sched_dead.interval_qt,
           evt_sched_dead.notify_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_sched_dead.sched_from_cd,
           evt_sched_dead.start_dt,
           evt_sched_dead.start_qt
      FROM
          evt_sched_dead
      WHERE
           evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
           evt_sched_dead.data_type_id = an_DataTypeId
           AND
           evt_sched_dead.event_db_id =  an_SchedDbId AND
           evt_sched_dead.event_id = an_SchedId;
           lrec_ActualsDeadlines  lcur_ActualsDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* get actual deadline of a task */
   OPEN  lcur_ActualsDeadlines();
   FETCH lcur_ActualsDeadlines INTO lrec_ActualsDeadlines;
   IF NOT lcur_ActualsDeadlines%FOUND THEN
      -- no actual deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_ActualsDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_ActualsDeadlines;


   an_IntervalQt   := lrec_ActualsDeadlines.interval_qt;
   an_NotifyQt     := lrec_ActualsDeadlines.notify_qt ;
   an_DeviationQt  := lrec_ActualsDeadlines.deviation_qt;
   an_PrefixedQt   := lrec_ActualsDeadlines.prefixed_qt;
   an_PostfixedQt  := lrec_ActualsDeadlines.postfixed_qt;
   as_Sched_FromCd :=lrec_ActualsDeadlines.sched_from_cd;
   ad_StartDt      :=lrec_ActualsDeadlines.start_dt;
   an_StartQt      :=lrec_ActualsDeadlines.start_qt;
   /* Measurement scheduling rules*/
   IF ab_RefreshMe THEN
        /* If this task satisfies the ME sched rules condition then use the baseline values*/
        GetMESchedRuleDeadline(
                               an_SchedDbId,
                               an_SchedId,
                               an_TaskDbId,
                               an_TaskId,
                               an_DataTypeDbId,
                               an_DataTypeId,
                               ln_IntervalQt_me,
                               ln_NotifyQt_me,
                               ln_DeviationQt_me,
                               ln_PrefixedQt_me,
                               ln_PostfixedQt_me,
                               ln_Return_me
        );

        IF ln_Return_me > 0
           AND  -- only if the measurement deadline has changed due to JIC measurement changing. We do not want to reset the deviation
                -- unless we have to
           NOT ( an_IntervalQt  = ln_IntervalQt_me AND
                 an_NotifyQt    = ln_NotifyQt_me   AND
                 an_PrefixedQt  = ln_PrefixedQt_me AND
                 an_PostfixedQt = ln_PostfixedQt_me    ) THEN

           an_IntervalQt   := ln_IntervalQt_me;
           an_NotifyQt     := ln_NotifyQt_me;
           an_DeviationQt  := ln_DeviationQt_me;
           an_PrefixedQt   := ln_PrefixedQt_me;
           an_PostfixedQt  := ln_PostfixedQt_me;
        END IF;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetActualDeadline@@@'||SQLERRM);
      RETURN;

END GetActualDeadline;

/********************************************************************************
*
* Procedure:    GetBaselineDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
*           an_TaskDbId     (long) - The primary key of the task's definition key
*           an_TaskId       (long) --//--
*           an_PartNoDbId   (long) - The primary key of the main inventory part no
*           an_PartNoId     (long) --//--
* Return:
*           an_IntervalQt   (float) - deadline interval qt
*           an_InitialQt    (float) - deadline initial qt
*           an_NotifyQt     (float) - dedline notify qt
*           an_DeviationQt  (float) - dedline deviation qt
*           an_PrefixedQt   (float) - dedline prefixed qt
*           an_PostfixedQt  (float) - dedline postfixed qt
*           an_DeadlineExists  (long) - 1 if baseline deadline exists
*           on_Return       (long) - Success/Failure
*
* Description:  This procedure is used to get deadline from task_sched_rule table.
*               Baselined deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetBaselineDeadline(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            an_TaskDbId     IN task_interval.task_db_id%TYPE,
            an_TaskId       IN task_interval.task_id%TYPE,
            an_SchedDbId    IN sched_stask.task_db_id%TYPE,
            an_SchedId      IN sched_stask.task_id%TYPE,
            an_PartNoDbId   IN task_interval.part_no_db_id%TYPE,
            an_PartNoId     IN task_interval.part_no_id%TYPE,
            an_HInvNoDbId   IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId     IN task_ac_rule.inv_no_id%TYPE,
            an_IntervalQt      OUT task_sched_rule.def_interval_qt%TYPE,
            an_InitialQt       OUT task_sched_rule.def_initial_qt%TYPE,
            an_NotifyQt        OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt     OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt      OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt     OUT task_sched_rule.def_postfixed_qt%TYPE,
            an_DeadlineExists  OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return          OUT typn_RetCode
   ) IS

    /*
   || Cursor used to get BASELINED scheduling rules for the task.  This
   || will only return deadlines for tasks with task definitions, since adhoc
   || task cannot have baselines deadlines.
   || It  will bring back the part-specific interval_qt if it exists.
   ||
   || When determine the deadline date for an actual task, the scheduling rules should be taken in this order:
   ||    Measurement Specifc
   ||    Part Sepcific
   ||    Tail Specific
   ||    Standard
   */
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id%TYPE,
            cn_TaskId      task_interval.task_id%TYPE,
            cn_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            cn_DataTypeId IN task_sched_rule.data_type_id%TYPE,
            cn_PartNoDbId  task_interval.part_no_db_id%TYPE,
            cn_PartNoId    task_interval.part_no_id%TYPE
         ) IS
         -- Standard, if no Part or Tail rules
         SELECT
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_notify_qt      notify_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId       AND
            task_sched_rule.data_type_db_id = cn_DataTypeDbId AND
            task_sched_rule.data_type_id    = cn_DataTypeId

            -- No Part Specific exists
            AND NOT EXISTS
             ( SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId )

            -- No Tail Specific exists
            AND NOT EXISTS
               (  SELECT 1
                  FROM
                     task_ac_rule
                  WHERE
                     task_ac_rule.task_db_id      = cn_TaskDbId     AND
                     task_ac_rule.task_id         = cn_TaskId       AND
                     task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
                     task_ac_rule.data_type_id    = cn_DataTypeId   AND
                     task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
                     task_ac_rule.inv_no_id       = an_HInvNoId )

        UNION ALL

         -- Tail Specific rules
         SELECT
            task_ac_rule.interval_qt,
            task_ac_rule.initial_qt,
            task_ac_rule.notify_qt,
            task_ac_rule.deviation_qt,
            task_ac_rule.prefixed_qt,
            task_ac_rule.postfixed_qt
         FROM
            task_ac_rule
         WHERE
            task_ac_rule.task_db_id      = cn_TaskDbId     AND
            task_ac_rule.task_id         = cn_TaskId       AND
            task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
            task_ac_rule.data_type_id    = cn_DataTypeId   AND
            task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
            task_ac_rule.inv_no_id       = an_HInvNoId
            -- No Part Specific exists
            AND NOT EXISTS
            (
               SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId
            )
        UNION ALL

         -- Part Specific Rules
         SELECT
            task_interval.interval_qt,
            task_interval.initial_qt,
            task_interval.notify_qt,
            task_interval.deviation_qt,
            task_interval.prefixed_qt,
            task_interval.postfixed_qt
         FROM
            task_interval
         WHERE
            task_interval.task_db_id      = cn_TaskDbId     AND
            task_interval.task_id         = cn_TaskId       AND
            task_interval.data_type_db_id = cn_DataTypeDbId AND
            task_interval.data_type_id    = cn_DataTypeId   AND
            task_interval.part_no_db_id   = cn_PartNoDbId   AND
            task_interval.part_no_id      = cn_PartNoId;

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;

            lrec_BaselineDeadlines  lcur_BaselineDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* If Measurement scheduling rules exist, then use them*/
   GetMESchedRuleDeadline(
                          an_SchedDbId,
                          an_SchedId,
                          an_TaskDbId,
                          an_TaskId,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_IntervalQt_me,
                          ln_NotifyQt_me,
                          ln_DeviationQt_me,
                          ln_PrefixedQt_me,
                          ln_PostfixedQt_me,
                          ln_Return_me
      );

   IF ln_Return_me > 0 THEN
      an_IntervalQt   := ln_IntervalQt_me;
      an_NotifyQt     := ln_NotifyQt_me;
      an_DeviationQt  := ln_DeviationQt_me;
      an_PrefixedQt   := ln_PrefixedQt_me;
      an_PostfixedQt  := ln_PostfixedQt_me;
      an_InitialQt    := 0;

      an_DeadlineExists :=1;
      on_Return         := icn_Success;
      RETURN;
   END IF;

    /* get the baseline deadline for this task definition.*/
   OPEN  lcur_BaselineDeadlines(
            an_TaskDbId,
            an_TaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            an_PartNoDbId,
            an_PartNoId );
   FETCH lcur_BaselineDeadlines INTO lrec_BaselineDeadlines;
   IF NOT lcur_BaselineDeadlines%FOUND THEN
      -- no baseline deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_BaselineDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_BaselineDeadlines;

   /* initialize the out variables */
   an_IntervalQt      := lrec_BaselineDeadlines.interval_qt;
   an_InitialQt       := lrec_BaselineDeadlines.initial_qt;
   an_NotifyQt        := lrec_BaselineDeadlines.notify_qt ;
   an_DeviationQt     := lrec_BaselineDeadlines.deviation_qt;
   an_PrefixedQt      := lrec_BaselineDeadlines.prefixed_qt;
   an_PostfixedQt     := lrec_BaselineDeadlines.postfixed_qt;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetBaselineDeadline@@@'||SQLERRM);
      RETURN;

END GetBaselineDeadline;

/********************************************************************************
*
* Procedure: BaselineDeadlinesChanged
* Arguments:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_OrigTaskDbId   - the task definition revision before the synch
*            an_OrigTaskId     -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_HInvNoDbId    -- the highest inventory of the main inventory
*            an_HInvNoId      -- // --
*            an_PartNoDbId    -- the part number of the main inventory
*            an_PartNoId      -- // --
*
* Return:
*            an_BaselineDeadlineChanged - 1 if deadline changed between two baseline revisions
*            on_Return                  - 1 is success
*
* Description: This procedure returns
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  BaselineDeadlinesChanged(
            an_LatestTaskDbId IN task_task.task_db_id%TYPE,
            an_LatestTaskId   IN task_task.task_id%TYPE,
            an_OrigTaskDbId   IN task_task.task_db_id%TYPE,
            an_OrigTaskId     IN task_task.task_id%TYPE,
            an_DataTypeDbId   IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId     IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId     IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     IN task_interval.part_no_db_id%TYPE,
            an_PartNoId       IN task_interval.part_no_id%TYPE,
            an_BaselineDeadlineChanged OUT NUMBER,
            on_Return       OUT typn_RetCode
   ) IS

   ln_orig_exist   NUMBER;
   ln_latest_exist NUMBER;


   /* task rules */
   CURSOR lcur_Rule(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_sched_rule.def_initial_qt,
                        NULL,
                        -999999,
                        task_sched_rule.def_initial_qt
                ) AS def_initial_qt,
                task_sched_rule.def_interval_qt,
                task_sched_rule.def_deviation_qt,
                task_sched_rule.def_prefixed_qt,
                task_sched_rule.def_postfixed_qt
          FROM
                task_sched_rule
          WHERE
                task_sched_rule.task_db_id      = cl_TaskDbId     AND
                task_sched_rule.task_id         = cl_TaskId       AND
                task_sched_rule.data_type_db_id = an_DataTypeDbId AND
                task_sched_rule.data_type_id    = an_DataTypeId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId )
                -- No Tail Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_ac_rule
                  WHERE  task_ac_rule.task_db_id      = cl_TaskDbId     AND
                         task_ac_rule.task_id         = cl_TaskId       AND
                         task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                         task_ac_rule.data_type_id    = an_DataTypeId   AND
                         task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                         task_ac_rule.inv_no_id       = an_HInvNoId);

   /* tail rules*/
   CURSOR lcur_Tail(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_ac_rule.initial_qt,
                        NULL,
                        -999999,
                        task_ac_rule.initial_qt
                 ) AS initial_qt,
                 task_ac_rule.interval_qt,
                 task_ac_rule.deviation_qt,
                 task_ac_rule.prefixed_qt,
                 task_ac_rule.postfixed_qt
          FROM
                 task_ac_rule
          WHERE
                 task_ac_rule.task_db_id      = cl_TaskDbId     AND
                 task_ac_rule.task_id         = cl_TaskId       AND
                 task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                 task_ac_rule.data_type_id    = an_DataTypeId   AND
                 task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                 task_ac_rule.inv_no_id       = an_HInvNoId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId );

   /* part rules*/
   CURSOR lcur_Part(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_interval.initial_qt,
                        NULL,
                        -999999,
                        task_interval.initial_qt
                 ) AS initial_qt,
                 task_interval.interval_qt,
                 task_interval.deviation_qt,
                 task_interval.prefixed_qt,
                 task_interval.postfixed_qt
          FROM
                 task_interval
          WHERE
                 task_interval.task_db_id      = cl_TaskDbId     AND
                 task_interval.task_id         = cl_TaskId       AND
                 task_interval.data_type_db_id = an_DataTypeDbId AND
                 task_interval.data_type_id    = an_DataTypeId   AND
                 task_interval.part_no_db_id   = an_PartNoDbId   AND
                 task_interval.part_no_id      = an_PartNoId;

   /* Measurement scheduling rules*/
   CURSOR lcur_ME(
          cn_PrevTaskDbId      task_task.task_db_id%TYPE,
          cn_PrevTaskId        task_task.task_id%TYPE,
          cn_LatestTaskDbId    task_task.task_db_id%TYPE,
          cn_LatestTaskId      task_task.task_id%TYPE
   )
   IS
   (--post minus pre
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt

     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  )
  UNION
  (--pre minus post
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  );

   /* task_task details */
   CURSOR lcur_Task(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                 task_task.recurring_task_bool,
                 task_task.relative_bool,
                 task_task.sched_from_latest_bool,
                 task_task.sched_from_received_dt_bool,
                 task_task.last_sched_dead_bool,
                 task_task.effective_gdt,
                 task_task.resched_from_cd
          FROM
                 task_task
          WHERE
                 task_task.task_db_id = cl_TaskDbId AND
                 task_task.task_id    = cl_TaskId;

   /* CURSORS */
   lrec_MERule    lcur_ME%ROWTYPE;

   lrec_OrigRule   lcur_Rule%ROWTYPE;
   lrec_LatestRule lcur_Rule%ROWTYPE;

   lrec_OrigPart   lcur_Part%ROWTYPE;
   lrec_LatestPart lcur_Part%ROWTYPE;

   lrec_OrigTail   lcur_Tail%ROWTYPE;
   lrec_LatestTail lcur_Tail%ROWTYPE;

   lrec_OrigTask   lcur_Task%ROWTYPE;
   lrec_LatestTask lcur_Task%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;
   an_BaselineDeadlineChanged:=0;

   /*
   * VERIFY FOR DIFFERENCES IN MEASUREMENT SCHEDULING RULES
   */
   OPEN lcur_ME(an_OrigTaskDbId, an_OrigTaskId, an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_ME INTO lrec_MERule;
   IF lcur_ME%FOUND THEN
      an_BaselineDeadlineChanged:=1;
      CLOSE lcur_ME;
      on_Return := icn_Success;
      RETURN;
   END IF;
   CLOSE lcur_ME;

   /*
   * VERIFY FOR DIFFERENCES IN TASK RULES
   */

   /* get original rule information */
   OPEN lcur_Rule(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Rule INTO lrec_OrigRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   /* get latest rule information */
   OPEN lcur_Rule(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Rule INTO lrec_LatestRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigRule.Def_Interval_Qt  = lrec_LatestRule.Def_Interval_Qt)  OR
           NOT(lrec_OrigRule.Def_Deviation_Qt = lrec_LatestRule.Def_Deviation_Qt) OR
           NOT(lrec_OrigRule.Def_Initial_Qt   = lrec_LatestRule.Def_Initial_Qt)   OR
           NOT(lrec_OrigRule.Def_Prefixed_Qt  = lrec_LatestRule.Def_Prefixed_Qt)  OR
           NOT(lrec_OrigRule.Def_Postfixed_Qt = lrec_LatestRule.Def_Postfixed_Qt)
          ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN PART RULES
   */

   /* get original rule information */
   OPEN lcur_Part(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Part INTO lrec_OrigPart;
   IF NOT lcur_Part%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Part;

   /* get latest rule information */
   OPEN lcur_Part(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Part INTO lrec_LatestPart;
   IF NOT lcur_Part%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Part;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigPart.Interval_Qt  = lrec_LatestPart.Interval_Qt)      OR
           NOT(lrec_OrigPart.Deviation_Qt = lrec_LatestPart.Deviation_Qt)     OR
           NOT(lrec_OrigPart.Initial_Qt   = lrec_LatestPart.Initial_Qt)       OR
           NOT(lrec_OrigPart.Prefixed_Qt  = lrec_LatestPart.Prefixed_Qt)      OR
           NOT(lrec_OrigPart.Postfixed_Qt = lrec_LatestPart.Postfixed_Qt)
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TAIL RULES
   */

   /* get original rule information */
   OPEN lcur_Tail(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Tail INTO lrec_OrigTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   /* get latest rule information */
   OPEN lcur_Tail(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Tail INTO lrec_LatestTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigTail.Interval_Qt  = lrec_LatestTail.Interval_Qt)      OR
           NOT(lrec_OrigTail.Deviation_Qt = lrec_LatestTail.Deviation_Qt)     OR
           NOT(lrec_OrigTail.Initial_Qt   = lrec_LatestTail.Initial_Qt)       OR
           NOT(lrec_OrigTail.Prefixed_Qt  = lrec_LatestTail.Prefixed_Qt)      OR
           NOT(lrec_OrigTail.Postfixed_Qt = lrec_LatestTail.Postfixed_Qt)
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TASK SCHEDULING DETAILS
   */

   /* get original task information */
   OPEN lcur_Task(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Task INTO lrec_OrigTask;
   IF NOT lcur_Task%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Task;

   /* get latest task information */
   OPEN lcur_Task(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Task INTO lrec_LatestTask;
   IF NOT lcur_Task%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Task;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both task revisions exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 )
         AND
         (
            (lrec_OrigTask.recurring_task_bool           != lrec_LatestTask.recurring_task_bool)             OR
            (lrec_OrigTask.relative_bool                 != lrec_LatestTask.relative_bool)                   OR
            (lrec_OrigTask.sched_from_latest_bool        != lrec_LatestTask.sched_from_latest_bool)          OR
            (lrec_OrigTask.sched_from_received_dt_bool   != lrec_LatestTask.sched_from_received_dt_bool)
            OR
            NOT(lrec_OrigTask.effective_gdt          = lrec_LatestTask.effective_gdt)                     OR
            (lrec_OrigTask.effective_gdt IS NULL     AND lrec_LatestTask.effective_gdt IS NOT NULL)       OR
            (lrec_OrigTask.effective_gdt IS NOT NULL AND lrec_LatestTask.effective_gdt IS NULL)
            OR
            NOT(lrec_OrigTask.resched_from_cd          = lrec_LatestTask.resched_from_cd )                OR
            (lrec_OrigTask.resched_from_cd IS NULL     AND lrec_LatestTask.resched_from_cd IS NOT NULL)   OR
            (lrec_OrigTask.resched_from_cd IS NOT NULL AND lrec_LatestTask.resched_from_cd IS NULL)
         )
      THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@BaselineDeadlinesChanged@@@'||SQLERRM);
      RETURN;

END BaselineDeadlinesChanged;


/********************************************************************************
*
* Procedure: GetValuesAndActionForSynch
* Arguments: an_OrigTaskDbId  -definition revision of the task before synch ran
*            an_OrigTaskId    -- // --
*            an_TaskDbId      -task definition pk
*            an_TaskId        -- // --
*            an_SchedDbId     - the actual task pk
*            an_SchedId       -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_PartNoDbId    - the main inventory part number pk
*            an_PartNoId      -- // --
* Return:
*            an_DeleteActualDealine   - true if actual deadline should be deleted
*            an_UpdateActualDeadline  - true if actual deadline should be updated to match baseline
*            an_InsertActualDeadline  - true if actual deadlin should be created as per baseline
*            as_sched_from_cd         - value of actual sched_from_cd
*            ad_start_dt              - value of actuals start_dt (used for UPDATE action)
*            an_start_qt              - value of actuals start_qt (used for UPDATE action)
*            an_BaselineIntervalQt    - value of baseline interval
*            an_BaselineInitialQt     - value of baseline initial interval
*            an_BaselineNotifyQt      - value of baseline notification quantity
*            an_BaselineDeviationQt   - value of baseline deviation
*            an_BaselinePrefixedQt    - value of baseline prefix
*            an_BaselinePostfixedQt   - value of baseline postfix
*            on_Return                - 1 is success
*
* Description: This procedue will retrieve the action to perform on the actual deadline, either
*              DELETE, CREATE or UPDATE the actual to match the baseline
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    September 29, 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  GetValuesAndActionForSynch(
            an_OrigTaskDbId            IN task_task.task_db_id%TYPE,
            an_OrigTaskId              IN task_task.task_id%TYPE,
            an_TaskDbId                IN task_task.task_db_id%TYPE,
            an_TaskId                  IN task_task.task_id%TYPE,
            an_PrevSchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_PrevSchedId             IN sched_stask.sched_id%TYPE,
            an_SchedDbId               IN sched_stask.task_db_id%TYPE,
            an_SchedId                 IN sched_stask.task_id%TYPE,
            an_DataTypeDbId            IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId              IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId              IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId                IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId              IN task_interval.part_no_db_id%TYPE,
            an_PartNoId                IN task_interval.part_no_id%TYPE,
            an_RecurringBool           IN task_task.recurring_task_bool%TYPE,
            an_EventsOnSameInv         IN NUMBER,

            -- OUT actions
            an_DeleteActualDealine    OUT NUMBER,
            an_UpdateActualDeadline   OUT NUMBER,
            an_InsertActualDeadline   OUT NUMBER,

            -- OUT actual values
            as_sched_from_cd          OUT evt_sched_dead.sched_from_cd%TYPE,
            ad_start_dt               OUT evt_sched_dead.start_dt%TYPE,
            an_start_qt               OUT evt_sched_dead.start_qt%TYPE,

            -- OUT baseline values
            an_BaselineIntervalQt     OUT task_sched_rule.def_interval_qt%TYPE,
            an_BaselineNotifyQt       OUT task_sched_rule.def_notify_qt%TYPE,
            an_BaselineDeviationQt    OUT task_sched_rule.def_deviation_qt%TYPE,
            an_BaselinePrefixedQt     OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_BaselinePostfixedQt    OUT task_sched_rule.def_postfixed_qt%TYPE,

            on_Return                 OUT typn_RetCode
   ) IS

      -- actual information
      ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
      ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
      ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
      ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
      ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
      ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
      ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
      ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
      ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

      -- original baseline information
      ln_OrigIntervalQt          task_sched_rule.def_Interval_Qt%TYPE;
      ln_OrigInitialQt           task_sched_rule.def_initial_qt%TYPE;
      ln_OrigNotifyQt            task_sched_rule.def_notify_qt%TYPE;
      ln_OrigDeviationQt         task_sched_rule.def_deviation_qt%TYPE;
      ln_OrigPrefixedQt          task_sched_rule.def_prefixed_qt%TYPE;
      ln_OrigPostfixedQt         task_sched_rule.def_postfixed_qt%TYPE;
      ln_OrigDeadlineExists      task_sched_rule.def_postfixed_qt%TYPE;

      ln_BaselineInitialQt       task_sched_rule.def_initial_qt%TYPE;
      ln_BaselineDeadlineChanged task_sched_rule.def_postfixed_qt%TYPE;
      ln_BaselineDeadlineExists  task_sched_rule.def_postfixed_qt%TYPE;
      ln_SameTaskDefn            NUMBER;
BEGIN
   -- initialize variables
   on_Return               := icn_NoProc;
   an_DeleteActualDealine  := 0;
   an_UpdateActualDeadline := 0;
   an_InsertActualDeadline := 0;

   /* see if the two tasks are on the same definition */
   SELECT COUNT(*)
   INTO   ln_SameTaskDefn
   FROM   sched_stask prev_sched,
          task_task   prev_task,
          task_task
   WHERE  prev_sched.sched_db_id = an_PrevSchedDbId AND
          prev_sched.sched_id    = an_PrevSchedId
          AND
          -- task definition of the previous task
          prev_task.task_db_id = prev_sched.task_db_id AND
          prev_task.task_id    = prev_sched.task_id
          AND
          -- task definition of the current task
          task_task.task_db_id = an_TaskDbId AND
          task_task.task_id    = an_TaskId
          AND
          (
             -- for config slot based task definition
             (
                task_task.assmbl_db_id  = prev_task.assmbl_db_id AND
                task_task.assmbl_cd     = prev_task.assmbl_cd    AND
                task_task.assmbl_bom_id = prev_task.assmbl_bom_id
             )
             -- for part number based task definition
             OR
             (
                task_task.assmbl_db_id IS NULL AND
                EXISTS
                (
                   SELECT 1
                   FROM
                      task_part_map,
                      task_part_map prev_task_part_map
                   WHERE
                      task_part_map.task_db_id = task_task.task_db_id AND
                      task_part_map.task_id    = task_task.task_id
                      AND
                      prev_task_part_map.task_db_id = prev_task.task_db_id AND
                      prev_task_part_map.task_id    = prev_task.task_id
                      AND
                      task_part_map.part_no_db_id = prev_task_part_map.part_no_db_id AND
                      task_part_map.part_no_id    = prev_task_part_map.part_no_id
                )
             )
          )
          AND
          ( --check if they are the same
            (prev_task.task_defn_db_id = task_task.task_defn_db_id AND
            prev_task.task_defn_id    = task_task.task_defn_id)
            OR
            (prev_task.block_chain_sdesc = task_task.block_chain_sdesc)
            OR
            ( EXISTS (SELECT 1
                      FROM   task_task all_prev,
                             task_task all_post
                      WHERE  all_post.block_chain_sdesc = task_task.block_chain_sdesc
                             AND
                             all_prev.block_chain_sdesc = prev_task.block_chain_sdesc
                             AND
                             all_post.task_defn_db_id = all_post.task_defn_db_id AND
                             all_post.task_defn_id    = all_post.task_defn_id ) )
          );

   /* get original deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_OrigTaskDbId,
            an_OrigTaskId,
            an_SchedDbId,
            an_SchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            ln_OrigIntervalQt,
            ln_OrigInitialQt,
            ln_OrigNotifyQt,
            ln_OrigDeviationQt,
            ln_OrigPrefixedQt,
            ln_OrigPostfixedQt,
            ln_OrigDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* get current deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_TaskDbId,
            an_TaskId,
            an_SchedDbId,
            an_SchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_BaselineIntervalQt,
            ln_BaselineInitialQt,
            an_BaselineNotifyQt,
            an_BaselineDeviationQt,
            an_BaselinePrefixedQt,
            an_BaselinePostfixedQt,
            ln_BaselineDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   -- if the first task in the chain or first on this inventory or first task of this definition, and the task is recurring, use initial interval
   IF (an_PrevSchedId =-1 OR an_EventsOnSameInv = 0 OR ln_SameTaskDefn = 0 ) AND an_RecurringBool = 1 AND ln_BaselineInitialQt IS NOT NULL THEN
      an_BaselineIntervalQt := ln_BaselineInitialQt;
   END IF;

      /* get existing deadline , do not refresh measurement rules */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            an_TaskDbId,
            an_TaskId,
            FALSE,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   -- If the baseline doesn't exist and the actual doesn't exists, then do nothing
   IF  ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 0 THEN
      on_Return := icn_Success;
      RETURN;

   -- If the baseline exists and the actual doesn't, then create the actual
   ELSIF ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 0 THEN
      an_InsertActualDeadline := 1;
      on_Return := icn_Success;
      RETURN;

   -- If the baseline doesn't exist and the actual does, then delete the actual
   ELSIF ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 1 THEN
      an_DeleteActualDealine := 1;
      on_Return := icn_Success;
      RETURN;

   -- if both deadlines exist, update to match the baseline
   END IF;


   --Now we know that both exist: ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 1
  BaselineDeadlinesChanged(
            an_TaskDbId,
            an_TaskId,
            an_OrigTaskDbId,
            an_OrigTaskId ,
            an_DataTypeDbId,
            an_DataTypeId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_PartNoDbId,
            an_PartNoId,
            ln_BaselineDeadlineChanged,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   IF ln_BaselineDeadlineChanged = 1 THEN
      an_UpdateActualDeadline := 1;
      as_sched_from_cd        := ls_ActualSchedFromCd;
      ad_start_dt             := ld_ActualStartDt;
      an_start_qt             := ln_ActualStartQt;

      --if the user modified the actual, keep their changes:
      IF ln_OrigDeadlineExists = 1 THEN
         -- if the user extended the actual deadline, keep the extension
         IF ln_ActualDeviationQt <> ln_OrigDeviationQt THEN
            an_BaselineDeviationQt := ln_ActualDeviationQt;
         END IF;
      END IF;
   ELSIF ls_ActualSchedFromCd <> 'CUSTOM' AND an_BaselineIntervalQt <> ln_ActualIntervalQt THEN
      an_UpdateActualDeadline := 1;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetValuesAndActionForSynch@@@'||SQLERRM || '--' || an_TaskDbId ||':' ||  an_TaskId || '--' || an_SchedDbId || ':' || an_SchedId || '--' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;

END GetValuesAndActionForSynch;
/********************************************************************************
*
* Procedure: DeleteDeadline
* Arguments:
*
*            an_SchedDbId - the actual task pk
*            an_SchedTaskId   -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*
* Return:
*
*            on_Return         - 1 is success
*
* Description: This procedue deletes a deadline form actual task
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2006
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE DeleteDeadline(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS
   BEGIN
      /* delete the deadline */
      DELETE
      FROM evt_sched_dead
      WHERE  event_db_id= an_SchedDbId AND
          event_id= an_SchedId AND
          evt_sched_dead.data_type_db_id=an_DataTypeDbId AND
          evt_sched_dead.data_type_id=an_DataTypeId;
   EXCEPTION

   WHEN OTHERS THEN
   -- Unexpected error
   on_Return := icn_Error;
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@DeleteDeadline@@@'||SQLERRM);
   RETURN;

END DeleteDeadline;

/********************************************************************************
*
* Procedure:    GetUsageParmInfo
* Arguments:
*           an_DataTypeDbId (long) - The primary key of mim data type
*           an_DataTypeId   (long) --//--
* Return:
*           as_DomainTypeCd (char) - domain type of the data type
*           al_RefMultQt    (float)- multiplier for the data type
*           as_EngUnitCd    (char) - eng unit for the data type
*           as_DataTypeCd   (char) - data type code
*           on_Return       (long) - 1 Success/Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetUsageParmInfo(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            as_DomainTypeCd   OUT mim_data_type.domain_type_cd%TYPE,
            al_RefMultQt     OUT ref_eng_unit.ref_mult_qt%TYPE,
            as_EngUnitCd  OUT mim_data_type.eng_unit_cd%TYPE,
            as_DataTypeCd   OUT mim_data_type.data_type_cd%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* select info about the mim datatype */
    SELECT mim_data_type.domain_type_cd,
             ref_eng_unit.ref_mult_qt,
             mim_data_type.eng_unit_cd,
             mim_data_type.data_type_cd
        INTO as_DomainTypeCd,
             al_RefMultQt,
             as_EngUnitCd,
             as_DataTypeCd
        FROM mim_data_type,
             ref_eng_unit
       WHERE ( mim_data_type.data_type_db_id = an_DataTypeDbId ) AND
             ( mim_data_type.data_type_id    = an_DataTypeId )
             AND
             ( ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id ) AND
             ( ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetUsageParmInfo@@@'||SQLERRM);
      RETURN;

END GetUsageParmInfo;


/********************************************************************************
*
* Procedure:    InsertDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*           an_InitialIntervalBool (boolean) -true if using task defn initial interval
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InsertDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt   IN task_sched_rule.def_interval_qt%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_NotifyQt     IN task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt  IN task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /*  insert row into the table */
    INSERT INTO evt_sched_dead(
             event_db_id,
             event_id,
             data_type_db_id,
             data_type_id,
             start_qt,
             start_dt,
             sched_from_db_id,
             sched_from_cd,
             sched_dead_qt,
             sched_dead_dt,
             interval_qt,
             notify_qt,
             deviation_qt,
             prefixed_qt,
             postfixed_qt)
      VALUES(
             an_SchedDbId,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             ad_StartQt,
             ad_StartDt,
             0,
             as_SchedFromCd,
             al_NewDeadlineQt,
             ad_NewDeadlineDt,
             an_IntervalQt,
             an_NotifyQt,
             an_DeviationQt,
             an_PrefixedQt,
             an_PostfixedQt
        );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@InsertDeadlineRow@@@'||SQLERRM);
      RETURN;

END InsertDeadlineRow;

/********************************************************************************
*
* Procedure:    UpdateDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN evt_sched_dead.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_IntervalQt   IN evt_sched_dead.interval_qt%TYPE,
            an_NotifyQt     IN evt_sched_dead.notify_qt%TYPE,
            an_DeviationQt  IN evt_sched_dead.deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* update the deadline */
    UPDATE evt_sched_dead
         SET
            sched_dead_qt              = al_NewDeadlineQt,
            sched_dead_dt              = ad_NewDeadlineDt,
            sched_dead_dt_last_updated = SYSDATE,
            start_qt                   = ad_StartQt,
            start_dt                   = ad_StartDt,
            sched_from_db_id           = 0,
            sched_from_cd              = as_SchedFromCd,
            prefixed_qt                = an_PrefixedQt,
            postfixed_qt               = an_PostfixedQt,
            interval_qt                = an_IntervalQt,
            sched_driver_bool          = 0,
            deviation_qt               = an_DeviationQt,
            notify_qt                  = an_NotifyQt
         WHERE
            event_db_id     = an_SchedDbId AND
            event_id        = an_SchedId AND
            data_type_db_id = an_DataTypeDbId AND
            data_type_id    = an_DataTypeId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDeadlineRow@@@'||SQLERRM);
      RETURN;

END UpdateDeadlineRow;

/********************************************************************************
*
* Procedure:    GetCorrectiveFaultInfo
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*
*
* Return:
*           an_FaultDbId   (long) - fault primary key
*           an_FaultId     (long) --//-
*           ad_FoundOnDate (Date) - fault raised date
*           on_Return      (long) -  1 Success/Failure
*
* Description:  This procedure returns found on date and fault associated with this task.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCorrectiveFaultInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_FaultDbId    OUT  sd_fault.fault_db_id%TYPE,
            an_FaultId      OUT  sd_fault.fault_id%TYPE,
            ad_FoundOnDate       OUT  evt_event.actual_start_dt%TYPE,
            ad_StartDt           OUT  evt_sched_dead.start_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

     /* fault info */
     CURSOR lcur_corrective_fault IS
     SELECT evt_event_rel.event_db_id,
            evt_event_rel.event_id,
            evt_event.actual_start_dt
     FROM   evt_event_rel,
            evt_event
     WHERE  evt_event_rel.rel_event_db_id = an_SchedDbId AND
            evt_event_rel.rel_event_id    = an_SchedId   AND
            evt_event_rel.rel_type_cd     = 'CORRECT'
            AND
            evt_event.event_db_id = evt_event_rel.event_db_id AND
            evt_event.event_id    = evt_event_rel.event_id
         AND
            evt_event.rstat_cd	= 0;
      lrec_corrective_fault lcur_corrective_fault%ROWTYPE;

     CURSOR lcur_custom_deadline IS
     SELECT evt_sched_dead.start_dt
     FROM   evt_sched_dead
     WHERE  evt_sched_dead.event_db_id   = an_SchedDbId AND
            evt_sched_dead.event_id      = an_SchedId   AND
            evt_sched_dead.sched_from_cd = 'CUSTOM'     AND
            evt_sched_dead.start_dt      IS NOT NULL;
      lrec_custom_deadline lcur_custom_deadline%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get fault info */
   OPEN  lcur_corrective_fault();
   FETCH lcur_corrective_fault INTO lrec_corrective_fault;
   CLOSE lcur_corrective_fault;

   an_FaultDbId:= lrec_corrective_fault.event_db_id;
   an_FaultId  := lrec_corrective_fault.event_id ;
   ad_FoundOnDate:=lrec_corrective_fault.actual_start_dt;

   OPEN  lcur_custom_deadline();
   FETCH lcur_custom_deadline INTO lrec_custom_deadline;
   IF    lcur_custom_deadline%FOUND THEN
         -- if the found on date is not the same as the custom start date
         IF TRUNC(lrec_custom_deadline.start_dt) <> TRUNC(lrec_corrective_fault.actual_start_dt) THEN
            ad_StartDt:=lrec_custom_deadline.start_dt;
         END IF;
   END IF;
   CLOSE lcur_custom_deadline;
   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCorrectiveFaultInfo@@@'||SQLERRM);
      IF lcur_custom_deadline%ISOPEN THEN CLOSE lcur_custom_deadline; END IF;
      RETURN;

END GetCorrectiveFaultInfo;


/********************************************************************************
*
* Procedure:    FindCalendarDeadlineVariables
* Arguments:
*            an_DataTypeDbId  (long)  - data type primary key
*            an_DataTypeId    (long)  --//--
*            ad_StartDt       (date)  - deadline start date
*            an_Interval      (float) - deadline interval
*            ad_NewDeadlineDt (date)  - deadline date
*
* Return:
*            ad_StartDt       (date)  - new deadline start date
*            an_Interval      (float) - new deadline interval
*            ad_NewDeadlineDt (date)  - mew deadline date
*            on_Return        (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartDt, an_Interval, ad_NewDeadlineDt
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Jonathan Clarkin
* Recent Date:    May 5, 2006
*
*********************************************************************************
*
* Copyright 2000-2006 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindCalendarDeadlineVariables(
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDt       IN OUT evt_sched_dead.start_dt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_dt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /*local variables */
   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   lb_EndOfDay                BOOLEAN;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get deadline parm info*/
   GetUsageParmInfo(
         an_DataTypeDbId,
         an_DataTypeId,
         ls_DomainTypeCd,
         ll_RefMultQt,
         ls_EngUnitCd,
         ls_DataTypeCd,
         on_Return   );
   IF on_Return < 0 THEN
     RETURN;
   END IF;

   /* Only round non Calendar Hour based dates */
   IF (NOT UPPER(ls_DataTypeCd) = 'CHR') THEN

      IF (ad_StartDt IS NOT NULL) THEN
         ad_StartDt := TO_DATE(TO_CHAR(ad_StartDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;


      /* round the all calendar deadlines to the end of the day */
      IF (ad_NewDeadlineDt IS NOT NULL)  AND lb_EndOfDay THEN
         ad_NewDeadlineDt := TO_DATE(TO_CHAR(ad_NewDeadlineDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;

   END IF;

   /* -- Calculate Deadline Date -- */
   IF (ad_NewDeadlineDt is NULL) THEN

      /* use the same function that adds the deviation to the deadline date, but instead add the interval to the start date */
      ad_NewDeadlineDt := getExtendedDeadlineDt(an_Interval, ad_StartDt, 'CA', ls_DataTypeCd, ll_RefMultQt);

   /* -- Calculate Start Date -- */
   ELSIF (ad_StartDt is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval ) - (an_Interval - TRUNC(an_Interval)) * ll_RefMultQt;

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval );

      /* if the data type is year then use years to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
          ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval*12 );

      /* add the correct # of days to the start date */
      ELSE
         ad_StartDt := ad_NewDeadlineDt  - (an_Interval * ll_RefMultQt);
      END IF;


   /* -- Calculate Interval -- */
   ELSIF (an_Interval is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' OR UPPER(ls_DataTypeCd) = 'CLMON' THEN
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= LAST_DAY(ad_StartDt);
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
         an_Interval := TRUNC( MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt )/12);

      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         an_Interval :=  (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt;

      /* add the correct # of days to the start date */
      ELSE
         an_Interval :=  TRUNC( (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt );
      END IF;

   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindCalendarDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindCalendarDeadlineVariables;


/********************************************************************************
*
* Procedure: FindUsageDeadlineVariables
* Arguments:
*            ad_StartQt        (float)  - deadline start qt
*            an_Interval       (float)  - deadline interval
*            ad_NewDeadlineTSN (float)  - deadline TSN due value
*
* Return:
             ad_StartDt      (date)  - new deadline start date
*            an_Interval     (float) - new deadline interval
*            ad_NewDeadlineDt (date) - mew deadline TSN due value
*            on_Return       (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartQt, an_Interval, ad_NewDeadlineTSN
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindUsageDeadlineVariables(
            ad_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineTSN IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

       /* recalculate deadline TSN */
       IF (ad_NewDeadlineTSN is NULL) THEN
            ad_NewDeadlineTSN := NVL( ad_StartQt, 0 ) + NVL( an_Interval, 0 );
       END IF;
       IF (ad_StartQt is NULL) THEN
            ad_StartQt := ad_NewDeadlineTSN  - NVL( an_Interval, 0 );
       END IF;
       IF (an_Interval is NULL) THEN
            an_Interval :=  ad_NewDeadlineTSN - ad_StartQt;
       END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindUsageDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindUsageDeadlineVariables;

/********************************************************************************
*
* Procedure: GetOldEvtInvUsage
* Arguments:
*           an_EventDbId    (long)  - event primary key
*           an_EventId      (long)  --//--
*           an_DataTypeDbId (long)  - deadline data type primary key
*           an_DataTypeId   (long)  --//--
*
* Return:
*           an_SnapshotTSN (date) - TSN snapshot
*           on_Return       - 1 is success
*
* Description: This procedure returns due and completion dates.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetOldEvtInvUsage(
            an_EventDbId    IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId      IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_SnapshotTSN OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

  CURSOR lcur_old_evt_inv_usage (
         cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
         cn_EventId        evt_sched_dead.event_id%TYPE,
         cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
         cn_DataTypeId     evt_sched_dead.data_type_id%TYPE

      ) IS
         SELECT
         evt_inv_usage.tsn_qt,
         evt_inv_usage.event_inv_id
      FROM
         evt_inv_usage
      WHERE
         evt_inv_usage.event_db_id     (+)= cn_EventDbId AND
         evt_inv_usage.event_id        (+)= cn_EventId AND
         evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId AND
         evt_inv_usage.data_type_id    (+)= cn_DataTypeId
         ORDER BY  evt_inv_usage.event_inv_id DESC;
         lrec_old_evt_inv_usage lcur_old_evt_inv_usage%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*get snapshot for the event*/
   OPEN  lcur_old_evt_inv_usage(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_old_evt_inv_usage INTO lrec_old_evt_inv_usage;
   CLOSE lcur_old_evt_inv_usage;


   an_SnapshotTSN:= lrec_old_evt_inv_usage.tsn_qt;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetOldEvtInvUsage@@@'||SQLERRM);
      RETURN;

END GetOldEvtInvUsage;

/********************************************************************************
*
* Procedure: GetMainInventoryBirthInfo
* Arguments:
*             an_SchedDbId (long) - task primary key
*             an_SchedId   (long) --//--
*
* Return:
*             ad_ManufactDt   (date) --  manufacturing date of the inventory
*             ad_ReceivedDt   (date) --  received date of the inventory
*             on_Return       - 1 is success
*
* Description: This procedure returns manufacturing and received dates of the inventory.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventoryBirthInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            ad_ManufactDt   OUT  inv_inv.received_dt%TYPE,
            ad_ReceivedDt OUT inv_inv.received_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

  SELECT inv_inv.received_dt,
                 inv_inv.manufact_dt
          INTO ad_ReceivedDt,
               ad_ManufactDt
          FROM inv_inv,
               evt_inv
          WHERE inv_inv.inv_no_db_id (+) = evt_inv.inv_no_db_id
            AND inv_inv.inv_no_id    (+) = evt_inv.inv_no_id
            AND inv_inv.rstat_cd	 (+) = 0
            AND evt_inv.main_inv_bool    = 1
            AND evt_inv.event_db_id      = an_SchedDbId
            AND evt_inv.event_id         = an_SchedId;

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetMainInventoryBirthInfo@@@'||SQLERRM);
      RETURN;

END GetMainInventoryBirthInfo;

/********************************************************************************
*
* Procedure: AreTasksOnTheSameInventory
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_PrevSchedDbId  (long) - previous task primary key
*            an_PrevSchedId    (long) --//--
* Return:
*             ll_EventsOnSameInv  (long) -- 1 if tasks are on the same inventory, 0 if not.
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns 1 if tasks are on the same inventory 0 if they are not.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AreTasksOnTheSameInventory(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_PrevSchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_PrevSchedId      IN  sched_stask.sched_id%TYPE,
            ll_EventsOnSameInv   OUT  NUMBER,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT COUNT(*)
              INTO ll_EventsOnSameInv
              FROM evt_inv    new_evt_inv,
                   evt_inv    previous_evt_inv
             WHERE ( new_evt_inv.event_db_id = an_SchedDbId ) AND
                   ( new_evt_inv.event_id    = an_SchedId ) AND
                   ( new_evt_inv.main_inv_bool = 1 )
                   AND
                   ( previous_evt_inv.event_db_id = an_PrevSchedDbId ) AND
                   ( previous_evt_inv.event_id    = an_PrevSchedId ) AND
                   ( previous_evt_inv.main_inv_bool = 1 )
                   AND
                   ( new_evt_inv.inv_no_db_id = previous_evt_inv.inv_no_db_id ) AND
                   ( new_evt_inv.inv_no_id    = previous_evt_inv.inv_no_id );

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@AreTasksOnTheSameInventory@@@'||SQLERRM);
      RETURN;

END AreTasksOnTheSameInventory;


/********************************************************************************
*
* Procedure: GetCurrentInventoryUsage
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_DataTypeDbId   (long) data type primary key
             an_DataTypeId      (long) --//--
* Return:
*             an_TsnQt (float) -- TSN value from the current inventory
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns current inventory usage TSN value.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCurrentInventoryUsage(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_TsnQt OUT inv_curr_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS
     xc_DataTypeNotOnInv  EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

     SELECT inv_curr_usage.tsn_qt
                INTO an_TsnQt
                FROM evt_inv,
                     inv_curr_usage
                WHERE ( evt_inv.event_db_id = an_SchedDbId ) AND
                      ( evt_inv.event_id    = an_SchedId )   AND
                      ( evt_inv.main_inv_bool = 1 )
                       AND
                      ( inv_curr_usage.data_type_db_id = an_DataTypeDbId )AND
                      ( inv_curr_usage.data_type_id = an_DataTypeId )    AND
                      ( inv_curr_usage.inv_no_db_id = evt_inv.inv_no_db_id )AND
                      ( inv_curr_usage.inv_no_id    = evt_inv.inv_no_id );

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
               RAISE xc_DataTypeNotOnInv;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCurrentInventoryUsage@@@'||SQLERRM);
      RETURN;

END GetCurrentInventoryUsage;



/********************************************************************************
*
* Procedure: GetTaskDetails
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
* Return:
*           an_PrevSchedDbId  (long) - previous task primary key
*           an_PrevSchedId    (long) --//--
*           an_TaskDbId       (long) - this task task definition primary key
*           an_TaskId         (long) --//--
*           an_PartNoDbId     (long) - this tasks main inventory part primary key
*           an_PartNoId       (long) --//--
*           ab_RelativeBool   (int) - 1 if this task is relative
*           ab_SchedFromLatest   (int) - 1 if this task is scheduled from latest
*           ad_EffectiveDt    (date) - effective date of the task definition
*           on_Return       -   (int) 1 is success
*
* Description: This procedure returns task details
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Yungjae Cho
* Recent Date:    2011-03-15
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetTaskDetails(
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE,
            an_PrevSchedDbId  OUT sched_stask.sched_db_id%TYPE,
            an_PrevSchedId    OUT sched_stask.sched_id%TYPE,
            an_TaskDbId       OUT task_interval.task_db_id%TYPE,
            an_TaskId         OUT task_interval.task_id%TYPE,
            an_HInvNoDbId     OUT task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       OUT task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     OUT task_interval.part_no_db_id%TYPE,
            an_PartNoId       OUT task_interval.part_no_id%TYPE,
            ab_RelativeBool   OUT task_task.relative_bool%TYPE,
            ab_SchedFromLatest OUT task_task.sched_from_latest_bool%TYPE,
            ad_EffectiveDt    OUT task_task.effective_dt%TYPE,
            av_ReschedFromCd  OUT task_task.resched_from_cd%TYPE,
            an_RecurringBool  OUT task_task.recurring_task_bool%TYPE,
            an_SchedFromReceivedDtBool OUT task_task.sched_from_received_dt_bool%TYPE,
            as_TaskClassCd    OUT task_task.task_class_cd%TYPE,
            on_Return         OUT typn_RetCode

   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
         sched_stask.task_db_id,
         sched_stask.task_id,
         DECODE( evt_event_rel.event_db_id, NULL, -1, evt_event_rel.event_db_id ) AS prev_sched_db_id,
         DECODE( evt_event_rel.event_id,    NULL, -1, evt_event_rel.event_id )    AS prev_sched_id,
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         evt_inv.part_no_db_id,
         evt_inv.part_no_id,
         task_task.relative_bool,
         task_task.sched_from_latest_bool,
         task_task.effective_dt,
         task_task.resched_from_cd,
         istaskdefnrecurring(sched_stask.task_db_id, sched_stask.task_id) AS recurring_task_bool,
         task_task.sched_from_received_dt_bool,
         task_task.task_class_cd
      INTO
         an_TaskDbId,
         an_TaskId,
         an_PrevSchedDbId,
         an_PrevSchedId,
         an_HInvNoDbId,
         an_HInvNoId,
         an_PartNoDbId,
         an_PartNoId,
         ab_RelativeBool,
         ab_SchedFromLatest,
         ad_EffectiveDt,
         av_ReschedFromCd,
         an_RecurringBool,
         an_SchedFromReceivedDtBool,
         as_TaskClassCd
      FROM
         sched_stask,
         evt_event_rel,
         evt_inv,
         evt_event,
         task_task
      WHERE
         sched_stask.sched_db_id = an_SchedDbId AND
         sched_stask.sched_id    = an_SchedId
         AND
         sched_stask.rstat_cd	= 0
         AND
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
         AND
         evt_event_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
         evt_event_rel.rel_event_id    (+)= sched_stask.sched_id    AND
         evt_event_rel.rel_type_cd     (+)= 'DEPT'
         AND
         evt_inv.event_db_id   = sched_stask.sched_db_id AND
         evt_inv.event_id      = sched_stask.sched_id    AND
         evt_inv.main_inv_bool = 1
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id AND
         task_task.task_id    (+)= sched_stask.task_id;

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTaskDetails@@@'||SQLERRM);
      RETURN;

END GetTaskDetails;



/********************************************************************************
*
* Procedure:    UpdateDependentDeadlinesTree
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the given task and its children plus forecasted tasks that follow them.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlinesTree(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   /* SQL to retrieve all children of the task */
   CURSOR lcur_TaskTree (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         event_db_id sched_db_id,
         event_id    sched_id
      FROM
         evt_event
      WHERE rstat_cd = 0
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId
      CONNECT BY
         nh_event_db_id = PRIOR event_db_id AND
         nh_event_id    = PRIOR event_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the children of the task*/
   FOR lrec_TasksTree IN lcur_TaskTree( an_StartSchedDbId, an_StartSchedId )
   LOOP

      UpdateDependentDeadlines( lrec_TasksTree.sched_db_id, lrec_TasksTree.sched_id, on_Return );
      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlinesTree@@@: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlinesTree;


/********************************************************************************
*
* Procedure: GetRescheduleFromDtValues
* Arguments:an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*
* Return:
*           ad_StartDt     (date)    - current task's start date
*           av_SchedFromCd (varchar) - actual task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromDtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            an_ServiceCheck IN NUMBER,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,                 
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_sched_dead.sched_dead_dt AS task_due_dt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           ref_eng_unit.ref_mult_qt,
           mim_data_type.data_type_cd,
           evt_event.event_dt AS task_end_dt,
           evt_event.hist_bool AS task_hist_bool,
           root_event.actual_start_gdt,
           root_event.sched_start_gdt,
           root_event.event_gdt,
           root_event.sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           sched_stask.task_class_cd          
        FROM
           evt_sched_dead,
           evt_event,
           evt_event root_event,
           sched_stask,
           mim_data_type,
           ref_eng_unit
        WHERE
           evt_event.event_db_id =  cn_EventDbId AND
           evt_event.event_id    =  cn_EventId AND
           evt_event.rstat_cd	   = 0
           AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
           mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
           AND
           ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
           ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id
           AND
           sched_stask.sched_db_id = root_event.event_db_id AND
           sched_stask.sched_id    = root_event.event_id;
   lrec_task lcur_task%ROWTYPE;

   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;

   ld_DueDate          evt_sched_dead.sched_dead_dt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ld_EndDate          evt_event.event_gdt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;
   ll_RefMultQt        ref_eng_unit.ref_mult_qt%TYPE;
   lv_DataTypeCd       mim_data_type.data_type_cd%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   lv_TaskClassCd      sched_stask.task_class_cd%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;

   ld_BeginWindowDt    evt_sched_dead.sched_dead_dt%TYPE;
   ld_EndWindowDt      evt_sched_dead.sched_dead_dt%TYPE;
   
BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ld_StartDt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ld_DueDate          := lrec_task.task_due_dt;
      ll_RefMultQt        := lrec_task.ref_mult_qt;
      lv_DataTypeCd       := lrec_task.data_type_cd;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ld_EndDate          := lrec_task.task_end_dt;
      lb_HistBool         := lrec_task.task_hist_bool;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_TaskClassCd      := lrec_task.task_class_cd;
      ld_RootEndDt        := lrec_task.event_gdt;
      ld_RootStartDt      := lrec_task.actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.sched_start_gdt;      
   CLOSE lcur_task;
   
  
   -- the previous task is ACTV, so use the extended due date if given
   IF lb_HistBool = 0 AND ld_DueDate IS NOT NULL THEN
      IF an_ServiceCheck = 0 THEN
        lv_ReschedFromCd := 'LASTDUE';
        ld_StartDt       := getExtendedDeadlineDt( ln_Deviation,
                                                   ld_DueDate,
                                                   'CA',
                                                   lv_DataTypeCd,
                                                   ll_RefMultQt );
      ELSE     
         IF ( lv_ReschedFromCd = 'WPEND' AND
                   lb_RootHistBool = 0 AND
                   ld_RootSchedEndDt IS NOT NULL ) THEN
            ld_StartDt := ld_RootSchedEndDt; 
         ELSIF
             ( lv_ReschedFromCd = 'WPSTART' AND
                lb_RootHistBool = 0 AND
                ld_RootSchedStartDt IS NOT NULL ) THEN
             ld_StartDt := ld_RootSchedStartDt; 
          ELSE
             lv_ReschedFromCd := 'LASTDUE';
             ld_StartDt       := getExtendedDeadlineDt( ln_Deviation,
                                                     ld_DueDate,
                                                     'CA',
                                                     lv_DataTypeCd, ll_RefMultQt );
          END IF;         
      END IF; 


   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF NOT (lv_TaskClassCd = 'CHECK' OR lv_TaskClassCd = 'RO') THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      IF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedStartDt;
      ELSIF ld_EndDate IS NOT NULL THEN
         ld_StartDt       := ld_EndDate;
         lv_ReschedFromCd := 'LASTEND';
      END IF;

      /* (now consider the scheduling window)
       * if the completion fell within the window, use scheduled completion information */
      IF ld_StartDt IS NOT NULL AND ld_DueDate IS NOT NULL THEN
         -- build window
         ld_BeginWindowDt := ld_DueDate - (ln_PrefixedQt * ll_RefMultQt);
         ld_EndWindowDt   := ld_DueDate + (ln_PostfixedQt * ll_RefMultQt);
         IF ( ld_StartDt >= ld_BeginWindowDt ) AND
            ( ld_StartDt <= ld_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ld_StartDt     := ld_DueDate;
         END IF;
      END IF;
  END IF;

  /* if we were able to determin the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ld_StartDt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     ad_StartDt     := ld_StartDt;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     ad_StartDt     := SYSDATE;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromDtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromDtValues;

/********************************************************************************
*
* Procedure:    FindDeadlineStartDate
* Arguments:     an_TaskDbId     (long)     - task definition primary key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_RelativeBool (int)      - boolean
*                ab_SchedFromLatest (int)   - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                an_FromRcvdDt   (int)      - boolean, schedule from received date
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                av_TaskClassCd  (varchar) - task class code
* Return:
*                ad_StartDt      (long)    - new deadline start dt
*                av_SchedFromCd  (long)    - new deadline sched from refterm
*                on_Return       (long)    - succss/failure of procedure
*
* Description:  This procedure looks up the start date for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Yungjae Cho
* Recent Date:  2011-03-15
*
*********************************************************************************
*
* Copyright ? 2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartDate (
      an_TaskDbId         IN task_task.task_db_id%TYPE,
      an_TaskId           IN task_task.task_id%TYPE,
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId    IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId      IN sched_stask.sched_id%TYPE,
      ab_RelativeBool     IN task_task.relative_bool%TYPE,
      ab_SchedFromLatest  IN task_task.sched_from_latest_bool%TYPE,
      ad_EffectiveDt      IN task_task.effective_dt%TYPE,
      av_ReschedFromCd    IN task_task.resched_from_cd%TYPE,
      an_FromRcvdDt       IN task_task.sched_from_received_dt_bool%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      av_TaskClassCd      IN task_task.task_class_cd%TYPE,
      ad_StartDt          IN OUT evt_sched_dead.start_dt%TYPE,
      av_SchedFromCd      IN OUT evt_sched_dead.sched_from_cd%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

   /* local variables */

   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ld_CustomDate              DATE;
   ln_FaultDbId   sd_fault.fault_db_id%TYPE;
   ln_FaultId sd_fault.fault_id%TYPE;
   ld_FoundOnDate evt_event.actual_start_dt%TYPE;
   ld_StartDt     evt_sched_dead.start_dt%TYPE;
   ln_ServiceCheck            NUMBER;
   ls_TaskModeCd              ref_task_class.class_mode_cd%TYPE;

BEGIN

  /* Find the task mode cd for the task class cd */
  SELECT
     ref_task_class.class_mode_cd
  INTO
     ls_TaskModeCd
  FROM
     ref_task_class
  WHERE
     ref_task_class.task_class_cd = av_TaskClassCd;
     
   /* set the variable to false*/
   ln_ServiceCheck := 0;
     
   /* If the task is BLOCK, check if it is the service check task*/
  IF ls_TaskModeCd = 'BLOCK' THEN
     ln_ServiceCheck := lpa_pkg.is_task_service_check( an_SchedDbId, an_SchedId );
   /* If the task is REQ, check if the parent task of the req is the service check task*/
   ELSIF ls_TaskModeCd = 'REQ' THEN
      ln_ServiceCheck := lpa_pkg.is_task_parent_service_check( an_SchedDbId, an_SchedId );
  END IF;
  
   /* if this task is not a first task */
   IF NOT an_PrevSchedDbId =-1 THEN
      /* set reschedule_from */
      GetRescheduleFromDtValues(
            an_PrevSchedDbId  ,
            an_PrevSchedId ,
            an_DataTypeDbId,
            an_DataTypeId ,
            av_ReschedFromCd,
            ln_ServiceCheck,
            ad_StartDt,
            av_SchedFromCd,           
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* if there is no previous task */
   ELSE
      ld_CustomDate := TO_DATE('1950-01-01 23:59:59', 'YYYYY-MM-DD HH24:MI:SS');

      /* check if this task has a corrective fault */
      GetCorrectiveFaultInfo(
            an_SchedDbId,
            an_SchedId,
            ln_FaultDbId,
            ln_FaultId,
            ld_FoundOnDate,
            ld_StartDt,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      /* if there is a corrective fault */
      IF (ln_FaultId IS NOT NULL) THEN
            IF ld_StartDt IS NOT NULL THEN
                ad_StartDt:=ld_StartDt;
            ELSE
                ad_StartDt:=ld_FoundOnDate;
            END IF;
            av_SchedFromCd:='CUSTOM';
      /* if this task is adhoc or REPL or CORR */
      ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
            (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
            ad_StartDt     := ld_CustomDate;
            av_SchedFromCd := 'CUSTOM';
      /* if this task is based on the task definition */
      ELSE
         /* get main inventory manufacturing and received date */
        GetMainInventoryBirthInfo(
          an_SchedDbId,
          an_SchedId,
          ld_ManufactDt,
          ld_ReceivedDt,
          on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        /* if this task should not be scheduled from birth */
        IF ( ab_RelativeBool = 1 ) THEN
           /** if we should schedule from date provided */
           IF ad_PreviousCompletionDt IS NOT NULL THEN
              ad_StartDt     := ad_PreviousCompletionDt;
              av_SchedFromCd := 'CUSTOM';
           ELSIF ad_EffectiveDt IS NOT NULL THEN
              /* always use effective date if sched_from_latest_bool is false */
              IF ( ab_SchedFromLatest = 0 ) THEN
                 ad_StartDt     := ad_EffectiveDt;
                 av_SchedFromCd := 'EFFECTIV';
              /* if there is effective date for the task definition and it is not before the manufactured date */
              ELSIF ( ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt)) THEN
                 ad_StartDt     := ad_EffectiveDt;
                 av_SchedFromCd := 'EFFECTIV';
              /* if the effective date is before the manufactured date, use the manufactured date */
              ELSIF ( ld_ManufactDt IS NOT NULL AND ld_ManufactDt > ad_EffectiveDt ) THEN
                 ad_StartDt     := ld_ManufactDt;
                 av_SchedFromCd := 'EFFECTIV';
              ELSE
                 ad_StartDt     := ld_CustomDate;
                 av_SchedFromCd := 'EFFECTIV';
              END IF;
           ELSE
              /* task should be scheduled from effective date but effective date is null */
              ad_StartDt     := ld_CustomDate;
              av_SchedFromCd := 'EFFECTIV';
           END IF;
        /* if the task should be scheduled from birth*/
        ELSE

          /* if the task is a create_on_install task, and a completion date of the
             installation task was provided, then use that date */
            IF ad_PreviousCompletionDt IS NOT NULL THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
            ELSIF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL THEN
               /* schedule from received date */
               ad_StartDt     := ld_ReceivedDt;
               av_SchedFromCd := 'BIRTH';
            ELSIF an_FromRcvdDt=0 AND ld_ManufactDt IS NOT NULL THEN
               /* schedule from manufacturer date */
               ad_StartDt     := ld_ManufactDt;
               av_SchedFromCd := 'BIRTH';
            ELSE
               /* scheduled from birth but appropriate date is null */
               ad_StartDt     := ld_CustomDate;
               av_SchedFromCd := 'BIRTH';
            END IF;
        END IF;
      END IF;
     END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartDate@@@ '||SQLERRM);
      RETURN;
END FindDeadlineStartDate;

/********************************************************************************
*
* Procedure: GetHistoricUsageAtDt
* Arguments:
*            ad_TargetDate     (Date) - usage at this date
*            an_DataTypeDbId   (long) data type primary key
*            an_DataTypeId     (long) --//--
*            an_InvNoDbId      (long) inventory primary key
*            an_InvNoId        (long) --//--
* Return:
*             an_TsnQt         (float) - TSN value at the target date
*             on_Return        (long) 1 is success
*
* Description: This procedure returns the TSN value of the specified inventory
*              at the specified target date.
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetHistoricUsageAtDt(
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
      tsn_qt INTO on_TsnQt
    FROM
   (

      SELECT
         tsn_qt
      FROM
      (
         SELECT
            evt_inv_usage.tsn_qt,
            evt_event.event_dt,
            evt_event.creation_dt
         FROM
            evt_inv_usage,
            inv_inv,
            inv_inv component_inv,
            evt_inv,
            evt_event
         WHERE
            inv_inv.inv_no_db_id = an_InvNoDbId AND
            inv_inv.inv_no_id    = an_InvNoId
            AND
            -- for TRK there is no historic usage
            inv_inv.inv_class_cd <> 'TRK'
            AND
            inv_inv.rstat_cd     = 0
            AND
            (
                  -- for SYS, use assembly to get usage
               (
                  inv_inv.inv_class_cd = 'SYS' AND
                  component_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                  component_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
               )
               OR
                  -- for ACFT or ASSY, use itself
               (
                  inv_inv.inv_class_cd IN ('ACFT', 'ASSY') AND
                  component_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                  component_inv.inv_no_id    = inv_inv.inv_no_id
               )
            )
            AND
            evt_inv.inv_no_db_id = component_inv.inv_no_db_id AND
            evt_inv.inv_no_id    = component_inv.inv_no_id
            AND
            evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
            evt_inv_usage.event_id     = evt_inv.event_id AND
            evt_inv_usage.event_inv_id = evt_inv.event_inv_id AND
            evt_inv_usage.data_type_db_id = an_DataTypeDbId AND
            evt_inv_usage.data_type_id    = an_DataTypeId AND
            evt_inv_usage.negated_bool    = 0
            AND
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id AND
            evt_event.hist_bool   = 1 AND
            evt_event.event_dt <= ad_TargetDate AND
            evt_event.event_type_cd = 'FG'

         UNION

           SELECT
              usg_usage_data.tsn_qt,
               usg_usage_record.usage_dt as event_dt,
               usg_usage_record.creation_dt as creation_dt
           FROM
              inv_inv,
               inv_inv component_inv,
               usg_usage_data,
               usg_usage_record
           WHERE
               inv_inv.inv_no_db_id = an_InvNoDbId AND
               inv_inv.inv_no_id    = an_InvNoId AND
               -- for TRK there is no historic usage
               inv_inv.inv_class_cd <> 'TRK'
               AND
               inv_inv.rstat_cd     = 0
               AND
               (
                   -- for SYS or TRK, use assembly to get usage
                   (
                   inv_inv.inv_class_cd = 'SYS' AND
                   component_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                   component_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
                   )
                   OR
                   -- for ACFT or ASSY, use itself
                   (
                   inv_inv.inv_class_cd IN ('ACFT', 'ASSY') AND
                   component_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                   component_inv.inv_no_id    = inv_inv.inv_no_id
                   )
               )
               AND
               usg_usage_data.inv_no_db_id = component_inv.inv_no_db_id AND
               usg_usage_data.inv_no_id    = component_inv.inv_no_id
               AND
               usg_usage_data.data_type_db_id = an_DataTypeDbId AND
               usg_usage_data.data_type_id    = an_DataTypeId AND
               usg_usage_data.negated_bool    = 0
               AND
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt <= ad_TargetDate
           )
           ORDER BY event_dt DESC,creation_dt DESC
      )
   WHERE
      rownum = 1;

   -- Return success
   on_Return := icn_Success;

  EXCEPTION
   WHEN NO_DATA_FOUND THEN
      on_TsnQt := 0;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetHistoricUsageAtDt@@@'||SQLERRM);
   RETURN;

END GetHistoricUsageAtDt;


/********************************************************************************
*
* Procedure: GetRescheduleFromQtValues
* Arguments:
*           an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*           an_InvNoDbId     (long)    - current task's main inventory primary key
*           an_InvNoId       (long)    --//--
* Return:
*           ad_StartDt     (date)    - current task's start date
*           ad_StartQt     (date)    - current task's start quantity
*           av_SchedFromCd (varchar) - current task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for the next task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromQtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            an_InvNoDbId     IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId       IN  inv_inv.inv_no_id%TYPE,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt       OUT evt_sched_dead.start_qt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_inv_usage.tsn_qt,
           evt_sched_dead.sched_dead_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_event.hist_bool AS task_hist_bool,
           root_event.actual_start_gdt,
           root_event.sched_start_gdt,
           root_event.event_gdt,
           root_event.sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           sched_stask.task_class_cd
        FROM
           evt_sched_dead,
           evt_event,
           evt_inv_usage,
           evt_event root_event,
           sched_stask
        WHERE
           evt_event.event_db_id =  cn_EventDbId AND
           evt_event.event_id    =  cn_EventId   AND
           evt_event.rstat_cd	   = 0
           AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id    AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId       AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           evt_inv_usage.event_db_id     (+)= evt_event.event_db_id AND
           evt_inv_usage.event_id        (+)= evt_event.event_id    AND
           evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId       AND
           evt_inv_usage.data_type_id    (+)= cn_DataTypeId
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id
           AND
           sched_stask.sched_db_id = root_event.event_db_id AND
           sched_stask.sched_id    = root_event.event_id;
   lrec_task lcur_task%ROWTYPE;


   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;
   ln_StartQt          evt_sched_dead.start_qt%TYPE;

   ln_DueQt            evt_sched_dead.sched_dead_qt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ln_EndQt            evt_inv_usage.tsn_qt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   lv_TaskClassCd      sched_stask.task_class_cd%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;

   ln_BeginWindowDt    evt_sched_dead.sched_dead_qt%TYPE;
   ln_EndWindowDt      evt_sched_dead.sched_dead_qt%TYPE;
BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ln_StartQt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ln_DueQt            := lrec_task.sched_dead_qt;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ln_EndQt            := lrec_task.tsn_qt;
      lb_HistBool         := lrec_task.task_hist_bool;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_TaskClassCd      := lrec_task.task_class_cd;
      ld_RootEndDt        := lrec_task.event_gdt;
      ld_RootStartDt      := lrec_task.actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.sched_start_gdt;
   CLOSE lcur_task;

   -- the previous task is ACTV, so use the extended due date if given
   IF lb_HistBool = 0 AND ln_DueQt IS NOT NULL THEN
      lv_ReschedFromCd := 'LASTDUE';
      ln_StartQt       := ln_DueQt + ln_Deviation;

   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF NOT (lv_TaskClassCd = 'CHECK' OR lv_TaskClassCd = 'RO') THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      IF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedStartDt;
      ELSIF ln_EndQt IS NOT NULL THEN
         ln_StartQt       := ln_EndQt;
         lv_ReschedFromCd := 'LASTEND';
      END IF;

      -- lookup the usage at the date
      IF ln_StartQt IS NULL AND ld_StartDt IS NOT NULL THEN
         GetHistoricUsageAtDt ( ld_StartDt,
                                an_DataTypeDbId,
                                an_DataTypeId,
                                an_InvNoDbId,
                                an_InvNoId,
                                ln_StartQt,
                                on_Return );
      END IF;

       /* (now consider the scheduling window)
       * if the completion fell within the window, use scheduled completion information */
      IF ln_StartQt IS NOT NULL AND ln_DueQt IS NOT NULL THEN
         -- build window
         ln_BeginWindowDt := ln_DueQt - ln_PrefixedQt;
         ln_EndWindowDt   := ln_DueQt + ln_PostfixedQt;
         IF ( ln_StartQt >= ln_BeginWindowDt ) AND
            ( ln_StartQt <= ln_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ln_StartQt       := ln_DueQt;
         END IF;
      END IF;
   END IF;

  /* if we were able to determin the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ln_StartQt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     an_StartQt     := ln_StartQt;
     -- a future start date indicates that we want to use the forecasting engine,
     -- and in this case we do not.
     IF ld_StartDt IS NOT NULL AND ld_StartDt <= SYSDATE THEN
        ad_StartDt     := ld_StartDt;
     END IF;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     GetCurrentInventoryUsage(
           an_EventDbId,
           an_EventId,
           an_DataTypeDbId,
           an_DataTypeId,
           an_StartQt,
           on_Return );
     IF on_Return < 0 THEN
        RETURN;
     END IF;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromQtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromQtValues;

/********************************************************************************
* Procedure:    AreTasksOnSameUsageInformation
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_PrevSchedDbId (long) - previous task primary key
*                an_PrevSchedId   (long) --//--
* Return:
*                on_EventsShareUsageInfo    (long) - 1 if tasks share the same usage information, otherwise 0
*
* Description:  This procedure checks if the tasks share usage information
*********************************************************************************/
PROCEDURE AreTasksOnSameUsageInformation(
	   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
	   an_SchedId   IN sched_stask.sched_id%TYPE,
	   an_PrevSchedDbId IN sched_stask.sched_db_id%TYPE,
	   an_PrevSchedId   IN sched_stask.sched_id%TYPE,
	   on_EventsShareUsageInfo OUT NUMBER
) IS
   CURSOR lcur_GetInvInfo (
      ln_SchedDbId IN sched_stask.sched_db_id%TYPE,
      ln_SchedId   IN sched_stask.sched_id%TYPE
   ) IS
   SELECT
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id,
      CASE WHEN inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY' THEN
         inv_inv.inv_no_db_id
      ELSE
         inv_inv.assmbl_inv_no_db_id
      END AS assmbl_inv_no_db_id,
      CASE WHEN inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY' THEN
         inv_inv.inv_no_id
      ELSE
         inv_inv.assmbl_inv_no_id
      END AS assmbl_inv_no_id,
      inv_inv.inv_class_db_id,
      inv_inv.inv_class_cd
   FROM
      sched_stask
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
         inv_inv.inv_no_id    = sched_stask.main_inv_no_id
   WHERE
      sched_stask.sched_db_id = ln_SchedDbId AND
      sched_stask.sched_id    = ln_SchedId
   ;
   
   lrec_CurrInvInfo lcur_GetInvInfo%ROWTYPE;
   lrec_PrevInvInfo lcur_GetInvInfo%ROWTYPE;
   
BEGIN
   on_EventsShareUsageInfo := 0;
   
   OPEN lcur_GetInvInfo(an_SchedDbId, an_SchedId);
   FETCH lcur_GetInvInfo INTO lrec_CurrInvInfo;
   CLOSE lcur_GetInvInfo;
   
   OPEN lcur_GetInvInfo(an_PrevSchedDbId, an_PrevSchedId);
   FETCH lcur_GetInvInfo INTO lrec_PrevInvInfo;
   CLOSE lcur_GetInvInfo;
   
   -- if both inventory are the same
   IF lrec_CurrInvInfo.inv_no_db_id = lrec_PrevInvInfo.inv_no_db_id AND
      lrec_CurrInvInfo.inv_no_id    = lrec_PrevInvInfo.inv_no_id THEN
      -- then they share the same usage information
      on_EventsShareUsageInfo := 1;
      RETURN;
   END IF;

   -- if both inventory are on the same assembly   
   IF lrec_CurrInvInfo.assmbl_inv_no_db_id = lrec_PrevInvInfo.assmbl_inv_no_db_id AND
      lrec_CurrInvInfo.assmbl_inv_no_id    = lrec_PrevInvInfo.assmbl_inv_no_id THEN
      -- and the inventory uses the assembly's usage
      IF lrec_CurrInvInfo.inv_class_db_id = 0 AND
         lrec_CurrInvInfo.inv_class_cd IN ('ACFT', 'ASSY', 'SYS') THEN
         IF lrec_PrevInvInfo.inv_class_db_id = 0 AND
            lrec_PrevInvInfo.inv_class_cd IN ('ACFT', 'ASSY', 'SYS') THEN
            -- then they share the same usage information
            on_EventsShareUsageInfo := 1;
            RETURN;
         END IF;
      END IF;
   END IF;
END AreTasksOnSameUsageInformation;

/********************************************************************************
*
* Procedure:    FindDeadlineStartQt
* Arguments:     an_TaskDbId     (long)     - task definition key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_RelativeBool (int)      - boolean
*                ab_SchedFromLatest (int)   - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                an_FromRcvdDt   (int)      - boolean, schedule from received date
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                av_TaskClassCd  (varchar)  - task class code
* Return:
*                ad_StartQt      (long)     - new deadline start qt
*                av_SchedFromCd  (long)     - new deadline sched from refterm
*                ad_StartDt      (long)     - new deadline start dt
*                on_Return       (long)     - succss/failure of procedure
*
* Description:  This procedure looks up the start qt and sched_from_cd for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Yungjae Cho
* Recent Date:  2011-03-15
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartQt (
      an_TaskDbId             IN task_task.task_db_id%TYPE,
      an_TaskId               IN task_task.task_id%TYPE,
      an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
      an_SchedId              IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId         IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId           IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId          IN sched_stask.sched_id%TYPE,
      ab_RelativeBool         IN task_task.relative_bool%TYPE,
      ab_SchedFromLatest      IN task_task.sched_from_latest_bool%TYPE,
      ad_EffectiveDt          IN task_task.effective_dt%TYPE,
      av_ReschedFromCd        IN task_task.resched_from_cd%TYPE,
      an_FromRcvdDt           IN task_task.sched_from_received_dt_bool%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      av_TaskClassCd          IN task_task.task_class_cd%TYPE,
      an_StartQt              IN OUT evt_sched_dead.start_qt%TYPE,
      av_SchedFromCd          IN OUT evt_sched_dead.sched_from_cd%TYPE,
      ad_StartDt              OUT evt_sched_dead.start_dt%TYPE,
      on_Return               OUT typn_RetCode
   ) IS


   /* local variables */
   ln_InvNoDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId                 inv_inv.inv_no_id%TYPE;
   ls_InvClassCd              inv_inv.inv_class_cd%TYPE;
   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ln_FaultDbId               sd_fault.fault_db_id%TYPE;
   ln_FaultId                 sd_fault.fault_id%TYPE;
   ld_FoundOnDate             evt_event.actual_start_dt%TYPE;
   ld_StartDt                 evt_sched_dead.start_dt%TYPE;
   ln_CustomQt                NUMBER;
   lIsInstalledOnAircraft     NUMBER;
   lAircraftDbId              NUMBER;
   lAircraftId                NUMBER;
   lSchedFromEffectiveDt      NUMBER;
   ln_EventsShareUsageInfo    NUMBER;
BEGIN

   /* set ln_InvNoPk for later use */
   EVENT_PKG.GetMainInventory(
         an_SchedDbId,
         an_SchedId,
         ln_InvNoDbId,
         ln_InvNoId,
         on_Return);
   IF on_Return < 0 THEN
         RETURN;
   END IF;

   /* if this task is a first task being create */
   IF NOT an_PrevSchedId =-1 THEN

      AreTasksOnSameUsageInformation(
        an_SchedDbId,
        an_SchedId,
        an_PrevSchedDbId,
        an_PrevSchedId,
        ln_EventsShareUsageInfo
      );
      IF ln_EventsShareUsageInfo = 0 THEN
         /* get the current inventory usage */
         GetCurrentInventoryUsage(
            an_SchedDbId,
            an_SchedId,
            an_DataTypeDbId,
            an_DataTypeId,
            an_StartQt,
            on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

         av_SchedFromCd:='CUSTOM';

      /* if the task is on the same usage information as the previous task */
      ELSE
         /* set reschedule_from */
         GetRescheduleFromQtValues(
            an_PrevSchedDbId,
            an_PrevSchedId,
            an_DataTypeDbId,
            an_DataTypeId,
            av_ReschedFromCd,
            ln_InvNoDbId,
            ln_InvNoId,
            ad_StartDt,
            an_StartQt,
            av_SchedFromCd,
            on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;

    /*if this task is a first task being created */
    ELSE
        ln_CustomQt := 0;

        /* find fault if this task is associated with one */
        GetCorrectiveFaultInfo(
                an_SchedDbId,
                an_SchedId,
                ln_FaultDbId,
                ln_FaultId,
                ld_FoundOnDate,
                ld_StartDt,
                on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        /* if fault is found */
        IF (ln_FaultId IS NOT NULL) THEN
           /* schedule from custom start date if exists a CUSTOM deadline already */
           IF ld_StartDt IS NOT NULL THEN
              ad_StartDt     := ld_StartDt;
              av_SchedFromCd := 'CUSTOM';
              /* if the start date is in the past, get the usage at that time */
              IF ld_StartDt< TRUNC(SYSDATE+1) THEN
                   GetHistoricUsageAtDt(
                         ld_StartDt,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         ln_InvNoDbId,
                         ln_InvNoId,
                         an_StartQt,
                         on_Return);
              ELSE
              /* the date is in the future, use the current usage or predict it if installed on aircraft*/
                   GetCurrentInventoryUsage(
                         an_SchedDbId,
                         an_SchedId,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         an_StartQt,
                         on_Return );
                   IF on_Return < 0 THEN
                      RETURN;
                   END IF;

                   lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft( ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                   IF lIsInstalledOnAircraft=1 THEN
                           EVENT_PKG.PredictUsageBetweenDt( lAircraftDbId,
                                                            lAircraftId,
                                                            an_DataTypeDbId,
                                                            an_DataTypeId,
                                                            SYSDATE,
                                                            ld_StartDt,
                                                            an_StartQt,
                                                            an_StartQt,
                                                            on_Return);
                   END IF;
              END IF;
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           ELSE
              ad_StartDt     := ld_FoundOnDate;
              av_SchedFromCd := 'CUSTOM';

              /* get usage snapshot of this fault */
              GetOldEvtInvUsage( ln_FaultDbId,
                                 ln_FaultId,
                                 an_DataTypeDbId,
                                 an_DataTypeId,
                                 an_StartQt,
                                 on_Return );
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           END IF;

        /* if task is adhoc or REPL or CORR */
        ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
              (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
                av_SchedFromCd := 'CUSTOM';
                an_StartQt     := ln_CustomQt;

        /* if task is based on task definition */
        ELSE
           /* get main inventory manufacturing and received date */
          GetMainInventoryBirthInfo(
            an_SchedDbId,
            an_SchedId,
            ld_ManufactDt,
            ld_ReceivedDt,
            on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;

          /* Retrieve inventory class code */
          SELECT
             inv_class_cd
          INTO
             ls_InvClassCd
          FROM
             inv_inv
          WHERE
             inv_no_db_id = ln_InvNoDbId AND
             inv_no_id    = ln_InvNoId;

         /* if the task is scheduled from install date */
          IF ( ad_PreviousCompletionDt IS NOT NULL) THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
               /* get the usage at the installation snapshot */
               GetHistoricUsageAtDt(
                      ad_PreviousCompletionDt,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      ln_InvNoDbId,
                      ln_InvNoId,
                      an_StartQt,
                      on_Return);

          /* if the task should be scheduled from birth */
          ELSIF (ab_RelativeBool = 0 ) THEN
              /* if scheduled from receive date  */
              IF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL THEN
                   av_SchedFromCd := 'BIRTH';

                   /* for TRK component the start value should be always zero */
                   IF ( ls_InvClassCd = 'TRK' ) THEN
                      an_StartQt := 0;
                   ELSE
                      ad_StartDt     := ld_ReceivedDt;
                      /* get the usage at received date */
                     IF ld_ReceivedDt<SYSDATE THEN
                        GetHistoricUsageAtDt(
                              ld_ReceivedDt,
                              an_DataTypeDbId,
                              an_DataTypeId,
                              ln_InvNoDbId,
                              ln_InvNoId,
                              an_StartQt,
                              on_Return);
    
                     ELSE
                         GetCurrentInventoryUsage(
                            an_SchedDbId,
                            an_SchedId,
                            an_DataTypeDbId,
                            an_DataTypeId,
                            an_StartQt,
                            on_Return );
                         IF on_Return < 0 THEN
                            RETURN;
                         END IF;
                         lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                         IF lIsInstalledOnAircraft=1 THEN
                            EVENT_PKG.PredictUsageBetweenDt(
                                                            lAircraftDbId,
                                                            lAircraftId,
                                                            an_DataTypeDbId,
                                                            an_DataTypeId,
                                                            SYSDATE ,
                                                            ld_ReceivedDt,
                                                            an_StartQt,
                                                            an_StartQt,
                                                            on_Return);
                           END IF;
                      END IF;
                      IF on_Return < 0 THEN
                         RETURN;
                      END IF;                         
                   END IF;

               /* if not scheduled from receive date then must be scheduled from manufactured date */
               ELSIF an_FromRcvdDt=0  THEN
                   -- we do not enforce a manufactured date in order to schedule from BIRTH for usage rules
                   av_SchedFromCd := 'BIRTH';
                   an_StartQt     := 0;
               /* scheduled from receive date but received date is null  */
               ELSE
                   av_SchedFromCd := 'BIRTH';
                   an_StartQt     := 0;
               END IF;

           /* if the task should not be scheduled from birth */
           ELSIF ad_EffectiveDt IS NOT NULL THEN
              -- if sched_from_latest_bool is false then always use effective date
              IF ( ab_SchedFromLatest = 0 ) THEN
                 lSchedFromEffectiveDt := 1;
              -- if effective date is later than manufactured date then use effective date
              ELSIF (ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt ) ) THEN
                 lSchedFromEffectiveDt := 1;
              -- otherwise use manufactured date
              ELSE
                 lschedfromEffectiveDt := 0;
              END IF;

              IF ( lschedfromEffectiveDt = 1 ) THEN
                 av_SchedFromCd := 'EFFECTIV';

                 /* for TRK component the start value should be always zero */
                 IF ( ls_InvClassCd = 'TRK' ) THEN
                    an_StartQt := 0;
                 ELSE
                     ad_StartDt     := ad_EffectiveDt;
                     /* That's where we need to define the TSN value at the specified date */
                     IF ad_EffectiveDt<SYSDATE THEN
                       GetHistoricUsageAtDt(
                          ad_EffectiveDt,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_InvNoDbId,
                          ln_InvNoId,
                          an_StartQt,
                          on_Return);
    
                     ELSE
                       GetCurrentInventoryUsage(
                          an_SchedDbId,
                          an_SchedId,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          an_StartQt,
                          on_Return );
                       IF on_Return < 0 THEN
                          RETURN;
                       END IF;
                        lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                        IF lIsInstalledOnAircraft=1 THEN
                          EVENT_PKG.PredictUsageBetweenDt(
                                                          lAircraftDbId,
                                                          lAircraftId,
                                                          an_DataTypeDbId,
                                                          an_DataTypeId,
                                                          SYSDATE ,
                                                          ad_EffectiveDt,
                                                          an_StartQt,
                                                          an_StartQt,
                                                          on_Return);
                         ELSE
                           GetCurrentInventoryUsage(
                                an_SchedDbId,
                                an_SchedId,
                                an_DataTypeDbId,
                                an_DataTypeId,
                                an_StartQt,
                                on_Return );
                         END IF;
                      END IF;
                      IF on_Return < 0 THEN
                         RETURN;
                      END IF;
                  END IF;
              /* use the manufactured date, since it is later than the effective date */
              ELSE
                 av_SchedFromCd := 'EFFECTIV';
                 an_StartQt     := 0;
              END IF;
          /* task should be scheduled from effective date but the effective date is null */
          ELSE
               av_SchedFromCd := 'EFFECTIV';
               an_StartQt     := 0;
          END IF;
        END IF;
    END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartQt@@@: '||SQLERRM);
      RETURN;
END FindDeadlineStartQt;


/********************************************************************************
*
* Procedure:    PrepareCalendarDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                           a component that fired the create_on_install
*                                           logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares calednar deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareCalendarDeadline (
      an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
      an_SchedId           IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId  IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId    IN task_task.task_id%TYPE,
      an_DataTypeDbId      IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId        IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      ad_PreviousCompletionDt  IN evt_event.sched_end_gdt%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ln_SchedFromLatest   task_task.sched_from_latest_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;

      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ld_NewDeadlineDt         evt_sched_dead.sched_dead_dt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ld_NewDeadlineDt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ln_SchedFromLatest,
            ld_EffectiveDt,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the original task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE
      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );

      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;

   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      /* find the start date of the deadline */
      FindDeadlineStartDate(
         ln_TaskDbId,
         ln_TaskId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_RelativeBool,
         ln_SchedFromLatest,
         ld_EffectiveDt,
         lv_ReschedFromCd,
         ln_SchedFromReceivedDtBool,
         ad_PreviousCompletionDt,
         ls_TaskClassCd,
         ld_StartDt,
         ls_SchedFromCd,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find the new deadline date */
   FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_NewDeadlineDt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
               an_SchedDbId,
               an_SchedId,
               an_DataTypeDbId,
               an_DataTypeId,
               NULL,
               ld_StartDt,
               ls_SchedFromCd,
               NULL,
               ld_NewDeadlineDt,
               ln_IntervalQt,
               ln_NotifyQt,
               ln_DeviationQt,
               ln_PrefixedQt,
               ln_PostfixedQt,
               on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
                 an_SchedDbId,
                 an_SchedId,
                 an_DataTypeDbId,
                 an_DataTypeId,
                 NULL,
                 ld_StartDt,
                 ls_SchedFromCd,
                 ln_IntervalQt,
                 NULL,
                 ld_NewDeadlineDt,
                 ln_NotifyQt,
                 ln_DeviationQt,
                 ln_PrefixedQt,
                 ln_PostfixedQt,
                 on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
    END IF;

   -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
   UPDATE evt_sched_dead
   SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
   WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
          evt_sched_dead.data_type_id    = an_DataTypeId   AND
          evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
          (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
          IN
          (
           SELECT evt_event_rel.rel_event_db_id,
                  evt_event_rel.rel_event_id
           FROM   evt_event,
                  evt_event_rel
           WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                  evt_event_rel.event_id    = an_SchedId   AND
                  evt_event_rel.rel_type_cd = 'DEPT'
                  AND
                  evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                  evt_event.event_id        = evt_event_rel.rel_event_id    AND
                  evt_event.event_status_cd = 'FORECAST' );

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareCalendarDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareCalendarDeadline;


/********************************************************************************
*
* Procedure:    PrepareUsageDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares usage deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2005 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareUsageDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ln_SchedFromLatest   task_task.sched_from_latest_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ln_NewDeadlineQt         evt_sched_dead.sched_dead_qt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ln_NewDeadlineQt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ln_SchedFromLatest,
            ld_EffectiveDt,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the previous task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE

      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;

   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      /* find deadline start qt */
      FindDeadlineStartQt (
         ln_TaskDbId,
         ln_TaskId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_RelativeBool,
         ln_SchedFromLatest,
         ld_EffectiveDt,
         lv_ReschedFromCd,
         ln_SchedFromReceivedDtBool,
         ad_PreviousCompletionDt,
         ls_TaskClassCd,
         ln_StartQt,
         ls_SchedFromCd,
         ld_StartDt,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find deadline qt valur */
   FindUsageDeadlineVariables(
            ln_StartQt,
            ln_IntervalQt,
            ln_NewDeadlineQt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_NewDeadlineQt,
              NULL,
              ln_IntervalQt,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_IntervalQt,
              ln_NewDeadlineQt,
              NULL,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
     END IF;

     -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
     UPDATE evt_sched_dead
     SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
     WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
            evt_sched_dead.data_type_id    = an_DataTypeId   AND
            evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
            (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
            IN
            (
             SELECT evt_event_rel.rel_event_db_id,
                    evt_event_rel.rel_event_id
             FROM   evt_event,
                    evt_event_rel
             WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                    evt_event_rel.event_id    = an_SchedId   AND
                    evt_event_rel.rel_type_cd = 'DEPT'
                    AND
                    evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                    evt_event.event_id        = evt_event_rel.rel_event_id    AND
                    evt_event.event_status_cd = 'FORECAST' );

     -- Return success
     on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareUsageDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareUsageDeadline;



/********************************************************************************
*
* Procedure:    PrepareSchedDeadlines
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares all deadlines for a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareSchedDeadlines (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      -- retrieves deadlines
      CURSOR lcur_ActualsDeadlines  IS
      (  SELECT
            evt_sched_dead.data_type_db_id as data_type_db_id,
            evt_sched_dead.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            evt_sched_dead,
            mim_data_type
         WHERE
            evt_sched_dead.event_db_id = an_SchedDbId AND
            evt_sched_dead.event_id    = an_SchedId
            AND
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
      UNION
         SELECT
            task_sched_rule.data_type_db_id as data_type_db_id,
            task_sched_rule.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            sched_stask,
            task_sched_rule,
            mim_data_type
         WHERE
            sched_stask.sched_db_id = an_SchedDbId AND
            sched_stask.sched_id    = an_SchedId
            AND
            sched_stask.rstat_cd	= 0
            AND
            task_sched_rule.task_db_id = sched_stask.task_db_id AND
            task_sched_rule.task_id    = sched_stask.task_id
            AND
            mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
            mim_data_type.data_type_id    = task_sched_rule.data_type_id
      )
      ORDER BY data_type_db_id, data_type_id;

BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the baseline, and actual deadlines */
   FOR lrec_ActualsDeadlines IN lcur_ActualsDeadlines
   LOOP

      /* Calendar Deadline */
      IF (lrec_ActualsDeadlines.domain_type_cd='CA') THEN

         -- prepare calendar deadline
         PrepareCalendarDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
            ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

      /* Usage Deadline */
      ELSE
         PrepareUsageDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
             ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;

   END LOOP;

   /* Update the Deadlines for this Task */
   EVENT_PKG.UpdateDeadline( an_SchedDbId, an_SchedId, on_Return );
   IF on_Return < 1 THEN
      RETURN;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareSchedDeadlines@@@: '||SQLERRM);
      RETURN;
END PrepareSchedDeadlines;

/********************************************************************************
*
* Procedure:    UpdateDependentDeadlines
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the tasks that have been forecasted to follow it.
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlines(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   ln_TaskTaskDbId  task_task.task_db_id%TYPE;
   ln_TaskTaskId    task_task.task_id%TYPE;

   /* SQL to retrieve all of the forecasted tasks starting with this one */
   CURSOR lcur_DepTasks (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         LEVEL,
         rel_event_db_id sched_db_id,
         rel_event_id    sched_id
      FROM
         evt_event_rel
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId AND
         rel_type_cd = 'DEPT'
      CONNECT BY
         event_db_id = PRIOR rel_event_db_id AND
         event_id    = PRIOR rel_event_id AND
         rel_type_cd = 'DEPT'
      ORDER BY 1;

   /* EXCEPTIONS */
   xc_UnkCUSTOMnSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    SELECT
        sched_stask.task_db_id,
        sched_stask.task_id
    INTO
        ln_TaskTaskDbId,
        ln_TaskTaskId
    FROM
        sched_stask
    WHERE
         sched_stask.sched_db_id = an_StartSchedDbId AND
         sched_stask.sched_id    = an_StartSchedId
         AND
         sched_stask.rstat_cd	= 0;

   /* loop dependant tasks, and prepare their deadlines*/
   FOR lrec_DepTasks IN lcur_DepTasks( an_StartSchedDbId, an_StartSchedId )
   LOOP

      PrepareSchedDeadlines( lrec_DepTasks.sched_db_id,
                             lrec_DepTasks.sched_id,
                             ln_TaskTaskDbId,
                             ln_TaskTaskId,
                             false,
                             NULL,
                             on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlines@@@: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlines;



/********************************************************************************
*
* Procedure:      PrepareDeadlineForInv
* Arguments:      al_InvNoDbId (IN NUMBER): The inventory to update
*                 al_InvNoId (IN NUMBER): ""
* Description:    Finds all the tasks with dedlines and runs
*                 prepare deadlines on them.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareDeadlineForInv(
      an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
      as_SchedFrom    IN evt_sched_dead.sched_from_cd%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      on_Return        OUT NUMBER
   ) IS

  /* cursor declarations */
   /* return all non-historic events in the inventory tree that have
      scheduled deadlines */
   CURSOR lcur_DeadlineTask (cn_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
                    cn_InvNoId      IN  inv_inv.inv_no_id%TYPE)
   IS
      SELECT
              evt_event.event_db_id,
             evt_event.event_id
        FROM
             (
             SELECT     inv_inv.inv_no_db_id, inv_inv.inv_no_id
             FROM       inv_inv
             START WITH inv_inv.inv_no_db_id 	= cn_InvNoDbId AND
                        inv_inv.inv_no_id    	= cn_InvNoId
             CONNECT BY	inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
                        inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
             )inv_tree,
             evt_inv,
             evt_event
       WHERE
             evt_inv.inv_no_db_id = inv_tree.inv_no_db_id AND
             evt_inv.inv_no_id    = inv_tree.inv_no_id    AND
             evt_inv.main_inv_bool=1
             AND
             evt_event.event_db_id     = evt_inv.event_db_id AND
             evt_event.event_id        = evt_inv.event_id    AND
             evt_event.event_type_cd   = 'TS'                AND
             evt_event.hist_bool       = 0                   AND
             evt_event.event_status_cd = 'ACTV'              AND
             evt_event.rstat_cd   = 0
             AND EXISTS
             (  SELECT 1
                FROM   evt_sched_dead
                WHERE  evt_sched_dead.event_db_id   = evt_event.event_db_id AND
                       evt_sched_dead.event_id      = evt_event.event_id    AND
                       evt_sched_dead.sched_from_cd = as_SchedFrom);
   lrec_DeadlineTask lcur_DeadlineTask%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all of the tasks which need updating */
   FOR lrec_DeadlineTask IN lcur_DeadlineTask(an_InvNoDbId, an_InvNoId) LOOP

      PrepareSchedDeadlines( lrec_DeadlineTask.event_db_id,
                             lrec_DeadlineTask.event_id,
                             NULL,
                             NULL,
                             abSyncWithBaseline,
                             NULL,
                             on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      /* prepare deadlines forecasted tasks*/
      UpdateDependentDeadlines( lrec_DeadlineTask.event_db_id,
                                lrec_DeadlineTask.event_id,
                                on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'prep_deadline_pkg@@@PrepareDeadlineForInv@@@' || SQLERRM);
      RETURN;
END PrepareDeadlineForInv;


FUNCTION GetScheduleDetails(
            an_TaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_TaskTaskId     IN task_task.task_id%TYPE,
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE
         ) RETURN typrec_ScheduleDetails
IS
   -- Local stask and task definition variables
   ln_PrevSchedDbId       sched_stask.sched_db_id%TYPE;
   ln_PrevSchedId         sched_stask.sched_id%TYPE;
   ln_ActiveTaskTaskDbId  task_interval.task_db_id%TYPE;
   ln_ActiveTaskTaskId    task_interval.task_id%TYPE;
   ln_HInvNoDbId          task_ac_rule.inv_no_db_id%TYPE;
   ln_HInvNoId            task_ac_rule.inv_no_id%TYPE;
   ln_PartNoDbId          task_interval.part_no_db_id%TYPE;
   ln_PartNoId            task_interval.part_no_id%TYPE;
   ln_RelativeBool        task_task.relative_bool%TYPE;
   ln_SchedFromLatest     task_task.sched_from_latest_bool%TYPE;
   ld_EffectiveDt         task_task.effective_dt%TYPE;
   lv_ReschedFromCd       task_task.resched_from_cd%TYPE;
   ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
   ln_RecurringBool       task_task.recurring_task_bool%TYPE;
   ls_TaskClassCd         task_task.task_class_cd%TYPE;
   ln_Return              typn_RetCode;

   orec_ScheduleDetails  typrec_ScheduleDetails;

BEGIN

   GetTaskDetails(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_ActiveTaskTaskDbId,
         ln_ActiveTaskTaskId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RelativeBool,
         ln_SchedFromLatest,
         ld_EffectiveDt,
         lv_ReschedFromCd,
         ln_RecurringBool,
         ln_SchedFromReceivedDtBool,
         ls_TaskClassCd,
         ln_Return );

   IF ln_Return < 0 THEN
      RETURN NULL;
   END IF;

   -- Populate task details
   orec_ScheduleDetails.STaskDbId         := an_SchedDbId;
   orec_ScheduleDetails.STaskId           := an_SchedId;
   orec_ScheduleDetails.FirstInstanceBool := ( ln_PrevSchedDbId = -1 );
   orec_ScheduleDetails.PreviousSTaskDbId := ln_PrevSchedDbId;
   orec_ScheduleDetails.PreviousSTaskID   := ln_PrevSchedId;
   orec_ScheduleDetails.HInvNoDbId        := ln_HInvNoDbId;
   orec_ScheduleDetails.HInvNoId          := ln_HInvNoId;
   orec_ScheduleDetails.PartNoDbId        := ln_PartNoDbId;
   orec_ScheduleDetails.PartNoId          := ln_PartNoId;

   -- Populate task definition details
   -- Because this function is related to forecasting deadlines, the revised task definition
   -- details are used instead of the stask's actual task definition details.
   orec_ScheduleDetails.RevisionTaskTaskDbId  := an_TaskTaskDbId;
   orec_ScheduleDetails.RevisionTaskTaskID    := an_TaskTaskId;
   orec_ScheduleDetails.ActiveTaskTaskDbId    := ln_ActiveTaskTaskDbId;
   orec_ScheduleDetails.ActiveTaskTaskId      := ln_ActiveTaskTaskId;

   SELECT
      task_task.relative_bool,
      task_task.sched_from_latest_bool,
      task_task.effective_dt,
      task_task.resched_from_cd,
      istaskdefnrecurring(an_TaskTaskDbId, an_TaskTaskID ),
      task_task.sched_from_received_dt_bool,
      task_task.last_sched_dead_bool,
      task_task.task_class_cd
   INTO
      orec_ScheduleDetails.RelativeBool,
      orec_ScheduleDetails.SchedFromLatestBool,
      orec_ScheduleDetails.EffectiveDt,
      orec_ScheduleDetails.ReschedFromCd,
      orec_ScheduleDetails.RecurringBool,
      orec_ScheduleDetails.SchedFromReceivedDtBool,
      orec_ScheduleDetails.ScheduleToLast,
      orec_ScheduleDetails.TaskClassCd
   FROM
      task_task
   WHERE
      task_task.task_db_id = an_TaskTaskDbId AND
      task_task.task_id    = an_TaskTaskId;


   RETURN orec_ScheduleDetails;

END GetScheduleDetails;



/********************************************************************************
*
* Function:     GetTaskRuleStartDate
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder: Yungjae Cho
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartDate(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

         ) RETURN evt_sched_dead.start_dt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   ls_SchedFromCd          evt_sched_dead.sched_from_cd%TYPE;
   ln_Return               typn_RetCode;
   -- Return value
   ld_StartDt              DATE;

BEGIN

   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF (
         lcur_CustomDeadline%FOUND AND
         lrec_CustomDeadline.sched_from_cd = 'CUSTOM' AND
         lrec_CustomDeadline.start_dt IS NOT NULL )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_dt;
      END IF;

      CLOSE lcur_CustomDeadline;
      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartDate (
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.SchedFromLatestBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ReschedFromCd,
         arec_Schedule.SchedFromReceivedDtBool,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ld_StartDt,                                 -- This sets the startDt
         ls_SchedFromCd,
         ln_Return
      );

   RETURN ld_StartDt;

END GetTaskRuleStartDate;



/********************************************************************************
*
* Function:     GetTaskRuleStartUsage
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartUsage(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

         ) RETURN evt_sched_dead.start_qt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   ls_SchedFromCd          ref_sched_from.sched_from_cd%TYPE;
   ld_StartDt              evt_sched_dead.start_dt%TYPE;
   ln_StartQt              evt_sched_dead.start_qt%TYPE;
   ln_Return               typn_RetCode;


BEGIN
   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF ( lcur_CustomDeadline%FOUND AND
           lrec_CustomDeadline.start_qt IS NOT NULL AND
           lrec_CustomDeadline.start_qt != 0 )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_qt;

      END IF;

      CLOSE lcur_CustomDeadline;

      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartQt(
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.SchedFromLatestBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ReschedFromCd,
         arec_Schedule.SchedFromReceivedDtBool,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ln_StartQt,
         ls_SchedFromCd,
         ld_StartDt,
         ln_Return
      );

   RETURN ln_StartQt;

END GetTaskRuleStartUsage;



/********************************************************************************
*
* Function:      getCustomDeviation
* Arguments:
*                ar_ScheduleDetails   task definition and stask information.
*
*
* Description:   Obtains custom deadline information for a task definition revision, or scheduled task.
*                If a task_deadline_ext has been defined, its deviation is returned.
*                If the scheduled task has an extension, its deviation is returned.
*                Otherwise NULL is returned.
*
* Orig.Coder:    Alexander Nazarian
* Recent Coder:  Alexander Nazarian
* Recent Date:   July 2009
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getCustomDeviation(
      arec_ScheduleDetails    IN   typrec_ScheduleDetails,
      an_DataTypeDbId         IN   mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId           IN   mim_data_type.data_type_id   %TYPE
   )  RETURN task_deadline_ext.deviation_qt%TYPE

IS
   ln_Deviation                NUMBER;
   lb_RecFound                 BOOLEAN;

   --First check if the user has setup an extension using the Extend Deadline page from the View Impact Report page.
   --If there is an extension, use it.
   CURSOR lCur_TaskDeadlineExt
   (
      cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
      cn_SchedId      IN sched_stask.sched_id%TYPE,
      cn_DataTypeDbId IN mim_data_type.data_type_db_id%TYPE,
      cn_DataTypeId   IN mim_data_type.data_type_id%TYPE
   )
   IS
      SELECT
         task_deadline_ext.deviation_qt
      FROM
         task_deadline_ext
      WHERE
         task_deadline_ext.sched_db_id        = cn_SchedDbId AND
         task_deadline_ext.sched_id           = cn_SchedId   AND
         task_deadline_ext.data_type_db_id    = cn_DataTypeDbId AND
         task_deadline_ext.data_type_id       = cn_DataTypeId;

   --Cursor variable for the task deadline extension
   lRec_TaskDeadlineExt lCur_TaskDeadlineExt%ROWTYPE;

   -- TODO: Not sure where to get this from
   ln_EventsOnSameInv         NUMBER;

   ln_DeleteActualDeadline   NUMBER;
   ln_UpdateActualDeadline   NUMBER;
   ln_InsertActualDeadline   NUMBER;

   ls_sched_from_cd          evt_sched_dead.sched_from_cd%TYPE;
   ld_start_dt               evt_sched_dead.start_dt%TYPE;
   ln_start_qt               evt_sched_dead.start_qt%TYPE;

   -- OUT baseline values
   ln_ActiveIntervalQt       task_sched_rule.def_interval_qt%TYPE;
   ln_ActiveNotifyQt         task_sched_rule.def_notify_qt%TYPE;
   ln_ActiveInitialQt        task_sched_rule.def_deviation_qt%TYPE;
   ln_ActiveDeviationQt      task_sched_rule.def_deviation_qt%TYPE;
   ln_ActivePrefixedQt       task_sched_rule.def_prefixed_qt%TYPE;
   ln_ActivePostfixedQt      task_sched_rule.def_postfixed_qt%TYPE;
   ln_ActiveDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

   ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
   ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
   ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
   ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
   ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
   ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
   ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
   ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
   ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

   ln_BaselineIntervalQt     task_sched_rule.def_interval_qt%TYPE;
   ln_BaselineNotifyQt       task_sched_rule.def_notify_qt%TYPE;
   ln_BaselineDeviationQt    task_sched_rule.def_deviation_qt%TYPE;
   ln_BaselinePrefixedQt     task_sched_rule.def_prefixed_qt%TYPE;
   ln_BaselinePostfixedQt    task_sched_rule.def_postfixed_qt%TYPE;

   ln_Return                 typn_RetCode;

BEGIN
   -- First, check for an extension set up by the user using the extend deadline page.
   OPEN lCur_TaskDeadlineExt
   (
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId
   ) ;

   FETCH lCur_TaskDeadlineExt INTO lRec_TaskDeadlineExt;

   lb_RecFound := lCur_TaskDeadlineExt%FOUND;
   CLOSE lCur_TaskDeadlineExt;

   IF ( lb_RecFound AND
        lrec_TaskDeadlineExt.deviation_qt IS NOT NULL AND
        lrec_taskDeadlineExt.deviation_qt > 0 )
   THEN
      ln_Deviation := lRec_TaskDeadlineExt.deviation_qt;
      RETURN ln_Deviation;
   END IF;

   /*
    * No impact report extension has been defined, so check for an extension on the actual stask.
    *
    * Note that this code repeats queries that will be performed by GetValuesAndActionForSynch in the following block of code.
    * This is because GetValuesAndActionForSynch does not return the stask's custom deviation if scheduling rules have not changed.
    * Because of this, we cannot rely on the deviation returned by it, and have to explicitly check the actual deadline first.
    * Ideally, GetValuesAndActionForSynch's behaviour will be updated when we can be sure it will not break baseline synch.
    */

   -- Get the deviation for the active task def.
   GetBaselineDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            arec_ScheduleDetails.PartNoDbId,
            arec_ScheduleDetails.PartNoId,
            arec_ScheduleDetails.HInvNoDbId,
            arec_ScheduleDetails.HInvNoId,
            ln_ActiveIntervalQt,
            ln_ActiveInitialQt,
            ln_ActiveNotifyQt,
            ln_ActiveDeviationQt,
            ln_ActivePrefixedQt,
            ln_ActivePostfixedQt,
            ln_ActiveDeadlineExists,
            ln_Return );

   -- If available, get the actual stask deviation for this data type.
   GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            TRUE,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            ln_Return );

   -- If both exist, and the active baseline deviation != stask deviation then a custom deviation has been defined.
   IF ln_ActualDeadlineExists = 1 AND ln_ActiveDeadlineExists = 1 THEN
      IF ln_ActualDeviationQt <> ln_ActiveDeviationQt THEN
         -- Return the stask's custom deviation.
         RETURN ln_ActualDeviationQt;
      END IF;
   END IF;

   /*
    * No custom deviations exist from the impact report, or the actual stask.  Figure out the new baseline's deviation for this data type.
    */
   GetValuesAndActionForSynch(
      arec_ScheduleDetails.ActiveTaskTaskDbId,
      arec_ScheduleDetails.ActiveTaskTaskId,
      arec_ScheduleDetails.RevisionTaskTaskDbId,
      arec_ScheduleDetails.RevisionTaskTaskId,
      arec_ScheduleDetails.PreviousSTaskDbId,
      arec_ScheduleDetails.PreviousSTaskId,
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId,
      arec_ScheduleDetails.HInvNoDbId,
      arec_ScheduleDetails.HInvNoId,
      arec_ScheduleDetails.PartNoDbId,
      arec_ScheduleDetails.PartNoId,
      arec_ScheduleDetails.RecurringBool,
      ln_EventsOnSameInv,
      ln_DeleteActualDeadline,
      ln_UpdateActualDeadline,
      ln_InsertActualDeadline,
      ls_sched_from_cd,
      ld_start_dt,
      ln_start_qt,
      ln_BaselineIntervalQt,
      ln_BaselineNotifyQt,
      ln_BaselineDeviationQt,
      ln_BaselinePrefixedQt,
      ln_BaselinePostfixedQt,

      ln_Return
   );

   IF ( ln_InsertActualDeadline = 1 OR ln_UpdateActualDeadline = 1 ) THEN
      RETURN ln_BaselineDeviationQt;
   ELSE

      -- The revision has not updated scheduling rules, or the rule has been deleted.  Either way, no custom deviation exists.
      RETURN NULL;
   END IF;

END getCustomDeviation;



/********************************************************************************
*
* Function:     getTaskRuleDeadline
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               as_DomainTypeCd          Code of the data type's domain.
*               as_EngUnitCd             Code of the data type's engineering unit.
*               an_MultiplierQt          Data type's multiplier.
*
* Return:       Deadline information for the task definition's scheduling rule.
*
* Description:  Calculates deadline information for calendar and usage data types.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getTaskRuleDeadline(
      arec_Schedule    typrec_ScheduleDetails,
      an_DataTypeDbId  mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId    mim_data_type.data_type_id   %TYPE,
      as_DomainTypeCd  mim_data_type.domain_type_cd %TYPE,
      as_DataTypeCd    mim_data_type.data_type_cd   %TYPE,
      as_EngUnitCd     mim_data_type.eng_unit_cd    %TYPE,
      an_MultiplierQt  ref_eng_unit.ref_mult_qt     %TYPE
   ) RETURN DeadlineRecord
IS

   ln_IntervalQt        task_sched_rule.def_interval_qt%TYPE;
   ln_InitialQt         task_sched_rule.def_initial_qt%TYPE;
   ln_NotifyQt          task_sched_rule.def_notify_qt%TYPE;
   ln_DeviationQt       task_sched_rule.def_deviation_qt%TYPE;
   ln_PrefixedQt        task_sched_rule.def_prefixed_qt%TYPE;
   ln_PostfixedQt       task_sched_rule.def_postfixed_qt%TYPE;
   ln_DeadlineExists    task_sched_rule.def_postfixed_qt%TYPE;
   ln_Return            typn_RetCode;

   ln_CustomDeviationQt task_sched_rule.def_deviation_qt%TYPE;

   ln_CurrentUsage      inv_curr_usage.tsn_qt%TYPE;
   ln_UsageRemaining    inv_curr_usage.tsn_qt%TYPE;
   ld_StartDt           evt_sched_dead.start_dt%TYPE;
   ln_StartQt           evt_sched_dead.start_qt%TYPE;
   ld_DeadlineDt        evt_sched_dead.start_dt%TYPE;

   lrec_Deadline        DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);

BEGIN
   lrec_Deadline.DeadlineDt := SYSDATE;

   -- Get the most relevant scheduling rule and interval information for the task definition revision + data type.

   GetBaselineDeadline(
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.RevisionTaskTaskDbId,
         arec_Schedule.RevisionTaskTaskId,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         arec_Schedule.PartNoDbId,
         arec_Schedule.PartNoId,
         arec_Schedule.HInvNoDbId,
         arec_Schedule.HInvNoId,
         ln_IntervalQt,
         ln_InitialQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         ln_DeadlineExists,
         ln_Return
      );

   IF ( ln_Return < 0 ) THEN
      -- No deadline information found.  This should never happen.
      RETURN lrec_Deadline;
   END IF;

   ln_CustomDeviationQt := getCustomDeviation( arec_Schedule, an_DataTypeDbId, an_DataTypeId );

   IF ( ln_CustomDeviationQt IS NOT NULL AND ln_customDeviationQt > 0 ) THEN
      -- If a custom extension has been defined, override the scheduling rule deviation.
      ln_DeviationQt := ln_CustomDeviationQt;
   END IF;


   IF ( arec_Schedule.FirstInstanceBool AND ln_InitialQt IS NOT NULL ) THEN
      -- Use the rule's initial interval for its first stask instance.
      ln_IntervalQt := ln_InitialQt;
   END IF;

   -- Get the deadline start date or usage
   IF ( as_DomainTypeCd = 'CA' ) THEN -- Calendar

      ld_StartDt := GetTaskRuleStartDate(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId
         );

      -- Calculate the new deadline date.
      FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_DeadlineDt,
            ln_Return
         );

      -- Assign default value for usage remaining - clients should ignore this for calendar deadlines.
      lrec_Deadline.UsageRemainingQt := 0;

   ELSE -- Usage

      -- Get the start usage for this scheduling rule.
      -- This obtains custom start usage defined for the task def revision, or an actual extension assigned to the stask.
      -- If no extensions are found, the usage is calculated using FindDeadlineStartQt.
      ln_StartQt := GetTaskRuleStartUsage(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId
         );

      ld_StartDt := SYSDATE;

      -- Get the main inventory's current usage.
      GetCurrentInventoryUsage(
            arec_Schedule.STaskDbId,
            arec_Schedule.STaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_CurrentUsage,
            ln_Return
         );

      ln_UsageRemaining := (ln_StartQt + ln_IntervalQt ) - ln_CurrentUsage;

      -- Calculate the usage remaining.
      EVENT_PKG.findForecastedDeadDt(
            arec_Schedule.HInvNoDbId,
            arec_Schedule.HInvNoId,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_UsageRemaining + ln_DeviationQt,
            ld_StartDt,
            ld_DeadlineDt, -- assigned a value by this procedure call.
            ln_Return
         );
      IF ( ln_UsageRemaining IS NULL ) THEN
         ln_UsageRemaining := 0;
      END IF;


      lrec_Deadline.StartUsageQt     := ln_StartQt;
      lrec_Deadline.CurrentUsageQt   := ln_CurrentUsage;
      lrec_Deadline.UsageRemainingQt := ln_UsageRemaining;
   END IF;

   lrec_Deadline.DataTypeDbId := an_DataTypeDbId;
   lrec_Deadline.DataTypeId   := an_DataTypeId;
   lrec_Deadline.DomainTypeCd := as_DomainTypeCd;
   lrec_Deadline.DataTypeCd   := as_DataTypeCd;
   lrec_Deadline.EngUnitCd    := as_EngUnitCd;
   lrec_Deadline.DeviationQt  := ln_DeviationQt;
   lrec_Deadline.DeadlineDt   := ld_DeadlineDt;
   lrec_Deadline.MultiplierQt := an_MultiplierQt;
   lrec_Deadline.IntervalQt   := ln_IntervalQt;

   -- determine the extended deadline date
   lrec_Deadline.ExtendedDeadlineDt :=
      getExtendedDeadlineDt(
         lrec_Deadline.DeviationQt,
         lrec_Deadline.DeadlineDt,
         lrec_Deadline.DomainTypeCd,
         lrec_Deadline.DataTypeCd,
         lrec_Deadline.MultiplierQt
      );

   RETURN lrec_Deadline;

END getTaskRuleDeadline;



/********************************************************************************
*
* Function:     getForecastedDrivingDueDate
* Arguments:
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // --
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // --
*               an_AircraftInvNoDbId      IN inv_inv.inv_no_db_id%TYPE,
*               an_AircraftInvNoId        IN inv_inv.inv_no_id%TYPE
*
* Return:       A string with * delimited deadline information.
*
* Description:  Obtains the driving deadline due date for a revised task definition.
*               All scheduling rules for the new revision have their deadline/extended deadline calculated.
*               The deadline returned will be the earliest, or latest depending on the
*               revised requirement's last_sched_dead_bool value.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetForecastedDrivingDueDate(
            an_STaskDbId              IN sched_stask.sched_db_id%TYPE,
            an_STaskId                IN sched_stask.sched_id%TYPE,
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE

   ) RETURN VARCHAR2
IS

   lrec_Schedule    typrec_ScheduleDetails;
   lrec_CurrentDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);
   lrec_DrivingDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);


   -- Get the new baseline interval and deviation for the REV requirement
   CURSOR lcur_NewBaselines (
      crec_Schedule typrec_ScheduleDetails
   ) IS
      SELECT
         task_sched_rule.data_type_db_id,
         task_sched_rule.data_type_id
      FROM
         task_sched_rule
      WHERE
         task_sched_rule.task_db_id    = an_RevisionTaskTaskDbId AND
         task_sched_rule.task_id       = an_RevisionTaskTaskId;

   lrec_NewBaseline           lcur_NewBaselines%ROWTYPE;
   ln_Return                  typn_RetCode;

   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;

BEGIN

   lrec_Schedule := GetScheduleDetails( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId, an_STaskDbId, an_STaskId );
   lrec_DrivingDeadline.DomainTypeCd := NULL;

   FOR lrec_NewBaseline IN lcur_NewBaselines( lrec_Schedule ) LOOP

      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return   );
      IF ln_Return < 0 THEN
        RETURN NULL;
      END IF;

      lrec_CurrentDeadline := getTaskRuleDeadline(
            lrec_Schedule,
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ls_DataTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      IF ( lrec_DrivingDeadline.DomainTypeCd IS NULL OR
         ( lrec_Schedule.ScheduleToLast = 1 AND lrec_CurrentDeadline.ExtendedDeadlineDt > lrec_DrivingDeadline.ExtendedDeadlineDt ) OR
         ( lrec_Schedule.ScheduleToLast = 0 AND lrec_CurrentDeadline.ExtendedDeadlineDt < lrec_DrivingDeadline.ExtendedDeadlineDt )
         )
      THEN
         lrec_DrivingDeadline := lrec_CurrentDeadline;
      END IF;
   END LOOP;

   IF ( lrec_DrivingDeadline.DataTypeDbId = -1 ) THEN
      -- No deadlines found.  Return empty data.
      RETURN '******';
   END IF;

   RETURN lrec_DrivingDeadline.DomainTypeCd || '*' || lrec_DrivingDeadline.UsageRemainingQt || '*' || lrec_DrivingDeadline.EngUnitCd || '*' ||
          lrec_DrivingDeadline.MultiplierQt || '*' || TO_CHAR( lrec_DrivingDeadline.DeadlineDt, 'DD-MON-YYYY' ) || ' 23:59 *' || lrec_DrivingDeadline.DeviationQt || '*' ||
          TO_CHAR( lrec_DrivingDeadline.ExtendedDeadlineDt, 'DD-MON-YYYY' ) || ' 23:59';

END GetForecastedDrivingDueDate;



/********************************************************************************
*
* Function:     GetTaskDefnRules
* Arguments:
*               an_TaskTaskDbId    Task definition primary key
*               an_TaskTaskId      -- // --
*               an_STaskDbId       Active stask primary key
*               an_STaskId         -- // --
*
* Return:       A table of scheduling rule information for the task definition.
*               Note that *only* "basic" rule information is returned - i.e.
*               tail, part, and measurement specific information is not included.
*
* Description:  Obtains all basic scheduling rule information for a task definition.
*               Each data type has exactly one row of rule information returned.
*               Because this is a helper function, part, tail, and measurement
*               information is not returned.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDefnRules(
            an_TaskTaskDbId     IN task_task.task_db_id   %TYPE,
            an_TaskTaskId       IN task_task.task_id      %TYPE
         )
         RETURN typtabrec_SchedulingRuleTable
IS

   -- Cursor that gets basic scheduling rules for the task.
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id   %TYPE,
            cn_TaskId      task_interval.task_id      %TYPE
         )
      IS
         SELECT
            task_sched_rule.data_type_db_id    data_type_db_id,
            task_sched_rule.data_type_id       data_type_id,
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId
      ;

   lrec_BaselineDeadline   lcur_BaselineDeadlines%ROWTYPE;

   ltab_Rules              typtabrec_SchedulingRuleTable;

   ln_Index                NUMBER := 0;

BEGIN
   FOR lrec_BaselineDeadline IN lcur_BaselineDeadlines(
         an_TaskTaskDbId,
         an_TaskTaskId
      )
   LOOP
      ltab_Rules( ln_Index ).DataTypeDbId := lrec_BaselineDeadline.data_type_db_id;
      ltab_Rules( ln_Index ).DataTypeId   := lrec_BaselineDeadline.data_type_id;
      ltab_Rules( ln_index ).IntervalQt  := lrec_BaselineDeadline.interval_qt;
      ltab_Rules( ln_index ).InitialQt   := lrec_BaselineDeadline.initial_qt;
      ltab_Rules( ln_index ).DeviationQt := lrec_BaselineDeadline.deviation_qt;
      ltab_Rules( ln_index ).PrefixedQt  := lrec_BaselineDeadline.prefixed_qt;
      ltab_Rules( ln_index ).PostfixedQt := lrec_BaselineDeadline.postfixed_qt;

      ln_Index := ln_Index + 1;
   END LOOP;

   RETURN ltab_Rules;

END GetTaskDefnRules;


/********************************************************************************
*
* Function:     isTaskDefnSchedulingChanged
* Arguments:
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // --
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // --
*
* Return:       0 if task definition information and scheduling rules are identical.
*               1 if the task definition has changed (recurring, schedule from, schedule to, etc.)
                1 if any scheduling rules have been added/removed/updated.
*
* Description:  Checks for differences between two revisions of a task definition.
*               Examines the basic task def information (recurring, schedule from, etc)
*               and checks all scheduling rules for differences.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isTaskDefnSchedulingChanged(
            an_ActiveTaskTaskDbId    task_task.task_db_id   %TYPE,
            an_ActiveTaskTaskId      task_task.task_id      %TYPE,
            an_RevisionTaskTaskDbId  task_task.task_db_id   %TYPE,
            an_RevisionTaskTaskId    task_task.task_id      %TYPE,
            an_STaskDbId             sched_stask.sched_db_id%TYPE,
            an_STaskId               sched_stask.sched_id   %TYPE
   ) RETURN NUMBER
IS

   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task defs.
   ltabrec_Rules         typtabrec_SchedulingRuleTable;
   ltabrec_RevisedRules  typtabrec_SchedulingRuleTable;

   -- Loop variable.
   lrec_Rule          typrec_SchedulingRule;

   ln_BaselineDeadlineChanged  INT;
   ln_Return                   INT;

BEGIN

   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_ActiveTaskTaskDbId, an_ActiveTaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules        := GetTaskDefnRules( an_ActiveTaskTaskDbId,   an_ActiveTaskTaskId );
   ltabrec_RevisedRules := GetTaskDefnRules( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId );

   IF ( ltabrec_Rules.COUNT != ltabrec_RevisedRules.COUNT ) THEN
      -- There's a mismatch in rule counts.  Something must have changed.
      RETURN 1;
   END IF;

   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );

      BaselineDeadlinesChanged(
            an_RevisionTaskTaskDbId,
            an_RevisionTaskTaskId,
            an_ActiveTaskTaskDbId,
            an_ActiveTaskTaskId,
            lrec_Rule.DataTypeDbId,
            lrec_Rule.DataTypeId,
            lrec_Schedule.HInvNoDbId,
            lrec_Schedule.HInvNoId,
            lrec_Schedule.PartNoDbId,
            lrec_Schedule.PartNoId,
            ln_BaselineDeadlineChanged,
            ln_Return
         );

      IF ( ln_BaselineDeadlineChanged > 0 ) THEN
         -- A rule has changed.  We can stop checking.
         RETURN 1;
      END IF;
   END LOOP;

   -- No differences found.
   RETURN 0;

END isTaskDefnSchedulingChanged;


/********************************************************************************
*
* Function:     GetTaskDeadlines
* Arguments:
*               an_TaskTaskDbId   Task definition primary key
*               an_TaskTaskId     -- // --
*               an_STaskDbId      Stask primary key
*               an_STaskId        -- // --
*
* Return:       Deadline information for every scheduling rule associated with the task definition.
*
* Description:  Returns one row for each scheduling rule in a task.
*               Part, tail, and measurement information will overwrite basic rule values.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDeadlines(
            an_TaskTaskDbId   IN task_task.task_db_id   %TYPE,
            an_TaskTaskId     IN task_task.task_id      %TYPE,
            an_STaskDbId      IN sched_stask.sched_db_id%TYPE,
            an_STaskId        IN sched_stask.sched_id   %TYPE

   ) RETURN DeadlineTable PIPELINED

IS
   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task def.
   ltabrec_Rules      typtabrec_SchedulingRuleTable;

   -- Used in the loop to store a scheduling rule's data type information.
   ls_DomainTypeCd    mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt       ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd       mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd      mim_data_type.data_type_cd%TYPE;

   -- Used in the loop to temporarily store scheduling rule and deadline data.
   lrec_Rule          typrec_SchedulingRule;
   lrec_DeadlineInfo  DeadlineRecord := DeadlineRecord(-1, -1, '', '', '', -1, -1, -1, null, null, -1, -1, -1);

   ln_Return          typn_RetCode;

BEGIN
   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_TaskTaskDbId, an_TaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules := GetTaskDefnRules( an_TaskTaskDbId, an_TaskTaskId );

   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );

      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return
         );

      -- Get the deadline information for the current data type.
      lrec_DeadlineInfo := getTaskRuleDeadline(
            lrec_Schedule,
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId,
            ls_DomainTypeCd,
            ls_DataTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      -- Output the deadline information.
      PIPE ROW( lrec_DeadlineInfo );
   END LOOP;

END GetTaskDeadlines;


END PREP_DEADLINE_PKG;
/

--changeSet DEV-1445_3:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE SCHED_STASK_PKG

/********************************************************************************
*
* Package:     SCHED_STASK_PKG
* Description: This package is used to perform various actions on task events
*              1) Generate scheduled tasks based on task definitions,
*              2) Cancels a scheduled task
*              3) use task applicability to determine if a task applies
*
* Orig.Coder:   Siku Adam
* Recent Coder: Lindsay Linkletter
* Recent Date:  March 1, 2000
*
*********************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
IS

/*------------------------------------ SUBTYPES ----------------------------*/
-- Define a subtype for return codes
SUBTYPE typn_RetCode IS NUMBER;

/*---------------------------------- Constants -----------------------------*/

-- Basic error handling codes
icn_Success CONSTANT typn_RetCode := 1;       -- Success
icn_NoProc  CONSTANT typn_RetCode := 0;       -- No processing done
icn_Error   CONSTANT typn_RetCode := -1;      -- Error

-- return codes for the GenSchedTask procedure
icn_ApplicabilityInvalid   CONSTANT typn_RetCode := -11;
icn_InvHasIncorrectPartNo  CONSTANT typn_RetCode := -12;
icn_TaskDefnNotActive      CONSTANT typn_RetCode := -13;
icn_InvIsLocked            CONSTANT typn_RetCode := -15;

-- Sub procedure validation TRUE, FALSE
icn_True    CONSTANT typn_RetCode := 1;  -- True
icn_False   CONSTANT typn_RetCode := 0;  -- False

/*---------------------------------- Procedures -----------------------------*/

/* procedure used to generate a schedulated task */
PROCEDURE GenSchedTask (
       an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode);

/* procedure used to generate a schedulated task */
PROCEDURE GenOneSchedTask (
       an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,        
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode);

PROCEDURE GenServiceTask(
       an_EvtEventDbId         IN evt_event.event_db_id%TYPE,
        an_EvtEventId           IN evt_event.event_id%TYPE,
        an_InvNoDbId            IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId              IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId             IN sched_stask.task_db_id%TYPE ,
        an_TaskId               IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId     IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId       IN evt_event.event_id%TYPE,
        an_NextTaskDbId         IN evt_event.event_db_id%TYPE,
        an_NextTaskId           IN evt_event.event_id%TYPE,
        ad_CompletionDate       IN evt_event.event_dt%TYPE,
        an_ReasonDbId           IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd             IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote             IN evt_stage.user_stage_note%TYPE,
        an_HrDbId               IN org_hr.hr_db_id%TYPE,
        an_HrId                 IN org_hr.hr_id%TYPE,
        ab_CalledExternally     IN BOOLEAN,
        ab_Historic             IN BOOLEAN,
        ab_CreateNATask         IN BOOLEAN,
        ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId            OUT evt_event.event_db_id%TYPE,
        on_SchedId              OUT evt_event.event_id%TYPE,
        on_Return               OUT typn_RetCode
);

PROCEDURE InsertTask(
        an_TaskDbId           IN evt_event.event_db_id%TYPE,
        an_TaskId             IN evt_event.event_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        an_NextTaskDbId       IN evt_event.event_db_id%TYPE,
        an_NextTaskId         IN evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
        );

/* This function will return number of actv task instances */
FUNCTION CountTaskInstances (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE

   ) RETURN NUMBER;


/* This function will return 1 if task is applicable for the inventory */
FUNCTION IsTaskApplicable (
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER;


/* This function will return 1 if applicability rule is valid */
FUNCTION VerifyApplicabilityRule (
      as_TaskApplSql IN task_task.task_appl_sql_ldesc%TYPE
   ) RETURN NUMBER;


/* This function will return number of parents for a beseline task*/
FUNCTION CountBaselineParents (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER;


/* creates a Dept link between two stasks */
PROCEDURE CreateTaskDependencyLink (
      an_PreviousSchedDbId     IN sched_stask.sched_db_id%TYPE,
      an_PreviousSchedId       IN sched_stask.sched_id%TYPE,
      an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
      an_SchedId              IN sched_stask.sched_id%TYPE,
      on_Return               OUT typn_RetCode
   );

/* creates part requirement */
PROCEDURE AddRmvdPartRequirement (
       an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
       an_InvId         IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_SchedId           IN sched_stask.sched_id%TYPE,
       an_SchedPartId       IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       an_SchedRmvdPartId    OUT sched_rmvd_part.sched_rmvd_part_id%TYPE,
       on_Return            OUT typn_RetCode

   );

   /* find removed inventory */
   PROCEDURE FindRemovedInventory (
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      an_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
      an_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   );

  /* find start inventory */
   PROCEDURE FindStartInventory (
      an_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   );

/* find lates stask instance */
PROCEDURE FindLatestTaskInstance (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskInstanceDbId    OUT sched_stask.sched_db_id%TYPE,
      an_TaskInstanceId      OUT sched_stask.sched_db_id%TYPE,
      on_Return          OUT typn_RetCode);

/* setup remove install part requirements from values in sched_part table */
PROCEDURE GenerateRemoveInstallPartReq (
       an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
       an_InvId         IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_SchedId           IN sched_stask.sched_id%TYPE,
       an_SchedPartId       IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       ab_Remove_bool        IN task_part_list.remove_bool%TYPE,
       ab_Install_bool        IN task_part_list.install_bool%TYPE,
       on_Return            OUT typn_RetCode
   );

/* This procedure will generate all task dependencies that fall within
   the next icn_ScheduleWindow number of days. */
PROCEDURE GenForecastedTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      os_ExitCd    OUT VARCHAR2,
      on_Return    OUT typn_RetCode );

/* This procedure will update the reference between a task and its
   root, non-check, task. */
PROCEDURE UpdateHSched (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode );

/*******************************************************************************
*
* Procedure:    UpdateReplSchedPart
* Arguments:    an_SchedDbId   (long) - the task that should be updated
*               an_SchedId     (long) - ""
* Return:       on_Return      (long) - succss/failure of procedure
*
* Description:  Updated the replacement part requirement for the task and its
*               children. This will also work if the task is a child of a REPL.
*
********************************************************************************
*
* Copyright 1997-2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UpdateReplSchedPart(
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   );

/* This procedure will update the dwt_task_labour_summary table. */
PROCEDURE UpdateTaskLabourSummary (
      an_Months     IN NUMBER,
      on_Return    OUT typn_RetCode );

PROCEDURE UpdatePartsAndToolsReadyBool(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           ab_PartReady          OUT INTEGER,
           ab_ToolReady          OUT INTEGER,
           on_Return             OUT typn_RetCode);

/* To add Assembly Measurement to a Given Task*/
PROCEDURE AddAssemblyMeasurements(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           an_DataTypeDbId       IN mim_data_type.data_type_db_id%TYPE,
           an_DataTypeId         IN mim_data_type.data_type_id%TYPE,
           an_RecParmQty         IN inv_parm_data.rec_parm_qt% TYPE,
           on_Return             OUT typn_RetCode);

END SCHED_STASK_PKG;
/

--changeSet DEV-1445_3:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY SCHED_STASK_PKG IS

/* *** ARRAY TYPES *** */
TYPE typtabn_DbId       IS TABLE OF inv_inv.inv_no_db_id%TYPE     INDEX BY BINARY_INTEGER;
TYPE typtabn_Id         IS TABLE OF inv_inv.inv_no_id%TYPE        INDEX BY BINARY_INTEGER;

/* *** Private procedure declarations *** */

/* procedure used to check for non-historical MOD tasks */
PROCEDURE CheckForNonHistMOD(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      on_TaskExists          OUT typn_RetCode,
      on_Return              OUT typn_RetCode );

/* Check that the inventory's part number is valid when
   you are creating a modification task */
PROCEDURE CheckModPartNum(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_MatchingPartNo      OUT typn_RetCode,
      on_Return              OUT typn_RetCode );


/* Check that the inventory and the task definition have matching boms */
PROCEDURE CheckTaskPartNo(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_TaskApplies         OUT typn_RetCode,
      on_Return              OUT typn_RetCode );

/* create all auto-apply children for a new task */
PROCEDURE AutoCreateChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode );

/* This procedure will create all of the rows in SCHED_PART that come
   from the baseline task definition */
PROCEDURE GenSchedParts (
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return      OUT typn_RetCode );

/* *** Procedure Bodies *** */


/********************************************************************************
*
* Procedure:    GenSchedTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
* Description:  This procedure is used to generate a new Task record. The task
*               will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask. If you do, then the
*               deadlines for this task will be initialized based on the scheduled
*               deadlines for the previous task.
*
* Orig.Coder:     Siku Adam
* Recent Coder:   Natasa Subotic
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenSchedTask(
        an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,
        ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
   ) IS

   /* *** DECLARE LOCAL VARIABLES *** */
   ls_ExitCd               VARCHAR2(8);
   lb_TempPartReady        sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady        sched_stask.tools_ready_bool%TYPE;
   
    
   /* cursor used to get task_defn class class code and class mode code */
   CURSOR lcur_TaskDefinitionClass (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT       
         task_task.task_class_cd,
         ref_task_class.class_mode_cd        
      FROM       
         task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd      = task_task.task_class_cd
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinitionClass  lcur_TaskDefinitionClass%ROWTYPE;

   BEGIN
 
     -- Initialize the return value
     on_Return := icn_NoProc;
  
     /* get the task definition class info */
     OPEN  lcur_TaskDefinitionClass( an_TaskDbId, an_TaskId );
     FETCH lcur_TaskDefinitionClass INTO lrec_TaskDefinitionClass;
     CLOSE lcur_TaskDefinitionClass;
     
     /* Create one scheduled task. If the previous task IS NOT provided new task will have status ACTV,
        otherwise the status will be FORECAST */
     GenOneSchedTask (
          an_EvtEventDbId,
          an_EvtEventId,
          an_InvNoDbId,
          an_InvNoId,
          an_TaskDbId,
          an_TaskId,
          an_PreviousTaskDbId,
          an_PreviousTaskId,
          ad_CompletionDate,
          an_ReasonDbId,
          an_ReasonCd,
          as_UserNote,
          an_HrDbId,
          an_HrId,
          ab_CalledExternally,
          ab_Historic,
          ab_CreateNATask,             
          on_SchedDbId,
          on_SchedId,
          on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;
  
          /* If a previous task was given, then add a row to the evt_event_rel table */
      IF ( an_PreviousTaskDbId <> -1 ) THEN
          CreateTaskDependencyLink(an_PreviousTaskDbId, an_PreviousTaskId, on_SchedDbId, on_SchedId, on_Return);
          IF on_Return < 0 THEN
             RETURN;
          END IF;
       END IF;


      /* For Non-Historic Tasks: */
      IF ab_historic = false THEN
  
        /* Create Task Deadlines */
        PREP_DEADLINE_PKG.PrepareSchedDeadlines( on_SchedDbId, on_SchedId, NULL, NULL, true, ad_PreviousCompletionDt, on_Return );
        IF on_Return < 1 THEN
          RETURN;
        END IF;
  
        /*
         * Create all child tasks, but only for REQs
         * Should not auto-create a job card under a CORR task
         */
        IF ( lrec_TaskDefinitionClass.class_mode_cd = 'REQ' AND lrec_TaskDefinitionClass.task_class_cd <> 'CORR') THEN
           AutoCreateChildTasks( on_SchedDbId, on_SchedId, on_Return );
           IF on_Return < 1 THEN
             RETURN;
           END IF;
  
           -- Evaluate part/tool readiness for this task
           UpdatePartsAndToolsReadyBool(
                 on_SchedDbId,
                 on_SchedId,
                 lb_TempPartReady,
                 lb_TempToolReady,
                 on_Return
              );
           IF on_Return < 1 THEN
              RETURN;
           END IF;
  
           -- if we are creating a replacement
           IF lrec_TaskDefinitionClass.task_class_cd = 'REPL' THEN
  
              --update the replacement part requirement for it and its children
              UpdateReplSchedPart(
                    on_SchedDbId,
                    on_SchedId,
                    on_Return
                 );
              IF on_Return < 1 THEN
                 RETURN;
              END IF;
           END IF;
        END IF;


        /* Update the references to the root task (non-check) */
        UpdateHSched( on_SchedDbId, on_SchedId, on_Return );
        IF on_Return < 1 THEN
           RETURN;
        END IF;
  
         /* Generate all of the dependendent Forecasted Tasks */
        GenForecastedTasks( on_SchedDbId, on_SchedId, ls_ExitCd, on_Return );
        IF on_Return < 1 THEN
           RETURN;
        END IF;

    END IF;

   /* record the event_id as this will ultimately return the highest event_id (original request) */
   on_Return    := icn_Success;

END GenSchedTask;


/********************************************************************************
*
* Procedure:    GenServiceTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
*
* Description:  This procedure is used to generate new scheduled tasks for a service
*                check.
*               Tasks will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask and an_NextTask. If you do,
*               only one new task will be created with status FORECAST and it will be
*               inserted in between an_PreviousTask and an_NextTask. If previous and next
*               task are not provided, the whole chain with ACTV and FORECAST tasks will be created.
*
* Orig.Coder:   Natasa Subotic
*
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenServiceTask(
       an_EvtEventDbId         IN evt_event.event_db_id%TYPE,
        an_EvtEventId           IN evt_event.event_id%TYPE,
        an_InvNoDbId            IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId              IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId             IN sched_stask.task_db_id%TYPE ,
        an_TaskId               IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId     IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId       IN evt_event.event_id%TYPE,
        an_NextTaskDbId         IN evt_event.event_db_id%TYPE,
        an_NextTaskId           IN evt_event.event_id%TYPE,
        ad_CompletionDate       IN evt_event.event_dt%TYPE,
        an_ReasonDbId           IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd             IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote             IN evt_stage.user_stage_note%TYPE,
        an_HrDbId               IN org_hr.hr_db_id%TYPE,
        an_HrId                 IN org_hr.hr_id%TYPE,
        ab_CalledExternally     IN BOOLEAN,
        ab_Historic             IN BOOLEAN,
        ab_CreateNATask         IN BOOLEAN,
        ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId            OUT evt_event.event_db_id%TYPE,
        on_SchedId              OUT evt_event.event_id%TYPE,
        on_Return               OUT typn_RetCode
    ) IS

        lb_TempPartReady        sched_stask.parts_ready_bool%TYPE;
        lb_TempToolReady        sched_stask.tools_ready_bool%TYPE;

   /* cursor used to get task_defn class class code and class mode code */
   CURSOR lcur_TaskDefinitionClass (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT       
         task_task.task_class_cd,
         ref_task_class.class_mode_cd        
      FROM       
         task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd      = task_task.task_class_cd
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinitionClass  lcur_TaskDefinitionClass%ROWTYPE;

   BEGIN

   /* get the task definition class info */
   OPEN  lcur_TaskDefinitionClass( an_TaskDbId, an_TaskId );
   FETCH lcur_TaskDefinitionClass INTO lrec_TaskDefinitionClass;
   CLOSE lcur_TaskDefinitionClass;
   

   /* if previous and next tasks are provided create only one FORECAST task */
   IF ( an_PreviousTaskDbId <> -1 AND an_PreviousTaskId <> -1 AND an_NextTaskDbId <> -1 AND an_NextTaskId <> -1 ) THEN
       GenOneSchedTask(
          an_EvtEventDbId,
          an_EvtEventId,
          an_InvNoDbId,
          an_InvNoId,
          an_TaskDbId,
          an_TaskId,
          an_PreviousTaskDbId,
          an_PreviousTaskId,
          ad_CompletionDate,
          an_ReasonDbId,
          an_ReasonCd,
          as_UserNote,
          an_HrDbId,
          an_HrId,
          ab_CalledExternally,
          ab_Historic,
          ab_CreateNATask,            
          on_SchedDbId,
          on_SchedId,
          on_Return );
          
       IF on_Return < 1 THEN
           RETURN;
        END IF;

       /* insert task in between previous and next task */      
       InsertTask(
            on_SchedDbId,
            on_SchedId,
            an_PreviousTaskDbId,
            an_PreviousTaskId,
            an_NextTaskDbId,
            an_NextTaskId,
            on_Return );
            
      IF on_Return < 1 THEN
         RETURN;
      END IF;  


     /* For Non-Historic Tasks: */
     IF ab_historic = false THEN
  
        /* Create Task Deadlines */
        PREP_DEADLINE_PKG.PrepareSchedDeadlines( on_SchedDbId, on_SchedId, NULL, NULL, true, ad_PreviousCompletionDt, on_Return );
        IF on_Return < 1 THEN
          RETURN;
        END IF;
  
        /*
         * Create all child tasks, but only for REQs
         * Should not auto-create a job card under a CORR task
         */
        IF ( lrec_TaskDefinitionClass.class_mode_cd = 'REQ' AND lrec_TaskDefinitionClass.task_class_cd <> 'CORR') THEN
           AutoCreateChildTasks( on_SchedDbId, on_SchedId, on_Return );
           IF on_Return < 1 THEN
             RETURN;
           END IF;
  
           -- Evaluate part/tool readiness for this task
           UpdatePartsAndToolsReadyBool(
                 on_SchedDbId,
                 on_SchedId,
                 lb_TempPartReady,
                 lb_TempToolReady,
                 on_Return
              );
           IF on_Return < 1 THEN
              RETURN;
           END IF;

           -- if we are creating a replacement
           IF lrec_TaskDefinitionClass.task_class_cd = 'REPL' THEN
  
              --update the replacement part requirement for it and its children
              UpdateReplSchedPart(
                    on_SchedDbId,
                    on_SchedId,
                    on_Return
                 );
              IF on_Return < 1 THEN
                 RETURN;
              END IF;
           END IF;
        END IF;


        /* Update the references to the root task (non-check) */
        UpdateHSched( on_SchedDbId, on_SchedId, on_Return );
        IF on_Return < 1 THEN
           RETURN;
        END IF;
     END IF;

    /* if previous and next tasks are not provided create chain with ACTV and FORECAST tasks */
   ELSE
      GenSchedTask(
          an_EvtEventDbId,
          an_EvtEventId,
          an_InvNoDbId,
          an_InvNoId,
          an_TaskDbId,
          an_TaskId,
          an_PreviousTaskDbId,
          an_PreviousTaskId,
          ad_CompletionDate,
          an_ReasonDbId,
          an_ReasonCd,
          as_UserNote,
          an_HrDbId,
          an_HrId,
          ab_CalledExternally,
          ab_Historic,
          ab_CreateNATask,
          ad_PreviousCompletionDt,
          on_SchedDbId,
          on_SchedId,
          on_Return
       );
       
       IF on_Return < 1 THEN
           RETURN;
       END IF;
        
   END IF;
   
   -- Return success
   on_Return := icn_Success;

END GenServiceTask;



/********************************************************************************
*
* Procedure:    InsertTask
* Arguments:
*        an_TaskDbId             (long) - The task that will be inserted in the existing chain of tasks
*        an_TaskId               (long) - ""
*        an_PreviousTaskDbId     (long) - The task that the new task will be inserted after
*        an_PreviousTaskId       (long) - ""
*        an_NextTaskDbId         (long) - The task taht the new task will be inserted before
*        an_NextTaskId           (long) - ""
* Return:
*        on_Return                (long) - Success/Failure of inserting the task
*
* Description:  This procedure is used to insert a new task in between two tasks in the existing chain.
*
* Orig.Coder:   Natasa Subotic
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InsertTask(
        an_TaskDbId           IN evt_event.event_db_id%TYPE,
        an_TaskId             IN evt_event.event_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        an_NextTaskDbId       IN evt_event.event_db_id%TYPE,
        an_NextTaskId         IN evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
   ) IS

 BEGIN

  -- Initialize the return value
   on_Return := icn_NoProc;

   /* Before inserting new task in between two tasks, delete relationship link between them */
   DELETE evt_event_rel
   WHERE
   event_db_id = an_PreviousTaskDbId AND
   event_id    = an_PreviousTaskId
   AND
   rel_event_db_id = an_NextTaskDbId AND
   rel_event_id    = an_NextTaskId
   AND
   evt_event_rel.rel_type_cd='DEPT';


   /* Create relationship between the previoius and the new task*/
   IF (  an_PreviousTaskDbId <> -1 ) THEN
      CreateTaskDependencyLink(an_PreviousTaskDbId,  an_PreviousTaskId,  an_TaskDbId, an_TaskId, on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

  /* Create relationship between the new and the next task */
   IF (  an_PreviousTaskDbId <> -1 ) THEN
      CreateTaskDependencyLink(an_TaskDbId,  an_TaskId,  an_NextTaskDbId, an_NextTaskId, on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','CreateTaskDependencyLink: '||SQLERRM);
     on_Return:= icn_Error;

END InsertTask;


/********************************************************************************
*
* Procedure:    GenOneSchedTask
* Arguments:
*        an_InvNoDbId             (long) - The inventory that the task will be
*                                          created on
*        an_InvNoId               (long) - ""
*        an_TaskDbId              (long) - The task definition that the new task
*                                          will be created from
*        an_TaskId                (long) - ""
*        an_PreviousTaskDbId      (long) - The previous task (set to -1 if no
*                                          previous task exists)
*        an_PreviousTaskId        (long) - ""
*        ad_CompletionDate        (date) - completion date only used when task is historic
*        an_ReasonDbId            (long) - reason for creation db id
*        an_ReasonCd             (String)- reason for creation code
*        as_UserNote             (String)- user note
*        an_HrDbId                (long) - human resource authorizing creation
*        an_HrId                  (long) - ""
*        ab_CalledExternally      (bool) - Flag indicating whether this procedure
*                                          was called externally (1) or
*                                          internally (0).  The flag controls
*                                          whether or not an exception is thrown
*                                          if the procedure fails to create a
*                                          task.
*        ab_Historic              (bool) - Flag indicating whether newly created
*                                          task is historic. Historic task will not generate
*                                          forecasted tasks,deadlines and children.
*        ab_CreateNATask          (bool) - Flag indicating whether newly created
*                                          task is N/A. evt_stage will not be created.
*        ad_PreviousCompletionDt      (date) - The completion date of the installation task, that triggered
*                                          the create_on_install logic. NULL otherwise.
* Return:
*        on_SchedDbId     (long) - The primary key of the newly created task
*        on_SchedId       (long) - ""
*        on_Return        (long) - Success/Failure of sched task generation
* Description:  This procedure is used to generate a new Task record. The task
*               will be created on the inventory given by an_InvNoDbId:an_InvNoId,
*               and it will be based on the task definition an_TaskDbId:an_TaskId.
*
*               You can optionally specify an_PreviousTask. If you do, then the
*               deadlines for this task will be initialized based on the scheduled
*               deadlines for the previous task.
*
* Orig.Coder:     Natasa Subotic
*
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenOneSchedTask (
        an_EvtEventDbId       IN evt_event.event_db_id%TYPE,
        an_EvtEventId         IN evt_event.event_id%TYPE,
        an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
        an_InvNoId            IN inv_inv.inv_no_id%TYPE,
        an_TaskDbId           IN sched_stask.task_db_id%TYPE ,
        an_TaskId             IN sched_stask.task_id%TYPE,
        an_PreviousTaskDbId   IN evt_event.event_db_id%TYPE,
        an_PreviousTaskId     IN evt_event.event_id%TYPE,
        ad_CompletionDate     IN evt_event.event_dt%TYPE,
        an_ReasonDbId         IN evt_stage.stage_reason_db_id%TYPE,
        an_ReasonCd           IN evt_stage.stage_reason_cd%TYPE,
        as_UserNote           IN evt_stage.user_stage_note%TYPE,
        an_HrDbId             IN org_hr.hr_db_id%TYPE,
        an_HrId               IN org_hr.hr_id%TYPE,
        ab_CalledExternally   IN BOOLEAN,
        ab_Historic           IN BOOLEAN,
        ab_CreateNATask       IN BOOLEAN,             
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
    )IS



    /* *** DECLARE LOCAL VARIABLES *** */

   ln_EventDbId            evt_event.event_db_id%TYPE;              /* event database id */
   ln_EventId              evt_event.event_id%TYPE;                 /* event id */
   ln_LabourId             sched_labour.labour_id%TYPE;             /* labour id of assigned labour */
   ln_ToolId               evt_tool.tool_id%TYPE;                   /* tool id of assigned tool */
   ln_LabourRoleId         sched_labour_role.labour_role_id%TYPE;   /* labour id of assigned labour */
   ln_LabourRoleStatusId   sched_labour_role_status.status_id%TYPE; /* labour role id of assigned labour */
   ln_StepId               sched_step.step_id%TYPE;                 /* step id of task step */
   ln_ParentTaskCount      NUMBER;                                  /* one if this task has a parent, 0 if it does not. */
   ls_TaskClassMode        ref_task_class.class_mode_cd%TYPE;       /* task class mode identifier */
   ln_TaskApplies          typn_RetCode;                            /* task applies return value. */
   ln_SubEventOrd          evt_event.sub_event_ord%TYPE;            /* child ordering number */
   ln_InitalTaskStatus     ref_event_status.event_status_cd%TYPE;   /* the initial event status */
   ln_PreviousTaskHistBool evt_event.hist_bool%TYPE;                /* historic value of the previous event */
   ls_UserStageNote        evt_stage.user_stage_note%TYPE;          /* user stage note */
   ls_TaskType             evt_stage.user_stage_note%TYPE;          /* the task type check, work order, task */
   ln_ExistsCount          NUMBER;
   ln_histBool             NUMBER;
   ln_woCommitLine         NUMBER;
   ln_PreviousTaskDbId     NUMBER DEFAULT -1;
   ln_PreviousTaskId       NUMBER DEFAULT -1;
   ln_StdPartNoDbId        NUMBER;
   ln_StdPartNoId          NUMBER;
   ln_count                NUMBER;
   ln_StageId              evt_stage.stage_id%TYPE;
   ln_PreventExeReqBool    sched_stask_flags.prevent_exe_bool%TYPE;
   ld_PreventExeReviewDt   sched_stask_flags.prevent_exe_review_dt%TYPE;
   ln_EngUnitDbId          mim_data_type.eng_unit_db_id%TYPE;
   ls_EngUnitCD            mim_data_type.eng_unit_cd%TYPE;

      /* *** DECLARE EXCEPTIONS *** */
   xc_UnknownSQLError         EXCEPTION;
   xc_InvHasIncorrectPartNo   EXCEPTION;
   xc_TaskDefnNotActive       EXCEPTION;
   xc_InvIsLocked             EXCEPTION;

   /* *** DECLARE CURSORS *** */
   /* cursor used to get task_defn info */
   CURSOR lcur_TaskDefinition (
         an_TaskDbId task_task.task_db_id%TYPE,
         an_TaskId   task_task.task_id%TYPE
      ) IS
      SELECT
         task_task.task_class_db_id,
         task_task.task_class_cd,
         task_task.task_subclass_db_id,
         task_task.task_subclass_cd,
         task_task.task_originator_db_id,
         task_task.task_originator_cd,
         task_task.bitmap_db_id,
         task_task.bitmap_tag,
         task_task.task_cd,
         task_task.task_name,
         task_task.task_priority_db_id,
         task_task.task_priority_cd,
         task_task.task_def_status_db_id,
         task_task.task_def_status_cd,
         task_task.task_ref_sdesc,
         task_task.recurring_task_bool,
         task_task.relative_bool,
         task_task.task_ldesc,
         task_task.routine_bool,
         task_task.effective_dt,
         task_task.resource_sum_bool,
         task_task.instruction_ldesc,
         task_task.unique_bool,
         task_task.auto_complete_bool,
         task_task.task_appl_eff_ldesc,
         task_task.est_duration_qt,
         task_task.min_plan_yield_pct,
         task_task.soft_deadline_bool,
         task_task.issue_account_db_id,
         task_task.issue_account_id,
         task_task.etops_bool,
         task_task.workscope_bool
      FROM
         task_task
      WHERE
         task_task.task_id    = an_TaskId AND
         task_task.task_db_id = an_TaskDbId;
   lrec_TaskDefinition  lcur_TaskDefinition%ROWTYPE;

   /* cursor used to get inventory details */
   CURSOR lcur_Inventory (
         cn_InvNoDbId   inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId     inv_inv.inv_no_id%TYPE
      ) IS
      SELECT
         inv_inv.nh_inv_no_db_id,
         inv_inv.nh_inv_no_id,
         inv_inv.h_inv_no_db_id,
         inv_inv.h_inv_no_id,
         inv_inv.assmbl_inv_no_db_id,
         inv_inv.assmbl_inv_no_id,
         inv_inv.assmbl_db_id,
         inv_inv.assmbl_cd,
         inv_inv.assmbl_bom_id,
         inv_inv.assmbl_pos_id,
         inv_inv.part_no_db_id,
         inv_inv.part_no_id,
         inv_inv.bom_part_db_id,
         inv_inv.bom_part_id,
         inv_inv.locked_bool,
         ass_inv_inv.appl_eff_cd

      FROM
         inv_inv,
         inv_inv ass_inv_inv
      WHERE
         inv_inv.inv_no_db_id = cn_InvNoDbId AND
         inv_inv.inv_no_id    = cn_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         ass_inv_inv.inv_no_db_id  (+)= decode( inv_inv.inv_class_cd, 'ASSY',
                                           inv_inv.inv_no_db_id,
                                           inv_inv.assmbl_inv_no_db_id
                                        )
         AND
         ass_inv_inv.inv_no_id     (+)= decode (inv_inv.inv_class_cd, 'ASSY',
                                           inv_inv.inv_no_id,
                                           inv_inv.assmbl_inv_no_id
                                        );
   lrec_Inventory lcur_Inventory%ROWTYPE;

   /* used to get labour list for a task defn */
   CURSOR lcur_TaskLabour (
         an_TaskDbId    task_labour_list.task_db_id%TYPE,
         an_TaskId      task_labour_list.task_id%TYPE
      ) IS
      SELECT
         labour_skill_db_id,
         labour_skill_cd,
         man_pwr_ct,
         work_perf_hr,
         work_perf_bool,
         cert_hr,
         cert_bool,
         insp_hr,
         insp_bool
      FROM task_labour_list
      WHERE
         task_db_id = an_TaskDbId AND
         task_id    = an_TaskId;

   /* used to get tool list for a task defn */
   CURSOR lcur_Tool (
         an_TaskDbId task_tool_list.task_db_id%TYPE,
         an_TaskId   task_tool_list.task_id%TYPE
      ) IS
      SELECT
         task_tool_list.bom_part_db_id,
         task_tool_list.bom_part_id,
         task_tool_list.task_tool_id,
         task_tool_list.sched_hr
      FROM task_tool_list
      WHERE
         task_tool_list.task_db_id = an_TaskDbId AND
         task_tool_list.task_id    = an_TaskId;

   /* used to get steps for a task defn */
   CURSOR lcur_Step (
         an_TaskDbId task_step.task_db_id%TYPE,
         an_TaskId   task_step.task_id%TYPE
      ) IS
      SELECT
         task_step.step_ord,
         task_step.step_ldesc
      FROM task_step
      WHERE
         task_step.task_db_id = an_TaskDbId AND
         task_step.task_id    = an_TaskId;

   /* used to get zone list for a task defn */
   CURSOR lcur_Zone (
         an_TaskDbId task_zone.task_db_id%TYPE,
         an_TaskId   task_zone.task_id%TYPE
      ) IS
      SELECT
         task_zone.zone_db_id,
         task_zone.zone_id
      FROM task_zone
      WHERE
         task_zone.task_db_id = an_TaskDbId AND
         task_zone.task_id    = an_TaskId;

   /* used to get panel list for a task defn */
   CURSOR lcur_Panel (
         an_TaskDbId task_panel.task_db_id%TYPE,
         an_TaskId   task_panel.task_id%TYPE
      ) IS
      SELECT
         task_panel.panel_db_id,
         task_panel.panel_id
      FROM task_panel
      WHERE
         task_panel.task_db_id = an_TaskDbId AND
         task_panel.task_id    = an_TaskId;

   /* used to get ietm list for a task defn */
   CURSOR lcur_TaskIetm (
         an_TaskDbId task_task_ietm.task_db_id%TYPE,
         an_TaskId   task_task_ietm.task_id%TYPE
      ) IS
      SELECT
         task_task_ietm.task_ietm_id,
         task_task_ietm.ietm_db_id,
         task_task_ietm.ietm_id,
         task_task_ietm.ietm_topic_id,
         task_task_ietm.ietm_ord,
         ietm_topic.attach_blob,
         ietm_topic.print_bool
      FROM
         task_task_ietm
         INNER JOIN ietm_topic ON
            ietm_topic.ietm_db_id    = task_task_ietm.ietm_db_id AND
            ietm_topic.ietm_id       = task_task_ietm.ietm_id AND
            ietm_topic.ietm_topic_id = task_task_ietm.ietm_topic_id,
         inv_inv
         INNER JOIN inv_inv h_inv_inv ON
            h_inv_inv.inv_no_db_id    = inv_inv.h_inv_no_db_id AND
            h_inv_inv.inv_no_id       = inv_inv.h_inv_no_id    AND
            h_inv_inv.rstat_cd        =  0
         LEFT OUTER JOIN inv_inv assmbl_inv_inv ON
           assmbl_inv_inv.inv_no_db_id    = inv_inv.assmbl_inv_no_db_id AND
           assmbl_inv_inv.inv_no_id       = inv_inv.assmbl_inv_no_id    AND
           assmbl_inv_inv.rstat_cd        = 0
      WHERE
         task_task_ietm.task_db_id = an_TaskDbId AND
         task_task_ietm.task_id    = an_TaskId
         AND
         inv_inv.inv_no_db_id = an_InvNoDbId    AND
         inv_inv.inv_no_id    = an_InvNoId
         AND
         isIETMApplicable(inv_inv.inv_class_cd,inv_inv.appl_eff_cd,h_inv_inv.appl_eff_cd,assmbl_inv_inv.inv_no_db_id,assmbl_inv_inv.appl_eff_cd,ietm_topic.appl_eff_ldesc ) = 1
         AND
         (
            h_inv_inv.carrier_db_id IS NULL
            OR
            EXISTS
            ( SELECT
                 1
              FROM
                 ietm_topic_carrier
              WHERE
                 ietm_topic_carrier.carrier_db_id =  h_inv_inv.carrier_db_id          AND
                 ietm_topic_carrier.carrier_id    =  h_inv_inv.carrier_id             AND
                 ietm_topic_carrier.ietm_db_id    =  ietm_topic.ietm_db_id            AND
                 ietm_topic_carrier.ietm_id       =  ietm_topic.ietm_id               AND
                 ietm_topic_carrier.ietm_topic_id =  ietm_topic.ietm_topic_id
            )
         );

   /* used to get measurements for a task  based on a task defn */
   /* Ignore if measurement is an assembly measurement */
   CURSOR lcur_Measurement (
         an_TaskDbId task_parm_data.task_db_id%TYPE,
         an_TaskId   task_parm_data.task_id%TYPE
      ) IS
      SELECT
         task_parm_data.data_type_db_id,
         task_parm_data.data_type_id,
         task_parm_data.data_ord
      FROM
         task_parm_data
      WHERE
         task_parm_data.task_db_id = an_TaskDbId
         AND
         task_parm_data.task_id    = an_TaskId
         AND
         task_parm_data.data_type_db_id || ':' || task_parm_data.data_type_id NOT IN
      (SELECT
           ref_data_type_assmbl_class.data_type_db_id || ':' || ref_data_type_assmbl_class.data_type_id
       FROM
           task_parm_data,
           ref_data_type_assmbl_class
       WHERE
           task_parm_data.task_db_id = an_TaskDbId
           AND
           task_parm_data.task_id    = an_TaskId
           AND
           ref_data_type_assmbl_class.data_type_db_id = task_parm_data.data_Type_db_id
           AND
           ref_data_type_assmbl_class.data_type_id = task_parm_data.data_type_id
      );

  /* Cursor for getting assembly measurements associated to the task definition */
  CURSOR lcur_Assembly_Measurement(
   an_TaskDbId task_parm_data.task_db_id%TYPE,
        an_TaskId   task_parm_data.task_id%TYPE
      ) IS
   SELECT
      task_parm_data.data_type_db_id,
      task_parm_data.data_type_id,
      task_parm_data.data_ord,
      ref_data_type_assmbl_class.assmbl_class_db_id,
      ref_data_type_assmbl_class.assmbl_class_cd

   FROM

      task_parm_data

      INNER JOIN ref_data_type_assmbl_class ON
         task_parm_data.data_type_db_id                 = ref_data_type_assmbl_class.data_type_db_id AND
         task_parm_data.data_type_id                    = ref_data_type_assmbl_class.data_type_id

      INNER JOIN ref_assmbl_class ON
         ref_data_type_assmbl_class.assmbl_class_db_id  = ref_assmbl_class.assmbl_class_db_id AND
         ref_data_type_assmbl_class.assmbl_class_cd     = ref_assmbl_class.assmbl_class_cd

   WHERE
      task_parm_data.task_db_id = an_TaskDbId AND
      task_parm_data.task_id    = an_TaskId;

   /* Cursor for getting the assemblies and sub assemblies from an inventory */
   CURSOR lcur_Assembly_Inventories(
      an_InventoryDbId inv_inv.inv_no_db_id%TYPE,
      an_InventoryId inv_inv.inv_no_id%TYPE
   )  IS

   SELECT
     inv_inv.orig_assmbl_db_id,
     inv_inv.orig_assmbl_cd,
     eqp_assmbl.assmbl_class_db_id,
     eqp_assmbl.assmbl_class_cd,
     inv_inv.inv_no_db_id,
     inv_inv.inv_no_id

   FROM
     eqp_assmbl,
     inv_inv

   WHERE
      inv_inv.orig_assmbl_db_id       = eqp_assmbl.assmbl_db_Id AND
      inv_inv.orig_assmbl_cd          = eqp_assmbl.assmbl_cd AND
      inv_inv.inv_no_id
      IN (
         SELECT
            inv_inv.inv_no_id
         FROM
            inv_inv
         WHERE
            inv_inv.inv_class_cd = 'ASSY'
            START WITH
               inv_inv.inv_no_db_id = an_InventoryDbId AND
               inv_inv.inv_no_id    = an_InventoryId
            CONNECT BY
               inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
               inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
      );

   /* used to get work types for a task defn */
   CURSOR lcur_WorkType (
         an_TaskDbId task_work_type.task_db_id%TYPE,
         an_TaskId   task_work_type.task_id%TYPE
      ) IS
      SELECT
         task_work_type.work_type_db_id,
         task_work_type.work_type_cd
      FROM
         task_work_type
      WHERE
         task_work_type.task_db_id = an_TaskDbId AND
         task_work_type.task_id    = an_TaskId;

 BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;


   /* get the task definition info */
   OPEN  lcur_TaskDefinition( an_TaskDbId, an_TaskId );
   FETCH lcur_TaskDefinition INTO lrec_TaskDefinition;
   CLOSE lcur_TaskDefinition;

   /* get the inventory info */
   OPEN  lcur_Inventory(an_InvNoDbId, an_InvNoId);
   FETCH lcur_Inventory INTO lrec_Inventory;
   CLOSE lcur_Inventory;

   /* if task will be historic */
   IF (ab_Historic) THEN
      -- initial status is COMPLETE and is Historic
      ln_InitalTaskStatus:='COMPLETE';
      ln_histBool:=1;
   ELSE
      -- initial status is ACTV and is Non-Historic
      ln_InitalTaskStatus:='ACTV';
      ln_histBool:=0;
   END IF;


   /* if previous task exists then use it */
   IF ( an_PreviousTaskDbId <> -1 AND an_PreviousTaskId <> -1) THEN
      ln_PreviousTaskDbId := an_PreviousTaskDbId;
      ln_PreviousTaskId   := an_PreviousTaskId;
   /* if the task is active and it has a parent task defined in the baseline */
   ELSIF ((NOT ab_Historic) AND CountBaselineParents(an_TaskDbId, an_TaskId) > 0) THEN
      FindLatestTaskInstance(
            an_TaskDbId,
            an_TaskId,
            an_InvNoDbId,
            an_InvNoId,
            ln_PreviousTaskDbId,
            ln_PreviousTaskId,
            on_Return
         );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   ls_TaskType:='task';

    /* if previous task exists */
   IF (ln_PreviousTaskDbId <> -1 ) THEN

      /* get the next rel_id for the previous task */
      SELECT hist_bool
      INTO ln_PreviousTaskHistBool
      FROM evt_event
      WHERE
         event_db_id = ln_PreviousTaskDbId AND
         event_id    = ln_PreviousTaskId   AND
         evt_event.rstat_cd = 0;

      /* if the previous task exists and not historic then this task is a forecasted task */
      IF ln_PreviousTaskHistBool <> 1 THEN
         ln_InitalTaskStatus := 'FORECAST';
      END IF;

   END IF;

   /* if task is unique, and there is and active instance of that task on the aircraft */
   IF (
         lrec_TaskDefinition.unique_bool = 1 AND
         ln_InitalTaskStatus <> 'FORECAST' AND
         ln_InitalTaskStatus <> 'COMPLETE' AND
         CountTaskInstances(an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId) > 0
         ) THEN
      on_Return:=icn_success;
      RETURN;
   END IF;

   /************************************************
   *  PERFORM VALIDATION BEFORE CREATING THE TASK
   ************************************************/

   /* check to make sure that the inventory is not locked */
   IF lrec_Inventory.locked_bool = 1 THEN
      RAISE xc_InvIsLocked;
   END IF;

   /* check to make sure that the task definition is active */
   IF lrec_TaskDefinition.task_def_status_cd NOT IN ('ACTV', 'SUPRSEDE') THEN
      RAISE xc_TaskDefnNotActive;
   END IF;

   /* check to make sure that the given inventory has the correct part no */
   CheckTaskPartNo(
         an_InvNoDbId,
         an_InvNoId,
         an_TaskDbId,
         an_TaskId,
         ln_TaskApplies,
         on_Return
      );
   IF on_Return < 1 THEN
      RETURN;
   END IF;

   IF ln_TaskApplies = icn_False THEN
      RAISE xc_InvHasIncorrectPartNo;
   END IF;

   /* check to make sure that the given piece of inventory falls within
      the task definition's applicability rule */
   ln_TaskApplies :=
         IsTaskApplicable(
               an_InvNoDbId,
               an_InvNoId,
               an_TaskDbId,
               an_TaskId
            );

   IF ln_TaskApplies = 1 THEN
      /* if the rule applicability passes, check to make sure that
         range applicability passes as well */
      ln_TaskApplies := isApplicable( lrec_TaskDefinition.task_appl_eff_ldesc, lrec_Inventory.appl_eff_cd );
   END IF;

   /* do not create a task branch (return) if the task definition is not applicable to the given piece of
   inventory and this procedure is called inernally, ie. task dependency. Applicability of task definition
   has to be checked in the calling code */
   IF ln_TaskApplies = 0 AND NOT ab_CalledExternally THEN
      RETURN;
   END IF;


   /************************************************
   *  CREATE THE NEW TASK, AND UPDATE THE DATABASE
   *********************************************** */

   /* if ln_EventDbId or ln_EventId was not provided, derive the new values for  the new task*/
   IF an_EvtEventDbId IS  null OR  an_EvtEventId  IS  null THEN
      -- get the db_id value
      ln_EventDbId := APPLICATION_OBJECT_PKG.getdbid;
      -- get the id of the new task
      SELECT event_id_seq.NEXTVAL INTO ln_EventId FROM dual;
   ELSE
      -- set local vars to input values
      ln_EventDbId :=  an_EvtEventDbId;
      ln_EventId   :=  an_EvtEventId;
   END IF;

   /*
    * check to make sure that the current task definition is a root
    * if ln_ParentTaskCount=0 then root if ln_ParentTaskCount=1 then subtask
    */
   SELECT
      ref_task_class.class_mode_cd
   INTO ls_TaskClassMode
   FROM
      task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
         ref_task_class.task_class_cd      = task_task.task_class_cd
   WHERE
      task_task.task_db_id  = an_TaskDbId  AND
      task_task.task_id     = an_TaskId
      AND
      ref_task_class.rstat_cd = 0;

   IF ( ls_TaskClassMode = 'BLOCK' ) THEN
      ln_SubEventOrd := 1;
   ELSE
      IF ( ls_TaskClassMode = 'REQ' ) THEN
         SELECT
            count(*)
         INTO
            ln_ParentTaskCount
         FROM
            task_task
            INNER JOIN task_block_req_map ON
               task_block_req_map.req_task_defn_db_id = task_task.task_defn_db_id   AND
               task_block_req_map.req_task_defn_id    = task_task.task_defn_id
         WHERE
            task_task.task_db_id = an_TaskDbId  AND
            task_task.task_id    = an_TaskId;

         IF ( ln_ParentTaskCount = 0 ) THEN
            ln_SubEventOrd := 1;
         END IF;
      END IF;
   END IF;

   /* Create the Evt_Event row */
   INSERT INTO evt_event (
      event_db_id,
      event_id,
      event_type_db_id,
      event_type_cd,
      event_status_db_id,
      event_status_cd,
      actual_start_dt,
      actual_start_gdt,
      event_dt,
      event_gdt,
      sched_priority_db_id,
      sched_priority_cd,
      bitmap_db_id,
      bitmap_tag,
      event_sdesc,
      event_ldesc,
      hist_bool,
      seq_err_bool,
      h_event_db_id,
      h_event_id,
      sub_event_ord,
      doc_ref_sdesc )
   VALUES (
      ln_EventDbId,
      ln_EventId,
      0,
      'TS',
      0,
      ln_InitalTaskStatus,
      ad_CompletionDate,
      ad_CompletionDate,
      ad_CompletionDate,
      ad_CompletionDate,
      0,
      'NONE',
      lrec_TaskDefinition.bitmap_db_id,
      lrec_TaskDefinition.bitmap_tag,
      lrec_TaskDefinition.task_cd || ' (' || lrec_TaskDefinition.task_name || ')',
      lrec_TaskDefinition.task_ldesc,
      ln_histBool,
      0,
      ln_EventDbId,
      ln_EventId,
      ln_SubEventOrd,
      lrec_TaskDefinition.task_ref_sdesc
   );

   /* Create the Evt_Inv row */
   INSERT INTO evt_inv (
         event_db_id,
         event_id,
         event_inv_id,
         inv_no_db_id,
         inv_no_id,
         nh_inv_no_db_id,
         nh_inv_no_id,
         assmbl_inv_no_db_id,
         assmbl_inv_no_id,
         h_inv_no_db_id,
         h_inv_no_id,
         assmbl_db_id,
         assmbl_cd,
         assmbl_bom_id,
         assmbl_pos_id,
         part_no_db_id,
         part_no_id,
         bom_part_db_id,
         bom_part_id,
         main_inv_bool)
      VALUES (
         ln_EventDbId,
         ln_EventId,
         1,
         an_InvNoDbId,
         an_InvNoId,
         lrec_Inventory.nh_inv_no_db_id,
         lrec_Inventory.nh_inv_no_id,
         lrec_Inventory.assmbl_inv_no_db_id,
         lrec_Inventory.assmbl_inv_no_id,
         lrec_Inventory.h_inv_no_db_id,
         lrec_Inventory.h_inv_no_id,
         lrec_Inventory.assmbl_db_id,
         lrec_inventory.assmbl_cd,
         lrec_Inventory.assmbl_bom_id,
         lrec_Inventory.assmbl_pos_id,
         lrec_Inventory.part_no_db_id,
         lrec_Inventory.part_no_id,
         lrec_Inventory.bom_part_db_id,
         lrec_Inventory.bom_part_id,
         1 );


   IF lrec_TaskDefinition.task_class_cd = 'CHECK' THEN
      ls_TaskType := 'check';
      ln_woCommitLine := 0;
   ELSIF lrec_TaskDefinition.task_class_cd = 'RO' THEN
      ls_TaskType := 'work order';
      ln_woCommitLine := 0;
   END IF;

   /* Create the Sched_Stask row */
   INSERT INTO sched_stask (
         sched_db_id,
         sched_id,
         task_db_id,
         task_id,
         task_class_db_id,
         task_class_cd,
         task_subclass_db_id,
         task_subclass_cd,
         task_originator_db_id,
         task_originator_cd,
         task_priority_db_id,
         task_priority_cd,
         task_ref_sdesc,
         routine_bool,
         resource_sum_bool,
         instruction_ldesc,
         issued_dt,
         issued_gdt,
         orig_part_no_db_id,
         orig_part_no_id,
         wo_commit_line_ord,
         adhoc_recur_bool,
         corr_fix_bool,
         auto_complete_bool,
         barcode_sdesc,
         est_duration_qt,
         min_plan_yield_pct,
         prevent_lpa_bool,
         soft_deadline_bool,
         issue_account_db_id,
         issue_account_id,
         main_inv_no_db_id,
         main_inv_no_id,
         etops_bool)
      VALUES (
         ln_EventDbId,
         ln_EventId,
         an_TaskDbId,
         an_TaskId,
         lrec_TaskDefinition.task_class_db_id,
         lrec_TaskDefinition.task_class_cd,
         lrec_TaskDefinition.task_subclass_db_id,
         lrec_TaskDefinition.task_subclass_cd,
         lrec_TaskDefinition.task_originator_db_id,
         lrec_TaskDefinition.task_originator_cd,
         lrec_TaskDefinition.task_priority_db_id,
         lrec_TaskDefinition.task_priority_cd,
         lrec_TaskDefinition.task_ref_sdesc,
         lrec_TaskDefinition.routine_bool,
         lrec_TaskDefinition.resource_sum_bool,
         lrec_TaskDefinition.instruction_ldesc,
         SYSDATE,
         SYSDATE,
         lrec_Inventory.part_no_db_id,
         lrec_Inventory.part_no_id,
         decode(ln_histBool, 1, ln_woCommitLine, null),
         0,
         1,
         NVL(lrec_TaskDefinition.auto_complete_bool, 0 ),
         GENERATE_TASK_BARCODE(),
         lrec_TaskDefinition.est_duration_qt,
         lrec_TaskDefinition.min_plan_yield_pct,
         decode(lrec_TaskDefinition.task_class_cd, 'RO', 1, decode(lrec_TaskDefinition.task_class_cd, 'CHECK', 1, 0)),
         lrec_TaskDefinition.soft_deadline_bool,
         lrec_TaskDefinition.issue_account_db_id,
         lrec_taskDefinition.issue_account_id,
         an_InvNoDbId,
         an_InvNoId,
         lrec_TaskDefinition.etops_bool);


   /* The note changes based on whether or not the task being created is a dependent task */
   IF ( an_PreviousTaskDbId = -1 ) THEN
      ls_UserStageNote := 'The '|| ls_TaskType || ' has been created.';
   ELSE
      ls_UserStageNote := 'The ' || ls_TaskType || ' has been created as a result of task dependency.';
   END IF;

   IF ( NOT ab_CreateNATask ) THEN
      /* Get the next Stage ID */
      SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

      /* insert a record into the status history */
      INSERT INTO evt_stage (
         event_db_id,
         event_id,
         stage_id,
         event_status_db_id,
         event_status_cd,
         hr_db_id,
         hr_id,
         stage_reason_db_id,
         stage_reason_cd,
         stage_dt,
         stage_gdt,
         user_stage_note,
         system_bool
      )
      VALUES (
         ln_EventDbId,
         ln_EventId,
         ln_StageId,
         0,
         ln_InitalTaskStatus,
         an_HrDbId,
         an_HrId,
         an_ReasonDbId,
         an_ReasonCd,
         SYSDATE,
         SYSDATE,
         NVL( as_UserNote, ls_UserStageNote ),
         DECODE( as_UserNote, null, 1, 0 )
      );
   END IF;

   /* If task class mode is REQ, then check to see if the requirement is prevented from being executed */
   IF ( ls_TaskClassMode = 'REQ' ) THEN
      SELECT
         prevent_exe_bool,
         prevent_exe_review_dt
      INTO
         ln_PreventExeReqBool,
         ld_PreventExeReviewDt
      FROM
         task_task_flags
      WHERE
         task_db_id = an_TaskDbId AND
         task_id    = an_TaskId;

      /* If the requirement is prevented from being executed, then make sure the actual task is prevented as well */
      IF ( ln_PreventExeReqBool = 1 ) THEN

         UPDATE
            sched_stask_flags
         SET
            prevent_exe_bool = 1,
            prevent_exe_review_dt = ld_PreventExeReviewDt
         WHERE
            sched_db_id = ln_EventDbId AND
            sched_id    = ln_EventId;

         /* Get the next stage id */
         SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

         /* Add a history note */
         INSERT INTO evt_stage (
            event_db_id,
            event_id,
            stage_id,
            event_status_db_id,
            event_status_cd,
            hr_db_id,
            hr_id,
            stage_reason_db_id,
            stage_reason_cd,
            stage_dt,
            stage_gdt,
            user_stage_note,
            system_bool
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            ln_StageId,
            0,
            ln_InitalTaskStatus,
            an_HrDbId,
            an_HrId,
            NULL,
            NULL,
            SYSDATE,
            SYSDATE,
            'The task has been prevented from being executed.',
            1
         );
      END IF;
   END IF;


   /* Add all of the Work Types */
   FOR lrec_WorkType IN lcur_WorkType( an_TaskDbId, an_TaskId )
   LOOP
      INSERT INTO sched_work_type (
         sched_db_id,
         sched_id,
         work_type_db_id,
         work_type_cd
      )
      VALUES (
         ln_EventDbId,
         ln_EventId,
         lrec_WorkType.work_type_db_id,
         lrec_WorkType.work_type_cd
      );
   END LOOP;

   /* Add all of the Baseline Attachments and Technical References*/
   FOR lrec_TaskIetm IN lcur_TaskIetm( an_TaskDbId, an_TaskId )
   LOOP

      IF lrec_TaskIetm.attach_blob IS NULL THEN
         INSERT INTO evt_ietm (
            event_db_id,
            event_id,
            event_ietm_id,
            ietm_db_id,
            ietm_id,
            ietm_topic_id,
            ietm_ord
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            lrec_TaskIetm.task_ietm_id,
            lrec_TaskIetm.ietm_db_id,
            lrec_TaskIetm.ietm_id,
            lrec_TaskIetm.ietm_topic_id,
            lrec_TaskIetm.ietm_ord
         );
      ELSE
         INSERT INTO evt_attach (
            event_db_id,
            event_id,
            event_attach_id,
            ietm_db_id,
            ietm_id,
            ietm_topic_id,
            print_bool
         )
         VALUES (
            ln_EventDbId,
            ln_EventId,
            lrec_TaskIetm.task_ietm_id,
            lrec_TaskIetm.ietm_db_id,
            lrec_TaskIetm.ietm_id,
            lrec_TaskIetm.ietm_topic_id,
            lrec_TaskIetm.print_bool
         );
      END IF;
   END LOOP;

   /* Add zones and panels but only if the task definition is a REQ, REF or JIC */
   IF ( ls_TaskClassMode = 'REQ' OR ls_TaskClassMode = 'REF' OR ls_TaskClassMode = 'JIC' )
   THEN

      /* Add all of the Baseline Zones */
      FOR lrec_Zone IN lcur_Zone( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO sched_zone (
              sched_db_id,
              sched_id,
              sched_zone_id,
              zone_db_id,
              zone_id
               )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              SCHED_ZONE_ID.nextval,
              lrec_Zone.zone_db_id,
              lrec_Zone.zone_id);
      END LOOP;

      /* Add all of the Baseline Panels */
      FOR lrec_Panel IN lcur_Panel( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO sched_panel (
              sched_db_id,
              sched_id,
              sched_panel_id,
              panel_db_id,
              panel_id
               )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              SCHED_PANEL_ID.nextval,
              lrec_Panel.panel_db_id,
              lrec_Panel.panel_id);
      END LOOP;
   END IF;

   /* If the task definition is a JIC or a executable requirement */
   IF ( ls_TaskClassMode = 'JIC' OR (ls_TaskClassMode = 'REQ' AND lrec_TaskDefinition.workscope_bool = 1) )
   THEN

      /* Add all of the Baseline Labours for Tasks allowing Resource Summaries */
      IF ( lrec_TaskDefinition.resource_sum_bool = 0 )
      THEN
         FOR lrec_TaskLabour IN lcur_TaskLabour( an_TaskDbId, an_TaskId )
         LOOP
            FOR ln_ManPwrLoop IN 1 .. lrec_TaskLabour.man_pwr_ct
            LOOP
               -- Generate the new Labour PK and insert the new labour
               SELECT SCHED_LABOUR_SEQ.nextval INTO ln_LabourId FROM dual;
               INSERT INTO sched_labour (
                  labour_db_id, labour_id,
                  sched_db_id, sched_id,
                  labour_stage_db_id, labour_stage_cd,
                  labour_skill_db_id, labour_skill_cd,
                  work_perf_bool,
                  cert_bool,
                  insp_bool,
                  current_status_ord
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  ln_EventDbId, ln_EventId,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV'),
                  lrec_TaskLabour.labour_skill_db_id, lrec_TaskLabour.labour_skill_cd,
                  lrec_TaskLabour.work_perf_bool,
                  lrec_TaskLabour.cert_bool,
                  lrec_TaskLabour.insp_bool,
                  1
               );

               -- Add Role and Status for Tech
               SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
               INSERT INTO sched_labour_role (
                  labour_role_db_id, labour_role_id,
                  labour_db_id, labour_id,
                  labour_role_type_db_id, labour_role_type_cd,
                  sched_hr
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  0, 'TECH',
                  lrec_TaskLabour.work_perf_hr
               );

               SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
               INSERT INTO sched_labour_role_status (
                  status_db_id, status_id,
                  labour_role_db_id, labour_role_id,
                  status_ord,
                  labour_role_status_db_id, labour_role_status_cd
               )
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  1,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV')
               );

               -- If Certification required, Add Role and Status for Cert
               IF( lrec_TaskLabour.cert_bool = 1 ) THEN
                  SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                  INSERT INTO sched_labour_role (
                     labour_role_db_id, labour_role_id,
                     labour_db_id, labour_id,
                     labour_role_type_db_id, labour_role_type_cd,
                     sched_hr
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                     0, 'CERT',
                     lrec_TaskLabour.cert_hr
                  );
                  SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                  INSERT INTO sched_labour_role_status (
                     status_db_id, status_id,
                     labour_role_db_id, labour_role_id,
                     status_ord,
                     labour_role_status_db_id, labour_role_status_cd
                  )
                  VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    1,
                    0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING')
                  );

               END IF;

               -- If Inspection required, Add Role and Status for Insp
               IF ( lrec_TaskLabour.insp_bool = 1 ) THEN
                  SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                  INSERT INTO sched_labour_role (
                     labour_role_db_id, labour_role_id,
                     labour_db_id, labour_id,
                     labour_role_type_db_id, labour_role_type_cd,
                     sched_hr
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                     0, 'INSP',
                     lrec_TaskLabour.insp_hr
                  );

                  SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                  INSERT INTO sched_labour_role_status (
                     status_db_id, status_id,
                     labour_role_db_id, labour_role_id,
                     status_ord,
                     labour_role_status_db_id, labour_role_status_cd
                  )
                  VALUES (
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                     APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                     1,
                     0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING')
                  );
               END IF;

            END LOOP;
         END LOOP;

      END IF;

      /* Add all of the Baseline Tools */
      ln_ToolId := 1;
      FOR lrec_Tool IN lcur_Tool( an_TaskDbId, an_TaskId )
      LOOP
         -- Get the Part Baseline key for the insert
         SELECT
            eqp_part_baseline.part_no_db_id,
            eqp_part_baseline.part_no_id
         INTO
            ln_StdPartNoDbId,
            ln_StdPartNoId
         FROM
            eqp_part_baseline
         WHERE
            eqp_part_baseline.bom_part_db_id = lrec_Tool.bom_part_db_id  AND
            eqp_part_baseline.bom_part_id    = lrec_Tool.bom_part_id
            AND
            eqp_part_baseline.standard_bool  = 1;

         INSERT INTO evt_tool (
               event_db_id,
               event_id,
               tool_id,
               bom_part_db_id,
               bom_part_id,
               part_no_db_id,
               part_no_id,
               task_db_id,
               task_id,
               task_tool_id,
               sched_hr )
         VALUES (
               ln_EventDbId,
               ln_EventId,
               ln_ToolId,
               lrec_Tool.bom_part_db_id,
               lrec_Tool.bom_part_id,
               ln_StdPartNoDbId,
               ln_StdPartNoId,
               an_TaskDbId,
               an_TaskId,
               lrec_Tool.task_tool_id,
               lrec_Tool.sched_hr );

         ln_ToolId := ln_ToolId + 1; -- increment the tool_id
      END LOOP;

      /* Add all of the Baseline Measurements */
      FOR lrec_Measurement IN lcur_Measurement( an_TaskDbId, an_TaskId )
      LOOP
         INSERT INTO inv_parm_data (
               event_db_id,
               event_id,
               event_inv_id,
               data_type_db_id,
               data_type_id,
               data_ord,
               inv_no_db_id,
               inv_no_id)
            VALUES (
               ln_EventDbId,
               ln_EventId,
               1,
               lrec_Measurement.data_type_db_id,
               lrec_Measurement.data_type_id,
               lrec_Measurement.data_ord,
               an_InvNoDbId,
               an_InvNoId);
     END LOOP;

/* Add all the OC and Non OC Assembly Measurements.  */

     FOR lrec_Assembly_Inventory IN lcur_Assembly_Inventories(an_InvNoDbId,an_InvNoId)
     LOOP

   FOR lrec_Assembly_Measurement IN lcur_Assembly_Measurement( an_TaskDbId, an_TaskId )
   LOOP
        /* Check for APU or ENG Measurement */
        SELECT
       count(*) INTO ln_count
        FROM
           ref_data_type_assmbl_class
        WHERE
      ref_data_type_assmbl_class.assmbl_class_db_id = lrec_Assembly_Inventory.assmbl_class_db_id
      AND
      ref_data_type_assmbl_class.assmbl_class_cd = lrec_Assembly_Inventory.assmbl_class_cd
      AND
      ref_data_type_assmbl_class.data_type_db_id = lrec_Assembly_Measurement.data_type_db_id
      AND
      ref_data_type_assmbl_class.data_type_id = lrec_Assembly_Measurement.data_type_id;

        IF ln_count > 0 THEN
           SELECT
               COUNT(*) INTO ln_ExistsCount
           FROM
               inv_parm_data
           WHERE
               inv_parm_data.event_db_id = an_TaskDbId
               AND
               inv_parm_data.event_id = an_TaskId
               AND
               inv_parm_data.event_inv_id = 1
               AND
               inv_parm_data.data_type_db_id = lrec_Assembly_Measurement.data_type_db_id
               AND
               inv_parm_data.data_type_id = lrec_Assembly_Measurement.data_type_id
               AND
               inv_parm_data.inv_no_db_id = lrec_Assembly_Inventory.inv_no_db_id
               AND
               inv_parm_data.inv_no_id  =  lrec_Assembly_Inventory.inv_no_id;

              IF (ln_ExistsCount = 0) THEN

         SELECT
             mim_data_type.eng_unit_db_id, mim_data_type.eng_unit_cd
         INTO ln_EngUnitDbId, ls_EngUnitCD
         FROM
             mim_data_type
         WHERE
             data_type_db_id=lrec_Assembly_Measurement.data_type_db_id
             AND
             data_type_id = lrec_Assembly_Measurement.data_type_id;

         INSERT
         INTO inv_parm_data (
             event_db_id,
             event_id,
             event_inv_id,
             data_type_db_id,
             data_type_id,
             data_ord,
             inv_no_db_id,
             inv_no_id,
             rec_eng_unit_db_id,
             rec_eng_unit_cd)
         VALUES (
             ln_EventDbId,
             ln_EventId,
             1,
             lrec_Assembly_Measurement.data_type_db_id,
             lrec_Assembly_Measurement.data_type_id,
             lrec_Assembly_Measurement.data_ord,
             lrec_Assembly_Inventory.inv_no_db_id,
             lrec_Assembly_Inventory.inv_no_id,
             ln_EngUnitDbId,
             ls_EngUnitCD);
       END IF;
        END IF;
        END LOOP;
     END LOOP;


      /* Add all of the Baseline Part Requirements */
      GenSchedParts( ln_EventDbId, ln_EventId, on_Return );
      IF on_Return < 1 THEN
        RETURN;
      END IF;

      /* Add all of the Baseline Steps */
      FOR lrec_Step IN lcur_Step( an_TaskDbId, an_TaskId )
    LOOP
       -- get the next step id
       SELECT SCHED_STEP_ID.nextval INTO ln_StepId FROM dual;

       INSERT INTO sched_step (
        sched_db_id,
        sched_id,
        step_id,
        step_ord,
        step_ldesc,
        step_status_cd)
          VALUES (
        ln_EventDbId,
        ln_EventId,
        ln_StepId,
        lrec_Step.step_ord,
        lrec_Step.step_ldesc,
        'MXPENDING');
      END LOOP;

   END IF; /* End JIC/CORR task Specific logic */


   /* Queue up the Task for Zipping as part of Baseline Sync */
   IF ( ls_TaskClassMode = 'BLOCK' ) THEN
      BaselineSyncPkg.RequestZipForNewBlock( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
   ELSIF ( ls_TaskClassMode = 'REQ' ) THEN
      BaselineSyncPkg.RequestZipForNewReq( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
   END IF;

   on_SchedDbId     := ln_EventDbId;
   on_SchedId       := ln_EventId;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN xc_InvHasIncorrectPartNo THEN
      on_Return := icn_InvHasIncorrectPartNo;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00045','');
      RETURN;
   WHEN xc_TaskDefnNotActive THEN
      on_Return := icn_TaskDefnNotActive;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00047','');
      RETURN;
   WHEN xc_InvIsLocked THEN
      on_Return := icn_InvIsLocked;
      APPLICATION_OBJECT_PKG.SetMxiError('BUS-00464','');
      RETURN;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenSchedTask@@@'||SQLERRM);
      RETURN;

END GenOneSchedTask;


/********************************************************************************
*
* Procedure:    AddAssemblyMeasurements
* Arguments:
*        an_TaskDbId                   The Task for which measurement is added
*        an_TaskId                     " "
*        an_DataTypeDbId               The Measurement Being added to the Task
*        an_DataTypeId                 " "
*
* Return:
*        on_Return        (long) - Success/Failure of Adding Assembly Measurements
*
* Description:
*            To Add Assembly Measurement to a Given Task
*
* Orig.Coder:     Balaji Rajasekaran
* Recent Coder:   Balaji Rajasekaran
*
*********************************************************************************
*
* Copyright 1997-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE AddAssemblyMeasurements(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           an_DataTypeDbId       IN mim_data_type.data_type_db_id%TYPE,
           an_DataTypeId         IN mim_data_type.data_type_id%TYPE,
           an_RecParmQty         IN inv_parm_data.rec_parm_qt% TYPE,
           on_Return             OUT typn_RetCode)
   IS
   /* *** DECLARE LOCAL VARIABLES *** */

   ln_InventoryDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InventoryId                 inv_inv.inv_no_id%TYPE;
   ln_EngUnitDbId                 mim_data_type.eng_unit_db_id%TYPE;
   ls_EngUnitCD                   mim_data_type.eng_unit_cd%TYPE;
   ln_ExistsCount         NUMBER;
   ln_InvCount                    NUMBER;
   ln_count                       NUMBER;
   ln_OrdCount                    NUMBER;


   /* Cursor for getting the assemblies and sub assemblies from an inventory */
   CURSOR lcur_Assembly_Inventories(
          an_InventoryDbId inv_inv.inv_no_db_id%TYPE,
            an_InventoryId inv_inv.inv_no_id%TYPE
   ) IS
   SELECT
     inv_inv.orig_assmbl_db_id,
     inv_inv.orig_assmbl_cd,
     eqp_assmbl.assmbl_class_db_id,
     eqp_assmbl.assmbl_class_cd,
     inv_inv.inv_no_db_id,
     inv_inv.inv_no_id
   FROM
     eqp_assmbl
     INNER JOIN inv_inv ON
        inv_inv.orig_assmbl_db_id = eqp_assmbl.assmbl_db_Id AND
        inv_inv.orig_assmbl_cd = eqp_assmbl.assmbl_cd
   WHERE
     (inv_inv.inv_no_db_id,inv_inv.inv_no_id)
     IN (
        SELECT
          inv_inv.inv_no_db_id,
          inv_inv.inv_no_id
        FROM
          inv_inv
        WHERE
          inv_inv.inv_class_cd = 'ASSY'
        START WITH
          inv_inv.inv_no_db_id = an_InventoryDbId AND
          inv_inv.inv_no_id = an_InventoryId
        CONNECT BY
          inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
          inv_inv.nh_inv_no_id  = PRIOR inv_inv.inv_no_id);

BEGIN
     on_Return := icn_NoProc;

     /**Get the Eng Unit details from MIM_DATA_TYPE Table*/
     SELECT
         mim_data_type.eng_unit_db_id, mim_data_type.eng_unit_cd
     INTO
         ln_EngUnitDbId, ls_EngUnitCD
     FROM
         mim_data_type
     WHERE
         data_type_db_id=an_DataTypeDbId AND
         data_type_id = an_DataTypeId;

     /**Compute the Data Ordering*/
     SELECT
         count(*)+1   INTO ln_OrdCount
     FROM
         inv_parm_data
     WHERE
         inv_parm_data.event_db_id = an_TaskDbId AND
         inv_parm_data.event_id = an_TaskId;

      /** Get the inventory details*/
     SELECT
         sched_stask.main_inv_no_db_id,
         sched_stask.main_inv_no_id
     INTO
         ln_InventoryDbId,
         ln_InventoryId
     FROM
         sched_stask
     WHERE
         sched_stask.sched_db_id = an_TaskDbId AND
         sched_stask.sched_id = an_TaskId;
             
     IF ln_InventoryDbId IS NOT NULL AND ln_InventoryId IS NOT NULL THEN
        FOR lrec_Assembly_Inventory IN lcur_Assembly_Inventories(ln_InventoryDbId,ln_InventoryId) LOOP
        
        /* Check for APU or ENG Measurement */
          SELECT
                count(*) INTO ln_count
          FROM
              ref_data_type_assmbl_class
          WHERE
              ref_data_type_assmbl_class.assmbl_class_db_id = lrec_Assembly_Inventory.assmbl_class_db_id AND
              ref_data_type_assmbl_class.assmbl_class_cd = lrec_Assembly_Inventory.assmbl_class_cd AND
              ref_data_type_assmbl_class.data_type_db_id = an_DataTypeDbId AND
              ref_data_type_assmbl_class.data_type_id = an_DataTypeId;

          IF ln_count > 0 THEN
              SELECT
                  COUNT(*) INTO ln_ExistsCount
              FROM
                  inv_parm_data
              WHERE
                inv_parm_data.event_db_id = an_TaskDbId AND
                inv_parm_data.event_id = an_TaskId AND
                inv_parm_data.event_inv_id = 1 AND
                inv_parm_data.data_type_db_id = an_DataTypeDbId AND
                inv_parm_data.data_type_id = an_DataTypeId AND
                inv_parm_data.inv_no_db_id = lrec_Assembly_Inventory.inv_no_db_id AND
                inv_parm_data.inv_no_id  =  lrec_Assembly_Inventory.inv_no_id;
    
              IF (ln_ExistsCount = 0) THEN
                INSERT
                INTO inv_parm_data (
                    event_db_id,
                    event_id,
                    event_inv_id,
                    data_type_db_id,
                    data_type_id,
                    data_ord,
                    inv_no_db_id,
                    inv_no_id,
                    rec_eng_unit_db_id,
                    rec_eng_unit_cd,
                    rec_parm_qt,
                    parm_qt)
                 VALUES (
                    an_TaskDbId,
                    an_TaskId,
                    1,
                    an_DataTypeDbId,
                    an_DataTypeId,
                    ln_OrdCount,
                    lrec_Assembly_Inventory.inv_no_db_id,
                    lrec_Assembly_Inventory.inv_no_id,
                    ln_EngUnitDbId,
                    ls_EngUnitCD,
                    an_RecParmQty,
                    an_RecParmQty);
              END IF;
          END IF;
        END LOOP;
     END IF;
EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AddAssemblyMeasurements@@@'||SQLERRM);
      RETURN;

END AddAssemblyMeasurements;

/********************************************************************************
*
* Procedure:    CheckForNonHistMOD
* Arguments:    an_InvNoDbId  (long) - inventory database id
*               an_InvNoId    (long) - inventory id
* Return:       on_TaskExists (long) - task exists
*               on_Return     (long) - succss/failure of procedure
*
* Description:  Check that a non-historical MOD task does not exist for
*               this inventory serial no.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: Andrew Hircock
* Recent Date:  Nov 18, 1999
*
*********************************************************************************
*
* Copyright ? 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckForNonHistMOD(
      an_InvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId     IN inv_inv.inv_no_id%TYPE,
      on_TaskExists  OUT typn_RetCode,
      on_Return      OUT typn_RetCode
   ) IS

   /*declare local variable */
   ln_Count      NUMBER;

BEGIN
   /* default to task does exist */
   on_TaskExists := icn_True;

   /* check for non historical rmvl tasks */
   SELECT COUNT(*)
      INTO ln_Count
      FROM
         evt_inv
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
         INNER JOIN sched_stask ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
      WHERE
         evt_inv.inv_no_db_id  = an_InvNoDbId AND
         evt_inv.inv_no_id     = an_InvNoId AND
         evt_inv.main_inv_bool = 1
         AND
         evt_event.hist_bool   = 0  AND
         evt_event.rstat_cd   = 0
         AND
         sched_stask.task_class_cd = 'MOD';

   /* no rows - task does not exists   */
   /* rows    - task exists            */
   IF ln_Count = 0 THEN
      on_TaskExists := icn_False;
   ELSE
      on_TaskExists := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckForNonHistMOD@@@'||SQLERRM);
     RETURN;
END CheckForNonHistMOD;


/********************************************************************************
*
* Procedure:    CheckModPartNum
* Arguments:    an_InvNoDbId           (long) - inventory database id
*               an_InvNoId             (long) - inventory id
*               an_TaskDbId            (long) - task definition database id
*               an_TaskId              (long) - task definition id
* Return:       on_MatchingPartNo      (long) - part existance
*               on_Return              (long) - succss/failure of procedure
*
* Description:  Check that the inventory's part number is correct
*
*
* Orig.Coder:  H. Strutt
* Recent Coder: H. Strutt
* Recent Date:  Aug.17,2002
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckModPartNum(
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      on_MatchingPartNo      OUT typn_RetCode,
      on_Return              OUT typn_RetCode
   ) IS

   /* declare local variable */
   ln_Count   NUMBER;     /* part transformation count */

BEGIN

   /* default to part does not exist */
   on_MatchingPartNo := ICN_FALSE;

   /* check that inventory part no contains a row in the */
   /* task_part_transformation table */
   SELECT COUNT(*)
     INTO ln_Count
     FROM task_part_transform,
          inv_inv
    WHERE inv_inv.inv_no_db_id = an_InvNoDbId
      AND inv_inv.inv_no_id    = an_InvNoId
      AND inv_inv.rstat_cd = 0
      AND task_part_transform.old_part_no_db_id = inv_inv.part_no_db_id
      AND task_part_transform.old_part_no_id    = inv_inv.part_no_id
      AND task_part_transform.task_db_id = an_TaskDbId
      AND task_part_transform.task_id    = an_TaskId;

   /* no rows - transformation does not exists   */
   /* rows    - transformation exists            */
   IF ln_Count = 0 THEN
      on_MatchingPartNo := icn_False;
   ELSE
      on_MatchingPartNo := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckModPartNum@@@'||SQLERRM);
     RETURN;
END CheckModPartNum;


/********************************************************************************
*
* Procedure:    CheckTaskPartNo
* Arguments:    an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:       on_TaskApplies (long) - task applies to the given BOM
*               on_Return      (long) - succss/failure of procedure
*
* Description:  Check that the given task definition applies to the inventory.
                Inventory match-ups are found by determining if the inventory's
                part no can fit into the task's bom item.
*
* Orig.Coder:  H. Strutt
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckTaskPartNo(
      an_InvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId     IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId    IN task_task.task_db_id%TYPE,
      an_TaskId      IN task_task.task_id%TYPE,
      on_TaskApplies OUT typn_RetCode,
      on_Return      OUT typn_RetCode
   ) IS

   /* declare local variables */
   ln_Count   NUMBER;  /* task existance count */
   ln_Count_Part NUMBER ;/* Count of Tasks for Part-Type*/

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* default to part does not exist */
   on_TaskApplies := ICN_FALSE;

   /* if the inventory is a system, then this check always succeeds */
   SELECT COUNT(*)
      INTO ln_Count
      FROM inv_inv
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId AND
         inv_inv.inv_class_cd = 'SYS'  AND
         inv_inv.rstat_cd  = 0;
   IF ln_Count = 1 THEN
      on_TaskApplies := icn_True;
      on_Return      := icn_Success;
      RETURN;
   END IF;

   /* check that the task applies, based on the inventory's part no */
   SELECT COUNT(*)
      INTO ln_Count
      FROM
         inv_inv,
         eqp_part_baseline,
         eqp_bom_part,
         ref_inv_class,
         task_task
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         eqp_part_baseline.part_no_db_id = inv_inv.part_no_db_id AND
         eqp_part_baseline.part_no_id    = inv_inv.part_no_id
         AND
         eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
         eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
         AND
         ref_inv_class.inv_class_db_id=eqp_bom_part.inv_class_db_id AND
         ref_inv_class.inv_class_cd=eqp_bom_part.inv_class_cd AND
         ref_inv_class.tracked_bool = 1
         AND
         task_task.task_db_id    = an_TaskDbId AND
         task_task.task_id       = an_TaskId
         AND
    (
       (
          task_task.assmbl_db_id  = eqp_bom_part.assmbl_db_id AND
          task_task.assmbl_cd     = eqp_bom_part.assmbl_cd AND
          task_task.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
       )
       OR
       (
          task_task.repl_assmbl_db_id  = eqp_bom_part.assmbl_db_id AND
          task_task.repl_assmbl_cd     = eqp_bom_part.assmbl_cd AND
          task_task.repl_assmbl_bom_id = eqp_bom_part.assmbl_bom_id
           )
         );

   /* check that inventory is based on one of the part numbers that is assigned to the part number task definition */
   SELECT COUNT(*)
      INTO ln_Count_Part
      FROM
         inv_inv,
         eqp_part_no,
         task_task,
         task_part_map
      WHERE
         inv_inv.inv_no_db_id = an_InvNoDbId AND
         inv_inv.inv_no_id    = an_InvNoId   AND
         inv_inv.rstat_cd  = 0
         AND
         eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
         eqp_part_no.part_no_id    = inv_inv.part_no_id
         AND
         task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
         task_part_map.part_no_id       = eqp_part_no.part_no_id
         AND
         task_task.task_db_id = task_part_map.task_db_id AND
         task_task.task_id    = task_part_map.task_id
         AND
         task_task.task_db_id    = an_TaskDbId AND
         task_task.task_id       = an_TaskId;

   /* return success/fail based on whether the query returned any rows */
   IF ln_Count = 0 AND ln_Count_Part = 0 THEN
      on_TaskApplies := icn_False;
   ELSE
      on_TaskApplies := icn_True;
   END IF;

   /* set return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CheckTaskPartNo@@@'||SQLERRM);
     RETURN;
END CheckTaskPartNo;


/********************************************************************************
*
* Procedure:    IsTaskApplicable
* Arguments:    an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:       ln_TaskApplies (long) - task applies to the given inventory
*
*
* Description:  Check the "task applicability" rules and determine if the given
*               task definition applies to the given inventory.
                This procedure will not validate the inputs (for performance
                reasons). Ensure that the given inv_id and task_id exist in
                the database
*
* Orig.Coder:   A. Hircock
* Recent Coder: Randy Abson
* Recent Date:  March 7, 2005
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsTaskApplicable (
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER IS
   ls_TaskApplSql    task_task.task_appl_sql_ldesc%TYPE; /* applicability clause */
   ln_TaskApplies    typn_RetCode;
   BEGIN

   /* if there is an applicability clause, then run the logic to determine
      if the given inventory is applicable */
   SELECT task_appl_sql_ldesc
     INTO ls_TaskApplSql
     FROM task_task
    WHERE task_db_id = an_TaskDbId
      AND task_id    = an_TaskId;
   IF ls_TaskApplSql IS NOT NULL THEN

      /* Attempt to execute a dynamic SQL statement. The dynamic SQL
         statement will use the applicability clause. It will then set
         on_TaskApplies to 1 if the task applies to the inventory, and 0
         otherwise. If an error occurs, then the general error exception
         will be called.  */

      /* Test the task applicability */
      SELECT
         getTaskApplicability(an_InvNoDbId, an_InvNoId, ls_TaskApplSql)
      INTO
         ln_TaskApplies
      FROM
         dual;


   /* if there is no applicability clause, then the task definition
      applies by default */
   ELSE
      RETURN icn_True;
   END IF;

   /* set return success */
   RETURN ln_TaskApplies;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@IsTaskApplicable@@@'||SQLERRM);
     RETURN icn_Error;
   END IsTaskApplicable;


/********************************************************************************
*
* Procedure:    VerifyApplicabilityRule
* Arguments:    as_TaskApplSql    (string) - the applicability rule
* Return:       icn_True          (long)   - if the applicability rule is valid
*               icn_False         (long)   - if the applicability rule is invalid
*
*
* Description:  Check if the "task applicability" rules is valid.
*
* Orig.Coder:   jcimino
* Recent Coder: ycho
* Recent Date:  2011-03-02
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION VerifyApplicabilityRule (
      as_TaskApplSql IN task_task.task_appl_sql_ldesc%TYPE
   ) RETURN NUMBER IS
   ln_CursorHandle   NUMBER;  /* handle to the dynamic SQL cursor */
   li_DbmsReturn     INTEGER; /* needed to run the DBMS functions */
   ls_SelectClause   VARCHAR2(32767); /* SQL clause */
   BEGIN

   /* if there is an applicability clause, then run the query to determine
      if the rule is valid applicable */
   IF as_TaskApplSql IS NOT NULL THEN

      /* Attempt to execute a dynamic SQL statement. The dynamic SQL
         statement will use the applicability clause. If the SQL runs without
         error, then the applicability rule is valid. If there is an error,
         then the applicability rule is not valid.  */

      /* Create the SQL select statement */
      ls_SelectClause :=
   ' SELECT' ||
   '   1 ' ||
   ' FROM' ||
   '   dual ' ||
   ' WHERE EXISTS ' ||
   ' (' ||
   '   SELECT' ||
   '      *' ||
   '   FROM' ||
   '      inv_inv,' ||
   '      inv_inv root,' ||
   '      inv_inv assembly,' ||
   '      eqp_part_no,' ||
   '      eqp_manufact,' ||
   '      inv_owner,' ||
   '      inv_owner ass_owner,' ||
   '      inv_ac_reg,' ||
   '      inv_inv ac_inv,' ||
   '      eqp_part_no rootpart,' ||
   '      eqp_part_no asspart,' ||
   '      org_carrier,' ||
   '      org_carrier ass_carrier' ||
   '   WHERE' ||
   '      inv_inv.inv_no_db_id IS NULL AND ' ||
   '      inv_inv.inv_no_id    IS NULL ' ||
   '      AND ' ||
   '      (' || as_TaskApplSql || ')' ||
   '      AND' ||
   '      root.inv_no_db_id = inv_inv.h_inv_no_db_id AND' ||
   '      root.inv_no_id    = inv_inv.h_inv_no_id    AND' ||
   '      root.rstat_cd  = 0' ||
   '      AND' ||
   '      org_carrier.carrier_db_id (+)= root.carrier_db_id AND' ||
   '      org_carrier.carrier_id    (+)= root.carrier_id' ||
   '      AND' ||
   '      eqp_part_no.part_no_db_id (+)= inv_inv.part_no_db_id AND' ||
   '      eqp_part_no.part_no_id    (+)= inv_inv.part_no_id' ||
   '      AND' ||
   '      eqp_manufact.manufact_db_id (+)= eqp_part_no.manufact_db_id AND' ||
   '      eqp_manufact.manufact_cd    (+)= eqp_part_no.manufact_cd' ||
   '      AND' ||
   '      inv_owner.owner_db_id (+)= inv_inv.owner_db_id AND' ||
   '      inv_owner.owner_id    (+)= inv_inv.owner_id' ||
   '      AND' ||
   '      inv_ac_reg.inv_no_db_id (+)= root.inv_no_db_id AND' ||
   '      inv_ac_reg.inv_no_id    (+)= root.inv_no_id' ||
   '      AND' ||
   '      ac_inv.inv_no_db_id (+)= inv_ac_reg.inv_no_db_id  AND' ||
   '      ac_inv.inv_no_id    (+)= inv_ac_reg.inv_no_id AND' ||
   '      ac_inv.inv_class_cd (+) = ''ACFT''' ||
   '      AND' ||
   '      rootpart.part_no_db_id (+)= root.part_no_db_id AND' ||
   '      rootpart.part_no_id    (+)= root.part_no_id' ||
   '      AND' ||
   '      assembly.inv_no_db_id =' ||
   '      DECODE (inv_inv.assmbl_inv_no_db_id, NULL, inv_inv.inv_no_db_id, DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id))' ||
   '      AND' ||
   '      assembly.inv_no_id =' ||
   '      DECODE (inv_inv.assmbl_inv_no_id, NULL, inv_inv.inv_no_id, DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id))' ||
   '      AND' ||
   '      ass_carrier.carrier_db_id (+)= assembly.carrier_db_id AND' ||
   '      ass_carrier.carrier_id    (+)= assembly.carrier_id' ||
   '      AND' ||
   '      ass_owner.owner_db_id (+)= assembly.owner_db_id AND' ||
   '      ass_owner.owner_id    (+)= assembly.owner_id' ||
   '      AND' ||
   '      asspart.part_no_db_id (+)= assembly.part_no_db_id AND' ||
   '      asspart.part_no_id    (+)= assembly.part_no_id' ||
   ' )';

      /* open dynamic cursor */
      ln_CursorHandle := DBMS_SQL.OPEN_CURSOR;

      /* parse and define variables for the SQL statement */
      DBMS_SQL.PARSE(ln_CursorHandle, ls_SelectClause, DBMS_SQL.V7);

      /* excute and fetch the dynamic SQL */
      li_DbmsReturn := DBMS_SQL.EXECUTE_AND_FETCH(ln_CursorHandle);

      /* close dynamic cursor */
      DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle);

   ELSE
      RETURN icn_True;
   END IF;

   /* set return success */
   RETURN icn_True;

EXCEPTION
   WHEN OTHERS THEN
    IF  DBMS_SQL.IS_OPEN(ln_CursorHandle) THEN DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle); END IF;
    APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@VerifyApplicabilityRule@@@'||SQLERRM);
    RETURN icn_False;
   END VerifyApplicabilityRule;



/********************************************************************************
*
* Procedure:    CountBaselineParents
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
* Return:
*               ln_Count       (long) - number of parent tasks
*
* Description:  Returns number of active parents for a baseline task.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Yui Sotozaki
* Recent Date:  December 28, 2007
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION CountBaselineParents (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE
   ) RETURN NUMBER IS
      ln_BlockCount NUMBER;
      ln_ReqCount NUMBER;

   BEGIN

      /* count number of baseline block parents */
      SELECT
         COUNT(*)
      INTO
         ln_BlockCount
      FROM
         task_task,
         task_block_req_map,
         task_task block_task_task
      WHERE
         task_task.task_db_id = an_TaskDbId  AND
         task_task.task_id    = an_TaskId
         AND
         task_block_req_map.req_task_defn_db_id = task_task.task_defn_db_id   AND
         task_block_req_map.req_task_defn_id    = task_task.task_defn_id
         AND
         block_task_task.task_db_id          = task_block_req_map.block_task_db_id  AND
         block_task_task.task_id             = task_block_req_map.block_task_id     AND
         block_task_task.task_def_status_cd  = 'ACTV';

      /* count number of baseline requirement parents */
      SELECT
         COUNT(*)
      INTO
         ln_ReqCount
      FROM
         task_task,
         task_jic_req_map,
         task_task req_task_task
      WHERE
         task_task.task_db_id = an_TaskDbId  AND
         task_task.task_id    = an_TaskId
         AND
         task_jic_req_map.jic_task_db_id  = task_task.task_db_id  AND
         task_jic_req_map.jic_task_id     = task_task.task_id
         AND
         req_task_task.task_defn_db_id    = task_jic_req_map.req_task_defn_db_id AND
         req_task_task.task_defn_id       = task_jic_req_map.req_task_defn_id    AND
         req_task_task.task_def_status_cd = 'ACTV';

      RETURN ( ln_BlockCount + ln_ReqCount );
   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CountBaselineParents@@@'||SQLERRM);
     RETURN icn_Error;
   END CountBaselineParents;


/********************************************************************************
*
* Procedure:   CountTaskInstances
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
*               an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
* Return:
*
*
* Description:  Count number of task active (non Forecast) instances on a specific inventory and
*               with specific task baseline dbid/id.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: N/A
* Recent Date:  July 8, 2004
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION CountTaskInstances (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE

   ) RETURN NUMBER IS
   ln_Count NUMBER;

   BEGIN

   /*count number of active task instances*/
   SELECT COUNT(*)
   INTO ln_Count
   FROM  sched_stask,
         evt_event,
         evt_inv
   WHERE
       sched_stask.task_db_id=an_TaskDbId AND
       sched_stask.task_id=an_TaskId   AND
       sched_stask.rstat_cd         = 0
       AND
       evt_event.event_db_id=sched_stask.sched_db_id AND
       evt_event.event_id=sched_stask.sched_id AND
       evt_event.hist_bool=0 AND
       NOT evt_event.event_status_cd='FORECAST'
       AND
       evt_inv.event_db_id=evt_event.event_db_id AND
       evt_inv.event_id=evt_event.event_id AND
       evt_inv.inv_no_db_id=an_InvNoDbId AND
       evt_inv.inv_no_id=an_InvNoId;

       RETURN ln_Count;
   EXCEPTION
   WHEN OTHERS THEN
   -- Unexpected error
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CountTaskInstances@@@'||SQLERRM);
   RETURN icn_Error;
   END CountTaskInstances;



/********************************************************************************
*
* Procedure:    FindLatestTaskInstance
* Arguments:
*               an_TaskDbId    (long) - task definition database id
*               an_TaskId      (long) - task definition id
*               an_InvNoDbId   (long) - inventory database id
*               an_InvNoId     (long) - inventory id
*
* Return:       an_TaskInstanceDbId (long) - task instance database id
*               an_TaskInstanceId (long) - task instance id
*               on_return (long) - 1 if error not found
*
*
* Description:  Find last instance of a task. Looks up latest task
*               based on a pecific task definition and associated with inventory.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindLatestTaskInstance (
      an_TaskDbId            IN task_task.task_db_id%TYPE,
      an_TaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId           IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId             IN inv_inv.inv_no_id%TYPE,
      an_TaskInstanceDbId    OUT sched_stask.sched_db_id%TYPE,
      an_TaskInstanceId      OUT sched_stask.sched_db_id%TYPE,
      on_Return              OUT typn_RetCode

   ) IS
  /* *** DECLARE CURSORS *** */
   /* cursor used to find latest active task */
   CURSOR lcur_FindPreviousTaskInstance (
         an_taskdbid  task_task.task_db_id%type,
         an_taskid    task_task.task_id%type,
         an_invnodbid inv_inv.inv_no_db_id%type,
         an_invnoid   inv_inv.inv_no_id%type
      ) IS
     SELECT evt_sched_dead.event_db_id,
            evt_sched_dead.event_id
     FROM sched_stask,
          evt_inv,
          evt_sched_dead,
          evt_event
     WHERE
          sched_stask.task_db_id       = an_taskdbid AND
          sched_stask.task_id          = an_taskid   AND
          sched_stask.orphan_frct_bool = 0           AND
          sched_stask.rstat_cd         = 0
          AND
          evt_event.event_db_id=sched_stask.sched_db_id AND
          evt_event.event_id=sched_stask.sched_id AND
         (evt_event.event_status_cd='ACTV' OR
          evt_event.event_status_cd='FORECAST' OR
          evt_event.event_status_cd='COMPLETE'
          )
          AND
          evt_inv.event_db_id=sched_stask.sched_db_id AND
          evt_inv.event_id=sched_stask.sched_id AND
          evt_inv.inv_no_db_id=an_invnodbid AND
          evt_inv.inv_no_id=an_invnoid AND
          evt_inv.main_inv_bool = 1
          AND
          evt_sched_dead.event_db_id=sched_stask.sched_db_id AND
          evt_sched_dead.event_id = sched_stask.sched_id AND
          evt_sched_dead.sched_driver_bool=1
          AND
          (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
          NOT IN(
          SELECT evt_event_rel.event_db_id,
                 evt_event_rel.event_id
          FROM evt_event_rel
          WHERE
          evt_event_rel.rel_type_cd = 'DEPT' )
          ORDER BY decode(evt_event.hist_bool, 1, evt_event.event_dt, evt_sched_dead.sched_dead_dt) desc;
          lrec_FindPreviousTaskInstance  lcur_FindPreviousTaskInstance%rowtype;

/* cursor used to find latest active, forecast or complete task based on older task revision. The task must be in INWORK or COMMIT check */
   CURSOR lcur_FindOlderPreviousTask(
         an_taskdbid  task_task.task_db_id%type,
         an_taskid    task_task.task_id%type,
         an_invnodbid inv_inv.inv_no_db_id%type,
         an_invnoid   inv_inv.inv_no_id%type
      ) IS
     SELECT task_event.event_db_id,
            task_event.event_id
     FROM
         task_task curent_task_task,
         task_task,
         inv_inv,
         sched_stask,
         evt_inv,
         evt_event task_event
     WHERE
         inv_inv.inv_no_db_id = an_invnodbid AND
         inv_inv.inv_no_id    = an_invnoid   AND
         inv_inv.rstat_cd  = 0
         AND
         evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id AND
         evt_inv.inv_no_id     = inv_inv.inv_no_id    AND
         evt_inv.main_inv_bool = 1
         AND
         task_event.event_db_id = evt_inv.event_db_id AND
         task_event.event_id    = evt_inv.event_id    AND
         task_event.event_status_cd IN ('ACTV','FORECAST','COMPLETE')
         AND
         sched_stask.sched_db_id = task_event.event_db_id AND
         sched_stask.sched_id    = task_event.event_id
         AND
         sched_stask.orphan_frct_bool = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id  AND
         task_task.task_id    = sched_stask.task_id
         AND
         curent_task_task.task_defn_db_id = task_task.task_defn_db_id AND
         curent_task_task.task_defn_id    = task_task.task_defn_id
         AND
         curent_task_task.task_db_id = an_taskdbid AND
         curent_task_task.task_id    = an_taskid  AND
         curent_task_task.recurring_task_bool = 1
         AND
         (sched_stask.sched_db_id, sched_stask.sched_id)
           NOT IN(
               SELECT evt_event_rel.event_db_id,
                 evt_event_rel.event_id
               FROM   evt_event_rel
               WHERE  evt_event_rel.rel_type_cd = 'DEPT' );
         lrec_FindOlderPreviousTask  lcur_FindOlderPreviousTask%rowtype;


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get the task definition info */
   OPEN  lcur_FindPreviousTaskInstance( an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId);
   FETCH lcur_FindPreviousTaskInstance INTO lrec_FindPreviousTaskInstance;
   CLOSE lcur_FindPreviousTaskInstance;

   /* if previous task not found, set the return values to -1*/
   IF (lrec_FindPreviousTaskInstance.event_db_id is null) THEN
      /* try to find an existing active task based on an older revision */
      OPEN  lcur_FindOlderPreviousTask( an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId);
      FETCH lcur_FindOlderPreviousTask INTO lrec_FindOlderPreviousTask;
      CLOSE lcur_FindOlderPreviousTask;

      IF (lrec_FindOlderPreviousTask.event_db_id is null) THEN
         an_TaskInstanceDbId:=-1;
         an_TaskInstanceId:=-1;
      ELSE
         an_TaskInstanceDbId := lrec_FindOlderPreviousTask.event_db_id;
         an_TaskInstanceId   := lrec_FindOlderPreviousTask.event_id;
      END IF;
   ELSE
      /* if previous values found return them*/
      an_TaskInstanceDbId := lrec_FindPreviousTaskInstance.event_db_id;
      an_TaskInstanceId   := lrec_FindPreviousTaskInstance.event_id;
   END IF;

   /*if this line is reached, return success*/
   on_Return := icn_Success;

EXCEPTION
WHEN OTHERS THEN
   -- Unexpected error
  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindLatestTaskInstance@@@'||SQLERRM);
  on_Return:= icn_Error;
END FindLatestTaskInstance;

/********************************************************************************
*
* Procedure:    CreateTaskDependencyLink
* Arguments:
*               an_PreviousSchedDbId    (long) - previous task database id
*               an_PreviousSchedId      (long) - previous task id
*               an_SchedDbId    (long) - stask database id
*               an_SchedId      (long) - stask definition id
* Return:       on_Return       (long) - 1 if no error found
*
*
* Description:  Creates DEPT link between two task instances.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CreateTaskDependencyLink (
       an_PreviousSchedDbId         IN sched_stask.sched_db_id%TYPE,
       an_PreviousSchedId           IN sched_stask.sched_id%TYPE,
       an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
       an_SchedId              IN sched_stask.sched_id%TYPE,
       on_Return               OUT typn_RetCode

   ) IS

   ln_NextEventRelId NUMBER;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get the next rel_id for the previous task */
   SELECT
         max( event_rel_id ) + 1
     INTO
         ln_NextEventRelId
     FROM
         evt_event_rel
    WHERE
         event_db_id = an_PreviousSchedDbId AND
         event_id    = an_PreviousSchedId;
   IF ln_NextEventRelId IS NULL THEN
      ln_NextEventRelId := 1;
   END IF;

   /* insert the relationship */
   INSERT INTO evt_event_rel (
         event_db_id,
         event_id,
         event_rel_id,
         rel_event_db_id,
         rel_event_id,
         rel_type_db_id,
         rel_type_cd,
         rel_event_ord )
   VALUES (
         an_PreviousSchedDbId,
         an_PreviousSchedId,
         ln_NextEventRelId,
         an_SchedDbId,
         an_SchedId,
         0,
         'DEPT',
         1 );

   /* link the deadlines if any */
   UPDATE evt_sched_dead t
   SET    t.sched_from_cd = 'LASTDUE'
   WHERE  t.event_db_id = an_SchedDbId AND
          t.event_id    = an_SchedId   AND
          t.sched_from_cd <> 'LASTDUE'
          -- where the two tasks share the same datatype, and the previous task is ACTV
          AND EXISTS
          (SELECT 1
           FROM   evt_sched_dead prev_sched_dead,
                  evt_event      prev_event
           WHERE  prev_event.event_db_id   = an_PreviousSchedDbId AND
                  prev_event.event_id      = an_PreviousSchedId   AND
                  prev_event.hist_bool     = 0
                  AND
                  prev_sched_dead.event_db_id     = prev_event.event_db_id AND
                  prev_sched_dead.event_id        = prev_event.event_id    AND
                  prev_sched_dead.data_type_db_id = t.data_type_db_id      AND
                  prev_sched_dead.data_type_id    = t.data_type_id);

   prep_deadline_pkg.UpdateDependentDeadlines( an_PreviousSchedDbId,
                                               an_PreviousSchedId,
                                               on_Return);

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@CreateTaskDependencyLink@@@'||SQLERRM);
     on_Return:= icn_Error;

   END CreateTaskDependencyLink;
/********************************************************************************
*
* Procedure:    FindRemovedInventory
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. It should only be used to
                search for TRK/ASSY inventories
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindRemovedInventory (
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      an_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
      an_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** LOCAL VARIABLES *** */
   ln_OnAssmbl NUMBER;

   /* *** CURSOR DECLARATIONS *** */
   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindOnAssmbl (
         cn_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
         cn_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
         cn_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv start_inv,
         inv_inv find_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId   AND
         start_inv.rstat_cd   = 0
         AND
         (
            ( find_inv.assmbl_inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
              find_inv.assmbl_inv_no_id    = start_inv.assmbl_inv_no_id )
            OR
            ( find_inv.assmbl_inv_no_db_id = start_inv.inv_no_db_id AND
              find_inv.assmbl_inv_no_id    = start_inv.inv_no_id )
            OR
            ( find_inv.inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
              find_inv.inv_no_id    = start_inv.assmbl_inv_no_id )
         )
         AND
         (
            ( find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
              find_inv.assmbl_cd     = cs_FindAssmblCd AND
              find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
              find_inv.assmbl_bom_id = cn_FindAssmblBomId AND
              find_inv.bom_part_db_id = cn_FindBomPartDbId AND
              find_inv.bom_part_id    = cn_FindBomPartId    AND
              find_inv.rstat_cd  = 0)
            OR
            ( cn_FindAssmblBomId = 0 AND
              find_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
              find_inv.orig_assmbl_cd    = cs_FindAssmblCd    AND
              find_inv.rstat_cd  = 0 )
         );
   lrec_FindOnAssmbl lcur_FindOnAssmbl%ROWTYPE;

   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindLoose (
         cn_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
         cn_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
         cn_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv start_inv,
         inv_inv find_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId    AND
         start_inv.rstat_cd   = 0
         AND
         find_inv.bom_part_db_id = cn_FindBomPartDbId AND
         find_inv.bom_part_id    = cn_FindBomPartId    AND
         find_inv.rstat_cd = 0
         AND
         find_inv.h_inv_no_db_id = start_inv.h_inv_no_db_id AND
         find_inv.h_inv_no_id    = start_inv.h_inv_no_id
         AND
         find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
         find_inv.assmbl_cd     = cs_FindAssmblCd AND
         find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
         find_inv.assmbl_bom_id = cn_FindAssmblBomId;

   lrec_FindLoose lcur_FindLoose%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* determine if the starting inventory is on an assembly */
   SELECT DECODE( assmbl_inv_no_db_id, NULL, 0, 1 )
      INTO ln_OnAssmbl
      FROM inv_inv
      WHERE
         inv_no_db_id = an_StartInvNoDbId AND
         inv_no_id    = an_StartInvNoId     AND
         inv_inv.rstat_cd  = 0;

   /* if the start_inv is on an assembly, then look for the matching component
      in that assembly */
   IF ln_OnAssmbl = 1 THEN
      OPEN lcur_FindOnAssmbl(
               an_StartInvNoDbId,
               an_StartInvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId,
               an_FindBomPartDbId,
               an_FindBomPartId );
      FETCH lcur_FindOnAssmbl INTO lrec_FindOnAssmbl;
      IF lcur_FindOnAssmbl%FOUND THEN
         on_InvNoDbId := lrec_FindOnAssmbl.inv_no_db_id;
         on_InvNoId   := lrec_FindOnAssmbl.inv_no_id;
      ELSE
         on_InvNoDbId := null;
         on_InvNoId   := null;
      END IF;
      CLOSE lcur_FindOnAssmbl;
   /* if the start_inv is on a loose component, then look for the matching
      component in the entire inventory tree. */
   ELSE
      OPEN lcur_FindLoose(
               an_StartInvNoDbId,
               an_StartInvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId,
               an_FindBomPartDbId,
               an_FindBomPartId );
      FETCH lcur_FindLoose INTO lrec_FindLoose;
      IF lcur_FindLoose%FOUND THEN
         on_InvNoDbId := lrec_FindLoose.inv_no_db_id;
         on_InvNoId   := lrec_FindLoose.inv_no_id;
      ELSE
         on_InvNoDbId := null;
         on_InvNoId   := null;
      END IF;
      CLOSE lcur_FindLoose;
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindRemovedInventory@@@'||SQLERRM);
     RETURN;
END FindRemovedInventory;



/********************************************************************************
*
* Procedure:    FindRemovedInventory
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. It should only be used to
                search for TRK/ASSY inventories
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindStartInventory (
      an_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* find the inventory that is currently installed on an
      assembly */
   CURSOR lcur_FindOnAssmbl (
         cn_InvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId      IN inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cs_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
         cn_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE
      ) IS
      /* find tracked inventory */
      SELECT
         find_inv.inv_no_db_id,
         find_inv.inv_no_id
      FROM
         inv_inv highest_inv,
         inv_inv find_inv
      WHERE
         highest_inv.inv_no_db_id=cn_InvNoDbId AND
         highest_inv.inv_no_id=cn_InvNoId      AND
         highest_inv.rstat_cd = 0
         AND
         find_inv.h_inv_no_db_id = highest_inv.h_inv_no_db_id AND
         find_inv.h_inv_no_id    = highest_inv.h_inv_no_id    AND
         find_inv.rstat_cd = 0
         AND
          find_inv.assmbl_db_id  = cn_FindAssmblDbId AND
          find_inv.assmbl_cd     = cs_FindAssmblCd AND
          find_inv.assmbl_pos_id = cn_FindAssmblPosId AND
          find_inv.assmbl_bom_id = cn_FindAssmblBomId;
   lrec_FindOnAssmbl lcur_FindOnAssmbl%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

      OPEN lcur_FindOnAssmbl(
               an_InvNoDbId,
               an_InvNoId,
               an_FindAssmblDbId,
               as_FindAssmblCd,
               an_FindAssmblBomId,
               an_FindAssmblPosId);
      FETCH lcur_FindOnAssmbl INTO lrec_FindOnAssmbl;
      IF lcur_FindOnAssmbl%FOUND THEN
         on_InvNoDbId := lrec_FindOnAssmbl.inv_no_db_id;
         on_InvNoId   := lrec_FindOnAssmbl.inv_no_id;
      ELSE
         on_InvNoDbId := an_InvNoDbId;
         on_InvNoId   := an_InvNoId;
      END IF;
      CLOSE lcur_FindOnAssmbl;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindStartInventory@@@'||SQLERRM);
     RETURN;
END FindStartInventory;

/********************************************************************************
*
* Procedure:    AddRmvdPartRequirement
* Arguments:
*
*      an_SchedDbId          (long) -- scheduled part requirement db id
*      an_SchedId            (long) -- scheduled part requirement id
*      an_SchedPartId        (long) -- scheduled part requirement part id
*      an_RemovedReasonDbid  (long) -- removed reason database id
*      as_RemovedReasonCd    (char) -- removed reasin code
*
* Return:      on_Return     (long) - 1 if no error found
*
*
* Description: This procedure generates rows in sched_rmvd_part table based on values
*              in sched_part table.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Jonathan Clarkin
* Recent Date:  September 30, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AddRmvdPartRequirement (
       an_InvDbId            IN inv_inv.inv_no_db_id%TYPE,
       an_InvId              IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId          IN sched_stask.sched_db_id%TYPE,
       an_SchedId            IN sched_stask.sched_id%TYPE,
       an_SchedPartId        IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
       an_SchedRmvdPartId    OUT sched_rmvd_part.sched_rmvd_part_id%TYPE,
       on_Return             OUT typn_RetCode

   ) IS

   /* quantity number */
   ln_QtNumber NUMBER;

   /* removed part id (index) */
   ln_SchedRmvdPartId NUMBER;

   /* removed inventory if one exists in a hole */
   ln_RmvdInvNoDbId  sched_rmvd_part.inv_no_db_id%TYPE;
   ln_RmvdInvNoId    sched_rmvd_part.inv_no_id%TYPE;

  /* removed inventory if one exists in a hole */
   ln_StartInvNoDbId  inv_inv.inv_no_db_id%TYPE;
   ln_StartInvNoId    inv_inv.inv_no_id%TYPE;

   /* removed part no */
   ln_RmvdPartNoDbId sched_rmvd_part.part_no_db_id%TYPE;
   ln_RmvdPartNoId   sched_rmvd_part.part_no_id%TYPE;

   /* removed serial no */
   ls_RmvdSerialNo   sched_rmvd_part.serial_no_oem%TYPE;

   /* removed reason */
   ln_RemovedReasonDbid task_part_list.remove_reason_db_id%TYPE;
   ls_RemovedReasonCd   task_part_list.remove_reason_cd%TYPE;

   /* used to get part requirements details */
   CURSOR lcur_SchedPart (
         cn_SchedDbId   sched_part.sched_db_id%TYPE,
         cn_SchedId     sched_part.sched_id%TYPE,
         cn_SchedPartId sched_part.sched_part_id%TYPE
      ) IS
      SELECT
         sched_part.sched_bom_part_db_id,
         sched_part.sched_bom_part_id,
         sched_part.assmbl_db_id,
         sched_part.assmbl_cd,
         sched_part.assmbl_bom_id,
         sched_part.assmbl_pos_id,
         sched_part.nh_assmbl_db_id,
         sched_part.nh_assmbl_cd,
         sched_part.nh_assmbl_bom_id,
         sched_part.nh_assmbl_pos_id,
         sched_part.spec_part_no_db_id,
         sched_part.spec_part_no_id,
         sched_part.sched_qt,
         eqp_bom_part.inv_class_cd
      FROM
         sched_part,
         eqp_bom_part
      WHERE
         sched_part.sched_db_id   = cn_SchedDbId AND
         sched_part.sched_id      = cn_SchedId AND
         sched_part.sched_part_id = cn_SchedPartId AND
         sched_part.rstat_cd  = 0
         AND
         eqp_bom_part.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_bom_part.bom_part_id    = sched_part.sched_bom_part_id;

         lrec_SchedPart lcur_SchedPart%ROWTYPE;

   /* get standard part for bom part */
   CURSOR lcur_StandardPart (
         cn_BomPartDbId sched_part.sched_bom_part_db_id %TYPE,
         cn_BomPartId   sched_part.sched_bom_part_id%TYPE
      ) IS
       SELECT
         eqp_part_baseline.part_no_db_id,
         eqp_part_baseline.part_no_id
       FROM
         eqp_part_baseline
       WHERE
         eqp_part_baseline.bom_part_db_id=cn_BomPartDbId AND
         eqp_part_baseline.bom_part_id=cn_BomPartId AND
         eqp_part_baseline.standard_bool=1;

         lrec_StandardPart lcur_StandardPart%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*  get list of part requirements */
   OPEN  lcur_SchedPart( an_SchedDbId, an_SchedId, an_SchedPartId );
   FETCH lcur_SchedPart INTO lrec_SchedPart;
   CLOSE lcur_SchedPart;

   /*  get standard part */
   OPEN  lcur_StandardPart( lrec_SchedPart.sched_bom_part_db_id, lrec_SchedPart.sched_bom_part_id ) ;
   FETCH lcur_StandardPart INTO lrec_StandardPart;
   CLOSE lcur_StandardPart;

   /* get next index for the removed part requirement */
   SELECT decode( max( sched_rmvd_part_id ), null, 0, max( sched_rmvd_part_id ) )
   INTO ln_SchedRmvdPartId
   FROM sched_rmvd_part
   WHERE sched_db_id   = an_SchedDbId and
         sched_id      = an_SchedId and
         sched_part_id = an_SchedPartId   AND
         rstat_cd = 0;

   /* default the serial number to XXX*/
   ls_RmvdSerialNo := 'XXX';
   /* default the qt to 1 */
   ln_QtNumber := 1;

   ln_RemovedReasonDbid := an_RemovedReasonDbid;
   ls_RemovedReasonCd   := as_RemovedReasonCd;

   /* default the part no to the sched part part no */
   ln_RmvdPartNoDbId := lrec_SchedPart.spec_part_no_db_id;
   ln_RmvdPartNoId   := lrec_SchedPart.spec_part_no_id;

   /* if the Part to be Removed is null, set it to the standard part no */
   IF( ln_RmvdPartNoDbId IS NULL ) THEN
      ln_RmvdPartNoDbId := lrec_StandardPart.part_no_db_id;
      ln_RmvdPartNoId   := lrec_StandardPart.part_no_id;
   END IF;

   /* if the bom part is BATCH */
   IF( lrec_SchedPart.inv_class_cd='BATCH' ) THEN
      /* qt is the sched part qt */
      ln_QtNumber := lrec_SchedPart.sched_qt; -- initialize default quantity to the part requirement qt

      /* serial number is null */
      ls_RmvdSerialNo := null;
   END IF;


   /* if bom part is ASSY or TRK */
   IF
      lrec_SchedPart.inv_class_cd = 'ASSY' OR
      lrec_SchedPart.inv_class_cd = 'TRK' THEN

      ln_RmvdPartNoDbId := NULL;
      ln_RmvdPartNoId   := NULL;
      ls_RmvdSerialNo   := '';

     FindStartInventory (
            an_InvDbId,
            an_InvId,
            lrec_SchedPart.nh_assmbl_db_id,
            lrec_SchedPart.nh_assmbl_cd,
            lrec_SchedPart.nh_assmbl_bom_id,
            lrec_SchedPart.nh_assmbl_pos_id,
            ln_StartInvNoDbId,
            ln_StartInvNoId,
            on_Return
      );



      /*look up an inventory for the part requirement, at given position and with given bom part*/
      FindRemovedInventory (
            ln_StartInvNoDbId,
            ln_StartInvNoId,
            lrec_SchedPart.assmbl_db_id,
            lrec_SchedPart.assmbl_cd,
            lrec_SchedPart.assmbl_bom_id,
            lrec_SchedPart.assmbl_pos_id,
            lrec_SchedPart.sched_bom_part_db_id,
            lrec_SchedPart.sched_bom_part_id,
            ln_RmvdInvNoDbId,
            ln_RmvdInvNoId,
            on_Return
      );

      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* if matching inventory was found, then initialize the other inventory details */
   IF ln_RmvdInvNoDbId IS NOT NULL THEN

      /* fill in the PART_NO and SERIAL_NO */
      SELECT
         inv_inv.part_no_db_id,
         inv_inv.part_no_id,
         inv_inv.serial_no_oem
      INTO
         ln_RmvdPartNoDbId,
         ln_RmvdPartNoId,
         ls_RmvdSerialNo
      FROM
         inv_inv
      WHERE
         inv_inv.inv_no_db_id = ln_RmvdInvNoDbId AND
         inv_inv.inv_no_id    = ln_RmvdInvNoId  AND
         inv_inv.rstat_cd  = 0;
   END IF;


   /* insert new part requirement */
   INSERT INTO sched_rmvd_part (
      sched_db_id,
      sched_id,
      sched_part_id,
      sched_rmvd_part_id,
      part_no_db_id,
      part_no_id,
      serial_no_oem,
      inv_no_db_id,
      inv_no_id,
      rmvd_qt,
      remove_reason_db_id,
      remove_reason_cd
   )
   VALUES (
      an_SchedDbId,
      an_SchedId,
      an_SchedPartId,
      ln_SchedRmvdPartId+1,
      ln_RmvdPartNoDbId,
      ln_RmvdPartNoId,
      ls_RmvdSerialNo,
      ln_RmvdInvNoDbId,
      ln_RmvdInvNoId,
      ln_QtNumber,
      ln_RemovedReasonDbid,
      ls_RemovedReasonCd
   );

   an_SchedRmvdPartId := ln_SchedRmvdPartId+1;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
     on_Return := icn_Error;
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AddRmvdPartRequirement@@@'||SQLERRM);

END AddRmvdPartRequirement;

/********************************************************************************
*
* Procedure:    GenerateRemoveInstallPartReq
* Arguments:
*
*      an_InvDbId            (long) -- main inventory database id
*      an_InvId              (long) -- main inventory dbid
*      an_SchedDbId          (long) -- scheduled part requirement dbid
*      an_SchedId            (long) -- scheduled part requirement id
*      an_SchedPartId        (long) -- shceduled part requirement part id
*      an_RemovedReasonDbid  (long) -- removed reason database id
*      as_RemovedReasonCd    (char) -- removed reasin code
*
* Return:      on_Return     (long) - 1 if no error found
*
*
* Description: This procedure generates rows in sched_inst_part (install_bool=true),
*              sched_rmvd_part (remove_bool=true) tables based on values in sched_part table.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: Jonathan Clarkin
* Recent Date:  September 30, 2005
*
*********************************************************************************
*
* Copyright ? 2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenerateRemoveInstallPartReq (
       an_InvDbId             IN inv_inv.inv_no_db_id%TYPE,
       an_InvId               IN inv_inv.inv_no_id%TYPE,
       an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
       an_SchedId             IN sched_stask.sched_id%TYPE,
       an_SchedPartId         IN sched_part.sched_part_id%TYPE,
       an_RemovedReasonDbid   IN task_part_list.remove_reason_db_id%TYPE,
       as_RemovedReasonCd     IN task_part_list.remove_reason_cd%TYPE,
       ab_Remove_bool         IN task_part_list.remove_bool%TYPE,
       ab_Install_bool        IN task_part_list.install_bool%TYPE,
       on_Return              OUT typn_RetCode

   ) IS

   /* local variables */
   ln_RmvdIndex NUMBER;    -- index for the removed inventory
   ln_InstIndex NUMBER;    -- index for the installed inventory
   ln_NumOfInserts NUMBER; -- number of expected inserts to sched_inst_part/sched_rmvd_part table
   ln_QtNumber NUMBER;     -- quantity number
   ln_count NUMBER;        -- count

   /* used to get part requirements details */
   CURSOR lcur_SchedPart (
         cn_SchedDbId   sched_part.sched_db_id%TYPE,
         cn_SchedId     sched_part.sched_id%TYPE,
         cn_SchedPartId sched_part.sched_part_id%TYPE
      ) IS
      SELECT
         sched_part.sched_bom_part_db_id,
         sched_part.sched_bom_part_id,
         sched_part.sched_qt,
         sched_part.spec_part_no_db_id,
         sched_part.spec_part_no_id,
         eqp_bom_part.inv_class_cd
      FROM
         sched_part,
         eqp_bom_part
      WHERE
         sched_part.sched_db_id   = cn_SchedDbId AND
         sched_part.sched_id      = cn_SchedId AND
         sched_part.sched_part_id = cn_SchedPartId AND
         sched_part.rstat_cd  = 0
         AND
         eqp_bom_part.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_bom_part.bom_part_id    = sched_part.sched_bom_part_id;

         lrec_SchedPart lcur_SchedPart%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*  get list of part requirements */
   OPEN  lcur_SchedPart( an_SchedDbId, an_SchedId, an_SchedPartId );
   FETCH lcur_SchedPart INTO lrec_SchedPart;
   CLOSE lcur_SchedPart;


   /* initialize variables */
   ln_NumOfInserts := 1; -- default number of inserts
   ln_InstIndex    := 1; -- starting inst part id
   ln_RmvdIndex    := 1; -- starting rmvd part id

   ln_QtNumber := lrec_SchedPart.sched_qt; -- initialize default quantity to the part requirement qt

   /*if bom part is of class SER, we will insert sched_qt number of rows, each of them contaning one qt*/
   IF ( lrec_SchedPart.inv_class_cd = 'SER' ) THEN
     ln_NumOfInserts := lrec_SchedPart.sched_qt;
     ln_QtNumber     := 1;
   END IF;


   ln_count := 1;

   /* insert rows into the tables */
   FOR ln_count IN 1..ln_NumOfInserts
   LOOP

     /*if remove part requirement */
     IF ( ab_Remove_bool = 1 ) THEN

         /* add new remove part requirement, this will set serial no, part no,
          * removed reason to default value if necessary, or to the actual removed inventory value */
         AddRmvdPartRequirement (
               an_InvDbId ,
               an_InvId,
               an_SchedDbId,
               an_SchedId,
               an_SchedPartId,
               an_RemovedReasonDbid,
               as_RemovedReasonCd,
               ln_RmvdIndex,
               on_Return
         );

         IF on_Return < 0 THEN
            RETURN;
         END IF;

      END IF;

      /*if install part requirement */
      IF ( ab_Install_bool = 1 ) THEN

         INSERT INTO sched_inst_part (
            sched_db_id,
            sched_id,
            sched_part_id,
            sched_inst_part_id,
            part_no_db_id,
            part_no_id,
            serial_no_oem,
            inst_qt
         )
         VALUES (
            an_SchedDbId,
            an_SchedId,
            an_SchedPartId,
            ln_InstIndex,
            lrec_SchedPart.spec_part_no_db_id,
            lrec_SchedPart.spec_part_no_id,
            null,
            ln_QtNumber
         );

         ln_InstIndex := ln_InstIndex+1;

      END IF;

   END LOOP;

   -- Return success
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
     on_Return := icn_Error;
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenerateRemoveInstallPartReq@@@'||SQLERRM);

END GenerateRemoveInstallPartReq;

/********************************************************************************
*
* Procedure:    FindBomInventoryInTree
* Arguments:    an_StartInvNoDbId  (long) - the inventory that you want to start
*                                           the search from
*               an_StartInvNoId    (long) - ""
*               an_FindAssmblDbId  (long) - the bom item that you are looking for
*               an_FindAssmblCd    (long) - ""
*               an_FindAssmblBomId (long) - ""
* Return:       on_InvNoDbId       (long[]) - the list of inventory that match
*                                             the given bom, and are in the given
*                                             inventory tree
*               on_InvNoId         (long[]) - ""
*               on_Return          (long) - succss/failure of procedure
*
* Description:  This procedure is used to find inventory in a tree. You will start
                with a given inventory, and then search for any inventory in the
                tree that have the FindXXX bom item. You will search your direct
                children and parents first. If no children or parents have this
                bom item, then you will start to search siblings.
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindBomInventoryInTree(
      an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_StartAssmblDbId    IN eqp_assmbl.assmbl_db_id%TYPE,
      as_StartAssmblCd      IN eqp_assmbl.assmbl_cd%TYPE,
      an_StartAssmblBomId   IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      otabn_InvNoDbId      OUT typtabn_DbId,
      otabn_InvNoId        OUT typtabn_Id,
      on_Return            OUT typn_RetCode
   ) IS

   /* find all parents and children of this inv that have the given BOM */
   CURSOR lcur_ParentOrChildInv (
         cn_StartInvNoDbId    inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    eqp_assmbl.assmbl_db_id%TYPE,
         cn_FindAssmblCd      eqp_assmbl.assmbl_cd%TYPE,
         cn_FindAssmblBomId   eqp_assmbl_bom.assmbl_bom_id%TYPE
      ) IS
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      FROM
         inv_inv
      WHERE
         ( inv_inv.inv_no_db_id, inv_inv.inv_no_id ) IN
         ( SELECT
               inv_no_db_id,
               inv_no_id
            FROM
               inv_inv
            WHERE rstat_cd = 0
            START WITH
               inv_no_db_id = cn_StartInvNoDbId AND
               inv_no_id    = cn_StartInvNoId
            CONNECT BY
               nh_inv_no_db_id = PRIOR inv_no_db_id AND
               nh_inv_no_id    = PRIOR inv_no_id )
         AND
         (
           (
             inv_inv.assmbl_db_id  = cn_FindAssmblDbId AND
             inv_inv.assmbl_cd     = cn_FindAssmblCd AND
             inv_inv.assmbl_bom_id = cn_FindAssmblBomId AND
             inv_inv.inv_class_cd <> 'SER'              AND
             inv_inv.rstat_cd = 0
           )
           OR
           (
             inv_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
             inv_inv.orig_assmbl_cd    = cn_FindAssmblCd AND
             cn_FindAssmblBomId = 0              AND
             inv_inv.rstat_cd = 0
           )
         )
      UNION
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      FROM
         inv_inv
      WHERE
         ( inv_inv.inv_no_db_id, inv_inv.inv_no_id ) IN
         ( SELECT
               inv_no_db_id,
               inv_no_id
            FROM
               inv_inv
            WHERE rstat_cd = 0
            START WITH
               inv_no_db_id = cn_StartInvNoDbId AND
               inv_no_id    = cn_StartInvNoId
            CONNECT BY
               inv_no_db_id = PRIOR nh_inv_no_db_id AND
               inv_no_id    = PRIOR nh_inv_no_id )
         AND
         (
           (
             inv_inv.assmbl_db_id  = cn_FindAssmblDbId AND
             inv_inv.assmbl_cd     = cn_FindAssmblCd AND
             inv_inv.assmbl_bom_id = cn_FindAssmblBomId AND
             inv_inv.inv_class_cd <> 'SER'              AND
             inv_inv.rstat_cd = 0
           )
           OR
           (
             inv_inv.orig_assmbl_db_id = cn_FindAssmblDbId AND
             inv_inv.orig_assmbl_cd    = cn_FindAssmblCd AND
             cn_FindAssmblBomId = 0              AND
             inv_inv.rstat_cd = 0
           )
         );

   /* find all siblings of this inv that have a particular BOM */
   CURSOR lcur_SiblingInv (
         cn_StartInvNoDbId    inv_inv.inv_no_db_id%TYPE,
         cn_StartInvNoId      inv_inv.inv_no_id%TYPE,
         cn_FindAssmblDbId    eqp_assmbl.assmbl_db_id%TYPE,
         cn_FindAssmblCd      eqp_assmbl.assmbl_cd%TYPE,
         cn_FindAssmblBomId   eqp_assmbl_bom.assmbl_bom_id%TYPE
      ) IS
      SELECT
         target_inv.inv_no_db_id,
         target_inv.inv_no_id,
         target_inv.inv_no_sdesc
      FROM
         inv_inv start_inv,
         inv_inv target_inv
      WHERE
         start_inv.inv_no_db_id = cn_StartInvNoDbId AND
         start_inv.inv_no_id    = cn_StartInvNoId              AND
         start_inv.rstat_cd   = 0
         AND
         target_inv.assmbl_db_id  = cn_FindAssmblDbId AND
         target_inv.assmbl_cd     = cn_FindAssmblCd AND
         target_inv.assmbl_bom_id = cn_FindAssmblBomId AND
         target_inv.inv_class_cd <> 'SER'              AND
         target_inv.rstat_cd    = 0
         AND
         (
           (
             target_inv.assmbl_inv_no_db_id = start_inv.assmbl_inv_no_db_id AND
             target_inv.assmbl_inv_no_id    = start_inv.assmbl_inv_no_id
           )
           OR
           (
             target_inv.assmbl_inv_no_db_id = start_inv.inv_no_db_id AND
             target_inv.assmbl_inv_no_id    = start_inv.inv_no_id
           )
         );

   /* local variables */
   ln_Index NUMBER;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* find the list of inventory that child tasks should be created on */
   otabn_InvNoDbId.DELETE;
   otabn_InvNoId.DELETE;

   /* if you are looking for the same bom item that you started with,
      then simply use the starting inventory */
   IF an_StartAssmblDbId  = an_FindAssmblDbId AND
      as_StartAssmblCd    = as_FindAssmblCd AND
      an_StartAssmblBomId = an_FindAssmblBomId THEN

      /* simply return the given inventory */
      otabn_InvNoDbId(1) := an_StartInvNoDbId;
      otabn_InvNoId(1)   := an_StartInvNoId;

      /* return success */
      on_Return := icn_Success;
      RETURN;
   END IF;

   /* find any other inventory that are directly above, or directly below
      the starting inventory */
   ln_Index := 0;
   FOR lrec_ParentOrChildInv IN lcur_ParentOrChildInv (
            an_StartInvNoDbId,
            an_StartInvNoId,
            an_FindAssmblDbId,
            as_FindAssmblCd,
            an_FindAssmblBomId )
   LOOP
      ln_Index := ln_Index + 1;
      otabn_InvNoDbId(ln_Index) := lrec_ParentOrChildInv.inv_no_db_id;
      otabn_InvNoId(ln_Index)   := lrec_ParentOrChildInv.inv_no_id;
   END LOOP;

   /* if there are no inventory directly above or below that start_inv for
      this BOM, then look for siblings */
   IF ln_Index = 0 THEN

      FOR lrec_SiblingInv IN lcur_SiblingInv (
            an_StartInvNoDbId,
            an_StartInvNoId,
            an_FindAssmblDbId,
            as_FindAssmblCd,
            an_FindAssmblBomId )
      LOOP
         ln_Index := ln_Index + 1;
         otabn_InvNoDbId(ln_Index) := lrec_SiblingInv.inv_no_db_id;
         otabn_InvNoId(ln_Index)   := lrec_SiblingInv.inv_no_id;
      END LOOP;

   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@FindBomInventoryInTree@@@'||SQLERRM);
     RETURN;
END FindBomInventoryInTree;


/********************************************************************************
*
* Procedure:    GenForecastedTasks
* Arguments:    an_SchedDbId (long) - the task whose dependencies should be created
                an_SchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will generate all task dependencies
                that fall within the next forecast_range_qt number of days.
*
* Orig.Coder:   A. Hircock
* Recent Coder: Jonathan R. Clarkin
* Recent Date:  February 24, 2009
*
*********************************************************************************
*
* Copyright 1997-2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenForecastedTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      os_ExitCd    OUT VARCHAR2,
      on_Return    OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* this cursor will return details about the task and its definition */
   CURSOR lcur_TaskDetails (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT
         DECODE(
            evt_sched_dead.sched_dead_dt,
            NULL,
            SYSDATE,
            evt_sched_dead.sched_dead_dt) sched_dead_gdt,
         DECODE( task_task.forecast_range_qt, NULL, 0, task_task.forecast_range_qt ) forecast_range_qt,
         DECODE( h_inv_inv.inv_class_cd, 'ACFT', 1, 0 ) on_wing_bool,
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_event.hist_bool,
         evt_event.event_status_cd,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         task_task.task_db_id,
         task_task.task_id,
         mim_data_type.domain_type_cd,
         sched_stask.orphan_frct_bool
      FROM
         sched_stask,
         task_task,
         evt_event,
         evt_inv,
         inv_inv h_inv_inv,
         evt_sched_dead,
         mim_data_type
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         evt_event.event_db_id = cn_SchedDbId AND
         evt_event.event_id    = cn_SchedId     AND
         evt_event.rstat_cd   = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         evt_inv.event_db_id = sched_stask.sched_db_id AND
         evt_inv.event_id    = sched_stask.sched_id
         AND
         h_inv_inv.inv_no_db_id = evt_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id    = evt_inv.h_inv_no_id
         AND
         evt_sched_dead.event_db_id       (+)= sched_stask.sched_db_id AND
         evt_sched_dead.event_id          (+)= sched_stask.sched_id AND
         evt_sched_dead.sched_driver_bool (+)= 1
         AND
         mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
         mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

/* in the forecast chain, find the ACTV head task and record the deadline */
 CURSOR lcur_ActiveTaskInfo (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT evt_sched_dead.sched_dead_dt,
             evt_event.sched_priority_cd,
             DECODE ( evt_event_rel.rel_event_db_id, NULL, 0, 1) AS has_forecast
      FROM   evt_event,
             evt_event_rel,
             evt_sched_dead,
             (SELECT     evt_event_rel.event_db_id,
                         evt_event_rel.event_id
              FROM       evt_event_rel
              START WITH rel_event_db_id      = cn_SchedDbId AND
                         rel_event_id         = cn_SchedId   AND
                         rel_type_cd      = 'DEPT'
              CONNECT BY rel_event_db_id  = PRIOR event_db_id AND
                         rel_event_id     = PRIOR event_id  AND
                         rel_type_cd      = 'DEPT'
              UNION ALL
              SELECT     cn_SchedDbId AS event_db_id,
                         cn_SchedId   AS event_id
              FROM       dual
              ) chain
      WHERE   evt_event.event_db_id     = chain.event_db_id AND
              evt_event.event_id        = chain.event_id    AND
              evt_event.event_status_cd = 'ACTV' AND
              evt_event.rstat_cd = 0
              AND
              evt_sched_dead.event_db_id       = evt_event.event_db_id AND
              evt_sched_dead.event_id          = evt_event.event_id    AND
              evt_sched_dead.sched_driver_bool = 1
              AND
              -- get the next task if it exists
              evt_event_rel.event_db_id (+)= evt_event.event_db_id AND
              evt_event_rel.event_id    (+)= evt_event.event_id    AND
              evt_event_rel.rel_type_cd (+)= 'DEPT';
   lrec_ActiveTaskInfo lcur_ActiveTaskInfo%ROWTYPE;


   /* this SQL will retrieve a list of all task definitions that are dependencies
      to this sched_task, but have not been instantiated yet */
   CURSOR lcur_DependentTask (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         DECODE( task_part_map.task_id, NULL, 0, 1) AS part_based
      FROM
         sched_stask init_stask,
         task_task_dep,
         evt_inv,
         inv_inv,
         task_task,
         task_part_map
      WHERE
         init_stask.sched_db_id = cn_SchedDbId AND
         init_stask.sched_id    = cn_SchedId   AND
         init_stask.rstat_cd  = 0
         AND
         task_task_dep.task_db_id = init_stask.task_db_id AND
         task_task_dep.task_id    = init_stask.task_id
         AND
         task_task_dep.task_dep_action_cd = 'CRT' AND
         task_task.task_defn_db_id        = task_task_dep.dep_task_defn_db_id AND
         task_task.task_defn_id           = task_task_dep.dep_task_defn_id
         AND
         evt_inv.event_db_id = init_stask.sched_db_id AND
         evt_inv.event_id    = init_stask.sched_id
         AND
         inv_inv.part_no_db_id (+)= evt_inv.part_no_db_id   AND
         inv_inv.part_no_id    (+)= evt_inv.part_no_id
         AND
         task_part_map.task_db_id (+)= init_stask.task_db_id AND
         task_part_map.task_id    (+)= init_stask.task_id
         AND
         -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         (task_task.assmbl_db_id IS NOT NULL
          OR
          (
            inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
            inv_inv.part_no_id    = task_part_map.part_no_id
          )
         )
         AND
         NOT EXISTS
         (
           SELECT
              1
           FROM
              sched_stask dep_stask,
              evt_event_rel,
              task_task dep_task
           WHERE
              evt_event_rel.event_db_id = init_stask.sched_db_id AND
              evt_event_rel.event_id    = init_stask.sched_id AND
              evt_event_rel.rel_type_cd = 'DEPT'
              AND
              dep_stask.sched_db_id = evt_event_rel.rel_event_db_id AND
              dep_stask.sched_id    = evt_event_rel.rel_event_id
              AND
              dep_task.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
              dep_task.task_defn_id    = task_task_dep.dep_task_defn_id
              AND
              dep_stask.task_db_id = dep_task.task_db_id AND
              dep_stask.task_id    = dep_task.task_id
         )
    UNION

      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id,
         DECODE( task_part_map.task_id, NULL, 0, 1) AS part_based
      FROM
         sched_stask init_stask,
         task_task_dep,
         task_task,
         task_task orig_task_task,
         evt_inv,
         inv_inv,
         task_part_map
      WHERE
         init_stask.sched_db_id = cn_SchedDbId AND
         init_stask.sched_id    = cn_SchedId AND
         init_stask.rstat_cd  = 0
         AND
         -- Get current task definition
         orig_task_task.task_db_id = init_stask.task_db_id AND
         orig_task_task.task_id    = init_stask.task_id
         AND
         -- Get POSTCRT relation only
         task_task_dep.task_dep_action_cd = 'POSTCRT'
         AND
         -- Join current task definition to the right columns in task_task_dep
         task_task_dep.dep_task_defn_db_id = orig_task_task.task_defn_db_id AND
         task_task_dep.dep_task_defn_id    = orig_task_task.task_defn_id
         AND
         -- Ensure the dependent task definition is active
         task_task.task_db_id = task_task_dep.task_db_id AND
         task_task.task_id    = task_task_dep.task_id
         AND
         task_task.task_def_status_cd = 'ACTV'
         AND
         evt_inv.event_db_id = init_stask.sched_db_id AND
         evt_inv.event_id    = init_stask.sched_id
         AND
         inv_inv.part_no_db_id (+)= evt_inv.part_no_db_id   AND
         inv_inv.part_no_id    (+)= evt_inv.part_no_id
         AND
         task_part_map.task_db_id (+)= init_stask.task_db_id AND
         task_part_map.task_id    (+)= init_stask.task_id
         AND
          -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         ( task_task.assmbl_db_id IS NOT NULL
           OR
           (
             inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
             inv_inv.part_no_id    = task_part_map.part_no_id
           )
         )

         AND
            NOT EXISTS
            (
               SELECT
                  1
               FROM
                  sched_stask dep_stask,
                  evt_event_rel,
                  task_task dep_task
               WHERE
                  evt_event_rel.event_db_id = init_stask.sched_db_id AND
                  evt_event_rel.event_id    = init_stask.sched_id AND
                  evt_event_rel.rel_type_cd = 'DEPT'
                  AND
                  dep_stask.sched_db_id = evt_event_rel.rel_event_db_id AND
                  dep_stask.sched_id    = evt_event_rel.rel_event_id
                  AND
                  dep_task.task_db_id = task_task_dep.task_db_id AND
                  dep_task.task_id   = task_task_dep.task_id
                  AND
                  dep_stask.task_db_id = dep_task.task_db_id AND
                  dep_stask.task_id    = dep_task.task_id
             );
             lrec_DependentTask lcur_DependentTask%ROWTYPE;

   /* local variables */
   ltabn_DepTaskInvNoDbId typtabn_DbId;
   ltabn_DepTaskInvNoId   typtabn_Id;
   ln_NewSchedDbId        sched_stask.sched_db_id%TYPE;
   ln_NewSchedId          sched_stask.sched_id%TYPE;
   ln_TaskDbId            task_defn.task_defn_db_id%TYPE;
   ln_TaskId              task_defn.task_defn_id%TYPE;
   ln_ForecastRangeQt     task_task.forecast_range_qt%TYPE;
   ln_ActiveDeadlineDt    evt_sched_dead.sched_dead_dt%TYPE;
   lb_GenerateForecast    BOOLEAN;
   ln_Count   NUMBER;     /* number of scheduled rules */

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;
   os_ExitCd := 'NORM';
   lb_GenerateForecast := TRUE;

   /* get details about the task and its definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
   IF lrec_TaskDetails.Orphan_Frct_Bool = 1 THEN
      on_Return := icn_Success;
      os_ExitCd := 'ORPHAN';
      RETURN;
   END IF;

   /* we should not generate forcasted tasks for canceled tasks */
   IF ( lrec_TaskDetails.hist_bool = 1 AND NOT ( lrec_TaskDetails.event_status_cd = 'COMPLETE' ) ) THEN
      on_Return := icn_Success;
      os_ExitCd := 'CANCEL';
      RETURN;
   END IF;

   /* do not forecast tasks with forecast range set to 0 */
   IF lrec_TaskDetails.forecast_range_qt = 0 AND lrec_TaskDetails.hist_bool = 0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'ZERO_RNG';
      RETURN;
   END IF;

   /* do not forecast more than the very next task for usage-based intervals for off-wing components */
   IF ( lrec_TaskDetails.on_wing_bool = 0 AND lrec_TaskDetails.domain_type_cd <> 'CA' ) AND lrec_TaskDetails.hist_bool = 0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'OFF_WING';
      RETURN;
   END IF;

   /* check if the current task has scheduling rule defined */
   SELECT
      COUNT(*)
   INTO
      ln_Count
   FROM
      task_sched_rule
   WHERE
      task_sched_rule.task_db_id = lrec_TaskDetails.task_db_id AND
      task_sched_rule.task_id    = lrec_TaskDetails.task_id;

   /* if the current task is active, and scheduled rule was not defined do not
      generate more tasks. When current task is completed and does not have
      schedule rules defined we will attempt to generate one more task.*/
   IF ln_Count = 0 AND lrec_TaskDetails.hist_bool=0 THEN
      on_Return := icn_Success;
      os_ExitCd := 'NO_RULE';
      RETURN;
   END IF;

   /* retrieve the ACTV task deadline date in order to add the forcaste range. */
   OPEN lcur_ActiveTaskInfo(an_SchedDbId, an_SchedId);
   FETCH lcur_ActiveTaskInfo INTO lrec_ActiveTaskInfo;
   IF lcur_ActiveTaskInfo%FOUND THEN
      ln_ActiveDeadlineDt := lrec_ActiveTaskInfo.sched_dead_dt;
      IF  lrec_ActiveTaskInfo.sched_priority_cd = 'O/D' AND lrec_ActiveTaskInfo.has_forecast = 1 THEN
          lb_GenerateForecast := FALSE;
      END IF;
   ELSE
      ln_ActiveDeadlineDt := SYSDATE;
   END IF;
   CLOSE lcur_ActiveTaskInfo;

    /* only generate dependent tasks if the current task's deadline falls before
      the forecast_range_qt number of days and the ACTV is not O/D with a FORECAST task already */
   IF lrec_TaskDetails.hist_bool=0 AND
      ( lb_GenerateForecast = FALSE OR
       lrec_TaskDetails.sched_dead_gdt > (ln_ActiveDeadlineDt + lrec_TaskDetails.forecast_range_qt ))    THEN
      on_Return := icn_Success;
      os_ExitCd := 'PAST_RNG';
      RETURN;
   END IF;

   /* There is a limit on number of open cursors, in order to keep the number of open cursors
      not to exceed its maximum limit we have specified number of maximum forecasted active tasks.
      We get the number of already forecasted tasks.*/

   SELECT
      COUNT(*)
   INTO
      ln_Count
   FROM
      evt_event
   WHERE
      hist_bool = 0  AND
      rstat_cd = 0
      AND
      (event_db_id, event_id) IN
      (  SELECT
            event_db_id,
            event_id
         FROM
            evt_event_rel
         START WITH
            rel_event_db_id = an_SchedDbId AND
            rel_event_id    = an_SchedId AND
            rel_type_cd  = 'DEPT'
         CONNECT BY
            rel_event_db_id  = PRIOR event_db_id AND
            rel_event_id     = PRIOR event_id  AND
            rel_type_cd  = 'DEPT'
      );

   /* the maximum number of non historic forecasted tasks is set to 200.
      Do not forecast any more tasks, if 200 tasks were forecasted.*/
   IF (ln_Count > 200) THEN
      on_Return := icn_Success;
      os_ExitCd := 'MAX';
      RETURN;
   END IF;

   /* find a list of all dependent tasks */
   FOR lrec_DependentTask IN lcur_DependentTask( an_SchedDbId, an_SchedId )
   LOOP
      IF lrec_DependentTask.part_based > 0 THEN

        /* Determine whether the given task/inventory combination are legal */
         SELECT
            COUNT(*)
         INTO
            ln_Count
         FROM
            inv_inv
            INNER JOIN task_part_map ON
                       task_part_map.part_no_db_id = inv_inv.part_no_db_id AND
                       task_part_map.part_no_id    = inv_inv.part_no_id
            INNER JOIN task_task ON
                       task_task.task_db_id = task_part_map.task_db_id AND
                       task_task.task_id    = task_part_map.task_id
         WHERE
            task_task.task_defn_db_id = lrec_DependentTask.task_defn_db_id AND
            task_task.task_defn_id    = lrec_DependentTask.task_defn_id
            AND
            inv_inv.inv_no_db_id  = lrec_TaskDetails.inv_no_db_id AND
            inv_inv.inv_no_id     = lrec_TaskDetails.inv_no_id   AND
            inv_inv.rstat_cd  = 0;

         IF ln_Count > 0 THEN
            ltabn_DepTaskInvNoDbId.DELETE();
            ltabn_DepTaskInvNoId.DELETE();

            ltabn_DepTaskInvNoDbId(1) := lrec_TaskDetails.inv_no_db_id;
            ltabn_DepTaskInvNoId(1)   := lrec_TaskDetails.inv_no_id;
         END IF;

      ELSE
         /* find the inventory that the dependent task should be created on */
         FindBomInventoryInTree(
             lrec_TaskDetails.inv_no_db_id,
             lrec_TaskDetails.inv_no_id,
             lrec_TaskDetails.assmbl_db_id,
             lrec_TaskDetails.assmbl_cd,
             lrec_TaskDetails.assmbl_bom_id,
             lrec_DependentTask.assmbl_db_id,
             lrec_DependentTask.assmbl_cd,
             lrec_DependentTask.assmbl_bom_id,
             ltabn_DepTaskInvNoDbId,
             ltabn_DepTaskInvNoId,
             on_Return );
         IF on_Return < 1 THEN
            os_ExitCd := 'FAIL';
            RETURN;
         END IF;
      END IF;

      /* loop for every inventory that this task could be on */
      FOR i IN 1..ltabn_DepTaskInvNoDbId.COUNT
      LOOP

         ln_TaskDbId := 0;
         ln_TaskId := 0;

         /* determine the correct revision to generate */
         BEGIN

            /* set the context for the view */
            context_package.set_inv(ltabn_DepTaskInvNoDbId(i), ltabn_DepTaskInvNoId(i), on_Return);

            IF on_Return < 1 THEN
               os_ExitCd := 'FAIL';
               RETURN;
            END IF;

            SELECT
               vw_baseline_task.task_db_id,
               vw_baseline_task.task_id
            INTO
               ln_TaskDbId,
               ln_TaskId
            FROM
               vw_baseline_task
            WHERE
               vw_baseline_task.task_defn_db_id = lrec_DependentTask.Task_Defn_db_id AND
               vw_baseline_task.task_defn_id = lrec_DependentTask.Task_Defn_id;

         /* If no rows found then we are not generating forecasted tasks */
         EXCEPTION WHEN NO_DATA_FOUND THEN
            on_Return := icn_Success;
            os_ExitCd := 'NO_DATA';
            RETURN;
         END;

         /* we are not generating forecasted tasks, if the selected task is OBSOLETE */
         IF (ln_TaskDbId IS NULL) THEN
            on_Return := icn_Success;
            os_ExitCd := 'OBSLT';
            RETURN;
         END IF;

         /* Obtain forecast range for the task definition revision */
         SELECT
            task_task.forecast_range_qt
         INTO
            ln_ForecastRangeQt
         FROM
            task_task
         WHERE
            task_task.task_db_id = ln_TaskDbId AND
            task_task.task_id    = ln_TaskId;

         /* generate dependent tasks only, if the initial task is historic, and the forcasted range of the dependent task is not 0 and the status of the dependent task is ACTV.  Otherwise deal with the next dependent task */
         IF NOT(ln_TaskDbId IS NULL AND ln_TaskId IS NULL) AND NOT (lrec_TaskDetails.hist_bool=0 AND ln_ForecastRangeQt=0) THEN

            /* create the dependent task */
            GenSchedTask(
                     null,
                     null,
                     ltabn_DepTaskInvNoDbId(i),
                     ltabn_DepTaskInvNoid(i),
                     ln_TaskDbId,
                     ln_TaskId,
                     an_SchedDbId,
                     an_SchedId,
                     null,
                     null,
                     null,
                     null,
                     null,
                     null,
                     false, -- Indicates that the procedure is being called internally
                     false, -- Indicated thet create labour, part, tool, forecast tasks, auto create children will be run
                     false,
                     NULL,
                     ln_NewSchedDbId,
                     ln_NewSchedId,
                     on_Return );
            IF on_Return < 1 THEN
               os_ExitCd := 'FAIL';
               RETURN;
            END IF;
         END IF;
      END LOOP;  -- Loop for creation of actual tasks

   END LOOP; -- Loop for lookup of potential tasks

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenForecastedTasks@@@'||SQLERRM);
     RETURN;
END GenForecastedTasks;



/********************************************************************************
*
* Procedure:    AutoCreateChildTasks
* Arguments:    an_SchedNoDbId   (long) - task's database id
*               an_SchedNoId     (long) - task's id
* Return:       on_Return        (long) - success/failure of procedure
*
* Description:  This procedure will create all child tasks that were defined as
*               "not on condition". It should be called when you are creating a
*               new task based on a task definition.
*
* Orig.Coder:   A. Hircock
* Recent Coder: Yui Sotozaki
* Recent Date:  January 09, 2008
*
*********************************************************************************
*
* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AutoCreateChildTasks (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode
   ) IS

   /* *** CURSOR DECLARATIONS *** */
   /* This cursor will return some details about the task and its definition */
   CURSOR lcur_TaskDetails (
         cn_SchedDbId  sched_stask.sched_db_id%TYPE,
         cn_SchedId    sched_stask.sched_id%TYPE
      ) IS
      SELECT
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         task_task.assmbl_db_id,
         task_task.assmbl_cd,
         task_task.assmbl_bom_id
      FROM
         sched_stask,
         task_task,
         evt_inv
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         evt_inv.event_db_id     = sched_stask.sched_db_id AND
         evt_inv.event_id        = sched_stask.sched_id AND
         evt_inv.main_inv_bool   = 1;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

   /* This cursor will provide a list of child task definitions for a
      particular task */
   CURSOR lcur_ChildTask (
         cn_SchedDbId task_task.task_db_id%TYPE,
         cn_SchedId   task_task.task_id%TYPE
      ) IS
      --    List of child tasks to create.
      SELECT
         jic_task_task.task_db_id,
         jic_task_task.task_id,
         jic_task_task.task_class_cd,
         jic_task_task.assmbl_db_id,
         jic_task_task.assmbl_cd,
         jic_task_task.assmbl_bom_id,
         task_part_map.task_id AS part_based,
         inv_inv.inv_no_db_id AS inv_no_db_id,
         inv_inv.inv_no_id    AS inv_no_id
      FROM
         sched_stask,
         task_task req_task_task,
         task_jic_req_map,
         task_task jic_task_task,
         task_part_map,
         evt_event,
         evt_inv,
         inv_inv

      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         req_task_task.task_db_id   = sched_stask.task_db_id   AND
         req_task_task.task_id      = sched_stask.task_id
         AND
         task_jic_req_map.req_task_defn_db_id   = req_task_task.task_defn_db_id  AND
         task_jic_req_map.req_task_defn_id      = req_task_task.task_defn_id
         AND
         jic_task_task.task_db_id   = task_jic_req_map.jic_task_db_id   AND
         jic_task_task.task_id      = task_jic_req_map.jic_task_id
         AND
         jic_task_task.task_def_status_cd = 'ACTV'
         AND
         evt_event.event_db_id = cn_SchedDbId AND
         evt_event.event_id    = cn_SchedId   AND
         evt_event.rstat_cd   = 0
         AND
         evt_inv.event_db_id   = evt_event.event_db_id AND
         evt_inv.event_id      = evt_event.event_id    AND
         evt_inv.main_inv_bool = 1
         AND
         inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = evt_inv.inv_no_id
         AND
         task_part_map.task_db_id (+)= jic_task_task.task_db_id AND
         task_part_map.task_id    (+)= jic_task_task.task_id
         AND

         ( task_part_map.task_id IS NULL
           OR
           EXISTS
            ( SELECT
                 1
              FROM
                 eqp_part_no
              WHERE
                 eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
                 eqp_part_no.part_no_id    = inv_inv.part_no_id
                 AND
                 eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                 eqp_part_no.part_no_id    = task_part_map.part_no_id
            )
         )

      UNION ALL
      SELECT
         req_task_task.task_db_id,
         req_task_task.task_id,
         req_task_task.task_class_cd,
         req_task_task.assmbl_db_id,
         req_task_task.assmbl_cd,
         req_task_task.assmbl_bom_id,
         NULL AS part_based,
         NULL AS inv_no_db_id,
         NULL AS inv_no_id
      FROM
         sched_stask,
         task_task block_task_task,
         task_block_req_map,
         task_task req_task_task
      WHERE
         sched_stask.sched_db_id = cn_SchedDbId AND
         sched_stask.sched_id    = cn_SchedId   AND
         sched_stask.rstat_cd = 0
         AND
         block_task_task.task_db_id   = sched_stask.task_db_id   AND
         block_task_task.task_id      = sched_stask.task_id
         AND
         task_block_req_map.block_task_db_id = block_task_task.task_db_id  AND
         task_block_req_map.block_task_id    = block_task_task.task_id
         AND
         req_task_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id   AND
         req_task_task.task_defn_id    = task_block_req_map.req_task_defn_id
         AND
         req_task_task.task_def_status_cd = 'ACTV';


   /* LOCAL VARIABLES */
   ltabn_InvNoDbId      typtabn_DbId;
   ltabn_InvNoId        typtabn_Id;
   ln_NewSchedDbId      sched_stask.sched_db_id%TYPE;
   ln_NewSchedId        sched_stask.sched_id%TYPE;
   ln_ChildrenCreated   NUMBER;

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get details about the task and its definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   ln_ChildrenCreated := 0;

   /* find all auto-create child tasks */
   FOR lrec_ChildTask IN lcur_ChildTask( an_SchedDbId, an_SchedId ) LOOP

   IF lrec_ChildTask.Part_Based IS NOT NULL THEN

   ltabn_InvNoDbId(1) := lrec_ChildTask.Inv_No_Db_Id;
   ltabn_InvNoId(1)   := lrec_ChildTask.Inv_No_Id;

   ELSE
       /* if the last know status of the child task is not CANCEL*/
        /* find  the target inventory in the current inventory tree */
        FindBomInventoryInTree(
                       lrec_TaskDetails.inv_no_db_id,
                       lrec_TaskDetails.inv_no_id,
                       lrec_TaskDetails.assmbl_db_id,
                       lrec_TaskDetails.assmbl_cd,
                       lrec_TaskDetails.assmbl_bom_id,
                       lrec_ChildTask.assmbl_db_id,
                       lrec_ChildTask.assmbl_cd,
                       lrec_ChildTask.assmbl_bom_id,
                       ltabn_InvNoDbId,
                       ltabn_InvNoId,
                       on_Return );
        IF on_Return < 1 THEN
           RETURN;
         END IF;
   END IF;
     /* create a child task on every inventory item */
     FOR i IN 1..ltabn_InvNoDbId.COUNT LOOP

         /* create the child task */
         GenSchedTask(
              null,
              null,
              ltabn_InvNoDbId(i),
              ltabn_InvNoId(i),
              lrec_ChildTask.task_db_id,
              lrec_ChildTask.task_id,
              -1,
              -1,
              null,
              null,
              null,
              null,
              null,
              null,
              false, -- Indicates that the procedure is being called internally
              false, -- Indicated thet create labour, part, tool, forecast tasks, auto create children will be run
              false,
              NULL,
              ln_NewSchedDbId,
              ln_NewSchedId,
              on_Return );
         IF on_Return < 1 THEN
            RETURN;
         END IF;

         /* record the parent of this new child task */
         UPDATE
          evt_event
            SET
          nh_event_db_id = an_SchedDbId,
          nh_event_id    = an_SchedId
          WHERE
          event_db_id = ln_NewSchedDbId AND
          event_id    = ln_NewSchedId;

         /* increment the number of children that were created */
         ln_ChildrenCreated := ln_ChildrenCreated + 1;

         /* set the sub event's order number */
         UPDATE evt_event
            SET sub_event_ord = ln_ChildrenCreated --ln_SubTaskOrd
          WHERE event_db_id = ln_NewSchedDbId AND
           event_id    = ln_NewSchedId;

     END LOOP;

   END LOOP;

   /* if any children were created, then record the highest task for this
      new child task */
   IF ln_ChildrenCreated > 0 THEN
      UPDATE
            evt_event
         SET
            h_event_db_id = an_SchedDbId,
            h_event_id    = an_SchedId
         WHERE
            ( event_db_id, event_id ) IN
            ( SELECT
                  event_db_id,
                  event_id
               FROM
                  evt_event
               WHERE rstat_cd = 0
               START WITH
                  event_db_id = an_SchedDbId AND
                  event_id    = an_SchedId
               CONNECT BY
                  nh_event_db_id = PRIOR event_db_id AND
                  nh_event_id    = PRIOR event_id );
   END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@AutoCreateChildTasks@@@'||SQLERRM);
     RETURN;
END AutoCreateChildTasks;


/*******************************************************************************
*
* Procedure:    UpdateReplSchedPart
* Arguments:    an_SchedDbId   (long) - the task that should be updated
*               an_SchedId     (long) - ""
* Return:       on_Return      (long) - succss/failure of procedure
*
* Description:  Updated the replacement part requirement for the task and its
*               children. This will also work if the task is a child of a REPL.
*
********************************************************************************
*
* Copyright 1997-2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UpdateReplSchedPart(
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   ) IS

   /*
    * This cursor will lookup the replacement part requirement given a task.
    */
   CURSOR lcur_ReplSchedPart(
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
      SELECT
         sched_part.sched_db_id,
         sched_part.sched_id,
         sched_part.sched_part_id
      FROM
         (SELECT
            start_stask.sched_db_id,
            start_stask.sched_id,
            NVL(parent_stask.sched_db_id, start_stask.sched_db_id) AS repl_sched_db_id,
            NVL(parent_stask.sched_id, start_stask.sched_id) AS repl_sched_id
         FROM
            sched_stask start_stask
            INNER JOIN evt_event ON
               evt_event.event_db_id = start_stask.sched_db_id AND
               evt_event.event_id    = start_stask.sched_id
            LEFT OUTER JOIN sched_stask parent_stask ON
               parent_stask.sched_db_id = evt_event.nh_event_db_id AND
               parent_stask.sched_id    = evt_event.nh_event_id
         WHERE
            start_stask.task_class_cd = 'REPL'
            OR
            parent_stask.task_class_cd = 'REPL'
         ) repl_stask_tree
         INNER JOIN sched_stask repl_stask ON
            repl_stask.sched_db_id = repl_stask_tree.repl_sched_db_id AND
            repl_stask.sched_id    = repl_stask_tree.repl_sched_id
         INNER JOIN task_task ON
            task_task.task_db_id = repl_stask.task_db_id AND
            task_task.task_id    = repl_stask.task_id
         INNER JOIN sched_part ON
            sched_part.sched_db_id = repl_stask_tree.sched_db_id AND
            sched_part.sched_id    = repl_stask_tree.sched_id
            AND
            sched_part.assmbl_db_id  = task_task.repl_assmbl_db_id AND
            sched_part.assmbl_cd     = task_task.repl_assmbl_cd AND
            sched_part.assmbl_bom_id = task_task.repl_assmbl_bom_id
         INNER JOIN evt_event root_event ON
            (
               root_event.event_db_id = repl_stask_tree.repl_sched_db_id AND
               root_event.event_id    = repl_stask_tree.repl_sched_id
            )
            OR
            (
               root_event.event_db_id = repl_stask_tree.sched_db_id AND
               root_event.event_id    = repl_stask_tree.sched_id
            )
            OR
            (
               root_event.nh_event_db_id = repl_stask_tree.repl_sched_db_id AND
               root_event.nh_event_id    = repl_stask_tree.repl_sched_id
            )
      WHERE
         root_event.event_db_id = cn_SchedDbId AND
         root_event.event_id    = cn_SchedId
         AND
         EXISTS (
            SELECT
               1
            FROM
               sched_rmvd_part
            WHERE
               sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
               sched_rmvd_part.sched_id      = sched_part.sched_id AND
               sched_rmvd_part.sched_part_id = sched_part.sched_part_id
         );
   lrec_ReplSchedPart lcur_ReplSchedPart%ROWTYPE;

   /**
    * Find the tasks in the tree under the replacement including the replacement
    */
   CURSOR lcur_ReplTree(
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      evt_event.event_db_id,
      evt_event.event_id
   FROM
      sched_stask repl_stask
      INNER JOIN evt_event ON
         evt_event.nh_event_db_id = repl_stask.sched_db_id AND
         evt_event.nh_event_id    = repl_stask.sched_id
         OR
         evt_event.event_db_id = repl_stask.sched_db_id AND
         evt_event.event_id    = repl_stask.sched_id
   WHERE
      repl_stask.sched_db_id = cn_SchedDbId AND
      repl_stask.sched_id    = cn_SchedId
      AND
      repl_stask.task_class_cd = 'REPL'
   UNION ALL
   SELECT
      evt_event.event_db_id,
      evt_event.event_id
   FROM
      evt_event start_event
      INNER JOIN sched_stask repl_stask ON
         repl_stask.sched_db_id = start_event.nh_event_db_id AND
         repl_stask.sched_id    = start_event.nh_event_id
      INNER JOIN evt_event ON
         evt_event.nh_event_db_id = repl_stask.sched_db_id AND
         evt_event.nh_event_id    = repl_stask.sched_id
         OR
         evt_event.event_db_id = repl_stask.sched_db_id AND
         evt_event.event_id    = repl_stask.sched_id
   WHERE
      start_event.event_db_id = cn_SchedDbId AND
      start_event.event_id    = cn_SchedId
      AND
      repl_stask.task_class_cd = 'REPL';
   lrec_ReplTree lcur_ReplTree%ROWTYPE;

   -- stores the part requirement that contains the replacement
   ln_SchedDbId  sched_part.sched_db_id%TYPE;
   ln_SchedId     sched_part.sched_id%TYPE;
   ln_SchedPartId sched_part.sched_part_id%TYPE;

   lb_ExecutedSchedPartLookup NUMBER;

BEGIN

   lb_ExecutedSchedPartLookup := 0;

   -- if this task is in a REPL tree
   OPEN lcur_ReplTree( an_SchedDbId, an_SchedId );
   LOOP
      FETCH lcur_ReplTree INTO lrec_ReplTree;
      EXIT WHEN lcur_ReplTree%NOTFOUND;

      IF lb_ExecutedSchedPartLookup = 0 THEN

         OPEN lcur_ReplSchedPart( an_SchedDbId, an_SchedId );
         FETCH lcur_ReplSchedPart INTO lrec_ReplSchedPart;
         IF lcur_ReplSchedPart%FOUND THEN
            ln_SchedDbId   := lrec_ReplSchedPart.sched_db_id;
            ln_SchedId     := lrec_ReplSchedPart.sched_id;
            ln_SchedPartId := lrec_ReplSchedPart.sched_part_id;
         END IF;
         CLOSE lcur_ReplSchedPart;

         lb_ExecutedSchedPartLookup := 1;
      END IF;

      -- update the task with the replacement part requirement
      UPDATE
         sched_stask
      SET
         repl_sched_db_id   = ln_SchedDbId,
         repl_sched_id      = ln_SchedId,
         repl_sched_part_id = ln_SchedPartId
      WHERE
         sched_db_id = lrec_ReplTree.event_db_id AND
         sched_id    = lrec_ReplTree.event_id;

   END LOOP;
   CLOSE lcur_ReplTree;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateReplSchedPart@@@'||SQLERRM);
     RETURN;

END UpdateReplSchedPart;

/********************************************************************************
*
* Procedure:    GenSchedParts
* Arguments:    an_SchedDbId (long) - the task that was just created
*               an_SchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will generate all of the rows in SCHED_PART that
*               come from the baseline task definition. This is a private method
*               that is only ever called from the GenSchedTask method
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GenSchedParts(
      an_SchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_SchedId    IN sched_stask.sched_id%TYPE,
      on_Return     OUT typn_RetCode
   ) IS

   /* *** DECLARE LOCAL VARIABLES *** */
   ln_SchedPartId       sched_part.sched_part_id%TYPE;
   ln_IsJIC             NUMBER;
   /* *** DECLARE EXCEPTIONS *** */
   xc_UnknownSQLError              EXCEPTION;

   /* *** DECLARE CURSORS *** */
   /* get some details on the retrieved task */
   CURSOR lcur_TaskDetails (
      cn_SchedDbId sched_stask.sched_db_id%TYPE,
      cn_SchedId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      task_task.task_class_cd,
      task_task.task_db_id,
      task_task.task_id,
      task_task.assmbl_db_id,
      task_task.assmbl_cd,
      task_task.assmbl_bom_id,
      evt_inv.assmbl_db_id  inv_assmbl_db_id,
      evt_inv.assmbl_cd     inv_assmbl_cd,
      evt_inv.assmbl_bom_id inv_assmbl_bom_id,
      evt_inv.assmbl_pos_id,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      evt_inv.bom_part_db_id,
      evt_inv.bom_part_id,
      assy.assmbl_db_id as root_assmbl_db_id,
      assy.assmbl_cd as root_assmbl_cd,
      assy.assmbl_bom_id as root_assmbl_bom_id,
      assy.assmbl_pos_id as root_assmbl_pos_id,
      comp_inv_inv.assmbl_db_id as comp_assmbl_db_id,
      comp_inv_inv.assmbl_cd as comp_assmbl_cd,
      comp_inv_inv.assmbl_bom_id as comp_assmbl_bom_id,
      comp_inv_inv.assmbl_pos_id as comp_assmbl_pos_id
   FROM
      task_task,
      evt_inv,
      sched_stask,
      inv_inv assy,
      inv_inv,
      evt_event,
      evt_event_rel,
      evt_inv comp_evt_inv,
      inv_inv comp_inv_inv
   WHERE
      sched_stask.sched_db_id = cn_SchedDbId AND
      sched_stask.sched_id    = cn_SchedId   AND
      sched_stask.rstat_cd = 0
      AND
      evt_inv.event_db_id = sched_stask.sched_db_id AND
      evt_inv.event_id    = sched_stask.sched_id AND
      evt_inv.main_inv_bool=1
      AND
      inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id = evt_inv.inv_no_id
      AND
      (
         (
            inv_inv.assmbl_inv_no_db_id IS NOT NULL AND
            assy.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
            assy.inv_no_id    = inv_inv.assmbl_inv_no_id
         )
         OR
         (
            inv_inv.assmbl_inv_no_db_id IS NULL AND
            assy.inv_no_db_id = inv_inv.h_inv_no_db_id AND
            assy.inv_no_id    = inv_inv.h_inv_no_id
         )
      )
      AND
      task_task.task_db_id = sched_stask.task_db_id AND
      task_task.task_id = sched_stask.task_id
      AND
      evt_event.event_db_id = sched_stask.sched_db_id AND
      evt_event.event_id = sched_stask.sched_id
      AND
      evt_event_rel.event_db_id (+)= evt_event.nh_event_db_id AND
      evt_event_rel.event_id (+)= evt_event.nh_event_id AND
      evt_event_rel.rel_type_cd (+)= 'WORMVL'
      AND
      comp_evt_inv.event_db_id (+)= evt_event_rel.rel_event_db_id AND
      comp_evt_inv.event_id (+)= evt_event_rel.rel_event_id AND
      comp_evt_inv.main_inv_bool(+) = 1
      AND
      comp_inv_inv.inv_no_db_id (+)= comp_evt_inv.inv_no_db_id AND
      comp_inv_inv.inv_no_id (+)= comp_evt_inv.inv_no_id;
   lrec_TaskDetails lcur_TaskDetails%ROWTYPE;

   /* used to get part list for a task defn */
   CURSOR lcur_Part (
      cn_TaskDbId sched_stask.sched_db_id%TYPE,
      cn_TaskId   sched_stask.sched_id%TYPE
   ) IS
   SELECT
      task_part_list.bom_part_db_id,
      task_part_list.bom_part_id,
      task_part_list.install_bool,
      task_part_list.remove_bool,
      task_part_list.req_qt,
      task_part_list.remove_reason_db_id,
      task_part_list.remove_reason_cd,
      task_part_list.assmbl_db_id,
      task_part_list.assmbl_cd,
      task_part_list.assmbl_bom_id,
      task_part_list.assmbl_pos_id,
      task_part_list.spec_part_no_db_id,
      task_part_list.spec_part_no_id,
      eqp_bom_part.inv_class_cd,
      task_part_list.req_action_db_id,
      task_part_list.req_action_cd
   FROM
      task_part_list,
      eqp_bom_part
   WHERE
      task_part_list.task_db_id = cn_TaskDbId AND
      task_part_list.task_id = cn_TaskId
      AND
      eqp_bom_part.bom_part_db_id=task_part_list.bom_part_db_id AND
      eqp_bom_part.bom_part_id=task_part_list.bom_part_id
   ORDER BY task_part_list.task_part_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get details about the task definition */
   OPEN lcur_TaskDetails( an_SchedDbId, an_SchedId );
   FETCH lcur_TaskDetails INTO lrec_TaskDetails;
   CLOSE lcur_TaskDetails;

   /* check if the task defn. is JIC */
   ln_IsJIC := 0;

   SELECT
      COUNT(*) INTO ln_IsJIC
   FROM
      ref_task_class
   WHERE
      class_mode_cd = 'JIC'
      AND
      task_class_cd = lrec_TaskDetails.task_class_cd;

   /* create a row in sched_part for every row in task_part_list */
   ln_SchedPartId := 1;
   FOR lrec_Part IN lcur_Part( lrec_TaskDetails.task_db_id, lrec_TaskDetails.task_id )
   LOOP


  /* if the inventory it TRK or ASSY has the same bom part as the task use the task bom item position */
   IF (lrec_Part.inv_class_cd='TRK' OR lrec_Part.inv_class_cd='ASSY') AND
      lrec_TaskDetails.bom_part_db_id  = lrec_Part.bom_part_db_id AND
      lrec_TaskDetails.bom_part_id   = lrec_Part.bom_part_id  AND
      ln_IsJIC != 1 THEN

      lrec_Part.assmbl_db_id:=lrec_TaskDetails.inv_assmbl_db_id;
      lrec_Part.assmbl_cd:= lrec_TaskDetails.inv_assmbl_cd;
      lrec_Part.assmbl_bom_id:=lrec_TaskDetails.inv_assmbl_bom_id;
      lrec_Part.assmbl_pos_id:= lrec_TaskDetails.assmbl_pos_id;
   END IF;

   /* If this is a replacement job card use the position of part to be removed */
   IF (lrec_TaskDetails.comp_assmbl_db_id IS NOT NULL) THEN
      lrec_Part.assmbl_db_id:=lrec_TaskDetails.comp_assmbl_db_id;
      lrec_Part.assmbl_cd:= lrec_TaskDetails.comp_assmbl_cd;
      lrec_Part.assmbl_bom_id:=lrec_TaskDetails.comp_assmbl_bom_id;
      lrec_Part.assmbl_pos_id:= lrec_TaskDetails.comp_assmbl_pos_id;
   END IF;

         INSERT INTO sched_part (
               sched_db_id,
               sched_id,
               sched_part_id,
               sched_bom_part_db_id,
               sched_bom_part_id,
               sched_qt,
               nh_assmbl_db_id,
               nh_assmbl_cd,
               nh_assmbl_bom_id,
               nh_assmbl_pos_id,
               assmbl_db_id,
               assmbl_cd,
               assmbl_bom_id,
               assmbl_pos_id,
               sched_part_status_db_id,
               sched_part_status_cd,
               spec_part_no_db_id,
               spec_part_no_id,
               req_action_db_id,
               req_action_cd)
            VALUES (
               an_SchedDbId,
               an_SchedId,
               ln_SchedPartId,
               lrec_Part.bom_part_db_id,
               lrec_Part.bom_part_id,
               lrec_Part.req_qt,
               lrec_TaskDetails.root_assmbl_db_id,
               lrec_TaskDetails.root_assmbl_cd,
               lrec_TaskDetails.root_assmbl_bom_id,
               lrec_TaskDetails.root_assmbl_pos_id,
               lrec_Part.assmbl_db_id,
               lrec_Part.assmbl_cd,
               lrec_Part.assmbl_bom_id,
               lrec_Part.assmbl_pos_id,
               0,
               'ACTV',
               lrec_Part.spec_part_no_db_id,
               lrec_Part.spec_part_no_id,
               lrec_Part.req_action_db_id,
               lrec_Part.req_action_cd
               );

         GenerateRemoveInstallPartReq(
              lrec_TaskDetails.inv_no_db_id,
              lrec_TaskDetails.inv_no_id,
              an_SchedDbId,
              an_SchedId,
              ln_SchedPartId,
              lrec_Part.remove_reason_db_id,
              lrec_Part.remove_reason_cd,
              lrec_Part.remove_bool,
              lrec_Part.install_bool,
              on_Return);
         IF on_Return < 0 THEN
            RETURN;
         END IF;


         /* increment the sched_part_id counter in the PK*/
         ln_SchedPartId := ln_SchedPartId + 1;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@GenSchedParts@@@'||SQLERRM);
     RETURN;
END GenSchedParts;



/********************************************************************************
*
* Procedure:    UpdateHSched
* Arguments:    an_SchedNoDbId   (long) - task's database id
*               an_SchedNoId     (long) - task's id
* Return:       on_Return        (long) - succss/failure of procedure
*
* Description:  This procedure will update the reference between a task and its
*               root, non-check, task.
*
* Orig.Coder:   Randy A
* Recent Coder: N/A
* Recent Date:  Dec 5, 2006
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateHSched (
      an_SchedDbId IN sched_stask.sched_db_id%TYPE,
      an_SchedId   IN sched_stask.sched_id%TYPE,
      on_Return    OUT typn_RetCode
   ) IS

   /* EXCEPTIONS */
   xc_UnknownSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* Run a single update that will traverse the event tree of the given task
      and set the h_sched_db_id:h_sched_id values accordingly. */

   UPDATE
      ( SELECT sched_stask.h_sched_db_id,
               sched_stask.h_sched_id,
               sched_stask.sched_db_id,
               sched_stask.sched_id
        FROM   sched_stask
        WHERE  /* Don't set checks */
               sched_stask.task_class_cd NOT IN ('RO', 'CHECK')   AND
               sched_stask.rstat_cd = 0
               AND
               ( sched_stask.sched_db_id, sched_stask.sched_id ) IN
               ( /* Use connect by prior.  This may not be a root task that's passed in. */
                 SELECT evt_event.event_db_id,
                        evt_event.event_id
                 FROM   evt_event
                 START WITH
                        evt_event.event_db_id = an_SchedDbId AND
                        evt_event.event_id    = an_SchedId
                 CONNECT BY
                        evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
                        evt_event.nh_event_id    = PRIOR evt_event.event_id
               )        
        ORDER BY sched_stask.sched_db_id, sched_stask.sched_id                       
      ) sched_stask
   SET
      ( sched_stask.h_sched_db_id, sched_stask.h_sched_id ) =
      (
         /* Find the higest non-check task looking upwards
            Order by level descending to ensure you get the highest in the hierarchy */
         SELECT DISTINCT
            FIRST_VALUE( evt_event.event_db_id ) OVER ( ORDER BY LEVEL DESC ),
            FIRST_VALUE( evt_event.event_id ) OVER ( ORDER BY LEVEL DESC )
         FROM
            evt_event
         WHERE rstat_cd = 0 AND
            /* Filter out checks */
            NOT EXISTS (
               SELECT
                  1
               FROM
                  sched_stask check_stask
               WHERE
                  check_stask.sched_db_id = evt_event.event_db_id AND
                  check_stask.sched_id    = evt_event.event_id
                  AND
                  check_stask.task_class_cd IN ('RO', 'CHECK')
            )
         START WITH
            evt_event.event_db_id =  sched_stask.sched_db_id AND
            evt_event.event_id    =  sched_stask.sched_id
         CONNECT BY
            evt_event.event_db_id = PRIOR evt_event.nh_event_db_id AND
            evt_event.event_id    = PRIOR evt_event.nh_event_id
      );

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateHSched@@@'||SQLERRM);

     RETURN;
END UpdateHSched;



/********************************************************************************
*
* Procedure:    UpdateTaskLabourSummary
* Arguments:    an_Months        (long) - task's database id
* Return:       on_Return        (long) - succss/failure of procedure
*
* Description:  This procedure will update dwt_task_labour_summary table as follows:
*               For each work scope task definition, find baselined labour requirement;
*               get its most recent update date in dwt_task_labour_summary table, if
*               not exists, use SYSDATE - aMonth; then for each completed task event
*               under this task definition, update ot insert data to dwt_task_labour_summary
*               table.
*
* Orig.Coder:   John Tang
* Recent Coder: N/A
* Recent Date:  Oct 15, 2007
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateTaskLabourSummary (
      an_Months            IN  NUMBER,
      on_Return            OUT typn_RetCode
   ) IS

   /* *** LOCAL VARIABLES *** */
   ld_RecentUpdate     dwt_task_labour_summary.revision_dt%TYPE;
   ln_TaskDnId         task_task.task_db_id%TYPE;
   ln_SchedManPwCt     task_labour_list.man_pwr_ct%TYPE;
   ln_SchedManHr       task_labour_list.work_perf_hr%TYPE;
   ln_ActualManPwrCt   dwt_task_labour_summary.actual_man_pwr_ct%TYPE;
   ln_ActualTotalManHr dwt_task_labour_summary.actual_total_man_hr%TYPE;
   ln_LabourSkillDbId  dwt_task_labour_summary.labour_skill_db_id%TYPE;
   ls_LabourSkillCd    dwt_task_labour_summary.labour_skill_cd%TYPE;


   /* *** CURSOR DECLARATIONS *** */
   /* find the Config Slot based Work Scope task definitions that are AVCT, SUPRSEDE OR OBSOLETE */
   CURSOR lcur_FindWorkScopeTaskDefn IS
      SELECT DISTINCT
         task_task.task_defn_db_id,
         task_task.task_defn_id,
         task_task.task_db_id,
         task_task.task_id,
         task_task.revision_ord,
         task_task.assmbl_db_id,
         task_task.assmbl_cd
      FROM
         task_task
      WHERE
        task_task.workscope_bool = 1 AND
        task_task.task_def_status_cd IN ('ACTV', 'SUPRSEDE', 'OBSOLETE')
        AND
        task_task.assmbl_db_id IS NOT NULL;

   lrec_FindWorkScopeTaskDefn lcur_FindWorkScopeTaskDefn%ROWTYPE;

   /* find all completed actual task events since passed in date and exclude part no. based task defn */
   CURSOR lcur_GetCompletedTaskLabours (
          cd_EventDt   evt_event.event_dt %TYPE,
          cn_TaskDbId  task_task.task_db_id %TYPE,
          cn_TaskId    task_task.task_id %TYPE
      ) IS
      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id,
         sched_stask.barcode_sdesc,
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_dt,
         evt_inv.assmbl_inv_no_db_id,
         evt_inv.assmbl_inv_no_id,
         inv_inv.inv_no_sdesc
      FROM
         task_task,
         sched_stask,
         evt_event,
         evt_inv,
         inv_inv
      WHERE
        task_task.task_db_id = cn_TaskDbId AND
        task_task.task_id    = cn_TaskId
        AND
        task_task.assmbl_db_id IS NOT NULL
        AND
        sched_stask.task_db_id = task_task.task_db_id AND
        sched_stask.task_id    = task_task.task_id AND
        sched_stask.rstat_cd   = 0
        AND
        evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id AND
        evt_event.event_status_cd = 'COMPLETE' AND
        evt_event.event_dt > cd_EventDt
        AND
        evt_inv.event_db_id = evt_event.event_db_id AND
        evt_inv.event_id    = evt_event.event_id AND
        evt_inv.assmbl_inv_no_db_id IS NOT NULL AND
        evt_inv.main_inv_bool = 1
        AND
        inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
        inv_inv.inv_no_id    = evt_inv.inv_no_id

      UNION

      /** Also include all actual tasks that are not recorded for a task_defn in dwt_task_labour_summary
          during past X months since SYS date */
      SELECT
         sched_stask.sched_db_id,
         sched_stask.sched_id,
         sched_stask.barcode_sdesc,
         evt_event.event_db_id,
         evt_event.event_id,
         evt_event.event_dt,
         evt_inv.assmbl_inv_no_db_id,
         evt_inv.assmbl_inv_no_id,
         inv_inv.inv_no_sdesc
      FROM
         task_task,
         sched_stask,
         evt_event,
         evt_inv,
         inv_inv
      WHERE
        task_task.task_db_id = cn_TaskDbId AND
        task_task.task_id    = cn_TaskId
        AND
        task_task.assmbl_db_id IS NOT NULL
        AND
        sched_stask.task_db_id = task_task.task_db_id AND
        sched_stask.task_id    = task_task.task_id    AND
        sched_stask.rstat_cd  = 0
        AND
        evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id AND
        evt_event.event_status_cd = 'COMPLETE' AND
        evt_event.event_dt > add_months(SYSDATE, -an_Months)
        AND
        evt_inv.event_db_id = evt_event.event_db_id AND
        evt_inv.event_id    = evt_event.event_id AND
        evt_inv.assmbl_inv_no_db_id IS NOT NULL AND
        evt_inv.main_inv_bool = 1
        AND
        inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
        inv_inv.inv_no_id    = evt_inv.inv_no_id
        AND
        NOT EXISTS (
        /** only need actual tasks that have not been recorded */
            SELECT
               1
            FROM
             dwt_task_labour_summary
            WHERE
             dwt_task_labour_summary.task_defn_db_id = cn_TaskDbId AND
             dwt_task_labour_summary.task_defn_id    = cn_TaskId
             AND
             dwt_task_labour_summary.sched_db_id = sched_stask.sched_db_id AND
             dwt_task_labour_summary.sched_id    = sched_stask.sched_id
        );

   lrec_GetCompletedTaskLabours lcur_GetCompletedTaskLabours%ROWTYPE;

   /* create a fully outer join between task_labour_list and sched_labour as
      planned skills v.s. acual completed skills
    */
   CURSOR lcur_GetTaskDefnEvtLabours (
          cd_SchedDbId   sched_stask.sched_db_id %TYPE,
          cn_SchedId     sched_stask.sched_id %TYPE
      ) IS
      /* get scheduled labour skills and actual labour skills */
        SELECT DISTINCT
        task_defn_labour_sched.task_db_id,
        task_defn_labour_sched.task_id,
        task_defn_labour_sched.labour_skill_db_id AS schedlbr_skill_db_id,
        task_defn_labour_sched.labour_skill_cd AS schedlbr_skill_cd,
        sched_labour.sched_db_id AS actual_task_db_id,
        sched_labour.sched_id AS actual_task_id,
        sched_labour.labour_skill_db_id AS actuallbr_db_id,
        sched_labour.labour_skill_cd AS actuallbr_cd
      FROM
         ( /** create a join of task_labour_list and sched_stask */
           SELECT
           task_labour_list.task_db_id,
           task_labour_list.task_id,
           task_labour_list.labour_skill_db_id,
           task_labour_list.labour_skill_cd,
           sched_stask.sched_db_id,
           sched_stask.sched_id
           FROM
              task_labour_list,
              sched_stask
           WHERE
              sched_stask.sched_db_id     = cd_SchedDbId AND
              sched_stask.sched_id        = cn_SchedId AND
              sched_stask.rstat_cd  = 0
              AND
              task_labour_list.task_db_id = sched_stask.task_db_id AND
              task_labour_list.task_id    = sched_stask.task_id
         )task_defn_labour_sched,
         sched_labour
      where
          sched_labour.sched_db_id (+)= task_defn_labour_sched.sched_db_id AND
          sched_labour.sched_id    (+)= task_defn_labour_sched.sched_id AND
          sched_labour.labour_skill_db_id (+)= task_defn_labour_sched.labour_skill_db_id AND
          sched_labour.labour_skill_cd    (+)= task_defn_labour_sched.labour_skill_cd AND
          sched_labour.labour_stage_cd (+)= 'COMPLETE'

      UNION

      SELECT DISTINCT
        task_defn_labour_sched.task_db_id,
        task_defn_labour_sched.task_id,
        task_defn_labour_sched.labour_skill_db_id AS schedlbr_skill_db_id,
        task_defn_labour_sched.labour_skill_cd AS schedlbr_skill_cd,
        sched_labour.sched_db_id AS actual_task_db_id,
        sched_labour.sched_id AS actual_task_id,
        sched_labour.labour_skill_db_id AS actuallbr_db_id,
        sched_labour.labour_skill_cd AS actuallbr_cd
      FROM
         ( /** create a join of task_labour_list and sched_stask */
           SELECT
           task_labour_list.task_db_id,
           task_labour_list.task_id,
           task_labour_list.labour_skill_db_id,
           task_labour_list.labour_skill_cd,
           sched_stask.sched_db_id,
           sched_stask.sched_id
           FROM
              task_labour_list,
              sched_stask
           WHERE
              sched_stask.sched_db_id     = cd_SchedDbId AND
              sched_stask.sched_id        = cn_SchedId AND
              task_labour_list.task_db_id = sched_stask.task_db_id AND
              task_labour_list.task_id    = sched_stask.task_id
         )task_defn_labour_sched,
         sched_labour
      WHERE
           sched_labour.sched_db_id = cd_SchedDbId AND
           sched_labour.sched_id    = cn_SchedId AND
           sched_labour.labour_stage_cd = 'COMPLETE' AND
           task_defn_labour_sched.sched_db_id        (+)= sched_labour.sched_db_id AND
           task_defn_labour_sched.sched_id           (+)= sched_labour.sched_id AND
           task_defn_labour_sched.labour_skill_db_id (+)= sched_labour.labour_skill_db_id AND
           task_defn_labour_sched.labour_skill_cd    (+)= sched_labour.labour_skill_cd;

   lrec_GetTaskDefnEvtLabours lcur_GetTaskDefnEvtLabours%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /** Get each work scope ACTV, SUPRSEDE or OBSOLETE task definition */
   FOR lrec_FindWorkScopeTaskDefn IN lcur_FindWorkScopeTaskDefn ()
   LOOP

      /** Get the most recent update */
      SELECT
         MAX(revision_dt)
      INTO
         ld_RecentUpdate
      FROM
         dwt_task_labour_summary
      WHERE
         dwt_task_labour_summary.task_db_id = lrec_FindWorkScopeTaskDefn.task_db_id AND
         dwt_task_labour_summary.task_id   = lrec_FindWorkScopeTaskDefn.task_id;

      /** If most recent update is null, get the the date past X months from now */
      IF ld_RecentUpdate IS NULL THEN
        ld_RecentUpdate:=add_months(SYSDATE, -an_Months);
      END IF;

      /** get all completed actual tasks after most recent update or
          the date past X months from now */
        FOR lrec_GetCompletedTaskLabours IN lcur_GetCompletedTaskLabours(
            ld_RecentUpdate,
            lrec_FindWorkScopeTaskDefn.task_db_id,
            lrec_FindWorkScopeTaskDefn.task_id)
        LOOP

            FOR lrec_GetTaskDefnEvtLabours IN lcur_GetTaskDefnEvtLabours(
              lrec_GetCompletedTaskLabours.event_db_id,
              lrec_GetCompletedTaskLabours.event_id)
            LOOP

                /** initalize the varlable to negative for later checking if it is updated */
                ln_ActualManPwrCt := -1;
                /** skill is not planned */
                IF lrec_GetTaskDefnEvtLabours.task_db_id IS NULL THEN
                   ln_SchedManPwCt := 0;
                   ln_SchedManHr := 0;
                ELSE
                    /** Get the scheduled man hours and scheduled numbers of men  for a skill*/
                    SELECT
                      task_labour_list.labour_skill_db_id,
                      task_labour_list.labour_skill_cd,
                      task_labour_list.man_pwr_ct,
                      (task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr) AS man_hr
                    INTO
                      ln_LabourSkillDbId,
                      ls_LabourSkillCd,
                      ln_SchedManPwCt,
                      ln_SchedManHr
                    FROM
                      task_labour_list
                    WHERE
                      task_labour_list.task_db_id         = lrec_FindWorkScopeTaskDefn.task_db_id AND
                      task_labour_list.task_id            = lrec_FindWorkScopeTaskDefn.task_id AND
                      task_labour_list.labour_skill_db_id = lrec_GetTaskDefnEvtLabours.schedlbr_skill_db_id AND
                      task_labour_list.labour_skill_cd    = lrec_GetTaskDefnEvtLabours.schedlbr_skill_cd;

                    /** planned skill is not actually used */
                    IF lrec_GetTaskDefnEvtLabours.actual_task_db_id IS NULL THEN
                       ln_ActualManPwrCt := 0;
                       ln_ActualTotalManHr := 0;
                    END IF;
                END IF;

                /** so far ln_ActualManPwrCt is still -1, which means planed skill is actually used
                then acquire the actual number of workers and hours of a skill  */
                IF ln_ActualManPwrCt = -1 THEN
                    SELECT
                       sched_labour.labour_skill_db_id,
                       sched_labour.labour_skill_cd,
                       COUNT(*),
                       NVL(SUM(tech_role.actual_hr),0)
                       + NVL(SUM(cert_role.actual_hr),0)
                       + NVL(SUM(insp_role.actual_hr),0)
                    INTO
                       ln_LabourSkillDbId,
                       ls_LabourSkillCd,
                       ln_ActualManPwrCt,
                       ln_ActualTotalManHr
                    FROM
                       sched_labour
                       INNER JOIN sched_labour_role tech_role ON
                          tech_role.labour_db_id = sched_labour.labour_db_id AND
                          tech_role.labour_id    = sched_labour.labour_id
                          AND
                          tech_role.labour_role_type_cd = 'TECH'
                       LEFT OUTER JOIN sched_labour_role cert_role ON
                          cert_role.labour_db_id = sched_labour.labour_db_id AND
                          cert_role.labour_id    = sched_labour.labour_id
                          AND
                          cert_role.labour_role_type_cd = 'CERT'
                       LEFT OUTER JOIN sched_labour_role insp_role ON
                          insp_role.labour_db_id = sched_labour.labour_db_id AND
                          insp_role.labour_id    = sched_labour.labour_id
                          AND
                          insp_role.labour_role_type_cd = 'INSP'
                    WHERE
                        sched_labour.sched_db_id = lrec_GetTaskDefnEvtLabours.actual_task_db_id AND
                        sched_labour.sched_id    = lrec_GetTaskDefnEvtLabours.actual_task_id AND
                        sched_labour.labour_skill_db_id = lrec_GetTaskDefnEvtLabours.actuallbr_db_id AND
                        sched_labour.labour_skill_cd    = lrec_GetTaskDefnEvtLabours.actuallbr_cd
                    GROUP BY labour_skill_db_id, labour_skill_cd;
                END IF;

               /** initalize this local variable for later determing if we should insert or update
                  strange behavior may happen when not initilized to null*/
               ln_TaskDnId :=NULL;

                /** Check if there exists a row with this pk in dwt_task_labour_summary */
                SELECT
                   count(*)
                INTO
                   ln_TaskDnId
                FROM
                   dwt_task_labour_summary
                WHERE
                   dwt_task_labour_summary.task_defn_db_id    = lrec_FindWorkScopeTaskDefn.task_defn_db_id AND
                   dwt_task_labour_summary.task_defn_id       = lrec_FindWorkScopeTaskDefn.task_defn_id AND
                   dwt_task_labour_summary.sched_db_id        = lrec_GetCompletedTaskLabours.sched_db_id AND
                   dwt_task_labour_summary.sched_id           = lrec_GetCompletedTaskLabours.sched_id AND
                   dwt_task_labour_summary.labour_skill_db_id = ln_LabourSkillDbId AND
                   dwt_task_labour_summary.labour_skill_cd    = ls_LabourSkillCd AND
                   dwt_task_labour_summary.task_db_id         = lrec_FindWorkScopeTaskDefn.task_db_id AND
                   dwt_task_labour_summary.task_id            = lrec_FindWorkScopeTaskDefn.task_id;

                /** no such row in dwt_task_labour_summary */
                IF ln_TaskDnId =0 THEN

                     /* insert a record into the status history */
                     INSERT INTO dwt_task_labour_summary (
                           task_defn_db_id,
                           task_defn_id,
                           sched_db_id,
                           sched_id,
                           labour_skill_db_id,
                           labour_skill_cd,
                           task_db_id,
                           task_id,
                           revision_ord,
                           barcode_sdesc,
                           complete_dt,
                           assmbl_db_id,
                           assmbl_cd,
                           assmbl_inv_no_db_id,
                           assmbl_inv_no_id,
                           assmbl_inv_no_sdesc,
                           sched_man_pwr_ct,
                           sched_man_hr,
                           actual_man_pwr_ct,
                           actual_total_man_hr)
                        VALUES (
                           lrec_FindWorkScopeTaskDefn.task_defn_db_id,
                           lrec_FindWorkScopeTaskDefn.task_defn_id,
                           lrec_GetCompletedTaskLabours.sched_db_id,
                           lrec_GetCompletedTaskLabours.sched_id,
                           ln_LabourSkillDbId,
                           ls_LabourSkillCd,
                           lrec_FindWorkScopeTaskDefn.task_db_id,
                           lrec_FindWorkScopeTaskDefn.task_id,
                           lrec_FindWorkScopeTaskDefn.revision_ord,
                           lrec_GetCompletedTaskLabours.barcode_sdesc,
                           lrec_GetCompletedTaskLabours.event_dt,
                           lrec_FindWorkScopeTaskDefn.assmbl_db_id,
                           lrec_FindWorkScopeTaskDefn.assmbl_cd,
                           lrec_GetCompletedTaskLabours.assmbl_inv_no_db_id,
                           lrec_GetCompletedTaskLabours.assmbl_inv_no_id,
                           lrec_GetCompletedTaskLabours.inv_no_sdesc,
                           ln_SchedManPwCt,
                           ln_SchedManHr,
                           ln_ActualManPwrCt,
                           ln_ActualTotalManHr );

                ELSE

                   /** UPDATE */
                    UPDATE dwt_task_labour_summary
                       SET
                           revision_ord    = lrec_FindWorkScopeTaskDefn.revision_ord,
                           barcode_sdesc   = lrec_GetCompletedTaskLabours.barcode_sdesc,
                           complete_dt     = lrec_GetCompletedTaskLabours.event_dt,
                           assmbl_db_id    = lrec_FindWorkScopeTaskDefn.assmbl_db_id,
                           assmbl_cd       = lrec_FindWorkScopeTaskDefn.assmbl_cd,
                           assmbl_inv_no_db_id = lrec_GetCompletedTaskLabours.assmbl_inv_no_db_id,
                           assmbl_inv_no_id    = lrec_GetCompletedTaskLabours.assmbl_inv_no_id,
                           assmbl_inv_no_sdesc = lrec_GetCompletedTaskLabours.inv_no_sdesc,
                           sched_man_pwr_ct    = ln_SchedManPwCt,
                           sched_man_hr        = ln_SchedManHr,
                           actual_man_pwr_ct   = ln_ActualManPwrCt,
                           actual_total_man_hr = ln_ActualTotalManHr
                     WHERE
                       dwt_task_labour_summary.task_defn_db_id     = lrec_FindWorkScopeTaskDefn.task_defn_db_id AND
                       dwt_task_labour_summary.task_defn_id        = lrec_FindWorkScopeTaskDefn.task_defn_id AND
                       dwt_task_labour_summary.sched_db_id         = lrec_GetCompletedTaskLabours.sched_db_id AND
                       dwt_task_labour_summary.sched_id            = lrec_GetCompletedTaskLabours.sched_id AND
                       dwt_task_labour_summary.labour_skill_db_id  = ln_LabourSkillDbId AND
                       dwt_task_labour_summary.labour_skill_cd     = ls_LabourSkillCd AND
                       dwt_task_labour_summary.task_db_id          = lrec_FindWorkScopeTaskDefn.task_db_id AND
                       dwt_task_labour_summary.task_id             = lrec_FindWorkScopeTaskDefn.task_id ;
                END IF;

           END LOOP;
        END LOOP;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdateTaskLabourSummary@@@'||SQLERRM);
     RETURN;
END UpdateTaskLabourSummary;

/********************************************************************************
*
* Procedure:    UpdatePartsAndToolsReadyBool
* Arguments:    an_TaskDbId    (long) - task database id
*               an_TaskId      (long) - task id
* Return:       ab_PartReady   (integer) - indicates part ready
*               ab_ToolReady   (integer) - indicates tool ready
*               on_Return      (long) - succss/failure of procedure
*
* Description: Updates the part and tool ready booleans on a task and its subtasks
*
* Orig.Coder:  L. Soh
* Recent Coder:
* Recent Date:  February 15, 2008
*
*********************************************************************************
*
* Copyright ? 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdatePartsAndToolsReadyBool(
           an_TaskDbId           IN sched_stask.task_db_id%TYPE,
           an_TaskId             IN sched_stask.task_id%TYPE,
           ab_PartReady          OUT INTEGER,
           ab_ToolReady          OUT INTEGER,
           on_Return             OUT typn_RetCode
   ) IS

   -- local variables
   lb_ChildPartReady   sched_stask.parts_ready_bool%TYPE;
   lb_ChildToolReady   sched_stask.tools_ready_bool%TYPE;
   lb_TempPartReady   sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady   sched_stask.tools_ready_bool%TYPE;
   ln_TempHasChildTasks NUMBER;

   -- Cursor: return all child tasks of given task
   CURSOR lcur_GetChildTasks (
         cn_SchedDbId sched_stask.task_db_id%TYPE,
         cn_SchedId   sched_stask.task_id%TYPE
      ) IS

      SELECT
         event_db_id, event_id
      FROM
         evt_event
      WHERE rstat_cd = 0
      START WITH
            evt_event.nh_event_db_id = cn_SchedDbId AND
            evt_event.nh_event_id    = cn_SchedId
      CONNECT BY
              evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
              evt_event.nh_event_id    = PRIOR evt_Event.event_id
      ORDER BY  
              event_db_id, event_id;

      lrec_GetChildTasks  lcur_GetChildTasks%rowtype;

   -- Cursor: return any install part requirement
   CURSOR lcur_SchedInstPart (
         cn_SchedDbId   sched_inst_part.sched_db_id%TYPE,
         cn_SchedId     sched_inst_part.sched_id%TYPE
      ) IS
      SELECT
         sched_inst_part.sched_db_id,
         sched_inst_part.sched_id
      FROM
         sched_inst_part
      WHERE
         sched_inst_part.sched_db_id   = cn_SchedDbId AND
         sched_inst_part.sched_id      = cn_SchedId;

         lrec_SchedInstPart lcur_SchedInstPart%ROWTYPE;

   -- Cursor: return any tool part requirement
   CURSOR lcur_EvtTool (
         cn_EventDbId   evt_tool.event_db_id%TYPE,
         cn_EventId     evt_tool.event_id%TYPE
      ) IS
      SELECT
         evt_tool.event_db_id,
         evt_tool.event_id
      FROM
         evt_tool
      WHERE
         evt_tool.event_db_id = cn_EventDbId AND
         evt_tool.event_id    = cn_EventId;

         lrec_EvtTool lcur_EvtTool%ROWTYPE;

BEGIN
    -- Initialize the return variable.
    on_Return := icn_NoProc;

    -- initialize local variables
    lb_ChildPartReady := 1;
    lb_ChildToolReady := 1;

    lb_TempPartReady := 1;
    lb_TempToolReady := 1;

    ln_TempHasChildTasks := 0;

    -- If task has sub tasks, we evaluate each sub tasks
    FOR lrec_GetChildTasks IN lcur_GetChildTasks( an_TaskDbId, an_TaskId )
    LOOP

        -- Set flag that we have child tasks
        ln_TempHasChildTasks := 1;

        -- Evaluate part/tool readiness for this sub task
        UpdatePartsAndToolsReadyBool(lrec_GetChildTasks.event_db_id,
                                    lrec_GetChildTasks.event_id,
                                    lb_TempPartReady,
                                    lb_TempToolReady,
                                    on_Return);

         -- Check if procedure is successful
         IF on_Return < 1 THEN
            RETURN;
         END IF;

        -- As long as one sub task has a part not ready, set flag
        IF lb_TempPartReady = 0 THEN
           lb_ChildPartReady := 0;
        END IF;

        -- As long as one sub task has a tool not ready, set flag
        IF lb_TempToolReady = 0 THEN
           lb_ChildToolReady := 0;
        END IF;

    END LOOP;

    -- Get install part requirement for this task
    OPEN  lcur_SchedInstPart( an_TaskDbId, an_TaskId );
    FETCH lcur_SchedInstPart INTO lrec_SchedInstPart;
    CLOSE lcur_SchedInstPart;

    -- Get tool requirement for this task
    OPEN  lcur_EvtTool( an_TaskDbId, an_TaskId );
    FETCH lcur_EvtTool INTO lrec_EvtTool;
    CLOSE lcur_EvtTool;

    -- If we have child tasks, have to take child task readiness into consideration
    IF ln_TempHasChildTasks = 1 THEN

       -- Set part ready to 1 if sub tasks are all part ready and
       -- current task has no install part requirement
       IF lb_ChildPartReady = 1 AND lrec_SchedInstPart.sched_db_id IS NULL THEN
          ab_PartReady := 1;
       ELSE
           ab_PartReady := 0;
       END IF;

       -- Set tool ready to 1 if sub tasks are all tool ready and
       -- current task has no tool requirement
       IF lb_ChildToolReady = 1 AND lrec_EvtTool.event_db_id IS NULL THEN
          ab_ToolReady := 1;
       ELSE
          ab_ToolReady := 0;
       END IF;

    -- If task has no child task
    ELSE

        -- if there is at least an install part requirement, part_ready is false
        IF lrec_SchedInstPart.sched_db_id IS NULL THEN
            ab_PartReady := 1;
        ELSE
            ab_PartReady := 0;
        END IF;

        -- if there is at least a tool part requirement, tool_ready is false
        IF lrec_EvtTool.event_db_id IS NULL THEN
            ab_ToolReady := 1;
        ELSE
            ab_ToolReady := 0;
        END IF;

    END IF;

    -- Update the task
    UPDATE sched_stask
    SET
        sched_stask.parts_ready_bool = ab_PartReady,
        sched_stask.tools_ready_bool = ab_ToolReady
    WHERE
          sched_stask.sched_db_id = an_TaskDbId AND
          sched_stask.sched_id    = an_TaskId   AND
          sched_stask.rstat_cd   = 0;

    -- Return success
    on_Return := icn_Success;

EXCEPTION
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        on_Return := icn_Error;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@UpdatePartsAndToolsReadyBool@@@'||SQLERRM);
        RETURN;
END UpdatePartsAndToolsReadyBool;

END SCHED_STASK_PKG;
/