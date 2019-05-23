--liquibase formatted sql


--changeSet getTaskLabourDuration:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskLabourDuration
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a sum of all labour for the task tree,
*                  based on workscope tasks
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2010.05.27
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskLabourDuration
(
   aSchedDbId number,
   aSchedId number
) RETURN number
IS

   /* Return Variable */
   ln_Sum number;
BEGIN


   SELECT
     NVL(SUM(tech_role.sched_hr),0)
     + NVL(SUM(cert_role.sched_hr),0)
     + NVL(SUM(insp_role.sched_hr),0)
   INTO
      ln_Sum
   FROM
     (  SELECT evt_tree.event_db_id, evt_tree.event_id
        FROM evt_event evt_tree
        START WITH
           evt_tree.event_db_id = aSchedDbId AND
           evt_tree.event_id = aSchedId
        CONNECT BY
           PRIOR evt_tree.event_db_id = evt_tree.nh_event_db_id AND
           PRIOR evt_tree.event_id = evt_tree.nh_event_id
     ) evt_tree
     INNER JOIN sched_stask ON
        sched_stask.sched_db_id = evt_tree.event_db_id   AND
        sched_stask.sched_id    = evt_tree.event_id
     INNER JOIN ref_task_class ON
        ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
        ref_task_class.task_class_cd = sched_stask.task_class_cd
     LEFT OUTER JOIN task_task ON
        task_task.task_db_id = sched_stask.task_db_id AND
        task_task.task_id = sched_stask.task_id
     INNER JOIN sched_labour ON
        sched_labour.sched_db_id = evt_tree.event_db_id   AND
        sched_labour.sched_id    = evt_tree.event_id
     INNER JOIN sched_labour_role tech_role ON
        tech_role.labour_db_id = sched_labour.labour_db_id AND
        tech_role.labour_id    = sched_labour.labour_id
        AND
        tech_role.labour_role_type_cd = 'TECH'
     LEFT OUTER JOIN sched_labour_role cert_role ON
        cert_role.labour_db_id = sched_labour.labour_db_id AND
        cert_role.labour_id    = sched_labour.labour_id
        AND
        cert_role.labour_role_type_cd = 'CERT'
     LEFT OUTER JOIN sched_labour_role insp_role ON
        insp_role.labour_db_id = sched_labour.labour_db_id AND
        insp_role.labour_id    = sched_labour.labour_id
        AND
        insp_role.labour_role_type_cd = 'INSP'
   WHERE
      (  task_task.workscope_bool = 1
         OR
         ref_task_class.workscope_bool = 1
      )
      AND
      sched_stask.dup_jic_sched_db_id IS NULL AND
      sched_stask.dup_jic_sched_id IS NULL;

   -- Return the value
   RETURN ln_Sum;

END getTaskLabourDuration;
/