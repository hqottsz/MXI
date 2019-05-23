--liquibase formatted sql


--changeSet OPER-9020:1 stripComments:false
/**************************************************************************************
* 
* OPER-9020 Update Spec2000 PO priority Map
*
***************************************************************************************/
UPDATE 
	utl_config_parm 
SET 
	parm_value = 'AOG-10,WSP-20,USR-40' 
WHERE 
	parm_name = 'SPEC2000_PO_PRIORITY_MAP'
AND 
	parm_type = 'LOGIC';