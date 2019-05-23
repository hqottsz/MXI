--liquibase formatted sql


--changeSet acor_acft_ts_deadline_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_ts_deadline_v1
AS
SELECT
         sched_stask.alt_id               AS sched_id,
         sched_stask.task_class_cd        AS task_class_code,
         mim_data_type.alt_id             AS data_type_id,
         mim_data_type.data_type_cd       AS data_type_code,
         mim_data_type.domain_type_cd     AS domain_type_code,
         --
         getExtendedDeadlineDt (evt_sched_dead.deviation_qt,
                                evt_sched_dead.sched_dead_dt,
                                mim_data_type.domain_type_cd,
                                mim_data_type.data_type_cd,
                               ref_eng_unit.ref_mult_qt
                              ) AS extended_deadline_date,
         evt_sched_dead.sched_dead_dt     AS schedule_deadline_date,
         evt_sched_dead.sched_driver_bool AS schedule_driver_flag,
         sched_stask.barcode_sdesc        AS task_barcode,
         --
         CASE mim_data_type.domain_type_cd
            WHEN 'CA' THEN
               TRIM(TO_CHAR(0, '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))
            ELSE
               TRIM(TO_CHAR(evt_sched_dead.usage_rem_qt + evt_sched_dead.deviation_qt,
                                             '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))
         END AS extended_usage_remaining,
         -- formating algorithm
         --
         --    Step 1 - determine the precision, and therefore a format mask. The precision value is located in mim_data_type.
         --    Step 2 - covert the value to a character string.
         --    Step 3 - trim the character string containing the converted value.
         --
         TRIM(TO_CHAR(evt_sched_dead.sched_dead_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS extended_deadline_quantity,
         TRIM(TO_CHAR(evt_sched_dead.sched_dead_qt,'9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS schedule_deadline_quantity,
         TRIM(TO_CHAR(evt_sched_dead.usage_rem_qt, '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS usage_remaining_quantity,
         TRIM(TO_CHAR(evt_sched_dead.interval_qt,  '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS interval_quantity,
         TRIM(TO_CHAR(getExtendedDeadlineDt (evt_sched_dead.deviation_qt,
                                             evt_sched_dead.sched_dead_dt,
                                             mim_data_type.domain_type_cd,
                                             mim_data_type.data_type_cd,
                                             ref_eng_unit.ref_mult_qt
                                            ) - SYSDATE,
                                                   '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS estimated_remaining_days,
         TRIM(TO_CHAR(evt_sched_dead.deviation_qt, '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS deviation_quantity,
         TRIM(TO_CHAR(evt_sched_dead.notify_qt,    '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS notification_quantity,
         TRIM(TO_CHAR(evt_sched_dead.prefixed_qt,  '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS prefixed_quantity,
         TRIM(TO_CHAR(evt_sched_dead.postfixed_qt, '9999999999999999999999' || RTRIM(RPAD('.', entry_prec_qt + 1,'9'),'.')))  AS postfixed_quantity
         --
     FROM
         sched_stask
         INNER JOIN evt_event ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
         -- aircraft
         INNER JOIN inv_inv ON
            sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
            sched_stask.main_inv_no_id = inv_inv.inv_no_id
         -- schedule
         INNER JOIN evt_sched_dead ON
            evt_event.event_db_id = evt_sched_dead.event_db_id AND
            evt_event.event_id    = evt_sched_dead.event_id
         INNER JOIN mim_data_type ON
            evt_sched_dead.data_type_db_id = mim_data_type.data_type_db_id AND
            evt_sched_dead.data_type_id    = mim_data_type.data_type_id
         INNER JOIN ref_eng_unit ON
            ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
            ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
     WHERE
       inv_inv.inv_class_db_id = 0 AND
       inv_inv.inv_class_cd IN ('ACFT','SYS','ASSY')
;