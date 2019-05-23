--liquibase formatted sql


--changeSet 0utl_perm_set_action_parm:1 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_ROLE_EDIT', 'API_ROLE_UPDATE_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:2 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_ROLE_VIEW', 'API_ROLE_VIEW_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:3 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_ROLE_VIEW', 'API_PERM_SET_VIEW_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:4 stripComments:false
-- OPER-5618
-- Add the action parms to the permission set.
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'ACTION_ADD_PART_REQUIREMENT', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:5 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'ACTION_SELECT_ALTERNATE_PART', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:6 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_PART_DEFINITION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:7 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_AIRCRAFT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:8 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_PART_REQUEST_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:9 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'ACTION_GETINVENTORY', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:10 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_WORK_PACKAGE_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:11 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_TASK_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:12 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'API_LOCATION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet OPER-10642:1 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('UTL_PART_SEARCH', 'ACTION_ADD_PART_REQUIREMENT_SEARCH', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:16 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'API_USER_ACCOUNT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:21 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'API_UPDATE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:22 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'API_DELETE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet AEB-401:3 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'API_ORDER_EXCEPTION', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet AEB-401:4 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Outbound Integrations', 'API_MENU_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:23 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'API_AEROEXCHANGE_REJECTIONS', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:24 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'API_CREATE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:25 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'API_SHIPMENT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet 0utl_perm_set_action_parm:26 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'API_ACKNOWLEDGE_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');

--changeSet AEB-159:1 stripComments:false
INSERT INTO utl_perm_set_action_parm(perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
VALUES('Spec 2000 PO Inbound Integrations', 'API_AEROBUY_ATA_SPARES_INVOICE', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI');




