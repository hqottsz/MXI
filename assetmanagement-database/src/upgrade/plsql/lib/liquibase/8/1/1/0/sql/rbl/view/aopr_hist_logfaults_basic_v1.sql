--liquibase formatted sql


--changeSet aopr_hist_logfaults_basic_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_hist_logfaults_basic_v1
AS
SELECT
   fleet,
   operator,
   ac_reg_code,
   ac_pn,
   ac_sn,
   logbook_ref,
   fault_description,
   ata_chapter,
   ata_section,
   found_on_date,
   fault_type,
   fault_source,
   deferral_class,
   closed_date,
   corrective_action,
   deferral_ref,
   flight_number,
   departure_airport,
   arrival_airport,
   fault_code, 
   jic_code, 
   jic_name, 
   req_code, 
   req_name, 
   block_code, 
   block_name, 
   removal_code, 
   work_order, 
   doc_reference, 
   mechanic, 
   certifier, 
   inspector,
   zone_list,
   --
   'CFCERT' as fault_status_code,
   'Legacy' as sourcesystem   
FROM 
   opr_rbl_hist_acft_fault
UNION ALL
SELECT 
   assmbly_code,
   operator_code,
   registration_code,
   part_number,
   oem_serial_number,
   logbook_reference,
   fault_description,
   ata_chapter,
   ata_section,
   found_on_date,
   spec2k_fault_source,
   fault_source,
   deferral_class,
   closed_date,
   action_description,
   deferral_reference,
   flight_number,
   departure_airport,
   arrival_airport,   
   fault_code, 
   jic_code, 
   jic_name, 
   req_code, 
   req_name, 
   block_code, 
   block_name, 
   removal_code, 
   work_order, 
   doc_reference, 
   mechanic, 
   certifier, 
   inspector,
   zone_list,
   --
   fault_status_code,
   'Maintenix' AS SourceSystem   
FROM 
   aopr_logbook_fault_mv1;            