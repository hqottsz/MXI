--liquibase formatted sql


--changeSet OPER-4303:1 stripComments:false
-- update the description of the sShowOnlyPartRequestsAssignedToHr session parameters
-- it is now only used on the Purchase Requests tab (no longer used on the Open Part Requests tab)
UPDATE
   UTL_CONFIG_PARM
SET
   PARM_DESC = 'Whether to hide part requests not assigned to the current user on the Purchase Requests todo list.'
WHERE
   PARM_NAME = 'sShowOnlyPartRequestsAssignedToHr';

--changeSet OPER-4303:2 stripComments:false
-- and add some new session parameters for the Open Part Requests tab
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sShowRequestsAssignedToOthers', 'SESSION','Whether to show requests not assigned to the current user on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sShowRequestsAssignedToOthers' );

--changeSet OPER-4303:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sShowPartRequests', 'SESSION','Whether to show all part requests on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sShowPartRequests' );

--changeSet OPER-4303:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '1', 'sShowStockRequests', 'SESSION','Whether to show all stock requests on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sShowStockRequests' );      

--changeSet OPER-4303:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ALL', 'sPartRequestPurchaseType', 'SESSION','Stores the last selected value of the Request Type dropdown  on the Open Part Requests todo list.','USER',  'STRING', 'ALL', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sPartRequestPurchaseType' );

--changeSet OPER-4303:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ALL', 'sPartRequestPartProvider', 'SESSION','Stores the last selected value of the Part Provider dropdown on the Open Part Requests todo list.','USER',  'STRING', 'ALL', 0, 'SESSION', '8.2-SP3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sPartRequestPartProvider' );      