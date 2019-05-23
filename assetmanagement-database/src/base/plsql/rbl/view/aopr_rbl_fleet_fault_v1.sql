--liquibase formatted sql


--changeSet aopr_rbl_fleet_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rbl_fleet_fault_v1
AS
SELECT
   operator_id,
   operator_code,
   fleet_type,
   aopr_rbl_fault_fleet_mv1.config_slot_code,
   config_slot_name,
   fault_source_code,
   year_code,
   month_code,
   year_month,
   m1_qt,
   m2_qt,
   m3_qt,
   m3_tot_qt,
   m6_tot_qt,
   m12_tot_qt,
   m1_avg_qt,
   m1_fc_avg_qt,
   m2_avg_qt,
   m2_fc_avg_qt,
   m3_avg_qt,
   m3_fc_avg_qt,
   m3_tot_avg_qt,
   m3_tot_fc_avg_qt,
   m6_tot_avg_qt,
   m6_tot_fc_avg_qt,
   m12_tot_avg_qt,
   m12_tot_fc_avg_qt,
   m12_m3to15_ucl_fh,
   m12_m3to15_ucl_fc
FROM
   aopr_rbl_fault_fleet_mv1
   INNER JOIN acor_2digit_ata_code_v1 ata_2digit ON
      aopr_rbl_fault_fleet_mv1.fleet_type       = ata_2digit.assembly_code AND
      aopr_rbl_fault_fleet_mv1.config_slot_code = ata_2digit.config_slot_code
;