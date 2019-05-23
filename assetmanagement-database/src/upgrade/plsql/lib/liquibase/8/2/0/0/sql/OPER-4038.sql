--liquibase formatted sql


--changeSet OPER-4038:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**
This script changes the status to AWAITING ISSUE, for any open part requests associated to a complete task with an installed part.
*/
DECLARE
   
   -- event record structure
   TYPE ltyp_event_rec IS RECORD (
       event_db_id  evt_event.event_db_id%TYPE,
       event_id     evt_event.event_id%TYPE
   );
   
   TYPE ltyp_event_tb IS TABLE OF ltyp_event_rec;
   lrec_events  ltyp_event_tb;
   
    lAutoIssue utl_config_parm.parm_value%TYPE;
   
BEGIN

   SELECT parm_value INTO lAutoIssue FROM utl_config_parm WHERE parm_name = 'AUTO_ISSUE_INVENTORY';
   
   IF lAutoIssue = 'FALSE' THEN
    
      -- collect all data in BULK to lrec_events type table
      SELECT 
         evt_event.event_db_id, 
         evt_event.event_id
      BULK COLLECT 
      INTO 
         lrec_events
      FROM
         evt_event
         INNER JOIN req_part ON
            evt_event.event_db_id = req_part.req_part_db_id AND
            evt_event.event_id    = req_part.req_part_id
         INNER JOIN sched_inst_part ON
            req_part.sched_db_id = sched_inst_part.sched_db_id AND
            req_part.sched_id    = sched_inst_part.sched_id AND
            req_part.sched_part_id = sched_inst_part.sched_part_id AND
            req_part.sched_inst_part_id = sched_inst_part.sched_inst_part_id
         INNER JOIN evt_event task_event ON
            task_event.event_db_id = sched_inst_part.sched_db_id AND
            task_event.event_id    = sched_inst_part.sched_id
       WHERE
           -- task is complete
           task_event.event_type_db_id = 0 AND
           task_event.event_type_cd    = 'TS'
           AND
           -- task is complete
           task_event.event_status_db_id = 0 AND
           task_event.event_status_cd    = 'COMPLETE'
           AND 
           task_event.hist_bool = 1
           AND
           -- there is an installed part
           sched_inst_part.inv_no_db_id IS NOT NULL
           AND
           evt_event.event_type_db_id = 0 AND
           evt_event.event_type_cd    = 'PR'
           AND 
           evt_event.event_status_db_id = 0 AND
           evt_event.event_status_cd    = 'PROPEN';
              
      -- bulk update event
      FORALL indx IN 1 .. lrec_events.COUNT 
       UPDATE
          evt_event
       SET
          event_status_db_id = 0,
          event_status_cd = 'PRAWAITISSUE'
       WHERE
          event_db_id = lrec_events(indx).event_db_id AND
          event_id    = lrec_events(indx).event_id;
             
      -- bulk insert stage
      FORALL indx IN 1 .. lrec_events.COUNT 
         INSERT 
         INTO 
          evt_stage
           ( 
              event_db_id, 
              event_id, 
              stage_id, 
              event_status_db_id,
              event_status_cd, 
              stage_event_db_id, 
              stage_event_id, 
              hr_db_id, 
              hr_id, 
              stage_dt, 
              stage_gdt, 
              user_stage_note, 
              system_bool
           )
         VALUES
          (
            lrec_events(indx).event_db_id, 
            lrec_events(indx).event_id, 
            EVT_STAGE_ID_SEQ.NEXTVAL, 
            0, 
            'PRAWAITISSUE', 
            lrec_events(indx).event_db_id, 
            lrec_events(indx).event_id, 
            0, 
            3, 
            sysdate, 
            sysdate,
            'Status changed to Awaiting Issue as a result of migration script to set Open part request associated with a completed task.', 
            1 
         );     
         
   END IF;      
   
END; 
/                 