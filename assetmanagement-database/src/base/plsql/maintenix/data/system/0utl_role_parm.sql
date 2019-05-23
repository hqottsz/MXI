--liquibase formatted sql


--changeSet 0utl_role_parm:1 stripComments:false
-- Administration
/****************************************** 
** 0-Level INSERT SCRIPT FOR UTL_ROLE_PARM
*******************************************/
INSERT INTO utl_role_parm (parm_name, parm_type, role_id, parm_value, utl_id) 
VALUES ('WELCOME_PAGE', 'MXCOMMONWEB', 19000, '/maintenix/common/ToDoList.jsp',0);

--changeSet 0utl_role_parm:2 stripComments:false
-- ensure that all of the SECURED_RESOURCES will get added to the Administation role
-- This insert statement is an anomoly.
INSERT INTO utl_role_parm ( parm_name, role_id, parm_value, parm_type, utl_id)
   (
      SELECT
      parm_name,
      19000 AS role_id,
      'true' as parm_value,
      parm_type,
      0
   FROM
      utl_config_parm
   WHERE
      parm_type = 'SECURED_RESOURCE'
   );