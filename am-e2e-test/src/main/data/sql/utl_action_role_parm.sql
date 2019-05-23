/*************************************************
** INSERT AND UPDATE SCRIPT FOR UTL_ACTION_ROLE_PARM
**************************************************/

-- ADMIN Role

INSERT INTO utl_action_role_parm
  (parm_name, role_id, parm_value, session_auth_bool, utl_id)
  (SELECT parm_name,
          19000 as role_id,
          'true' as parm_value,
          0 as session_auth_bool,
          0 as utl_id
     FROM utl_action_config_parm
    WHERE parm_name not in (SELECT t.parm_name
                              FROM utl_action_role_parm t
                             WHERE role_id = 19000));

-- Engineering Role

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_SHOW_ASSEMBLY_SENSITIVITIES_TAB', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_SHOW_ASSEMBLY_SENSITIVITIES_TAB');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ASSIGN_ASSEMBLY_SENSITIVITIES', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ASSIGN_ASSEMBLY_SENSITIVITIES');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_SHOW_CAPABILITIES_TAB', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_SHOW_CAPABILITIES_TAB');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ASSIGN_CAPABILITIES', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ASSIGN_CAPABILITIES');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_REMOVE_CAPABILITIES', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_REMOVE_CAPABILITIES');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_EDIT_CONFIGURED_CAPABILITY_LEVEL', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_EDIT_CONFIGURED_CAPABILITY_LEVEL');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_CREATE_CONFIG_SLOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_CREATE_CONFIG_SLOT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_EDIT_CONFIG_SLOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_EDIT_CONFIG_SLOT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_UNASSIGN_TEMPORARY_ROLE', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_UNASSIGN_TEMPORARY_ROLE');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_CREATE_REF_DOC_REVISION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_CREATE_REF_DOC_REVISION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ACTIVATE_MAINT_PROGRAM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ACTIVATE_MAINT_PROGRAM');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ACTIVATE_BLOCK', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ACTIVATE_BLOCK');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ACTIVATE_REQ', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ACTIVATE_REQ');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ACTIVATE_JIC', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ACTIVATE_JIC');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_ACTIVATE_REF_DOC', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_ACTIVATE_REF_DOC');

INSERT INTO
  utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
  10009, 'ACTION_CREATE_REQ', 'true', 10
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_CREATE_REQ');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_CREATE_USAGE_DEFINITION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_CREATE_USAGE_DEFINITION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_DELETE_USAGE_DEFINITION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_DELETE_USAGE_DEFINITION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_CREATE_REQ_REVISION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_CREATE_REQ_REVISION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_PERFORM_CORRECTIVE_ACTIONS', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_PERFORM_CORRECTIVE_ACTIONS');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'API_ACCESS_FOLLOW_ON_REQUIREMENT_DEFINITION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'API_ACCESS_FOLLOW_ON_REQUIREMENT_DEFINITION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_INSPECT_TASK', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_INSPECT_TASK');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10009, 'ACTION_INSPECT_TASK_AFTER_TASK_COMPLETE', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10009 AND parm_name = 'ACTION_INSPECT_TASK_AFTER_TASK_COMPLETE');   
   
UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10009 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Maintenance Controller Role
INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'ACTION_SHOW_CAPABILITIES_TAB', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'ACTION_SHOW_CAPABILITIES_TAB');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'ACTION_ASSIGN_CAPABILITIES', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'ACTION_ASSIGN_CAPABILITIES');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'ACTION_REMOVE_CAPABILITIES', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'ACTION_REMOVE_CAPABILITIES');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'ACTION_EDIT_CURRENT_CAPABILITY_LEVEL', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'ACTION_EDIT_CURRENT_CAPABILITY_LEVEL');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10000 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'API_FAULT_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'API_FAULT_REQUEST');
   
 INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'API_FAULT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'API_FAULT_PARM');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'API_FLIGHT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'API_FLIGHT_PARM');
   
INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10000, 'API_AIRCRAFT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10000 AND parm_name = 'API_AIRCRAFT_PARM');

-- Purchasing Manager

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10019, 'API_PURCHASE_ORDER_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_PURCHASE_ORDER_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10019, 'API_CREATE_ORDER_EXCEPTION_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_CREATE_ORDER_EXCEPTION_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10019, 'API_RAISE_ALERT_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_RAISE_ALERT_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10019, 'API_AEROEXCHANGE_REJECTIONS', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10019 AND parm_name = 'API_AEROEXCHANGE_REJECTIONS');


UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10019 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Purchasing Agent (if the purchase agent doen't have the following permission, uncaught error is thrown when issue an order)

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10018, 'API_PURCHASE_ORDER_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10018 AND parm_name = 'API_PURCHASE_ORDER_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10018, 'API_VENDOR_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10018 AND parm_name = 'API_VENDOR_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10018, 'API_HISTORY_NOTE_FOR_ORDER_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10018 AND parm_name = 'API_HISTORY_NOTE_FOR_ORDER_REQUEST');


UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10018 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Invoicing Agent (adding Validate Invoice and Mark as Paid permission)

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10024, 'ACTION_CREATE_PO_INVOICE', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10024 AND parm_name = 'ACTION_CREATE_PO_INVOICE');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10024, 'ACTION_MARK_AS_PAID', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10024 AND parm_name = 'ACTION_MARK_AS_PAID');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10024, 'ACTION_VALIDATE_FOR_PAYMENT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10024 AND parm_name = 'ACTION_VALIDATE_FOR_PAYMENT');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10024, 'ACTION_VIEW_PO_INVOICE_DETAILS', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10024 AND parm_name = 'ACTION_VIEW_PO_INVOICE_DETAILS');


UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10024 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- QC Inspector (The following 4 permissions are mandatory to inspect inventory, otherwise the system will throw uncaught error saying "This account is not authorized to access this API." )

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10013, 'API_RAISE_ALERT_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10013 AND parm_name = 'API_RAISE_ALERT_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10013, 'ACTION_GETINVENTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10013 AND parm_name = 'ACTION_GETINVENTORY');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10013, 'API_PART_DEFINITION_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10013 AND parm_name = 'API_PART_DEFINITION_REQUEST');


INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10013, 'API_ORDER_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10013 AND parm_name = 'API_ORDER_REQUEST');


UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10013 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Add to Line Planner role the permissions for Aircraft Grouping
INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10008, 'ACTION_EDIT_AIRCRAFT_GROUP_DETAILS', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10008 AND parm_name = 'ACTION_EDIT_AIRCRAFT_GROUP_DETAILS');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10008, 'ACTION_ASSIGN_AIRCRAFT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10008 AND parm_name = 'ACTION_ASSIGN_AIRCRAFT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10008, 'ACTION_REMOVE_AIRCRAFT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10008 AND parm_name = 'ACTION_REMOVE_AIRCRAFT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10008, 'ACTION_CREATE_AIRCRAFT_GROUP', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10008 AND parm_name = 'ACTION_CREATE_AIRCRAFT_GROUP');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10008, 'ACTION_REMOVE_AIRCRAFT_GROUP', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10008 AND parm_name = 'ACTION_REMOVE_AIRCRAFT_GROUP');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10008 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Technical Records Clerk

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10014, 'ACTION_RESOLUTION_CONFIG_SLOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10014 AND parm_name = 'ACTION_RESOLUTION_CONFIG_SLOT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10014, 'ACTION_START_HISTORIC_CHECK', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10014 AND parm_name = 'ACTION_START_HISTORIC_CHECK');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10014, 'ACTION_EDIT_HISTORIC_USAGE_SNAPSHOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10014 AND parm_name = 'ACTION_EDIT_HISTORIC_USAGE_SNAPSHOT');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10014 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Line Supervisor

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'API_AIRCRAFT_FLIGHTINFO_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'API_AIRCRAFT_FLIGHTINFO_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_OPEN_SMA', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_OPEN_SMA');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_REVIEW_WORK_CAPTURED', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_REVIEW_WORK_CAPTURED');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_RESOLUTION_CONFIG_SLOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_RESOLUTION_CONFIG_SLOT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_EDIT_HISTORIC_LABOUR_HISTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_EDIT_HISTORIC_LABOUR_HISTORY');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_EDIT_LABOUR_HISTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_EDIT_LABOUR_HISTORY');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_EDIT_HISTORIC_LABOUR_INFORMATION', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_EDIT_HISTORIC_LABOUR_INFORMATION');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_HISTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_HISTORY');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10001, 'ACTION_JSP_WEB_LABOUR_EDIT_WPLABOUR_HISTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10001 AND parm_name = 'ACTION_JSP_WEB_LABOUR_EDIT_WPLABOUR_HISTORY');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10001 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


-- Line Technician

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_ADD_PART_REQUIREMENT_SEARCH', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_ADD_PART_REQUIREMENT_SEARCH');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_PART_DEFINITION_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_PART_DEFINITION_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_TASK_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_TASK_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_CREATE_REFERENCE_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_CREATE_REFERENCE_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_SELECT_REFERENCE', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_SELECT_REFERENCE');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_FAULT_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_FAULT_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_GETINVENTORY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_GETINVENTORY');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_FAULT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_FAULT_PARM');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_AIRCRAFT_GROUP_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_AIRCRAFT_GROUP_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_SEARCH_REFERENCE_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_SEARCH_REFERENCE_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_SEARCH_DEFERRAL_REFERENCE_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_SEARCH_DEFERRAL_REFERENCE_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_FLIGHT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_FLIGHT_PARM');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_AIRCRAFT_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_AIRCRAFT_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_PART_REQUEST_REQUEST', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_PART_REQUEST_REQUEST');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_BATCH_COMPLETE_TASK', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_BATCH_COMPLETE_TASK');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_COMPLETE_CHECK_AND_SUBTASKS', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_COMPLETE_CHECK_AND_SUBTASKS');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_RESOLUTION_CONFIG_SLOT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_RESOLUTION_CONFIG_SLOT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_ALLOW_MEL_DEFERRAL_START_EDITING', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_ALLOW_MEL_DEFERRAL_START_EDITING');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_RECORD_OIL_UPTAKE', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_RECORD_OIL_UPTAKE');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_LOG_FAULT_AND_DEFER', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_LOG_FAULT_AND_DEFER');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'ACTION_ADD_EDIT_DAMAGE_RECORD', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'ACTION_ADD_EDIT_DAMAGE_RECORD');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10011 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';
	
INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10011, 'API_AIRCRAFT_PARM', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10011 AND parm_name = 'API_AIRCRAFT_PARM');


 -- Storeroom Clerk
 INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10017, 'ACTION_COMPLETE_PUTAWAY', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10017 AND parm_name = 'ACTION_COMPLETE_PUTAWAY');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10017, 'ACTION_INV_TURN_IN', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10017 AND parm_name = 'ACTION_INV_TURN_IN');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10017 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';


 -- Crew Lead(heavy lead)
 INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10007, 'ACTION_ADD_HR_SHIFT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10007 AND parm_name = 'ACTION_ADD_HR_SHIFT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10007, 'ACTION_EDIT_HR_SHIFT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10007 AND parm_name = 'ACTION_EDIT_HR_SHIFT');

INSERT INTO
   utl_action_role_parm (role_id, parm_name, parm_value, utl_id)
SELECT
   10007, 'ACTION_REMOVE_HR_SHIFT', 'true', 10
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE role_id = 10007 AND parm_name = 'ACTION_REMOVE_HR_SHIFT');

UPDATE
	utl_action_role_parm
SET
	parm_value = 'TRUE'
WHERE
	role_id = 10007 AND parm_name = 'ACTION_START_TASK' AND parm_value = 'false';
