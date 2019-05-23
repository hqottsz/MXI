--liquibase formatted sql


--changeSet getToolsCapacitySummary:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getToolsCapacitySummary
* Arguments:     aLocDbId  - primary key of the location (supply location)
*                aLocId    - primary key of the location (supply location)
*                aDate     - specific date
*
* Description:   For all tool requirements assigned to open tasks belonging to checks 
*                that are scheduled during line capacity shifts on a particular 
*                date, determine if there are no inventory in the same part group
*                that are serviceable and located at the same airport.
*
* Returns:        1 if all tool requirements can be met.
*                -1 if at least one tool requirement cannot be met.
*
* Orig.Coder:    Lisa Henderson/Yui Sotozaki
* Recent Coder:  Andrei Smolko
* Recent Date:   May 24, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getToolsCapacitySummary
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId   inv_loc.loc_id%TYPE,
   aDate    evt_event.event_dt%TYPE
) RETURN NUMBER
IS
   lToolReqsMet NUMBER;
BEGIN

	SELECT
		DECODE( COUNT(*), 0, 1, -1 )
	INTO
		lToolReqsMet
	FROM
		(
			SELECT
				evt_event.event_db_id,
				evt_event.event_id,
				evt_inv.inv_no_db_id,
				evt_inv.inv_no_id
			FROM
				shift_shift,
				inv_loc,
				evt_loc,
				evt_event,
				sched_stask,
				evt_inv
			WHERE
				inv_loc.supply_loc_db_id = aLocDbId AND
				inv_loc.supply_loc_id    = aLocId
				AND
         			inv_loc.rstat_cd	= 0
				AND
				inv_loc.overnight_shift_db_id = shift_shift.shift_db_id AND
				inv_loc.overnight_shift_id    = shift_shift.shift_id 
				AND
				evt_loc.loc_db_id = inv_loc.loc_db_id AND
				evt_loc.loc_id    = inv_loc.loc_id
				AND
				evt_event.event_db_id = evt_loc.event_db_id AND
				evt_event.event_id    = evt_loc.event_id
				AND
				evt_event.hist_bool = 0
				AND
				IsInShiftWithDate(evt_event.sched_start_gdt, evt_event.sched_end_gdt, aDate, inv_loc.timezone_cd, shift_shift.start_hour, shift_shift.duration_qt) = 1
				AND
				sched_stask.sched_db_id = evt_event.event_db_id AND
				sched_stask.sched_id    = evt_event.event_id
				AND
				sched_stask.task_class_db_id = 0 AND
				sched_stask.task_class_cd    = 'CHECK' AND
				sched_stask.heavy_bool       = 0
				AND
				evt_inv.event_db_id = evt_event.event_db_id AND
				evt_inv.event_id    = evt_event.event_id
				AND
				evt_inv.main_inv_bool = 1
		) overnight_checks,
		evt_event tool_event,
		evt_tool
	WHERE
		tool_event.h_event_db_id = overnight_checks.event_db_id AND
		tool_event.h_event_id    = overnight_checks.event_id
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
				inv_loc.loc_db_id = inv_inv.loc_db_id AND
				inv_loc.loc_id    = inv_inv.loc_id
				AND
				inv_loc.supply_loc_db_id = aLocDbId AND
				inv_loc.supply_loc_id    = aLocId
				AND
         			inv_loc.rstat_cd	= 0
		);

   /* Return the result */
   RETURN lToolReqsMet;
END;
/