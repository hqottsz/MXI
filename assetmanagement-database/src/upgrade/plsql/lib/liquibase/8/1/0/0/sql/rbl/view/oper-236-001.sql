--liquibase formatted sql


--changeSet oper-236-001:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rbl_fault_v1
AS 
SELECT
   operator_id,
   operator_code,
   fleet_type          AS fleet_code,
   tail_number         AS registration_code,
   opr_rbl_fault_summary.config_slot_code,
   config_slot_name,
   fault_source_code,
   year_code,
   month_code,
   ym_1mon             AS year_month,
   m1_qt,
   m2_qt,
   m3_qt,
   m3_tot_qt,
   m6_tot_qt,
   m12_tot_qt,
   CASE
     WHEN m1_fh_qt > 0 THEN
        ROUND((m1_qt*1000)/m1_fh_qt,2)
     ELSE
       0
   END          AS m1_avg_qt,
   CASE
     WHEN m1_fc_qt > 0 THEN
        ROUND((m1_qt*100)/m1_fc_qt,2)
     ELSE
       0
   END          AS m1_fc_avg_qt,
   CASE
     WHEN m2_fh_qt > 0 THEN
        ROUND((m2_qt*1000)/m2_fh_qt,2)
     ELSE
       0
   END          AS m2_avg_qt,
   CASE
     WHEN m2_fc_qt > 0 THEN
        ROUND((m2_qt*100)/m2_fc_qt,2)
     ELSE
       0
   END          AS m2_fc_avg_qt,
   CASE
     WHEN m3_fh_qt > 0 THEN
        ROUND((m3_qt*1000)/m3_fh_qt,2)
     ELSE
       0
   END          AS m3_avg_qt,
   CASE
     WHEN m3_fc_qt > 0 THEN
        ROUND((m3_qt*100)/m3_fc_qt,2)
     ELSE
       0
   END          AS m3_fc_avg_qt,
   CASE
     WHEN m3_fh_tot_qt > 0 THEN
        ROUND((m3_tot_qt*1000)/m3_fh_tot_qt,2)
     ELSE
       0
   END          AS m3_tot_avg_qt,
   CASE
     WHEN m3_fc_tot_qt > 0 THEN
        ROUND((m3_tot_qt*100)/m3_fc_tot_qt,2)
     ELSE
       0
   END          AS m3_tot_fc_avg_qt,
   CASE
     WHEN m6_fh_tot_qt > 0 THEN
        ROUND((m6_tot_qt*1000)/m6_fh_tot_qt,2)
     ELSE
       0
   END          AS m6_tot_avg_qt,
   CASE
     WHEN m6_fc_tot_qt > 0 THEN
        ROUND((m6_tot_qt*100)/m6_fc_tot_qt,2)
     ELSE
       0
   END          AS m6_tot_fc_avg_qt,
   CASE
     WHEN m12_fh_tot_qt > 0 THEN
        ROUND((m12_tot_qt*1000)/m12_fh_tot_qt,2)
     ELSE
       0
   END          AS m12_tot_avg_qt,
   CASE
     WHEN m12_fc_tot_qt > 0 THEN
        ROUND((m12_tot_qt*100)/m12_fc_tot_qt,2)
     ELSE
       0
   END          AS m12_tot_fc_avg_qt
FROM
   opr_rbl_fault_summary
   INNER JOIN aopr_2digit_ata_code_v1 ata_2digit ON
      opr_rbl_fault_summary.fleet_type       = ata_2digit.assembly_code AND
      opr_rbl_fault_summary.config_slot_code = ata_2digit.config_slot_code
ORDER BY
   tail_number,
   config_slot_code
;