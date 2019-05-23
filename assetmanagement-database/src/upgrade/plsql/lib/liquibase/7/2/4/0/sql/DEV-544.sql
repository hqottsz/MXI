--liquibase formatted sql


--changeSet DEV-544:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSREL');
END;
/

--changeSet DEV-544:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTASSIGNEDMANHRSREL
 AS OBJECT (
 lrp_event_db_id     NUMBER(10),
 lrp_event_id        NUMBER(10),
 planning_type_db_id NUMBER(10),
 planning_type_id    NUMBER(10),
 planning_type_cd    VARCHAR2(80),
 assignedManhours    NUMBER(10)
 );
/

--changeSet DEV-544:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSRELTABLE');
END;
/

--changeSet DEV-544:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTASSIGNEDMANHRSRELTABLE AS TABLE OF PTAssignedManhrsRel;
/

--changeSet DEV-544:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE ASSIGNED_MANHOURS_PKG is

  -- Author  : RAHULB
  -- Created : 8/25/2010 6:30:11 AM
  -- Purpose : This will calculate the assigned manhours for the event


-- Basic error handling codes
 	icn_Success CONSTANT NUMBER := 1;       -- Success
 	icn_NoProc  CONSTANT NUMBER := 0;       -- No processing done
 	icn_Error   CONSTANT NUMBER := -1;      -- Error
  -- Public function and procedure declarations
  FUNCTION calculateAssignedManhours(
                                     an_LrpDbId lrp_plan.lrp_db_id%TYPE,
                                     an_LrpId   lrp_plan.lrp_id%TYPE)
                                     return  PTAssignedManhrsRelTable;

END ASSIGNED_MANHOURS_PKG;
/

--changeSet DEV-544:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY ASSIGNED_MANHOURS_PKG is

/********************************************************************************
*
* Procedure:     getStdPlanningTypesForAssembly
* Arguments:
*               an_AssemblyDbId     Assembly key
*               an_AssemblyCd         -- // --
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*
*
* Description:  Get the STDR Planning type associated with the assemnly.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getStdPlanningTypesForAssembly(
            an_AssemblyDbId     IN  eqp_assmbl.assmbl_db_id%TYPE,
            an_AssemblyCd       IN  eqp_assmbl.assmbl_cd%TYPE,
            on_PlanningTypeDbId OUT eqp_planning_type.planning_type_db_id%TYPE,
            on_PlanningTypeId   OUT eqp_planning_type.planning_type_id%TYPE,
            os_PlanningTypeCd   OUT eqp_planning_type.planning_type_cd%TYPE,
            on_Return           OUT NUMBER
            )IS
BEGIN

   on_Return                := 0;

SELECT
     eqp_planning_type.planning_type_db_id,
     eqp_planning_type.planning_type_id,
     eqp_planning_type.planning_type_cd
INTO
     on_PlanningTypeDbId,
     on_PlanningTypeId,
     os_PlanningTypeCd
FROM
     eqp_planning_type
WHERE
     eqp_planning_type.assmbl_db_id = an_AssemblyDbId AND
     eqp_planning_type.assmbl_cd    = an_AssemblyCd
     AND
     eqp_planning_type.planning_type_cd = 'STDR'
     AND
     eqp_planning_type.rstat_cd = 0;

 -- Return success
   on_Return := 1;
RETURN;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := -1;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@getStdPlanningTypesForAssembly@@@'||SQLERRM);
RETURN;
END getStdPlanningTypesForAssembly;

/********************************************************************************
*
* Procedure:     getTaskDetails
* Arguments:
*               an_SchedDbId       Task primary key
*               an_SchedId         -- // --
*               on_TaskDbId        Task primary key
*               on_TaskId          -- // --
*               on_TaskDefnDbId    Task definition primary key
*               on_TaskDefnId          -- // --
*               os_TaskClassCd      Task class
*	              on_WorkScopeBool    Workscope bool
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*	              on_ParentTaskDbId   Parent Task primary key
*	              on_ParentTaskId     -- // --
*
*
* Description:  Get the details for a particular Task.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getTaskDetails(
            an_SchedDbId           IN sched_wo_line.wo_sched_db_id%TYPE,
            an_SchedId             IN sched_wo_line.wo_sched_id%TYPE,
            on_TaskDbId            OUT task_task.task_db_id%TYPE,
            on_TaskId              OUT task_task.task_id%TYPE,
            on_TaskDefnDbId        OUT task_task.task_defn_db_id%TYPE,
            on_TaskDefnId          OUT task_task.task_defn_id%TYPE,
            ob_WorkScopeBool       OUT task_task.workscope_bool%TYPE,
            os_TaskClassCd         OUT task_task.task_class_cd%TYPE,
            on_PlanningTypeDbId    OUT task_task.planning_type_db_id%TYPE,
            on_PlanningTypeId      OUT task_task.planning_type_id%TYPE,
            os_PlanningTypeCd      OUT eqp_planning_type.planning_type_cd%TYPE,
            on_ParentTaskDbId      OUT sched_stask.sched_db_id%TYPE,
            on_ParentTaskId        OUT sched_stask.sched_id%TYPE,
            on_Return              OUT NUMBER
   ) IS

BEGIN
   on_Return                := 0;

-- Get the task's details separately from the planning type because if the 
-- planning type is null then this query will not return anything!
SELECT task_task.task_db_id,
       task_task.task_id,
       task_task.task_defn_db_id,
       task_task.task_defn_id,
       task_task.workscope_bool,
       task_task.task_class_cd,
       task_task.planning_type_db_id,
       task_task.planning_type_id,
       vw_evt_stask.nh_sched_db_id,
       vw_evt_stask.nh_sched_id
INTO
       on_TaskDbId,
       on_TaskId,
       on_TaskDefnDbId,
       on_TaskDefnId,
       ob_WorkScopeBool,
       os_TaskClassCd,
       on_PlanningTypeDbId,
       on_PlanningTypeId,
       on_ParentTaskDbId,
       on_ParentTaskId
FROM   vw_evt_stask,
       sched_stask,
       task_task
WHERE  vw_evt_stask.sched_db_id = an_SchedDbId AND
       vw_evt_stask.sched_id    = an_SchedId
       AND
       sched_stask.sched_db_id = vw_evt_stask.sched_db_id AND
       sched_stask.sched_id    = vw_evt_stask.sched_id
       AND
       sched_stask.task_db_id = task_task.task_db_id AND
       sched_stask.task_id    = task_task.task_id;
       
-- Now, get the planning type code       
SELECT eqp_planning_type.planning_type_cd
INTO   os_PlanningTypeCd
FROM   eqp_planning_type
WHERE  eqp_planning_type.planning_type_db_id = on_PlanningTypeDbId AND
       eqp_planning_type.planning_type_id = on_PlanningTypeId;     

       -- Return success
       on_Return := 1;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := -1;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@getTaskDetails@@@'||SQLERRM);
RETURN;

END getTaskDetails;

/********************************************************************************
*
* Procedure:     buildPTAssignedManhoursList
* Arguments:
*               an_lrpEventDbId        Lrp Event key
*               an_lrpEventId          -- // --
*	              an_PlanningTypeDbId    Planning type Db Id
*	              an_PlanningTypeId      Planning type Id
*	              as_PlanningTypeCd      Planning type Cd
*               an_AssignedManHrs      Assigned Manhours to this event
*	              atab_AssManHrsForEvent Table having Assigned manhours and PT for lrp event
*
*
* Description:  Build the list of PT ans associated assingned manhours list for an event.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE buildPTAssignedManhoursList(
    an_lrpEventDbId                  IN lrp_event.lrp_event_db_id%TYPE,
    an_lrpEventId                    IN lrp_event.lrp_event_id%TYPE,
    an_PlanningTypeDbId              IN task_task.planning_type_db_id%TYPE,
    an_PlanningTypeId                IN task_task.planning_type_id%TYPE,
    as_PlanningTypeCd                IN eqp_planning_type.planning_type_cd%TYPE,
    an_AssignedManHrs                IN NUMBER,
    atab_AssManHrsForEvent           IN OUT PTAssignedManhrsRelTable
    )
IS
    lb_PlanningTypeExists       BOOLEAN;
BEGIN

    lb_PlanningTypeExists := FALSE;

       FOR i IN 1..atab_AssManHrsForEvent.COUNT
        LOOP
          IF (
              atab_AssManHrsForEvent(i).lrp_event_db_id     = an_lrpEventDbId     AND
              atab_AssManHrsForEvent(i).lrp_event_id        = an_lrpEventId       AND
              atab_AssManHrsForEvent(i).planning_type_db_id = an_PlanningTypeDbId AND
              atab_AssManHrsForEvent(i).planning_type_id    = an_PlanningTypeId
            ) THEN
             -- If the planning type already exist in the list then
             -- Increment the PT labor hours by new labor hours
             lb_PlanningTypeExists := TRUE;
             atab_AssManHrsForEvent(i).assignedManhours := an_AssignedManHrs + atab_AssManHrsForEvent(i).assignedManhours;
          END IF ;
          EXIT WHEN lb_PlanningTypeExists = TRUE;
        END LOOP;

        IF(lb_PlanningTypeExists = FALSE) THEN
          atab_AssManHrsForEvent.EXTEND;
          atab_AssManHrsForEvent(atab_AssManHrsForEvent.LAST) := PTAssignedManhrsRel(an_lrpEventDbId, an_lrpEventId, an_PlanningTypeDbId,an_PlanningTypeId, as_PlanningTypeCd, an_AssignedManHrs);

        END IF;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@buildPTRoutineManhoursList@@@'||SQLERRM);

RETURN;
END buildPTAssignedManhoursList;

/********************************************************************************
*
* Procedure:     getPTForReqTask
* Arguments:
*               an_SchedDbId       Task primary key
*               an_SchedId         -- // --
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*
*
* Description:  Get the Planning type associated with this task
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getPTForReqTask(
            an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
            an_SchedId           IN sched_stask.sched_id%TYPE,
            on_PlanningTypeDbId  OUT task_task.planning_type_db_id%TYPE,
            on_PlanningTypeId    OUT task_task.planning_type_id%TYPE,
            os_PlanningTypeCd    OUT eqp_planning_type.planning_type_cd%TYPE,
            on_Return            OUT NUMBER
          )IS
  ln_TaskDbId                  task_task.task_db_id%TYPE;
  ln_TaskId                    task_task.task_id%TYPE;
  ln_TaskDefnDbId              task_defn.task_defn_db_id%TYPE;
  ln_TaskDefnId                task_defn.task_defn_id%TYPE;
  ls_TaskClassCd               task_task.task_class_cd%TYPE;
  ln_BlockPlanningTypeDbId     task_task.planning_type_db_id%TYPE;
  ln_BlockPlanningTypeId       task_task.planning_type_id%TYPE;
  ls_BlockPlanningTypeCd       eqp_planning_type.planning_type_cd%TYPE;
  ln_ReqPlanningTypeDbId       task_task.planning_type_db_id%TYPE;
  ln_ReqPlanningTypeId         task_task.planning_type_id%TYPE;
  ls_ReqPlanningTypeCd         eqp_planning_type.planning_type_cd%TYPE;
  lb_WorkScopeBool             task_task.workscope_bool%TYPE;
  ln_ParentDbId                sched_stask.sched_db_id%TYPE;
  ln_ParentId                  sched_stask.sched_id%TYPE;
  ln_NextParentDbId            sched_stask.sched_db_id%TYPE;
  ln_NextParentId              sched_stask.sched_id%TYPE;

  BEGIN
       getTaskDetails(   an_SchedDbId,
                         an_SchedId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_ReqPlanningTypeDbId,
                         ln_ReqPlanningTypeId,
                         ls_ReqPlanningTypeCd,
                         ln_ParentDbId,
                         ln_ParentId,
                         on_Return);

       IF(ln_ReqPlanningTypeDbId IS NULL) THEN
       -- If the PT for requirement task defn is null then
       -- get the PT associated with the requirement's block task defn
         getTaskDetails( ln_ParentDbId,
                         ln_ParentId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_BlockPlanningTypeDbId,
                         ln_BlockPlanningTypeId,
                         ls_BlockPlanningTypeCd,
                         ln_NextParentDbId,
                         ln_NextParentId,
                         on_Return);
           -- Set the reterived planning type
            on_PlanningTypeDbId  := ln_BlockPlanningTypeDbId;
            on_PlanningTypeId    := ln_BlockPlanningTypeId;
            os_PlanningTypeCd    := ls_BlockPlanningTypeCd;
       ELSE
           -- Set the reterived planning type
            on_PlanningTypeDbId  := ln_ReqPlanningTypeDbId;
            on_PlanningTypeId    := ln_ReqPlanningTypeId;
            os_PlanningTypeCd    := ls_ReqPlanningTypeCd;
       END IF;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@getPTForReqTask@@@'||SQLERRM);

RETURN;
END getPTForReqTask;

/********************************************************************************
*
* Procedure:     getPTForJICTask
* Arguments:
*               an_SchedDbId       Task primary key
*               an_SchedId         -- // --
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*
*
* Description:  Get the Planning type associated with this task
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getPTForJICTask(
            an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
            an_SchedId           IN sched_stask.sched_id%TYPE,
            on_PlanningTypeDbId  OUT task_task.planning_type_db_id%TYPE,
            on_PlanningTypeId    OUT task_task.planning_type_id%TYPE,
            os_PlanningTypeCd    OUT eqp_planning_type.planning_type_cd%TYPE,
            on_Return            OUT NUMBER
          )IS
  ln_TaskDbId                  task_task.task_db_id%TYPE;
  ln_TaskId                    task_task.task_id%TYPE;
  ls_TaskClassCd               task_task.task_class_cd%TYPE;
  ln_PlanningTypeDbId          task_task.planning_type_db_id%TYPE;
  ln_PlanningTypeId            task_task.planning_type_id%TYPE;
  ls_PlanningTypeCd            eqp_planning_type.planning_type_cd%TYPE;
  lb_WorkScopeBool             task_task.workscope_bool%TYPE;
  ln_ParentReqDbId             sched_stask.sched_db_id%TYPE;
  ln_ParentReqId               sched_stask.sched_id%TYPE;
  ln_TaskDefnDbId              task_defn.task_defn_db_id%TYPE;
  ln_TaskDefnId                task_defn.task_defn_id%TYPE;
BEGIN

         getTaskDetails( an_SchedDbId,
                         an_SchedId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_PlanningTypeDbId,
                         ln_PlanningTypeId,
                         ls_PlanningTypeCd,
                         ln_ParentReqDbId,
                         ln_ParentReqId,
                         on_Return);

IF( ln_PlanningTypeDbId IS NULL) THEN
-- If the planning type for the JIC task definition is null then
-- get the PT associated with the JIC requirement task defn

       getPTForReqTask(
                              ln_ParentReqDbId,
                              ln_ParentReqId,
                              ln_PlanningTypeDbId,
                              ln_PlanningTypeId,
                              ls_PlanningTypeCd,
                              on_Return);



 END IF;
     -- Set the reterived planning type
     on_PlanningTypeDbId  := ln_PlanningTypeDbId;
     on_PlanningTypeId    := ln_PlanningTypeId;
     os_PlanningTypeCd    := ls_PlanningTypeCd;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@getPTForJICTask@@@'||SQLERRM);

RETURN;
END getPTForJICTask;

/********************************************************************************
*
* Procedure:     getPTForCORRTask
* Arguments:
*               an_SchedDbId       Task primary key
*               an_SchedId         -- // --
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*
*
* Description:  Get the Planning type associated with this task
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getPTForCORRTask(
            an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
            an_SchedId              IN sched_stask.sched_id%TYPE,
            on_PlanningTypeDbId     OUT task_task.planning_type_db_id%TYPE,
            on_PlanningTypeId       OUT task_task.planning_type_id%TYPE,
            os_PlanningTypeCd       OUT eqp_planning_type.planning_type_cd%TYPE,
            on_Return               OUT NUMBER
          )IS
  ls_TaskClassCd               task_task.task_class_cd%TYPE;
  ln_RaisedFromTaskDbId        evt_event_rel.rel_event_db_id%TYPE;
  ln_RaisedFromTaskId          evt_event_rel.rel_event_id%TYPE;
  ln_CORRPlanningTypeDbId      task_task.planning_type_db_id%TYPE;
  ln_CORRPlanningTypeId        task_task.planning_type_id%TYPE;
  ls_CORRPlanningTypeCd        eqp_planning_type.planning_type_cd%TYPE;
  lb_WorkScopeBool             task_task.workscope_bool%TYPE;
  ln_TaskDbId                  task_task.task_db_id%TYPE;
  ln_TaskId                    task_task.task_id%TYPE;
  ln_TaskDefnDbId              task_defn.task_defn_db_id%TYPE;
  ln_TaskDefnId                task_defn.task_defn_id%TYPE;
  ln_ParentDbId                sched_stask.sched_db_id%TYPE;
  ln_ParentId                  sched_stask.sched_id%TYPE;
  ln_NextParentDbId            sched_stask.sched_db_id%TYPE;
  ln_NextParentId              sched_stask.sched_id%TYPE;


BEGIN

SELECT evt_event_rel.event_db_id,
       evt_event_rel.event_id
     INTO
         ln_RaisedFromTaskDbId,
         ln_RaisedFromTaskId
     FROM
         sched_stask,
         evt_event,
         evt_event_rel
     WHERE
          sched_stask.sched_db_id = an_SchedDbId AND
          sched_stask.sched_id    = an_SchedId AND
          sched_stask.rstat_cd    = 0
          AND
          evt_event.event_db_id   = sched_stask.sched_db_id AND
          evt_event.event_id      = sched_stask.sched_id
          AND
          evt_event_rel.rel_event_db_id(+) = evt_event.event_db_id AND
          evt_event_rel.rel_event_id(+)    = evt_event.event_id    AND
          evt_event_rel.rel_type_cd        = 'CORRECT'             AND
          evt_event_rel.rstat_cd           = 0;


  IF( ln_RaisedFromTaskDbId IS NOT NULL) THEN

         getTaskDetails( ln_RaisedFromTaskDbId,
                         ln_RaisedFromTaskId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_CORRPlanningTypeDbId,
                         ln_CORRPlanningTypeId,
                         ls_CORRPlanningTypeCd,
                         ln_ParentDbId,
                         ln_ParentId,
                         on_Return);


     IF( ln_CORRPlanningTypeDbId IS NULL AND ls_TaskClassCd = 'JIC') THEN

       -- If the corrective action is raised from a JIC task
       -- Get planning type associated with the JIC's requirement task defn
         getTaskDetails( ln_ParentDbId,
                         ln_ParentId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_CORRPlanningTypeDbId,
                         ln_CORRPlanningTypeId,
                         ls_CORRPlanningTypeCd,
                         ln_NextParentDbId,
                         ln_NextParentId,
                         on_Return);

     END IF;
END IF;

    -- Set the reterived planning type
     on_PlanningTypeDbId  := ln_CORRPlanningTypeDbId;
     on_PlanningTypeId    := ln_CORRPlanningTypeId;
     os_PlanningTypeCd    := ls_CORRPlanningTypeCd;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@getPTForCORRTask@@@'||SQLERRM);

RETURN;
END getPTForCORRTask;

/********************************************************************************
*
* Procedure:     isRaisedFromTask
* Arguments:
*               an_SchedDbId       Task primary key
*               an_SchedId         -- // --
*
*
* Description:  Checks if this Corrective task was raised from a task
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION isRaisedFromTask(
  an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
  an_SchedId             IN sched_stask.sched_id%TYPE
) RETURN NUMBER
IS
ln_IsRaisedFromTask NUMBER;
BEGIN
ln_IsRaisedFromTask := 0;
     SELECT count(*)
     INTO
         ln_IsRaisedFromTask
     FROM
         sched_stask,
         evt_event,
         evt_event_rel
     WHERE
          sched_stask.sched_db_id = an_SchedDbId AND
          sched_stask.sched_id    = an_SchedId AND
          sched_stask.rstat_cd    = 0
          AND
          evt_event.event_db_id   = sched_stask.sched_db_id AND
          evt_event.event_id      = sched_stask.sched_id
          AND
          evt_event_rel.event_db_id = evt_event.event_db_id AND
          evt_event_rel.event_id    = evt_event.event_id    AND
          evt_event_rel.rel_type_cd = 'CORRECT'             AND
          evt_event_rel.rstat_cd    = 0;

  IF ( ln_IsRaisedFromTask > 0) THEN
     RETURN 1;
  END IF;
     RETURN 0;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@israisedFromTask@@@'||SQLERRM);

END israisedFromTask;


/********************************************************************************
*
* Procedure:     calculateAssignedManhours
* Arguments:
*               an_LrpDbId       LRP plan primary key
*               an_LrpId         -- // --
*
*
* Description:  Calculate assigned manhours for the workscope items of the events in this LRP Plan
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION calculateAssignedManhours(
                         an_LrpDbId lrp_plan.lrp_db_id%Type,
                         an_LrpId   lrp_plan.lrp_id%Type
 )RETURN PTAssignedManhrsRelTable
 IS
  ln_SchedDbId           sched_stask.sched_db_id%TYPE;
  ln_SchedId             sched_stask.sched_id%TYPE;
  ln_TaskDbId            task_task.task_db_id%TYPE;
  ln_TaskId              task_task.task_id%TYPE;
  ln_TaskDefnDbId        task_task.task_defn_db_id%TYPE;
  ln_TaskDefnId          task_task.task_defn_id%TYPE;
  ls_TaskClassCd         task_task.task_class_cd%TYPE;
  lb_WorkScopeBool       task_task.workscope_bool%TYPE;
  ln_PlanningTypeDbId    task_task.planning_type_db_id%TYPE;
  ln_PlanningTypeId      task_task.planning_type_id%TYPE;
  ls_PlanningTypeCd      eqp_planning_type.planning_type_cd%TYPE;
  ln_STDPlanningTypeDbId task_task.planning_type_db_id%TYPE;
  ln_STDPlanningTypeId   task_task.planning_type_id%TYPE;
  ls_STDPlanningTypeCd   eqp_planning_type.planning_type_cd%TYPE;
  ln_lrpEventDbId        lrp_event.lrp_event_db_id%TYPE;
  ln_lrpEventId          lrp_event.lrp_event_id%TYPE;
  ln_ParentDbId          sched_stask.sched_db_id%TYPE;
  ln_ParentId            sched_stask.sched_id%TYPE;

  ln_AssemblyDbId        eqp_assmbl.assmbl_db_id%TYPE;
  ls_AssemblyCd          eqp_assmbl.assmbl_cd%TYPE;
  on_Return              NUMBER;
  l_tab_AssManHrsForEvent PTAssignedManhrsRelTable;


   -- The workscope for the work package might not have been generated yet (because
   -- work package hasn't been started, or workscope hasnn't been committed/generated),
   -- so, we'll read from the list of tasks that'll form the workscope for all the
   -- work packages for this plan
   CURSOR cur_WPToCal(an_LrpDbId lrp_plan.lrp_db_id%Type,an_LrpId lrp_plan.lrp_id%Type) IS
         (
         -- Select every task having a task definition that specifies it to be a
         -- line item.  Note that if a task has a definition, then the definition
         -- workscope flag overrides the task class workscope flag.
          SELECT
            sched_stask.sched_db_id,
            sched_stask.sched_id,
            lrp_event.lrp_event_db_id,
            lrp_event.lrp_event_id,
            evt_inv.assmbl_db_id,
            evt_inv.assmbl_cd
          FROM
            lrp_event,
            evt_event,
            sched_stask,
            sched_stask check_task,
            task_task,
            evt_inv
          WHERE -- Lookup all non-cancelled, non-suppressed subtasks of the check
            lrp_event.lrp_db_id = an_LrpDbId AND
            lrp_event.lrp_id = an_LrpId
            AND
            check_task.task_class_cd = 'CHECK'
            AND
            check_task.sched_db_id = lrp_event.sched_db_id AND
            check_task.sched_id = lrp_event.sched_id
            AND
            check_task.lrp_bool = 1
            AND
            evt_event.h_event_db_id         = check_task.sched_db_id AND
            evt_event.h_event_id            = check_task.sched_id
            AND
            evt_event.event_status_cd NOT IN( 'CANCEL', 'COMPLETE', 'ERROR' )
            AND
            sched_stask.sched_db_id         = evt_event.event_db_id AND
            sched_stask.sched_id            = evt_event.event_id
            AND
            sched_stask.dup_jic_sched_db_id is null AND
            sched_stask.dup_jic_sched_id    is null
            AND
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id = evt_event.event_id
            AND
            sched_stask.rstat_cd				= 0
            AND
            check_task.rstat_cd = 0
            AND
            evt_event.rstat_cd	= 0
            AND
            lrp_event.rstat_cd = 0
            AND
            task_task.rstat_cd = 0
            AND
            evt_inv.rstat_cd = 0
          AND -- Ensure that task definition specifies it to be a line item
            task_task.task_db_id            = sched_stask.task_db_id AND
            task_task.task_id               = sched_stask.task_id AND
            task_task.workscope_bool        = 1

          -- All records from the two queries can be unioned because they are
          -- mutually exclusive
          UNION ALL

          -- Select every task having no task definition and a task class that
          -- specifies it to be a line item.  Since a task definition overrides its
          -- class, in this query we need to exclude all tasks that have a
          -- definition.
          SELECT
            sched_stask.sched_db_id,
            sched_stask.sched_id,
            lrp_event.lrp_event_db_id,
            lrp_event.lrp_event_id,
            evt_inv.assmbl_db_id,
            evt_inv.assmbl_cd
          FROM
            ref_task_class,
            lrp_event,
            evt_event,
            sched_stask,
            sched_stask check_task,
            evt_inv
          WHERE
            lrp_event.lrp_db_id = an_LrpDbId AND
            lrp_event.lrp_id = an_LrpId
            AND
            lrp_event.sched_db_id = check_task.sched_db_id AND
            lrp_event.sched_id = check_task.sched_id
            AND
            check_task.lrp_bool = 1
            AND
            -- Lookup all non-cancelled subtasks of the check
            evt_event.h_event_db_id = check_task.sched_db_id AND
            evt_event.h_event_id = check_task.sched_id
            AND
            evt_event.event_status_cd NOT IN( 'CANCEL', 'COMPLETE', 'ERROR' )
            AND
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id = evt_event.event_id
            AND
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id = evt_event.event_id
            AND
            evt_event.rstat_cd = 0
            AND
            sched_stask.rstat_cd = 0
            AND
            lrp_event.rstat_cd = 0
            AND
            ref_task_class.rstat_cd = 0
            AND
            check_task.rstat_cd = 0
            AND
            evt_inv.rstat_cd = 0
            -- Exclude those that have a task definition
            AND
            sched_stask.task_db_id IS NULL
            AND
            -- Ensure that task class specifies it to be a line item
            ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
            ref_task_class.task_class_cd    = sched_stask.task_class_cd AND
            ref_task_class.workscope_bool   = 1
      );
      TYPE tab_WPToCal IS TABLE OF cur_WPToCal%ROWTYPE INDEX BY PLS_INTEGER;
      ltab_WPToCal tab_WPToCal;
      ln_AssignedManHrs NUMBER;
  BEGIN


   l_tab_AssManHrsForEvent        := PTAssignedManhrsRelTable();
  -- Open cursor
   OPEN cur_WPToCal(an_LrpDbId,an_LrpId);
   LOOP
   FETCH cur_WPToCal BULK COLLECT INTO ltab_WPToCal;
      FOR i IN 1 .. ltab_WPToCal.COUNT
      LOOP
          ln_SchedDbId        := ltab_WPToCal(i).sched_db_id;
          ln_SchedId          := ltab_WPToCal(i).sched_id;
          ln_lrpEventDbId     := ltab_WPToCal(i).lrp_event_db_id;
          ln_lrpEventId       := ltab_WPToCal(i).lrp_event_id;
          ln_AssemblyDbId     := ltab_WPToCal(i).assmbl_db_id;
          ls_AssemblyCd       := ltab_WPToCal(i).assmbl_cd;
      --For each workscope item within a package
       -- Get Planning type

       getTaskDetails(
                         ln_SchedDbId,
                         ln_SchedId,
                         ln_TaskDbId,
                         ln_TaskId,
                         ln_TaskDefnDbId,
                         ln_TaskDefnId,
                         lb_WorkScopeBool,
                         ls_TaskClassCd,
                         ln_PlanningTypeDbId,
                         ln_PlanningTypeId,
                         ls_PlanningTypeCd,
                         ln_ParentDbId,
                         ln_ParentId,
                         on_Return);
           -- Get std plannng type for assembly
        getStdPlanningTypesForAssembly(
                                ln_AssemblyDbId,
                                ls_AssemblyCd,
                                ln_STDPlanningTypeDbId,
                                ln_STDPlanningTypeId,
                                ls_STDPlanningTypeCd,
                                on_Return
                                );
         -- Add this STD PT to list with 0 as default assigned manhours
         buildPTAssignedManhoursList(ln_lrpEventDbId, ln_lrpEventId, ln_STDPlanningTypeDbId, ln_STDPlanningTypeId,
                                                                   ls_STDPlanningTypeCd,0,l_tab_AssManHrsForEvent);

        --Check if this workscope item is a
        -- 1. Corrective action and is not raised from a task OR
        -- 2. Adhoc task
        IF (ls_TaskClassCd = 'ADHOC' OR (ls_TaskClassCd = 'CORR' AND israisedFromTask(ln_SchedDbId,ln_SchedId)=0 )) THEN
           -- Iterate over the list to get the STDR PT and sum up the assigned man hours
            ln_PlanningTypeDbId := ln_STDPlanningTypeDbId;
            ln_PlanningTypeId   := ln_STDPlanningTypeId;
            ls_PlanningTypeCd   := ls_STDPlanningTypeCd;
        ELSE IF (ls_TaskClassCd = 'CORR' AND israisedFromTask(ln_SchedDbId,ln_SchedId)>0 ) THEN
                -- If it is a corrective action and is raised from a task
             getPTForCORRTask( ln_SchedDbId,ln_SchedId,ln_PlanningTypeDbId,ln_PlanningTypeId,ls_PlanningTypeCd, on_Return);

        ELSE IF( ls_TaskClassCd = 'JIC') THEN
             getPTForJICTask( ln_SchedDbId, ln_SchedId, ln_PlanningTypeDbId,ln_PlanningTypeId,ls_PlanningTypeCd, on_Return);

        ELSE IF( lb_WorkScopeBool =1 AND ls_TaskClassCd = 'REQ') THEN
             getPTForReqTask( ln_SchedDbId, ln_SchedId, ln_PlanningTypeDbId,ln_PlanningTypeId,ls_PlanningTypeCd, on_Return);

        END IF;
        END IF;END IF;END IF;

       IF (ln_PlanningTypeDbId IS NULL) THEN
          ln_PlanningTypeDbId := ln_STDPlanningTypeDbId;
          ln_PlanningTypeId   := ln_STDPlanningTypeId;
          ls_PlanningTypeCd   := ls_STDPlanningTypeCd;
       END IF;

       -- Calculate the labour duration for the actual task
       ln_AssignedManHrs := gettasklabourduration( ln_SchedDbId,ln_SchedId);

       buildPTAssignedManhoursList(ln_lrpEventDbId, ln_lrpEventId, ln_PlanningTypeDbId, ln_PlanningTypeId,
                                               ls_PlanningTypeCd, ln_AssignedManHrs,  l_tab_AssManHrsForEvent);

       END LOOP;
      EXIT WHEN cur_WPToCal%NOTFOUND;
   END LOOP;
  CLOSE cur_WPToCal;

  RETURN l_tab_AssManHrsForEvent;

EXCEPTION
   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-544','ASSIGNED_MANHOURS_PKG@@@calculateAssignedManhours@@@'||SQLERRM);

END calculateAssignedManhours;

END ASSIGNED_MANHOURS_PKG;
/