--liquibase formatted sql


--changeSet QC-4639:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sReceiveShipmentManufactDtValidation', 'SESSION', 'Enforce manufactured mate validation for receive shipment in maintenance receipt', 'USER', '', '', 0, 'Core Logic', '7.1-SP1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sReceiveShipmentManufactDtValidation' );    