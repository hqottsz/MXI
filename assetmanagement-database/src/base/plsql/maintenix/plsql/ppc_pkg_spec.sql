--liquibase formatted sql


--changeSet ppc_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE PPC_PKG

IS

/********************************************************************************
*
* Package:     PPC_PKG
* Description: This package is used for Production Planning and Control
*
* Orig.Coder:   Jonathan R. Clarkin
* Recent Coder: edo
* Recent Date:  September 28, 2010
*
*********************************************************************************
*
* Copyright 1998-2010 Mxi Technologies Ltd.  All Rights Reversed.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

/*------------------------------------ SUBTYPES ----------------------------*/
-- Define a subtype for return codes
SUBTYPE typn_RetCode IS NUMBER;

/*---------------------------------- Constants -----------------------------*/

-- Basic error handling codes
icn_Success CONSTANT typn_RetCode := 1;       -- Success
icn_NoProc  CONSTANT typn_RetCode := 0;       -- No processing done
icn_Error   CONSTANT typn_RetCode := -1;      -- Error

-- Sub procedure validation TRUE, FALSE
icn_True    CONSTANT typn_RetCode := 1;  -- True
icn_False   CONSTANT typn_RetCode := 0;  -- False

-- Predefined activity types
icn_Milestone CONSTANT ref_ppc_activity_type.ppc_activity_type_cd%TYPE := 'MILESTONE';
icn_Phase     CONSTANT ref_ppc_activity_type.ppc_activity_type_cd%TYPE := 'PHASE';
icn_Task      CONSTANT ref_ppc_activity_type.ppc_activity_type_cd%TYPE := 'TASK';
icn_WorkArea  CONSTANT ref_ppc_activity_type.ppc_activity_type_cd%TYPE := 'WORKAREA';

/*---------------------------------- Procedures -----------------------------*/

PROCEDURE DeletePlan (
      an_PpcId         IN ppc_plan.plan_id%TYPE,
      on_Return        OUT typn_RetCode
   );
PROCEDURE DeleteTask (
      an_PpcTaskId     IN ppc_task.task_id%TYPE,
      on_Return        OUT typn_RetCode
   );
PROCEDURE DeleteWp (
      an_WpId          IN ppc_wp.work_package_id%TYPE,
      on_Return        OUT typn_RetCode
   );
PROCEDURE DeleteLocation (
      an_LocId         IN ppc_loc.location_id%TYPE,
      on_Return        OUT typn_RetCode
   );
PROCEDURE UpdateLabourRoleHr (
   an_LabourRoleDbId   IN    sched_labour_role_status.labour_role_db_id%TYPE,
   an_LabourRoleId     IN    sched_labour_role_status.labour_role_id%TYPE,
   an_HrDbId           IN    sched_labour_role_status.hr_db_id%TYPE,
   an_HrId             IN    sched_labour_role_status.hr_id%TYPE,
   on_RoleStatusDbId   OUT   sched_labour_role_status.labour_role_status_db_id%TYPE,
   on_RoleStatusCd     OUT   sched_labour_role_status.labour_role_status_cd%TYPE,
   on_Return           OUT   typn_RetCode
);
PROCEDURE DeleteTaskDefinition (
      an_TaskDefnId         IN ppc_task_defn.task_definition_id%TYPE,
      on_Return             OUT typn_RetCode
   );
PROCEDURE DeleteLabour (
      an_PpcWpId         IN ppc_wp.work_package_id%TYPE,
      on_Return             OUT typn_RetCode
   );
PROCEDURE DeleteCapacityPattern (
      an_PpcCapacityId         IN ppc_loc_capacity.location_capacity_id%TYPE,
      on_Return             OUT typn_RetCode
   );
PROCEDURE RefreshPlanDates (
      an_PpcId         IN ppc_plan.plan_id%TYPE,
      on_Return        OUT typn_RetCode
   );
END PPC_PKG;
/