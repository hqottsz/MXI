--liquibase formatted sql


--changeSet getToolsCapacityForTask:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getToolsCapacityForTask
* Arguments:     aLocDbId   - primary key of the location (supply location)
*                aLocId     - primary key of the location (supply location)
*                aEventDbId - primary key of the event
*                aEventId   - primary key of the event
*
* Description:   Checks if the tools are available for the task
*
* Returns:        1 if all tool requirements can be met.
*                -1 if at least one tool requirement cannot be met.
*
* Orig.Coder:    Josh Cimino
* Recent Coder:  
* Recent Date:   October 30, 2006
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getToolsCapacityForTask
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId   inv_loc.loc_id%TYPE,
   aEventDbId evt_event.event_db_id%TYPE,
   aEventId   evt_event.event_id%TYPE
) RETURN NUMBER
IS
   lToolReqsMet NUMBER;
BEGIN

	SELECT
		DECODE( COUNT(*), 0, 1, -1 )
	INTO
		lToolReqsMet
	FROM
		evt_event tool_event,
		evt_tool
	WHERE
		tool_event.h_event_db_id = aEventDbId AND
		tool_event.h_event_id    = aEventId
		AND
		tool_event.hist_bool = 0
		AND
         	tool_event.rstat_cd	= 0
		AND
		evt_tool.event_db_id = tool_event.event_db_id AND
		evt_tool.event_id    = tool_event.event_id
		AND NOT EXISTS
		(
			SELECT
				1
			FROM
				eqp_part_baseline,
				inv_inv,
				inv_loc
			WHERE
				eqp_part_baseline.bom_part_db_id = evt_tool.bom_part_db_id AND
				eqp_part_baseline.bom_part_id    = evt_tool.bom_part_id
				AND
				inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
				inv_inv.part_no_id    = eqp_part_baseline.part_no_id
				AND
				inv_inv.inv_cond_db_id = 0 AND
				inv_inv.inv_cond_cd    = 'RFI'
				AND
         			inv_inv.rstat_cd	= 0
				AND
				inv_loc.loc_db_id = inv_inv.loc_db_id AND
				inv_loc.loc_id    = inv_inv.loc_id
				AND
				inv_loc.supply_loc_db_id = aLocDbId AND
				inv_loc.supply_loc_id    = aLocId
		);

   /* Return the result */
   RETURN lToolReqsMet;
END;
/