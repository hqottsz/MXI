--liquibase formatted sql


--changeSet acor_workpackages_v2:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_workpackages_v2
AS 
SELECT
    sched_stask.alt_id,
    inv_inv.alt_id                AS inventory_id,
    inv_inv.inv_no_sdesc          AS inv_name,
    evt_event.event_sdesc         AS work_package_name,
    inv_loc.loc_cd                AS location_code,
    evt_event.actual_start_dt     AS actual_start_date,
    evt_event.event_dt            AS actual_end_date,
    evt_event.sched_start_dt      AS schedule_start_date,
    evt_event.sched_end_dt        AS schedule_end_date,
    inv_inv.serial_no_oem         as serial_number,
    inv_loc.timezone_cd,
    sched_stask.heavy_bool        as heavy_bool,
    (
      SELECT
        MIN(sched_arrival_dt)
      FROM
         fl_leg
      WHERE
         fl_leg.aircraft_db_id = inv_inv.h_inv_no_db_id AND
         fl_leg.aircraft_id    = inv_inv.h_inv_no_id
         AND
         -- schedule arrival is less than schedule end date
         fl_leg.sched_arrival_dt <= evt_event.sched_start_dt
         AND
         -- airport
         fl_leg.arrival_loc_db_id = inv_loc.loc_db_id AND
         fl_leg.arrival_loc_id    = inv_loc.loc_id
         AND
         ROWNUM = 1
      GROUP BY
         leg_no
    )                             AS last_schedule_arrival_date,
    (
      SELECT
         MIN(sched_departure_dt)
      FROM
        fl_leg
      WHERE
         fl_leg.aircraft_db_id = inv_inv.h_inv_no_db_id AND
         fl_leg.aircraft_id    = inv_inv.h_inv_no_id
         AND
         -- schedule departure is greater than schedule end date
         fl_leg.sched_departure_dt >= evt_event.sched_end_dt
         AND
         -- airport
         fl_leg.departure_loc_db_id = inv_loc.loc_db_id AND
         fl_leg.departure_loc_id    = inv_loc.loc_id
         AND
         ROWNUM = 1
      GROUP BY
         leg_no
    )                             AS last_schedule_departure_date,
    sched_stask.wo_ref_sdesc      AS work_package_number,
    sched_stask.task_priority_cd  AS task_priority_code,
    sched_stask.barcode_sdesc     AS barcode,
    --
    CONCAT_DATA(CURSOR(SELECT
                          work_type_cd
                       FROM
                          sched_work_type
                       WHERE
                          sched_work_type.sched_db_id = sched_stask.sched_db_id AND
                          sched_work_type.sched_id    = sched_stask.sched_id
                      )
                 )                AS work_type_list,
    evt_event.event_status_cd     AS event_status_code,
	sched_stask.sched_db_id,
    sched_stask.sched_id
FROM
   sched_stask
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   -- inventory
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   LEFT JOIN evt_loc ON
      evt_event.event_db_id = evt_loc.event_db_id AND
      evt_event.event_id    = evt_loc.event_id
   -- view for location hierarchy
   LEFT JOIN inv_loc ON
      evt_loc.loc_db_id = inv_loc.loc_db_id AND
      evt_loc.loc_id    = inv_loc.loc_id
WHERE
   sched_stask.wo_ref_sdesc IS NOT NULL
   AND
   sched_stask.task_class_cd IN ('CHECK','RO')
;