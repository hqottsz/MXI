--liquibase formatted sql


--changeSet RND-2472:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
* Recent Coder:   Jonathan Clarkin
*
*********************************************************************************
*
* Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
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
        ad_PreviousCompletionDt   IN evt_event.sched_end_gdt%TYPE,
        on_SchedDbId          OUT evt_event.event_db_id%TYPE,
        on_SchedId            OUT evt_event.event_id%TYPE,
        on_Return             OUT typn_RetCode
   ) IS

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
   ln_histBool             NUMBER;
   ln_woCommitLine         NUMBER;
   ln_PreviousTaskDbId     NUMBER;
   ln_PreviousTaskId       NUMBER;
   ln_StdPartNoDbId        NUMBER;
   ln_StdPartNoId          NUMBER;
   ln_count 		   NUMBER;
   ln_StageId              evt_stage.stage_id%TYPE;
   lb_TempPartReady   		sched_stask.parts_ready_bool%TYPE;
   lb_TempToolReady   		sched_stask.tools_ready_bool%TYPE;
   ln_PreventExeReqBool    sched_stask_flags.prevent_exe_bool%TYPE;
   ld_PreventExeReviewDt   sched_stask_flags.prevent_exe_review_dt%TYPE;
   ln_OilDataTypeDbId 	   eqp_assmbl_bom_oil.oil_data_type_db_id%TYPE;
   ln_OilDataTypeId 	   eqp_assmbl_bom_oil.oil_data_type_id%TYPE;
   ln_EngUnitDbId          mim_data_type.eng_unit_db_id%TYPE;
   ls_EngUnitCD            mim_data_type.eng_unit_cd%TYPE;
   ls_ExitCd               VARCHAR2(8);                             /* holder variable for the exit code, not used */

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

      FROM inv_inv, inv_inv ass_inv_inv
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
         task_zone.task_zone_id,
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
         task_panel.task_panel_id,
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
            ietm_topic.ietm_topic_id = task_task_ietm.ietm_topic_id
      WHERE
         task_task_ietm.task_db_id = an_TaskDbId AND
         task_task_ietm.task_id    = an_TaskId;

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
                inv_inv.inv_no_db_id = an_InventoryDbId
                AND
                inv_inv.inv_no_id    = an_InventoryId
              CONNECT BY 
                inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id
                AND
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


   /* if the task is active and it has a parent task defined in the baseline */
   IF ((NOT ab_Historic) AND CountBaselineParents(an_TaskDbId, an_TaskId)>0) THEN
        FindLatestTaskInstance(
              an_TaskDbId,
              an_TaskId,
              an_InvNoDbId,
              an_InvNoId,
              ln_PreviousTaskDbId,
              ln_PreviousTaskId,
              on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;
   ELSE
        ln_PreviousTaskDbId := an_PreviousTaskDbId;
        ln_PreviousTaskId   := an_PreviousTaskId;
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
         evt_event.rstat_cd   = 0;

      /* if the previous task exists and not historic then this task is a forecasted task */
      IF ln_PreviousTaskHistBool<>1 THEN
         ln_InitalTaskStatus := 'FORECAST';
      END IF;

   END IF;

  /* if task is unique, and there is and active instance of that task on the aircraft */
   IF (lrec_TaskDefinition.unique_bool=1 AND ln_InitalTaskStatus<>'FORECAST' AND
       ln_InitalTaskStatus<>'COMPLETE' AND CountTaskInstances(an_TaskDbId,
       an_TaskId, an_InvNoDbId, an_InvNoId)>0) THEN
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
            on_Return );
   IF on_Return < 1 THEN
      RETURN;
   END IF;
   IF ln_TaskApplies = icn_False THEN
      RAISE xc_InvHasIncorrectPartNo;
   END IF;

   /* check to make sure that the given piece of inventory falls within
      the task definition's applicability rule */
   ln_TaskApplies := IsTaskApplicable(
            an_InvNoDbId,
            an_InvNoId,
            an_TaskDbId,
            an_TaskId );

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
       ln_EventId :=  an_EvtEventId;
   END IF;

  /* check to make sure that the current task definition is a root
     if ln_ParentTaskCount=0 then root if ln_ParentTaskCount=1 then subtask*/
   SELECT ref_task_class.class_mode_cd
     INTO ls_TaskClassMode
     FROM task_task
          INNER JOIN ref_task_class ON
             ref_task_class.task_class_db_id   = task_task.task_class_db_id  AND
             ref_task_class.task_class_cd      = task_task.task_class_cd
    WHERE task_task.task_db_id  = an_TaskDbId  AND
          task_task.task_id     = an_TaskId
          AND
          ref_task_class.rstat_cd       = 0;

   IF ( ls_TaskClassMode = 'BLOCK' ) THEN
      ln_SubEventOrd := 1;
   ELSE
      IF ( ls_TaskClassMode = 'REQ' )
      THEN
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
         lrec_TaskDefinition.task_ref_sdesc );

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


   /* check to make sure that the task definition is active */
   IF lrec_TaskDefinition.task_class_cd='CHECK' THEN
     ls_TaskType:='check';
     ln_woCommitLine:=0;
   END IF;

   IF lrec_TaskDefinition.task_class_cd='RO' THEN
     ls_TaskType:='work order';
     ln_woCommitLine:=0;
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
         system_bool )
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
                  system_bool )
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
             lrec_WorkType.work_type_cd );
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
              ietm_ord )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              lrec_TaskIetm.task_ietm_id,
              lrec_TaskIetm.ietm_db_id,
              lrec_TaskIetm.ietm_id,
              lrec_TaskIetm.ietm_topic_id,
              lrec_TaskIetm.ietm_ord );
      ELSE
         INSERT INTO evt_attach (
              event_db_id,
              event_id,
              event_attach_id,
              ietm_db_id,
              ietm_id,
              ietm_topic_id,
              print_bool )
           VALUES (
              ln_EventDbId,
              ln_EventId,
              lrec_TaskIetm.task_ietm_id,
              lrec_TaskIetm.ietm_db_id,
              lrec_TaskIetm.ietm_id,
              lrec_TaskIetm.ietm_topic_id,
              lrec_TaskIetm.print_bool );
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
              lrec_Zone.task_zone_id,
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
              lrec_Panel.task_panel_id,
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
                  current_status_ord)
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  ln_EventDbId, ln_EventId,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV'),
                  lrec_TaskLabour.labour_skill_db_id, lrec_TaskLabour.labour_skill_cd,
                  lrec_TaskLabour.work_perf_bool,
                  lrec_TaskLabour.cert_bool,
                  lrec_TaskLabour.insp_bool,
                  1);

               -- Add Role and Status for Tech
               SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
               INSERT INTO sched_labour_role (
                  labour_role_db_id, labour_role_id,
                  labour_db_id, labour_id,
                  labour_role_type_db_id, labour_role_type_cd,
                  sched_hr)
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                  0, 'TECH',
                  lrec_TaskLabour.work_perf_hr);
               SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
               INSERT INTO sched_labour_role_status (
                  status_db_id, status_id,
                  labour_role_db_id, labour_role_id,
                  status_ord,
                  labour_role_status_db_id, labour_role_status_cd)
               VALUES (
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                  APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                  1,
                  0, decode (ln_histBool, 1, 'COMPLETE', 'ACTV'));

              -- If Certification required, Add Role and Status for Cert
              IF( lrec_TaskLabour.cert_bool = 1 )
              THEN
                 SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                 INSERT INTO sched_labour_role (
                    labour_role_db_id, labour_role_id,
                    labour_db_id, labour_id,
                    labour_role_type_db_id, labour_role_type_cd,
                    sched_hr)
                 VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                    0, 'CERT',
                    lrec_TaskLabour.cert_hr);
                 SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                 INSERT INTO sched_labour_role_status (
                    status_db_id, status_id,
                    labour_role_db_id, labour_role_id,
                    status_ord,
                    labour_role_status_db_id, labour_role_status_cd)
                 VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    1,
                    0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING'));

              END IF;
              -- If Inspection required, Add Role and Status for Insp
              IF( lrec_TaskLabour.insp_bool = 1 )
              THEN
                 SELECT SCHED_LABOUR_ROLE_SEQ.nextval INTO ln_LabourRoleId FROM dual;
                 INSERT INTO sched_labour_role (
                    labour_role_db_id, labour_role_id,
                    labour_db_id, labour_id,
                    labour_role_type_db_id, labour_role_type_cd,
                    sched_hr)
                 VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourId,
                    0, 'INSP',
                    lrec_TaskLabour.insp_hr);
                 SELECT SCHED_LABOUR_ROLE_STATUS_SEQ.nextval INTO ln_LabourRoleStatusId FROM dual;
                 INSERT INTO sched_labour_role_status (
                    status_db_id, status_id,
                    labour_role_db_id, labour_role_id,
                    status_ord,
                    labour_role_status_db_id, labour_role_status_cd)
                 VALUES (
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleStatusId,
                    APPLICATION_OBJECT_PKG.getdbid, ln_LabourRoleId,
                    1,
                    0, decode (ln_histBool, 1, 'COMPLETE', 'PENDING'));
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

/* Add all of the assembly measurements.  */

     FOR lrec_Assembly_Inventory IN lcur_Assembly_Inventories(an_InvNoDbId,an_InvNoId)
     LOOP

        SELECT COUNT(*) INTO ln_count FROM
        eqp_assmbl_bom_oil WHERE
        eqp_assmbl_bom_oil.assmbl_db_id = lrec_Assembly_Inventory.orig_assmbl_db_id AND
        eqp_assmbl_bom_oil.assmbl_cd = lrec_Assembly_Inventory.orig_assmbl_cd;

        IF ln_count > 0 THEN

        SELECT eqp_assmbl_bom_oil.oil_data_type_db_id,eqp_assmbl_bom_oil.oil_data_type_id
        INTO ln_OilDataTypeDbId, ln_OilDataTypeId
        FROM
        eqp_assmbl_bom_oil WHERE
        eqp_assmbl_bom_oil.assmbl_db_id = lrec_Assembly_Inventory.orig_assmbl_db_id AND
        eqp_assmbl_bom_oil.assmbl_cd = lrec_Assembly_Inventory.orig_assmbl_cd;


        FOR lrec_Assembly_Measurement IN lcur_Assembly_Measurement( an_TaskDbId, an_TaskId )
        LOOP

          IF ((lrec_Assembly_Measurement.data_type_db_id =  ln_OilDataTypeDbId) AND (lrec_Assembly_Measurement.data_type_id = ln_OilDataTypeId)) THEN

           SELECT
         mim_data_type.eng_unit_db_id, mim_data_type.eng_unit_cd
      INTO
           ln_EngUnitDbId, ls_EngUnitCD
      FROM
           mim_data_type
      WHERE
           data_type_db_id=ln_OilDataTypeDbId AND
                data_type_id = ln_OilDataTypeId;

           INSERT INTO inv_parm_data (
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
          END LOOP;

     END IF;
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
        step_ldesc )
          VALUES (
        ln_EventDbId,
        ln_EventId,
        ln_StepId,
        lrec_Step.step_ord,
        lrec_Step.step_ldesc );
      END LOOP;

   END IF; /* End JIC/CORR task Specific logic */


   /* Cue up the Task for Zipping as part of Baseline Sync */
   IF ( ls_TaskClassMode = 'BLOCK' )
   THEN
      BaselineSyncPkg.RequestZipForNewBlock( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
   ELSE
      IF ( ls_TaskClassMode = 'REQ' )
      THEN
         BaselineSyncPkg.RequestZipForNewReq( an_InvNoDbId, an_InvNoId, ln_EventDbId, ln_EventId, on_Return );
      END IF;
   END IF;


   /* If a previous task was given, then add a row to the evt_event_rel table */
   IF (ln_PreviousTaskDbId <> -1 ) THEN
    CreateTaskDependencyLink(ln_PreviousTaskDbId, ln_PreviousTaskId, ln_EventDbId, ln_EventId, on_Return);
    IF on_Return < 0 THEN
       RETURN;
    END IF;
   END IF;


   /* For Non-Historic Tasks: */
   IF ab_historic=false THEN

      /* Create Task Deadlines */
     PREP_DEADLINE_PKG.PrepareSchedDeadlines( ln_EventDbId, ln_EventId, NULL, NULL, true, ad_PreviousCompletionDt, on_Return );
      IF on_Return < 1 THEN
        RETURN;
      END IF;

      /* Create all child tasks, but only for REQs
         Should not auto-create a job card under a CORR task */
      IF ( ls_TaskClassMode = 'REQ' AND lrec_TaskDefinition.task_class_cd <> 'CORR')
      THEN
         AutoCreateChildTasks( ln_EventDbId, ln_EventId, on_Return );
         IF on_Return < 1 THEN
           RETURN;
         END IF;

         -- Evaluate part/tool readiness for this task
         UpdatePartsAndToolsReadyBool(ln_EventDbId,
                                      ln_EventId,
                                      lb_TempPartReady,
                                      lb_TempToolReady,
                                      on_Return);
         IF on_Return < 1 THEN
           RETURN;
         END IF;
      END IF;


      /* Update the references to the root task (non-check) */
      UpdateHSched( ln_EventDbId, ln_EventId, on_Return );
      IF on_Return < 1 THEN
        RETURN;
      END IF;

      /* Generate all of the dependendent Forecasted Tasks */
      GenForecastedTasks( ln_EventDbId, ln_EventId, ls_ExitCd, on_Return );
      IF on_Return < 1 THEN
        RETURN;
      END IF;

   END IF;

   /* record the event_id as this will ultimately return the highest event_id (original request) */
   on_SchedDbId := ln_EventDbId;
   on_SchedId   := ln_EventId;
   on_Return    := icn_Success;

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
END GenSchedTask;



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
   ln_InvCount                    NUMBER;
   ln_count                       NUMBER;
   ln_OrdCount                    NUMBER;
   ln_ExistsCount		  NUMBER;

   ln_OilDataTypeDbId eqp_assmbl_bom_oil.oil_data_type_db_id%TYPE;
   ln_OilDataTypeId eqp_assmbl_bom_oil.oil_data_type_id%TYPE;
   /* Cursor for getting the assemblies and sub assemblies from an inventory */
   CURSOR lcur_Assembly_Inventories(
          an_InventoryDbId inv_inv.inv_no_db_id%TYPE,
            an_InventoryId inv_inv.inv_no_id%TYPE
   ) IS
   SELECT
     inv_inv.orig_assmbl_db_id,
     inv_inv.orig_assmbl_cd,
     eqp_assmbl.assmbl_class_cd,
     inv_inv.inv_no_db_id,
     inv_inv.inv_no_id
   FROM
     eqp_assmbl
     INNER JOIN inv_inv ON
        inv_inv.orig_assmbl_db_id = eqp_assmbl.assmbl_db_Id AND
        inv_inv.orig_assmbl_cd = eqp_assmbl.assmbl_cd
   WHERE
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
         inv_parm_data.event_db_id = an_TaskDbId and
         inv_parm_data.event_id = an_TaskId;

     /** Get the inventory count*/
     SELECT
         count(*) INTO  ln_InvCount

     FROM
         sched_stask
     WHERE
         sched_stask.sched_db_id = an_TaskDbId AND
         sched_stask.sched_id = an_TaskId;

     IF ln_InvCount > 0 THEN
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

     FOR lrec_Assembly_Inventory IN lcur_Assembly_Inventories(ln_InventoryDbId,ln_InventoryId)
     LOOP

        SELECT COUNT(*) INTO ln_count FROM
        eqp_assmbl_bom_oil WHERE
        eqp_assmbl_bom_oil.assmbl_db_id = lrec_Assembly_Inventory.orig_assmbl_db_id AND
        eqp_assmbl_bom_oil.assmbl_cd = lrec_Assembly_Inventory.orig_assmbl_cd;

        IF ln_count > 0 THEN

        SELECT eqp_assmbl_bom_oil.oil_data_type_db_id,eqp_assmbl_bom_oil.oil_data_type_id
        INTO ln_OilDataTypeDbId, ln_OilDataTypeId
        FROM
        eqp_assmbl_bom_oil WHERE
        eqp_assmbl_bom_oil.assmbl_db_id = lrec_Assembly_Inventory.orig_assmbl_db_id AND
        eqp_assmbl_bom_oil.assmbl_cd = lrec_Assembly_Inventory.orig_assmbl_cd;


           IF ((an_DataTypeDbId =  ln_OilDataTypeDbId) AND (an_DataTypeId = ln_OilDataTypeId)) THEN
           
		SELECT COUNT(*) INTO ln_ExistsCount FROM
        	inv_parm_data WHERE      
		inv_parm_data.event_db_id = an_TaskDbId AND
                inv_parm_data.event_id = an_TaskId AND
                inv_parm_data.event_inv_id = 1 AND
                inv_parm_data.data_type_db_id = an_DataTypeDbId AND
                inv_parm_data.data_type_id = an_DataTypeId AND
                inv_parm_data.inv_no_db_id = lrec_Assembly_Inventory.inv_no_db_id AND
                inv_parm_data.inv_no_id  =  lrec_Assembly_Inventory.inv_no_id;    	

           	IF (ln_ExistsCount = 0) THEN


		      INSERT INTO inv_parm_data (
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
   ln_PostModExists  NUMBER;
   BEGIN

   /* determine if the post mod relationship exists*/
   SELECT COUNT (*)
   INTO ln_PostModExists
   FROM
   task_task_dep,
   task_task
   WHERE
   task_task.task_db_id = an_TaskDbId AND
   task_task.task_id    = an_TaskId
   AND
   task_task_dep.dep_task_defn_db_id = task_task.task_defn_db_id AND
   task_task_dep.dep_task_defn_id    = task_task.task_defn_id AND
   task_task_dep.task_dep_action_db_id = 0 AND
   task_task_dep.task_dep_action_cd = 'POSTMOD';

   /* check for post mod applicability */
   /* if the post mod relationship exists and all the alteration task has been completed */
   IF ((ln_PostModExists > 0) AND (getAlterationTasksCompleted(an_TaskDbId, an_TaskId, an_InvNoDbId, an_InvNoId) < ln_PostModExists)) THEN
       RETURN icn_False;
   END IF;

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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
     RETURN icn_Error;
   END;


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
* Recent Date:  2008-08-25
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
     RETURN icn_False;
   END;



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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','CountBaselineParents: '||SQLERRM);
     RETURN icn_Error;
   END;

/********************************************************************************
*
* Procedure:    getAlterationTasksCompleted
* Arguments:
*               an_DepTaskDbId    (long) - dependent task definition database id
*               an_DepTaskId      (long) - dependent task definition id
*               an_InvNoDbId      (long) - inventory database id
*               an_InvNoId        (long) - inventory id
* Return:
*               ln_AlterationCompleted (long) - the number of alteration tasks completed.
*                                               Should return 0 if no alteration tasks were completed,
*                                               else the number of alteration tasks
*                                               that were completed for the given post mod task.
*
*
* Description:  Returns whether the alteration task(s) for a post mod requirement were
*               completed
*
* Orig.Coder:   Ritika Mehta
* Recent Coder:
* Recent Date:  February 26, 2007
*
*********************************************************************************
*
* Copyright 1997-2007 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getAlterationTasksCompleted (
      an_DepTaskDbId            IN task_task.task_db_id%TYPE,
      an_DepTaskId              IN task_task.task_id%TYPE,
      an_InvNoDbId              IN inv_inv.inv_no_db_id%TYPE,
      an_InvNoId                IN inv_inv.inv_no_id%TYPE
   ) RETURN NUMBER IS

   ln_AlterationCompleted NUMBER;

   BEGIN

      SELECT COUNT(*)
      INTO ln_AlterationCompleted
      FROM
        ( /* get the inventory tree associated with the task*/
   SELECT
     full_assembly_tree.inv_no_db_id,
     full_assembly_tree.inv_no_id
   FROM
     inv_inv,
        inv_inv full_assembly_tree
   WHERE
     inv_inv.inv_no_db_id = an_InvNoDbId AND
     inv_inv.inv_no_id    = an_InvNoId  AND
     inv_inv.rstat_cd   = 0
     AND
     (
     full_assembly_tree.assmbl_inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
     full_assembly_tree.assmbl_inv_no_id    = inv_inv.assmbl_inv_no_id
     OR
     full_assembly_tree.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
     full_assembly_tree.inv_no_id    = inv_inv.assmbl_inv_no_id
     )
        )assembly_tree,
        (
          SELECT DISTINCT
          FIRST_VALUE( sched_stask.sched_db_id ) OVER
                        ( PARTITION BY task_task_dep.task_db_id || ':' || task_task_dep.task_id
                           ORDER BY sched_stask.sched_id DESC ) AS sched_db_id,
          FIRST_VALUE( sched_stask.sched_id ) OVER
                        ( PARTITION BY task_task_dep.task_db_id || ':' || task_task_dep.task_id
                           ORDER BY sched_stask.sched_id DESC ) AS sched_id
          FROM
          task_task_dep,
          task_task,
          task_task dep_task,
          sched_stask
          WHERE
          dep_task.task_db_id = an_DepTaskDbId AND
          dep_task.task_id    = an_DepTaskId
          AND
          task_task_dep.dep_task_defn_db_id = dep_task.task_defn_db_id AND
          task_task_dep.dep_task_defn_id    = dep_task.task_defn_id AND
          task_task_dep.task_dep_action_db_id = 0 AND
          task_task_dep.task_dep_action_cd = 'POSTMOD'
          AND
          task_task.task_db_id = task_task_dep.task_db_id AND
          task_task.task_id    = task_task_dep.task_id
          AND
          sched_stask.task_db_id = task_task.task_db_id AND
          sched_stask.task_id    = task_task.task_id  AND
          sched_stask.rstat_cd   = 0
        )postmod_tasks,
        evt_inv,
        evt_event
      WHERE
         evt_inv.event_db_id  = postmod_tasks.sched_db_id AND
         evt_inv.event_id     = postmod_tasks.sched_id AND
         evt_inv.inv_no_db_id = assembly_tree.inv_no_db_id AND
         evt_inv.inv_no_id    = assembly_tree.inv_no_id
         AND
         evt_event.event_db_id        = evt_inv.event_db_id AND
         evt_event.event_id           = evt_inv.event_id AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'COMPLETE';

         RETURN ln_AlterationCompleted;
   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','getAlterationTasksCompleted : '||SQLERRM);
     RETURN icn_Error;
   END;
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','CountTaskInstances: '||SQLERRM);
     RETURN icn_Error;
   END;



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
          sched_stask.task_db_id=an_taskdbid AND
          sched_stask.task_id=an_taskid        AND
          sched_stask.rstat_cd   = 0
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
          AND
          isorphanedforecastedtask(evt_sched_dead.event_db_id, evt_sched_dead.event_id) = 0
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
         evt_event task_event,
         evt_event check_event,
         utl_config_parm

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
         check_event.event_db_id = task_event.h_event_db_id AND
         check_event.event_id    = task_event.h_event_id    AND
         check_event.hist_bool   = 0
         AND
         check_event.event_status_cd IN ('COMMIT', 'IN WORK')
         AND
         sched_stask.sched_db_id = task_event.event_db_id AND
         sched_stask.sched_id    = task_event.event_id    AND
         sched_stask.task_db_id  = task_task.task_db_id   AND
         sched_stask.task_id     = task_task.task_id
         AND
         task_task.task_defn_db_id = curent_task_task.task_defn_db_id AND
         task_task.task_defn_id    = curent_task_task.task_defn_id
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
  APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','FindLatestTaskInstance: '||SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','CreateTaskDependencyLink: '||SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','AddRmvdPartRequirement: '||SQLERRM);

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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','GenerateRemoveInstallPartReq: '||SQLERRM);

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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
         mim_data_type.domain_type_cd
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
         task_part_map.part_no_db_id (+)= inv_inv.part_no_db_id AND
         task_part_map.part_no_id    (+)= inv_inv.part_no_id
         AND
         -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         (task_task.assmbl_db_id IS NOT NULL
          OR
          inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
          inv_inv.part_no_id    = task_part_map.part_no_id
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
         task_part_map.part_no_db_id (+)= inv_inv.part_no_db_id AND
         task_part_map.part_no_id    (+)= inv_inv.part_no_id
         AND
          -- All Config Slot based task definitions OR Part No based task definitions mapped to the same part as the current inventory
         ( task_task.assmbl_db_id IS NOT NULL
           OR
           inv_inv.part_no_db_id = task_part_map.part_no_db_id AND
           inv_inv.part_no_id    = task_part_map.part_no_id
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

   /* if task belongs to a chain of orphaned forecasted tasks than do nothing */
   IF IsOrphanedForecastedTask(an_SchedDbId, an_SchedId) = 1 THEN
      on_Return := icn_Success;
      os_ExitCd := 'ORPHAN';
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
     RETURN;
END AutoCreateChildTasks;


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
      inv_inv.inv_no_db_id=evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id=evt_inv.inv_no_id
      AND
      (
         (
            inv_inv.assmbl_inv_no_db_id is not null AND
            assy.inv_no_db_id=inv_inv.assmbl_inv_no_db_id AND
            assy.inv_no_id=inv_inv.assmbl_inv_no_id
         )
         OR
         (
            inv_inv.assmbl_inv_no_db_id is null AND
            assy.inv_no_db_id=inv_inv.h_inv_no_db_id AND
            assy.inv_no_id=inv_inv.h_inv_no_id
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
      sched_stask
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
      )
   WHERE
      /* Don't set checks */
      sched_stask.task_class_cd NOT IN ('RO', 'CHECK')   AND
      sched_stask.rstat_cd = 0
      AND
      ( sched_stask.sched_db_id, sched_stask.sched_id ) IN
      (
         /* Use connect by prior.  This may not be a root task that's passed in. */
         SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         START WITH
            evt_event.event_db_id = an_SchedDbId AND
            evt_event.event_id    = an_SchedId
         CONNECT BY
            evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
            evt_event.nh_event_id    = PRIOR evt_event.event_id
      );

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
     -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',SQLERRM);
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
              evt_event.nh_event_id    = PRIOR evt_Event.event_id;

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
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999','sched_stask_pkg@@@UpdatePartsAndToolsReadyBool@@@'||SQLERRM);
        RETURN;
END UpdatePartsAndToolsReadyBool;

END SCHED_STASK_PKG;
/