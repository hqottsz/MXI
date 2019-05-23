--liquibase formatted sql


--changeSet MX-13141:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     IsInShiftWithDate
* Arguments:    aEventStartGdt   - event start date
*               aEventEndGdt     - event end date
*               aDate 	         - date of the shift
*               aTimeZone        - time zone of the shift
*               aShiftStartHr    - shift start hour
*               aShiftDurationHr - shift duration
* Return:       (NUMBER)       - 1 - event overlaps with the shift, 0 - otherwise
*
* Description:  Determines if the provided event overlaps with the provided shift 
*               information. It is assumed that the event dates are expressed in 
*               the default timezone, and the shift hour is defined in the provided 
*               timezone. 
* Orig.Coder:   A. Smolko
* Recent Coder: 
* Recent Date:  May 24, 2012
*
*********************************************************************************
*
* Copyright © 2012 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION IsInShiftWithDate
  (
     aEventStartGdt   DATE,
     aEventEndGdt     DATE,
     aDate 	          DATE,
     aTimeZone        utl_timezone.timezone_cd%TYPE,
     aShiftStartHr    NUMBER,
     aShiftDurationHr NUMBER

  ) RETURN NUMBER IS

   lShiftStartHr    VARCHAR2 (10);
   lShiftStart      TIMESTAMP;
   lShiftEnd        TIMESTAMP;
   lDefaultTimeZone utl_timezone.timezone_cd%TYPE;

BEGIN
   /* Convert the float hour value to string representation */
   SELECT lpad(trunc(aShiftStartHr), 2, '0')||':'||lpad(round((aShiftStartHr-trunc(aShiftStartHr))*60), 2, '0')
      INTO lShiftStartHr
   FROM dual;

   /* Find out shift start date based on the provided information */
   lShiftStart := to_timestamp( to_char(aDate,'DD-Mon-YYYY' )||lShiftStartHr||':00','DD-Mon-YYYY HH24:Mi:SS');

   /* Get the default timezone, the timezone of the provided event dates */
   SELECT timezone_cd
      INTO lDefaultTimeZone
      FROM utl_timezone
   WHERE default_bool=1;

   /* convert the provided date and shift start hr values into a date adjusted from the specified timezone to the server timezone */
   lShiftStart:= cast(FROM_TZ(lShiftStart, aTimeZone) AT TIME ZONE lDefaultTimeZone as timestamp) ;


   /* find the shift end timestamp */
   lShiftEnd := lShiftStart + ( aShiftDurationHr / 24 ) ;

   /* compare the provided event dates with the shift date */
   IF aEventStartGdt <= lShiftEnd AND
      aEventEndGdt   >= lShiftStart THEN
      RETURN 1;
   END IF;

   RETURN 0;

END IsInShiftWithDate;
/

--changeSet MX-13141:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      IsInShift
* Arguments:     aEventStartGdt   - event start date
*                aEventEndGdt     - event end date
*                aEventTimeZone   - event time zone to be applied to shift start hr
*                aShiftStartHr    - shift start hour
*                aShiftDurationHr - shift duration
* Description:   This function determines if the event with the specified start and 
*                end dates falls within the shift with the provided start hour and 
*                duration. It is assumed that the event dates are in default time zone.
*                The shift start hour is provided in event's timezone and has to be 
*                converted to the default timezone
*
* Orig.Coder:    Andrei Smolko
* Recent Coder:  
* Recent Date:   May 17, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION IsInShift
(
   aEventStartGdt   DATE,
   aEventEndGdt     DATE,
   aEventTimeZone   utl_timezone.timezone_cd%TYPE,
   aShiftStartHr    NUMBER,
   aShiftDurationHr NUMBER

) RETURN NUMBER
IS
  lEventStartDate TIMESTAMP;
  lResult         NUMBER;
BEGIN
  lEventStartDate := TRUNC( aEventStartGdt );

  lResult := IsInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate, aEventTimeZone, aShiftStartHr, aShiftDurationHr);
  IF lResult = 1 THEN
     RETURN 1;
  END IF;

  -- Move the shift start date by one day and check again to handle the case when workpackage spans two days and 
  -- the workpackage start date is different than the start date of the shift that the workpackage overlaps with
  -- (i.e. 1:00-3:00 shift and the workpackage is 22:00-6:00)
  lResult := IsInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate+1, aEventTimeZone, aShiftStartHr, aShiftDurationHr);
  IF lResult = 1 THEN
     RETURN 1;
  END IF;

  -- Move the shift by one day and check again to handle the case when shift spans two days and the workpackage 
  -- start date is different than the start date of the shift that the workpackage overlaps with
  -- (i.e. 22:00-6:00 shift and the workpackage is 1:00-3:00)
  RETURN IsInShiftWithDate(aEventStartGdt, aEventEndGdt, lEventStartDate-1, aEventTimeZone, aShiftStartHr, aShiftDurationHr);

END IsInShift;
/

--changeSet MX-13141:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet MX-13141:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getLabourCapacitySummary
* Arguments:     aLocDbId  - primary key of the location (line location)
*                aLocId    - primary key of the location (line location)
*                aDate     - specific date
*
* Description:   For all labor assigned to open tasks belonging to checks that
*                are scheduled during line capacity shifts on a particular date,
*                determine if the assigned work for a particular labor skill exceeds
*                the airport's capacity.
*
* Returns:        1 if the station has sufficient capacity.
*                 0 if the station capacity is approaching the warning threshold.
*                -1 if the station has insufficient capacity.
*
* Orig.Coder:    Lisa Henderson/Yui Sotozaki
* Recent Coder:  Andrei Smolko
* Recent Date:   May 24, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getLabourCapacitySummary
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId   inv_loc.loc_id%TYPE,
   aDate    evt_event.event_dt%TYPE
) RETURN NUMBER
IS
   lSufficientLabour NUMBER;
BEGIN

   SELECT
      /* Find worst case result: */
      /* If capacity - warning percentage < scheduled, return 0 */
      /* If capacity = scheduled, return 0 */
      /* If capacity - warning percentage < scheduled, return 0 */
      /* If capacity - warning percentage >= scheduled, return 1 */
      NVL(
         MIN(
            DECODE(
               SIGN( NVL( capacity.capacity_hr, 0 ) - NVL( labour.sched_hr, 0 ) ),
               -1,
               -1,
               0,
               0,
               DECODE(
                  SIGN( utl_config_parm.parm_value * NVL( capacity.capacity_hr, 0 ) - NVL( labour.sched_hr, 0 ) ),
                  -1,
                  0,
                  1
               )
            )
         ),
         1
      ) AS labour_indicator
   INTO
      lSufficientLabour
   FROM
      (
         SELECT
            sched_labour.labour_skill_db_id,
            sched_labour.labour_skill_cd,
            NVL(SUM(tech_role.sched_hr),0) AS sched_hr
         FROM
            (  SELECT
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
                  inv_loc.loc_db_id = aLocDbId AND
                  inv_loc.loc_id = aLocId AND
                  inv_loc.rstat_cd  = 0
                  AND
                  inv_loc.overnight_shift_db_id = shift_shift.shift_db_id AND
                  inv_loc.overnight_shift_id = shift_shift.shift_id 
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
                  sched_stask.heavy_bool       = 0 AND
                  sched_stask.rstat_cd = 0
                  AND
                  evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id    = evt_event.event_id
                  AND
                  evt_inv.main_inv_bool = 1
            ) overnight_checks,
            evt_event task_event,
            sched_labour
            INNER JOIN sched_labour_role tech_role ON
               tech_role.labour_db_id = sched_labour.labour_db_id AND
               tech_role.labour_id    = sched_labour.labour_id
               AND
               tech_role.labour_role_type_cd = 'TECH'
         WHERE
            task_event.h_event_db_id = overnight_checks.event_db_id AND
            task_event.h_event_id    = overnight_checks.event_id
            AND
            task_event.hist_bool = 0 AND
            task_event.rstat_cd = 0
            AND
            sched_labour.sched_db_id = task_event.event_db_id AND
            sched_labour.sched_id    = task_event.event_id
            AND
            sched_labour.labour_stage_cd IN ('ACTV', 'IN WORK')
            AND
            -- Only consider labour with scheduled hours
            tech_role.sched_hr > 0
         GROUP BY
            sched_labour.labour_skill_db_id,
            sched_labour.labour_skill_cd
      ) labour,
      (
         SELECT
            org_hr_shift_plan.labour_skill_db_id,
            org_hr_shift_plan.labour_skill_cd,
            SUM( org_hr_shift_plan.work_hours_qt ) AS capacity_hr
         FROM
            org_hr_shift_plan,
            inv_loc
         WHERE
            org_hr_shift_plan.day_dt = trunc( aDate )
            AND
            org_hr_shift_plan.loc_db_id = aLocDbId AND
            org_hr_shift_plan.loc_id    = aLocId            
            AND
            inv_loc.loc_db_id = org_hr_shift_plan.loc_db_id AND
            inv_loc.loc_id    = org_hr_shift_plan.loc_id 
            AND
            inv_loc.overnight_shift_db_id IS NOT NULL 
         GROUP BY
            org_hr_shift_plan.labour_skill_db_id,
            org_hr_shift_plan.labour_skill_cd
      ) capacity,
      utl_config_parm
   WHERE
      capacity.labour_skill_db_id (+)= labour.labour_skill_db_id AND
      capacity.labour_skill_cd    (+)= labour.labour_skill_cd
      AND
      utl_config_parm.parm_type = 'LOGIC' AND
      utl_config_parm.parm_name = 'CAPACITY_WARNING_PERCENT';

   /* Return the result */
   RETURN lSufficientLabour;
END;
/

--changeSet MX-13141:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getPartsCapacitySummary
* Arguments:     aLocDbId  - primary key of the location (supply location)
*                aLocId    - primary key of the location (supply location)
*                aDate     - specific date
*
* Description:   For all part requests assigned to open tasks belonging to checks 
*                that are scheduled during line capacity shifts on a particular 
*                date, determine if the part requests are all fulfilled.
*
* Returns:        1 if all part requests are fulfilled.
*                -1 if at least one part request is not fulfilled.
*		  2 if there are remote parts available
*
* Orig.Coder:    Lisa Henderson/Yui Sotozaki
* Recent Coder:  Andrei Smolko
* Recent Date:   May 24, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getPartsCapacitySummary
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId   inv_loc.loc_id%TYPE,
   aDate    evt_event.event_dt%TYPE
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
            -- Distinct is NOT required for filtering out duplicate shift rows since only one shift can be marked for line capacity.
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
               inv_loc.rstat_cd		= 0
               AND
               inv_loc.overnight_shift_db_id = shift_shift.shift_db_id AND
               inv_loc.overnight_shift_id  = shift_shift.shift_id
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
         sched_inst_part,
         req_part
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
         AND
         req_part.sched_db_id        (+)= sched_inst_part.sched_db_id AND
         req_part.sched_id           (+)= sched_inst_part.sched_id AND
         req_part.sched_part_id      (+)= sched_inst_part.sched_part_id AND
         req_part.sched_inst_part_id (+)= sched_inst_part.sched_inst_part_id
         AND
         req_part.inv_no_db_id IS NULL
      GROUP BY
         sched_part.sched_bom_part_db_id,
         sched_part.sched_bom_part_id
   ) parts
WHERE
   parts.inst_qt
   >
   (
      SELECT
         NVL( SUM( NVL( inv_inv.bin_qt, 1 ) - NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ) ), 0 )
      FROM
         eqp_part_baseline,
         inv_inv,
         inv_loc
      WHERE
         eqp_part_baseline.bom_part_db_id = parts.sched_bom_part_db_id AND
         eqp_part_baseline.bom_part_id    = parts.sched_bom_part_id
         AND
         inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
         inv_inv.part_no_id    = eqp_part_baseline.part_no_id
         AND
         inv_inv.inv_cond_db_id = 0 AND
         inv_inv.inv_cond_cd    = 'RFI'
         AND
         inv_inv.issued_bool = 0	AND
         inv_inv.rstat_cd    = 0
         AND
         inv_loc.loc_db_id = inv_inv.loc_db_id AND
         inv_loc.loc_id    = inv_inv.loc_id
         AND
         inv_loc.supply_loc_db_id = aLocDbId AND
         inv_loc.supply_loc_id    = aLocId
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
	            -- Distinct is NOT required for filtering out duplicate shift rows since only one shift can be marked for line capacity.
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
	               inv_loc.supply_loc_id    = aLocId	AND
         	       inv_loc.rstat_cd	  	= 0
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
	         sched_inst_part,
	         req_part
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
	         AND
	         req_part.sched_db_id        (+)= sched_inst_part.sched_db_id AND
	         req_part.sched_id           (+)= sched_inst_part.sched_id AND
	         req_part.sched_part_id      (+)= sched_inst_part.sched_part_id AND
	         req_part.sched_inst_part_id (+)= sched_inst_part.sched_inst_part_id
	         AND
	         req_part.inv_no_db_id IS NULL
	      GROUP BY
	         sched_part.sched_bom_part_db_id,
	         sched_part.sched_bom_part_id
	   ) parts
	WHERE
	   parts.inst_qt
	   >
	   (
	      SELECT
	         NVL( SUM( NVL( inv_inv.bin_qt, 1 ) - NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ) ), 0 )
	      FROM
	         eqp_part_baseline,
	         inv_inv,
	         inv_loc
	      WHERE
	         eqp_part_baseline.bom_part_db_id = parts.sched_bom_part_db_id AND
	         eqp_part_baseline.bom_part_id    = parts.sched_bom_part_id
	         AND
	         inv_inv.part_no_db_id = eqp_part_baseline.part_no_db_id AND
	         inv_inv.part_no_id    = eqp_part_baseline.part_no_id
	         AND
	         inv_inv.inv_cond_db_id = 0 AND
	         inv_inv.inv_cond_cd    = 'RFI'
	         AND
	         inv_inv.issued_bool = 0	AND
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

--changeSet MX-13141:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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