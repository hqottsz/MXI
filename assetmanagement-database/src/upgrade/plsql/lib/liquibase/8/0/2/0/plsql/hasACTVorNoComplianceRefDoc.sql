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
      -- Get the complies dependents
      task_task_dep.task_dep_action_db_id = 0 AND
      task_task_dep.task_dep_action_cd    = 'COMPLIES'
      AND
      -- ... that are not OBSOLETE
      (task_task.task_def_status_db_id, task_task.task_def_status_cd)
        NOT IN ((0, 'OBSOLETE'))
      AND
      -- and isn't marked as complete or not applicable (NOTE: all initialized reference documents are completed or not applicable)
      NOT EXISTS(
         SELECT
            1
         FROM
            sched_stask
            INNER JOIN task_task sched_task_task ON
               sched_task_task.task_db_id = sched_stask.task_db_id AND
               sched_task_task.task_id    = sched_stask.task_id
            INNER JOIN inv_inv ON
               inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
               inv_inv.inv_no_id    = sched_stask.main_inv_no_id
            INNER JOIN inv_inv req_inv_inv ON
               req_inv_inv.h_inv_no_db_id = inv_inv.h_inv_no_db_id AND
               req_inv_inv.h_inv_no_id    = inv_inv.h_inv_no_id
               AND
               req_inv_inv.inv_no_db_id = aInvNoDbId AND
               req_inv_inv.inv_no_id    = aInvNoId
         WHERE
            sched_task_task.task_defn_db_id = task_defn.task_defn_db_id AND
            sched_task_task.task_defn_id    = task_defn.task_defn_id
      )
   ;
   
   IF lActvComplianceRefDocs <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;
END hasACTVOrNoComplianceRefDoc;
/