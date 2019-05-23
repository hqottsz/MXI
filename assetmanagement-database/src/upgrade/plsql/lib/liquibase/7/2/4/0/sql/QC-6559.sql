--liquibase formatted sql


--changeSet QC-6559:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PPC_PKG IS
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

PROCEDURE DeletePlan (
      an_PpcDbId        IN ppc_plan.ppc_db_id%TYPE,
      an_PpcId          IN ppc_plan.ppc_id%TYPE,
      on_Return         OUT typn_RetCode
   ) IS

   /* *** DECLARE CURSORS *** */
   CURSOR lcur_RootLocationList IS
      SELECT
         ppc_loc.ppc_loc_db_id,
         ppc_loc.ppc_loc_id
      FROM
         ppc_loc
      WHERE
         ppc_loc.ppc_db_id = an_PpcDbId AND
         ppc_loc.ppc_id = an_PpcId
         AND
         ppc_loc.nh_ppc_loc_db_id IS NULL AND
         ppc_loc.nh_ppc_loc_id IS NULL;
      lrec_RootLocationList  lcur_RootLocationList%ROWTYPE;
BEGIN
   on_Return := icn_NoProc;

   FOR lrec_RootLocationList IN lcur_RootLocationList()
   LOOP
      /*** Delete all the Locations of the Plan, starting at current the Root ***/
      DeleteLocation(lrec_RootLocationList.ppc_loc_db_id,
                     lrec_RootLocationList.ppc_loc_id,
                     on_Return );
   END LOOP;



   /* *** Delete all Plan level data ****/

   -- Del: ppc_hr
   DELETE ppc_hr
   WHERE
      ppc_hr.ppc_db_id = an_PpcDbId AND
      ppc_hr.ppc_id    = an_PpcId;

   -- Del: ppc_loc_exclude
   DELETE FROM ppc_loc_exclude
   WHERE
      ppc_loc_exclude.ppc_db_id = an_PpcDbId AND
      ppc_loc_exclude.ppc_id = an_PpcId;
   -- Del: ppc_opt_status
   DELETE FROM ppc_opt_status
   WHERE
      ppc_opt_status.ppc_db_id = an_PpcDbId AND
      ppc_opt_status.ppc_id = an_PpcId;
   -- Del: ppc_plan
   DELETE FROM ppc_plan
   WHERE
      ppc_plan.ppc_db_id = an_PpcDbId AND
      ppc_plan.ppc_id = an_PpcId;

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
      an_PpcTaskDbId      IN ppc_task.ppc_task_db_id%TYPE,
      an_PpcTaskId        IN ppc_task.ppc_task_id%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

BEGIN
   on_Return := icn_NoProc;

   /* *** Delete a single Task from the PPC Work Package ****/

   -- Del: ppc_publish_failure
   DELETE FROM ppc_publish_failure
   WHERE
      ppc_publish_failure.ppc_task_db_id = an_PpcTaskDbId AND
      ppc_publish_failure.ppc_task_id = an_PpcTaskId;
   -- Del: ppc_hr_slot
   DELETE FROM ppc_hr_slot
   WHERE
      ( ppc_hr_slot_db_id, ppc_hr_slot_id )
      IN
      (  SELECT ppc_hr_slot_db_id, ppc_hr_slot_id
         FROM
            ppc_hr_slot
            INNER JOIN ppc_labour_role ON
               ppc_labour_role.labour_role_db_id = ppc_hr_slot.labour_role_db_id AND
               ppc_labour_role.labour_role_id = ppc_hr_slot.labour_role_id
            INNER JOIN ppc_labour ON
               ppc_labour.labour_db_id = ppc_labour_role.labour_db_id AND
               ppc_labour.labour_id = ppc_labour_role.labour_id
         WHERE
            ppc_labour.ppc_task_db_id = an_PpcTaskDbId AND
            ppc_labour.ppc_task_id = an_PpcTaskId
      );

   -- Del: ppc_labour_role
   DELETE FROM ppc_labour_role
   WHERE
      (  labour_role_db_id, labour_role_id )
      IN
      (  SELECT labour_role_db_id, labour_role_id
         FROM
            ppc_labour_role
            INNER JOIN ppc_labour ON
               ppc_labour.labour_db_id = ppc_labour_role.labour_db_id AND
               ppc_labour.labour_id = ppc_labour_role.labour_id
         WHERE
            ppc_labour.ppc_task_db_id = an_PpcTaskDbId AND
            ppc_labour.ppc_task_id = an_PpcTaskId
      );

   -- Del: ppc_labour
   DELETE FROM ppc_labour
   WHERE
      ppc_labour.ppc_task_db_id = an_PpcTaskDbId AND
      ppc_labour.ppc_task_id = an_PpcTaskId;

   -- Del: ppc_task_defn_map
   DELETE FROM ppc_task_defn_map
   WHERE
      ppc_task_defn_map.ppc_task_db_id = an_PpcTaskDbId AND
      ppc_task_defn_map.ppc_task_id = an_PpcTaskId;

   -- Del: ppc_task
   DELETE FROM ppc_task
   WHERE
      ppc_task.ppc_task_db_id = an_PpcTaskDbId AND
      ppc_task.ppc_task_id = an_PpcTaskId;

   -- Del: ppc_dependency
   DELETE FROM ppc_dependency
   WHERE
      ppc_dependency.from_activity_db_id = an_PpcTaskDbId AND
      ppc_dependency.from_activity_id = an_PpcTaskId;
   DELETE FROM ppc_dependency
   WHERE
      ppc_dependency.to_activity_db_id = an_PpcTaskDbId AND
      ppc_dependency.to_activity_id = an_PpcTaskId;

   -- Del: ppc_activity
   DELETE FROM ppc_activity
   WHERE
      ppc_activity.ppc_activity_db_id = an_PpcTaskDbId AND
      ppc_activity.ppc_activity_id = an_PpcTaskId;

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
      an_WpDbId        IN ppc_wp.ppc_wp_db_id%TYPE,
      an_WpId          IN ppc_wp.ppc_wp_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   /* *** DECLARE CURSORS *** */

   CURSOR lcur_TaskList (
      an_WpDbId        ppc_wp.ppc_wp_db_id%TYPE,
      an_WpId          ppc_wp.ppc_wp_id%TYPE
   ) IS
      SELECT
         ppc_task.ppc_task_db_id,
         ppc_task.ppc_task_id
      FROM
         ppc_task
         INNER JOIN ppc_activity ON
            ppc_activity.ppc_activity_db_id = ppc_task.ppc_task_db_id AND
            ppc_activity.ppc_activity_id = ppc_task.ppc_task_id
      WHERE
         ppc_activity.ppc_wp_db_id = an_WpDbId AND
         ppc_activity.ppc_wp_id = an_WpId;
   lrec_TaskList  lcur_TaskList%ROWTYPE;

BEGIN

   on_Return := icn_NoProc;

   /* *** Delete all the Tasks of the Work Package ****/
   FOR lrec_TaskList IN lcur_TaskList( an_WpDbId, an_WpId )
   LOOP
      DeleteTask( lrec_TaskList.ppc_task_db_id, lrec_TaskList.ppc_task_id, on_Return );
   END LOOP;

   /* *** Delete all the Work Package data ****/

   -- Clear: ppc_phase Nullable FKs
   UPDATE ppc_phase
   SET
      nr_phase_db_id = NULL,
      nr_phase_id = NULL,
      nr_start_milestone_db_id = NULL,
      nr_start_milestone_id = NULL,
      nr_end_milestone_db_id = NULL,
      nr_end_milestone_id = NULL
   WHERE
      ( ppc_phase_db_id, ppc_phase_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   -- Clear: ppc_wp Nullable FKs
   UPDATE ppc_wp
   SET
      nr_phase_db_id = NULL,
      nr_phase_id = NULL
   WHERE
      ppc_wp_db_id = an_WpDbId AND
      ppc_wp_id = an_WpDbId;

   -- Del: ppc_phase_class
   DELETE ppc_phase_class
   WHERE
      ( ppc_phase_class_db_id, ppc_phase_class_id )
      IN
      (  SELECT ppc_phase_class_db_id, ppc_phase_class_id
         FROM
            ppc_phase_class
            INNER JOIN ppc_activity ON
               ppc_activity.ppc_activity_db_id = ppc_phase_class.ppc_phase_db_id AND
               ppc_activity.ppc_activity_id = ppc_phase_class.ppc_phase_id
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   -- Del: ppc_phase
   DELETE ppc_phase
   WHERE
      ( ppc_phase_db_id, ppc_phase_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   -- Del: ppc_work_area_zone
   DELETE ppc_work_area_zone
   WHERE
      ( ppc_work_area_zone_db_id, ppc_work_area_zone_id )
      IN
      (  SELECT ppc_work_area_zone_db_id, ppc_work_area_zone_id
         FROM
            ppc_work_area_zone
            INNER JOIN ppc_activity ON
               ppc_activity.ppc_activity_db_id = ppc_work_area_zone.ppc_work_area_db_id AND
               ppc_activity.ppc_activity_id = ppc_work_area_zone.ppc_work_area_id
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
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
               ppc_activity.ppc_activity_db_id = ppc_work_area_crew.ppc_work_area_db_id AND
               ppc_activity.ppc_activity_id    = ppc_work_area_crew.ppc_work_area_id
           WHERE
               ppc_activity.ppc_wp_db_id = an_WpDbId AND
               ppc_activity.ppc_wp_id    = an_WpId
       );
   -- Del: ppc_work_area
   DELETE ppc_work_area
   WHERE
      ( ppc_work_area_db_id, ppc_work_area_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );

   -- Del: ppc_milestone_cond
   DELETE ppc_milestone_cond
   WHERE
      ( ppc_milestone_db_id, ppc_milestone_id )
      IN
      (  SELECT ppc_milestone_db_id, ppc_milestone_id
         FROM
            ppc_milestone_cond
            INNER JOIN ppc_activity ON
               ppc_activity.ppc_activity_db_id = ppc_milestone_cond.ppc_milestone_db_id AND
               ppc_activity.ppc_activity_id = ppc_milestone_cond.ppc_milestone_id
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   -- Del: ppc_milestone
   DELETE ppc_milestone
   WHERE
      ( ppc_milestone_db_id, ppc_milestone_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );

   -- Del: ppc_task_defn_map
   DELETE ppc_task_defn_map
   WHERE
      ( ppc_task_defn_db_id, ppc_task_defn_id )
      IN
      (
        SELECT
            ppc_task_defn_db_id,
            ppc_task_defn_id
        FROM
            ppc_task_defn
        WHERE
            ppc_task_defn.ppc_wp_db_id = an_WpDbId AND
            ppc_task_defn.ppc_wp_id    = an_WpId
       );

   -- Del: ppc_task_defn
   DELETE ppc_task_defn
   WHERE
        ppc_task_defn.ppc_wp_db_id = an_WpDbId AND
        ppc_task_defn.ppc_wp_id    = an_WpId;

   -- Del: ppc_dependency
   DELETE FROM ppc_dependency
   WHERE
      ( from_activity_db_id, from_activity_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   DELETE FROM ppc_dependency
   WHERE
      ( to_activity_db_id, to_activity_id )
      IN
      (  SELECT ppc_activity_db_id, ppc_activity_id
         FROM
            ppc_activity
         WHERE
            ppc_activity.ppc_wp_db_id = an_WpDbId AND
            ppc_activity.ppc_wp_id = an_WpId
      );
   -- Del: ppc_activity
   DELETE FROM ppc_activity
   WHERE
      ppc_activity.ppc_wp_db_id = an_WpDbId AND
      ppc_activity.ppc_wp_id = an_WpId;

   -- Del: ppc_publish_failure
   DELETE FROM ppc_publish_failure
   WHERE
      ppc_publish_failure.ppc_wp_db_id = an_WpDbId AND
      ppc_publish_failure.ppc_wp_id = an_WpId;
   -- Del: ppc_publish
   DELETE FROM ppc_publish
   WHERE
      ppc_publish.ppc_wp_db_id = an_WpDbId AND
      ppc_publish.ppc_wp_id = an_WpId;

   -- Del: ppc_planning_skill
   DELETE FROM ppc_planning_type_skill
   WHERE
      ppc_planning_type_skill.ppc_wp_db_id = an_WpDbId AND
      ppc_planning_type_skill.ppc_wp_id = an_WpId;
   -- Del: ppc_planning_type
   DELETE FROM ppc_planning_type
   WHERE
      ppc_planning_type.ppc_wp_db_id = an_WpDbId AND
      ppc_planning_type.ppc_wp_id = an_WpId;

   -- Del: ppc_wp
   DELETE FROM ppc_wp
   WHERE
      ppc_wp.ppc_wp_db_id = an_WpDbId AND
      ppc_wp.ppc_wp_id = an_WpId;

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
      an_LocDbId    IN ppc_loc.ppc_loc_db_id%TYPE,
      an_LocId      IN ppc_loc.ppc_loc_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   ln_LocDbId                ppc_loc.ppc_loc_db_id%TYPE;
   ln_LocId                  ppc_loc.ppc_loc_id%TYPE;

   /* *** DECLARE CURSORS *** */

   CURSOR lcur_LocationList (
      an_LocDbId        ppc_loc.ppc_loc_db_id%TYPE,
      an_LocId          ppc_loc.ppc_loc_id%TYPE
   ) IS
      SELECT
         ppc_loc.ppc_loc_db_id,
         ppc_loc.ppc_loc_id
      FROM
         ppc_loc
      START WITH
         ppc_loc.ppc_loc_db_id = an_LocDbId AND
         ppc_loc.ppc_loc_id = an_LocId
      CONNECT BY
         ppc_loc.nh_ppc_loc_db_id = PRIOR ppc_loc.ppc_loc_db_id AND
         ppc_loc.nh_ppc_loc_id = PRIOR ppc_loc.ppc_loc_id
      ORDER BY LEVEL DESC;
   lrec_LocationList  lcur_LocationList%ROWTYPE;

   CURSOR lcur_WpList (
      an_LocDbId        ppc_loc.ppc_loc_db_id%TYPE,
      an_LocId          ppc_loc.ppc_loc_id%TYPE
   ) IS
      SELECT
         ppc_wp.ppc_wp_db_id,
         ppc_wp.ppc_wp_id
      FROM
         ppc_wp
      WHERE
         ppc_wp.ppc_loc_db_id = an_LocDbId AND
         ppc_wp.ppc_loc_id = an_LocId;
   lrec_WpList  lcur_WpList%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;

   FOR lrec_LocationList IN lcur_LocationList( an_LocDbId, an_LocId )
   LOOP
      ln_LocDbId := lrec_LocationList.ppc_loc_db_id;
      ln_LocId := lrec_LocationList.ppc_loc_id;

     -- Del: ppc_hr_lic
     DELETE ppc_hr_lic
     WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_hr_shift_plan
           WHERE
               ppc_hr_shift_plan.ppc_loc_db_id = ln_LocDbId AND
               ppc_hr_shift_plan.ppc_loc_id    = ln_LocId
               AND
               ppc_hr_lic.ppc_hr_shift_db_id = ppc_hr_shift_plan.ppc_hr_shift_db_id AND
               ppc_hr_lic.ppc_hr_shift_id    = ppc_hr_shift_plan.ppc_hr_shift_id
       );

     -- Del: ppc_hr_shift_plan
     DELETE ppc_hr_shift_plan
     WHERE
          ppc_hr_shift_plan.ppc_loc_db_id = ln_LocDbId AND
          ppc_hr_shift_plan.ppc_loc_id    = ln_LocId;
   END LOOP;

   /* *** Delete all the children locations and this location ****/
   FOR lrec_LocationList IN lcur_LocationList( an_LocDbId, an_LocId )
   LOOP
      ln_LocDbId := lrec_LocationList.ppc_loc_db_id;
      ln_LocId := lrec_LocationList.ppc_loc_id;

      /* *** Delete all the Work Packages of the Location ****/
      FOR lrec_WpList IN lcur_WpList( ln_LocDbId, ln_LocId )
      LOOP
         DeleteWp( lrec_WpList.ppc_wp_db_id, lrec_WpList.ppc_wp_id, on_Return );
      END LOOP;


     /* *** Delete all the Location data ****/
      -- Del: ppc_loc_capacity
      DELETE ppc_loc_capacity
      WHERE
         ppc_loc_capacity.ppc_loc_db_id = ln_LocDbId AND
         ppc_loc_capacity.ppc_loc_id = ln_LocId;
      -- Del: ppc_crew
      DELETE ppc_crew
      WHERE
         ppc_crew.ppc_loc_db_id = ln_LocDbId AND
         ppc_crew.ppc_loc_id = ln_LocId;
      -- Del: ppc_loc
      DELETE ppc_loc
      WHERE
         ppc_loc.ppc_loc_db_id = ln_LocDbId AND
         ppc_loc.ppc_loc_id = ln_LocId;

   END LOOP;

    -- Del: ppc_hr
    DELETE ppc_hr
    WHERE
       (
          SELECT COUNT(1)
          FROM
             ppc_hr_shift_plan
             WHERE
             ppc_hr_shift_plan.ppc_hr_db_id = ppc_hr.ppc_hr_db_id AND
             ppc_hr_shift_plan.ppc_hr_id    = ppc_hr.ppc_hr_id

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
      an_TaskDefnDbId    IN ppc_task_defn.ppc_task_defn_db_id%TYPE,
      an_TaskDefnId      IN ppc_task_defn.ppc_task_defn_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

   /* Determine if there are any tasks associate with this task definition only */
   CURSOR lcur_TaskList  IS
      SELECT
         ppc_task.ppc_task_db_id,
         ppc_task.ppc_task_id
      FROM
         ppc_task
         INNER JOIN ppc_task_defn_map ON
               ppc_task_defn_map.ppc_task_db_id = ppc_task.ppc_task_db_id AND
               ppc_task_defn_map.ppc_task_id    = ppc_task.ppc_task_id
      WHERE
         ppc_task_defn_map.ppc_task_defn_db_id = an_TaskDefnDbId AND
         ppc_task_defn_map.ppc_task_defn_id    = an_TaskDefnId
         AND
         ( ppc_task.ppc_task_db_id, ppc_task.ppc_task_id )
         NOT IN
         (
             SELECT
                 ppc_task_db_id,
                 ppc_task_id
             FROM
                 ppc_task_defn_map
             WHERE
                 NOT
                 (
                    ppc_task_defn_map.ppc_task_defn_db_id = an_TaskDefnDbId AND
                    ppc_task_defn_map.ppc_task_defn_id    = an_TaskDefnId
                 )
                 AND
                 (
                    ppc_task_defn_map.ppc_task_db_id = ppc_task.ppc_task_db_id AND
                    ppc_task_defn_map.ppc_task_id    = ppc_task.ppc_task_id
                 )
         );
 lrec_TaskList  lcur_TaskList%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;

   OPEN lcur_TaskList;

    /* Del: ppc_task_defn_map */
   DELETE ppc_task_defn_map
   WHERE
        ppc_task_defn_db_id = an_TaskDefnDbId AND
        ppc_task_defn_id    = an_TaskDefnId;

   LOOP
        FETCH lcur_TaskList INTO lrec_TaskList;
        DeleteTask( lrec_TaskList.ppc_task_db_id, lrec_TaskList.ppc_task_id, on_Return );
        EXIT WHEN lcur_TaskList%NOTFOUND;
   END LOOP;

   /* Del: ppc_task_defn */
   DELETE ppc_task_defn
   WHERE
        ppc_task_defn_db_id = an_TaskDefnDbId AND
        ppc_task_defn_id    = an_TaskDefnId;

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
      an_PpcWpDbId       IN ppc_wp.ppc_wp_db_id%TYPE,
      an_PpcWpId         IN ppc_wp.ppc_wp_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

BEGIN

   on_Return := icn_NoProc;

   -- Del: ppc_hr_slot
   DELETE FROM
   (
      SELECT
         ppc_hr_slot.ppc_hr_slot_db_id,
         ppc_hr_slot.ppc_hr_slot_id
      FROM
         ppc_hr_slot
         INNER JOIN ppc_labour_role ON
               ppc_hr_slot.labour_role_db_id = ppc_labour_role.labour_role_db_id AND
               ppc_hr_slot.labour_role_id    = ppc_labour_role.labour_role_id
         INNER JOIN ppc_labour ON
               ppc_labour.labour_db_id = ppc_labour_role.labour_db_id AND
               ppc_labour.labour_id    = ppc_labour_role.labour_id
         INNER JOIN ppc_task ON
               ppc_task.ppc_task_db_id = ppc_labour.ppc_task_db_id AND
               ppc_task.ppc_task_id    = ppc_labour.ppc_task_id
         INNER JOIN ppc_activity ON
               ppc_activity.ppc_activity_db_id = ppc_task.ppc_task_db_id AND
               ppc_activity.ppc_activity_id    = ppc_task.ppc_task_id
      WHERE
         ppc_activity.ppc_wp_db_id = an_PpcWpDbId AND
         ppc_activity.ppc_wp_id    = an_PpcWpId
    );

    --Del: ppc_labour_role
    DELETE FROM
    (
       SELECT
           ppc_labour_role.labour_role_db_id,
           ppc_labour_role.labour_role_id
       FROM
           ppc_labour_role
           INNER JOIN ppc_labour ON
                 ppc_labour.labour_db_id = ppc_labour_role.labour_db_id AND
                 ppc_labour.labour_id    = ppc_labour_role.labour_id
           INNER JOIN ppc_task ON
                 ppc_task.ppc_task_db_id = ppc_labour.ppc_task_db_id AND
                 ppc_task.ppc_task_id    = ppc_labour.ppc_task_id
           INNER JOIN ppc_activity ON
                 ppc_activity.ppc_activity_db_id = ppc_task.ppc_task_db_id AND
                 ppc_activity.ppc_activity_id    = ppc_task.ppc_task_id
       WHERE
           ppc_activity.ppc_wp_db_id = an_PpcWpDbId AND
           ppc_activity.ppc_wp_id    = an_PpcWpId
    );

    --Del: ppc_labour
    DELETE FROM
    (
       SELECT
           ppc_labour.labour_db_id,
           ppc_labour.labour_id
       FROM
           ppc_labour
           INNER JOIN ppc_task ON
                 ppc_task.ppc_task_db_id = ppc_labour.ppc_task_db_id AND
                 ppc_task.ppc_task_id    = ppc_labour.ppc_task_id
           INNER JOIN ppc_activity ON
                 ppc_activity.ppc_activity_db_id = ppc_task.ppc_task_db_id AND
                 ppc_activity.ppc_activity_id    = ppc_task.ppc_task_id
       WHERE
           ppc_activity.ppc_wp_db_id = an_PpcWpDbId AND
           ppc_activity.ppc_wp_id    = an_PpcWpId
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
      an_PpcCapacityDbId     IN ppc_loc_capacity.ppc_capacity_db_id%TYPE,
      an_PpcCapacityId       IN ppc_loc_capacity.ppc_capacity_id%TYPE,
      on_Return        OUT typn_RetCode
   ) IS

BEGIN
   on_Return := icn_NoProc;

   -- Update Slots first and set NULL to ppc HR shift if it belongs
   --- to the Capacity pattern that will be deleted:
   UPDATE
    (
       SELECT ppc_hr_slot.ppc_hr_shift_db_id,
              ppc_hr_slot.ppc_hr_shift_id
       FROM ppc_hr_slot
            INNER JOIN ppc_hr_shift_plan ON
            ppc_hr_shift_plan.ppc_hr_shift_db_id  = ppc_hr_slot.ppc_hr_shift_db_id AND
            ppc_hr_shift_plan.ppc_hr_shift_id = ppc_hr_slot.ppc_hr_shift_id
       WHERE
        ppc_hr_shift_plan.ppc_capacity_db_id = an_PpcCapacityDbId AND
        ppc_hr_shift_plan.ppc_capacity_id    = an_PpcCapacityId
    )
    SET
        ppc_hr_shift_db_id = null,
        ppc_hr_shift_id    = null;

   -- Del: ppc_hr_lic
   DELETE ppc_hr_lic
   WHERE
       EXISTS
       (
           SELECT 1
           FROM
               ppc_loc_capacity
               INNER JOIN ppc_hr_shift_plan ON
                     ppc_hr_shift_plan.ppc_capacity_db_id = ppc_loc_capacity.ppc_capacity_db_id AND
                     ppc_hr_shift_plan.ppc_capacity_id    = ppc_loc_capacity.ppc_capacity_id
           WHERE
               ppc_loc_capacity.ppc_capacity_db_id = an_PpcCapacityDbId AND
               ppc_loc_capacity.ppc_capacity_id    = an_PpcCapacityId
               AND
               ppc_hr_lic.ppc_hr_shift_db_id = ppc_hr_shift_plan.ppc_hr_shift_db_id AND
               ppc_hr_lic.ppc_hr_shift_id    = ppc_hr_shift_plan.ppc_hr_shift_id
       );

   -- Del: ppc_hr_shift_plan
   DELETE ppc_hr_shift_plan
   WHERE
      ppc_hr_shift_plan.ppc_capacity_db_id = an_PpcCapacityDbId AND
      ppc_hr_shift_plan.ppc_capacity_id    = an_PpcCapacityId;

    -- Del: ppc_hr
    DELETE ppc_hr
    WHERE
       (SELECT COUNT(1)
          FROM
             ppc_hr_shift_plan
             WHERE
             ppc_hr_shift_plan.ppc_hr_db_id = ppc_hr.ppc_hr_db_id AND
             ppc_hr_shift_plan.ppc_hr_id    = ppc_hr.ppc_hr_id

       ) = 0;

   -- Del: ppc_loc_cap
   DELETE ppc_loc_capacity
   WHERE
        ppc_loc_capacity.ppc_capacity_db_id = an_PpcCapacityDbId AND
        ppc_loc_capacity.ppc_capacity_id    = an_PpcCapacityId;

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
      an_PpcDbId       IN ppc_plan.ppc_db_id%TYPE,
      an_PpcId         IN ppc_plan.ppc_id%TYPE,
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
                 ppc_labour_role.labour_role_db_id AS role_db_id,
                 ppc_labour_role.labour_role_id   AS role_id
            FROM
                 ppc_wp
                 INNER JOIN ppc_activity ON
                      ppc_activity.ppc_wp_db_id = ppc_wp.ppc_wp_db_id AND
                      ppc_activity.ppc_wp_id    = ppc_wp.ppc_wp_id
                 INNER JOIN ppc_task ON
                      ppc_task.ppc_task_db_id = ppc_activity.ppc_activity_db_id AND
                      ppc_task.ppc_task_id    = ppc_activity.ppc_activity_id
                 INNER JOIN ppc_labour ON
                      ppc_labour.ppc_task_db_id = ppc_task.ppc_task_db_id AND
                      ppc_labour.ppc_task_id    = ppc_task.ppc_task_id
                 INNER JOIN ppc_labour_role ON
                      ppc_labour_role.labour_db_id = ppc_labour.labour_db_id AND
                      ppc_labour_role.labour_id    = ppc_labour.labour_id
             WHERE
                 ppc_wp.ppc_db_id = an_PpcDbId AND
                 ppc_wp.ppc_id    = an_PpcId
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
                  ppc_hr_slot.labour_role_db_id = role_db_id AND
                  ppc_hr_slot.labour_role_id    = role_id
        );

     -- update task
     UPDATE
      (
            SELECT
                 ppc_task.start_dt,
                 ppc_task.end_dt,
                 ppc_task.ppc_task_db_id AS task_db_id,
                 ppc_task.ppc_task_id AS task_id
            FROM
                 ppc_wp
                 INNER JOIN ppc_activity ON
                      ppc_activity.ppc_wp_db_id = ppc_wp.ppc_wp_db_id AND
                      ppc_activity.ppc_wp_id    = ppc_wp.ppc_wp_id
                 INNER JOIN ppc_task ON
                      ppc_task.ppc_task_db_id = ppc_activity.ppc_activity_db_id AND
                      ppc_task.ppc_task_id    = ppc_activity.ppc_activity_id
             WHERE
                 ppc_wp.ppc_db_id = an_PpcDbId AND
                 ppc_wp.ppc_id    = an_PpcId
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
                      ppc_labour_role.labour_db_id = ppc_labour.labour_db_id AND
                      ppc_labour_role.labour_id    = ppc_labour.labour_id
             WHERE
                  ppc_labour.ppc_task_db_id = task_db_id AND
                  ppc_labour.ppc_task_id    = task_id
        );

    -- update Work Packages
    UPDATE
        (
           SELECT
               ppc_wp.start_dt,
               ppc_wp.ppc_wp_db_id AS wp_db_id,
               ppc_wp.ppc_wp_id AS wp_id
           FROM
               ppc_wp
           WHERE
               ppc_wp.ppc_db_id = an_PpcDbId AND
               ppc_wp.ppc_id    = an_PpcId
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
                              ppc_task.ppc_task_db_id = ppc_activity.ppc_activity_db_id AND
                              ppc_task.ppc_task_id    = ppc_activity.ppc_activity_id
                     WHERE
                         ppc_activity.ppc_wp_db_id = wp_db_id AND
                         ppc_activity.ppc_wp_id    = wp_id
                  );

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','ppc_pkg@@@RefreshPlanDates@@@'||SQLERRM);
      RETURN;

END RefreshPlanDates;

END PPC_PKG;
/