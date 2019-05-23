--liquibase formatted sql


--changeSet SWA-311:1 stripComments:false
/**************************************************************************************
* 
* SWA-311 Update Spec2000 PO priority Map
*
***************************************************************************************/
UPDATE 
	utl_config_parm 
SET 
	parm_value = 'AOG-10,WSP-20,USR-40,RTN-100' 
WHERE 
	parm_name = 'SPEC2000_PO_PRIORITY_MAP'
AND 
	parm_type = 'LOGIC';