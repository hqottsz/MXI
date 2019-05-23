--liquibase formatted sql


--changeSet OPER-4193_2:1 stripComments:false
/***************************************************************
* Update new configuration parameter for determining the overage of a shelf life percentage remaining
****************************************************************/
UPDATE utl_config_parm 
SET parm_value = '100'
WHERE parm_name = 'PERCENT_SHELF_LIFE_OVERAGE_WARNING';