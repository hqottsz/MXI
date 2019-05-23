--liquibase formatted sql


--changeSet MX-27269:1 stripComments:false
-- update description in table utl_action_config_parm for PARM_NAME='ACTION_HIST_ADD_CUST_PART_REQUIREMENT'
UPDATE
   utl_action_config_parm
SET
   PARM_DESC  = 'Permission to add historic custom part requirements in Web Maintenix.'
WHERE
   PARM_NAME='ACTION_HIST_ADD_CUST_PART_REQUIREMENT' ;   