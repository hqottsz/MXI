--liquibase formatted sql


--changeSet OPER-3622:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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