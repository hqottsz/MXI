--liquibase formatted sql


--changeSet MX-17959:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_MOVE_SHIPMENT_LINE_INVENTORY', 'SECURED_RESOURCE','Permission to move shipment line inventory to the dock.','USER', 'TRUE/FALSE', 'FALSE', 1,'Supply - Shipments', '6.8.12',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_MOVE_SHIPMENT_LINE_INVENTORY' );          