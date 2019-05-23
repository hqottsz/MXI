--liquibase formatted sql


--changeSet SWA-1658:1 stripComments:false
-- A config parm to configure a proceesing action for ACN in ATA_SparesOrder SPEC2000 message.
INSERT INTO
   utl_config_parm
   (
      parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
   SELECT
       'SPEC2000_ACN_PROCESSING', 'INTEGRATION', 'NONE', 0, 'Determines a proceesing action for ACN in ATA_SparesOrder SPEC2000 message.', 'GLOBAL', 'LEFT_5_ALPHA_NUMERIC_CHARS - Uses 5 alphanum chars from left and removes other chars like dashes, RIGHT_5_ALPHA_NUMERIC_CHARS - Uses 5 alphanum chars from right and removes other chars like dashes, NUMERIC_CHARS - Uses only numeric chars, NONE - Does not change the ACN', 'NONE', 1, 'Integration', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'SPEC2000_ACN_PROCESSING' AND parm_type = 'INTEGRATION');