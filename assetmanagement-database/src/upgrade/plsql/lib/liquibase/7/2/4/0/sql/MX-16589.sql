--liquibase formatted sql


--changeSet MX-16589:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'PREVENT_SYNCH_ON_INV_CREATED_AS_INSPREQ', 'TRUE', 'LOGIC', 0, 'TRUE-Inventory creation logic will mark new inventory that are created as INSPREQ as Prevent Sync.FALSE-Inventory creation logic will not mark new inventory that are created as INSPREQ as Prevent Sync.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '0812', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'PREVENT_SYNCH_ON_INV_CREATED_AS_INSPREQ' );          