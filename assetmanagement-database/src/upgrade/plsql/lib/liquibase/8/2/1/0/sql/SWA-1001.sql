--liquibase formatted sql


--changeSet SWA-1001:1 stripComments:false
-- A config parm to configure the polling interval for  Order Exceptions Indicator
INSERT INTO
   utl_config_parm
   (
      parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
   SELECT
      'SPEC2000_AEROBUY_ORDER_EXCEPTIONS_INDICATOR_POLLING', 'LOGIC', '5000', 0, 'Determines the refresh frequency (ms) for the Order Exceptions Indicator.', 'GLOBAL', 'NUMBER', '5000', 1, 'Integration', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'SPEC2000_AEROBUY_ORDER_EXCEPTIONS_INDICATOR_POLLING' AND parm_type = 'LOGIC');