--liquibase formatted sql


--changeSet MX-19797:1 stripComments:false
UPDATE
   utl_action_config_parm
SET
   category = 'Maint Program - Reference Documents',
   modified_in = '8.0'
WHERE
   parm_name IN ('ACTION_REF_DOC_LOCK', 'ACTION_REF_DOC_UNLOCK')
   AND
   category = 'Maint Program - Requirements'
;