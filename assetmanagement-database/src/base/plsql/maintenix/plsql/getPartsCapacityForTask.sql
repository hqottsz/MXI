--liquibase formatted sql


--changeSet getPartsCapacityForTask:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getPartsCapacityForTask
* Arguments:     aLocDbId  - primary key of the location (supply location)
*                aLocId    - primary key of the location (supply location)
*                aDate     - specific date
*                aEventDbId - primary key of the event
*                aEventId   - primary key of the event
*
* Description:   Checks if the parts are available for the task
*
* Returns:        1 if all part requests are fulfilled.
*                -1 if at least one part request is not fulfilled.
*		  2 if there are some remote parts available
*
* Orig.Coder:    Josh Cimino
* Recent Coder:  Andrei Smolko
* Recent Date:   May 24, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getPartsCapacityForTask
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId   inv_loc.loc_id%TYPE,
   aDate    evt_event.event_dt%TYPE,
   aEventDbId evt_event.event_db_id%TYPE,
   aEventId   evt_event.event_id%TYPE
) RETURN NUMBER
IS
   lPartReqsFulfilled NUMBER;
BEGIN

	SELECT
		DECODE( COUNT(*), 0, 1, -1 )
	INTO
		lPartReqsFulfilled
	FROM
		(
			SELECT
				sched_part.sched_bom_part_db_id,
				sched_part.sched_bom_part_id,
				SUM( sched_inst_part.inst_qt ) AS inst_qt
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
						inv_loc.supply_loc_id    = aLocId   AND
         					inv_loc.rstat_cd	= 0
						AND
						inv_loc.overnight_shift_db_id =  shift_shift.shift_db_id AND
						inv_loc.overnight_shift_id = shift_shift.shift_id
						AND
						evt_loc.loc_db_id = inv_loc.loc_db_id AND
						evt_loc.loc_id    = inv_loc.loc_id
						AND
						evt_event.event_db_id = evt_loc.event_db_id AND
						evt_event.event_id    = evt_loc.event_id
						AND
						evt_event.hist_bool = 0 AND
						evt_event.rstat_cd = 0
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
				evt_event task_event,
				sched_part,
				sched_inst_part
			WHERE
				task_event.h_event_db_id = overnight_checks.event_db_id AND
				task_event.h_event_id    = overnight_checks.event_id
				AND
				task_event.hist_bool = 0	AND
         			task_event.rstat_cd  = 0
				AND
				sched_part.sched_db_id = task_event.event_db_id AND
				sched_part.sched_id    = task_event.event_id
				AND
				sched_inst_part.sched_db_id   = sched_part.sched_db_id AND
				sched_inst_part.sched_id      = sched_part.sched_id AND
				sched_inst_part.sched_part_id = sched_part.sched_part_id
			GROUP BY
				sched_part.sched_bom_part_db_id,
				sched_part.sched_bom_part_id
		) check_parts,
      (
			SELECT
				sched_part.sched_bom_part_db_id,
				sched_part.sched_bom_part_id,
				SUM( sched_inst_part.inst_qt ) AS inst_qt
			FROM
				evt_event task_event,
				sched_part,
				sched_inst_part
			WHERE
				task_event.h_event_db_id = aEventDbId AND
				task_event.h_event_id    = aEventId
				AND
				task_event.hist_bool = 0	AND
         			task_event.rstat_cd	= 0
				AND
				sched_part.sched_db_id = task_event.event_db_id AND
				sched_part.sched_id    = task_event.event_id
				AND
				sched_inst_part.sched_db_id   = sched_part.sched_db_id AND
				sched_inst_part.sched_id      = sched_part.sched_id AND
				sched_inst_part.sched_part_id = sched_part.sched_part_id
			GROUP BY
				sched_part.sched_bom_part_db_id,
				sched_part.sched_bom_part_id
      ) task_parts
	WHERE
      check_parts.sched_bom_part_db_id (+)= task_parts.sched_bom_part_db_id AND
      check_parts.sched_bom_part_id    (+)= task_parts.sched_bom_part_id
      AND
		( task_parts.inst_qt + NVL( check_parts.inst_qt, 0 ) )
		>
		(
			SELECT
				NVL( SUM( NVL( inv_inv.bin_qt, 1 ) - NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ) ), 0 )
			FROM
				eqp_part_baseline,
				inv_inv,
				inv_loc
			WHERE
				eqp_part_baseline.bom_part_db_id = task_parts.sched_bom_part_db_id AND
				eqp_part_baseline.bom_part_id    = task_parts.sched_bom_part_id
				AND
				inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
				inv_inv.part_no_id    = eqp_part_baseline.part_no_id
				AND
				inv_inv.inv_cond_db_id = 0 AND
				inv_inv.inv_cond_cd    = 'RFI'
				AND
				inv_inv.issued_bool = 0		AND
         			inv_inv.rstat_cd    = 0
				AND
				inv_loc.loc_db_id = inv_inv.loc_db_id AND
				inv_loc.loc_id    = inv_inv.loc_id
				AND
				inv_loc.supply_loc_db_id = aLocDbId AND
				inv_loc.supply_loc_id    = aLocId	AND
         			inv_loc.rstat_cd	 = 0
		);


   -- If lPartReqsFulfilled = -1, then we know that not all part requests can be fulfilled.
   -- Then check for part at remote location.
   IF lPartReqsFulfilled = -1 THEN

	SELECT
			DECODE( COUNT(*), 0, 2, -1 )
		INTO
			lPartReqsFulfilled
		FROM
			(
				SELECT
					sched_part.sched_bom_part_db_id,
					sched_part.sched_bom_part_id,
					SUM( sched_inst_part.inst_qt ) AS inst_qt
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
							inv_loc.supply_loc_id    = aLocId   AND
							inv_loc.rstat_cd	 = 0
							AND
							inv_loc.overnight_shift_db_id = shift_shift.shift_db_id AND
							inv_loc.overnight_shift_id	= shift_shift.shift_id
							AND
							evt_loc.loc_db_id = inv_loc.loc_db_id AND
							evt_loc.loc_id    = inv_loc.loc_id
							AND
							evt_event.event_db_id = evt_loc.event_db_id AND
							evt_event.event_id    = evt_loc.event_id
							AND
							evt_event.hist_bool = 0 AND
							evt_event.rstat_cd = 0
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
					evt_event task_event,
					sched_part,
					sched_inst_part
				WHERE
					task_event.h_event_db_id = overnight_checks.event_db_id AND
					task_event.h_event_id    = overnight_checks.event_id
					AND
					task_event.hist_bool = 0 AND
					task_event.rstat_cd = 0
					AND
					sched_part.sched_db_id = task_event.event_db_id AND
					sched_part.sched_id    = task_event.event_id
					AND
					sched_inst_part.sched_db_id   = sched_part.sched_db_id AND
					sched_inst_part.sched_id      = sched_part.sched_id AND
					sched_inst_part.sched_part_id = sched_part.sched_part_id
				GROUP BY
					sched_part.sched_bom_part_db_id,
					sched_part.sched_bom_part_id
			) check_parts,
	      (
				SELECT
					sched_part.sched_bom_part_db_id,
					sched_part.sched_bom_part_id,
					SUM( sched_inst_part.inst_qt ) AS inst_qt
				FROM
					evt_event task_event,
					sched_part,
					sched_inst_part
				WHERE
					task_event.h_event_db_id = aEventDbId AND
					task_event.h_event_id    = aEventId
					AND
					task_event.hist_bool = 0	AND
         				task_event.rstat_cd  = 0
					AND
					sched_part.sched_db_id = task_event.event_db_id AND
					sched_part.sched_id    = task_event.event_id
					AND
					sched_inst_part.sched_db_id   = sched_part.sched_db_id AND
					sched_inst_part.sched_id      = sched_part.sched_id AND
					sched_inst_part.sched_part_id = sched_part.sched_part_id
				GROUP BY
					sched_part.sched_bom_part_db_id,
					sched_part.sched_bom_part_id
	      ) task_parts
		WHERE
	      check_parts.sched_bom_part_db_id (+)= task_parts.sched_bom_part_db_id AND
	      check_parts.sched_bom_part_id    (+)= task_parts.sched_bom_part_id
	      AND
			( task_parts.inst_qt + NVL( check_parts.inst_qt, 0 ) )
			>
			(
				SELECT
					NVL( SUM( NVL( inv_inv.bin_qt, 1 ) - NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ) ), 0 )
				FROM
					eqp_part_baseline,
					inv_inv,
					inv_loc
				WHERE
					eqp_part_baseline.bom_part_db_id = task_parts.sched_bom_part_db_id AND
					eqp_part_baseline.bom_part_id    = task_parts.sched_bom_part_id
					AND
					inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
					inv_inv.part_no_id    = eqp_part_baseline.part_no_id
					AND
					inv_inv.inv_cond_db_id = 0 AND
					inv_inv.inv_cond_cd    = 'RFI'
					AND
					inv_inv.issued_bool = 0		AND
         				inv_inv.rstat_cd    = 0
					AND
					inv_loc.loc_db_id = inv_inv.loc_db_id AND
					inv_loc.loc_id    = inv_inv.loc_id
		);

	END IF;

   /* Return the result */
   RETURN lPartReqsFulfilled;
END;
/