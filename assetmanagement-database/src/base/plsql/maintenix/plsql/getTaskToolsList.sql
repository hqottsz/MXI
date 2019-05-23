--liquibase formatted sql


--changeSet getTaskToolsList:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskToolsList
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*
* Description:   This function will return a list of Tools for the
*                  Task Subtree starting at the provided root.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.14
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskToolsList
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
         ) AS part_group_name
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
         evt_tool,
         eqp_part_no,
         eqp_bom_part
      WHERE
         evt_tool.event_db_id = task_tree.event_db_id AND
         evt_tool.event_id    = task_tree.event_id
         AND
         -- Tie to a Part or Part Group if one exists
         eqp_part_no.part_no_db_id (+)= evt_tool.part_no_db_id AND
         eqp_part_no.part_no_id    (+)= evt_tool.part_no_id
         AND
         eqp_part_no.rstat_cd	= 0
         AND
         eqp_bom_part.bom_part_db_id (+)= evt_tool.bom_part_db_id AND
         eqp_bom_part.bom_part_id    (+)= evt_tool.bom_part_id
         AND
         -- Make sure that at least one is defined
         (  eqp_part_no.part_no_db_id IS NOT NULL
            OR
            eqp_bom_part.bom_part_db_id IS NOT NULL
         );
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
                || lRecSingleRow.part_no_pk           || ls_Delimiter1
                || lRecSingleRow.part_name            || ls_Delimiter1
                || lRecSingleRow.part_group_pk        || ls_Delimiter1
                || lRecSingleRow.part_group_name      || ls_Delimiter2;

   END LOOP;

   -- Return the list
   RETURN lList;

END getTaskToolsList;
/