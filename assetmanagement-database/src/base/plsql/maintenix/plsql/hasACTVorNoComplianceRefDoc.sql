--liquibase formatted sql


--changeSet hasACTVorNoComplianceRefDoc:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:     hasACTVOrNoComplianceRefDoc
* Arguments:    aTaskDbId, aTaskId 	- pk for the task definition revision
*               aInvNoDbId, aInvNoId    - pk for the main inventory
* Description:  This function will determine if the task complies to any non-historic reference documents
* Returns 1 if there are ACTV compliance or no reference document, 0 if there are only historic reference documents
*********************************************************************************/
CREATE OR REPLACE FUNCTION hasACTVOrNoComplianceRefDoc(
    aTaskDbId task_task.task_db_id%TYPE,
    aTaskId   task_task.task_id%TYPE,
    aInvNoDbId inv_inv.inv_no_db_id%TYPE,
    aInvNoId   inv_inv.inv_no_id%TYPE
) RETURN NUMBER
IS
   -- local variables
   lHasComplianceRefDocs NUMBER;
   lActvComplianceRefDocs NUMBER;
BEGIN
   -- If it complies with no refrence documents then it's implicitly required
   SELECT
      COUNT(1)
   INTO
      lHasComplianceRefDocs
   FROM
      task_task_dep
      INNER JOIN task_defn ON
         task_defn.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
         task_defn.task_defn_id    = task_task_dep.dep_task_defn_id
      INNER JOIN task_task ON
         task_task.task_defn_db_id = task_defn.task_defn_db_id AND
         task_task.task_defn_id    = task_defn.task_defn_id AND
         task_task.revision_ord    = task_defn.last_revision_ord
   WHERE
      task_task_dep.task_db_id = aTaskDbId AND
      task_task_dep.task_id    = aTaskId
      AND
      task_task_dep.task_dep_action_db_id = 0 AND
      task_task_dep.task_dep_action_cd    = 'COMPLIES'
      AND
      (task_task.task_def_status_db_id, task_task.task_def_status_cd) NOT IN ((0, 'OBSOLETE'))
   ;

   IF lHasComplianceRefDocs = 0 THEN
      RETURN 1;
   END IF;

   -- If it complies with an active reference document then it's required
   SELECT
      COUNT(1)
   INTO
      lActvComplianceRefDocs
   FROM
      task_task_dep
      INNER JOIN task_task req ON
         req.task_db_id = task_task_dep.task_db_id AND
         req.task_id    = task_task_dep.task_id
      INNER JOIN task_defn ref_doc_defn ON
         ref_doc_defn.task_defn_db_id = task_task_dep.dep_task_defn_db_id AND
         ref_doc_defn.task_defn_id    = task_task_dep.dep_task_defn_id
      INNER JOIN task_task ref_doc ON
         ref_doc.task_defn_db_id = ref_doc_defn.task_defn_db_id AND
         ref_doc.task_defn_id    = ref_doc_defn.task_defn_id AND
         ref_doc.revision_ord    = ref_doc_defn.last_revision_ord
   WHERE
      req.task_db_id = aTaskDbId AND
      req.task_id    = aTaskId
      AND
      -- Get the complies dependents
      task_task_dep.task_dep_action_db_id = 0 AND
      task_task_dep.task_dep_action_cd    = 'COMPLIES'
      AND
      -- ... that are not OBSOLETE
      (ref_doc.task_def_status_db_id, ref_doc.task_def_status_cd)
        NOT IN ((0, 'OBSOLETE'))
      AND
      (
        -- and isn't marked as complete or not applicable (NOTE: all initialized reference documents are completed or not applicable)
        NOT EXISTS(
           SELECT
              1
           FROM
              sched_stask
              INNER JOIN evt_event ON
                 evt_event.event_db_id = sched_stask.sched_db_id AND
                 evt_event.event_id    = sched_stask.sched_id
              INNER JOIN task_task sched_task_task ON
                 sched_task_task.task_db_id = sched_stask.task_db_id AND
                 sched_task_task.task_id    = sched_stask.task_id
              INNER JOIN inv_inv ref_doc_inv ON
                 ref_doc_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
                 ref_doc_inv.inv_no_id    = sched_stask.main_inv_no_id
              INNER JOIN inv_inv req_inv ON
                 req_inv.h_inv_no_db_id = ref_doc_inv.h_inv_no_db_id AND
                 req_inv.h_inv_no_id    = ref_doc_inv.h_inv_no_id
           WHERE
              sched_task_task.task_defn_db_id = ref_doc_defn.task_defn_db_id AND
              sched_task_task.task_defn_id    = ref_doc_defn.task_defn_id
              AND
              req_inv.inv_no_db_id = aInvNoDbId AND
              req_inv.inv_no_id    = aInvNoId
              AND
              (
                 -- The req_inv is the same of ref_doc_inv
                 (
                    req_inv.inv_no_db_id = ref_doc_inv.inv_no_db_id AND
                    req_inv.inv_no_id    = ref_doc_inv.inv_no_id
                 )
                 OR
                 -- The config slot of req_inv must not be the same of ref_doc_inv.
                 -- e.g. For a config slot (i.e. Engine) with multiple positions, if the ref doc is initialized on one position,
                 --      the req can still be initialized on the other positions.
                 NOT
                 (
                    req_inv.assmbl_db_id  = ref_doc_inv.assmbl_db_id AND
                    req_inv.assmbl_cd     = ref_doc_inv.assmbl_cd AND
                    req_inv.assmbl_bom_id = ref_doc_inv.assmbl_bom_id
                 )
              )
              AND
              -- The actual task is not in ERROR status.
              NOT (
                 evt_event.event_status_db_id = 0 AND
                 evt_event.event_status_cd    = 'ERROR'
              )
        )
      )
   ;

   IF lActvComplianceRefDocs <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;
END hasACTVOrNoComplianceRefDoc;
/