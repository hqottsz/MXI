--liquibase formatted sql


--changeSet acor_fnc_trx_wp_ac_info_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_FNC_TRX_WP_AC_INFO_V1 AS
SELECT
   -- NOTE: Make sure this view includes all columns of acor_fnc_trx_wp_info_v1
   transaction_id,
   transaction_type,
   part_request_id,
   task_id,
   wp_id,
   work_package_number,
   aircraft_id,
   registration_code
FROM
   acor_fnc_trx_wp_info_v1
WHERE
   aircraft_id IS NOT NULL
;