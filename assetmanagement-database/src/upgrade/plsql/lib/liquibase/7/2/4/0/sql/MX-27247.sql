--liquibase formatted sql


--changeSet MX-27247:1 stripComments:false
-- update description in table utl_action_config_parm for PARM_NAME='ACTION_ACTIVATE_REF_DOC'
UPDATE
   utl_action_config_parm
SET
   PARM_DESC  = 'Visibility control for the Disposition Reference Document button.'
WHERE
   PARM_NAME='ACTION_ACTIVATE_REF_DOC';   