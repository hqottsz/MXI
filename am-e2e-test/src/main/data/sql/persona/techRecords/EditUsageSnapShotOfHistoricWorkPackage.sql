-- Insert the usage into the table for the specific work package
INSERT INTO
   evt_inv_usage
   (
      event_db_id,
      event_id,
      event_inv_id,
      data_type_db_id,
      data_type_id,
      tsn_qt,
      tso_qt,
      tsi_qt,
      source_db_id,
      source_cd
   )
   SELECT
      evt_event.event_db_id,
      evt_event.event_id,
      1,
      0,
      1,
      300,
      200,
      100,
      0,
      'MAINTENIX'
   FROM
      evt_event
   WHERE
      evt_event.event_sdesc = 'EUSS-WP'
;
INSERT INTO
   evt_inv_usage
   (
      event_db_id,
      event_id,
      event_inv_id,
      data_type_db_id,
      data_type_id,
      tsn_qt,
      tso_qt,
      tsi_qt,
      source_db_id,
      source_cd
   )
   SELECT
      evt_event.event_db_id,
      evt_event.event_id,
      1,
      0,
      10,
      1,
      1,
      1,
      0,
      'MAINTENIX'
   FROM
      evt_event
   WHERE
      evt_event.event_sdesc = 'EUSS-WP'
;

-- Update the work packages to set its status to COMPLETE and
-- set its actual start and end dates using the scheduled start and end dates.
MERGE INTO
   evt_event tgt
USING
   (
      SELECT
         event_db_id,
         event_id,
         sched_start_dt,
         sched_start_gdt,
         sched_end_dt,
         sched_end_gdt
      FROM
         evt_event
      WHERE
         event_sdesc = 'EUSS-WP'
   ) src
ON
   (
      tgt.event_db_id = src.event_db_id AND
      tgt.event_id    = src.event_id
   )
WHEN MATCHED THEN UPDATE SET
   tgt.event_status_cd  = 'COMPLETE',
   tgt.hist_bool        = 1,
   tgt.actual_start_dt  = src.sched_start_dt,
   tgt.actual_start_gdt = src.sched_start_gdt,
   tgt.event_dt         = src.sched_end_dt,
   tgt.event_gdt        = src.sched_end_gdt
;