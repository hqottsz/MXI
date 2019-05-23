--liquibase formatted sql
	  

--changeSet SWA-367:1 stripComments:false
-- A config parm to determine to add additional wrapper to outbound message
INSERT INTO 
   utl_config_parm 
   (
      parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id 
   )
   SELECT 
      'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER', 'LOGIC', 'FALSE', 0, 'Determines whether to add additional wrapper to outbound message.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Integration Message', '8.2-SP2', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER' AND parm_type = 'LOGIC');