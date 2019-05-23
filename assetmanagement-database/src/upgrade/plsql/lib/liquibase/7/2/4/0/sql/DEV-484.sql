--liquibase formatted sql


--changeSet DEV-484:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PACKAGE_DROP('ROUTINE_MANHOURS_PKG');
END;
/

--changeSet DEV-484:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('TAB_PTROUTINEMANHOURS');
END;
/

--changeSet DEV-484:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTROUTINEMANHOURS');
END;
/

--changeSet DEV-484:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTROUTINEMANHOURS
 IS OBJECT (
    planning_type_db_id NUMBER(10),
    planning_type_id    NUMBER(10),
    planning_type_cd    VARCHAR2(80),
    manhours            NUMBER(10));
/

--changeSet DEV-484:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE TAB_PTROUTINEMANHOURS AS TABLE OF PTRoutineManHours;
/

--changeSet DEV-484:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE ROUTINE_MANHOURS_PKG IS

-- Basic error handling codes
 	icn_Success CONSTANT NUMBER := 1;       -- Success
 	icn_NoProc  CONSTANT NUMBER := 0;       -- No processing done
 	icn_Error   CONSTANT NUMBER := -1;      -- Error

FUNCTION calculateRoutineManhrsForBlock
(
   an_BlockDbId task_task.task_db_id%TYPE,
   an_BlockId   task_task.task_id%TYPE
)RETURN tab_PTRoutineManHours;

END ROUTINE_MANHOURS_PKG;
/

--changeSet DEV-484:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY ROUTINE_MANHOURS_PKG IS

/********************************************************************************
*
* Procedure:     getTaskDefnDetails
* Arguments:
*               an_TaskDbId       Task definition primary key
*               an_TaskId         -- // --
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*
*
* Description:  Get the STDR Planning type associated with the assemnly of the Block.
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

   on_Return                := icn_NoProc;

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
   on_Return := icn_Success;
RETURN;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@getStdPlanningTypesForAssembly@@@'||SQLERRM);
RETURN;
END getStdPlanningTypesForAssembly;
/********************************************************************************
*
* Procedure:     getTaskDefnDetails
* Arguments:
*               an_TaskDbId       Task definition primary key
*               an_TaskId         -- // --
*               os_TaskClassCd      Task class
*	              on_PlanningTypeDbId Planning type Db Id
*	              on_PlanningTypeId   Planning type Id
*	              os_PlanningTypeCd   Planning type Cd
*	              on_AssemblyDbId     Assembly db id
*	              os_AssemblyCd       Assembly cd
*	              on_WorkScopeBool    Workscope bool
*
*
* Description:  Get the details for a particular Block.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE getTaskDefnDetails(
            an_TaskDbId         IN OUT sched_stask.sched_db_id%TYPE,
            an_TaskId           IN OUT sched_stask.sched_id%TYPE,
            os_TaskClassCd      OUT task_task.task_class_cd%TYPE,
	          on_PlanningTypeDbId OUT task_task.planning_type_db_id%TYPE,
	          on_PlanningTypeId   OUT task_task.planning_type_id%TYPE,
	          os_PlanningTypeCd   OUT eqp_planning_type.planning_type_cd%TYPE,
	          on_AssemblyDbId     OUT eqp_assmbl.assmbl_db_id%TYPE,
	          os_AssemblyCd       OUT eqp_assmbl.assmbl_cd%TYPE,
	          on_WorkScopeBool    OUT task_task.workscope_bool%TYPE,
            on_Return           OUT NUMBER
   ) IS

BEGIN
   on_Return                := icn_NoProc;

SELECT DISTINCT
       task_task.task_db_id,
       task_task.task_id,
       ref_task_class.class_mode_cd,
       task_task.planning_type_db_id,
       task_task.planning_type_id,
       eqp_planning_type.planning_type_cd,
       -- assembly details
       eqp_assmbl.assmbl_db_id,
       eqp_assmbl.assmbl_cd,
       task_task.workscope_bool
	INTO
       an_TaskDbId,
       an_TaskId,
  	   os_TaskClassCd,
  	   on_PlanningTypeDbId,
  	   on_PlanningTypeId,
  	   os_PlanningTypeCd,
       on_AssemblyDbId,
       os_AssemblyCd,
  	   on_WorkScopeBool
	FROM
       task_task,
       ref_task_class,
       eqp_planning_type,
       eqp_assmbl,
       eqp_assmbl_bom
 WHERE
       task_task.task_defn_db_id    = an_TaskDbId  AND
       task_task.task_defn_id       = an_TaskId  AND
       task_task.task_def_status_cd = 'ACTV'
       AND
       -- assembly details
       eqp_assmbl.assmbl_db_id      = task_task.assmbl_db_id AND
       eqp_assmbl.assmbl_cd         = task_task.assmbl_cd AND
       eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id AND
       eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd AND
       eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id
       AND
       -- ref terms
       ref_task_class.task_class_db_id(+)        = task_task.task_class_db_id AND
       ref_task_class.task_class_cd(+)           = task_task.task_class_cd
       AND
       eqp_planning_type.planning_type_db_id (+) = task_task.planning_type_db_id AND
       eqp_planning_type.planning_type_id (+)    = task_task.planning_type_id
       AND
       task_task.rstat_cd 		  = 0
       AND
       eqp_assmbl.rstat_cd 		  = 0
       AND
       eqp_assmbl_bom.rstat_cd 		  = 0;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@getTaskDefnDetails@@@'||SQLERRM);
RETURN;

END getTaskDefnDetails;

/********************************************************************************
*
* Function:     getReqForBlock
* Arguments:
*               an_TaskDbId   Task definition primary key
*               an_TaskId     -- // --
*
* Return:       Cursor having the list of Reqs.
*
* Description:  Get the list of Reqs for a particular Block.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION getReqForBlock
(
   an_TaskDbId            task_task.task_db_id%TYPE,
   an_TaskId              task_task.task_id%TYPE,
   on_Return              OUT NUMBER
) RETURN SYS_REFCURSOR
IS
lcur_TaskListCursor sys_refcursor;
BEGIN

   on_Return                := icn_NoProc;
OPEN lcur_TaskListCursor FOR

  SELECT
      req_task.task_db_id,
      req_task.task_id,
      req_task.planning_type_db_id,
      req_task.planning_type_id,
      eqp_planning_type.planning_type_cd,
      ref_task_class.class_mode_cd       AS class_mode_cd,
      req_task.workscope_bool
  FROM
      task_task block_task,
      task_block_req_map,
      task_task req_task,
      task_defn,
      eqp_planning_type,
      ref_task_class
  WHERE
     -- retrieve active blocks
     block_task.task_db_id          = an_TaskDbId AND
     block_task.task_id             = an_TaskId AND
     block_task.task_def_status_cd  = 'ACTV'
     AND
     task_block_req_map.block_task_db_id = block_task.task_db_id AND
     task_block_req_map.block_task_id    = block_task.task_id
     AND
     -- retrieve active requirements associated to a block
     req_task.task_defn_db_id    = task_block_req_map.req_task_defn_db_id AND
     req_task.task_defn_id       = task_block_req_map.req_task_defn_id AND
     req_task.task_def_status_cd = 'ACTV'
     AND
     task_defn.task_defn_db_id 	 = req_task.task_defn_db_id AND
     task_defn.task_defn_id    	 = req_task.task_defn_id
     AND
     eqp_planning_type.planning_type_db_id (+) = req_task.planning_type_db_id AND
     eqp_planning_type.planning_type_id (+)    = req_task.planning_type_id
     AND
     -- ref terms
     ref_task_class.task_class_db_id(+)        = req_task.task_class_db_id AND
     ref_task_class.task_class_cd(+)           = req_task.task_class_cd
     AND
     block_task.rstat_cd 		    = 0
     AND
     task_block_req_map.rstat_cd = 0
     AND
     req_task.rstat_cd 		       = 0;
 -- Return success
   on_Return := icn_Success;

RETURN lcur_TaskListCursor;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@getReqForBlock@@@'||SQLERRM);

END getReqForBlock;
/********************************************************************************
*
* Function:     getJICForReq
* Arguments:
*               an_TaskDbId   Task definition primary key
*               an_TaskId     -- // --
*
* Return:       Cursor having the list of JICs.
*
* Description:  Get the list of JICs for a particular requirement.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION getJICForReq
(
   an_TaskDbId           task_task.task_db_id%TYPE,
   an_TaskId 	           task_task.task_id%TYPE,
   on_Return             OUT NUMBER
)RETURN SYS_REFCURSOR
IS
lcur_JICListCursor sys_refcursor;
BEGIN

   on_Return                := icn_NoProc;
OPEN lcur_JICListCursor FOR

 SELECT
     jic_task.task_db_id AS task_db_id,
     jic_task.task_id AS task_id,
     jic_task.planning_type_db_id,
     jic_task.planning_type_id,
     eqp_planning_type.planning_type_cd,
     ref_task_class.class_mode_cd
  FROM
     task_task jic_task,
     task_task req_task,
     task_jic_req_map,
     eqp_planning_type,
     ref_task_class
  WHERE
     req_task.task_db_id = an_TaskDbId AND
     req_task.task_id    = an_TaskId
     AND
     task_jic_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
     task_jic_req_map.req_task_defn_id    = req_task.task_defn_id
     AND
     jic_task.task_db_id = task_jic_req_map.jic_task_db_id AND
     jic_task.task_id    = task_jic_req_map.jic_task_id AND
     jic_task.task_def_status_cd ='ACTV'
     AND
     eqp_planning_type.planning_type_db_id (+) = jic_task.planning_type_db_id AND
     eqp_planning_type.planning_type_id (+)    = jic_task.planning_type_id
     AND
     -- ref terms
     ref_task_class.task_class_db_id(+)        = jic_task.task_class_db_id AND
     ref_task_class.task_class_cd(+)           = jic_task.task_class_cd
    /* AND
     -- Get the latest assigned JIC revision
     jic_task.revision_ord IN
     (
        SELECT
            MAX (latest_assigned_task_task.revision_ord)
        FROM
            task_task latest_assigned_task_task,
            task_jic_req_map latest_assigned_task_map
        WHERE
           latest_assigned_task_task.task_db_id = latest_assigned_task_map.jic_task_db_id AND
           latest_assigned_task_task.task_id 	 = latest_assigned_task_map.jic_task_id
           AND
           latest_assigned_task_task.task_defn_db_id = jic_task.task_defn_db_id AND
           latest_assigned_task_task.task_defn_id 	= jic_task.task_defn_id
           AND
           latest_assigned_task_map.req_task_defn_db_id = task_jic_req_map.req_task_defn_db_id AND
           latest_assigned_task_map.req_task_defn_id 	= task_jic_req_map.req_task_defn_id
     )*/
     AND
     jic_task.rstat_cd 		  = 0
     AND
     req_task.rstat_cd = 0
     AND
     task_jic_req_map.rstat_cd  = 0 ;

 -- Return success
   on_Return := icn_Success;

RETURN lcur_JICListCursor;
EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@getJICForReq@@@'||SQLERRM);

END getJICForReq;
/********************************************************************************
*
* Function:     getManHoursForTaskDefn
* Arguments:
*               aTaskDbId   Task definition primary key
*               aTaskDbId     -- // --
*
* Return:       Routine man hours.
*
* Description:  Get the assigned task manhours.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION getManHoursForTaskDefn
(
   aTaskDbId                   task_task.task_db_id%TYPE,
   aTaskId                     task_task.task_id%TYPE,
   on_Return                   OUT NUMBER
) RETURN NUMBER
IS
ln_ManHours NUMBER;
BEGIN
   on_Return                := icn_NoProc;

SELECT
   NVL(SUM(task_labour_list.work_perf_hr * task_labour_list.man_pwr_ct),0)
INTO ln_ManHours
FROM
   task_labour_list
WHERE
   task_labour_list.task_db_id = aTaskDbId  AND
   task_labour_list.task_id    = aTaskId
   AND
   task_labour_list.rstat_cd 		  = 0;

 -- Return success
   on_Return := icn_Success;

RETURN ln_ManHours;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@getManHoursForTaskDefn@@@'||SQLERRM);

END getManHoursForTaskDefn;

/********************************************************************************
*
* Function:     buildPTRoutineManhoursList
* Arguments:
*                t_ltab_PTRoutineManHours   List of Planning type with their routine man hours.
*                an_PlanningTypeDbId        Planning type primary key
*                an_PlanningTypeId          -- // --
*                as_PlanningTypeCd          -- // --
*                an_STDPlanningTypeDbId     STD Planning type primary key for assembly
*                an_STDPlanningTypeId       -- // --
*                as_STDPlanningTypeCd       -- // --
*
* Description:  Build the list of Planning type with their routine man hours.
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE buildPTRoutineManhoursList(
    t_ltab_PTRoutineManHours   IN OUT tab_PTRoutineManHours,
    an_PlanningTypeDbId      IN task_task.planning_type_db_id%TYPE,
    an_PlanningTypeId        IN task_task.planning_type_id%TYPE,
    as_PlanningTypeCd        IN eqp_planning_type.planning_type_cd%TYPE,
    an_STDPlanningTypeDbId   IN task_task.planning_type_db_id%TYPE,
    an_STDPlanningTypeId     IN task_task.planning_type_id%TYPE,
    as_STDPlanningTypeCd     IN eqp_planning_type.planning_type_cd%TYPE,
    an_LabourHrs             IN NUMBER
    )
IS
    t_ln_PlanningTypeDbId       task_task.planning_type_db_id%TYPE;
    t_ln_PlanningTypeId         task_task.planning_type_id%TYPE;
    t_ls_PlanningTypeCd         eqp_planning_type.planning_type_cd%TYPE;
    ln_LabourHrs                NUMBER;
    lb_PlanningTypeExists       BOOLEAN;
BEGIN
    ln_LabourHrs := an_LabourHrs;
    lb_PlanningTypeExists := FALSE;

    t_ltab_PTRoutineManHours.EXTEND;
    IF(an_PlanningTypeDbId IS NOT NULL AND
         as_PlanningTypeCd != 'STDR') THEN
      -- Case where non-standard planning type is found
      t_ln_PlanningTypeDbId := an_PlanningTypeDbId;
      t_ln_PlanningTypeId   := an_PlanningTypeId;
      t_ls_PlanningTypeCd   := as_PlanningTypeCd;
      ELSE
      -- Case where:
      -- No planning type is found but labour hours are found, so hours should go to STDR planning type OR
      -- STDR planning type is found
      t_ln_PlanningTypeDbId := an_STDPlanningTypeDbId;
      t_ln_PlanningTypeId   := an_STDPlanningTypeId;
      t_ls_PlanningTypeCd   := as_STDPlanningTypeCd;
      END IF;
       FOR i IN 1..t_ltab_PTRoutineManHours.COUNT
        LOOP
          IF (t_ltab_PTRoutineManHours(i).planning_type_db_id = t_ln_PlanningTypeDbId AND
             t_ltab_PTRoutineManHours(i).planning_type_id     = t_ln_PlanningTypeId
            ) THEN
             -- Increment the PT labor hours by new labor hours
             lb_PlanningTypeExists := TRUE;
             t_ltab_PTRoutineManHours(i).manhours := ln_LabourHrs + t_ltab_PTRoutineManHours(i).manhours;
          END IF ;
          EXIT WHEN lb_PlanningTypeExists = TRUE;
        END LOOP;
        IF(lb_PlanningTypeExists = FALSE) THEN
          t_ltab_PTRoutineManHours(t_ltab_PTRoutineManHours.LAST) := PTRoutineManHours(t_ln_PlanningTypeDbId, t_ln_PlanningTypeId, t_ls_PlanningTypeCd, ln_LabourHrs);
        END IF;

EXCEPTION

   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@buildPTRoutineManhoursList@@@'||SQLERRM);

END buildPTRoutineManhoursList;
/********************************************************************************
*
* Function:     calculateRoutineManhrs
* Arguments:
*                t_ltab_PTRoutineManHours   List of Planning type with their routine man hours.
*                an_ReqTaskDbId             Task definition primary key
*                an_ReqTaskId               -- // --
*                an_ReqPlanningTypeDbId     Planning type primary key
*                an_ReqPlanningTypeId       -- // --
*                as_ReqPlanningTypeCd       -- // --
*	               ab_ReqWorkScopeBool        Workscope bool for requirement
*                an_STDPlanningTypeDbId     STD Planning type primary key for assembly
*                an_STDPlanningTypeId       -- // --
*                as_STDPlanningTypeCd       -- // --
*
* Description:  Calculates the routine manhours associated with a Planning type for the requirement
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
PROCEDURE calculateRoutineManhrs(
    t_ltab_PTRoutineManHours   IN OUT tab_PTRoutineManHours,
    an_ReqTaskDbId             IN sched_stask.sched_db_id%TYPE,
    an_ReqTaskId               IN sched_stask.sched_id%TYPE,
    an_ReqPlanningTypeDbId     IN task_task.planning_type_db_id%TYPE,
    an_ReqPlanningTypeId       IN task_task.planning_type_id%TYPE,
    as_ReqPlanningTypeCd       IN eqp_planning_type.planning_type_cd%TYPE,
	  ab_ReqWorkScopeBool        IN task_task.workscope_bool%TYPE,
    an_STDPlanningTypeDbId     IN task_task.planning_type_db_id%TYPE,
    an_STDPlanningTypeId       IN task_task.planning_type_id%TYPE,
    as_STDPlanningTypeCd       IN eqp_planning_type.planning_type_cd%TYPE
    )
IS
    ln_LabourHrs                NUMBER;
    on_Return                   NUMBER;
    lcur_JICTaskList            sys_refcursor;
    ltab_JICList                mxkeytable;

    ln_JICTaskDbId          	  task_task.task_db_id%TYPE;
  	ln_JICTaskId             	  task_task.task_id%TYPE;
 	  ln_JICPlanningTypeDbId      task_task.planning_type_db_id%TYPE;
  	ln_JICPlanningTypeId        task_task.planning_type_id%TYPE;
  	ls_JICPlanningTypeCd        eqp_planning_type.planning_type_cd%TYPE;
  	ls_JICClassModeCd           task_task.task_class_cd%TYPE;
    lb_IsJICDuplicate           BOOLEAN;

BEGIN

   ln_LabourHrs             := 0;
   on_Return                := icn_NoProc;
   ltab_JICList             := mxkeytable();
   lb_IsJICDuplicate        := FALSE;

	      -- If this req is non executable
        IF( ab_ReqWorkscopeBool= 0) THEN
  	      -- Get the JIC's for req
           lcur_JICTaskList := getJICForReq(
                                  			an_ReqTaskDbId,
                                  			an_ReqTaskId,
                                        on_Return
                                        );
        		LOOP
            FETCH lcur_JICTaskList INTO ln_JICTaskDbId, ln_JICTaskId,ln_JICPlanningTypeDbId,ln_JICPlanningTypeId, ls_JICPlanningTypeCd, ls_JICClassModeCd;
            EXIT WHEN lcur_JICTaskList%NOTFOUND;
            lb_IsJICDuplicate        := FALSE;
            --lb_PlanningTypeExists    := FALSE;
            ln_LabourHrs             := 0;
            ltab_JICList.EXTEND;
            -- Iterate over the JIC list to find if this JIC is duplicate or not
            FOR j IN 1..ltab_JICList.COUNT
              LOOP
                  IF (ln_JICTaskDbId = ltab_JICList(j).db_id AND
                      ln_JICTaskDbId = ltab_JICList(j).id) THEN
                      lb_IsJICDuplicate := TRUE;
                  END IF;
                  EXIT WHEN lb_IsJICDuplicate = TRUE;
              END LOOP;
              -- If this JIC is not duplicate only then count it labor hours
              IF (lb_IsJICDuplicate = FALSE) THEN
                 -- Put this JIC into unique JIC list
                 ltab_JICList(ltab_JICList.LAST) := mxkey( ln_JICTaskDbId, ln_JICTaskId);
            		 -- Get Labour Row
            		 ln_LabourHrs := getManHoursForTaskDefn(ln_JICTaskDbId,ln_JICTaskId,on_Return );
            		 -- Set the Planning type
            		 IF( ln_JICPlanningTypeDbId IS NULL) THEN
                          ln_JICPlanningTypeDbId := an_ReqPlanningTypeDbId;
            		        	ln_JICPlanningTypeId   := an_ReqPlanningTypeId;
            		        	ls_JICPlanningTypeCd   := as_ReqPlanningTypeCd;
            		 END IF;

          	      -- Check if any planning type is found then add the labour req in the list
                  IF( ln_LabourHrs >0) THEN
                  buildPTRoutineManhoursList(
                                             t_ltab_PTRoutineManHours,
                                             ln_JICPlanningTypeDbId,
                                             ln_JICPlanningTypeId,
                                             ls_JICPlanningTypeCd,
                                             an_STDPlanningTypeDbId,
                                             an_STDPlanningTypeId,
                                             as_STDPlanningTypeCd,
                                             ln_LabourHrs);
                 END IF;
            END IF;
            END LOOP;
            CLOSE lcur_JICTaskList;
        ELSE
             -- just find the labour hours for the requirement
  		       ln_LabourHrs := getManHoursForTaskDefn(an_ReqTaskDbId,an_ReqTaskId,on_Return );
             --place for insertion logic
              IF(ln_LabourHrs >0 )THEN
               buildPTRoutineManhoursList(
                                           t_ltab_PTRoutineManHours,
                                           an_ReqPlanningTypeDbId,
                                           an_ReqPlanningTypeId,
                                           as_ReqPlanningTypeCd,
                                           an_STDPlanningTypeDbId,
                                           an_STDPlanningTypeId,
                                           as_STDPlanningTypeCd,
                                           ln_LabourHrs);
               END IF;
        END IF;

   EXCEPTION

   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@calculateRoutineManhrs@@@'||SQLERRM);
END calculateRoutineManhrs;

/********************************************************************************
*
* Function:     calculateRoutineManhrsForBlock
* Arguments:
*               an_BlockDbId   Task definition primary key
*               an_BlockId     -- // --
*
* Return:       List of Planning type wit their routine man hours.
*
* Description:  Calculates the routine manhours associated with a Planning type for the block
*
* Orig.Coder:   Rahul Bakshi
*
*********************************************************************************/
FUNCTION calculateRoutineManhrsForBlock
(
   an_BlockDbId task_task.task_db_id%TYPE,
   an_BlockId   task_task.task_id%TYPE
)RETURN tab_PTRoutineManHours
IS
    ln_PlanningTypeDbId         task_task.planning_type_db_id%TYPE;
    ln_PlanningTypeId           task_task.planning_type_id%TYPE;
    ls_PlanningTypeCd           eqp_planning_type.planning_type_cd%TYPE;
    lb_WorkScopeBool            task_task.workscope_bool%TYPE;
    ln_STDPlanningTypeDbId      task_task.planning_type_db_id%TYPE;
    ln_STDPlanningTypeId        task_task.planning_type_id%TYPE;
    ls_STDPlanningTypeCd        eqp_planning_type.planning_type_cd%TYPE;

    ln_BlockPlanningTypeDbId    task_task.planning_type_db_id%TYPE;
    ln_BlockPlanningTypeId      task_task.planning_type_id%TYPE;
    ls_BlockPlanningTypeCd      eqp_planning_type.planning_type_cd%TYPE;
    ln_BlockAssemblyDbId        eqp_assmbl.assmbl_db_id%TYPE;
    ls_BlockAssemblyCd          eqp_assmbl.assmbl_cd%TYPE;
    ls_BlockTaskClassCd         task_task.task_class_cd%TYPE;

    ln_ReqTaskDbId          	  task_task.task_db_id%TYPE;
  	ln_ReqTaskId             	  task_task.task_id%TYPE;
 	  ln_ReqPlanningTypeDbId      task_task.planning_type_db_id%TYPE;
  	ln_ReqPlanningTypeId        task_task.planning_type_id%TYPE;
  	ls_ReqPlanningTypeCd        eqp_planning_type.planning_type_cd%TYPE;
  	ls_ReqClassModeCd           task_task.task_class_cd%TYPE;
  	lb_ReqWorkscopeBool         task_task.workscope_bool%TYPE;

    lcur_ReqTaskList            sys_refcursor;
    t_ltab_PTRoutineManHours    tab_PTRoutineManHours;
    on_Return                   NUMBER;
    tn_BlockDbId task_task.task_db_id%TYPE;
    tn_BlockId   task_task.task_id%TYPE;
BEGIN

   on_Return                := icn_NoProc;
   t_ltab_PTRoutineManHours := tab_PTRoutineManHours();
   tn_BlockDbId             := an_BlockDbId;
   tn_BlockId               := an_BlockId;
-- get details for block i.e. class mode and planning types
  getTaskDefnDetails(
          	tn_BlockDbId,
          	tn_BlockId,
          	ls_BlockTaskClassCd,
          	ln_BlockPlanningTypeDbId,
          	ln_BlockPlanningTypeId,
          	ls_BlockPlanningTypeCd,
            ln_BlockAssemblyDbId,
            ls_BlockAssemblyCd,
            lb_WorkScopeBool,
            on_Return
            );

-- Set the Planning type
  IF(ln_BlockPlanningTypeDbId IS NOT NULL) THEN
          ln_PlanningTypeDbId := ln_BlockPlanningTypeDbId;
          ln_PlanningTypeId   := ln_BlockPlanningTypeId;
  	      ls_PlanningTypeCd	  := ls_BlockPlanningTypeCd;
  END IF;
  -- Get the stdr planning type for assembly, which will be intial row in the collection with 0 labor hours
  getStdPlanningTypesForAssembly(
                          ln_BlockAssemblyDbId,
                          ls_BlockAssemblyCd,
                          ln_STDPlanningTypeDbId,
                          ln_STDPlanningTypeId,
                          ls_STDPlanningTypeCd,
                          on_Return
                          );
  -- Insert the default row for STDR planning type having 0 labor hours
  t_ltab_PTRoutineManHours.EXTEND;
  t_ltab_PTRoutineManHours(t_ltab_PTRoutineManHours.LAST) := PTRoutineManHours(ln_STDPlanningTypeDbId, ln_STDPlanningTypeId, ls_STDPlanningTypeCd, 0);


  IF (ls_BlockTaskClassCd ='BLOCK') THEN
   -- Get the requirement for the block
   lcur_ReqTaskList := getReqForBlock(
              	           tn_BlockDbId,
              	           tn_BlockId,
                           on_Return
                           );
  -- Get childs and iterate over childs
    LOOP
    FETCH lcur_ReqTaskList INTO ln_ReqTaskDbId, ln_ReqTaskId, ln_ReqPlanningTypeDbId, ln_ReqPlanningTypeId, ls_ReqPlanningTypeCd, ls_ReqClassModeCd, lb_ReqWorkscopeBool;
    EXIT WHEN lcur_ReqTaskList%NOTFOUND;
  -- Set the Planning type
    		IF( ln_ReqPlanningTypeDbId IS NULL) THEN
                 ln_ReqPlanningTypeDbId := ln_PlanningTypeDbId;
    		         ln_ReqPlanningTypeId   := ln_PlanningTypeId;
    			       ls_ReqPlanningTypeCd   := ls_PlanningTypeCd;
    		END IF;
    -- Calculate routine manhours for this req
    calculateRoutineManhrs(
                           t_ltab_PTRoutineManHours,
                           ln_ReqTaskDbId,
                           ln_ReqTaskId,
                           ln_ReqPlanningTypeDbId,
                           ln_ReqPlanningTypeId,
                           ls_ReqPlanningTypeCd,
                           lb_ReqWorkscopeBool,
                           ln_STDPlanningTypeDbId,
                           ln_STDPlanningTypeId,
                           ls_STDPlanningTypeCd);
   END LOOP;
   CLOSE lcur_ReqTaskList;
  ELSE IF(ls_BlockTaskClassCd ='REQ')THEN
       -- Calculate routine manhours for req only as a block in LRP can be a REQ
          calculateRoutineManhrs(
                           t_ltab_PTRoutineManHours,
                           tn_BlockDbId,
                           tn_BlockId,
                           ln_PlanningTypeDbId,
                           ln_PlanningTypeId,
                           ls_PlanningTypeCd,
                           lb_WorkscopeBool,
                           ln_STDPlanningTypeDbId,
                           ln_STDPlanningTypeId,
                           ls_STDPlanningTypeCd);
     END IF;
END IF;

RETURN t_ltab_PTRoutineManHours;

EXCEPTION

   WHEN OTHERS THEN
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-484','ROUTINE_MANHOURS_PKG@@@calculateRoutineManhrsForBlock@@@'||SQLERRM);

END calculateRoutineManhrsForBlock;

/*----------------------- End of Package -----------------------------------*/
END ROUTINE_MANHOURS_PKG;
/

--changeSet DEV-484:8 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'LRP_PLAN_RANGE_ID_SEQ', 1, 'LRP_TASK_PLAN_RANGE', 'TASK_PLAN_RANGE_ID', 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'LRP_PLAN_RANGE_ID_SEQ');

--changeSet DEV-484:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.sequence_create('LRP_PLAN_RANGE_ID_SEQ', 1);
END;
/