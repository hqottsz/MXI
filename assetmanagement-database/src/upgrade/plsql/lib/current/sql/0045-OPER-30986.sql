--liquibase formatted sql
-- Remove Aerobuy related Core API Permissions from the Permission sets

--changeSet OPER-30986:1 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_PURCHASE_ORDER_REQUEST';

--changeSet OPER-30986:2 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_VENDOR_REQUEST';

--changeSet OPER-30986:3 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_PART_REQUEST_REQUEST';

--changeSet OPER-30986:4 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_AIRCRAFT_REQUEST';

--changeSet OPER-30986:5 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_PRIORITY_REQUEST';

--changeSet OPER-30986:6 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_PART_DEFINITION_REQUEST';

--changeSet OPER-30986:7 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_HISTORY_NOTE_FOR_ORDER_REQUEST';

--changeSet OPER-30986:8 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_RAISE_ALERT_REQUEST';

--changeSet OPER-30986:9 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_UPDATE_PURCHASE_ORDER_REQUEST';

--changeSet OPER-30986:10 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Outbound Integrations' AND 
	parm_name = 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST';

--changeSet OPER-30986:11 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_SHIPMENT_SEND_REQUEST';

--changeSet OPER-30986:12 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_SHIPMENT_UPDATE_REQUEST';

--changeSet OPER-30986:13 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_RAISE_ALERT_REQUEST';

--changeSet OPER-30986:14 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_GLOBAL_PARAMETER_REQUEST';

--changeSet OPER-30986:15 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_PURCHASE_ORDER_REQUEST';

--changeSet OPER-30986:16 stripComments:false
DELETE FROM 
	utl_perm_set_action_parm 
WHERE 
	perm_set_id = 'Spec 2000 PO Inbound Integrations' AND 
	parm_name = 'API_VENDOR_REQUEST';