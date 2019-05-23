--liquibase formatted sql


--changeSet MTX-925:1 stripComments:false
UPDATE utl_config_parm SET
   parm_value = (SELECT owner_cd FROM inv_owner WHERE default_bool = 1)
WHERE
   parm_name = 'ARC_DEFAULT_OWNER';