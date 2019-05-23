--liquibase formatted sql


--changeSet aopr_rbl_acft_eng_type_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rbl_acft_eng_type_v1
AS
SELECT
   operator_id,
   operator_code,
   fleet_type,
   acft_serial_number,
   assmbl_code   AS engine_type
FROM 
   acor_rbl_acft_assy_type_v1
WHERE
   assmbl_class_code= 'ENG';