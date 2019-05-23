--liquibase formatted sql


--changeSet getTaskSubtreeList:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskSubtreeList
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a List of all the SubTasks of a given root
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.14
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskSubtreeList
(
   aSchedDbId number,
   aSchedId number
) RETURN CLOB
IS
   /* Local Variable */
   ls_Delimiter1 VARCHAR2(5);
   ls_Delimiter2 VARCHAR2(5);

   /* Cursor Declarations */

   CURSOR lCur_FullList
   IS
      SELECT
         task_tree.event_db_id ||':'|| task_tree.event_id AS event_pk
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
         ) task_tree
      WHERE
         task_tree.event_db_id != aSchedDbId OR
         task_tree.event_id    != aSchedId;
   lRecSingleRow lCur_FullList%ROWTYPE;

   /* Return Variable */
   lList CLOB;
BEGIN

   ls_Delimiter1 := '*|*';
   ls_Delimiter2 := '*||*';
   lList := '';

   -- loop through each row and build the list
   FOR lRecSingleRow IN lCur_FullList LOOP

      -- Append to the List all elements of the Query
      lList := lList
                || lRecSingleRow.event_pk       || ls_Delimiter2;

   END LOOP;

   -- Return the list
   RETURN lList;

END getTaskSubtreeList;
/