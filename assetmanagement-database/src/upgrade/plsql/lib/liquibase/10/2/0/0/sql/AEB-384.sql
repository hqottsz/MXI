--liquibase formatted sql
-- Add missing permissions to spec2k outbound permission set
-- Add alert permission to integration role 

--changeSet AEB-384:1 stripComments:false
-- delete API_HISTORY_NOTE_FOR_ORDER_REQUEST permission from the inbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_HISTORY_NOTE_FOR_ORDER_REQUEST';

--changeSet AEB-384:2 stripComments:false
-- and add API_HISTORY_NOTE_FOR_ORDER_REQUEST permission to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_HISTORY_NOTE_FOR_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_HISTORY_NOTE_FOR_ORDER_REQUEST');

--changeSet AEB-384:3 stripComments:false
-- add API_RAISE_ALERT_REQUEST to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_RAISE_ALERT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_RAISE_ALERT_REQUEST');

--changeSet AEB-384:4 stripComments:false
-- assign API_RAISE_ALERT_REQUEST to the integration role 
INSERT INTO utl_action_role_parm( parm_name, role_id, parm_value, session_auth_bool, utl_id)
SELECT
'API_RAISE_ALERT_REQUEST', '17001', 'true', 0, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_action_role_parm where parm_name = 'API_RAISE_ALERT_REQUEST' AND role_id = '17001');