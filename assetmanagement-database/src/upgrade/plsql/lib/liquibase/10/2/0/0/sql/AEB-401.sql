--liquibase formatted sql
-- Add missing permissions to spec2k outbound permission set
-- Add alert permission to integration role 

--changeSet AEB-401:1 stripComments:false
-- delete API_UPDATE_PURCHASE_ORDER_REQUEST permission from the inbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_UPDATE_PURCHASE_ORDER_REQUEST';

--changeSet AEB-401:2 stripComments:false
-- delete API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST permission from the inbound set 
DELETE FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST';

--changeSet AEB-401:3 stripComments:false
-- and add API_UPDATE_PURCHASE_ORDER_REQUEST permission to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_UPDATE_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_UPDATE_PURCHASE_ORDER_REQUEST');

--changeSet AEB-401:4 stripComments:false
-- add API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST');

--changeSet AEB-401:5 stripComments:false
-- and add API_ORDER_EXCEPTION permission to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_ORDER_EXCEPTION', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_ORDER_EXCEPTION');

--changeSet AEB-401:6 stripComments:false
-- add API_MENU_REQUEST to the outbound set 
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT
'Spec 2000 PO Outbound Integrations', 'API_MENU_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Outbound Integrations' AND parm_name = 'API_MENU_REQUEST');

