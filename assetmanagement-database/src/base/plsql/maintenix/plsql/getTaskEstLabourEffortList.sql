--liquibase formatted sql


--changeSet getTaskEstLabourEffortList:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskEstLabourEffortList
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a list of Labour Types and Effort for
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.14
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskEstLabourEffortList
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
         sched_labour.labour_skill_db_id ||':'|| sched_labour.labour_skill_cd AS skill_pk,
         NVL(SUM(tech_role.sched_hr),0) AS est_effort
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
         sched_labour
         INNER JOIN sched_labour_role tech_role ON
            tech_role.labour_db_id = sched_labour.labour_db_id AND
            tech_role.labour_id    = sched_labour.labour_id
            AND
            tech_role.labour_role_type_cd = 'TECH'
      WHERE
         sched_labour.sched_db_id = task_tree.event_db_id AND
         sched_labour.sched_id    = task_tree.event_id
      GROUP BY
         sched_labour.labour_skill_db_id,
         sched_labour.labour_skill_cd;
   lRecSingleRow lCur_FullList%ROWTYPE;

   /* Return Variable */
   lList CLOB;
BEGIN

   ls_Delimiter1 := '*|*';
   ls_Delimiter2 := '*||*';
   lList := '';

   -- loop through each row and build the list
   FOR lRecSingleRow IN lCur_FullList LOOP

      -- Append to the List all elements of the Table Row
      lList := lList
                || lRecSingleRow.skill_pk           || ls_Delimiter1
                || lRecSingleRow.est_effort         || ls_Delimiter2;

   END LOOP;

   -- Return the list
   RETURN lList;

END getTaskEstLabourEffortList;
/