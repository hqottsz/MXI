--liquibase formatted sql


--changeSet aopr_operator_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_operator_v1
AS
SELECT
   operator_id,
   iata_code,
   icao_code,
   callsign_description,
   external_control_flag,
   organization_sub_type_code,
   operator_code,
   rstat_code
FROM
   acor_operator_v1
;