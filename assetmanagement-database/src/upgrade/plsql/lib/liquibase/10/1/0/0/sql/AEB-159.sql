--liquibase formatted sql
-- Update the descriptions of the Spec 2000 permission sets
-- Add the Spec 2000 Po Outbound Integrations permission set to the SPEC2K role
-- Add permissions to the Spec 2000 PO Inbound Integrations 
-- Add permissions to the Spec 2000 PO Outbound Integrations 

--changeSet AEB-159:1 stripComments:false
UPDATE 
   utl_perm_set 
SET 
   description = 'Permissions required to receive and process inbound Spec 2000 messages for the AeroBuy Parts Purchasing feature'
WHERE 
   id = 'Spec 2000 PO Inbound Integrations'; 

--changeSet AEB-159:2 stripComments:false
UPDATE 
   utl_perm_set 
SET 
   description = 'Permissions required to generate and send Spec 2000 messages for the AeroBuy Parts Purchasing feature'
WHERE 
   id = 'Spec 2000 PO Outbound Integrations'; 


--changeSet AEB-159:3 stripComments:false
INSERT INTO utl_role_perm_set(role_id, perm_set_id, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
17000, 'Spec 2000 PO Outbound Integrations', 0, 1, 0, sysdate, 0, sysdate, 0,'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_role_perm_set WHERE role_id = '17000' AND perm_set_id = 'Spec 2000 PO Outbound Integrations');
   

--changeSet AEB-159:4 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_UPDATE_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_UPDATE_PURCHASE_ORDER_REQUEST');

--changeSet AEB-159:5 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_SHIPMENT_SEND_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_SHIPMENT_SEND_REQUEST');

--changeSet AEB-159:6 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_SHIPMENT_UPDATE_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_SHIPMENT_UPDATE_REQUEST');

--changeSet AEB-159:7 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_AEROBUY_ATA_SPARES_INVOICE', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_AEROBUY_ATA_SPARES_INVOICE');

--changeSet AEB-159:8 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_CURRENCY_CODE', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_CURRENCY_CODE');

--changeSet AEB-159:9 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_INVOICE_HISTORY_NOTE_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_INVOICE_HISTORY_NOTE_REQUEST');

--changeSet AEB-159:10 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_ALERT_PARM', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_ALERT_PARM');

--changeSet AEB-159:11 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'WRITE_INVOICE', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'WRITE_INVOICE');

--changeSet AEB-159:12 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'READ_INVOICE', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'READ_INVOICE');

--changeSet AEB-159:13 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_PART_PARM', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_PART_PARM');

--changeSet AEB-159:14 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_VENDOR_PARM', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_VENDOR_PARM');

--changeSet AEB-159:15 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'READ_PURCHASE_ORDER', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'READ_PURCHASE_ORDER');

--changeSet AEB-159:16 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
SELECT 
'Spec 2000 PO Inbound Integrations', 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id = 'Spec 2000 PO Inbound Integrations' AND parm_name = 'API_CREATE_HISTORY_NOTE_FOR_ORDER_REQUEST');