--liquibase formatted sql

--changeSet OPER-12073:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PPC_PKG IS

   CURSOR lcur_PlanLocationList (
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
      SELECT
         ppc_loc.location_id
      FROM
         ppc_plan
         INNER JOIN ppc_loc ON ppc_loc.plan_id = ppc_plan.plan_id
      WHERE
         ppc_plan.plan_id = an_PpcId;
   lrec_PlanLocationList  lcur_PlanLocationList%ROWTYPE;

PROCEDURE deletePpcHrShiftPlan(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   FOR lrec_PlanLocationList IN lcur_PlanLocationList( an_PpcId )
   LOOP
     -- Delete: ppc_hr_lic
     DELETE ppc_hr_lic
     WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_hr_lic
               INNER JOIN ppc_hr_shift_plan ON ppc_hr_shift_plan.human_resource_shift_id = ppc_hr_lic.human_resource_shift_id
           WHERE
               ppc_hr_shift_plan.location_id    = lrec_PlanLocationList.Location_Id
       );

     -- Delete: This is a first stage in removing the plan's ppc_hr_slot entities.
     -- Here we removing records under location scope (referencing ppc_hr_shift_plan only)
     -- Other Plan's ppc_hr_slot entities will be removed under activity scope (see deletePpcLabour)
     DELETE ppc_hr_slot
     WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_hr_slot
               INNER JOIN ppc_hr_shift_plan ON ppc_hr_shift_plan.human_resource_shift_id = ppc_hr_slot.human_resource_shift_id

           WHERE
               ppc_hr_shift_plan.location_id = lrec_PlanLocationList.Location_Id
       );

     -- Delete: ppc_hr_shift_plan
     DELETE ppc_hr_shift_plan
     WHERE
          ppc_hr_shift_plan.location_id = lrec_PlanLocationList.Location_Id;
   END LOOP;
END deletePpcHrShiftPlan;

PROCEDURE deletePpcLabour(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   DELETE FROM ppc_hr_slot
   WHERE
      ( ppc_hr_slot.human_resource_slot_id )
      IN
      (  SELECT
            ppc_hr_slot.human_resource_slot_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id    = ppc_wp.work_package_id
            INNER JOIN ppc_labour ON ppc_labour.task_id                = ppc_activity.activity_id
            INNER JOIN ppc_labour_role ON ppc_labour_role.labour_id    =  ppc_labour.labour_id
            INNER JOIN ppc_hr_slot ON ppc_hr_slot.labour_role_id    = ppc_labour_role.labour_role_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   DELETE FROM ppc_labour_role
   WHERE
      ( ppc_labour_role.labour_role_id )
      IN
      (  SELECT
            ppc_labour_role.labour_role_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id    = ppc_wp.work_package_id
            INNER JOIN ppc_labour ON ppc_labour.task_id  = ppc_activity.activity_id
            INNER JOIN ppc_labour_role ON ppc_labour_role.labour_id    =  ppc_labour.labour_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   DELETE FROM ppc_labour
   WHERE
      ( ppc_labour.labour_id )
      IN
      (  SELECT
            ppc_labour.labour_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id    = ppc_wp.work_package_id
            INNER JOIN ppc_labour ON ppc_labour.task_id  = ppc_activity.activity_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
END deletePpcLabour;

PROCEDURE deletePpcPublish(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   DELETE FROM ppc_publish_failure
   WHERE
      ( ppc_publish_failure.failure_id )
      IN
      (  SELECT
            ppc_publish_failure.failure_id
         FROM
            ppc_wp
            INNER JOIN ppc_publish_failure ON ppc_publish_failure.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   DELETE FROM ppc_publish
   WHERE
      ( ppc_publish.work_package_id )
      IN
      (  SELECT
            ppc_wp.work_package_id
         FROM
            ppc_wp
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
END deletePpcPublish;

PROCEDURE deletePpcPlanningType(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   DELETE FROM ppc_planning_type_skill
   WHERE
      ( ppc_planning_type_skill.work_package_id,
        ppc_planning_type_skill.planning_type_db_id,
        ppc_planning_type_skill.planning_type_id,
        ppc_planning_type_skill.labour_skill_db_id,
        ppc_planning_type_skill.labour_skill_cd )
      IN
      (  SELECT
            ppc_planning_type_skill.work_package_id,
            ppc_planning_type_skill.planning_type_db_id,
            ppc_planning_type_skill.planning_type_id,
            ppc_planning_type_skill.labour_skill_db_id,
            ppc_planning_type_skill.labour_skill_cd
         FROM
            ppc_wp
            INNER JOIN ppc_planning_type_skill ON ppc_planning_type_skill.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   DELETE FROM ppc_planning_type
   WHERE
      ( ppc_planning_type.work_package_id,
        ppc_planning_type.planning_type_db_id,
        ppc_planning_type.planning_type_id)
      IN
      (  SELECT
            ppc_planning_type.work_package_id,
            ppc_planning_type.planning_type_db_id,
            ppc_planning_type.planning_type_id
         FROM
            ppc_wp
            INNER JOIN ppc_planning_type ON ppc_planning_type.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
END deletePpcPlanningType;

PROCEDURE deletePpcTaskDefn(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   DELETE FROM ppc_task_defn_map
   WHERE
      ( ppc_task_defn_map.task_definition_id,
        ppc_task_defn_map.task_id )
      IN
      (  SELECT
            ppc_task_defn_map.task_definition_id,
            ppc_task_defn_map.task_id
         FROM
            ppc_wp
            INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id = ppc_wp.work_package_id
            INNER JOIN ppc_task_defn_map ON ppc_task_defn_map.task_definition_id = ppc_task_defn.task_definition_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   DELETE FROM ppc_task_defn
   WHERE
      ( ppc_task_defn.task_definition_id )
      IN
      (  SELECT
            ppc_task_defn.task_definition_id
         FROM
            ppc_wp
            INNER JOIN ppc_task_defn ON ppc_task_defn.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
END deletePpcTaskDefn;

PROCEDURE deletePpcActivity(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN
   -- Delete ppc_mpc_template_task
   DELETE FROM ppc_mpc_template_task
   WHERE
      ( ppc_mpc_template_task.task_id )
      IN
      (
         SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
      
   -- Delete ppc_task_panel
   DELETE FROM ppc_task_panel
   WHERE
      ( ppc_task_panel.task_id )
      IN
      ( 
          SELECT
             ppc_activity.activity_id
          FROM
             ppc_wp
             INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
          WHERE
             ppc_wp.plan_id = an_PpcId                     
      );
      
   -- Delete ppc_task entities
   DELETE FROM ppc_task
   WHERE
      ( ppc_task.task_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
            INNER JOIN ppc_task ON ppc_task.task_id = ppc_activity.activity_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   -- Delete ppc_work_area entities
   DELETE FROM ppc_work_area_crew
   WHERE
      ( ppc_work_area_crew.work_area_id,
        ppc_work_area_crew.crew_id )
      IN
      (  SELECT
            ppc_work_area_crew.work_area_id,
            ppc_work_area_crew.crew_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
            INNER JOIN ppc_work_area_crew ON ppc_work_area_crew.work_area_id = ppc_activity.activity_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_WorkArea

         UNION

         SELECT
            ppc_work_area_crew.work_area_id,
            ppc_work_area_crew.crew_id
         FROM
            ppc_loc
            INNER JOIN ppc_crew ON ppc_crew.location_id = ppc_loc.location_id
            INNER JOIN ppc_work_area_crew ON ppc_work_area_crew.crew_id = ppc_crew.crew_id
         WHERE
            ppc_loc.plan_id = an_PpcId
      );

   DELETE FROM ppc_work_area_zone
   WHERE
      ( ppc_work_area_zone.work_area_zone_id )
      IN
      (  SELECT
            ppc_work_area_zone.work_area_zone_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
            INNER JOIN ppc_work_area_zone ON ppc_work_area_zone.work_area_id = ppc_activity.activity_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_WorkArea
      );

   DELETE FROM ppc_work_area
   WHERE
      ( ppc_work_area.work_area_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_WorkArea
      );

   -- Delete ppc_phase entities
   -- Remove nullable references to ppc_phase first
   UPDATE ppc_phase
   SET
      nr_phase_id = NULL,
      nr_start_milestone_id = NULL,
      nr_end_milestone_id = NULL
   WHERE
      ( ppc_phase.phase_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_Phase
      );

   UPDATE ppc_wp
   SET
      ppc_wp.nr_phase_id = NULL
   WHERE
      ppc_wp.plan_id = an_PpcId;

   DELETE FROM ppc_phase_class
   WHERE
      ( ppc_phase_class.phase_class_id )
      IN
      (  SELECT
            ppc_phase_class.phase_class_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
            INNER JOIN ppc_phase_class ON ppc_phase_class.phase_id = ppc_activity.activity_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_Phase
      );

   DELETE FROM ppc_phase
   WHERE
      ( ppc_phase.phase_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_Phase
      );

   -- Delete ppc_milestone entities
   DELETE FROM ppc_milestone_cond
   WHERE
      ( ppc_milestone_cond.milestone_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_Milestone
      );

   DELETE FROM ppc_milestone
   WHERE
      ( ppc_milestone.milestone_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
            AND
            ppc_activity.ppc_activity_type_db_id = 0 AND
            ppc_activity.ppc_activity_type_cd = icn_Milestone
      );

   -- Delete all dependencies under giving plan
   DELETE FROM ppc_dependency
   WHERE
      ( from_activity_id )
      IN
      (  SELECT
            activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );
   DELETE FROM ppc_dependency
   WHERE
      ( to_activity_id )
      IN
      (  SELECT
            activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

   -- Delete all activities under giving plan
   DELETE FROM ppc_activity
   WHERE
      ( ppc_activity.activity_id )
      IN
      (  SELECT
            ppc_activity.activity_id
         FROM
            ppc_wp
            INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
         WHERE
            ppc_wp.plan_id = an_PpcId
      );

END deletePpcActivity;

PROCEDURE deletePpcLoc(
      an_PpcId          IN ppc_plan.plan_id%TYPE
   ) IS
BEGIN

   DELETE FROM ppc_loc_capacity
   WHERE
      ( ppc_loc_capacity.location_capacity_id )
      IN
      (  SELECT
            ppc_loc_capacity.location_capacity_id
         FROM
            ppc_plan
            INNER JOIN ppc_loc ON ppc_loc.plan_id = ppc_plan.plan_id
            INNER JOIN ppc_loc_capacity ON ppc_loc_capacity.location_id = ppc_loc.location_id
         WHERE
            ppc_plan.plan_id = an_PpcId
      );

   DELETE FROM ppc_loc_exclude
   WHERE
      ppc_loc_exclude.plan_id = an_PpcId;

   DELETE FROM ppc_crew
   WHERE
      ( ppc_crew.crew_id )
      IN
      (  SELECT
            ppc_crew.crew_id
         FROM
            ppc_plan
            INNER JOIN ppc_loc ON ppc_loc.plan_id = ppc_plan.plan_id
            INNER JOIN ppc_crew ON ppc_crew.location_id = ppc_loc.location_id
         WHERE
            ppc_plan.plan_id = an_PpcId
      );

   UPDATE ppc_loc
   SET
      ppc_loc.nh_location_id = NULL
   WHERE
      ppc_loc.plan_id = an_PpcId;

   DELETE FROM ppc_loc
   WHERE
      ppc_loc.plan_id = an_PpcId;

END deletePpcLoc;

/********************************************************************************
*
* Procedure: DeletePlan
* Arguments:
*            an_PlanDbId production plan pk
*            an_PlanId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific plan
*
*
* Orig.Coder:     yvakulenko
* Recent Coder:
* Recent Date:    Oct, 22 2012
*
*********************************************************************************
*
* Copyright 2000-2012 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeletePlan (
      an_PpcId          IN ppc_plan.plan_id%TYPE,
      on_Return         OUT typn_RetCode
   ) IS


BEGIN
   on_Return := icn_NoProc;

   deletePpcPublish(an_PpcId); -- 1
   deletePpcPlanningType(an_PpcId); -- 2

   DELETE FROM ppc_opt_status -- 3
   WHERE
      ppc_opt_status.plan_id = an_PpcId;

   deletePpcLabour(an_PpcId); -- 4
   deletePpcHrShiftPlan(an_PpcId); -- 5
   deletePpcTaskDefn(an_PpcId); -- 6
   deletePpcActivity(an_PpcId); -- 7

   -- Delete all ppc_wp entities
   DELETE FROM ppc_wp -- 8
   WHERE
      ppc_wp.plan_id = an_PpcId;

	 -- If the plan is a template, remove the link to the template from the wp
   update ppc_wp set ppc_wp.template_id = null
   WHERE
      ppc_wp.template_id = an_PpcId;

   deletePpcLoc(an_PpcId); -- 9

   -- Delete all ppc_wp entities
   DELETE FROM ppc_hr  -- 10
   WHERE
      ppc_hr.plan_id = an_PpcId;

   DELETE FROM ppc_plan -- 11
   WHERE
      ppc_plan.plan_id = an_PpcId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeletePlan@@@'||SQLERRM);
      RETURN;

END DeletePlan;

/********************************************************************************
*
* Procedure: DeleteTask
* Arguments:
*            an_PpcTaskDbId ppc task pk
*            an_PpcTaskId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific ppc task
*
*
* Orig.Coder:     Jonathan R. Clarkin
* Recent Coder:   Joe Liu
* Recent Date:    Nov 10, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteTask (
      an_PpcTaskId        IN ppc_task.task_id%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

BEGIN
   on_Return := icn_NoProc;

   /* *** Delete a single Task from the PPC Work Package ****/

   -- Del: ppc_publish_failure
   DELETE FROM ppc_publish_failure
   WHERE
      ppc_publish_failure.task_id = an_PpcTaskId;
   -- Del: ppc_hr_slot
   DELETE FROM ppc_hr_slot
   WHERE
      ( human_resource_slot_id )
      IN
      (  SELECT human_resource_slot_id
         FROM
            ppc_hr_slot
            INNER JOIN ppc_labour_role ON
               ppc_labour_role.labour_role_id = ppc_hr_slot.labour_role_id
            INNER JOIN ppc_labour ON
               ppc_labour.labour_id = ppc_labour_role.labour_id
         WHERE
            ppc_labour.task_id = an_PpcTaskId
      );

   -- Del: ppc_labour_role
   DELETE FROM ppc_labour_role
   WHERE
      (  labour_role_id )
      IN
      (  SELECT labour_role_id
         FROM
            ppc_labour_role
            INNER JOIN ppc_labour ON
               ppc_labour.labour_id = ppc_labour_role.labour_id
         WHERE
            ppc_labour.task_id = an_PpcTaskId
      );

   -- Del: ppc_labour
   DELETE FROM ppc_labour
   WHERE
      ppc_labour.task_id = an_PpcTaskId;

   -- Del: ppc_task_defn_map
   DELETE FROM ppc_task_defn_map
   WHERE
      ppc_task_defn_map.task_id = an_PpcTaskId;

   -- Del: ppc_mpc_template_task
   DELETE FROM ppc_mpc_template_task
   WHERE
      ppc_mpc_template_task.task_id = an_PpcTaskId;
      
   -- Del: ppc_task_panel
   DELETE FROM ppc_task_panel
   WHERE
      ppc_task_panel.task_id = an_PpcTaskId;
      
   -- Del: ppc_task
   DELETE FROM ppc_task
   WHERE
      ppc_task.task_id = an_PpcTaskId;

   -- Del: ppc_dependency
   DELETE FROM ppc_dependency
   WHERE
      ppc_dependency.from_activity_id = an_PpcTaskId;
   DELETE FROM ppc_dependency
   WHERE
      ppc_dependency.to_activity_id = an_PpcTaskId;

   -- Del: ppc_activity
   DELETE FROM ppc_activity
   WHERE
      ppc_activity.activity_id = an_PpcTaskId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteTask@@@'||SQLERRM);
      RETURN;

END DeleteTask;

/********************************************************************************
*
* Procedure: DeleteWP
* Arguments:
*            an_PpcWpDbId ppc work package pk
*            an_PpcWpId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific ppc work package
*
*
* Orig.Coder:     Jonathan R. Clarkin
* Recent Coder:
* Recent Date:    April 28, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteWp (
      an_WpId          IN ppc_wp.work_package_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   /* *** DECLARE CURSORS *** */

   CURSOR lcur_TaskList (
      an_WpId          ppc_wp.work_package_id%TYPE
   ) IS
      SELECT
         ppc_task.task_id
      FROM
         ppc_task
         INNER JOIN ppc_activity ON
            ppc_activity.activity_id = ppc_task.task_id
      WHERE
         ppc_activity.work_package_id = an_WpId;
   lrec_TaskList  lcur_TaskList%ROWTYPE;

BEGIN

   on_Return := icn_NoProc;

   /* *** Delete all the Tasks of the Work Package ****/
   FOR lrec_TaskList IN lcur_TaskList( an_WpId )
   LOOP
      DeleteTask( lrec_TaskList.task_id, on_Return );
   END LOOP;

   /* *** Delete all the Work Package data ****/

   -- Clear: ppc_phase Nullable FKs
   UPDATE ppc_phase
   SET
      nr_phase_id = NULL,
      nr_start_milestone_id = NULL,
      nr_end_milestone_id = NULL
   WHERE
      ( phase_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   -- Clear: ppc_wp Nullable FKs
   UPDATE ppc_wp
   SET
      nr_phase_id = NULL
   WHERE
      work_package_id = an_WpId;

   -- Del: ppc_phase_class
   DELETE ppc_phase_class
   WHERE
      ( phase_class_id )
      IN
      (  SELECT phase_class_id
         FROM
            ppc_phase_class
            INNER JOIN ppc_activity ON
               ppc_activity.activity_id = ppc_phase_class.phase_id
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   -- Del: ppc_phase
   DELETE ppc_phase
   WHERE
      ( phase_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   -- Del: ppc_work_area_zone
   DELETE ppc_work_area_zone
   WHERE
      ( work_area_zone_id )
      IN
      (  SELECT work_area_zone_id
         FROM
            ppc_work_area_zone
            INNER JOIN ppc_activity ON
               ppc_activity.activity_id = ppc_work_area_zone.work_area_id
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
    -- Del: ppc_work_area_crew
    DELETE ppc_work_area_crew
    WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_work_area_crew
           INNER JOIN ppc_activity ON
               ppc_activity.activity_id    = ppc_work_area_crew.work_area_id
           WHERE
               ppc_activity.work_package_id    = an_WpId
       );
   -- Del: ppc_work_area
   DELETE ppc_work_area
   WHERE
      ( work_area_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );

   -- Del: ppc_milestone_cond
   DELETE ppc_milestone_cond
   WHERE
      ( milestone_id )
      IN
      (  SELECT milestone_id
         FROM
            ppc_milestone_cond
            INNER JOIN ppc_activity ON
               ppc_activity.activity_id = ppc_milestone_cond.milestone_id
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   -- Del: ppc_milestone
   DELETE ppc_milestone
   WHERE
      ( milestone_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );

   -- Del: ppc_task_defn_map
   DELETE ppc_task_defn_map
   WHERE
      ( task_definition_id )
      IN
      (
        SELECT
            task_definition_id
        FROM
            ppc_task_defn
        WHERE
            ppc_task_defn.work_package_id    = an_WpId
       );

   -- Del: ppc_task_defn
   DELETE ppc_task_defn
   WHERE
        ppc_task_defn.work_package_id    = an_WpId;

   -- Del: ppc_dependency
   DELETE FROM ppc_dependency
   WHERE
      ( from_activity_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   DELETE FROM ppc_dependency
   WHERE
      ( to_activity_id )
      IN
      (  SELECT activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.work_package_id = an_WpId
      );
   -- Del: ppc_activity
   DELETE FROM ppc_activity
   WHERE
      ppc_activity.work_package_id = an_WpId;

   -- Del: ppc_publish_failure
   DELETE FROM ppc_publish_failure
   WHERE
      ppc_publish_failure.work_package_id = an_WpId;
   -- Del: ppc_publish
   DELETE FROM ppc_publish
   WHERE
      ppc_publish.work_package_id = an_WpId;

   -- Del: ppc_planning_skill
   DELETE FROM ppc_planning_type_skill
   WHERE
      ppc_planning_type_skill.work_package_id = an_WpId;
   -- Del: ppc_planning_type
   DELETE FROM ppc_planning_type
   WHERE
      ppc_planning_type.work_package_id = an_WpId;

   -- Del: ppc_wp
   DELETE FROM ppc_wp
   WHERE
      ppc_wp.work_package_id = an_WpId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteWp@@@'||SQLERRM);
      RETURN;

END DeleteWp;

/********************************************************************************
*
* Procedure: DeleteLocation
* Arguments:
*            an_PpcLocDbId ppc location pk
*            an_PpcLocId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific ppc location
*
*
* Orig.Coder:     Jonathan R. Clarkin
* Recent Coder:   Edo
* Recent Date:    November 10,2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteLocation (
      an_LocId      IN ppc_loc.location_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   ln_LocId                  ppc_loc.location_id%TYPE;

   /* *** DECLARE CURSORS *** */

   CURSOR lcur_LocationList (
      an_LocId          ppc_loc.location_id%TYPE
   ) IS
      SELECT
         ppc_loc.location_id
      FROM
         ppc_loc
      START WITH
         ppc_loc.location_id = an_LocId
      CONNECT BY
         ppc_loc.nh_location_id = PRIOR ppc_loc.location_id
      ORDER BY LEVEL DESC;
   lrec_LocationList  lcur_LocationList%ROWTYPE;

   CURSOR lcur_WpList (
      an_LocId          ppc_loc.location_id%TYPE
   ) IS
      SELECT
         ppc_wp.work_package_id
      FROM
         ppc_wp
      WHERE
         ppc_wp.location_id = an_LocId;
   lrec_WpList  lcur_WpList%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;

   FOR lrec_LocationList IN lcur_LocationList( an_LocId )
   LOOP
      ln_LocId := lrec_LocationList.location_id;

     -- Del: ppc_hr_lic
     DELETE ppc_hr_lic
     WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_hr_shift_plan
           WHERE
               ppc_hr_shift_plan.location_id    = ln_LocId
               AND
               ppc_hr_lic.human_resource_shift_id  = ppc_hr_shift_plan.human_resource_shift_id
       );

     -- Del: ppc_hr_shift_plan
     DELETE ppc_hr_shift_plan
     WHERE
          ppc_hr_shift_plan.location_id    = ln_LocId;
   END LOOP;

   /* *** Delete all the children locations and this location ****/
   FOR lrec_LocationList IN lcur_LocationList( an_LocId )
   LOOP
      ln_LocId := lrec_LocationList.location_id;

      /* *** Delete all the Work Packages of the Location ****/
      FOR lrec_WpList IN lcur_WpList( ln_LocId )
      LOOP
         DeleteWp( lrec_WpList.work_package_id, on_Return );
      END LOOP;


     /* *** Delete all the Location data ****/
      -- Del: ppc_loc_capacity
      DELETE ppc_loc_capacity
      WHERE
         ppc_loc_capacity.location_id = ln_LocId;
      -- Del: ppc_crew
      DELETE ppc_crew
      WHERE
         ppc_crew.location_id = ln_LocId;
      -- Del: ppc_loc
      DELETE ppc_loc
      WHERE
         ppc_loc.location_id = ln_LocId;

   END LOOP;

    -- Del: ppc_hr
    DELETE ppc_hr
    WHERE
       (
          SELECT COUNT(1)
          FROM
             ppc_hr_shift_plan
             WHERE
             ppc_hr_shift_plan.human_resource_id = ppc_hr.human_resource_id

       ) = 0;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteLocation@@@'||SQLERRM);
      RETURN;

END DeleteLocation;



/********************************************************************************
*
* Procedure: UpdateLabourRoleHr
* Arguments:
*            an_LabourRoleDbId   labour role key
*            an_LabourRoleId     -- // --
*            an_HrDbId           hr key
*            an_HrId             -- // --
*
* Return:
*            on_RoleStatusDbId   role status key (if COMPLETE or CANCEL)
*            on_RoleStatusCd     -- // --
*            on_Return           1 if record updated, otherwise 0
*
* Description: This procedure updates the hr key in the sched_labour_role_status
*              table provided the hr key has changed and the status is neither
*              COMPLETE or CANCEL.
*
* Orig.Coder:     ahogan
* Recent Coder:
* Recent Date:    May 05, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE UpdateLabourRoleHr (
   an_LabourRoleDbId   IN    sched_labour_role_status.labour_role_db_id%TYPE,
   an_LabourRoleId     IN    sched_labour_role_status.labour_role_id%TYPE,
   an_HrDbId           IN    sched_labour_role_status.hr_db_id%TYPE,
   an_HrId             IN    sched_labour_role_status.hr_id%TYPE,
   on_RoleStatusDbId   OUT   sched_labour_role_status.labour_role_status_db_id%TYPE,
   on_RoleStatusCd     OUT   sched_labour_role_status.labour_role_status_cd%TYPE,
   on_Return           OUT   typn_RetCode
)
IS
   ln_StatusDbId             sched_labour_role_status.status_db_id%TYPE;
   ln_StatusId               sched_labour_role_status.status_id%TYPE;
   ln_StatusOrd              sched_labour_role_status.status_ord%TYPE;
   ln_LabourRoleStatusDbId   sched_labour_role_status.labour_role_status_db_id%TYPE;
   ln_LabourRoleStatusCd     sched_labour_role_status.labour_role_status_cd%TYPE;
   ln_HrDbId                 sched_labour_role_status.hr_db_id%TYPE;
   ln_HrId                   sched_labour_role_status.hr_id%TYPE;

BEGIN

   on_Return := icn_False;

   -- get current sched_labour_role_status
   SELECT
      sched_labour_role_status.status_db_id,
      sched_labour_role_status.status_id,
      sched_labour_role_status.status_ord,
      sched_labour_role_status.labour_role_status_db_id,
      sched_labour_role_status.labour_role_status_cd,
      sched_labour_role_status.hr_db_id,
      sched_labour_role_status.hr_id
   INTO
      ln_StatusDbId,
      ln_StatusId,
      ln_StatusOrd,
      ln_LabourRoleStatusDbId,
      ln_LabourRoleStatusCd,
      ln_HrDbId,
      ln_HrId
   FROM
      sched_labour_role
   INNER JOIN sched_labour ON
      sched_labour.labour_db_id = sched_labour_role.labour_db_id AND
      sched_labour.labour_id    = sched_labour_role.labour_id
   INNER JOIN sched_labour_role_status ON
      sched_labour_role_status.labour_role_db_id = sched_labour_role.labour_role_db_id AND
      sched_labour_role_status.labour_role_id    = sched_labour_role.labour_role_id AND
      sched_labour_role_status.status_ord        = sched_labour.current_status_ord
   WHERE
      sched_labour_role.labour_role_db_id = an_LabourRoleDbId AND
      sched_labour_role.labour_role_id    = an_LabourRoleId;

   -- check if hr key is different, only update the hr if it has changed
   IF nvl(ln_HrDbId, -1) != nvl(an_HrDbId,-1) OR nvl(ln_HrId,-1) != nvl(an_HrId,-1) THEN

      IF ln_LabourRoleStatusDbId=0 THEN
         IF ln_LabourRoleStatusCd='COMPLETE' OR ln_LabourRoleStatusCd='CANCEL' THEN
            -- if complete or cancel then return the status key
            on_RoleStatusDbId := ln_LabourRoleStatusDbId;
            on_RoleStatusCd   := ln_LabourRoleStatusCd;

         ELSE
            -- otherwise, update the hr key for the current sched_labour_role_status
            UPDATE
               sched_labour_role_status
            SET
               hr_db_id = an_HrDbId,
               hr_id    = an_HrId
            WHERE
               status_db_id = ln_StatusDbId AND
               status_id    = ln_StatusId AND
               status_ord   = ln_StatusOrd;

            on_Return := icn_True;

         END IF;
      END IF;
   END IF;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
      -- no data found
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('PPC-99998','ppc_pkg@@@UpdateLabourRoleHr@@@'||SQLERRM);
      RETURN;

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@UpdateLabourRoleHr@@@'||SQLERRM);
      RETURN;

END UpdateLabourRoleHr;

/********************************************************************************
*
* Procedure: DeleteTaskDefinition
* Arguments:
*            an_TaskDefnDbId ppc task definition pk
*            an_TaskDefnId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific ppc task definition
*
*
* Orig.Coder:     Elise Do
* Recent Coder:
* Recent Date:    July 7, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteTaskDefinition (
      an_TaskDefnId      IN ppc_task_defn.task_definition_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   /* Determine if there are any tasks associate with this task definition only */
   CURSOR lcur_TaskList  IS
      SELECT
         ppc_task.task_id
      FROM
         ppc_task
         INNER JOIN ppc_task_defn_map ON
               ppc_task_defn_map.task_id    = ppc_task.task_id
      WHERE
         ppc_task_defn_map.task_definition_id    = an_TaskDefnId
         AND
         ( ppc_task.task_id )
         NOT IN
         (
             SELECT
                 task_id
             FROM
                 ppc_task_defn_map
             WHERE
                 NOT
                 (
                    ppc_task_defn_map.task_definition_id    = an_TaskDefnId
                 )
                 AND
                 (
                    ppc_task_defn_map.task_id    = ppc_task.task_id
                 )
         );
 lrec_TaskList  lcur_TaskList%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;

   OPEN lcur_TaskList;

    /* Del: ppc_task_defn_map */
   DELETE ppc_task_defn_map
   WHERE
        task_definition_id    = an_TaskDefnId;

   LOOP
        FETCH lcur_TaskList INTO lrec_TaskList;
        DeleteTask( lrec_TaskList.task_id, on_Return );
        EXIT WHEN lcur_TaskList%NOTFOUND;
   END LOOP;

   /* Del: ppc_task_defn */
   DELETE ppc_task_defn
   WHERE
        task_definition_id    = an_TaskDefnId;

   CLOSE lcur_TaskList;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteTaskDefinition@@@'||SQLERRM);
      RETURN;

END DeleteTaskDefinition;


/********************************************************************************
*
* Procedure: DeleteLabour
* Arguments:
*            an_PpcWpDbId Work packge pk
*            an_PpcWpId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific work package
*
*
* Orig.Coder:     Elise Do
* Recent Coder:
* Recent Date:    August 15, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteLabour (
      an_PpcWpId         IN ppc_wp.work_package_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

BEGIN

   on_Return := icn_NoProc;

   -- Del: ppc_hr_slot
   DELETE FROM
   (
      SELECT
         ppc_hr_slot.human_resource_slot_id
      FROM
         ppc_hr_slot
         INNER JOIN ppc_labour_role ON
               ppc_hr_slot.labour_role_id    = ppc_labour_role.labour_role_id
         INNER JOIN ppc_labour ON
               ppc_labour.labour_id    = ppc_labour_role.labour_id
         INNER JOIN ppc_task ON
               ppc_task.task_id    = ppc_labour.task_id
         INNER JOIN ppc_activity ON
               ppc_activity.activity_id    = ppc_task.task_id
      WHERE
         ppc_activity.work_package_id    = an_PpcWpId
    );

    --Del: ppc_labour_role
    DELETE FROM
    (
       SELECT
           ppc_labour_role.labour_role_id
       FROM
           ppc_labour_role
           INNER JOIN ppc_labour ON
                 ppc_labour.labour_id    = ppc_labour_role.labour_id
           INNER JOIN ppc_task ON
                 ppc_task.task_id    = ppc_labour.task_id
           INNER JOIN ppc_activity ON
                 ppc_activity.activity_id    = ppc_task.task_id
       WHERE
           ppc_activity.work_package_id    = an_PpcWpId
    );

    --Del: ppc_labour
    DELETE FROM
    (
       SELECT
           ppc_labour.labour_id
       FROM
           ppc_labour
           INNER JOIN ppc_task ON
                 ppc_task.task_id    = ppc_labour.task_id
           INNER JOIN ppc_activity ON
                 ppc_activity.activity_id    = ppc_task.task_id
       WHERE
           ppc_activity.work_package_id    = an_PpcWpId
    );

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteLabour@@@'||SQLERRM);
      RETURN;

END DeleteLabour;

/********************************************************************************
*
* Procedure: DeleteCapacityPattern
* Arguments:
*            an_PpcCapacityDbId ppc LOC_CAP pk
*            an_PpcCapacityId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure deletes all data related to a specific ppc capacity pattern
*
*
* Orig.Coder:     Edo
* Recent Coder:   Edo
* Recent Date:    November 10, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE DeleteCapacityPattern (
      an_PpcCapacityId       IN ppc_loc_capacity.location_capacity_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

BEGIN
   on_Return := icn_NoProc;

   -- Update Slots first and set NULL to ppc HR shift if it belongs
   --- to the Capacity pattern that will be deleted:
   UPDATE
    (
       SELECT ppc_hr_slot.human_resource_shift_id
       FROM ppc_hr_slot
            INNER JOIN ppc_hr_shift_plan ON
            ppc_hr_shift_plan.human_resource_shift_id = ppc_hr_slot.human_resource_shift_id
       WHERE
        ppc_hr_shift_plan.capacity_id = an_PpcCapacityId
    )
    SET
        human_resource_shift_id = null;

   -- Del: ppc_hr_lic
   DELETE ppc_hr_lic
   WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_loc_capacity
               INNER JOIN ppc_hr_shift_plan ON
                     ppc_hr_shift_plan.capacity_id    = ppc_loc_capacity.location_capacity_id
           WHERE
               ppc_loc_capacity.location_capacity_id = an_PpcCapacityId
               AND
               ppc_hr_lic.human_resource_shift_id = ppc_hr_shift_plan.human_resource_shift_id
       );

   -- Del: ppc_hr_shift_plan
   DELETE ppc_hr_shift_plan
   WHERE
      ppc_hr_shift_plan.capacity_id    = an_PpcCapacityId;

    -- Del: ppc_hr
    DELETE ppc_hr
    WHERE
       (SELECT COUNT(1)
          FROM
             ppc_hr_shift_plan
             WHERE
             ppc_hr_shift_plan.human_resource_id = ppc_hr.human_resource_id

       ) = 0;

   -- Del: ppc_loc_cap
   DELETE ppc_loc_capacity
   WHERE
        ppc_loc_capacity.location_capacity_id  = an_PpcCapacityId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@DeleteCapacityPattern@@@'||SQLERRM);
      RETURN;

END DeleteCapacityPattern;


/********************************************************************************
*
* Procedure: RefreshPlanDates
* Arguments:
*            an_PpcDbId PPC PLAN pk
*            an_PpcId   -- // --
*
* Return:
*            on_Return         1 is success
*
* Description: This procedure refreshes work package, task and slot dates
*
* Orig.Coder:     Edo
* Recent Coder:   Edo
* Recent Date:    October 25, 2010
*
*********************************************************************************
*
* Copyright 2000-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE RefreshPlanDates (
      an_PpcId         IN ppc_plan.plan_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

BEGIN

     on_Return := icn_NoProc;

     -- update slots: ppc_hr_slot
     UPDATE
      (
            SELECT
                 ppc_labour_role.start_dt,
                 ppc_labour_role.end_dt,
                 ppc_labour_role.labour_role_id   AS role_id
            FROM
                 ppc_wp
                 INNER JOIN ppc_activity ON
                      ppc_activity.work_package_id    = ppc_wp.work_package_id
                 INNER JOIN ppc_task ON
                      ppc_task.task_id    = ppc_activity.activity_id
                 INNER JOIN ppc_labour ON
                      ppc_labour.task_id    = ppc_task.task_id
                 INNER JOIN ppc_labour_role ON
                      ppc_labour_role.labour_id    = ppc_labour.labour_id
             WHERE
                 ppc_wp.plan_id    = an_PpcId
                 AND
                 ppc_wp.excluded_bool = 0
      )
     SET
        ( start_dt, end_dt ) = (
             SELECT
                  MIN( ppc_hr_slot.start_dt ),
                  MAX( ppc_hr_slot.end_dt )
             FROM
                  ppc_hr_slot
             WHERE
                  ppc_hr_slot.labour_role_id    = role_id
        );

     -- update task
     UPDATE
      (
            SELECT
                 ppc_task.start_dt,
                 ppc_task.end_dt,
                 ppc_task.task_id AS task_id
            FROM
                 ppc_wp
                 INNER JOIN ppc_activity ON
                      ppc_activity.work_package_id    = ppc_wp.work_package_id
                 INNER JOIN ppc_task ON
                      ppc_task.task_id    = ppc_activity.activity_id
             WHERE
                 ppc_wp.plan_id    = an_PpcId
                 AND
                 ppc_wp.excluded_bool = 0
      )
     SET
        ( start_dt, end_dt ) = (
             SELECT
                  MIN( ppc_labour_role.start_dt ),
                  MAX( ppc_labour_role.end_dt )
             FROM
                 ppc_labour
                 INNER JOIN ppc_labour_role ON
                      ppc_labour_role.labour_id    = ppc_labour.labour_id
             WHERE
                  ppc_labour.task_id    = task_id
        );

    -- update Work Packages
    UPDATE
        (
           SELECT
               ppc_wp.start_dt,
               ppc_wp.work_package_id AS wp_id
           FROM
               ppc_wp
           WHERE
               ppc_wp.plan_id    = an_PpcId
               AND
               ppc_wp.excluded_bool = 0
        )
    SET
       start_dt = (
                     SELECT
                         MIN( ppc_task.start_dt ) AS ealiest_start_dt
                     FROM
                         ppc_activity
                         INNER JOIN ppc_task ON
                              ppc_task.task_id    = ppc_activity.activity_id
                     WHERE
                         ppc_activity.work_package_id    = wp_id
                  );

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@RefreshPlanDates@@@'||SQLERRM);
      RETURN;

END RefreshPlanDates;

END PPC_PKG;
/