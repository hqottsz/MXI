--liquibase formatted sql


--changeSet getLabourCapacitySummary:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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