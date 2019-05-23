--liquibase formatted sql


--changeSet OPER-27971:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY WARRANTY_EVAL_PKG IS

/********************************************************************************
*
*  Procedure:    DeleteWPEvalResults
*  Arguments:    an_WPSchedDbId (long) - Work Package Key(DbId)
*                an_WPSchedId (long)   - Work Package Key(Id)
*  Description:  This procedure deletes all work package evaluation results
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE DeleteWPEvalResults
(
   an_WPSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_WPSchedId   IN sched_stask.sched_id%TYPE
)
IS
   ln_WtyEvalDbId  warranty_eval.warranty_eval_db_id%TYPE;
   ln_WtyEvalId  warranty_eval.warranty_eval_id%TYPE;
   lCur_WtyEval SYS_REFCURSOR;
BEGIN
   --Get all the warranty eval keys into a cursor
   OPEN lCur_WtyEval FOR
      SELECT
         warranty_eval.warranty_eval_db_id,
         warranty_eval.warranty_eval_id
      FROM
         warranty_eval
      WHERE
         warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
         warranty_eval.wp_sched_id    = an_WPSchedId;
   LOOP
      FETCH lCur_WtyEval
         INTO ln_WtyEvalDbId,
              ln_WtyEvalId;
         EXIT WHEN lCur_WtyEval%NOTFOUND;

      --Delete from the warranty eval part table
      DELETE FROM
         warranty_eval_part
      WHERE
         warranty_eval_part.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_part.warranty_eval_id    = ln_WtyEvalId;

      --Delete from the warranty eval labor table
      DELETE FROM
         warranty_eval_labour
      WHERE
         warranty_eval_labour.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_labour.warranty_eval_id    = ln_WtyEvalId;

      --Delete from the warranty eval task table
      DELETE FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_task.warranty_eval_id    = ln_WtyEvalId;

      --Delete from the warranty eval table
      DELETE FROM
         warranty_eval
      WHERE
         warranty_eval.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval.warranty_eval_id    = ln_WtyEvalId;
   END LOOP;
   CLOSE lCur_WtyEval;
END DeleteWPEvalResults;

/*********************************************************************************
*  Procedure:    isWarrantyExpired
*  Arguments:    an_WtyInitDbId  - Warranty identifier DbId.
*                an_WtyInitId    - Warranty identifier Id.
*  Return:       Bool (1 if the warranty is expired and 0 other wise)
*  Description:  Checks if the warranty is expired.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isWarrantyExpired
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE
)
RETURN NUMBER
IS
   lb_WtyExpireBool NUMBER := 0;
BEGIN
   --Check the warranty evaluation
   SELECT
      warranty_init.expired_bool
   INTO
      lb_WtyExpireBool
   FROM
      warranty_init
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId;

   RETURN lb_WtyExpireBool;
END isWarrantyExpired;

/********************************************************************************
*
*  Procedure:    GetAssOrCompWarrantiesFromWP
*  Arguments:    an_WPSchedDbId (long) - WP Key(DbId)
*                an_WPSchedId   (long) - WP Key(Id)
*  Description:  This procedure takes in a wp key and finds all non expired assembly and
*                component warranties associated with it's main inventory and direct sub
*                assembly and stores it in a scratch pad table(task_warranty_sp).
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetAssOrCompWarrantiesFromWP
(
   an_WPSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_WPSchedId   IN sched_stask.sched_id%TYPE
)
IS
   -- Get non-expired initialized inventory ASSEMBLY or COMPONENT
   -- warranties associated with the main inventory */
   -- of the work package*/
   ln_WtyDbId warranty_init.warranty_init_db_id%TYPE;
   ln_WtyId   warranty_init.warranty_init_id%TYPE;
   ln_WtyTypeDbId warranty_defn.warranty_type_db_id%TYPE;
   lv_WtyTypeCd   warranty_defn.warranty_type_cd%TYPE;

   CURSOR lCur_WPAssCompWarranties IS
      SELECT warranty_init.warranty_init_db_id,
             warranty_init.warranty_init_id,
             warranty_defn.warranty_type_db_id,
             warranty_defn.warranty_type_cd
        FROM evt_inv,
             warranty_init_inv,
             warranty_init,
             warranty_defn
       WHERE evt_inv.event_db_id = an_WPSchedDbId AND
             evt_inv.event_id    = an_WPSchedId
             AND
             --Get the man inventory of the work package
             evt_inv.main_inv_bool = 1
             AND
             (

                   --Get initialized warranties against the main inventory
                   warranty_init_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
                   warranty_init_inv.inv_no_id    = evt_inv.inv_no_id
                   AND
                   warranty_init.warranty_init_db_id = warranty_init_inv.warranty_init_db_id AND
                   warranty_init.warranty_init_id    = warranty_init_inv.warranty_init_id
                   AND
                   warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
                   warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
                   --Warranty of type assembly or component
                   AND
                   (
                      (
                         warranty_defn.warranty_type_db_id = 0 AND
                         warranty_defn.warranty_type_cd    = 'ASSEMBLY'
                      )
                      OR
                      (
                         warranty_defn.warranty_type_db_id = 0 AND
                         warranty_defn.warranty_type_cd    = 'COMPONENT'
                      )
                   )
             );
BEGIN
   --Open the cursor and insert the data into the scratch pad table
   OPEN lCur_WPAssCompWarranties;
   LOOP
      FETCH lCur_WPAssCompWarranties
      INTO  ln_WtyDbId,
            ln_WtyId,
            ln_WtyTypeDbId,
            lv_WtyTypeCd;
      EXIT WHEN lCur_WPAssCompWarranties%NOTFOUND;

      --Insert the Wp,warranty and warranty type key in the scratch pad
      INSERT INTO task_warranty_sp (
         sched_db_id,
         sched_id,
         warranty_init_db_id,
         warranty_init_id,
         warranty_type_db_id,
         warranty_type_cd
         )
      VALUES (
         an_WPSchedDbId,
         an_WPSchedId,
         ln_WtyDbId,
         ln_WtyId,
         ln_WtyTypeDbId,
         lv_WtyTypeCd
         );
   END LOOP;
   CLOSE lCur_WPAssCompWarranties;
END GetAssOrCompWarrantiesFromWP;


/********************************************************************************
*
*  Procedure:    GetTaskWarranties
*  Arguments:    an_SchedDbId (long) - Task Key(DbId)
*                an_SchedId   (long) - Task Key(Id)
*  Description:  This procedure takes in a task key and finds all non expired task warranties
*                associated with class_mode_cd = REQ and ADHOC.and stores it in a scratch pad
*                table(task_warranty_sp).
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetTaskWarranties
(
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE
)IS
   ln_WtyDbId warranty_init_task.warranty_init_db_id%TYPE;
   ln_WtyId   warranty_init_task.warranty_init_id%TYPE;

   ln_WtyTypeDbId warranty_defn.warranty_type_db_id%TYPE;
   lv_WtyTypeCd   warranty_defn.warranty_type_cd%TYPE;

   CURSOR lCur_TaskWarranties IS
      SELECT warranty_init_task.warranty_init_db_id,
             warranty_init_task.warranty_init_id,
             warranty_defn.warranty_type_db_id,
             warranty_defn.warranty_type_cd
        FROM sched_stask,
             ref_task_class,
             warranty_init_task,
             warranty_init,
             warranty_defn
       WHERE sched_stask.sched_db_id = an_SchedDbId AND
             sched_stask.sched_id    = an_SchedId
             AND
             --Make sure the class mode code is either an adhoc or a req
             ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
             ref_task_class.task_class_cd    = sched_stask.task_class_cd
             AND
             ref_task_class.class_mode_cd IN ('ADHOC','REQ')
             AND
             warranty_init_task.sched_db_id = sched_stask.sched_db_id AND
             warranty_init_task.sched_id    = sched_stask.sched_id
             AND
             warranty_init.warranty_init_db_id = warranty_init_task.warranty_init_db_id AND
             warranty_init.warranty_init_id    = warranty_init_task.warranty_init_id
             AND
             --Go to the warranty definition table to make sure they are only task warranties
             warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
             warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
             AND
             warranty_defn.warranty_type_db_id = 0 AND
             warranty_defn.warranty_type_cd    = 'TASK';
BEGIN
   --Open the cursor and insert the data into the scratch pad table
   OPEN lCur_TaskWarranties;
   LOOP
      FETCH lCur_TaskWarranties
      INTO  ln_WtyDbId,
            ln_WtyId,
            ln_WtyTypeDbId,
            lv_WtyTypeCd;
      EXIT WHEN lCur_TaskWarranties%NOTFOUND;

      --Insert the Wp,warranty and warranty type key
      INSERT INTO task_warranty_sp (
         sched_db_id,
         sched_id,
         warranty_init_db_id,
         warranty_init_id,
         warranty_type_db_id,
         warranty_type_cd
         )
      VALUES (
         an_SchedDbId,
         an_SchedId,
         ln_WtyDbId,
         ln_WtyId,
         ln_WtyTypeDbId,
         lv_WtyTypeCd
         );
   END LOOP;
   CLOSE lCur_TaskWarranties;
END  GetTaskWarranties;

/********************************************************************************
*
*  Procedure:    GetAssOrCompWarrantiesFromTask
*  Arguments:    an_TaskSchedDbId (long) - Task Key(DbId)
*                an_TaskSchedId   (long) - Task Key(Id)
*  Description:  This procedure takes in a task key and finds all non expired assembly and
*                component warranties associated with it's main inventory and parent assembly
*                up the tree and stores it in a scratch pad table(task_warranty_sp).
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetAssOrCompWarrantiesFromTask
(
   an_TaskSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_TaskSchedId   IN sched_stask.sched_id%TYPE
)IS
  --Get non-expired initialized COMPONENT warranties associated
  --with main inventory of the task*/
   ln_WtyDbId warranty_init_inv.warranty_init_db_id%TYPE;
   ln_WtyId   warranty_init_inv.warranty_init_id%TYPE;
   ln_WtyTypeDbId warranty_defn.warranty_type_db_id%TYPE;
   lv_WtyTypeCd   warranty_defn.warranty_type_cd%TYPE;

   CURSOR lCur_AssCompWarranties IS
      SELECT warranty_init_inv.warranty_init_db_id,
             warranty_init_inv.warranty_init_id,
             warranty_defn.warranty_type_db_id,
             warranty_defn.warranty_type_cd
        FROM evt_inv,
             warranty_init_inv,
             warranty_init,
             warranty_defn
       WHERE evt_inv.event_db_id = an_TaskSchedDbId AND
             evt_inv.event_id    = an_TaskSchedId
             AND
             --Get the man inventory of the work package
             evt_inv.main_inv_bool = 1
             AND
             (  --Get initialized warranties against the main inventory
                (
                   warranty_init_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
                   warranty_init_inv.inv_no_id    = evt_inv.inv_no_id
                   AND
                   warranty_init.warranty_init_db_id = warranty_init_inv.warranty_init_db_id AND
                   warranty_init.warranty_init_id    = warranty_init_inv.warranty_init_id
                   --Filter out only the warranty of type component
                   AND
                   warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
                   warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
                   AND
                   warranty_defn.warranty_type_db_id = 0 AND
                   warranty_defn.warranty_type_cd    = 'COMPONENT'
                )

             );
BEGIN
   --Open the cursor and insert the data into the scratch pad table
   OPEN lCur_AssCompWarranties;
   LOOP
      FETCH lCur_AssCompWarranties
      INTO  ln_WtyDbId,
            ln_WtyId,
            ln_WtyTypeDbId,
            lv_WtyTypeCd;
      EXIT WHEN lCur_AssCompWarranties%NOTFOUND;

      --Insert the Wp,warranty and warranty type key in the scratch pad
      INSERT INTO task_warranty_sp (
         sched_db_id,
         sched_id,
         warranty_init_db_id,
         warranty_init_id,
         warranty_type_db_id,
         warranty_type_cd
         )
      VALUES(
         an_TaskSchedDbId,
         an_TaskSchedId,
         ln_WtyDbId,
         ln_WtyId,
         ln_WtyTypeDbId,
         lv_WtyTypeCd
         );
   END LOOP;
   CLOSE lCur_AssCompWarranties;
END GetAssOrCompWarrantiesFromTask;

/********************************************************************************
*
*  Procedure:    DeleteTaskEvalResults
*  Arguments:    an_TaskSchedDbId (long) - Task Key(DbId)
*                an_TaskSchedId (long)   - Task Key(Id)
*  Description:  This procedure deletes all Task evaluation results
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE DeleteTaskEvalResults
(
   an_TaskSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_TaskSchedId   IN sched_stask.sched_db_id%TYPE
)
IS
   ln_WtyEvalDbId  warranty_eval_task.warranty_eval_db_id%TYPE;
   ln_WtyEvalId  warranty_eval_task.warranty_eval_id%TYPE;
   lCur_WtyEval SYS_REFCURSOR;
BEGIN
   --Get all the warranty eval keys into a cursor
   OPEN lCur_WtyEval FOR
      SELECT
         warranty_eval_task.warranty_eval_db_id,
         warranty_eval_task.warranty_eval_id
      FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.sched_db_id = an_TaskSchedDbId AND
         warranty_eval_task.sched_id    = an_TaskSchedId;
   LOOP
      FETCH lCur_WtyEval
         INTO ln_WtyEvalDbId,
              ln_WtyEvalId;
         EXIT WHEN lCur_WtyEval%NOTFOUND;

      --Delete from the warranty eval part table
      DELETE FROM
         warranty_eval_part
      WHERE
         warranty_eval_part.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_part.warranty_eval_id    = ln_WtyEvalId;

      --Delete from the warranty eval labor table
      DELETE FROM
         warranty_eval_labour
      WHERE
         warranty_eval_labour.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_labour.warranty_eval_id    = ln_WtyEvalId;

      --Delete from the warranty eval task table
      DELETE FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.warranty_eval_db_id = ln_WtyEvalDbId AND
         warranty_eval_task.warranty_eval_id    = ln_WtyEvalId;
   END LOOP;
   CLOSE lCur_WtyEval;
END DeleteTaskEvalResults;

/********************************************************************************
*
*  Procedure:    hasPartsOrLabor
*  Arguments:    an_TaskDbId  - Task identifier DbId.
*                an_TaskId    - Task identifier Id.
*  Return:       Bool (1 if the task has parts or labor)
*  Description:  Checks if a task has parts or labor
*                takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION hasPartsOrLabor
(
   an_TaskDbId IN evt_event.event_db_id%TYPE,
   an_TaskId   IN evt_event.event_id%TYPE
)
RETURN NUMBER
IS
   ln_LaborCount NUMBER;
   ln_PartCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_LaborCount
   FROM
      sched_labour
   WHERE
      sched_labour.sched_db_id = an_TaskDbId AND
      sched_labour.sched_id    = an_TaskId;

   SELECT
      COUNT(*)
   INTO
      ln_PartCount
   FROM
      sched_part consumed_part,
      sched_rmvd_part
   WHERE
      consumed_part.sched_db_id = an_TaskDbId AND
      consumed_part.sched_id    = an_TaskId
      AND
      sched_rmvd_part.sched_db_id   (+)= consumed_part.sched_db_id AND
      sched_rmvd_part.sched_id      (+)= consumed_part.sched_id AND
      sched_rmvd_part.sched_part_id (+)= consumed_part.sched_part_id;

   IF ln_LaborCount + ln_PartCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END hasPartsOrLabor;
/********************************************************************************
*
*  Procedure:    isTaskOrCompWarranty
*  Arguments:    an_WtyInitDbId  - Warranty identifier DbId.
*                an_WtyInitId    - Warranty identifier Id.
*                an_SchedDbId    - Task (or SubTask) identifier DbId.
*                an_SchedId      - Task (or SubTask) identifier Id.
*  Return:       Bool (1 if a task or component warranty)
*  Description:  Checks if a warranty is of type 'task' or 'component'
*                takes a warranty and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isTaskOrCompWarranty
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE
)
RETURN NUMBER
IS
   ln_WarrantyCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_WarrantyCount
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
      AND
      (
          (
          warranty_defn.warranty_type_db_id = 0 AND
          warranty_defn.warranty_type_cd    = 'TASK'
          AND
          -- Determine if there is a record in warranty_init_task for this warranty and this task, or any of its subtask
          ( an_SchedDbId, an_SchedId ) IN 
		  (SELECT 
		      evt_event.event_db_id, 
			  evt_event.event_id
           FROM 
		      evt_event
           START WITH (evt_event.event_db_id, evt_event.event_id) IN (SELECT warranty_init_task.sched_db_id,
																			 warranty_init_task.sched_id
																		FROM warranty_init_task
																	   WHERE warranty_init_task.warranty_init_db_id = an_WtyInitDbId
																		 AND warranty_init_task.warranty_init_id    = an_WtyInitId)
												 
		   CONNECT BY evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
					  evt_event.nh_event_id    = PRIOR evt_event.event_id)
         )
         OR
         (
            warranty_defn.warranty_type_db_id = 0 AND
            warranty_defn.warranty_type_cd    = 'COMPONENT'
         )
      );

   IF ln_WarrantyCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isTaskOrCompWarranty;

/********************************************************************************
*
*  Procedure:    IsTaskConfigSlotUnderWarranty
*  Arguments:    an_WtyInitDbId  - Warranty DbId
*                an_WtyInitId    - Warranty Id
*                an_WPSchedDbId  - Work Package identifier DbId.
*                an_WPSchedId    - Work Package identifier Id.
*  Return:       1 if it has else 0 if the task does not  have a config slot.
*  Description:  This procedure is responsible for the determining if a tasks config
*                slot is under warranty or not.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION IsTaskConfigSlotUnderWarranty
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_id%TYPE
)
RETURN NUMBER
IS
   ln_ConfigSlotDbId eqp_assmbl_bom.assmbl_db_id%TYPE;
   ln_ConfigWtyDbId eqp_assmbl_bom.assmbl_db_id%TYPE;
   lv_ConfigSlotCd eqp_assmbl_bom.assmbl_cd%TYPE;
   lv_ConfigWtyCd eqp_assmbl_bom.assmbl_cd%TYPE;
   ln_ConfigSlotBomId eqp_assmbl_bom.assmbl_bom_id%TYPE;
   ln_ConfigWtyBomId eqp_assmbl_bom.assmbl_bom_id%TYPE;
   ln_ConfigSlotCount NUMBER :=0;
   ln_ConfigSlotExist NUMBER :=0;
   ls_ConfigSlotStatusDbId warranty_defn.config_slot_state_db_id%TYPE;
   lv_ConfigSlotStatusCd warranty_defn.config_slot_state_cd%TYPE;
   lv_WarrantyTypeCd     warranty_defn.warranty_type_cd%TYPE;
   lCur_ConfigSlotChildren SYS_REFCURSOR;
BEGIN
   --Get the status of the warranty config slot
   SELECT
      warranty_defn.config_slot_state_db_id,
      warranty_defn.config_slot_state_cd,
      warranty_defn.warranty_type_cd
   INTO
      ls_ConfigSlotStatusDbId,
      lv_ConfigSlotStatusCd,
      lv_WarrantyTypeCd
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

   IF lv_WarrantyTypeCd <> 'ASSEMBLY' THEN
      RETURN 0;
   END IF;

   --If the config slot status is 'all' return 1
   IF
      ls_ConfigSlotStatusDbId = 0 AND
      lv_ConfigSlotStatusCd   = 'ALL'
   THEN
      ln_ConfigSlotCount:= 1;
   ELSIF
      ls_ConfigSlotStatusDbId = 0 AND
      lv_ConfigSlotStatusCd   = 'NONE'
   THEN
      ln_ConfigSlotCount:= 0;
   ELSE
      --Check if the task config slot exist first
      SELECT COUNT(*)
      INTO ln_ConfigSlotExist
      FROM
         sched_stask,
         task_task,
         eqp_assmbl_bom
      WHERE
         sched_stask.sched_db_id = an_SchedDbId AND
         sched_stask.sched_id    = an_SchedId
         AND
         task_task.task_db_id = sched_stask.task_db_id AND
         task_task.task_id    = sched_stask.task_id
         AND
         eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id AND
         eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd AND
         eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id;

      IF ln_ConfigSlotExist >= 1 THEN
         --Get the task config slot if it is from task definition
         SELECT
            eqp_assmbl_bom.assmbl_db_id,
            eqp_assmbl_bom.assmbl_cd,
            eqp_assmbl_bom.assmbl_bom_id
         INTO
            ln_ConfigSlotDbId,
            lv_ConfigSlotCd,
            ln_ConfigSlotBomId
         FROM
            sched_stask,
            task_task,
            eqp_assmbl_bom
         WHERE
            sched_stask.sched_db_id = an_SchedDbId AND
            sched_stask.sched_id    = an_SchedId
            AND
            task_task.task_db_id = sched_stask.task_db_id AND
            task_task.task_id    = sched_stask.task_id
            AND
            eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd AND
            eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id;

      ELSIF ln_ConfigSlotExist = 0 THEN
         --Get the config slot of the inventory
         SELECT
            evt_inv.assmbl_db_id,
            evt_inv.assmbl_cd,
            evt_inv.assmbl_bom_id
         INTO
            ln_ConfigSlotDbId,
            lv_ConfigSlotCd,
            ln_ConfigSlotBomId
         FROM
            evt_inv
         WHERE
            evt_inv.event_db_id = an_SchedDbId AND
            evt_inv.event_id    = an_SchedId;
      END IF;

      OPEN lCur_ConfigSlotChildren FOR
         --Load warranty config slots in the cursor and
         --match it to the task config slot
         SELECT
            warranty_terms_config_slots.assmbl_db_id,
            warranty_terms_config_slots.assmbl_cd,
            warranty_terms_config_slots.assmbl_bom_id
         FROM
            warranty_init,
            warranty_terms_config_slots
         WHERE
            warranty_init.warranty_init_db_id = an_WtyInitDbId AND
            warranty_init.warranty_init_id    = an_WtyInitId
            AND
            warranty_terms_config_slots.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
            warranty_terms_config_slots.warranty_defn_id    = warranty_init.warranty_defn_id;
      LOOP
         FETCH lCur_ConfigSlotChildren
         INTO  ln_ConfigWtyDbId,
                  lv_ConfigWtyCd,
                  ln_ConfigWtyBomId;
         EXIT WHEN lCur_ConfigSlotChildren%NOTFOUND;

         SELECT COUNT(*)
         INTO
            ln_ConfigSlotCount
         FROM
            ( --Get the child list of a config slot of the each config slot in the warranty config slot list
            SELECT
               eqp_assmbl_bom.assmbl_db_id,
               eqp_assmbl_bom.assmbl_cd,
               eqp_assmbl_bom.assmbl_bom_id
            FROM
               eqp_assmbl_bom
            START WITH
               eqp_assmbl_bom.assmbl_db_id  = ln_ConfigWtyDbId AND
               eqp_assmbl_bom.assmbl_cd     = lv_ConfigWtyCd   AND
               eqp_assmbl_bom.assmbl_bom_id = ln_ConfigWtyBomId
            CONNECT BY
               eqp_assmbl_bom.nh_assmbl_db_id  = PRIOR eqp_assmbl_bom.assmbl_db_id AND
               eqp_assmbl_bom.nh_assmbl_cd     = PRIOR eqp_assmbl_bom.assmbl_cd AND
               eqp_assmbl_bom.nh_assmbl_bom_id = PRIOR eqp_assmbl_bom.assmbl_bom_id
            ) assembl_children
         WHERE
            --Check if it matches the config slot list found above
            assembl_children.assmbl_db_id  = ln_ConfigSlotDbId AND
            assembl_children.assmbl_cd     = lv_ConfigSlotCd AND
            assembl_children.assmbl_bom_id = ln_ConfigSlotBomId;

         --If atleast one was found exit the loop
         IF ln_ConfigSlotCount >= 1 THEN
            EXIT;
         END IF;
      END LOOP;
      CLOSE lCur_ConfigSlotChildren;

      --If the config slot status is 'exclude'
      IF lv_ConfigSlotStatusCd   = 'EXCLUDE' THEN
         IF ln_ConfigSlotCount >= 1 THEN
            --return 0 if config slot exist
            ln_ConfigSlotCount:= 0;
         ELSE
            ln_ConfigSlotCount:= 1;
         END IF;
      --If the config slot status is 'include'
      ELSIF lv_ConfigSlotStatusCd   = 'INCLUDE' THEN
         IF ln_ConfigSlotCount >= 1 THEN
            --return 0 if config slot exist
            ln_ConfigSlotCount:= 1;
         ELSE
            ln_ConfigSlotCount:= 0;
         END IF;
      END IF;
   END IF;

   -- Return the value
   RETURN ln_ConfigSlotCount;
END IsTaskConfigSlotUnderWarranty;


/********************************************************************************
*  Procedure:    isTaskJIC
*  Arguments:    an_TaskDbId  - Task identifier DbId.
*                an_TaskId    - Task identifier Id.
*  Return:       Bool (1 if the task is a JIC 0 other wise)
*  Description:  Checks if a task is a JIC task
*                takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isTaskJIC
(
   an_TaskDbId IN sched_stask.sched_db_id%TYPE,
   an_TaskId   IN sched_stask.sched_id%TYPE
)
RETURN NUMBER
IS
   ln_JICCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_JICCount
   FROM
      sched_stask,
      ref_task_class
   WHERE
      sched_stask.sched_db_id = an_TaskDbId AND
      sched_stask.sched_id    = an_TaskId
      AND
      ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
      ref_task_class.task_class_cd    = sched_stask.task_class_cd
      AND
      ref_task_class.class_mode_cd = 'JIC';

   IF ln_JICCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isTaskJIC;


/********************************************************************************
*  Procedure:    isOEMOnlyPart
*  Arguments:    an_WtyInitDbId  - Warranty identifier DbId.
*                an_WtyInitId    - Warranty identifier Id.
*  Return:       Bool (1 if the warranty is oem parts only and 0 other wise)
*  Description:  Checks if a warranty is oem parts only
*                takes in the warranty key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isOEMOnlyPart
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE
)
RETURN NUMBER
IS
   ln_OEMPartCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_OEMPartCount
   FROM
      warranty_init,
      warranty_defn
   WHERE
      --Checks if the warranty is a oem parts only warranty
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_init_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
      AND
      warranty_defn.oem_parts_only_bool = 1;

   IF ln_OEMPartCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isOEMOnlyPart;

/********************************************************************************
*  Procedure:    isOEMInventory
*  Arguments:    an_InvNoDbId  - Inventory identifier DbId.
*                an_InvNoId    - Inventory identifier Id.
*  Return:       Bool (1 if the inventory is an oem inventory only and 0 other wise)
*  Description:  Checks if a given inventory is OEM
*                takes in the inventory key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isOEMInventory
(
   an_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
   an_InvNoId   IN inv_inv.inv_no_id%TYPE
)
RETURN NUMBER
IS
   ln_OEMCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_OEMCount
   FROM
      inv_inv_oem_assmbl oem
   WHERE
      --Checks if the warranty is a oem parts only warranty
      oem.inv_no_db_id = an_InvNoDbId AND
      oem.inv_no_id    = an_InvNoId;

   IF ln_OEMCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isOEMInventory;


/********************************************************************************
*  Procedure:    isCompleteTask
*  Arguments:    an_SchedDbId  - Task identifier DbId.
*                an_SchedId    - Task identifier Id.
*  Return:       Bool (1 if the task is complete and 0 other wise)
*  Description:  Checks if the task is complete. This Function
*                takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isCompleteTask
(
   an_SchedDbId IN evt_event.event_db_id%TYPE,
   an_SchedId   IN evt_event.event_id%TYPE
)
RETURN NUMBER
IS
   ln_TaskCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_TaskCount
   FROM
      evt_event
   WHERE
      evt_event.event_db_id = an_SchedDbId AND
      evt_event.event_id    = an_SchedId
      AND
      --Makes sure the task is complete
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd    = 'COMPLETE';

   IF ln_TaskCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isCompleteTask;

/********************************************************************************
*  Procedure:    hasRemovedPart
*  Arguments:    an_TaskDbId  - Task identifier DbId.
*                an_TaskId    - Task identifier Id.
*  Return:       Bool (1 if the task has a removed part and 0 other wise)
*  Description:  Checks if a task is has a removed part
*                takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION hasRemovedPart
(
   an_SchedDbId IN evt_event.event_db_id%TYPE,
   an_SchedId   IN evt_event.event_id%TYPE
)
RETURN NUMBER
IS
   ln_RemovedPartCount NUMBER;
   ln_NonRepairableGroup NUMBER;
   ln_NonRepairablePart NUMBER;

BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_RemovedPartCount
   FROM
      evt_event,
      sched_rmvd_part consumed_part
   WHERE
      --Makes sure the task has removed parts
      evt_event.event_db_id = an_SchedDbId AND
      evt_event.event_id    = an_SchedId
      AND
      consumed_part.sched_db_id = evt_event.event_db_id   AND
      consumed_part.sched_id    = evt_event.event_id;

   -- all non-repairable parts from part-group
   SELECT
      COUNT(*)
   INTO
      ln_NonRepairableGroup
   FROM
      sched_part
      INNER JOIN eqp_part_baseline ON
         eqp_part_baseline.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_part_baseline.bom_part_id = sched_part.sched_bom_part_id
      INNER JOIN eqp_part_no ON
         eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
         eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id
      WHERE
         sched_part.sched_db_id =   an_SchedDbId   AND
         sched_part.sched_id    = an_SchedId
         AND
         eqp_part_no.repair_bool = 0;

   -- non-repairable specified part
   SELECT
      COUNT(*)
   INTO
      ln_NonRepairablePart
   FROM
      sched_part
      INNER JOIN eqp_part_no ON
         eqp_part_no.part_no_db_id = sched_part.spec_part_no_db_id AND
         eqp_part_no.part_no_id    = sched_part.spec_part_no_id
   WHERE
      sched_part.sched_db_id = an_SchedDbId   AND
      sched_part.sched_id    = an_SchedId
      AND
      eqp_part_no.repair_bool = 0;

   IF ln_RemovedPartCount + ln_NonRepairablePart + ln_NonRepairableGroup >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END hasRemovedPart;

/***********************************************************************************
*  Procedure:    UpdateWarrantyEvalPartByInv
*  Arguments:    an_WarrantyEvalDbId - warranty eval identifier DbId.
*                an_StatusDbId   - status identifier DbId.
*                as_StatusCd     - status code.
*                an_WarrantyDbId - warranty identifier DbId.
*                an_WarrantyId   - warranty identifier Id.
*                an_TaskDbId     - Task identifier DbId.
*                an_TaskId       - Task identifier Id.
*                an_InvNoDbId    - Inv identifier DbId.
*                an_InvNoId      - Inv identifier Id.
*  Description:  Updates the Warranty_Eval, Warranty_Eval_Task and Warranty_Eval_Part.
*                In takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWarrantyEvalPartByInv
   (
    an_WarrantyEvalDbId IN warranty_eval.warranty_eval_db_id%TYPE,
    an_StatusDbId       IN warranty_eval.status_db_id%TYPE,
    av_StatusCd         IN warranty_eval.status_cd%TYPE,
    an_WarrantyDbId     IN warranty_eval.warranty_init_db_id%TYPE,
    an_WarrantyId       IN warranty_eval.warranty_init_id%TYPE,
    an_WPSchedDbId      IN sched_stask.sched_db_id%TYPE,
    an_WPSchedId        IN sched_stask.sched_id%TYPE,
    an_TaskDbId         IN sched_stask.sched_db_id%TYPE,
    an_TaskId           IN sched_stask.sched_id%TYPE,
    an_InvNoDbId        IN inv_inv.inv_no_db_id%TYPE,
    an_InvNoId          IN inv_inv.inv_no_id%TYPE
   )
IS
   ln_CountWtyEval NUMBER := 0;
   ln_CountWtyEvalTask NUMBER := 0;
   ln_WarrantyEvalId warranty_eval.warranty_eval_id%TYPE;
   ln_WarrantyEvalTaskId warranty_eval_task.warranty_eval_task_id%TYPE;
   ln_WarrantyEvalPartId warranty_eval_part.warranty_eval_part_id%TYPE;
BEGIN
   --WARRANTY_EVAL
   --Check if the a row exist in warranty_eval table
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEval
   FROM
      warranty_eval
   WHERE
      warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
      warranty_eval.warranty_init_id    = an_WarrantyId
      AND
      warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
      warranty_eval.wp_sched_id    = an_WPSchedId;

   --If it does not exist
   IF ln_CountWtyEval = 0 THEN
      --Get the next value in the sequence
      SELECT
         warranty_eval_id_seq.NEXTVAL
      INTO
         ln_WarrantyEvalId
      FROM
         dual;

      --Insert in to warranty eval table
      INSERT INTO warranty_eval (
         warranty_eval_db_id,
         warranty_eval_id,
         status_db_id,
         status_cd,
         warranty_init_db_id,
         warranty_init_id,
         wp_sched_db_id,
         wp_sched_id,
         reject_hr_db_id,
         reject_hr_id,
         reject_gdt
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         an_StatusDbId,
         av_StatusCd,
         an_WarrantyDbId,
         an_WarrantyId,
         an_WPSchedDbId,
         an_WPSchedId,
         NULL,
         NULL,
         NULL
         );
   ELSE
      SELECT
         warranty_eval.warranty_eval_id
      INTO
         ln_WarrantyEvalId
      FROM
         warranty_eval
      WHERE
         warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
         warranty_eval.warranty_init_id    = an_WarrantyId
         AND
         warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
         warranty_eval.wp_sched_id    = an_WPSchedId;
   END IF;

   --WARRANTY_EVAL_TASK
   --Check if a row exist in warranty_eval_task
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEvalTask
   FROM
      warranty_eval_task
   WHERE
      warranty_eval_task.sched_db_id = an_TaskDbId AND
      warranty_eval_task.sched_id    = an_TaskId
      AND
      warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
      warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;

   IF ln_CountWtyEvalTask = 0 THEN
      --Get the next value in the sequence
      SELECT
         warranty_eval_task_id.NEXTVAL
      INTO
         ln_WarrantyEvalTaskId
      FROM
         dual;

      INSERT INTO warranty_eval_task (
         warranty_eval_db_id,
         warranty_eval_id,
         warranty_eval_task_id,
         sched_db_id,
         sched_id
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         ln_WarrantyEvalTaskId,
         an_TaskDbId,
         an_TaskId
         );
   ELSE
      SELECT
         warranty_eval_task.warranty_eval_task_id
      INTO
         ln_WarrantyEvalTaskId
      FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.sched_db_id = an_TaskDbId AND
         warranty_eval_task.sched_id    = an_TaskId
         AND
         warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
         warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;
   END IF;

   --For each removed part insert a row in warranty_eval_part
   FOR eachInv IN
      (SELECT
         sched_rmvd_part.sched_db_id,
         sched_rmvd_part.sched_id,
         sched_rmvd_part.sched_part_id
      FROM
         sched_rmvd_part
      WHERE
         sched_rmvd_part.inv_no_db_id = an_InvNoDbId AND
         sched_rmvd_part.inv_no_id    = an_InvNoId
         AND
         sched_rmvd_part.sched_db_id = an_TaskDbId AND
         sched_rmvd_part.sched_id    = an_TaskId
      )
   LOOP
      --Get the next sequence value in from the warranty eval table.
      SELECT
         warranty_eval_part_id.NEXTVAL
      INTO
         ln_WarrantyEvalPartId
      FROM
          dual;
      --Insert into the warranty eval part
      INSERT INTO warranty_eval_part (
         warranty_eval_db_id,
         warranty_eval_id,
         warranty_eval_task_id,
         warranty_eval_part_id,
         workscope_sched_db_id,
         workscope_sched_id,
         workscope_sched_part_id,
         rtrn_req_bool
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         ln_WarrantyEvalTaskId,
         ln_WarrantyEvalPartId,
         eachInv.sched_db_id,
         eachInv.sched_id,
         eachInv.sched_part_id,
         1
         );
   END LOOP;
END UpdateWarrantyEvalPartByInv;


/***********************************************************************************
*  Procedure:    UpdateWarrantyEvalLabour
*  Arguments:    an_WarrantyEvalDbId - warranty eval identifier DbId.
*                an_StatusDbId   - status identifier DbId.
*                as_StatusCd     - status code.
*                an_WarrantyDbId - warranty identifier DbId.
*                an_WarrantyId   - warranty identifier Id.
*                an_TaskDbId     - Task identifier DbId.
*                an_TaskId       - Task identifier Id.
*                an_EventDbId    - task identifier DbId.
*                an_EventId      - task identifier Id.
*  Description:  Updates the Warranty_Eval, Warranty_Eval_Task and Warranty_Eval_Part.
*                In takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWarrantyEvalLabour
   (
    an_WarrantyEvalDbId IN warranty_eval.warranty_eval_db_id%TYPE,
    an_StatusDbId       IN warranty_eval.status_db_id%TYPE,
    av_StatusCd         IN warranty_eval.status_cd%TYPE,
    an_WarrantyDbId     IN warranty_eval.warranty_init_db_id%TYPE,
    an_WarrantyId       IN warranty_eval.warranty_init_id%TYPE,
    an_WPSchedDbId      IN sched_stask.sched_db_id%TYPE,
    an_WPSchedId        IN sched_stask.sched_id%TYPE,
    an_TaskSaveDbId     IN sched_stask.sched_db_id%TYPE,
    an_TaskSaveId       IN sched_stask.sched_id%TYPE,
    an_EventDbId        IN evt_event.event_db_id%TYPE,
    an_EventId          IN evt_event.event_id%TYPE
   )
IS
   ln_CountWtyEval NUMBER := 0;
   ln_CountWtyEvalTask NUMBER := 0;
   ln_WarrantyEvalId warranty_eval.warranty_eval_id%TYPE;
   ln_WarrantyEvalTaskId warranty_eval_task.warranty_eval_task_id%TYPE;
   ln_warrantyEvalLabourId warranty_eval_labour.warranty_eval_labour_id%TYPE;
BEGIN
   --WARRANTY_EVAL
   --Check if the a row exist in warranty_eval table
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEval
   FROM
      warranty_eval
   WHERE
      warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
      warranty_eval.warranty_init_id    = an_WarrantyId
      AND
      warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
      warranty_eval.wp_sched_id    = an_WPSchedId;

   --If it does not exist
   IF ln_CountWtyEval = 0 THEN
      --Get the next value in the sequence
      SELECT
         warranty_eval_id_seq.NEXTVAL
      INTO
         ln_WarrantyEvalId
      FROM
         dual;

      --Insert in to warranty eval table
      INSERT INTO warranty_eval (
         warranty_eval_db_id,
         warranty_eval_id,
         status_db_id,
         status_cd,
         warranty_init_db_id,
         warranty_init_id,
         wp_sched_db_id,
         wp_sched_id,
         reject_hr_db_id,
         reject_hr_id,
         reject_gdt
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         an_StatusDbId,
         av_StatusCd,
         an_WarrantyDbId,
         an_WarrantyId,
         an_WPSchedDbId,
         an_WPSchedId,
         NULL,
         NULL,
         NULL
         );
   ELSE
      --Get the warranty_eval_id
      SELECT
         warranty_eval.warranty_eval_id
      INTO
         ln_WarrantyEvalId
      FROM
         warranty_eval
      WHERE
         warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
         warranty_eval.warranty_init_id    = an_WarrantyId
         AND
         warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
         warranty_eval.wp_sched_id    = an_WPSchedId;
   END IF;

   --WARRANTY_EVAL_TASK
   --Check if a row exist in warranty_eval_task
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEvalTask
   FROM
      warranty_eval_task
   WHERE
      warranty_eval_task.sched_db_id = an_TaskSaveDbId AND
      warranty_eval_task.sched_id    = an_TaskSaveId
      AND
      warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
      warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;

   --If a row doesn't exist, insert a row into warranty_eval_task
   IF ln_CountWtyEvalTask = 0 THEN
      --Get the next value in the sequence
      SELECT
         warranty_eval_task_id.NEXTVAL
      INTO
         ln_WarrantyEvalTaskId
      FROM
         dual;

      INSERT INTO warranty_eval_task (
         warranty_eval_db_id,
         warranty_eval_id,
         warranty_eval_task_id,
         sched_db_id,
         sched_id
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         ln_WarrantyEvalTaskId,
         an_TaskSaveDbId,
         an_TaskSaveId
         );
   ELSE
      --Get the warranty_eval_taskId variable
      SELECT
         warranty_eval_task.warranty_eval_task_id
      INTO
         ln_WarrantyEvalTaskId
      FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.sched_db_id = an_TaskSaveDbId AND
         warranty_eval_task.sched_id    = an_TaskSaveId
         AND
         warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
         warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;
   END IF;

   --WARRANTY EVAL LABOUR
   --For each removed part insert a row in warranty_eval_part
   FOR eachInv IN
      (SELECT
         sched_labour.labour_db_id,
         sched_labour.labour_id
      FROM
         sched_labour
      WHERE
         sched_labour.sched_db_id = an_EventDbId AND
         sched_labour.sched_id    = an_EventId
      )
   LOOP
      --Get the next sequence value in from the warranty eval table.
      SELECT
         warranty_eval_labour_id.NEXTVAL
      INTO
         ln_warrantyEvalLabourId
      FROM
         dual;
      --Insert into the warranty eval part
      INSERT INTO warranty_eval_labour (
         warranty_eval_db_id,
         warranty_eval_id,
         warranty_eval_task_id,
         warranty_eval_labour_id,
         labour_db_id,
         labour_id
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         ln_WarrantyEvalTaskId,
         ln_warrantyEvalLabourId,
         eachInv.labour_db_id,
         eachInv.labour_id
         );
   END LOOP;
END UpdateWarrantyEvalLabour;


/***********************************************************************************
*  Procedure:    UpdateWarrantyEvalPartByReq
*  Arguments:    an_WarrantyEvalDbId - warranty eval identifier DbId.
*                an_StatusDbId   - status identifier DbId.
*                as_StatusCd     - status code.
*                an_WarrantyDbId - warranty identifier DbId.
*                an_WarrantyId   - warranty identifier Id.
*                an_TaskDbId     - Task identifier DbId.
*                an_TaskId       - Task identifier Id.
*                an_InvNoDbId    - Inv identifier DbId.
*                an_InvNoId      - Inv identifier Id.
*  Description:  Updates the Warranty_Eval, Warranty_Eval_Task and Warranty_Eval_Part.
*                In takes in the task key.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWarrantyEvalPartByReq
   (
    an_WarrantyEvalDbId IN warranty_eval.warranty_eval_db_id%TYPE,
    an_StatusDbId       IN warranty_eval.status_db_id%TYPE,
    av_StatusCd         IN warranty_eval.status_cd%TYPE,
    an_WarrantyDbId     IN warranty_eval.warranty_init_db_id%TYPE,
    an_WarrantyId       IN warranty_eval.warranty_init_id%TYPE,
    an_WPSchedDbId      IN sched_stask.sched_db_id%TYPE,
    an_WPSchedId        IN sched_stask.sched_id%TYPE,
    an_TaskDbId         IN sched_stask.sched_db_id%TYPE,
    an_TaskId           IN sched_stask.sched_id%TYPE,
    an_wsSchedDbId      IN sched_part.sched_db_id%TYPE,
    an_wsSchedId        IN sched_part.sched_id%TYPE,
    an_wsSchedPartId    IN sched_part.sched_part_id%TYPE,
    an_rtrn_req_bool    IN NUMBER
   )
IS
   ln_CountWtyEvalTaskPart NUMBER := 0;
   ln_CountWtyEval NUMBER := 0;
   ln_CountWtyEvalTask NUMBER := 0;
   ln_WarrantyEvalId warranty_eval.warranty_eval_id%TYPE;
   ln_WarrantyEvalTaskId warranty_eval_task.warranty_eval_task_id%TYPE;
   ln_WarrantyEvalPartId warranty_eval_part.warranty_eval_part_id%TYPE;
BEGIN
   --WARRANTY_EVAL
   --Check if the a row exist in warranty_eval table
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEval
   FROM
      warranty_eval
   WHERE
      warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
      warranty_eval.warranty_init_id    = an_WarrantyId
      AND
      warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
      warranty_eval.wp_sched_id    = an_WPSchedId;

   --If it does not exist
   IF ln_CountWtyEval = 0  THEN
      --Get the next value in the sequence
      SELECT
         warranty_eval_id_seq.NEXTVAL
      INTO
         ln_WarrantyEvalId
      FROM
         dual;

      --Insert in to warranty eval table
      INSERT INTO warranty_eval (
         warranty_eval_db_id,
         warranty_eval_id,
         status_db_id,
         status_cd,
         warranty_init_db_id,
         warranty_init_id,
         wp_sched_db_id,
         wp_sched_id,
         reject_hr_db_id,
         reject_hr_id,
         reject_gdt
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         an_StatusDbId,
         av_StatusCd,
         an_WarrantyDbId,
         an_WarrantyId,
         an_WPSchedDbId,
         an_WPSchedId,
         NULL,
         NULL,
         NULL
         );
   ELSE
      --Get the warranty_eval_id
      SELECT
         warranty_eval.warranty_eval_id
      INTO
         ln_WarrantyEvalId
      FROM
         warranty_eval
      WHERE
         warranty_eval.warranty_init_db_id = an_WarrantyDbId AND
         warranty_eval.warranty_init_id    = an_WarrantyId
         AND
         warranty_eval.wp_sched_db_id = an_WPSchedDbId AND
         warranty_eval.wp_sched_id    = an_WPSchedId;
   END IF;

   --WARRANTY_EVAL_TASK
   --Check if a row exist in warranty_eval_task
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyEvalTask
   FROM
      warranty_eval_task
   WHERE
      warranty_eval_task.sched_db_id = an_TaskDbId AND
      warranty_eval_task.sched_id    = an_TaskId
      AND
      warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
      warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;

   IF ln_CountWtyEvalTask = 0 THEN
     --Get the next value in the sequence
     SELECT
        warranty_eval_task_id.NEXTVAL
     INTO
        ln_WarrantyEvalTaskId
     FROM
        dual;

     INSERT INTO warranty_eval_task (
        warranty_eval_db_id,
        warranty_eval_id,
        warranty_eval_task_id,
        sched_db_id,
        sched_id
        )
     VALUES (
        an_WarrantyEvalDbId,
        ln_WarrantyEvalId,
        ln_WarrantyEvalTaskId,
        an_TaskDbId,
        an_TaskId
        );
   ELSE
      --Get the warranty_eval_taskId variable
      SELECT
         warranty_eval_task.warranty_eval_task_id
      INTO
         ln_WarrantyEvalTaskId
      FROM
         warranty_eval_task
      WHERE
         warranty_eval_task.sched_db_id = an_TaskDbId AND
         warranty_eval_task.sched_id    = an_TaskId
         AND
         warranty_eval_task.warranty_eval_db_id = an_WarrantyEvalDbId AND
         warranty_eval_task.warranty_eval_id    = ln_WarrantyEvalId;
   END IF;

   --WARRANTY EVAL PART
   -- Insert into warranty_eval_part IFF the part is not already specified for this task
   SELECT
      COUNT(1)
   INTO
      ln_CountWtyEvalTaskPart
   FROM
      warranty_eval_part
   WHERE
      warranty_eval_part.warranty_eval_db_id = an_WarrantyEvalDbId AND
      warranty_eval_part.warranty_eval_id    = ln_WarrantyEvalId AND
      warranty_eval_part.warranty_eval_task_id = ln_WarrantyEvalTaskId
      AND
      warranty_eval_part.workscope_sched_db_id = an_wsSchedDbId AND
      warranty_eval_part.workscope_sched_id    = an_wsSchedId AND
      warranty_eval_part.workscope_sched_part_id = an_wsSchedPartId;

   IF ln_CountWtyEvalTaskPart <= 0 THEN
      --Get the next sequence value in from the warranty eval table.
      SELECT
         warranty_eval_part_id.NEXTVAL
      INTO
         ln_WarrantyEvalPartId
      FROM
          dual;

      --Insert into the warranty eval part
      INSERT INTO warranty_eval_part (
         warranty_eval_db_id,
         warranty_eval_id,
         warranty_eval_task_id,
         warranty_eval_part_id,
         workscope_sched_db_id,
         workscope_sched_id,
         workscope_sched_part_id,
         rtrn_req_bool
         )
      VALUES (
         an_WarrantyEvalDbId,
         ln_WarrantyEvalId,
         ln_WarrantyEvalTaskId,
         ln_WarrantyEvalPartId,
         an_wsSchedDbId,
         an_wsSchedId,
         an_wsSchedPartId,
         an_rtrn_req_bool
         );
    END IF;
END UpdateWarrantyEvalPartByReq;



/***********************************************************************************
*  Procedure:    UpdateWarrantyInitInv
*  Arguments:    an_WarrantyDbId - warranty identifier DbId.
*                an_WarrantyId   - warranty identifier Id.
*                an_InvNoDbId    - Inv identifier DbId.
*                an_InvNoId      - Inv identifier Id.
*  Description:  Update the warranty init inv table.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateWarrantyInitInv
   (
    an_WarrantyDbId     IN warranty_init_inv.warranty_init_db_id%TYPE,
    an_WarrantyId       IN warranty_init_inv.warranty_init_id%TYPE,
    an_InvNoDbId        IN inv_inv.inv_no_db_id%TYPE,
    an_InvNoId          IN inv_inv.inv_no_id%TYPE
   )
IS
   ln_CountWtyInv NUMBER := 0;
BEGIN
   -- Is this inventory already associated with the warranty's definition?
   SELECT
      COUNT(*)
   INTO
      ln_CountWtyInv
   FROM
      warranty_init,
      warranty_init_inv,
      (SELECT
         warranty_defn_db_id, warranty_defn_id
      FROM
         warranty_init
      WHERE
         warranty_init.warranty_init_db_id = an_WarrantyDbId AND
         warranty_init.warranty_init_id    = an_WarrantyId
      ) warranty_defn
   WHERE
      warranty_init.warranty_defn_db_id = warranty_defn.warranty_defn_db_id AND
      warranty_init.warranty_defn_id    = warranty_defn.warranty_defn_id
      AND
      warranty_init_inv.inv_no_db_id = an_InvNoDbId AND
      warranty_init_inv.inv_no_id    = an_InvNoId;

   --If the inventory is not associated with the warranty's definition.
   IF ln_CountWtyInv = 0 THEN
      --Insert in to warranty init inv table
      INSERT INTO warranty_init_inv (
         warranty_init_db_id,
         warranty_init_id,
         inv_no_db_id,
         inv_no_id,
         master_bool
         )
      VALUES (
         an_WarrantyDbId,
         an_WarrantyId,
         an_InvNoDbId,
         an_InvNoId,
         0
         );
   END IF;
END UpdateWarrantyInitInv;

/********************************************************************************
*  Procedure:    isRoutineTaskIncluded
*  Arguments:    an_SchedDbId  - Task identifier DbId.
*                an_SchedId    - Task identifier Id.
*                an_WtyInitDbId (long) - WarrantyKey(DbId)
*                an_WtyInitId  (long)  - WarrantyKey(Id)
*  Return:       Bool (1 if the task is complete and 0 other wise)
*  Description:  Checks if the task is a routine task. This Function
*                takes in the task key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isRoutineTaskIncluded
(
   an_WarrantyDbId     IN warranty_init_inv.warranty_init_db_id%TYPE,
   an_WarrantyId       IN warranty_init_inv.warranty_init_id%TYPE,
   an_SchedDbId IN evt_event.event_db_id%TYPE,
   an_SchedId   IN evt_event.event_id%TYPE
)
RETURN NUMBER
IS
   ln_TaskCount NUMBER;
   ln_RoutineBool NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_TaskCount
   FROM
      sched_stask
   WHERE
      sched_stask.sched_db_id = an_SchedDbId AND
      sched_stask.sched_id    = an_SchedId
      AND
      --Makes sure the task is a routine task
      sched_stask.routine_bool = 1;

   SELECT
      COUNT(*)
   INTO
      ln_RoutineBool
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WarrantyDbId AND
      warranty_init.warranty_init_id    = an_WarrantyId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
      AND
      warranty_defn.routine_tasks_bool = 1;

   IF
      ln_TaskCount = 0
      OR
      (
         ln_TaskCount >= 1
         AND
         ln_RoutineBool >= 1
      )
   THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isRoutineTaskIncluded;



/********************************************************************************
*
*  Procedure:    IsRepPartUnderWarranty
*  Arguments:    an_WtyInitDbId  - Warranty DbId
*                an_WtyInitId    - Warranty Id
*                an_PartNoDbId   - PartNoDbId.
*                an_PartNoId     - an_PartNoId.
*  Return:       1 if it has else 0 if the task does not  have a config slot.
*  Description:  This procedure is responsible for the filtering out of tasks
*                whose config slots are not covered under warranty.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

FUNCTION IsRepPartUnderWarranty
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_PartNoDbId IN eqp_part_no.part_no_db_id%TYPE,
   an_PartNoId   IN eqp_part_no.part_no_id%TYPE
)
RETURN NUMBER
IS
   ln_PartNoCount NUMBER;
   ln_PartNoStatusDbId warranty_defn.rep_parts_state_db_id%TYPE;
   lv_PartNoStatusCd warranty_defn.rep_parts_state_cd%TYPE;
BEGIN
   --Get the status of the warranty config slot
   SELECT
      warranty_defn.rep_parts_state_db_id,
      warranty_defn.rep_parts_state_cd
   INTO
      ln_PartNoStatusDbId,
      lv_PartNoStatusCd
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

   --If the ln_PartNoConfig slot status is 'all' return 1
   IF
      ln_PartNoStatusDbId = 0 AND
      lv_PartNoStatusCd   = 'ALL'
   THEN
      ln_PartNoCount:= 1;
   --If the config slot status is 'none' return 0
   ELSIF
      ln_PartNoStatusDbId = 0 AND
      lv_PartNoStatusCd   = 'NONE'
   THEN
      ln_PartNoCount:= 0;
   ELSE
      --Get the task config slot
      SELECT COUNT(*)
      INTO ln_PartNoCount
      FROM
         warranty_init,
         warranty_defn,
         warranty_terms_rep_parts
      WHERE
         warranty_init.warranty_init_db_id = an_WtyInitDbId AND
         warranty_init.warranty_init_id    = an_WtyInitId
         AND
         warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
         warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
         AND
         warranty_terms_rep_parts.warranty_defn_db_id = warranty_defn.warranty_defn_db_id AND
         warranty_terms_rep_parts.warranty_defn_id    = warranty_defn.warranty_defn_id
         AND
         warranty_terms_rep_parts.part_no_db_id = an_PartNoDbId AND
         warranty_terms_rep_parts.part_no_id    = an_PartNoId;

      --If the part no status is 'exclude'
      IF
         ln_PartNoStatusDbId = 0 AND
         lv_PartNoStatusCd   = 'EXCLUDE'
      THEN
         IF ln_PartNoCount >= 1 THEN
            --return part no exist
            ln_PartNoCount:= 0;
         ELSE
            ln_PartNoCount:= 1;
         END IF;
      --If the part no status is 'include'
      ELSIF
         ln_PartNoStatusDbId  = 0 AND
         lv_PartNoStatusCd    = 'INCLUDE'
      THEN
         IF ln_PartNoCount >= 1 THEN
            --return 0 if part no count exist
            ln_PartNoCount:= 1;
         ELSE
            ln_PartNoCount:= 0;
         END IF;
      END IF;
   END IF;

   -- Return the value
   RETURN ln_PartNoCount;
END IsRepPartUnderWarranty;


/********************************************************************************
*  Procedure:    isPartNoAupAboveThreshhold
*  Arguments:    an_PartNoDbId  - Part No Db Id.
*                an_PartNoId    - Part No Id.
*  Return:       Bool (1 if this function evaluates to true and 0 other wise)
*  Description:  Checks if rep part no Average Unit Price is greater than the
*                warranty threshold.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isPartNoAupAboveThreshhold
(
   an_PartNoDbId IN eqp_part_no.part_no_db_id%TYPE,
   an_PartNoId   IN eqp_part_no.part_no_id%TYPE,
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE
)
RETURN NUMBER
IS
   ln_Threshold warranty_defn.unit_price_threshold_qt%TYPE;
   ln_ThresholdCount NUMBER :=0;
   ln_RepPartCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_ThresholdCount
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

   IF ln_ThresholdCount >= 1 THEN
      SELECT
         warranty_defn.unit_price_threshold_qt
      INTO
         ln_Threshold
      FROM
         warranty_init,
         warranty_defn
      WHERE
         warranty_init.warranty_init_db_id = an_WtyInitDbId AND
         warranty_init.warranty_init_id    = an_WtyInitId
         AND
         warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
         warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

      SELECT
         COUNT(*)
      INTO
         ln_RepPartCount
      FROM
         eqp_part_no
      WHERE
         --Makes sure the avg unit price of the part no is greater than
         --the threshold price
         eqp_part_no.part_no_db_id = an_PartNoDbId   AND
         eqp_part_no.part_no_id    = an_PartNoId
         AND
         eqp_part_no.avg_unit_price >= ln_Threshold;
   END IF;

   IF ln_RepPartCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isPartNoAupAboveThreshhold;

/********************************************************************************
*  Procedure:    isAupLessThanTurnInThreshold
*  Arguments:    an_PartNoDbId  - Part No Db Id.
*                an_PartNoId    - Part No Id.
*  Return:       Bool (1 if this function evaluates to true and 0 other wise)
*  Description:  Checks if rep part no Average Unit Price is greater than the
*                turn-in threshold.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isAupLessThanTurnInThreshold
(
   an_PartNoDbId IN eqp_part_no.part_no_db_id%TYPE,
   an_PartNoId   IN eqp_part_no.part_no_id%TYPE,
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE
)
RETURN NUMBER
IS
   ln_Threshold warranty_defn.turn_in_price_threshold_qt%TYPE;
   ln_RepPartCount NUMBER;
   ln_TurnInCount NUMBER :=0;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_TurnInCount
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

   IF ln_TurnInCount>=1 THEN

      SELECT
         warranty_defn.turn_in_price_threshold_qt
      INTO
         ln_Threshold
      FROM
         warranty_init,
         warranty_defn
      WHERE
         warranty_init.warranty_init_db_id = an_WtyInitDbId AND
         warranty_init.warranty_init_id    = an_WtyInitId
         AND
         warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
         warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

      SELECT
         COUNT(*)
      INTO
         ln_RepPartCount
      FROM
         eqp_part_no
      WHERE
         --Makes sure the avg unit price of the part no is greater than
         --the threshold price
         eqp_part_no.part_no_db_id = an_PartNoDbId   AND
         eqp_part_no.part_no_id    = an_PartNoId
         AND
         eqp_part_no.avg_unit_price <= ln_Threshold;
   END IF;

   IF ln_RepPartCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isAupLessThanTurnInThreshold;

/********************************************************************************
*
*  Procedure:    IsNonRepPartUnderWarranty
*  Arguments:    an_WtyInitDbId  - Warranty DbId
*                an_WtyInitId    - Warranty Id
*                an_PartNoDbId   - PartNoDbId.
*                an_PartNoId     - an_PartNoId.
*  Return:       1 if it has else 0 if the warranty includes a non rep part.
*  Description:  This procedure is responsible for the filtering out of tasks
*                whose config slots are not covered under warranty.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

FUNCTION IsNonRepPartUnderWarranty
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_PartNoDbId IN eqp_part_no.part_no_db_id%TYPE,
   an_PartNoId   IN eqp_part_no.part_no_id%TYPE
)
RETURN NUMBER
IS
   ln_PartNoCount NUMBER := 0;
   ln_PartNoStatusDbId warranty_defn.non_rep_parts_state_db_id%TYPE;
   lv_PartNoStatusCd warranty_defn.non_rep_parts_state_cd%TYPE;
BEGIN
   --Get the status of the warranty non rep parts
   SELECT
      warranty_defn.non_rep_parts_state_db_id,
      warranty_defn.non_rep_parts_state_cd
   INTO
      ln_PartNoStatusDbId,
      lv_PartNoStatusCd
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id;

   --If the ln_PartNo slot status is 'all' return 1
   IF
      ln_PartNoStatusDbId = 0 AND
      lv_PartNoStatusCd   = 'ALL'
   THEN
      IF
         isPartNoAupAboveThreshhold(
               an_PartNoDbId, an_PartNoId,
               an_WtyInitDbId, an_WtyInitId
            ) = 1
      THEN
         ln_PartNoCount:= 1;
      END IF;
   --If the config slot status is 'all' return 1
   ELSIF
      ln_PartNoStatusDbId = 0 AND
      lv_PartNoStatusCd   = 'NONE'
   THEN
      ln_PartNoCount:= 0;
   ELSE
      --Get the task config slot
      SELECT COUNT(*)
      INTO ln_PartNoCount
      FROM
         warranty_init,
         warranty_defn,
         warranty_terms_non_rep_parts
      WHERE
         warranty_init.warranty_init_db_id = an_WtyInitDbId AND
         warranty_init.warranty_init_id    = an_WtyInitId
         AND
         warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
         warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
         AND
         warranty_terms_non_rep_parts.warranty_defn_db_id = warranty_defn.warranty_defn_db_id AND
         warranty_terms_non_rep_parts.warranty_defn_id    = warranty_defn.warranty_defn_id
         AND
         warranty_terms_non_rep_parts.part_no_db_id = an_PartNoDbId AND
         warranty_terms_non_rep_parts.part_no_id    = an_PartNoId;

      --If the part no status is 'exclude'
      IF
         ln_PartNoStatusDbId = 0 AND
         lv_PartNoStatusCd   = 'EXCLUDE'
      THEN
         IF ln_PartNoCount >= 1 THEN
            --return  part no exist
            ln_PartNoCount:= 0;
         ELSE
            ln_PartNoCount:= 1;
         END IF;
      --If the part no status is 'include'
      ELSIF
         ln_PartNoStatusDbId  = 0 AND
         lv_PartNoStatusCd    = 'INCLUDE'
      THEN
         IF ln_PartNoCount >= 1 THEN
            --return 0 if part no count exist
            ln_PartNoCount:= 1;
         ELSE
            ln_PartNoCount:= 0;
         END IF;
      END IF;
   END IF;

   -- Return the value
   RETURN ln_PartNoCount;
END IsNonRepPartUnderWarranty;


/********************************************************************************
*  Procedure:    isRepPart
*  Arguments:    an_TaskDbId  - Task identifier DbId.
*                an_TaskId    - Task identifier Id.
*  Return:       Bool (1 if the task has a part requirement and 0 other wise)
*  Description:  Checks if a part is a repairable part.
*                Takes in the part key and returns a number.
*
*********************************************************************************
*
*  Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isRepPart
(
   an_PartNoDbId IN sched_rmvd_part.part_no_db_id%TYPE,
   an_PartNoId   IN sched_rmvd_part.part_no_id%TYPE
)
RETURN NUMBER
IS
   ln_PartCount NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_PartCount
   FROM
      eqp_part_no
   WHERE
      --Makes sure the part is repairable
      eqp_part_no.part_no_db_id = an_PartNoDbId   AND
      eqp_part_no.part_no_id    = an_PartNoId
      AND
      eqp_part_no.repair_bool = 1;

   IF ln_PartCount >= 1 THEN
      RETURN 1;
   END IF;

   RETURN 0;
END isRepPart;


/********************************************************************************
*
*  Procedure:    EvaluateRemovedComponents
*  Arguments:    an_WtyInitDbId (long) - WarrantyKey(DbId)
*                an_WtyInitId  (long)  - WarrantyKey(Id)
*                an_SchedDbId (long)   - Task Key(DbId)
*                an_SchedId (long)     - Task Key(Id)
*  Description:  This procedure evaluates the removed component for a qualified
*                task.
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateRemovedComponents
(
   an_WtyInitDbId   IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId     IN warranty_init.warranty_init_id%TYPE,
   an_SchedDbId     IN sched_stask.sched_db_id%TYPE,
   an_SchedId       IN sched_stask.sched_id%TYPE,
   an_TaskSaveDbId  IN sched_stask.sched_db_id%TYPE,
   an_TaskSaveId    IN sched_stask.sched_id%TYPE,
   an_WPSchedDbId   IN sched_stask.sched_db_id%TYPE,
   an_WPSchedId     IN sched_stask.sched_id%TYPE,
   an_LocalDbId     IN mim_local_db.db_id%TYPE
)
IS
   ln_InvDbId inv_inv.inv_no_db_id%TYPE;
   ln_InvId   inv_inv.inv_no_id%TYPE;
   ln_OemInvDbId inv_inv.inv_no_db_id%TYPE;
   ln_OemInvId   inv_inv.inv_no_id%TYPE;
   ln_SchedDbId  sched_part.sched_db_id%TYPE;
   ln_SchedId    sched_part.sched_id%TYPE;
   ln_SchedPartId sched_part.sched_part_id%TYPE;
   ln_PartNoDbId eqp_part_no.part_no_db_id%TYPE;
   ln_PartNoId   eqp_part_no.part_no_id%TYPE;
   ln_OemCount    NUMBER;
   ln_OemAssCount NUMBER;
   ln_TransCount  NUMBER;
   lCur_RemovedComponent SYS_REFCURSOR;
   ln_RtrnReqBool NUMBER(1) :=1;
BEGIN
   OPEN lCur_RemovedComponent FOR
      --Load the removed part inventory into a cursor
      SELECT
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         sched_rmvd_part.sched_db_id,
         sched_rmvd_part.sched_id,
         sched_rmvd_part.sched_part_id,
         eqp_part_no.part_no_db_id,
         eqp_part_no.part_no_id
      FROM
         evt_event,
         sched_rmvd_part,
         inv_inv,
         eqp_part_no
      WHERE
         evt_event.event_db_id = an_SchedDbId AND
         evt_event.event_id    = an_SchedId
         AND
         -- Get all removed parts
         sched_rmvd_part.sched_db_id = evt_event.event_db_id AND
         sched_rmvd_part.sched_id    = evt_event.event_id
         AND
         -- Get the removed part inventory
         inv_inv.inv_no_db_id = sched_rmvd_part.inv_no_db_id AND
         inv_inv.inv_no_id    = sched_rmvd_part.inv_no_id
         AND
         -- Get the removed part no
         eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
         eqp_part_no.part_no_id    = inv_inv.part_no_id;
   LOOP
      --For each removed inventory
      FETCH lCur_RemovedComponent
      INTO  ln_InvDbId,
            ln_InvId,
            ln_SchedDbId,
            ln_SchedId,
            ln_SchedPartId,
            ln_PartNoDbId,
            ln_PartNoId;
      EXIT WHEN lCur_RemovedComponent%NOTFOUND;

      IF isRepPart(ln_PartNoDbId, ln_PartNoId) = 1 THEN
         --For Repairable parts
         --Check the inventory records oem assembly link if it exist
         SELECT
            COUNT(*)
         INTO
            ln_OemAssCount
         FROM
            inv_inv_oem_assmbl
         WHERE
            inv_inv_oem_assmbl.inv_no_db_id = ln_InvDbId AND
            inv_inv_oem_assmbl.inv_no_id    = ln_InvId;

         --Get the inventory records oem assembly link
         IF ln_OemAssCount >= 1 THEN
            SELECT
               inv_inv_oem_assmbl.oem_assmbl_inv_no_db_id,
               inv_inv_oem_assmbl.oem_assmbl_inv_no_id
            INTO
               ln_OemInvDbId,
               ln_OemInvId
            FROM
               inv_inv_oem_assmbl
            WHERE
               inv_inv_oem_assmbl.inv_no_db_id = ln_InvDbId AND
               inv_inv_oem_assmbl.inv_no_id    = ln_InvId;

            --Make sure it matches the initialized warranty inventory
            SELECT
               COUNT(*)
            INTO
               ln_OemCount
            FROM
               warranty_init_inv
            WHERE
               warranty_init_inv.inv_no_db_id = ln_OemInvDbId AND
               warranty_init_inv.inv_no_id    = ln_OemInvId
               AND
               warranty_init_inv.master_bool = 1;

            --If there is a match above then Insert into the respective eval tables
            IF ln_OemCount >= 1 THEN
               --Check if warranty contract transfers terms to removed inventory
               SELECT
                  COUNT(*)
               INTO
                  ln_TransCount
               FROM
                  warranty_init,
                  warranty_defn_assembly
               WHERE
                  warranty_init.warranty_init_db_id = an_WtyInitDbId AND
                  warranty_init.warranty_init_id    = an_WtyInitId
                  AND
                  warranty_defn_assembly.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
                  warranty_defn_assembly.warranty_defn_id    = warranty_init.warranty_defn_id
                  AND
                  warranty_defn_assembly.transfer_bool = 1;

               IF ln_TransCount >= 1 THEN
                  --Updates the warranty init inv table and sets the master bool to 0
                  UpdateWarrantyInitInv(
                        an_WtyInitDbId, an_WtyInitId,
                        ln_InvDbId, ln_InvId
                     );
               END IF;

               --Update the warranty eval part
               UpdateWarrantyEvalPartByInv(
                     an_LocalDbId,
                     0, 'PENDING',
                     an_WtyInitDbId, an_WtyInitId,
                     an_WPSchedDbId, an_WPSchedId,
                     an_TaskSaveDbId, an_TaskSaveId,
                     ln_InvDbId, ln_InvId
                  );
            END IF;
         END IF;
      ELSE
         IF
            -- Evaluate non-routine parts
            -- if task qualifies
            isRoutineTaskIncluded(
                  an_WtyInitDbId, an_WtyInitId,
                  an_SchedDbId, an_SchedId
               ) = 1
            AND
            -- if part is non-repairable
            isRepPart(ln_PartNoDbId, ln_PartNoId) = 0
            AND
            -- and it's under warranty and unit price threshold
            IsNonRepPartUnderWarranty(
                  an_WtyInitDbId, an_WtyInitId,
                  ln_PartNoDbId, ln_PartNoId
               ) = 1
         THEN
            --If the Aup is less than the turn-in threshold
            IF
               isAupLessThanTurnInThreshold(
                     an_WtyInitDbId, an_WtyInitId,
                     ln_PartNoDbId, ln_PartNoId
                  ) = 1
            THEN
               ln_RtrnReqBool := 0;
            END IF;

            --Update the warranty eval part
            UpdateWarrantyEvalPartByReq(
                  an_LocalDbId,
                  0,'PENDING',
                  an_WtyInitDbId, an_WtyInitId,
                  an_WPSchedDbId, an_WPSchedId,
                  an_TaskSaveDbId, an_TaskSaveId,
                  ln_SchedDbId, ln_SchedId, ln_SchedPartId,
                  ln_RtrnReqBool
               );
         END IF;
      END IF;
   END LOOP;
   CLOSE lCur_RemovedComponent;
END EvaluateRemovedComponents;


/********************************************************************************
*
*  Procedure:    EvaluateTaskParts
*  Arguments:    an_WtyInitDbId (long) - WarrantyKey(DbId)
*                an_WtyInitId  (long)  - WarrantyKey(Id)
*                an_SchedDbId (long)   - Task Key(DbId)
*                an_SchedId (long)     - Task Key(Id)
*  Description:  This procedure evaluates the task part for a qualified
*                task.
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateTaskParts
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
   an_SchedId     IN sched_stask.sched_db_id%TYPE,
   an_TaskSaveDbId   IN sched_stask.sched_db_id%TYPE,
   an_TaskSaveId     IN sched_stask.sched_id%TYPE,
   an_WPSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_WPSchedId   IN sched_stask.sched_id%TYPE,
   an_LocalDbId   IN mim_local_db.db_id%TYPE
)
IS
   ln_InvDbId inv_inv.inv_no_db_id%TYPE;
   ln_InvId   inv_inv.inv_no_id%TYPE;
   ln_SchedDbId   sched_part.sched_db_id%TYPE;
   ln_SchedId     sched_part.sched_id%TYPE;
   ln_SchedPartId sched_part.sched_part_id%TYPE;
   ln_PartNoDbId   eqp_part_no.part_no_db_id%TYPE;
   ln_PartNoId     eqp_part_no.part_no_id%TYPE;
   ln_TransCount  NUMBER;
   lCur_TaskPartReq SYS_REFCURSOR;
   ln_RtrnReqBool NUMBER(1) :=1;

BEGIN

   OPEN lCur_TaskPartReq FOR
      SELECT
         sched_rmvd_part.inv_no_db_id,
         sched_rmvd_part.inv_no_id,
         sched_rmvd_part.sched_db_id,
         sched_rmvd_part.sched_id,
         sched_rmvd_part.sched_part_id,
         sched_rmvd_part.part_no_db_id,
         sched_rmvd_part.part_no_id
      FROM
         sched_rmvd_part
      WHERE
         sched_rmvd_part.sched_db_id = an_SchedDbId AND
         sched_rmvd_part.sched_id    = an_SchedId
      UNION ALL
      --The task is an in-complete task
      SELECT
         NULL,
         NULL,
         sched_part.sched_db_id,
         sched_part.sched_id,
         sched_part.sched_part_id,
         sched_part.spec_part_no_db_id,
         sched_part.spec_part_no_id
      FROM
         sched_part
      WHERE
         sched_part.sched_db_id = an_SchedDbId AND
         sched_part.sched_id    = an_SchedId
         AND NOT
         (  --Get spec_part_no_db_id where  it's not null
            sched_part.spec_part_no_db_id IS NULL AND
            sched_part.spec_part_no_id    IS NULL
         )
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
         )

      UNION ALL
      --Get standard part of part group from the eqp_part_baseline if
      --spec_part_no_db_id is null.
      SELECT
         NULL,
         NULL,
         sched_part.sched_db_id,
         sched_part.sched_id,
         sched_part.sched_part_id,
         eqp_part_baseline.part_no_db_id,
         eqp_part_baseline.part_no_id
      FROM
         sched_part,
         eqp_part_baseline
      WHERE
         sched_part.sched_db_id = an_SchedDbId AND
         sched_part.sched_id    = an_SchedId
         AND
         --spec part no id is null
         sched_part.spec_part_no_db_id IS NULL AND
         sched_part.spec_part_no_id    IS NULL
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
         )
         AND
         eqp_part_baseline.bom_part_db_id = sched_part.sched_bom_part_db_id AND
         eqp_part_baseline.bom_part_id    = sched_part.sched_bom_part_id
         AND
         eqp_part_baseline.standard_bool = 1;

   LOOP
      FETCH lCur_TaskPartReq
      INTO
         ln_InvDbId,
         ln_InvId,
         ln_SchedDbId,
         ln_SchedId,
         ln_SchedPartId,
         ln_PartNoDbId,
         ln_PartNoId;
      EXIT WHEN lCur_TaskPartReq%NOTFOUND;

      IF
         --If the part no is repairable and under warranty
         isRepPart(ln_PartNoDbId, ln_PartNoId) = 1
         AND
         IsRepPartUnderWarranty(
               an_WtyInitDbId, an_WtyInitId,
               ln_PartNoDbId, ln_PartNoId
            ) = 1
      THEN
         --Update the warranty eval part
         UpdateWarrantyEvalPartByReq(
               an_LocalDbId,
               0, 'PENDING',
               an_WtyInitDbId, an_WtyInitId,
               an_WPSchedDbId, an_WPSchedId,
               an_TaskSaveDbId, an_TaskSaveId,
               ln_SchedDbId, ln_SchedId, ln_SchedPartId,
               ln_RtrnReqBool
            );

         --Check if warranty contract transfers terms to removed inventory
         --Only do this is OEM only is false and these is an actual inventory removal record
         --EvaluateRemovedComponents(...) procedure will handle OEM stuff once removed removal
         --Check if warranty contract transfers terms to removed inventory
         IF
            ln_InvDbId IS NOT NULL
            AND
            isOEMOnlyPart(an_WtyInitDbId, an_WtyInitId) = 0
         THEN
            SELECT
               COUNT(*)
            INTO
               ln_TransCount
            FROM
               warranty_init,
               warranty_defn_assembly
            WHERE
               warranty_init.warranty_init_db_id = an_WtyInitDbId AND
               warranty_init.warranty_init_id    = an_WtyInitId
               AND
               warranty_defn_assembly.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
               warranty_defn_assembly.warranty_defn_id    = warranty_init.warranty_defn_id
               AND
               warranty_defn_assembly.transfer_bool = 1;

            -- now check to make sure the removed inventory is OEM and if transfer is enabled.
            -- if so, then transfer warranty.
            IF
               ln_TransCount >= 1
               AND
               isOEMInventory(ln_InvDbId, ln_InvId) = 1
            THEN
               --Updates the warranty init inv table and sets the master bool to 0
               UpdateWarrantyInitInv(
                     an_WtyInitDbId, an_WtyInitId,
                     ln_InvDbId, ln_InvId
                  );
            END IF;
         END IF;
      ELSE
         --Evaluate Non-Repairable Parts
         IF
            isRepPart(ln_PartNoDbId, ln_PartNoId) = 0
            AND
            IsNonRepPartUnderWarranty(
                  an_WtyInitDbId, an_WtyInitId,
                  ln_PartNoDbId, ln_PartNoId
               ) = 1
         THEN
            IF
               --If the Aup is less than the turn-in threshold
               isAupLessThanTurnInThreshold(
                     an_WtyInitDbId, an_WtyInitId,
                     ln_PartNoDbId, ln_PartNoId
                  ) = 1
            THEN
               ln_RtrnReqBool := 0;
            END IF;

            --Update the warranty eval part
            UpdateWarrantyEvalPartByReq(
                  an_LocalDbId,
                  0, 'PENDING',
                  an_WtyInitDbId, an_WtyInitId,
                  an_WPSchedDbId, an_WPSchedId,
                  an_TaskSaveDbId, an_TaskSaveId,
                  ln_SchedDbId, ln_SchedId, ln_SchedPartId,
                  ln_RtrnReqBool
               );
         END IF;
      END IF;
   END LOOP;
   CLOSE lCur_TaskPartReq;
END EvaluateTaskParts;



/********************************************************************************
*
*  Procedure:    EvaluateTaskLabor
*  Arguments:    an_WtyInitDbId (long) - WarrantyKey(DbId)
*                an_WtyInitId  (long)  - WarrantyKey(Id)
*                an_SchedDbId (long)   - Task Key(DbId)
*                an_SchedId (long)     - Task Key(Id)
*  Description:  This procedure evaluates the labors associated with the task key
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateTaskLabor
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE,
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_db_id%TYPE,
   an_TaskSaveDbId   IN sched_stask.sched_db_id%TYPE,
   an_TaskSaveId     IN sched_stask.sched_id%TYPE,
   an_WPSchedDbId IN sched_stask.sched_db_id%TYPE,
   an_WPSchedId   IN sched_stask.sched_id%TYPE,
   an_LocalDbId   IN mim_local_db.db_id%TYPE
)
IS
   ln_LabourCount   NUMBER;
BEGIN
   SELECT
      COUNT(*)
   INTO
      ln_LabourCount
   FROM
      warranty_init,
      warranty_defn
   WHERE
      warranty_init.warranty_init_db_id = an_WtyInitDbId AND
      warranty_init.warranty_init_id    = an_WtyInitId
      AND
      warranty_defn.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND
      warranty_defn.warranty_defn_id    = warranty_init.warranty_defn_id
      AND
      warranty_defn.labour_bool = 1;

   IF
      --The task is a routine task
      isRoutineTaskIncluded(
            an_WtyInitDbId, an_WtyInitId,
            an_SchedDbId, an_SchedId
         ) = 1
      AND
      ln_LabourCount >= 1
   THEN
      --Update the warranty eval labour
      UpdateWarrantyEvalLabour(
            an_LocalDbId,
            0, 'PENDING',
            an_WtyInitDbId, an_WtyInitId,
            an_WPSchedDbId,an_WPSchedId,
            an_TaskSaveDbId, an_TaskSaveId,
            an_SchedDbId, an_SchedId
         );
   END IF;
END EvaluateTaskLabor;


/********************************************************************************
*
*  Procedure:    EvaluateWarranty
*  Arguments:    an_SchedDbId (long) - Task Key(DbId)
*                an_SchedId (long)   - Task Key(Id)
*  Description:  This procedure takes in a task or workpackage key and evaluates it
*                for warranties.
*
*********************************************************************************
*
*  Copyright 2011 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateWarranty
(
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_db_id%TYPE,
   on_Return OUT NUMBER
)
IS
   lv_TaskType sched_stask.task_class_cd%TYPE;
   ln_TaskDbId sched_stask.task_db_id%TYPE;
   ln_TaskId   sched_stask.task_id%TYPE;
   ln_SaveTaskDbId sched_stask.task_db_id%TYPE;
   ln_SaveTaskId   sched_stask.task_id%TYPE;
   ln_WtyDbId warranty_init.warranty_init_db_id%TYPE;
   ln_WtyId   warranty_init.warranty_init_id%TYPE;
   lCur_AssignedWPTask SYS_REFCURSOR;
   lCur_TaskWarranties SYS_REFCURSOR;
   lCur_Subtask SYS_REFCURSOR;
   ln_LocalDbId mim_local_db.db_id%TYPE;
BEGIN

   -- we need to fetch the local db.
   SELECT
      mim_local_db.db_id
   INTO
      ln_LocalDbId
   FROM
      mim_local_db;

   --Start by setting the processing bool WP(Task) key that
   --is being set.
   UPDATE
      warranty_eval_queue
   SET
      execute_bool = 1
   WHERE
      warranty_eval_queue.sched_db_id = an_SchedDbId AND
      warranty_eval_queue.sched_id    = an_SchedId;

   --Check if the key is a WP key or Task Key
   SELECT
      sched_stask.task_class_cd
   INTO
      lv_TaskType
   FROM
      sched_stask
   WHERE
      sched_stask.sched_db_id = an_SchedDbId AND
      sched_stask.sched_id    = an_SchedId;
   IF
      --If the Task key is a WP
      lv_TaskType IN ('RO','CHECK')
   THEN
      --Delete all WP existing evaluation results
      DeleteWPEvalResults(an_SchedDbId, an_SchedId);

      --Get Assembly and Component warranties associated
      --with the main inventory of the work package
      --Store information in the scratch Pad table(task_warranty_sp)
      GetAssOrCompWarrantiesFromWP(an_SchedDbId, an_SchedId);

      --Load the cursor with the sub-task of the WP
      OPEN lCur_AssignedWPTask FOR
         SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            evt_event.h_event_db_id = an_SchedDbId AND
            evt_event.h_event_id    = an_SchedId
            AND NOT
            (
               --Excluding the WP key
               evt_event.event_db_id = an_SchedDbId AND
               evt_event.event_id    = an_SchedId
            );

      --For each assigned tasks
      LOOP
         FETCH lCur_AssignedWPTask
         INTO  ln_TaskDbId,
               ln_TaskId;
         EXIT WHEN lCur_AssignedWPTask%NOTFOUND;

         --Get warranties associated to the task and
         --Store in the scratch Pad table(task_warranty_sp)
         GetTaskWarranties(ln_TaskDbId, ln_TaskId);
      END LOOP;
      CLOSE lCur_AssignedWPTask;
   ELSE
      --Delete all existing evaluation results
      Deletetaskevalresults(an_SchedDbId, an_SchedId);

      --Get Assembly and Component warranties of the task
      --Store in the scratch Pad table(task_warranty_sp)
      GetAssOrCompWarrantiesFromTask(an_SchedDbId, an_SchedId);

      --Get Task warranties associated to the task and
      --Store in the scratch Pad table(task_warranty_sp)
      GetTaskWarranties(an_SchedDbId, an_SchedId);

   END IF;

   --Filter out all expired warranties from the scratch pad table
   DELETE FROM
      task_warranty_sp
   WHERE
      isWarrantyExpired(
            task_warranty_sp.warranty_init_db_id,
            task_warranty_sp.warranty_init_id
         ) = 1;

   --Get all the warranties from the scratch pad table
   --related to the work package.
   OPEN lCur_TaskWarranties FOR
      SELECT DISTINCT
         task_warranty_sp.warranty_init_db_id,
         task_warranty_sp.warranty_init_id
      FROM
         task_warranty_sp;
      --For each warranty in cursor
   LOOP
      FETCH lCur_TaskWarranties
      INTO  ln_WtyDbId,
            ln_WtyId;
      EXIT WHEN lCur_TaskWarranties%NOTFOUND;

      --Get the list of subtasks related to that warranty
      OPEN lCur_Subtask FOR
         SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event,
            sched_stask
         WHERE
            evt_event.h_event_db_id = an_SchedDbId AND
            evt_event.h_event_id    = an_SchedId
            AND
            (evt_event.event_status_db_id, evt_event.event_status_cd) NOT IN ((0, 'CANCEL'), (0, 'TERMINATE' ))
            AND
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
            AND
            (sched_stask.task_class_db_id, sched_stask.task_class_cd) NOT IN ((0, 'RO'),(0, 'CHECK'));

            --For each subtask in the cursor
      LOOP
         FETCH lCur_Subtask
         INTO  ln_TaskDbId,
               ln_TaskId;
         EXIT WHEN lCur_Subtask%NOTFOUND;

         --If the subtask has parts and labor and
         --it's config slot is under the warranty
         --contract list proceed to evaluation.
         IF
            hasPartsOrLabor(ln_TaskDbId, ln_TaskId) = 1
         THEN
            --Assign the task to save to the current
            ln_SaveTaskDbId := ln_TaskDbId;
            ln_SaveTaskId   := ln_TaskId;

            --Checks if the task is a JIC
            IF isTaskJIC (ln_TaskDbId, ln_TaskId) = 1 THEN
               --Saves the parent task of the JIC
               SELECT
                  evt_event.nh_event_db_id,
                  evt_event.nh_event_id
               INTO
                  ln_SaveTaskDbId,
                  ln_SaveTaskId
               FROM
                  evt_event
               WHERE
                  evt_event.event_db_id = ln_TaskDbId AND
                  evt_event.event_id    = ln_TaskId;
            END IF;

            IF
               IsTaskConfigSlotUnderWarranty(
                     ln_WtyDbId, ln_WtyId,
                     ln_TaskDbId, ln_TaskId
                  ) = 1
               OR
               isTaskOrCompWarranty(
                     ln_WtyDbId, ln_WtyId,
                     ln_TaskDbId, ln_TaskId
                  ) = 1
            THEN
               IF
                  --If the warranty is OEMonly part and the task is complete
                  --and the task has a removed component get and evaluate the
                  --removed parts
                  isOEMOnlyPart(ln_WtyDbId, ln_WtyId) = 1
                  AND
                  isCompleteTask(ln_TaskDbId, ln_TaskId) = 1
                  AND
                  hasRemovedPart(ln_TaskDbId, ln_TaskId) = 1
               THEN
                  --Evaluate the removed components of the tasks
                  --OEM components are under warranty regardless if removed on
                  --routine or non-routine tasks
                  EvaluateRemovedComponents(
                        ln_WtyDbId, ln_WtyId,
                        ln_TaskDbId, ln_TaskId,
                        ln_SaveTaskDbId, ln_SaveTaskId,
                        an_SchedDbId, an_SchedId,
                        ln_LocalDbId
                     );
               ELSIF
                  --Oem flag is false OR
                  --Oem flag is true, but task is not complete w/ removals

                  --Make sure the task is not a routine task
                  isRoutineTaskIncluded(
                        ln_WtyDbId, ln_WtyId,
                        ln_TaskDbId, ln_TaskId
                     ) = 1
               THEN
                  --Evaluate the part requirements of the task
                  EvaluateTaskParts(
                        ln_WtyDbId, ln_WtyId,
                        ln_TaskDbId, ln_TaskId,
                        ln_SaveTaskDbId, ln_SaveTaskId,
                        an_SchedDbId, an_SchedId,
                        ln_LocalDbId
                     );
               END IF;

               --Evaluate the labour requirements of the task
               EvaluateTaskLabor(
                     ln_WtyDbId, ln_WtyId,
                     ln_TaskDbId, ln_TaskId,
                     ln_SaveTaskDbId, ln_SaveTaskId,
                     an_SchedDbId, an_SchedId,
                     ln_LocalDbId
                  );
            END IF;
         END IF;
      END LOOP;
      CLOSE lCur_Subtask;
   END LOOP;

   --Clear the task_warranty scratch pad.
   DELETE FROM task_warranty_sp;
   CLOSE lCur_TaskWarranties;

   --Delete the the work package key
   --processed from the warranty eval queue.
   DELETE FROM
      warranty_eval_queue
   WHERE
      warranty_eval_queue.sched_db_id = an_SchedDbId AND
      warranty_eval_queue.sched_id    = an_SchedId
      AND
      warranty_eval_queue.execute_bool = 1;
   on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_OracleError;
   APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'warranty_eval_pkg@@@EvaluateWarranty@@@' || SQLERRM);
   RETURN;
END EvaluateWarranty;

/*----------------------- End of Package -----------------------------------*/
END WARRANTY_EVAL_PKG;
/

--changeSet OPER-27971:2 stripComments:false 
INSERT INTO warranty_eval_queue 
   SELECT sched_db_id,warranty_eval_id_seq.nextval,sched_db_id,sched_id,0,0,NULL,NULL,NULL,NULL
   FROM (
      SELECT 
         DISTINCT wp_ss.sched_db_id,wp_ss.sched_id
      FROM
         warranty_init_task
      JOIN evt_event ON
         evt_event.event_db_id = warranty_init_task.sched_db_id AND
         evt_event.event_id    = warranty_init_task.sched_id
      JOIN evt_event wp_evt ON
         wp_evt.event_db_id = evt_event.h_event_db_id AND
         wp_evt.event_id    = evt_event.h_event_id
      JOIN sched_stask wp_ss ON
         wp_ss.sched_db_id = wp_evt.event_db_id AND
         wp_ss.sched_id    = wp_evt.event_id
      WHERE 
         evt_event.hist_bool = 1 
         AND
         wp_ss.task_class_db_id = 0 AND
         wp_ss.task_class_cd = 'CHECK'
      AND NOT EXISTS
         (SELECT
             1
          FROM
             warranty_eval_task
          WHERE
             warranty_eval_task.sched_db_id = evt_event.event_db_id AND
             warranty_eval_task.sched_id    = evt_event.event_id)
   );