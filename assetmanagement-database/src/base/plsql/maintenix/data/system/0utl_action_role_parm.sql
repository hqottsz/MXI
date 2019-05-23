--liquibase formatted sql


--changeSet 0utl_action_role_parm:1 stripComments:false
-- ensure that all of the action parms will get added to the Administation role
-- This insert statement is an anomoly.
--
-- This script is based on 0utl_role_parm.sql.
-- All the action config parms are, by nature, SECURED_RESOURCE.
-- (created when the action config parms were moved into the new utl_action_role_parm)
/*************************************************
** 0-Level INSERT SCRIPT FOR UTL_ACTION_ROLE_PARM
**************************************************/
INSERT INTO utl_action_role_parm ( parm_name, role_id, parm_value, session_auth_bool, utl_id)
   (
      SELECT
         parm_name,
         19000 as role_id,
         'true' as parm_value,
         0 as session_auth_bool,
         0 as utl_id
      FROM
         utl_action_config_parm
   );
   
--changeSet 0utl_action_role_parm:2 stripComments:false
INSERT INTO utl_action_role_parm( parm_name, role_id, parm_value, session_auth_bool, utl_id)
VALUES ( 'API_PUBSUB_PARM', '17001', 'true', 0, 0);
                          
--changeSet AEB-384:1 stripComments:false
INSERT INTO utl_action_role_parm( parm_name, role_id, parm_value, session_auth_bool, utl_id)
VALUES ( 'API_RAISE_ALERT_REQUEST', '17001', 'true', 0, 0);
