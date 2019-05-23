--liquibase formatted sql


--changeSet OPER-8391:1 stripComments:false
-- An action config parm to add permission for api API_HISTORY_NOTE_FOR_ORDER_REQUEST and added to permission set
INSERT INTO
   utl_action_config_parm
   (
       parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
   SELECT
       'API_HISTORY_NOTE_FOR_ORDER_REQUEST', 'FALSE',  0, 'Permission to allow API history note for order request' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'API_HISTORY_NOTE_FOR_ORDER_REQUEST');

--changeSet OPER-8391:2 stripComments:false
INSERT INTO
  utl_perm_set_action_parm
  (
    perm_set_id, parm_name, parm_value, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user
  )
  SELECT
    'Spec 2000 PO Inbound Integrations', 'API_HISTORY_NOTE_FOR_ORDER_REQUEST', 'true', 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
  FROM
    dual
  WHERE
    NOT EXISTS (SELECT 1 FROM utl_perm_set_action_parm WHERE perm_set_id='Spec 2000 PO Inbound Integrations' AND parm_name='API_HISTORY_NOTE_FOR_ORDER_REQUEST' );