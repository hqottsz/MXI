--liquibase formatted sql


--changeSet OPER-5323:1 stripComments:false
-- Task Name Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:4 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskStatus', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskStatus' AND parm_type = 'SESSION');      

--changeSet OPER-5323:5 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:6 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskAircraft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskAircraft' AND parm_type = 'SESSION');      

--changeSet OPER-5323:7 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByTaskWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByTaskWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:8 stripComments:false
-- ID Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNumber', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNumber' AND parm_type = 'SESSION');      

--changeSet OPER-5323:9 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByWPLineNo', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByWPLineNo' AND parm_type = 'SESSION');      

--changeSet OPER-5323:10 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByVendorWPLineNo', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByVendorWPLineNo' AND parm_type = 'SESSION');      

--changeSet OPER-5323:11 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByWPName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByWPName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:12 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByJICREQ' AND parm_type = 'SESSION');      

--changeSet OPER-5323:13 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:14 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:15 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDSubType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:16 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:17 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDRoutine' AND parm_type = 'SESSION');      

--changeSet OPER-5323:18 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByIDWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByIDWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:19 stripComments:false
-- Aircraft Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcft' AND parm_type = 'SESSION');      

--changeSet OPER-5323:20 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftHist' AND parm_type = 'SESSION');      

--changeSet OPER-5323:21 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftDueAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:22 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftDateRange' AND parm_type = 'SESSION');      

--changeSet OPER-5323:23 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftCmpltBefore' AND parm_type = 'SESSION');      

--changeSet OPER-5323:24 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftCmpltAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:25 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftJICREQ' AND parm_type = 'SESSION');      

--changeSet OPER-5323:26 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftTaskName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:27 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:28 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:29 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftSubType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:30 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:31 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftRoutine' AND parm_type = 'SESSION');      

--changeSet OPER-5323:32 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByAcftWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByAcftWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:33 stripComments:false
-- Config Slot Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsAssembly', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsAssembly' AND parm_type = 'SESSION');      

--changeSet OPER-5323:34 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:35 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsHist' AND parm_type = 'SESSION');      

--changeSet OPER-5323:36 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsDueAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:37 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsDateRange' AND parm_type = 'SESSION');      

--changeSet OPER-5323:38 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsCmpltBefore' AND parm_type = 'SESSION');      

--changeSet OPER-5323:39 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsCmpltAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:40 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsJICREQ' AND parm_type = 'SESSION');      

--changeSet OPER-5323:41 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsTaskName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:42 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsTaskType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:43 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:44 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsTaskSubType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:45 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:46 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsRoutine' AND parm_type = 'SESSION');      

--changeSet OPER-5323:47 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByCsWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByCsWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:48 stripComments:false
-- Serial Number Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialNum', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialNum' AND parm_type = 'SESSION');      

--changeSet OPER-5323:49 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialHist' AND parm_type = 'SESSION');      

--changeSet OPER-5323:50 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialDueAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:51 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialDateRange' AND parm_type = 'SESSION');      

--changeSet OPER-5323:52 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialCmpltBefore' AND parm_type = 'SESSION');      

--changeSet OPER-5323:53 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialCmpltAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:54 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialJICREQ' AND parm_type = 'SESSION');      

--changeSet OPER-5323:55 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialTaskName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:56 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialTaskType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:57 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:58 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialTaskSubType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:59 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:60 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialRoutine' AND parm_type = 'SESSION');      

--changeSet OPER-5323:61 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchBySerialWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchBySerialWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:62 stripComments:false
-- Part Number Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartNum', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartNum' AND parm_type = 'SESSION');      

--changeSet OPER-5323:63 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartHist' AND parm_type = 'SESSION');      

--changeSet OPER-5323:64 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartDueAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:65 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartDateRange' AND parm_type = 'SESSION');      

--changeSet OPER-5323:66 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartCmpltBefore' AND parm_type = 'SESSION');      

--changeSet OPER-5323:67 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartCmpltAfter' AND parm_type = 'SESSION');      

--changeSet OPER-5323:68 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartJICREQ' AND parm_type = 'SESSION');      

--changeSet OPER-5323:69 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartTaskName' AND parm_type = 'SESSION');      

--changeSet OPER-5323:70 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartTaskType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:71 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:72 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartTaskSubType' AND parm_type = 'SESSION');      

--changeSet OPER-5323:73 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartPriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:74 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartRoutine' AND parm_type = 'SESSION');      

--changeSet OPER-5323:75 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByPartWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByPartWorkType' AND parm_type = 'SESSION');

--changeSet OPER-5323:76 stripComments:false
-- Do not execute Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNotExeRevDate', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNotExeRevDate' AND parm_type = 'SESSION');      

--changeSet OPER-5323:77 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNotExeAircraft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNotExeAircraft' AND parm_type = 'SESSION');      

--changeSet OPER-5323:78 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNotExeOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNotExeOrig' AND parm_type = 'SESSION');      

--changeSet OPER-5323:79 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNotExePriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNotExePriority' AND parm_type = 'SESSION');      

--changeSet OPER-5323:80 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
   SELECT
     null, 'sTaskTypeSearchByNotExeWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sTaskTypeSearchByNotExeWorkType' AND parm_type = 'SESSION');