--liquibase formatted sql


--changeSet getTaskPartDetailsList:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskPartDetailsList
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a List of Part Details for the
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.09
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskPartDetailsList
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
         -- Part Details
         DECODE( eqp_part_no.part_no_db_id, NULL,
            NULL,
            eqp_part_no.part_no_db_id ||':'|| eqp_part_no.part_no_id
         ) AS part_no_pk,
         eqp_part_no.part_no_oem AS part_name,

         -- Part Group Details
         DECODE( eqp_bom_part.bom_part_db_id, NULL,
            NULL,
            eqp_bom_part.bom_part_db_id ||':'|| eqp_bom_part.bom_part_id
         ) AS part_group_pk,
         DECODE( eqp_bom_part.bom_part_db_id, NULL,
            NULL,
            eqp_bom_part.bom_part_cd || ' (' || eqp_bom_part.bom_part_name || ')'
         ) AS part_group_name,

         -- Quantity (Assumes at least 1)
         NVL( SUM (sched_inst_part.inst_qt), 1 ) AS inst_qt
      FROM
         (  SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            WHERE rstat_cd = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id    = PRIOR evt_event.event_id
         ) task_tree,
         sched_inst_part,
         sched_part,
         eqp_part_no,
         eqp_bom_part
      WHERE
         sched_inst_part.sched_db_id = task_tree.event_db_id AND
         sched_inst_part.sched_id    = task_tree.event_id
         AND
         sched_part.sched_db_id   = sched_inst_part.sched_db_id AND
         sched_part.sched_id      = sched_inst_part.sched_id AND
         sched_part.sched_part_id = sched_inst_part.sched_part_id
         AND
         sched_part.rstat_cd	= 0
         AND
         -- Tie to a Part or Part Group if one exists
         eqp_part_no.part_no_db_id (+)= sched_inst_part.part_no_db_id AND
         eqp_part_no.part_no_id    (+)= sched_inst_part.part_no_id
         AND
         eqp_bom_part.bom_part_db_id (+)= sched_part.sched_bom_part_db_id AND
         eqp_bom_part.bom_part_id    (+)= sched_part.sched_bom_part_id
         AND
         -- Make sure that at least one is defined
         (  eqp_part_no.part_no_db_id IS NOT NULL
            OR
            eqp_bom_part.bom_part_db_id IS NOT NULL
         )
      GROUP BY
         eqp_part_no.part_no_db_id,
         eqp_part_no.part_no_id,
         eqp_part_no.part_no_oem,
         eqp_bom_part.bom_part_db_id,
         eqp_bom_part.bom_part_id,
         eqp_bom_part.bom_part_cd,
         eqp_bom_part.bom_part_name;
   lRecSingleRow lCur_FullList%ROWTYPE;

   /* Return Variable */
   lList CLOB;
BEGIN

   ls_Delimiter1 := '*|*';
   ls_Delimiter2 := '*||*';
   lList := '';

   -- loop through each row and build the list
   FOR lRecSingleRow IN lCur_FullList LOOP

      -- Append to the List all elements of Part Details
      lList := lList
                || lRecSingleRow.part_no_pk           || ls_Delimiter1
                || lRecSingleRow.part_name            || ls_Delimiter1
                || lRecSingleRow.part_group_pk        || ls_Delimiter1
                || lRecSingleRow.part_group_name      || ls_Delimiter1
                || lRecSingleRow.inst_qt              || ls_Delimiter2;

   END LOOP;

   -- Return the list
   RETURN lList;

END getTaskPartDetailsList;
/