--liquibase formatted sql


--changeSet MX-17639:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'SPEC2000_ENFORCE_PART_NO_COMPLIANCE', 'FALSE', 'LOGIC', 0,
      'If true, part numbers must be SPEC2000 compliant.',
      'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '7.5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'SPEC2000_ENFORCE_PART_NO_COMPLIANCE' );

--changeSet MX-17639:2 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'SPEC2000_ENFORCE_PART_NO_COMPLIANCE';