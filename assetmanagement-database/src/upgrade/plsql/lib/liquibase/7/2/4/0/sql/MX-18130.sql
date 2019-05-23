--liquibase formatted sql


--changeSet MX-18130:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, 
      ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, 
      DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 
      'false', 'ACTION_ALLOW_MEL_DEFERRAL_START_EDITING', 'SECURED_RESOURCE', 
      0, 'Permission to edit the deferral start fields when raising/deferring/editing a MEL corrective task in WebMaintenix.', 'USER', 'TRUE/FALSE', 
      'FALSE', 1, 'Maint - Tasks', '7.5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ALLOW_MEL_DEFERRAL_START_EDITING' );

--changeSet MX-18130:2 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE parm_name = 'ACTION_ALLOW_MEL_DEFERRAL_START_EDITING';