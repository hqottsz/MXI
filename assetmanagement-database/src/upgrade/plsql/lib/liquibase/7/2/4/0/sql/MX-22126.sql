--liquibase formatted sql


--changeSet MX-22126:1 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET PARM_DESC='This parameter is used to determine whether activating task definition is allowed before approval.'
   WHERE PARM_NAME = 'ALLOW_ACTIVATING_TD_BEFORE_APPROVAL' AND PARM_TYPE = 'LOGIC';  	