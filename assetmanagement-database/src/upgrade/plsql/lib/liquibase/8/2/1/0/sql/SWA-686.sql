--liquibase formatted sql


--changeSet SWA-686:1 stripComments:false
/**************************************************************************************
* 
* SWA-686: Insert scripts for the permissions, permission sets and new role Spec2k
*
***************************************************************************************/
INSERT INTO 
	utl_user 
	(
		USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
	)
  SELECT
  	17, 'mxspec2000pointegration', 'password', 'mxspec2000pointegration'   , NULL, 'mxspec2000pointegration', NULL, NULL, 0, 0, 0, 0, to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_user WHERE USER_ID = 17 );	

--changeSet SWA-686:2 stripComments:false
INSERT INTO 
	utl_role 
	(
		ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID
	)
  SELECT
  	17000, 'SPEC2K', 'Spec 2000 Integration', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_role WHERE ROLE_ID = 17000 AND UTL_ID = 0 );	

--changeSet SWA-686:3 stripComments:false
INSERT INTO 
	utl_user_role 
	(
		USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
	)
  SELECT
  	17,17000, 1, 0, 0, to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_user_role WHERE ROLE_ID = 17000 AND USER_ID = 17 );		

--changeSet SWA-686:4 stripComments:false
INSERT INTO 
	utl_perm_set
	(
		id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'Spec 2000', 'Send Spec 2000 Message', 'Permissions required to generate and send Spec 2000 messages for Purchase Orders', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set WHERE ID = 'Spec 2000 PO Outbound Integrations');	

--changeSet SWA-686:5 stripComments:false
INSERT INTO 
	utl_perm_set
	(
		id, category, label, description, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Inbound Integrations', 'Spec 2000', 'Receive Spec 2000 Message', 'Permissions required to call inbound Spec 2000 APIs for Purchase Orders', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set WHERE ID = 'Spec 2000 PO Inbound Integrations');

--changeSet SWA-686:6 stripComments:false
INSERT INTO 
	utl_role_perm_set
	(
		role_id, perm_set_id, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	17000, 'Spec 2000 PO Inbound Integrations', 0, 1, 0, sysdate, 0, sysdate, 0,'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_role_perm_set WHERE PERM_SET_ID = 'Spec 2000 PO Inbound Integrations' AND ROLE_ID = 17000);	

--changeSet SWA-686:7 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'ACTION_GETINVENTORY', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'ACTION_GETINVENTORY');

--changeSet SWA-686:8 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_PURCHASE_ORDER_REQUEST');	

--changeSet SWA-686:9 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_VENDOR_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_VENDOR_REQUEST');	

--changeSet SWA-686:10 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_PART_REQUEST_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_PART_REQUEST_REQUEST');	

--changeSet SWA-686:11 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_USER_ACCOUNT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_USER_ACCOUNT_REQUEST');	

--changeSet SWA-686:12 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_AIRCRAFT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_AIRCRAFT_REQUEST');	

--changeSet SWA-686:13 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_PRIORITY_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_PRIORITY_REQUEST');	

--changeSet SWA-686:14 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_ORDER_REQUEST');	

--changeSet SWA-686:15 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_PART_DEFINITION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_PART_DEFINITION_REQUEST');	

--changeSet SWA-686:16 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_SHIPMENT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_SHIPMENT_REQUEST');	

--changeSet SWA-686:17 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_GLOBAL_PARAMETER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_GLOBAL_PARAMETER_REQUEST');	

--changeSet SWA-686:18 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Inbound Integrations', 'API_AEROEXCHANGE_REJECTIONS', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Inbound Integrations' AND PARM_NAME = 'API_AEROEXCHANGE_REJECTIONS');	

--changeSet SWA-686:19 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Inbound Integrations', 'API_CREATE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Inbound Integrations' AND PARM_NAME = 'API_CREATE_ORDER_EXCEPTION_REQUEST');	

--changeSet SWA-686:20 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Inbound Integrations', 'API_SHIPMENT_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Inbound Integrations' AND PARM_NAME = 'API_SHIPMENT_REQUEST');	

--changeSet SWA-686:21 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Inbound Integrations', 'API_ACKNOWLEDGE_PURCHASE_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Inbound Integrations' AND PARM_NAME = 'API_ACKNOWLEDGE_PURCHASE_ORDER_REQUEST');

--changeSet SWA-686:22 stripComments:false
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_UPDATE_ORDER_EXCEPTION_REQUEST', 'FALSE',  0, 'Permission to update an Order Exception' , 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_UPDATE_ORDER_EXCEPTION_REQUEST' );		

--changeSet SWA-686:23 stripComments:false
INSERT INTO 
	utl_action_config_parm 
	(
		parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
	)
  SELECT
  	'API_DELETE_ORDER_EXCEPTION_REQUEST', 'FALSE',  0, 'Permission to delete an Order Exception' , 'TRUE/FALSE', 'FALSE', 1, 'API - PROCUREMENT', '8.2-SP2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_DELETE_ORDER_EXCEPTION_REQUEST' );		

--changeSet SWA-686:24 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_UPDATE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_UPDATE_ORDER_EXCEPTION_REQUEST');	

--changeSet SWA-686:25 stripComments:false
INSERT INTO 
	utl_perm_set_action_parm
	(
		perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
	)
  SELECT
  	'Spec 2000 PO Outbound Integrations', 'API_DELETE_ORDER_EXCEPTION_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_perm_set_action_parm WHERE PERM_SET_ID = 'Spec 2000 PO Outbound Integrations' AND PARM_NAME = 'API_DELETE_ORDER_EXCEPTION_REQUEST');			