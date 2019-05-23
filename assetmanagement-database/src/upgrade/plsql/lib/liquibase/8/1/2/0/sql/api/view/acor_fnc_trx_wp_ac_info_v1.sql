--liquibase formatted sql


--changeSet acor_fnc_trx_wp_ac_info_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_FNC_TRX_WP_AC_INFO_V1 AS
SELECT
   -- Use '*' here because we want to get all columns from acor_fnc_trx_wp_info_v1 view.
   -- If the column is changed in acor_fnc_trx_wp_info_v1, we don't need to change here.
   -- In this case, we keep these two views consistent.
   *
FROM
   acor_fnc_trx_wp_info_v1
WHERE
   aircraft_id IS NOT NULL
;