--liquibase formatted sql
-- Add missing permissions to spec2k inbound permission set

--changeSet AEB-402:1 stripComments:false
-- and add API_RAISE_ALERT_REQUEST permission to the inbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Inbound Integrations', 'API_RAISE_ALERT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_RAISE_ALERT_REQUEST');

--changeSet AEB-402:2 stripComments:false
-- and add API_USER_ACCOUNT_REQUEST permission to the inbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Inbound Integrations', 'API_USER_ACCOUNT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_USER_ACCOUNT_REQUEST');

--changeSet AEB-402:3 stripComments:false
-- and add API_GLOBAL_PARAMETER_REQUEST permission to the inbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Inbound Integrations', 'API_GLOBAL_PARAMETER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_GLOBAL_PARAMETER_REQUEST');

--changeSet AEB-402:4 stripComments:false
-- and add API_PURCHASE_ORDER_REQUEST permission to the inbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Inbound Integrations', 'API_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_PURCHASE_ORDER_REQUEST');

--changeSet AEB-402:5 stripComments:false
-- and add API_VENDOR_REQUEST permission to the inbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Inbound Integrations', 'API_VENDOR_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_VENDOR_REQUEST');