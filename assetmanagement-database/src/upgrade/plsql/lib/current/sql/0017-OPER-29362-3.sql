--liquibase formatted sql

--changeSet OPER-29362:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.procedure_drop('DPO_DBMS_CAPTURE_BUILD_JOB');
END;
/

--changeSet OPER-29362:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.procedure_drop('DPO_EXPORT_BASELINE_FILES');
END;
/

--changeSet OPER-29362:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.procedure_drop('DPO_IMPORT_BASELINE_FILES');
END;
/


--changeSet OPER-29362:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_CODE_TEMPLATE');
END;
/

--changeSet OPER-29362:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_CONSOL_TRACKING');
END;
/

--changeSet OPER-29362:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_EXPORT_FILE');
END;
/

--changeSet OPER-29362:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_EXPORT_LOG');
END;
/

--changeSet OPER-29362:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_GEN_OBJECTS');
END;
/

--changeSet OPER-29362:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_HANDLER');
END;
/

--changeSet OPER-29362:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_IMPORT_FILE');
END;
/

--changeSet OPER-29362:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_IMPORT_LOG');
END;
/

--changeSet OPER-29362:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_REP_PATH');
END;
/

--changeSet OPER-29362:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_REP_PATH_FILE');
END;
/

--changeSet OPER-29362:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_REP_PATH_RULSET');
END;
/

--changeSet OPER-29362:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_REP_RULESET');
END;
/

--changeSet OPER-29362:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_REP_TABLE');
END;
/

--changeSet OPER-29362:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_EVT_STRUCT');
END;
/

--changeSet OPER-29362:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_FUNCTION');
END;
/

--changeSet OPER-29362:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_OBJ_TYPE');
END;
/

--changeSet OPER-29362:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_RULE');
END;
/

--changeSet OPER-29362:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_RULE_CLASS');
END;
/

--changeSet OPER-29362:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_TYPE_ATTR');
END;
/

--changeSet OPER-29362:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_VERSION');
END;
/

--changeSet OPER-29362:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_RLM_VERSION_LOG');
END;
/

--changeSet OPER-29362:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_ROW_FILTERING');
END;
/

--changeSet OPER-29362:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_SEQ_TRACKING');
END;
/

--changeSet OPER-29362:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_CUST_TABLE');
END;
/

--changeSet OPER-29362:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_EXPORT_FILE');
END;
/

--changeSet OPER-29362:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_EXPORT_INV');
END;
/

--changeSet OPER-29362:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_FILE_GRP_CFG');
END;
/

--changeSet OPER-29362:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_IMPORT_FILE');
END;
/

--changeSet OPER-29362:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_IMPORT_INV');
END;
/

--changeSet OPER-29362:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_DPO_XFER_RLM_ERROR');
END;
/

--changeSet OPER-29362:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DPO_IMPORT_FAIL_MODE');
END;
/

--changeSet OPER-29362:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DPO_XFER_EXP_INV_S');
END;
/

--changeSet OPER-29362:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DPO_XFER_EXP_STATUS');
END;
/

--changeSet OPER-29362:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DPO_XFER_IMP_INV_S');
END;
/

--changeSet OPER-29362:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_DPO_XFER_IMP_S');
END;
/

--changeSet OPER-29362:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_CODE_TEMPLATE');
END;
/

--changeSet OPER-29362:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_CONSOL_TRACKING');
END;
/

--changeSet OPER-29362:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_EXPORT_FILE');
END;
/

--changeSet OPER-29362:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_EXPORT_LOG');
END;
/

--changeSet OPER-29362:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_GEN_OBJECTS');
END;
/

--changeSet OPER-29362:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_HANDLER');
END;
/

--changeSet OPER-29362:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_IMPORT_FILE');
END;
/

--changeSet OPER-29362:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_IMPORT_LOG');
END;
/

--changeSet OPER-29362:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_REP_PATH');
END;
/

--changeSet OPER-29362:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_REP_PATH_FILE');
END;
/

--changeSet OPER-29362:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_REP_PATH_RULSET');
END;
/

--changeSet OPER-29362:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_REP_RULESET');
END;
/

--changeSet OPER-29362:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_REP_TABLE');
END;
/

--changeSet OPER-29362:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_EVT_STRUCT');
END;
/

--changeSet OPER-29362:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_FUNCTION');
END;
/

--changeSet OPER-29362:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_OBJ_TYPE');
END;
/

--changeSet OPER-29362:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_RULE');
END;
/

--changeSet OPER-29362:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_RULE_CLASS');
END;
/

--changeSet OPER-29362:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_TYPE_ATTR');
END;
/

--changeSet OPER-29362:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_VERSION');
END;
/

--changeSet OPER-29362:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_RLM_VERSION_LOG');
END;
/

--changeSet OPER-29362:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_ROW_FILTERING');
END;
/

--changeSet OPER-29362:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_SEQ_TRACKING');
END;
/

--changeSet OPER-29362:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_CUST_TABLE');
END;
/

--changeSet OPER-29362:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_EXPORT_FILE');
END;
/

--changeSet OPER-29362:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_EXPORT_INV');
END;
/

--changeSet OPER-29362:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_FILE_GRP_CFG');
END;
/

--changeSet OPER-29362:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_IMPORT_FILE');
END;
/

--changeSet OPER-29362:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_IMPORT_INV');
END;
/

--changeSet OPER-29362:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_DPO_XFER_RLM_ERROR');
END;
/

--changeSet OPER-29362:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DPO_IMPORT_FAIL_MODE');
END;
/

--changeSet OPER-29362:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DPO_XFER_EXP_INV_S');
END;
/

--changeSet OPER-29362:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DPO_XFER_EXP_STATUS');
END;
/

--changeSet OPER-29362:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DPO_XFER_IMP_INV_S');
END;
/

--changeSet OPER-29362:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_DPO_XFER_IMP_S');
END;
/

--changeSet OPER-29362:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REPL_CTRL');
END;
/

--changeSet OPER-29362:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REPL_CTRL');
END;
/

--changeSet OPER-29362:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REPL_HIST');
END;
/

--changeSet OPER-29362:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REPL_HIST');
END;
/

--changeSet OPER-29362:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TUBR_REF_REPL_STATUS');
END;
/

--changeSet OPER-29362:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.trigger_drop('TIBR_REF_REPL_STATUS');
END;
/



--changeSet OPER-29362:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_RLM_ERROR');
END;
/

--changeSet OPER-29362:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_TYPE_ATTR');
END;
/

--changeSet OPER-29362:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_EVT_STRUCT');
END;
/

--changeSet OPER-29362:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_OBJ_TYPE');
END;
/

--changeSet OPER-29362:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_RULE');
END;
/

--changeSet OPER-29362:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_RULE_CLASS');
END;
/

--changeSet OPER-29362:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_FUNCTION');
END;
/

--changeSet OPER-29362:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_CUST_TABLE');
END;
/

--changeSet OPER-29362:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_FILE_GRP_CFG');
END;
/

--changeSet OPER-29362:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_EXPORT_INV');
END;
/

--changeSet OPER-29362:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_DPO_XFER_EXP_INV_STATUS');
END;
/

--changeSet OPER-29362:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_EXPORT_FILE');
END;
/

--changeSet OPER-29362:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_DPO_XFER_EXP_STATUS');
END;
/

--changeSet OPER-29362:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_IMPORT_INV');
END;
/

--changeSet OPER-29362:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_DPO_XFER_IMP_INV_STATUS');
END;
/

--changeSet OPER-29362:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_XFER_IMPORT_FILE');
END;
/

--changeSet OPER-29362:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_DPO_IMPORT_FAIL_MODE');
END;
/

--changeSet OPER-29362:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_DPO_XFER_IMP_STATUS');
END;
/

--changeSet OPER-29362:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_IMPORT_FILE');
END;
/

--changeSet OPER-29362:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_IMPORT_LOG');
END;
/

--changeSet OPER-29362:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_EXPORT_FILE');
END;
/

--changeSet OPER-29362:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_EXPORT_LOG');
END;
/

--changeSet OPER-29362:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_CODE_TEMPLATE');
END;
/

--changeSet OPER-29362:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_ROW_FILTERING');
END;
/

--changeSet OPER-29362:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_SEQ_TRACKING');
END;
/

--changeSet OPER-29362:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_CONSOL_TRACKING');
END;
/

--changeSet OPER-29362:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_GEN_OBJECTS');
END;
/

--changeSet OPER-29362:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_REP_PATH_RULSET');
END;
/

--changeSet OPER-29362:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_REP_PATH_FILE');
END;
/

--changeSet OPER-29362:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_REP_PATH');
END;
/

--changeSet OPER-29362:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_VERSION_LOG');
END;
/

--changeSet OPER-29362:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_RLM_VERSION');
END;
/

--changeSet OPER-29362:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_HANDLER');
END;
/

--changeSet OPER-29362:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_REP_TABLE');
END;
/

--changeSet OPER-29362:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DPO_REP_RULESET');
END;
/




--changeSet OPER-29362:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.view_drop('VW_REPL_CTRL_STATUS');
END;
/

--changeSet OPER-29362:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REPL_HIST');
END;
/

--changeSet OPER-29362:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REPL_CTRL');
END;
/

--changeSet OPER-29362:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('REF_REPL_STATUS');
END;
/



--changeSet OPER-29362:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.table_drop('DB_TYPE_CONFIG_PARM');
END;
/
