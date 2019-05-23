/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

/**
 * Collection of Test Constants that are used throughout the test code
 */
public class TestConstants {

   public static final int PARALLELISM_DEGREE_DEFAULT = 1;
   public static final int CHUNKSIZE_DEFAULT = 10000;
   public static final String DATA_CD_DEFAULT = "STGTB";
   public static final int GATHER_STATS_DEFAULT = 0;
   public static final int LOGGING_DEFAULT = 0;
   public static final int AUTO_CREATE_TASK_BOOL_DEFAULT = 0;
   public static final String STATUS_COMPLETE = "COMPLETE";
   public static final String STATUS_NA = "N/A";
   public static final String STATUS_ACTV = "ACTV";
   public static final String STATUS_TERMINATE = "TERMINATE";
   public static final String STATUS_FORECAST = "FORECAST";
   public static final String STATUS_ASSIGN = "ASSIGN";
   public static final String STATUS_PROPEN = "PROPEN";
   public static final String TASK_CLASS_CD_REQ = "REQ";
   public static final String TASK_CLASS_CD_AMP = "AMP";
   public static final String TASK_CLASS_CD_BLOCK = "BLOCK";
   public static final String TASK_CLASS_CD_JIC = "JIC";
   public static final String TASK_CLASS_CD_ADHOC = "ADHOC";
   public static final String TASK_CLASS_CD_RO = "RO";
   public static final String TASK_CLASS_CD_REPL = "REPL";
   public static final String TASK_CLASS_CD_SRVC = "SRVC";
   public static final String REL_TYPE_CD_DEPT = "DEPT";
   public static final String REL_TYPE_CD_DRVTASK = "DRVTASK";
   public static final String REL_TYPE_CD_WORMVL = "WORMVL";
   public static final String SCHED_FROM_CD_LASTDUE = "LASTDUE";
   public static final String SCHED_FROM_CD_LASTEND = "LASTEND";
   public static final String SCHED_FROM_CD_BIRTH = "BIRTH";
   public static final String SCHED_FROM_CD_CUSTOM = "CUSTOM";

   public static final String BUILD_RESOURCES = "build\\resources\\";
   public static final String ACFT_DATA_FILE_PATH = BUILD_RESOURCES + "aircraft_datafile\\data\\";
   public static final String ACFT_BATCH_FILE_PATH =
         BUILD_RESOURCES + "\\main\\script\\aircraft_datafile";
   public static String TASK_BATCH_FILE = BUILD_RESOURCES + "main\\script_Actuals\\task";
   public static String TASK_DATA_FILE_PATH = TASK_BATCH_FILE + "\\data\\";
   public static final String TEST_CASE_DATA = BUILD_RESOURCES + "\\test\\testCaseData\\";

   public static String ODF_BATCH_FILE = BUILD_RESOURCES + "main\\script_Actuals\\odf";

   public static String HISTORICAL_USAGE_BATCH_FILE =
         BUILD_RESOURCES + "main\\script_Actuals\\usage";

   public static String PO_BATCH_FOLDER = BUILD_RESOURCES + "main\\script_Actuals\\po";

   public static String WPK_BATCH_FILE = BUILD_RESOURCES + "main\\script_Actuals\\workpackage";
   public static String BUILD_RESOURCES_MAIN = BUILD_RESOURCES + "main\\";
   public static String BUILD_RESOURCES_MAIN_LIB = BUILD_RESOURCES_MAIN + "lib\\";
   public static String BUILD_RESOURCES_MAIN_LIB_PLSQL = BUILD_RESOURCES_MAIN_LIB + "plsql\\";
   public static String BUILD_RESOURCES_MAIN_LIB_PLSQL_BL =
         BUILD_RESOURCES_MAIN_LIB_PLSQL + "baseline\\";

   // Actual loader CSV files
   public static final String ODF_CSV_FILE = "AL_OPEN_DEFERRED_FAULT_sample.csv";
   public static final String PO_HEADER_CSV_FILE = "C_PO_HEADER_sample.csv";
   public static final String PO_LINE_CSV_FILE = "C_PO_LINE_sample.csv";
   public static final String PO_LINE_TAX_CSV_FILE = "C_PO_LINE_TAX_sample.csv";
   public static final String PO_LINE_CHARGE_CSV_FILE = "C_PO_LINE_CHARGE_sample.csv";

   public static final String HISTORICAL_USAGE_CSV_FILE = "AL_HISTORICAL_USAGE_sample.csv";

   public static final String WPK_CSV_FILE = "AL_WORK_PACKAGE_sample.csv";
   public static final String WPK_TASK_CSV_FILE = "AL_WORK_PACKAGE_TASK_sample.csv";
   public static final String WPK_TASK_ATA_CSV_FILE = "AL_WORK_PACKAGE_TASK_ATA_sample.csv";

   // Actual loaded query String
   // public static final String ODF_ERROR_CHECK_TABLE = "select count(*) from "
   // + " (SELECT 1 from al_proc_open_deferred_fault " + " INNER JOIN al_proc_result ON "
   // + " al_proc_open_deferred_fault.record_id = al_proc_result.record_id " + " UNION ALL "
   // + " SELECT 1 FROM al_proc_odf_part_requirement " + " INNER JOIN al_proc_result ON "
   // + " al_proc_odf_part_requirement.record_id = al_proc_result.record_id)";

   public static final String ODF_ERROR_CHECK_TABLE = "select count(*) from "
         + " al_proc_open_deferred_fault " + " INNER JOIN al_proc_result ON "
         + " al_proc_open_deferred_fault.record_id = al_proc_result.record_id";

   public static final String ODF_ERROR_CHECK_MSG =
         "SELECT count(*) FROM al_proc_odf_part_requirement INNER JOIN al_proc_result ON "
               + " al_proc_odf_part_requirement.record_id = al_proc_result.record_id";

   public static final String HISTORICAL_USAGE_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM al_proc_hist_usage INNER JOIN al_proc_result ON "
               + " al_proc_hist_usage.record_id = al_proc_result.record_id";

   public static final String WPK_ERROR_CHECK_TABLE = "SELECT count(*) FROM al_proc_result "
         + "INNER JOIN DL_REF_MESSAGE ON " + "DL_REF_MESSAGE.result_cd = al_proc_result.result_cd "
         + "WHERE al_proc_result.func_area_cd = 'WRKPKG'";

   public static final String C_REQ_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req INNER JOIN DL_REF_MESSAGE ON "
               + " c_req.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req.result_cd IS NOT NULL";

   public static final String C_REQ_DYNAMIC_DEADLINE_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_dynamic_deadline INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_dynamic_deadline.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_dynamic_deadline.result_cd IS NOT NULL";

   public static final String C_REQ_DYNAMIC_PART_DEADLINE_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_dynamic_part_deadline INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_dynamic_part_deadline.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_dynamic_part_deadline.result_cd IS NOT NULL";

   public static final String C_REQ_IETM_TOPIC_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_ietm_topic INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_ietm_topic.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_ietm_topic.result_cd IS NOT NULL";

   public static final String C_REQ_JIC_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_jic INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_jic.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_jic.result_cd IS NOT NULL";

   public static final String C_REQ_PART_TRANSFORM_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_part_transform INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_part_transform.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_part_transform.result_cd IS NOT NULL";

   public static final String C_REQ_REPL_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_repl INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_repl.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_repl.result_cd IS NOT NULL";

   public static final String C_REQ_STANDARD_DEADLINE_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_standard_deadline INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_standard_deadline.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_standard_deadline.result_cd IS NOT NULL";

   public static final String C_REQ_ADVISORY_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_advisory INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_advisory.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_advisory.result_cd IS NOT NULL";

   public static final String C_REQ_IMPACT_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_impact INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_impact.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_impact.result_cd IS NOT NULL";

   public static final String C_REQ_LABOUR_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_labour INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_labour.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_labour.result_cd IS NOT NULL";

   public static final String C_REQ_PART_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_part INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_part.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_part.result_cd IS NOT NULL";

   public static final String C_REQ_TOOL_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_tool INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_tool.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_tool.result_cd IS NOT NULL";

   public static final String C_REQ_STEP_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_step INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_step.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_step.result_cd IS NOT NULL";

   public static final String C_REQ_STEP_SKILL_ERROR_CHECK_TABLE =
         "SELECT count(*) FROM c_req_step_skill INNER JOIN DL_REF_MESSAGE ON "
               + " c_req_step_skill.result_cd = DL_REF_MESSAGE.result_cd "
               + " WHERE c_req_step_skill.result_cd IS NOT NULL";

   public static final String AL_PROC_INVENTORY_ERROR_CHECK_TABLE_CRITICAL =
         "SELECT count(*) FROM al_proc_inventory_error INNER JOIN DL_REF_MESSAGE ON "
               + " al_proc_inventory_error.error_cd=DL_REF_MESSAGE.result_cd "
               + " WHERE DL_REF_MESSAGE.SEVERITY_CD='CRITICAL'";

   public static final String AL_ODF_ERROR_CHECK = "SELECT "
         + " al_proc_open_deferred_fault.assmbl_cd, " + " al_proc_open_deferred_fault.part_no_oem, "
         + " al_proc_open_deferred_fault.ata_sys_cd, " + " DL_REF_MESSAGE.result_cd, "
         + " DL_REF_MESSAGE.USER_DESC "
         + " FROM AL_PROC_OPEN_DEFERRED_FAULT INNER JOIN al_proc_result ON "
         + " al_proc_open_deferred_fault.record_id = al_proc_result.record_id "
         + " INNER JOIN DL_REF_MESSAGE ON "
         + " al_proc_result.result_cd = DL_REF_MESSAGE.result_cd " + " UNION ALL " + " SELECT "
         + " al_proc_odf_part_requirement.prt_assmbl_cd, "
         + " al_proc_odf_part_requirement.prt_part_no_oem, "
         + " al_proc_odf_part_requirement.prt_ipc_ref_cd, " + " DL_REF_MESSAGE.result_cd, "
         + "   DL_REF_MESSAGE.USER_DESC " + " FROM al_proc_odf_part_requirement "
         + " INNER JOIN al_proc_result ON "
         + " al_proc_odf_part_requirement.record_id = al_proc_result.record_id "
         + " INNER JOIN DL_REF_MESSAGE ON "
         + " al_proc_result.result_cd = DL_REF_MESSAGE.result_cd";

   public static final String BL_SENSITIVITY_ERROR_CHECK =
         "select  bl_proc_result.result_cd, DL_REF_MESSAGE.user_desc  from bl_proc_result "
               + " inner join bl_proc_sensitivity_sys on "
               + " bl_proc_result.record_id=bl_proc_sensitivity_sys.record_id "
               + " inner join DL_REF_MESSAGE on "
               + " DL_REF_MESSAGE.result_cd=bl_proc_result.result_cd " + "UNION ALL "
               + " select bl_proc_result.result_cd, DL_REF_MESSAGE.user_desc  from bl_proc_result "
               + " inner join bl_proc_sensitivity_pg on "
               + " bl_proc_result.record_id=bl_proc_sensitivity_pg.record_id "
               + " inner join DL_REF_MESSAGE on "
               + " DL_REF_MESSAGE.result_cd=bl_proc_result.result_cd";

   public static final String BL_COMHW_ERROR_CHECK =
         "select c_comhw_def.result_cd, DL_REF_MESSAGE.tech_desc from c_comhw_def "
               + "inner join DL_REF_MESSAGE on " + "c_comhw_def.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String C_PART_ERROR_CHECK =
         "select c_part_config.result_cd, DL_REF_MESSAGE.tech_desc from c_part_config "
               + "inner join DL_REF_MESSAGE on "
               + "c_part_config.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + " select c_dyn_part_config.result_cd, DL_REF_MESSAGE.tech_desc from c_dyn_part_config "
               + "inner join DL_REF_MESSAGE on "
               + "c_dyn_part_config.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + " select C_PROC_PARTS_CONFIG_PART.result_cd, DL_REF_MESSAGE.tech_desc from C_PROC_PARTS_CONFIG_PART "
               + "inner join DL_REF_MESSAGE on "
               + "C_PROC_PARTS_CONFIG_PART.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_USAGE_ERROR_CHECK =
         "select c_std_usage_def.result_cd, DL_REF_MESSAGE.tech_desc from c_std_usage_def "
               + "inner join DL_REF_MESSAGE on "
               + "c_std_usage_def.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "select c_dyn_usage_def.result_cd, DL_REF_MESSAGE.tech_desc from c_dyn_usage_def "
               + "inner join DL_REF_MESSAGE on "
               + "c_dyn_usage_def.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "select C_PROC_USAGE_DEF.Result_Cd, DL_REF_MESSAGE.tech_desc from C_PROC_USAGE_DEF "
               + "inner join DL_REF_MESSAGE on "
               + "C_PROC_USAGE_DEF.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String C_JIC_ERROR_CHECK =
         "select c_assmbl_panel.result_cd, DL_REF_MESSAGE.tech_desc from c_assmbl_panel "
               + "inner join DL_REF_MESSAGE on "
               + "c_assmbl_panel.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "select c_assmbl_zone.result_cd, DL_REF_MESSAGE.tech_desc from c_assmbl_zone "
               + "inner join DL_REF_MESSAGE on "
               + "c_assmbl_zone.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "select c_jic.Result_Cd, DL_REF_MESSAGE.tech_desc from c_jic "
               + "inner join DL_REF_MESSAGE on " + "c_jic.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "select c_jic_ietm_topic.result_cd, DL_REF_MESSAGE.tech_desc from c_jic_ietm_topic "
               + "inner join DL_REF_MESSAGE on "
               + "c_jic_ietm_topic.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "select c_jic_labor.result_cd, DL_REF_MESSAGE.tech_desc from c_jic_labor "
               + "inner join DL_REF_MESSAGE on " + "c_jic_labor.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "select c_jic_part.result_cd, DL_REF_MESSAGE.tech_desc from c_jic_part "
               + "inner join DL_REF_MESSAGE on " + "c_jic_part.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "select c_jic_tool.result_cd, DL_REF_MESSAGE.tech_desc from c_jic_tool "
               + "inner join DL_REF_MESSAGE on " + "c_jic_tool.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "select c_jic_step.result_cd, DL_REF_MESSAGE.tech_desc from c_jic_step "
               + "inner join DL_REF_MESSAGE on " + "c_jic_step.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_REF_DOC_ERROR_CHECK =
         "SELECT c_ref.result_cd,DL_REF_MESSAGE.tech_desc FROM c_ref "
               + "INNER JOIN DL_REF_MESSAGE ON " + "c_ref.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT c_ref_task_link.result_cd,DL_REF_MESSAGE.tech_desc FROM c_ref_task_link "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_ref_task_link.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_ref_standard_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_ref_standard_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_ref_standard_deadline.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_ref_dynamic_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_ref_dynamic_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_ref_dynamic_deadline.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_USERS_ERROR_CHECK =
         "SELECT C_ORG_HR.result_cd,DL_REF_MESSAGE.tech_desc FROM C_ORG_HR "
               + "INNER JOIN DL_REF_MESSAGE ON " + "C_ORG_HR.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT C_ORG_HR_FLOW_LEVEL.result_cd,DL_REF_MESSAGE.tech_desc FROM C_ORG_HR_FLOW_LEVEL "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "C_ORG_HR_FLOW_LEVEL.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_KIT_ERROR_CHECK =
         "select bl_proc_result.result_cd, DL_REF_MESSAGE.user_desc from bl_proc_result "
               + "inner join DL_REF_MESSAGE on "
               + "bl_proc_result.result_cd=DL_REF_MESSAGE.result_cd ";

   public static final String CIETM_ERROR_CHECK =
         "select C_IETM_IETM.result_cd, DL_REF_MESSAGE.tech_desc from C_IETM_IETM "
               + "inner join DL_REF_MESSAGE on " + "C_IETM_IETM.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + " select C_IETM_TOPIC_ATTACH.result_cd, DL_REF_MESSAGE.tech_desc from C_IETM_TOPIC_ATTACH "
               + " inner join DL_REF_MESSAGE on "
               + " C_IETM_TOPIC_ATTACH.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + " select C_IETM_TOPIC_TECH_REF.result_cd, DL_REF_MESSAGE.tech_desc from C_IETM_TOPIC_TECH_REF "
               + " inner join DL_REF_MESSAGE on "
               + " C_IETM_TOPIC_TECH_REF.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String C_ASSIGN_PARTS_ERROR_CHECK =
         "select C_ASSIGN_PARTS_TO_STOCK.result_cd, DL_REF_MESSAGE.tech_desc from C_ASSIGN_PARTS_TO_STOCK "
               + "inner join DL_REF_MESSAGE on "
               + "C_ASSIGN_PARTS_TO_STOCK.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String AL_TASK_ERROR_WARNING_CHECK =
         "SELECT 'C_RI_TASK' AS \"TABLE\", al_proc_tasks.task_cd, al_proc_tasks.serial_no_oem, "
               + "al_proc_tasks.part_no_oem, al_proc_tasks.manufact_cd,'N/A' AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.SEVERITY_CD, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_tasks INNER JOIN al_proc_tasks_error ON al_proc_tasks.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "WHERE DL_REF_MESSAGE.SEVERITY_CD in ('CRITICAL', 'DEPENDENCY') "
               + "UNION ALL SELECT 'C_RI_TASK' AS \"TABLE\", al_proc_hist_task.task_cd, al_proc_hist_task.serial_no_oem, "
               + "al_proc_hist_task.part_no_oem, al_proc_hist_task.manufact_cd, 'N/A' AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.SEVERITY_CD, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_hist_task INNER JOIN al_proc_tasks_error ON al_proc_hist_task.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY')  UNION ALL "
               + "SELECT 'C_RI_TASK_SCHED' AS \"TABLE\", al_proc_tasks_parameter.task_cd, al_proc_tasks_parameter.serial_no_oem, "
               + "'N/A' AS \"PART_NO_OEM\", 'N/A' AS \"MANUFACT_CD\", al_proc_tasks_parameter.parameter_code AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_tasks_parameter INNER JOIN al_proc_tasks_error ON al_proc_tasks_parameter.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') "
               + "UNION ALL SELECT 'C_RI_TASK_SCHED' AS \"TABLE\",al_proc_hist_task_parameter.task_cd, "
               + "al_proc_hist_task_parameter.serial_no_oem, al_proc_hist_task_parameter.part_no_oem, "
               + "al_proc_hist_task_parameter.manufact_cd,al_proc_hist_task_parameter.scheduling_parameter AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + "DL_REF_MESSAGE.user_desc FROM al_proc_hist_task_parameter "
               + "INNER JOIN al_proc_tasks_error ON al_proc_hist_task_parameter.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') "
               + "UNION ALL SELECT 'GLOBAL' AS \"TABLE\", 'N/A' AS task_cd, 'N/A' AS serial_no_oem, 'N/A' AS part_no_oem,'N/A' AS manufact_cd, "
               + "'N/A' AS \"PARAMETER_CD\", al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_tasks_error INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY')";

   public static final String AL_TASK_ERROR_CHECK =
         "SELECT 'C_RI_TASK' AS \"TABLE\", al_proc_tasks.task_cd, al_proc_tasks.serial_no_oem, "
               + "al_proc_tasks.part_no_oem, al_proc_tasks.manufact_cd,'N/A' AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_tasks INNER JOIN al_proc_tasks_error ON al_proc_tasks.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "UNION ALL SELECT 'C_RI_TASK' AS \"TABLE\", al_proc_hist_task.task_cd, al_proc_hist_task.serial_no_oem, "
               + "al_proc_hist_task.part_no_oem, al_proc_hist_task.manufact_cd, 'N/A' AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_hist_task INNER JOIN al_proc_tasks_error ON al_proc_hist_task.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + " UNION ALL "
               + "SELECT 'C_RI_TASK_SCHED' AS \"TABLE\", al_proc_tasks_parameter.task_cd, al_proc_tasks_parameter.serial_no_oem, "
               + "'N/A' AS \"PART_NO_OEM\", 'N/A' AS \"MANUFACT_CD\", al_proc_tasks_parameter.parameter_code AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + "FROM al_proc_tasks_parameter INNER JOIN al_proc_tasks_error ON al_proc_tasks_parameter.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "UNION ALL SELECT 'C_RI_TASK_SCHED' AS \"TABLE\",al_proc_hist_task_parameter.task_cd, "
               + "al_proc_hist_task_parameter.serial_no_oem, al_proc_hist_task_parameter.part_no_oem, "
               + "al_proc_hist_task_parameter.manufact_cd,al_proc_hist_task_parameter.scheduling_parameter AS \"PARAMETER_CD\", "
               + "al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + "DL_REF_MESSAGE.user_desc FROM al_proc_hist_task_parameter "
               + "INNER JOIN al_proc_tasks_error ON al_proc_hist_task_parameter.record_id = al_proc_tasks_error.record_id "
               + "INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd "
               + "UNION ALL SELECT 'GLOBAL' AS \"TABLE\", 'N/A' AS task_cd, 'N/A' AS serial_no_oem, 'N/A' AS part_no_oem, "
               + "'N/A' AS manufact_cd, 'N/A' AS \"PARAMETER_CD\", al_proc_tasks_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + "DL_REF_MESSAGE.user_desc FROM al_proc_tasks_error INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_tasks_error.error_cd";

   public static final String AL_INVENTORY_ERROR_CHECK = "SELECT 'C_RI_INVENTORY' AS \"TABLE\", "
         + "   al_proc_inventory.serial_no_oem,  al_proc_inventory.part_no_oem, "
         + "   al_proc_inventory.manufact_cd, " + "   al_proc_inventory_error.error_cd, "
         + "   DL_REF_MESSAGE.severity_cd, " + "   DL_REF_MESSAGE.user_desc "
         + "   FROM  al_proc_inventory " + "   INNER JOIN al_proc_inventory_error ON "
         + "   al_proc_inventory.record_id = al_proc_inventory_error.record_id "
         + "   INNER JOIN DL_REF_MESSAGE ON "
         + "   DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd " + "   UNION ALL "
         + "   SELECT 'C_RI_INVENTORY_SUB' AS \"TABLE\", "
         + "   al_proc_inventory_sub.serial_no_oem, " + "   al_proc_inventory_sub.part_no_oem, "
         + "   al_proc_inventory_sub.manufact_cd, " + "   al_proc_inventory_error.error_cd, "
         + "   DL_REF_MESSAGE.severity_cd, " + "   DL_REF_MESSAGE.user_desc "
         + "   FROM al_proc_inventory_sub " + "   INNER JOIN al_proc_inventory_error ON "
         + "   al_proc_inventory_sub.record_id = al_proc_inventory_error.record_id "
         + "   INNER JOIN DL_REF_MESSAGE ON "
         + "   DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd " + "   UNION ALL "
         + "   SELECT 'C_RI_INVENTORY_USAGE' AS \"TABLE\", "
         + "   al_proc_inventory_usage.serial_no_oem, " + "   al_proc_inventory_usage.part_no_oem, "
         + "   al_proc_inventory_usage.manufact_cd, " + "   al_proc_inventory_error.error_cd, "
         + "   DL_REF_MESSAGE.severity_cd, " + "   DL_REF_MESSAGE.user_desc "
         + "   FROM al_proc_inventory_usage " + "   INNER JOIN al_proc_inventory_error ON "
         + "   al_proc_inventory_usage.record_id = al_proc_inventory_error.record_id "
         + "   INNER JOIN DL_REF_MESSAGE ON "
         + "   DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd " + "   UNION ALL "
         + "   SELECT 'C_RI_ATTACH' AS \"TABLE\", " + "   al_proc_attach.attach_serial_no_oem, "
         + "   al_proc_attach.attach_part_no_oem, " + "   al_proc_attach.attach_manufact_cd, "
         + "   al_proc_inventory_error.error_cd, " + "   DL_REF_MESSAGE.severity_cd, "
         + "   DL_REF_MESSAGE.user_desc " + "   FROM al_proc_attach "
         + "   INNER JOIN al_proc_inventory_error ON "
         + "   al_proc_attach.record_id = al_proc_inventory_error.record_id "
         + "   INNER JOIN DL_REF_MESSAGE ON "
         + "   DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd"
         + " UNION ALL SELECT 'C_RI_INVENTORY_CAP_LEVELS' AS \"TABLE\", "
         + " al_proc_inventory_cap_levels.serial_no_oem, al_proc_inventory_cap_levels.part_no_oem, "
         + " al_proc_inventory_cap_levels.manufact_cd, al_proc_inventory_error.error_cd, "
         + " DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
         + " FROM al_proc_inventory_cap_levels INNER JOIN al_proc_inventory_error ON "
         + " al_proc_inventory_cap_levels.record_id = al_proc_inventory_error.record_id "
         + " INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd";

   public static final String AL_INVENTORY_ERROR_NO_WARNING_CHECK =
         "SELECT 'C_RI_INVENTORY' AS \"TABLE\", al_proc_inventory.serial_no_oem, "
               + " al_proc_inventory.part_no_oem,  al_proc_inventory.manufact_cd, "
               + " al_proc_inventory_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + " DL_REF_MESSAGE.user_desc FROM al_proc_inventory "
               + " INNER JOIN al_proc_inventory_error ON "
               + " al_proc_inventory.record_id = al_proc_inventory_error.record_id "
               + " INNER JOIN DL_REF_MESSAGE ON "
               + " DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd "
               + " WHERE  DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') " + " UNION ALL "
               + "SELECT 'C_RI_INVENTORY_SUB' AS \"TABLE\", "
               + " al_proc_inventory_sub.serial_no_oem, "
               + " al_proc_inventory_sub.part_no_oem, al_proc_inventory_sub.manufact_cd, "
               + " al_proc_inventory_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + " DL_REF_MESSAGE.user_desc FROM  al_proc_inventory_sub "
               + " INNER JOIN al_proc_inventory_error ON "
               + " al_proc_inventory_sub.record_id = al_proc_inventory_error.record_id "
               + " INNER JOIN DL_REF_MESSAGE ON  DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd "
               + " WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') " + " UNION ALL "
               + "SELECT 'C_RI_INVENTORY_USAGE' AS \"TABLE\", "
               + " al_proc_inventory_usage.serial_no_oem, "
               + " al_proc_inventory_usage.part_no_oem, " + " al_proc_inventory_usage.manufact_cd, "
               + " al_proc_inventory_error.error_cd,  DL_REF_MESSAGE.severity_cd, "
               + " DL_REF_MESSAGE.user_desc "
               + " FROM al_proc_inventory_usage INNER JOIN al_proc_inventory_error ON "
               + " al_proc_inventory_usage.record_id = al_proc_inventory_error.record_id "
               + " INNER JOIN DL_REF_MESSAGE ON "
               + " DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd "
               + " WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') " + " UNION ALL "
               + "SELECT 'C_RI_ATTACH' AS \"TABLE\", " + " al_proc_attach.attach_serial_no_oem, "
               + " al_proc_attach.attach_part_no_oem, al_proc_attach.attach_manufact_cd, "
               + " al_proc_inventory_error.error_cd, DL_REF_MESSAGE.severity_cd, "
               + " DL_REF_MESSAGE.user_desc FROM  al_proc_attach "
               + " INNER JOIN al_proc_inventory_error ON "
               + " al_proc_attach.record_id = al_proc_inventory_error.record_id "
               + " INNER JOIN DL_REF_MESSAGE ON "
               + " DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd "
               + " WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY') "
               + " UNION ALL SELECT 'C_RI_INVENTORY_CAP_LEVELS' AS \"TABLE\", "
               + " al_proc_inventory_cap_levels.serial_no_oem, al_proc_inventory_cap_levels.part_no_oem, "
               + " al_proc_inventory_cap_levels.manufact_cd, al_proc_inventory_error.error_cd, "
               + " DL_REF_MESSAGE.severity_cd, DL_REF_MESSAGE.user_desc "
               + " FROM al_proc_inventory_cap_levels INNER JOIN al_proc_inventory_error ON "
               + " al_proc_inventory_cap_levels.record_id = al_proc_inventory_error.record_id "
               + " INNER JOIN DL_REF_MESSAGE ON DL_REF_MESSAGE.result_cd = al_proc_inventory_error.error_cd "
               + " WHERE DL_REF_MESSAGE.severity_cd in ('CRITICAL', 'DEPENDENCY')";

   public static final String BL_STOCKLEVEL_ERROR_CHECK =
         "SELECT c_stock_level_attr.result_cd,DL_REF_MESSAGE.tech_desc FROM c_stock_level_attr "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_stock_level_attr.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_stock_level_alloc.result_cd,DL_REF_MESSAGE.tech_desc FROM c_stock_level_alloc "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_stock_level_alloc.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_BLOCK_ERROR_CHECK =
         "SELECT c_block.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block "
               + "INNER JOIN DL_REF_MESSAGE ON " + "c_block.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT c_block_chain_task_details.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block_chain_task_details "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_block_chain_task_details.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_block_dynamic_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block_dynamic_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_block_dynamic_deadline.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_block_dynamic_part_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block_dynamic_part_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_block_dynamic_part_deadline.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_block_req.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block_req "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_block_req.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_block_standard_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_block_standard_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_block_standard_deadline.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_COMJIC_ERROR_CHECK =
         "SELECT c_comp_jic.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic "
               + "INNER JOIN DL_REF_MESSAGE ON " + "c_comp_jic.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT c_comp_jic_assigned_part.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_assigned_part "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_assigned_part.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_jic_labour.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_labour "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_labour.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_jic_comhw.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_comhw "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_comhw.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_jic_tool.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_tool "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_tool.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_jic_measurement.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_measurement "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_measurement.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_comp_jic_step.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_step "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_step.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_comp_jic_ietm.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_jic_ietm "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_jic_ietm.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_COMREF_ERROR_CHECK =
         "SELECT c_comp_ref.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_ref "
               + "INNER JOIN DL_REF_MESSAGE ON " + "c_comp_ref.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT c_comp_ref_assigned_part.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_ref_assigned_part "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_ref_assigned_part.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_ref_task_link.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_ref_task_link "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_ref_task_link.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_ref_dynamic_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_ref_dynamic_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_ref_dynamic_deadline.result_cd=DL_REF_MESSAGE.result_cd";

   public static final String BL_COMPREQ_ERROR_CHECK =
         "SELECT c_comp_req.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req "
               + "INNER JOIN DL_REF_MESSAGE ON " + "c_comp_req.result_cd=DL_REF_MESSAGE.result_cd "
               + "UNION ALL "
               + "SELECT c_comp_req_assigned_part.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_assigned_part "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_assigned_part.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_req_jic.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_jic "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_jic.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_req_dynamic_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_dynamic_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_dynamic_deadline.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_req_standard_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_standard_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_standard_deadline.result_cd=DL_REF_MESSAGE.result_cd  " + "UNION ALL "
               + "SELECT c_comp_req_dyn_part_deadline.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_dyn_part_deadline "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_dyn_part_deadline.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_comp_req_ietm.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_ietm "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_ietm.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_comp_req_advisory.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_advisory "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_advisory.result_cd=DL_REF_MESSAGE.result_cd " + "UNION ALL "
               + "SELECT c_comp_req_impact.result_cd,DL_REF_MESSAGE.tech_desc FROM c_comp_req_impact "
               + "INNER JOIN DL_REF_MESSAGE ON "
               + "c_comp_req_impact.result_cd=DL_REF_MESSAGE.result_cd";


   /**
    * Creates a new {@linkplain TestConstants} object.
    */
   private TestConstants() {
   }
}
