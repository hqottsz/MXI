--liquibase formatted sql


--changeSet acor_acft_wp_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_acft_wp_v1
AS 
SELECT
    sched_stask.alt_id            AS sched_id,
    sched_stask.wo_ref_sdesc      AS work_package_number,
    sched_stask.barcode_sdesc     AS barcode,
    evt_event.sched_start_dt      AS schedule_start_date,
    evt_event.sched_end_dt        AS schedule_end_date,
    evt_event.actual_start_dt     AS actual_start_date,
    evt_event.event_dt            AS actual_end_date,
    evt_event.event_sdesc         AS work_package_name,
    evt_event.doc_ref_sdesc       AS doc_reference_name,
    fnc_account.alt_id            AS account_id,
    fnc_account.account_cd        AS issue_to_account,
    sched_stask.heavy_bool        AS maintenance_flag,
    inv_loc.alt_id                AS location_id,
    inv_loc.loc_cd                AS location_code,
    inv_loc.loc_type_cd           AS location_type_code,
    -- work types
    CONCAT_DATA(CURSOR(SELECT
                          work_type_cd
                       FROM
                          sched_work_type
                       WHERE
                          sched_work_type.sched_db_id = sched_stask.sched_db_id AND
                          sched_work_type.sched_id    = sched_stask.sched_id
                      )
                 )                AS work_type_list,
    acft_inv.alt_id               AS aircraft_id,
    inv_ac_reg.ac_reg_cd          AS registration_code,
    inv_inv.serial_no_oem         AS aircraft_serial_number,
    eqp_part_no.part_no_oem       AS aircraft_part_number,
    eqp_part_no.manufact_cd       AS aircraft_manufacturer,
    evt_event.event_status_cd     AS status,
    airport_location.loc_cd       AS station,
    org_carrier.alt_id            AS operator_id,
    operator_org.alt_id           AS operator_organization_id,
    (
       SELECT
          org_org.alt_id
       FROM
          org_org
       WHERE
          org_org.org_type_cd='ADMIN'
    ) AS admin_organization_id,
    hr_usg.h_tsn_qt hours_tsn_completion,
    hr_usg.h_tso_qt hours_ts0_completion,
    cyc_usg.h_tsn_qt cycles_tsn_completion,
    cyc_usg.h_tso_qt cycles_ts0_completion
FROM
   sched_stask
   INNER JOIN evt_event ON
      sched_stask.sched_db_id = evt_event.event_db_id AND
      sched_stask.sched_id    = evt_event.event_id
   -- issue to account
   LEFT JOIN fnc_account ON
      sched_stask.issue_account_db_id = fnc_account.account_db_id AND
      sched_stask.issue_account_id    = fnc_account.account_id
   -- aircraft
   INNER JOIN inv_inv ON
      sched_stask.main_inv_no_db_id = inv_inv.inv_no_db_id AND
      sched_stask.main_inv_no_id    = inv_inv.inv_no_id
   INNER JOIN eqp_part_no ON
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
   -- aircraft
   INNER JOIN inv_ac_reg ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   -- location
   LEFT JOIN evt_loc ON
      evt_event.event_db_id = evt_loc.event_db_id AND
      evt_event.event_id    = evt_loc.event_id
   LEFT JOIN inv_loc ON
      evt_loc.loc_db_id = inv_loc.loc_db_id AND
      evt_loc.loc_id    = inv_loc.loc_id
   LEFT JOIN inv_loc airport_location ON
      inv_loc.supply_loc_db_id = airport_location.loc_db_id AND
      inv_loc.supply_loc_id    = airport_location.loc_id
   -- operator
   LEFT JOIN org_carrier ON
      inv_inv.carrier_db_id = org_carrier.carrier_db_id AND
      inv_inv.carrier_id    = org_carrier.carrier_id
   LEFT JOIN org_org operator_org ON
      org_carrier.org_db_id = operator_org.org_db_id AND
      org_carrier.org_id    = operator_org.org_id
   INNER JOIN evt_inv ON
      evt_inv.event_db_id = evt_event.event_db_id AND
      evt_inv.event_id    = evt_event.event_id
      AND
      evt_inv.main_inv_bool = 1
   -- completion usage hours
   LEFT JOIN evt_inv_usage hr_usg ON
      hr_usg.event_db_id  = evt_inv.event_db_id AND
      hr_usg.event_id     = evt_inv.event_id AND
      hr_usg.event_inv_id = evt_inv.event_inv_id
      AND
      hr_usg.data_type_db_id = 0 AND
      hr_usg.data_type_id    = 1
   -- completion usage cycles
   LEFT JOIN evt_inv_usage cyc_usg ON
      cyc_usg.event_db_id  = evt_inv.event_db_id AND
      cyc_usg.event_id     = evt_inv.event_id AND
      cyc_usg.event_inv_id = evt_inv.event_inv_id
      AND
      cyc_usg.data_type_db_id = 0 AND
      cyc_usg.data_type_id    = 10
WHERE
   sched_stask.task_class_db_id = 0 AND
   sched_stask.task_class_cd    = 'CHECK'
   AND
   sched_stask.auto_complete_bool = 0
;