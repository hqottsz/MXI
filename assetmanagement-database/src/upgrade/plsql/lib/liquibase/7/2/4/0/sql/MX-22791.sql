--liquibase formatted sql


--changeSet MX-22791:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- TRG_MX_QUEUE_REQ_UPDATE.prc
--
-- This is a trigger that will do the following:
--
-- Add a new work item as follows:
-- * ID: REQ_UPDATE_QUEUE_ID's next value
-- * KEY: The task key
-- * DATA: The data in properties format
--
-- Arguments:
-- * an_SchedDbIdan_SchedId - Primary key for the part requests' task
-- * ad_OrigReqByDt - The original req_by_dt on the part request
-- * ad_NewReqByDt - The new req_by_dt on the part request
-- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE OR REPLACE PROCEDURE TRG_MX_QUEUE_REQ_UPDATE(
      an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
      an_SchedId     IN sched_stask.sched_id%TYPE,
      ad_OrigReqByDt IN req_part.req_by_dt%TYPE,
      ad_NewReqByDt  IN req_part.req_by_dt%TYPE,
      ol_Return      OUT NUMBER
) IS
   -- data holder
   ls_Data utl_work_item.data%TYPE;
   
   -- work package
   ln_WPSchedDbId sched_stask.sched_db_id%TYPE;
   ln_WPSchedId   sched_stask.sched_id%TYPE;
   
   -- status
   ls_TaskStatusCd evt_event.event_status_cd%TYPE;
   
   -- trigger status
   lb_TriggerActiveBool utl_trigger.active_bool%TYPE;
   
   -- variable for inseting into the work item table
   ln_WorkId  utl_work_item.id%TYPE;
   ln_MimDbId mim_local_db.db_id%TYPE;
   
   /* constant declarations (return codes) */
   icn_Success   CONSTANT NUMBER := 1;
   icn_NoProc    CONSTANT NUMBER := 0;  -- No processing done
   
   CURSOR lcur_TriggerStatus
   IS
      SELECT
         1
      FROM
         dual
      WHERE
         EXISTS (
            SELECT 
               1 
            FROM 
               utl_trigger
            WHERE 
               utl_trigger.trigger_cd = 'MX_QUEUE_REQ_UPDATE' 
               AND
               utl_trigger.active_bool = 1
         );

   lreq_TriggerStatus lcur_TriggerStatus%ROWTYPE;

BEGIN
   -- Initialize the return value
   ol_Return := icn_NoProc;
   
   IF ad_OrigReqByDt IS NOT NULL AND ad_NewReqByDt IS NOT NULL THEN
     IF ad_OrigReqByDt = ad_NewReqByDt THEN
        RETURN;
     END IF;
   ELSIF ad_OrigReqByDt IS NULL AND ad_NewReqByDt IS NULL THEN
       RETURN;
   END IF;
   
   -- check if the trigger is activated
   OPEN lcur_TriggerStatus;
   FETCH lcur_TriggerStatus INTO lreq_TriggerStatus;
      
   IF lcur_TriggerStatus%NOTFOUND THEN
      CLOSE lcur_TriggerStatus;
      RETURN;
   END IF;
   
   CLOSE lcur_TriggerStatus;

-- SAMPLE DATA
--#Thu Sep 30 10:13:26 EDT 2010
--mx_orig_req_by=2002-03-20 18\:42
--mx_task_status=ACTV
--mx_wp=5000000\:81858
--# mx_hr not set since null
--# mx_hr=4650\:6000010 
--mx_new_req_by=2010-09-30 18\:42

   -- query to determine task's work package
   SELECT
      wp_stask.sched_db_id,
      wp_stask.sched_id,
      evt_event.event_status_cd
   INTO
      ln_WPSchedDbId,
      ln_WPSchedId,
      ls_TaskStatusCd
   FROM
      evt_event
      LEFT OUTER JOIN sched_stask wp_stask ON
         wp_stask.sched_db_id = evt_event.h_event_db_id AND
         wp_stask.sched_id    = evt_event.h_event_id
         AND
         wp_stask.task_class_cd IN ('CHECK', 'RO')
   WHERE
      evt_event.event_db_id = an_SchedDbId AND
      evt_event.event_id    = an_SchedId;

   -- fill in data
   ls_Data := '#' || to_char( SYSTIMESTAMP, 'Dy Mon DD HH24:MI:SS TZH:TZM YYYY') || chr(10) ||
              'mx_orig_req_by=' || to_char( ad_OrigReqByDt, 'DD-MON-YYYY HH24:MI' ) || chr(10) ||
              'mx_task_status=' || ls_TaskStatusCd || chr(10) ||
              'mx_new_req_by=' || to_char( ad_NewReqByDt, 'DD-MON-YYYY HH24:MI' ) || chr(10);
   
   -- if task is assigned to WP
   IF ln_WPSchedDbId IS NOT NULL THEN
      ls_Data := ls_Data ||
                 'mx_wp=' || ln_WPSchedDbId || ':' || ln_WPSchedId || chr(10);
   END IF;
   
   SELECT
      db_id,
      utl_work_item_id.nextval
   INTO
      ln_MimDbId,
      ln_WorkId
   FROM
      mim_local_db;
   
   -- insert into UTL_WORK_ITEM table
   INSERT INTO UTL_WORK_ITEM (ID, TYPE, KEY, DATA, UTL_ID)
      VALUES (ln_WorkId, 'REQ_PART_UPDATE_QUEUE', an_SchedDbId || ':' || an_SchedId, ls_Data, ln_MimDbId);

   /* return sucess */
   ol_Return  := icn_Success;
EXCEPTION
   WHEN OTHERS THEN
      ol_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'event_pkg@@@TRG_MX_QUEUE_REQ_UPDATE@@@' || SQLERRM);
      RETURN;
END TRG_MX_QUEUE_REQ_UPDATE;
/