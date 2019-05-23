--liquibase formatted sql


--changeSet MX-17547:1 stripComments:false
/*
	This view displays the req jic hierarchy
*/
CREATE OR REPLACE VIEW vw_req_tasks(
   sched_db_id,
   sched_id,
   task_class_db_id,
   task_class_cd,
   jic_task_db_id,
   jic_task_id,
   event_status_db_id,
   event_status_cd
) AS
SELECT
   parent_sched_stask.sched_db_id,
   parent_sched_stask.sched_id,
   req_task_task.task_class_db_id,
   req_task_task.task_class_cd,
   task_jic_req_map.jic_task_db_id,
   task_jic_req_map.jic_task_id,
   root_event.event_status_db_id,
   root_event.event_status_cd
FROM
   task_jic_req_map,
   task_task req_task_task,
   sched_stask parent_sched_stask,
   evt_event parent_evt_event,
   evt_inv parent_evt_inv,
   evt_event root_event,
   (SELECT
      CASE WHEN inv_inv.inv_class_cd IN ('ASSY', 'ACFT') THEN
      	inv_inv.inv_no_db_id
      	   WHEN inv_inv.nh_inv_no_db_id IS NULL THEN
              inv_inv.inv_no_db_id
      ELSE
        inv_inv.assmbl_inv_no_db_id
      END AS inv_no_db_id,
      CASE WHEN inv_inv.inv_class_cd IN ('ASSY', 'ACFT') THEN
         inv_inv.inv_no_id
           WHEN inv_inv.nh_inv_no_id IS NULL THEN
              inv_inv.inv_no_id    
      ELSE
         inv_inv.assmbl_inv_no_id
      END AS inv_no_id
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
      inv_inv.inv_no_id    = context_package.get_inv_no_id()
   ) assy_inv,
   inv_inv parent_inv_inv
WHERE
   -- Get all REQs for the REQ Task Definition
   req_task_task.task_defn_db_id = task_jic_req_map.req_task_defn_db_id AND
   req_task_task.task_defn_id    = task_jic_req_map.req_task_defn_id
   AND
   parent_sched_stask.task_db_id = req_task_task.task_db_id AND
   parent_sched_stask.task_id = req_task_task.task_id
   AND
   -- Limit to non-historics
   parent_evt_event.event_db_id = parent_sched_stask.sched_db_id AND
   parent_evt_event.event_id = parent_sched_stask.sched_id AND
   parent_evt_event.hist_bool = 0
   AND
   -- Limit to REQs on the Inventory
   parent_evt_inv.event_db_id = parent_evt_event.event_db_id AND
   parent_evt_inv.event_id = parent_evt_event.event_id
   AND
   DECODE( 
              parent_inv_inv.assmbl_inv_no_db_id, NULL, 
                parent_inv_inv.inv_no_db_id, 
                parent_inv_inv.assmbl_inv_no_db_id)  
                     = assy_inv.inv_no_db_id AND 
   DECODE( 
              parent_inv_inv.assmbl_inv_no_id, NULL, 
                parent_inv_inv.inv_no_id, 
                parent_inv_inv.assmbl_inv_no_id ) 
               = assy_inv.inv_no_id
   AND
   parent_evt_inv.inv_no_db_id = parent_inv_inv.inv_no_db_id AND
   parent_evt_inv.inv_no_id    = parent_inv_inv.inv_no_id AND
   parent_evt_inv.main_inv_bool = 1
   AND
   -- Get the Root Task details
   root_event.event_db_id = parent_evt_event.h_event_db_id AND
   root_event.event_id = parent_evt_event.h_event_id;