--liquibase formatted sql


--changeSet getTaskWorkTypesList:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskWorkTypesList
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a list of Work Types for the
*                  Task Subtree starting at the provided root.
*
* Note:			 RSTAT check merged 
* Orig.Coder:    jclarkin
* Recent Coder:  jbajer
* Recent Date:   Feb 25th, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskWorkTypesList
(
   aSchedDbId number,
   aSchedId number
) RETURN CLOB
IS
   /* Local Variable */
   ls_Delimiter2 VARCHAR2(5);

   /* Cursor Declarations */

   CURSOR lCur_FullList
   IS
      SELECT
         sched_work_type.work_type_db_id ||':'|| sched_work_type.work_type_cd AS work_type_pk
      FROM
         (  SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id    = PRIOR evt_event.event_id
         ) task_tree,
         sched_stask,
         sched_work_type
      WHERE
         sched_stask.sched_db_id = task_tree.event_db_id AND
         sched_stask.sched_id    = task_tree.event_id
		 AND
         sched_stask.rstat_cd	= 0
         AND
         sched_work_type.sched_db_id = sched_stask.sched_db_id AND
         sched_work_type.sched_id    = sched_stask.sched_id;
   lRecSingleRow lCur_FullList%ROWTYPE;

   /* Return Variable */
   lList CLOB;
BEGIN

   ls_Delimiter2 := '*||*';
   lList := '';

   -- loop through each row and build the list
   FOR lRecSingleRow IN lCur_FullList LOOP

      -- Append to the List all elements of the Table Row
      lList := lList
                || lRecSingleRow.work_type_pk || ls_Delimiter2;

   END LOOP;

   -- Return the list
   RETURN lList;

END getTaskWorkTypesList;
/