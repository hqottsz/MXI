--liquibase formatted sql


--changeSet acor_hist_comp_rmvl_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hist_comp_rmvl_v1 
AS
WITH
rvw_inv_inv
AS
   (
     SELECT
        alt_id,
        inv_no_db_id,
        inv_no_id,
        part_no_db_id,
        part_no_id,
        carrier_db_id,
        carrier_id,
        po_db_id,
        po_id,
        serial_no_oem,
        inv_no_sdesc,
        inv_cond_cd,
        receive_cond_cd
     FROM 
        inv_inv 
   ),
rvw_inv_usage  
AS
   (
     SELECT
        event_db_id,
        event_id,
        event_inv_id,
        data_type_db_id,
        data_type_id,
        tsn_qt,
        tsi_qt
     FROM 
        evt_inv_usage
   ),
rvw_repair_order 
AS
  (
    SELECT
       main_inv_no_db_id,
       main_inv_no_id,
       barcode_sdesc,
       wo_ref_sdesc,
       ROW_NUMBER() OVER (PARTITION BY main_inv_no_db_id, main_inv_no_id ORDER BY evt_event.event_dt DESC) rn
    FROM 
       sched_stask
       INNER JOIN evt_event ON 
          sched_stask.sched_db_id = evt_event.event_db_id AND
          sched_stask.sched_id    = evt_event.event_id
          AND
          hist_bool = 1
    WHERE
       sched_stask.task_class_cd = 'RO'
  )   
SELECT 
   rmvd_inv.alt_id                 AS inventory_id,
   sched_stask.alt_id              AS sched_id,
   evt_inv.assmbl_cd,
   org_carrier.carrier_cd,
   inv_ac_reg.ac_reg_cd,
   ac_part_no.part_no_oem          AS ac_pn,
   acft_inv.serial_no_oem          AS ac_sn,
   eqp.part_no_oem                 AS rmvd_pn,
   eqp.manufact_cd,
   rmvd_inv.serial_no_oem,
   eqp.part_no_sdesc               AS rmd_partdescription,
   evt_event.event_dt              AS rmvl_dt,
   ref_rem.spec2k_remove_reason_cd,
   rmvd.remove_reason_cd,
   root.inv_no_sdesc  root_name,
   root.serial_no_oem root_sn,
   substr(eab.assmbl_bom_cd, 1, 2) AS ata_chapter,
   substr(eab.assmbl_bom_cd, 4, 2) AS ata_section,
   hours.tsn_qt                    AS tsn,
   cycles.tsn_qt                   AS csn,
   hours.tsi_qt                    AS tsi,
   cycles.tsi_qt                   AS csi,
   inv_last_install.install_dt     AS last_install_date,
   recent_ro.barcode_sdesc         AS ro_number,
   last_ro.barcode_sdesc           AS last_ro,
   eqp_part_no.part_no_oem         AS pn_in,
   sched_inst_part.serial_no_oem   AS sn_in,
   rmvd_inv.receive_cond_cd AS last_received_condition,
   po_event.event_sdesc            AS po_number
FROM 
   sched_rmvd_part rmvd    -- Removed components
   INNER JOIN sched_stask ON 
      rmvd.sched_db_id = sched_stask.sched_db_id AND
      rmvd.sched_id    = sched_stask.sched_id
   -- pn, sn of removed component
   INNER JOIN rvw_inv_inv rmvd_inv ON 
      rmvd.inv_no_db_id = rmvd_inv.inv_no_db_id AND 
      rmvd.inv_no_id    = rmvd_inv.inv_no_id
   INNER JOIN eqp_part_no eqp ON 
      rmvd.part_no_db_id = eqp.part_no_db_id AND 
      rmvd.part_no_id = eqp.part_no_id
   -- last install date
   LEFT JOIN (
               SELECT
                 inv_no_db_id,
                 inv_no_id,
                 MAX(event_dt) install_dt
               FROM 
                 sched_inst_part
                 INNER JOIN evt_event ON 
                    sched_inst_part.sched_db_id = evt_event.event_db_id AND
                    sched_inst_part.sched_id    = evt_event.event_id   
               GROUP BY
                  inv_no_db_id,
                  inv_no_id
              ) inv_last_install ON
      rmvd_inv.inv_no_db_id = inv_last_install.inv_no_db_id AND
      rmvd_inv.inv_no_id    = inv_last_install.inv_no_id 
   -- recent repair order
   LEFT JOIN rvw_repair_order recent_ro ON 
      rmvd.inv_no_db_id = recent_ro.main_inv_no_db_id AND
      rmvd.inv_no_id    = recent_ro.main_inv_no_id
      AND
      recent_ro.rn = 1
   -- last repair order
   LEFT JOIN rvw_repair_order last_ro ON 
      rmvd.inv_no_db_id = last_ro.main_inv_no_db_id AND
      rmvd.inv_no_id    = last_ro.main_inv_no_id
      AND
      last_ro.rn = 2
   -- installation
   LEFT JOIN sched_inst_part ON 
      rmvd.sched_db_id   = sched_inst_part.sched_db_id AND
      rmvd.sched_id      = sched_inst_part.sched_id AND      
      rmvd.sched_part_id = sched_inst_part.sched_part_id
   LEFT JOIN eqp_part_no ON 
      sched_inst_part.part_no_db_id = eqp_part_no.part_no_db_id AND
      sched_inst_part.part_no_id    = eqp_part_no.part_no_id
   -- po number
   LEFT JOIN evt_event po_event ON 
      rmvd_inv.po_db_id = po_event.event_db_id AND
      rmvd_inv.po_id    = po_event.event_id
   -- removal reason
   LEFT JOIN ref_remove_reason ref_rem ON 
      rmvd.remove_reason_db_id = ref_rem.remove_reason_db_id AND 
      rmvd.remove_reason_cd    = ref_rem.remove_reason_cd
   -- get completion date from evt_event
   INNER JOIN evt_event ON 
      rmvd.sched_db_id = evt_event.event_db_id AND 
      rmvd.sched_id    = evt_event.event_id
   -- removed from aircraft
   INNER JOIN evt_inv ON 
      evt_event.event_db_id = evt_inv.event_db_id AND 
      evt_event.event_id    = evt_inv.event_id
      AND
      evt_inv.main_inv_bool = 1
   INNER JOIN inv_ac_reg ON 
      evt_inv.assmbl_inv_no_db_id = inv_ac_reg.inv_no_db_id AND 
      evt_inv.assmbl_inv_no_id    = inv_ac_reg.inv_no_id
   -- aircraft pn, sn
   INNER JOIN rvw_inv_inv acft_inv ON 
      evt_inv.assmbl_inv_no_db_id = acft_inv.inv_no_db_id AND 
      evt_inv.assmbl_inv_no_id    = acft_inv.inv_no_id
   INNER JOIN eqp_part_no ac_part_no ON 
      acft_inv.part_no_db_id = ac_part_no.part_no_db_id AND
      acft_inv.part_no_id    = ac_part_no.part_no_id
   -- aircraft operator
   INNER JOIN org_carrier ON 
      acft_inv.carrier_db_id = org_carrier.carrier_db_id AND 
      acft_inv.carrier_id    = org_carrier.carrier_id
   -- hours at removal
   INNER JOIN rvw_inv_usage hours ON 
      hours.event_db_id  = evt_inv.event_db_id AND
      hours.event_id     = evt_inv.event_id AND 
      hours.event_inv_id = evt_inv.event_inv_id 
      AND 
      hours.data_type_db_id = 0 AND
      hours.data_type_id    = 1
   -- cycle at removal
   INNER JOIN rvw_inv_usage cycles ON 
      cycles.event_db_id  = evt_inv.event_db_id AND 
      cycles.event_id     = evt_inv.event_id AND 
      cycles.event_inv_id = evt_inv.event_inv_id 
      AND 
      cycles.data_type_db_id = 0 AND
      cycles.data_type_id    = 10
   -- ata chapter on which the task was performed
   INNER JOIN eqp_assmbl_bom eab ON 
      evt_inv.assmbl_db_id  = eab.assmbl_db_id AND 
      evt_inv.assmbl_cd     = eab.assmbl_cd AND 
      evt_inv.assmbl_bom_id = eab.assmbl_bom_id
   -- root inventory
   LEFT JOIN inv_inv root ON 
      evt_inv.h_inv_no_db_id = root.inv_no_db_id AND 
      evt_inv.h_inv_no_id    = root.inv_no_id
WHERE 
   -- complete removal
   evt_event.event_status_cd = 'COMPLETE';           