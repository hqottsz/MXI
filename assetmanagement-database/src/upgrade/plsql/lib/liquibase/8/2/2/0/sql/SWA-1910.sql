--liquibase formatted sql


--changeSet SWA-1910:1 stripComments:false
/**************************************************************************************
* 
* SWA-1910 Update Utl_perm-set to fix incorrect naming
*
***************************************************************************************/
UPDATE 
	utl_perm_set
SET
	category = 'Spec 2000', label = 'Receive Spec 2000 Message'
WHERE
	id = 'Spec 2000 PO Inbound Integrations';		

--changeSet SWA-1910:2 stripComments:false
UPDATE 
	utl_perm_set
SET
	category = 'Spec 2000', label = 'Send Spec 2000 Message'
WHERE
	id = 'Spec 2000 PO Outbound Integrations';