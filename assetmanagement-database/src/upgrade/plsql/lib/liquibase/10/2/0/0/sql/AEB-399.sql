--liquibase formatted sql
-- Remove unnecessary permissions to spec2k outbound permission set

--changeSet AEB-399:1 stripComments:false
-- delete API_SHIPMENT_REQUEST permission from the outbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_SHIPMENT_REQUEST';

--changeSet AEB-399:2 stripComments:false
-- delete ACTION_GETINVENTORY permission from the outbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'ACTION_GETINVENTORY';

--changeSet AEB-399:3 stripComments:false
-- delete API_ORDER_REQUEST permission from the outbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_ORDER_REQUEST';