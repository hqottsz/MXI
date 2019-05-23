--liquibase formatted sql


--changeSet aopr_comp_rmvl_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_COMP_RMVL_MV1');
END;
/

--changeSet aopr_comp_rmvl_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_comp_rmvl_mv1
BUILD DEFERRED
REFRESH FORCE ON DEMAND
AS
SELECT
   assmbl_cd,
   carrier_cd,
   ac_reg_cd,
   ac_pn,
   ac_sn,
   rmvd_pn,
   manufact_cd,
   serial_no_oem,
   rmd_partdescription,
   rmvl_dt,
   spec2k_remove_reason_cd,
   remove_reason_cd,
   root_name,
   root_sn,
   ata_chapter,
   ata_section,
   tsi,
   csi,
   last_install_date,
   ro_number,
   last_ro,
   pn_in,
   sn_in,
   fault.barcode AS fault_code,
   NVL(ROUND(rmvl_dt-last_install_date),0)  days_since_install,
   removal.po_number,
   fault.fault_description,
   fault_action.action_description,
   fault.deferral_class,
   fault.deferral_reference,
   fault.departure_airport,
   fault.arrival_airport,
   fault.flight_number,
   fault.mechanic,
   fault.inspector,
   workorder.work_package_number,
   workorder.work_package_name,
   fault.jic_code,
   last_received_condition,
   --
   fault_status_code
FROM
   acor_hist_comp_rmvl_v1 removal
   LEFT JOIN acor_logbook_fault_v1 fault ON
      removal.sched_id = fault.sched_id
   LEFT JOIN acor_fault_action_v1 fault_action ON
      fault.sched_id = fault_action.sched_id
      AND
      fault_action.cancel_flag = 0
   LEFT JOIN acor_acft_wp_v1 workorder ON
      fault.workpackage_id = workorder.sched_id;