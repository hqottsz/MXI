--liquibase formatted sql


--changeSet mv_fleetlist:1 stripComments:false
/********************************************************************************
*
* View:           mv_fleetlist
*
* Description:    This materialized view will return the details of the
*                 fleetlist
*
* Orig.Coder:    eirvine
* Recent Coder:  rbellemare
* Recent Date:   2017.10.23
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MV_FLEETLIST
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT *
FROM (
        SELECT
           -- aircraft details
           inv_inv.inv_no_db_id AS aircraft_db_id,
           inv_inv.inv_no_id AS aircraft_id,
           inv_inv.inv_no_sdesc AS aircraft_sdesc,
           inv_ac_reg.inv_capability_cd AS aircraft_capability_cd,
           inv_inv.authority_db_id AS aircraft_authority_db_id,
           inv_inv.authority_id AS aircraft_authority_id,
           inv_inv.assmbl_db_id AS aircraft_assmbl_db_id,
           inv_inv.assmbl_cd AS aircraft_assmbl_cd,
           --aircraft location details
           acft_loc.loc_db_id AS aircraft_loc_db_id,
           acft_loc.loc_id AS aircraft_loc_id,
           acft_loc.loc_cd AS aircraft_loc_cd,
           -- work package details
           check_evt_event.event_db_id AS wp_db_id,
           check_evt_event.event_id AS wp_id,
           check_evt_event.event_sdesc AS wp_desc,
           getSchedWorkTypes(check_sched_stask.sched_db_id, check_sched_stask.sched_id) AS wp_work_type_cd,
           check_evt_event.event_status_cd AS wp_status_cd,
           check_sched_stask.task_class_cd AS wp_class_cd,
           ref_task_subclass.user_subclass_cd AS wp_user_subclass_cd,
           -- work package scheduling details
           check_evt_event.actual_start_gdt AS wp_actual_start_gdt,
           check_evt_event.sched_start_gdt AS wp_sched_start_gdt,
           check_evt_event.sched_end_gdt AS wp_sched_end_gdt,
           check_evt_event.event_gdt AS wp_event_gdt,
           check_inv_loc.loc_db_id AS wp_work_loc_db_id,
           check_inv_loc.loc_id AS wp_work_loc_id,
           check_inv_loc.loc_cd AS wp_work_loc_cd,
           -- flight locations
           fl_leg.departure_loc_db_id AS dep_loc_db_id,
           fl_leg.departure_loc_id AS dep_loc_id,
           dep_inv_loc.loc_cd AS dep_loc_cd,
           fl_leg.arrival_loc_db_id AS arr_loc_db_id,
           fl_leg.arrival_loc_id AS arr_loc_id,
           arr_inv_loc.loc_cd AS arr_loc_cd,
           fl_leg.leg_id,
		   -- Flight validation
           FIRST_VALUE( fl_leg.leg_id )
                    OVER (
                         PARTITION BY
                            fl_leg.aircraft_db_id,
                            fl_leg.aircraft_id
                         ORDER BY
                            fl_leg.actual_departure_dt desc
                    ) AS last_leg_id,
           -- sorting info
           -- IF IN WORK sort before other status based on actual start date
           -- ELSE there is a scheduled check, sort before unscheduled
           ( CASE WHEN check_evt_event.event_status_cd = 'IN WORK'
                  THEN 0
                  ELSE 1
             END ) ||
                NVL2( check_evt_event.actual_start_gdt,
                    TO_CHAR( check_evt_event.actual_start_gdt, 'YYYY-MM-DD HH24:MI:SS'),
                    NVL2( check_evt_event.sched_start_gdt,
                        TO_CHAR( check_evt_event.sched_start_gdt, 'YYYY-MM-DD HH24:MI:SS' ),
                        'A'
                    )
            ) AS start_date_sorting
        FROM
           -- aircraft details
           inv_ac_reg
           INNER JOIN inv_inv ON
                 inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
                 inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
           INNER JOIN inv_loc acft_loc ON
                 acft_loc.loc_db_id = inv_inv.loc_db_id AND
                 acft_loc.loc_id    = inv_inv.loc_id
           LEFT OUTER JOIN
           -- next scheduled work package details
           (  SELECT DISTINCT
                 inv_ac_reg.inv_no_db_id,
                 inv_ac_reg.inv_no_id,
                 FIRST_VALUE( sched_stask.sched_db_id )
                    OVER (
                       PARTITION BY
                          inv_ac_reg.inv_no_db_id,
                          inv_ac_reg.inv_no_id
                       ORDER BY
                          DECODE( evt_event.event_status_cd, 'IN WORK', 0, 1) ASC,
                          evt_event.sched_start_gdt
                    ) AS sched_db_id,
                 FIRST_VALUE( sched_stask.sched_id )
                    OVER (
                       PARTITION BY
                          inv_ac_reg.inv_no_db_id,
                          inv_ac_reg.inv_no_id
                       ORDER BY
                          DECODE(evt_event.event_status_cd, 'IN WORK', 0, 1) ASC,
                          evt_event.sched_start_gdt
                    ) AS sched_id
              FROM
                inv_ac_reg
                JOIN sched_stask ON
                  sched_stask.main_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
                  sched_stask.main_inv_no_id    = inv_ac_reg.inv_no_id AND
                  sched_stask.task_class_db_id  = 0 AND
                  sched_stask.task_class_cd     = 'CHECK'
                JOIN evt_event ON
                  evt_event.event_db_id = sched_stask.sched_db_id AND
                  evt_event.event_id    = sched_stask.sched_id AND
                  evt_event.hist_bool   = 0
           ) next_sched_stask ON
                next_sched_stask.inv_no_db_id = inv_inv.inv_no_db_id AND
                next_sched_stask.inv_no_id    = inv_inv.inv_no_id
           LEFT OUTER JOIN evt_event check_evt_event ON
                check_evt_event.event_db_id = next_sched_stask.sched_db_id AND
                check_evt_event.event_id    = next_sched_stask.sched_id
           LEFT OUTER JOIN sched_stask check_sched_stask ON
                check_sched_stask.sched_db_id = check_evt_event.event_db_id AND
                check_sched_stask.sched_id    = check_evt_event.event_id
           LEFT OUTER JOIN ref_task_subclass ON
                ref_task_subclass.task_subclass_db_id  = check_sched_stask.task_subclass_db_id AND
                ref_task_subclass.task_subclass_cd     = check_sched_stask.task_subclass_cd
           LEFT OUTER JOIN evt_loc check_evt_loc ON
                check_evt_loc.event_db_id = check_evt_event.event_db_id AND
                check_evt_loc.event_id   = check_evt_event.event_id AND
                check_evt_loc.event_loc_id   = 1
           LEFT OUTER JOIN inv_loc check_inv_loc ON
                check_inv_loc.loc_db_id = check_evt_loc.loc_db_id AND
                check_inv_loc.loc_id    = check_evt_loc.loc_id
           -- get the current flight details
           LEFT OUTER JOIN fl_leg ON
             fl_leg.aircraft_db_id  = inv_inv.inv_no_db_id AND
             fl_leg.aircraft_id     = inv_inv.inv_no_id AND
             fl_leg.flight_leg_status_cd IN ('MXOUT', 'MXOFF', 'MXON', 'MXIN', 'MXDIVERT')
           LEFT OUTER JOIN inv_loc dep_inv_loc ON
                       dep_inv_loc.loc_db_id = fl_leg.departure_loc_db_id AND
                       dep_inv_loc.loc_id    = fl_leg.departure_loc_id
           LEFT OUTER JOIN inv_loc arr_inv_loc ON
                       arr_inv_loc.loc_db_id = fl_leg.arrival_loc_db_id AND
                       arr_inv_loc.loc_id    = fl_leg.arrival_loc_id
        WHERE
           -- get the aircraft details
           inv_inv.inv_class_db_id = 0 AND
           inv_inv.inv_class_cd    = 'ACFT' AND
           inv_inv.rstat_cd        = 0   AND
           inv_inv.locked_bool     = 0 )
WHERE
   leg_id IS NULL
   OR
   leg_id = last_leg_id;