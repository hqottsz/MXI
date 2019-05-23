--liquibase formatted sql


--changeSet MX-24587:1 stripComments:false
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
WITH context_inv AS (
   SELECT
      inv_inv.inv_no_db_id, 
      inv_inv.inv_no_id,
      inv_inv.h_inv_no_db_id,
      inv_inv.h_inv_no_id,
      inv_inv.assmbl_inv_no_db_id,
      inv_inv.assmbl_inv_no_id,
      inv_inv.inv_class_cd
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
      inv_inv.inv_no_id    = context_package.get_inv_no_id()
)
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
   -- below we find the tree of inventory on the same assembly or in the same 
   -- tree if the inventory is loose
   (
   -- loose inventory that do not have ACFT or ASSY parents
   SELECT
      inv_tree.inv_no_db_id, inv_tree.inv_no_id
   FROM
      context_inv
      INNER JOIN inv_inv h_inv_inv ON
         h_inv_inv.inv_no_db_id = context_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id = context_inv.h_inv_no_id
      INNER JOIN inv_inv inv_tree ON
         inv_tree.h_inv_no_db_id = h_inv_inv.inv_no_db_id AND
         inv_tree.h_inv_no_id = h_inv_inv.inv_no_id
   WHERE
      h_inv_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
   UNION ALL
   -- assembly or aircraft inventory
   SELECT
      CASE WHEN context_inv.inv_class_cd IN ('ASSY', 'ACFT') THEN
            context_inv.inv_no_db_id
         ELSE
            context_inv.assmbl_inv_no_db_id
      END AS inv_no_db_id, 
      CASE WHEN context_inv.inv_class_cd IN ('ASSY', 'ACFT') THEN
            context_inv.inv_no_id
         ELSE
            context_inv.assmbl_inv_no_id
      END AS inv_no_id
   FROM
      context_inv
   UNION ALL
   -- inventory under the same assembly
   SELECT
      assy_inv.inv_no_db_id, assy_inv.inv_no_id
   FROM
      context_inv
      INNER JOIN inv_inv assy_inv ON
         assy_inv.assmbl_inv_no_db_id = context_inv.assmbl_inv_no_db_id AND
         assy_inv.assmbl_inv_no_id    = context_inv.assmbl_inv_no_id
   WHERE
      context_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
      AND
      NOT (
         assy_inv.inv_no_db_id = context_inv.assmbl_inv_no_db_id AND
         assy_inv.inv_no_id    = context_inv.assmbl_inv_no_id
      )
   UNION ALL
   -- inventory under the same assembly when inv is the assembly
   SELECT
      sub_inv.inv_no_db_id, sub_inv.inv_no_id
   FROM
      context_inv
      INNER JOIN inv_inv sub_inv ON
         sub_inv.assmbl_inv_no_db_id = context_inv.inv_no_db_id AND
         sub_inv.assmbl_inv_no_id    = context_inv.inv_no_id
   WHERE
      context_inv.inv_class_cd IN ('ASSY', 'ACFT')
      AND
      NOT (
         sub_inv.inv_no_db_id = context_inv.inv_no_db_id AND
         sub_inv.inv_no_id    = context_inv.inv_no_id
      )
   ) assy_inv_tree
   INNER JOIN evt_inv parent_evt_inv ON
      parent_evt_inv.inv_no_db_id = assy_inv_tree.inv_no_db_id AND
      parent_evt_inv.inv_no_id    = assy_inv_tree.inv_no_id 
      AND
      parent_evt_inv.main_inv_bool = 1
   INNER JOIN evt_event parent_evt_event ON
      -- Limit to REQs on the Inventory
      parent_evt_inv.event_db_id = parent_evt_event.event_db_id AND
      parent_evt_inv.event_id    = parent_evt_event.event_id
   INNER JOIN sched_stask parent_sched_stask ON
      parent_evt_event.event_db_id = parent_sched_stask.sched_db_id AND
      parent_evt_event.event_id    = parent_sched_stask.sched_id 
   INNER JOIN task_task req_task_task ON
      parent_sched_stask.task_db_id = req_task_task.task_db_id AND
      parent_sched_stask.task_id    = req_task_task.task_id
   INNER JOIN task_jic_req_map ON
      -- Get all REQs for the REQ Task Definition
      req_task_task.task_defn_db_id = task_jic_req_map.req_task_defn_db_id AND
      req_task_task.task_defn_id    = task_jic_req_map.req_task_defn_id
   INNER JOIN evt_event root_event ON
      -- Get the Root Task details
      root_event.event_db_id = parent_evt_event.h_event_db_id AND
      root_event.event_id    = parent_evt_event.h_event_id
WHERE
   -- Limit to non-historics
   parent_evt_event.hist_bool = 0
   ;

--changeSet MX-24587:2 stripComments:false
-- queue inventory that were affected for baseline sync
INSERT INTO inv_sync_queue (inv_no_db_id, inv_no_id, queue_date)
(SELECT
   inv_no_db_id,
   inv_no_id,
   SYSDATE
FROM
   (SELECT DISTINCT
      inv_inv.inv_no_db_id,
      inv_inv.inv_no_id
   FROM
      inv_inv
      INNER JOIN evt_inv parent_evt_inv ON
         parent_evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
         parent_evt_inv.inv_no_id    = inv_inv.inv_no_id 
         AND
         parent_evt_inv.main_inv_bool = 1
      INNER JOIN evt_event parent_evt_event ON
         -- Limit to REQs on the Inventory
         parent_evt_inv.event_db_id = parent_evt_event.event_db_id AND
         parent_evt_inv.event_id    = parent_evt_event.event_id
      INNER JOIN sched_stask parent_sched_stask ON
         parent_evt_event.event_db_id = parent_sched_stask.sched_db_id AND
         parent_evt_event.event_id    = parent_sched_stask.sched_id 
      INNER JOIN task_task req_task_task ON
         parent_sched_stask.task_db_id = req_task_task.task_db_id AND
         parent_sched_stask.task_id    = req_task_task.task_id
      INNER JOIN task_jic_req_map ON
         -- Get all REQs for the REQ Task Definition
         req_task_task.task_defn_db_id = task_jic_req_map.req_task_defn_db_id AND
         req_task_task.task_defn_id    = task_jic_req_map.req_task_defn_id
   WHERE
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd = 'ASSY'
      AND
      inv_inv.locked_bool = 0
      AND
      inv_inv.prevent_synch_bool = 0
      AND
      -- Limit to non-historics
      parent_evt_event.hist_bool = 0
      AND
      NOT EXISTS (
         SELECT
            1
         FROM
            evt_event
            INNER JOIN sched_stask ON
               evt_event.event_db_id = sched_stask.sched_db_id AND
               evt_event.event_id    = sched_stask.sched_id
         WHERE
            evt_event.nh_event_db_id = parent_evt_event.event_db_id AND
            evt_event.nh_event_id    = parent_evt_event.event_id
            AND
            sched_stask.task_db_id = task_jic_req_map.jic_task_db_id AND
            sched_stask.task_id    = task_jic_req_map.jic_task_id
      )
      AND
      NOT EXISTS (
         SELECT
            1
         FROM
            inv_sync_queue
         WHERE
            inv_sync_queue.inv_no_db_id = inv_inv.inv_no_db_id AND
            inv_sync_queue.inv_no_id    = inv_inv.inv_no_id
      )
   ) tmp_table
);