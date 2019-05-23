--liquibase formatted sql


--changeSet OPER-13121:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   
   -- main cursor for non-historic part requests of tasks assigned to non-historic work package
   CURSOR lcur_part_request IS
      WITH
      -- non-historic part requests with null issue to account
      part_requests
      AS
         (
            SELECT
               req_part.req_part_db_id,
               req_part.req_part_id,
               req_part.sched_db_id,
               req_part.sched_id
            FROM
               req_part 
               -- part request event
               INNER JOIN evt_event ON 
                  evt_event.event_db_id = req_part.req_part_db_id AND
                  evt_event.event_id    = req_part.req_part_id
            WHERE
               req_part.issue_account_id is null AND
               req_part.rstat_cd = 0
               AND
               evt_event.hist_bool = 0 AND
               evt_event.rstat_cd   = 0 AND
               evt_event.event_status_cd <> 'PRISSUED'
         ),
      -- tasks that are assigned to non-historic WP
      assigned_tasks
      AS
         (
            SELECT
               task.sched_db_id,
               task.sched_id
            FROM
               sched_stask task
               INNER JOIN evt_event task_event ON 
                  task_event.event_db_id = task.sched_db_id AND
                  task_event.event_id = task.sched_id
               INNER JOIN evt_event check_event ON 
                  check_event.event_db_id = task_event.h_event_db_id AND
                  check_event.event_id = task_event.h_event_id   
               INNER JOIN sched_stask check_task ON 
                  check_task.sched_db_id = check_event.event_db_id AND
                  check_task.sched_id = check_event.event_id
            WHERE
               check_task.task_class_db_id = 0 AND
               check_task.task_class_cd = 'CHECK' AND
               check_event.hist_bool = 0  
         )
      SELECT 
         part_requests.req_part_db_id,
         part_requests.req_part_id,
         part_requests.sched_db_id,
         part_requests.sched_id
      FROM
         part_requests
         INNER JOIN assigned_tasks ON
            assigned_tasks.sched_db_id = part_requests.sched_db_id AND
            assigned_tasks.sched_id = part_requests.sched_id;
            
   -- define setIssueAccountForPartRequest procedure
   -- to set issue account of the part request with the one found in the task tree or aircraft
   -- in the order of issue accounts from task, parent task, ..., work package, aircraft
   PROCEDURE setIssueAccountForPartRequest
   (
       aReqPartDbId NUMBER,
       aReqPartId NUMBER,
       aSchedDbId NUMBER,
       aSchedId NUMBER
   ) 
   IS
            
      CURSOR lcur_issue_account IS
         WITH 
         -- issue accounts from task tree or aircraft in the proper order 
         issue_account_list
         AS
            (
               -- issue accounts from task tree in the order of task, parent task, ..., work package
               SELECT
                  sched_stask.sched_db_id,
                  sched_stask.sched_id,
                  sched_stask.issue_account_db_id,
                  sched_stask.issue_account_id
               FROM 
                  -- task and its ancestors
                  (
                     SELECT 
                        event_db_id, event_id
                     FROM
                        evt_event
                     WHERE
                        rstat_cd =  0
                     START WITH
                        event_db_id = aSchedDbId AND
                        event_id    = aSchedId
                     CONNECT BY
                        event_db_id   = PRIOR nh_event_db_id AND
                        event_id      = PRIOR nh_event_id
                  ) task_ancestors
                  INNER JOIN sched_stask ON
                     sched_stask.sched_db_id = task_ancestors.event_db_id AND
                     sched_stask.sched_id    = task_ancestors.event_id
               WHERE
                  -- only retrieves rows where issue account is specified
                  sched_stask.issue_account_db_id IS NOT NULL AND
                  sched_stask.rstat_cd = 0
                 
               -- UNION ALL will preserve the order of query results in the union
               -- 1 issue accounts from task tree will come before issue account from aircraft
               -- 2 issue accounts from task tree will be in the order of task, task parent,..., work package
               UNION ALL

               -- issue account from non-archived aircraft for the task
               SELECT 
                  task_event.event_db_id sched_db_id,
                  task_event.event_id sched_id,   
                  inv_ac_reg.issue_account_db_id,
                  inv_ac_reg.issue_account_id
               FROM
                  -- start from the task
                  evt_event task_event
                  -- use check event inv
                  INNER JOIN evt_inv check_evt_inv ON
                     check_evt_inv.event_db_id = task_event.h_event_db_id  AND
                     check_evt_inv.event_id = task_event.h_event_id
                  -- get the inv for the check  
                  INNER JOIN inv_inv ON
                     inv_inv.inv_no_db_id = check_evt_inv.inv_no_db_id AND
                     inv_inv.inv_no_id = check_evt_inv.inv_no_id
                  -- get the aircraft from Aircraft Registration table 
                  INNER JOIN inv_ac_reg ON
                     inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
                     inv_ac_reg.inv_no_id = inv_inv.inv_no_id
               WHERE 
                  task_event.event_db_id = aSchedDbId AND 
                  task_event.event_id = aSchedId 
                  AND 
                  check_evt_inv.main_inv_bool = 1 
                  AND
                  -- non-archived inventory ( aircraft )
                  inv_inv.inv_cond_db_id = 0 AND    
                  inv_inv.inv_cond_cd !='ARCHIVE' 
            )
         SELECT 
            issue_account_db_id,
            issue_account_id
         FROM 
            issue_account_list
         WHERE
            ROWNUM <= 1;  
            
      l_issue_account lcur_issue_account%ROWTYPE;
      BEGIN
         
         OPEN lcur_issue_account;
         FETCH lcur_issue_account INTO l_issue_account;
         CLOSE lcur_issue_account;
         
         UPDATE 
            req_part
         SET 
            issue_account_db_id  = l_issue_account.issue_account_db_id, 
            issue_account_id   = l_issue_account.issue_account_id
         WHERE
            req_part_db_id = aReqPartDbId AND
            req_part_id = aReqPartId;
            
      END setIssueAccountForPartRequest;
           
BEGIN
   
   -- for each non-historic part request in a task assigned to a non-historic work package
   FOR part_request_rec IN lcur_part_request
   LOOP
      -- set issue account for the part request with the one from the task tree or aircraft
      setIssueAccountForPartRequest(
         part_request_rec.req_part_db_id,
         part_request_rec.req_part_id,
         part_request_rec.sched_db_id,
         part_request_rec.sched_id
      );
   END LOOP;   
   
END;
/