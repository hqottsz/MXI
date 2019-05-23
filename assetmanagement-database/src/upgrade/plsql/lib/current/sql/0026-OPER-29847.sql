--liquibase formatted sql
-- Remove unnecessary permissions to spec2k inbound/outbound permission set

--changeSet OPER-29847:1 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_VENDOR_PARM';

--changeSet OPER-29847:2 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_PART_PARM';

--changeSet OPER-29847:3 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_ALERT_PARM';

--changeSet OPER-29847:4 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_GLOBAL_PARAMETER_REQUEST';

--changeSet OPER-29847:5 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_USER_ACCOUNT_REQUEST';

--changeSet OPER-29847:6 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_CURRENCY_CODE';

--changeSet OPER-29847:7 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_INVOICE_HISTORY_NOTE_REQUEST';

--changeSet OPER-29847:8 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_INVOICE_PARM';

--changeSet OPER-29847:9 stripComments:false
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_PURCHASE_ORDER_PARM';