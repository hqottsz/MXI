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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Class containing static methods that return utility strings for dealing with the db
 */
public class TableUtil {

   // ACTUALS LOADER Tables
   public static final String AL_PROC_ATTACH = "AL_PROC_ATTACH";
   public static final String AL_PROC_HIST_TASK = "AL_PROC_HIST_TASK";
   public static final String AL_PROC_HIST_TASK_PARAMETER = "AL_PROC_HIST_TASK_PARAMETER";
   public static final String AL_PROC_INVENTORY = "AL_PROC_INVENTORY";
   public static final String AL_PROC_INVENTORY_CAP_LEVELS = "AL_PROC_INVENTORY_CAP_LEVELS";
   public static final String AL_PROC_INVENTORY_ERROR = "AL_PROC_INVENTORY_ERROR";
   public static final String AL_PROC_INVENTORY_SUB = "AL_PROC_INVENTORY_SUB";
   public static final String AL_PROC_INVENTORY_USAGE = "AL_PROC_INVENTORY_USAGE";
   public static final String AL_PROC_TASKS = "AL_PROC_TASKS";
   public static final String AL_PROC_TASKS_ERROR = "al_proc_tasks_error";
   public static final String AL_PROC_TASKS_PARAMETER = "AL_PROC_TASKS_PARAMETER";
   public static final String AL_WF_CYCLE_LOG = "al_wf_cycle_log";
   public static final String AL_MISSING_TASK = "al_missing_task";
   public static final String AL_MISSING_INVENTORY = "AL_MISSING_INVENTORY";
   public static final String AL_TEMP_INVENTORY_SUB = "AL_TEMP_INVENTORY_SUB";
   public static final String AL_IOE_ASSMBL = "AL_IOE_ASSMBL";
   public static final String AL_PROC_IOE_ASSMBL = "AL_PROC_IOE_ASSMBL";
   public static final String AL_HISTORICAL_USAGE = "AL_HISTORICAL_USAGE";

   public static final String C_RI_TASK = "c_ri_task";
   public static final String C_RI_TASK_SCHED = "c_ri_task_sched";
   public static final String C_RI_INVENTORY = "C_RI_INVENTORY";
   public static final String C_RI_INVENTORY_CAP_LEVELS = "C_RI_INVENTORY_CAP_LEVELS";
   public static final String C_RI_INVENTORY_USAGE = "C_RI_INVENTORY_USAGE";
   public static final String C_RI_INVENTORY_SUB = "C_RI_INVENTORY_SUB";
   public static final String C_RI_ATTACH = "C_RI_ATTACH";
   public static final String AL_PROC_INV_KEYS = "AL_PROC_INV_KEYS";
   public static final String C_PROC_RI_INVENTORY = "c_proc_ri_inventory";
   public static final String C_PROC_RI_INVENTORY_SUB = "c_proc_ri_inventory_sub";
   public static final String C_PROC_RI_INVENTORY_USAGE = "C_PROC_RI_INVENTORY_USAGE";
   public static final String C_PROC_RI_ATTACH = "C_PROC_RI_ATTACH";
   // public static final String C_PROC_RI_MESSAGE = "c_proc_ri_message";

   public static final String AL_OPEN_DEFERRED_FAULT = "AL_OPEN_DEFERRED_FAULT";
   public static final String AL_WORK_PACKAGE = "AL_WORK_PACKAGE";
   public static final String AL_WORK_PACKAGE_TASK = "AL_WORK_PACKAGE_TASK";
   public static final String AL_WORK_PACKAGE_TASK_ATA = "AL_WORK_PACKAGE_TASK_ATA";

   // BASELINE LOADER Tables
   public static final String C_FNC_ACCOUNT = "C_FNC_ACCOUNT";
   public static final String C_OWNER = "C_OWNER";
   public static final String C_FNC_TCODE = "C_FNC_TCODE";
   public static final String C_EQP_MANUFACT_MAP = "C_EQP_MANUFACT_MAP";
   public static final String C_ASSMBL_LIST = "C_ASSMBL_LIST";
   public static final String C_ATA_SYS = "C_ATA_SYS";
   public static final String C_ACFT_SUBASSY = "C_ACFT_SUBASSY";
   public static final String C_PART_CONFIG = "C_PART_CONFIG";
   public static final String C_DYN_PART_CONFIG = "C_DYN_PART_CONFIG";

   public static final String BL_KIT = "BL_KIT";
   public static final String BL_KIT_CONTENT = "BL_KIT_CONTENT";
   public static final String BL_INSTALL_KIT = "BL_INSTALL_KIT";
   public static final String BL_WEIGHT_BALANCE = "BL_WEIGHT_BALANCE";
   public static final String BL_PROC_WEIGHT_BALANCE = "BL_PROC_WEIGHT_BALANCE";
   public static final String BL_PG_PART_INCOMPAT = "BL_PG_PART_INCOMPAT";

   public static final String BL_PART_ADVISORY = "BL_PART_ADVISORY";
   public static final String C_JIC_PART = "C_JIC_PART";
   public static final String C_JIC = "C_JIC";
   public static final String C_JIC_LABOR = "C_JIC_LABOR";
   public static final String C_JIC_STEP = "C_JIC_STEP";
   public static final String C_JIC_STEP_SKILL = "C_JIC_STEP_SKILL";
   public static final String C_PART_NO_MISC_INFO = "C_PART_NO_MISC_INFO";
   public static final String C_STOCK_NO = "C_STOCK_NO";
   public static final String C_STOCK_LOC = "C_STOCK_LOC";
   public static final String C_BIN_PART = "C_BIN_PART";
   public static final String BL_ALTERNATE_UOM = "BL_ALTERNATE_UOM";
   public static final String C_LIC_DEFN = "C_LIC_DEFN";
   public static final String C_LIC_PREREQ = "C_LIC_PREREQ";
   public static final String C_REQ_LABOUR = "C_REQ_LABOUR";
   public static final String C_REQ_PART = "C_REQ_PART";
   public static final String C_REQ_TOOL = "C_REQ_TOOL";
   public static final String C_REQ_STEP = "C_REQ_STEP";
   public static final String C_REQ_STEP_SKILL = "C_REQ_STEP_SKILL";
   public static final String BL_FAIL_DEFER_REF = "BL_FAIL_DEFER_REF";
   public static final String BL_FAIL_DEFER_CONFLICT = "BL_FAIL_DEFER_CONFLICT";
   public static final String BL_FAIL_DEFER_REL = "BL_FAIL_DEFER_REL";
   public static final String BL_FAIL_DEFER_INSP = "BL_FAIL_DEFER_INSP";
   public static final String BL_FAIL_DEFER_DEAD = "BL_FAIL_DEFER_DEAD";
   public static final String BL_FAIL_DEFER_CAP_LEVEL = "BL_FAIL_DEFER_CAP_LEVEL";
   public static final String BL_CALC = "BL_CALC";
   public static final String BL_CALC_INPUT = "BL_CALC_INPUT";
   public static final String BL_CALC_PART_INPUT = "BL_CALC_PART_INPUT";

   // Automation
   public static final String C_STOCK_HEADER = "C_STOCK_HEADER";
   public static final String C_STOCK_ATTR = "C_STOCK_ATTR";
   public static final String C_LOC_AIRPORT = "C_LOC_AIRPORT";
   public static final String C_LOC_BIN = "C_LOC_BIN";
   public static final String C_LOC_DOCK = "C_LOC_DOCK";
   public static final String C_LOC_REPAIR = "C_LOC_REPAIR";
   public static final String C_LOC_STORE = "C_LOC_STORE";
   public static final String C_LOC_TRACK = "C_LOC_TRACK";
   public static final String C_ASSIGN_PARTS_TO_STOCK = "C_ASSIGN_PARTS_TO_STOCK";
   public static final String C_STOCK_LEVEL_ALLOC = "C_STOCK_LEVEL_ALLOC";
   public static final String C_STOCK_LEVEL_ATTR = "C_STOCK_LEVEL_ATTR";
   public static final String C_DEPT = "C_DEPT";
   public static final String C_AIRPORT_DEPT = "C_AIRPORT_DEPT";
   public static final String C_REF_FLOW = "C_REF_FLOW";
   public static final String C_REF_FLOW_LEVEL = "C_REF_FLOW_LEVEL";
   public static final String C_ORG_HR = "C_ORG_HR";
   public static final String C_ORG_HR_FLOW_LEVEL = "C_ORG_HR_FLOW_LEVEL";
   public static final String C_ORG_HR_LIC = "C_ORG_HR_LIC";
   public static final String BL_TAIL_SPEC_SCHEDULING = "BL_TAIL_SPEC_SCHEDULING";
   public static final String C_PO_HEADER = "C_PO_HEADER";
   public static final String C_PO_LINE = "C_PO_LINE";
   public static final String C_PO_LINE_TAX = "C_PO_LINE_TAX";
   public static final String C_PO_LINE_CHARGE = "C_PO_LINE_CHARGE";
   public static final String C_PROC_PO_HEADER = "C_PROC_PO_HEADER";
   public static final String C_PROC_PO_LINE = "C_PROC_PO_LINE";
   public static final String C_PROC_PO_LINE_TAX = "C_PROC_PO_LINE_TAX";
   public static final String C_PROC_PO_LINE_CHARGE = "C_PROC_PO_LINE_CHARGE";
   public static final String C_FAIL_MODE = "C_FAIL_MODE";

   public static final String C_ASSMBL_PANEL = "C_ASSMBL_PANEL";
   public static final String C_ASSMBL_ZONE = "C_ASSMBL_ZONE";
   public static final String C_JIC_TOOL = "C_JIC_TOOL";
   public static final String C_JIC_IETM_TOPIC = "C_JIC_IETM_TOPIC";
   public static final String C_REQ = "C_REQ";
   public static final String C_REQ_DYNAMIC_DEADLINE = "C_REQ_DYNAMIC_DEADLINE";
   public static final String C_REQ_DYNAMIC_PART_DEADLINE = "C_REQ_DYNAMIC_PART_DEADLINE";
   public static final String C_REQ_STANDARD_DEADLINE = "C_REQ_STANDARD_DEADLINE";
   public static final String C_REQ_IETM_TOPIC = "C_REQ_IETM_TOPIC";
   public static final String C_REQ_JIC = "C_REQ_JIC";
   public static final String C_REQ_ADVISORY = "C_REQ_ADVISORY";
   public static final String C_REQ_IMPACT = "C_REQ_IMPACT";
   public static final String C_REQ_REPL = "C_REQ_REPL";
   public static final String C_REQ_PART_TRANSFORM = "C_REQ_PART_TRANSFORM";

   public static final String C_COMP_JIC = "C_COMP_JIC";
   public static final String C_COMP_JIC_ASSIGNED_PART = "C_COMP_JIC_ASSIGNED_PART";
   public static final String C_COMP_JIC_LABOUR = "C_COMP_JIC_LABOUR";
   public static final String C_COMP_JIC_COMHW = "C_COMP_JIC_COMHW";
   public static final String C_COMP_JIC_TOOL = "C_COMP_JIC_TOOL";
   public static final String C_COMP_JIC_MEASUREMENT = "C_COMP_JIC_MEASUREMENT";
   public static final String C_COMP_JIC_STEP = "C_COMP_JIC_STEP";
   public static final String C_COMP_JIC_IETM = "C_COMP_JIC_IETM";
   public static final String C_COMP_REQ = "C_COMP_REQ";
   public static final String C_COMP_REQ_ASSIGNED_PART = "C_COMP_REQ_ASSIGNED_PART";
   public static final String C_COMP_REQ_DYNAMIC_DEADLINE = "C_COMP_REQ_DYNAMIC_DEADLINE";
   public static final String C_COMP_REQ_DYN_PART_DEADLINE = "C_COMP_REQ_DYN_PART_DEADLINE";
   public static final String C_COMP_REQ_JIC = "C_COMP_REQ_JIC";
   public static final String C_COMP_REQ_STANDARD_DEADLINE = "C_COMP_REQ_STANDARD_DEADLINE";
   public static final String C_COMP_REQ_IETM = "C_COMP_REQ_IETM";
   public static final String C_COMP_REQ_ADVISORY = "C_COMP_REQ_ADVISORY";
   public static final String C_COMP_REQ_IMPACT = "C_COMP_REQ_IMPACT";
   public static final String C_BLOCK = "C_BLOCK";
   public static final String C_BLOCK_REQ = "C_BLOCK_REQ";
   public static final String C_BLOCK_DYNAMIC_DEADLINE = "C_BLOCK_DYNAMIC_DEADLINE";
   public static final String C_BLOCK_DYNAMIC_PART_DEADLINE = "C_BLOCK_DYNAMIC_PART_DEADLINE";
   public static final String C_BLOCK_STANDARD_DEADLINE = "C_BLOCK_STANDARD_DEADLINE";
   public static final String C_BLOCK_CHAIN_TASK_DETAILS = "C_BLOCK_CHAIN_TASK_DETAILS";
   public static final String C_REF = "C_REF";
   public static final String C_REF_TASK_LINK = "C_REF_TASK_LINK";
   public static final String C_REF_DYNAMIC_DEADLINE = "C_REF_DYNAMIC_DEADLINE";
   public static final String C_REF_STANDARD_DEADLINE = "C_REF_STANDARD_DEADLINE";
   public static final String C_TOOL_DEF = "C_TOOL_DEF";
   public static final String C_MAINT_PRGM = "C_MAINT_PRGM";
   public static final String C_MAINT_PRGM_TASK = "C_MAINT_PRGM_TASK";
   public static final String C_IETM_IETM = "C_IETM_IETM";
   public static final String C_IETM_TOPIC_ATTACH = "C_IETM_TOPIC_ATTACH";
   public static final String C_IETM_TOPIC_TECH_REF = "C_IETM_TOPIC_TECH_REF";
   public static final String C_TASK_LINK = "C_TASK_LINK";
   public static final String C_COMP_REF = "C_COMP_REF";
   public static final String C_COMP_REF_ASSIGNED_PART = "C_COMP_REF_ASSIGNED_PART";
   public static final String C_COMP_REF_TASK_LINK = "C_COMP_REF_TASK_LINK";
   public static final String C_COMP_REF_DYNAMIC_DEADLINE = "C_COMP_REF_DYNAMIC_DEADLINE";
   public static final String C_COMP_REF_STANDARD_DEADLINE = "C_COMP_REF_STANDARD_DEADLINE";
   public static final String BL_SENSITIVITY_SYS = "BL_SENSITIVITY_SYS";
   public static final String BL_SENSITIVITY_PG = "BL_SENSITIVITY_PG";
   public static final String C_COMHW_DEF = "C_COMHW_DEF";
   public static final String C_DYN_USAGE_DEF = "C_DYN_USAGE_DEF";
   public static final String C_STD_USAGE_DEF = "C_STD_USAGE_DEF";

   public static final String C_VENDOR = "C_VENDOR";
   public static final String C_ORG_ORG_VENDOR = "C_ORG_ORG_VENDOR";
   public static final String C_ORG_VENDOR_PO_TYPE = "C_ORG_VENDOR_PO_TYPE";
   public static final String C_ORG_VENDOR_SERVICE_TYPE = "C_ORG_VENDOR_SERVICE_TYPE";
   public static final String C_VENDOR_ACCOUNT = "C_VENDOR_ACCOUNT";
   public static final String C_VENDOR_PURCHASE = "C_VENDOR_PURCHASE";
   public static final String C_VENDOR_REPAIR = "C_VENDOR_REPAIR";
   public static final String C_VENDOR_EXCHANGE = "C_VENDOR_EXCHANGE";

   // MAINTENIX Tables
   public static final String SCHED_STASK = "SCHED_STASK";
   public static final String SCHED_STASK_FLAGS = "SCHED_STASK_FLAGS";
   public static final String EQP_PART_BASELINE = "EQP_PART_BASELINE";
   public static final String EVT_INV = "EVT_INV";
   public static final String EVT_EVENT = "EVT_EVENT";
   public static final String EVT_EVENT_REL = "EVT_EVENT_REL";
   public static final String EVT_SCHED_DEAD = "EVT_SCHED_DEAD";
   public static final String EVT_STAGE = "EVT_STAGE";
   public static final String EVT_TOOL = "EVT_TOOL";
   public static final String TASK_DEFN = "task_defn";
   public static final String TASK_SCHED_RULE = "task_sched_rule";
   public static final String TASK_TASK = "task_task";
   private static final String BLT_WF_ERROR_LOG = "blt_wf_error_log";
   public static final String AL_PROC_TASKS_BIZ_LOG = "al_proc_tasks_biz_log";
   public static final String AL_PROC_TASK_IMP_RPT = "al_proc_task_imp_rpt";
   public static final String INV_INV_OEM_ASSMBL = "INV_INV_OEM_ASSMBL";
   public static final String INV_INV = "INV_INV";
   public static final String TASK_LABOUR_LIST = "TASK_LABOUR_LIST";
   public static final String SCHED_LABOUR = "SCHED_LABOUR";
   public static final String SCHED_LABOUR_ROLE = "SCHED_LABOUR_ROLE";
   public static final String SCHED_LABOUR_ROLE_STATUS = "SCHED_LABOUR_ROLE_STATUS";
   public static final String TASK_TOOL_LIST = "TASK_TOOL_LIST";
   public static final String TASK_STEP = "TASK_STEP";
   public static final String SCHED_STEP = "TASK_STEP";
   public static final String TASK_ZONE = "TASK_ZONE";
   public static final String SCHED_ZONE = "SCHED_ZONE";
   public static final String TASK_PANEL = "TASK_PANEL";
   public static final String SCHED_PANEL = "SCHED_PANEL";
   public static final String TASK_WORK_TYPE = "TASK_WORK_TYPE";
   public static final String SCHED_WORK_TYPE = "SCHED_WORK_TYPE";
   public static final String MIM_DATA_TYPE = "MIM_DATA_TYPE";
   public static final String TASK_PARM_DATA = "TASK_PARM_DATA";
   public static final String REF_DATA_TYPE_ASSMBL_CLASS = "REF_DATA_TYPE_ASSMBL_CLASS";
   public static final String INV_PARM_DATA = "INV_PARM_DATA";
   public static final String IETM_IETM = "IETM_IETM";
   public static final String IETM_TOPIC = "IETM_TOPIC";
   public static final String IETM_ASSMBL = "IETM_ASSMBL";
   public static final String TASK_TASK_IETM = "TASK_TASK_IETM";
   public static final String EVT_ATTACH = "EVT_ATTACH";
   public static final String EVT_IETM = "EVT_IETM";
   public static final String INV_LOC_BIN = "INV_LOC_BIN";
   public static final String INV_LOC_STOCK = "INV_LOC_STOCK";
   public static final String EQP_STOCK_NO = "EQP_STOCK_NO";
   public static final String EQP_PART_NO = "EQP_PART_NO";
   public static final String TASK_WEIGHT_BALANCE = "TASK_WEIGHT_BALANCE";
   public static final String EQP_PART_ALT_UNIT = "EQP_PART_ALT_UNIT";
   public static final String EQP_ADVSRY = "EQP_ADVSRY";
   public static final String EQP_PART_ADVSRY = "EQP_PART_ADVSRY";
   public static final String INV_ADVSRY = "INV_ADVSRY";
   public static final String EQP_PART_COMPAT_DEF = "EQP_PART_COMPAT_DEF";
   public static final String TASK_REP_REF = "TASK_REP_REF";
   public static final String EQP_ASSMBL_BOM = "EQP_ASSMBL_BOM";
   public static final String LIC_DEFN = "LIC_DEFN";
   public static final String GRP_DEFN = "GRP_DEFN";
   public static final String GRP_DEFN_LIC = "GRP_DEFN_LIC";
   public static final String LIC_DEFN_PREREQ = "LIC_DEFN_PREREQ";
   public static final String EVT_LIC_DEFN = "EVT_LIC_DEFN";
   public static final String TAX = "TAX";
   public static final String EQP_ASSMBL_POS = "EQP_ASSMBL_POS";
   public static final String EQP_ASSMBL = "EQP_ASSMBL";

   public static final String FAIL_DEFER_REF = "FAIL_DEFER_REF";
   public static final String FAIL_DEFER_CARRIER = "FAIL_DEFER_CARRIER";
   public static final String FAIL_DEFER_REF_CONFLICT_DEF = "FAIL_DEFER_REF_CONFLICT_DEF";
   public static final String FAIL_DEFER_REF_REL_DEF = "FAIL_DEFER_REF_REL_DEF";
   public static final String FAIL_DEFER_REF_TASK_DEFN = "FAIL_DEFER_REF_TASK_DEFN";
   public static final String FAIL_DEFER_REF_DEAD = "FAIL_DEFER_REF_DEAD";
   public static final String FAIL_DEFER_REF_DEGRAD_CAP = "FAIL_DEFER_REF_DEGRAD_CAP";

   public static final String EQP_KIT_PART_MAP = "EQP_KIT_PART_MAP";
   public static final String EQP_KIT_PART_GROUP_MAP = "EQP_KIT_PART_GROUP_MAP";
   public static final String EQP_KIT_PART_GROUPS = "EQP_KIT_PART_GROUPS";
   public static final String EQP_INSTALL_KIT_MAP = "EQP_INSTALL_KIT_MAP";
   public static final String EQP_INSTALL_KIT_PART_MAP = "EQP_INSTALL_KIT_PART_MAP";
   public static final String TASK_STEP_SKILL = "TASK_STEP_SKILL";
   public static final String MIM_CALC = "MIM_CALC";
   public static final String MIM_CALC_INPUT = "MIM_CALC_INPUT";
   public static final String MIM_PART_INPUT = "MIM_PART_INPUT";

   public static final String REQ_PART = "REQ_PART";

   // Automation
   public static final String INV_LOC = "INV_LOC";
   public static final String INV_LOC_ORG = "INV_LOC_ORG";
   public static final String INV_OWNER = "INV_OWNER";
   public static final String ORG_WORK_DEPT = "ORG_WORK_DEPT";
   public static final String INV_LOC_DEPT = "INV_LOC_DEPT";
   public static final String REF_PO_AUTH_FLOW = "REF_PO_AUTH_FLOW";
   public static final String REF_PO_AUTH_LVL = "REF_PO_AUTH_LVL";
   public static final String UTL_USER = "UTL_USER";
   public static final String ORG_HR = "ORG_HR";
   public static final String UTL_USER_ROLE = "UTL_USER_ROLE";
   public static final String ORG_HR_QUAL = "ORG_HR_QUAL";
   public static final String ORG_HR_AUTHORITY = "ORG_HR_AUTHORITY";
   public static final String ORG_DEPT_HR = "ORG_DEPT_HR";
   public static final String ORG_ORG_HR = "ORG_ORG_HR";
   public static final String ORG_HR_PO_AUTH_LVL = "ORG_HR_PO_AUTH_LVL";
   public static final String ORG_HR_SUPPLY = "ORG_HR_SUPPLY";
   public static final String ORG_AUTHORITY = "ORG_AUTHORITY";
   public static final String UTL_ROLE = "UTL_ROLE";
   public static final String ORG_ORG = "ORG_ORG";
   public static final String ORG_HR_LIC = "ORG_HR_LIC";
   public static final String TASK_AC_RULE = "TASK_AC_RULE";
   public static final String INV_AC_REG = "INV_AC_REG";
   public static final String FAIL_MODE = "FAIL_MODE";
   public static final String REF_TASK_PRIORITY = "REF_TASK_PRIORITY";
   public static final String REF_TASK_CLASS = "REF_TASK_CLASS";
   public static final String SD_FAULT = "SD_FAULT";
   public static final String SCHED_PART = "SCHED_PART";
   public static final String SCHED_INST_PART = "SCHED_INST_PART";
   public static final String SCHED_RMVD_PART = "SCHED_RMVD_PART";
   public static final String USG_USAGE_RECORD = "USG_USAGE_RECORD";
   public static final String USG_USAGE_DATA = "USG_USAGE_DATA";
   public static final String SCHED_WP = "SCHED_WP";
   public static final String SCHED_WP_SIGN_REQ = "SCHED_WP_SIGN_REQ";
   public static final String EVT_LOC = "EVT_LOC";
   public static final String FNC_ACCOUNT = "FNC_ACCOUNT";
   public static final String PO_HEADER = "PO_HEADER";
   public static final String PO_LINE = "PO_LINE";
   public static final String PO_LINE_CHARGE = "PO_LINE_CHARGE";
   public static final String PO_LINE_TAX = "PO_LINE_TAX";
   public static final String SHIP_SHIPMENT = "SHIP_SHIPMENT";
   public static final String SHIP_SHIPMENT_LINE = "SHIP_SHIPMENT_LINE";
   public static final String EQP_TASK_PANEL = "EQP_TASK_PANEL";
   public static final String EQP_TASK_ZONE = "EQP_TASK_ZONE";
   public static final String TASK_LABOR_LIST = "TASK_LABOR_LIST";
   public static final String TASK_PART_LIST = "TASK_PART_LIST";
   public static final String TASK_TASK_FLAGS = "TASK_TASK_FLAGS";
   public static final String TASK_PART_MAP = "TASK_PART_MAP";
   public static final String EQP_BOM_PART = "EQP_BOM_PART";
   public static final String TASK_TASK_DEP = "TASK_TASK_DEP";
   public static final String TASK_INTERVAL = "TASK_INTERVAL";
   public static final String TASK_REF_DOC = "TASK_REF_DOC";
   public static final String TASK_ADVISORY = "TASK_ADVISORY";
   public static final String TASK_IMPACT = "TASK_IMPACT";
   public static final String TASK_PART_TRANSFORM = "TASK_PART_TRANSFORM";
   public static final String TASK_JIC_REQ_MAP = "TASK_JIC_REQ_MAP";
   public static final String TASK_BLOCK_REQ_MAP = "TASK_BLOCK_REQ_MAP";
   public static final String REF_IMPACT = "REF_IMPACT";
   public static final String EVT_INV_USAGE = "EVT_INV_USAGE";
   public static final String INV_CURR_USAGE = "INV_CURR_USAGE";
   public static final String ORG_CARRIER = "ORG_CARRIER";
   public static final String ACFT_CAP_LEVELS = "ACFT_CAP_LEVELS";

   public static final String MAINT_PRGM = "MAINT_PRGM";
   public static final String MAINT_PRGM_CARRIER_MAP = "MAINT_PRGM_CARRIER_MAP";
   public static final String MAINT_PRGM_DEFN = "MAINT_PRGM_DEFN";
   public static final String MAINT_PRGM_TASK = "MAINT_PRGM_TASK";
   public static final String EQP_ASSMBL_BOM_SENS = "EQP_ASSMBL_BOM_SENS";
   public static final String EQP_BOM_PART_SENS = "EQP_BOM_PART_SENS";
   public static final String EQP_ASSMBL_SENS = "EQP_ASSMBL_SENS";
   public static final String MIM_PART_NUMDATA = "MIM_PART_NUMDATA";
   public static final String EQP_DATA_SOURCE = "EQP_DATA_SOURCE";
   public static final String EQP_DATA_SOURCE_SPEC = "EQP_DATA_SOURCE_SPEC";

   public static final String ORG_VENDOR = "ORG_VENDOR";
   public static final String INV_LOC_CONTACT = "INV_LOC_CONTACT";
   public static final String ORG_ORG_VENDOR = "ORG_ORG_VENDOR";
   public static final String ORG_VENDOR_PO_TYPE = "ORG_VENDOR_PO_TYPE";
   public static final String ORG_VENDOR_SERVICE_TYPE = "ORG_VENDOR_SERVICE_TYPE";
   public static final String ORG_VENDOR_ACCOUNT = "ORG_VENDOR_ACCOUNT";
   public static final String EQP_PART_VENDOR = "EQP_PART_VENDOR";
   public static final String EQP_PART_VENDOR_PRICE = "EQP_PART_VENDOR_PRICE";
   public static final String EQP_PART_VENDOR_REP = "EQP_PART_VENDOR_REP";
   public static final String EQP_PART_VENDOR_EXCHG = "EQP_PART_VENDOR_EXCHG";

   public static final String REF_LOC_TYPE = "REF_LOC_TYPE";
   public static final String REF_VENDOR_TYPE = "REF_VENDOR_TYPE";
   public static final String REF_CURRENCY = "REF_CURRENCY";
   public static final String REF_PO_TYPE = "REF_PO_TYPE";
   public static final String REF_VENDOR_STATUS = "REF_VENDOR_STATUS";
   public static final String REF_SERVICE_TYPE = "REF_SERVICE_TYPE";

   // processing table
   public static final String AL_PROC_RESULT = "AL_PROC_RESULT";
   public static final String BL_PROC_RESULT = "BL_PROC_RESULT";

   // error msg tables
   public static final String DL_REF_MESSAGE = "DL_REF_MESSAGE";

   // reference tables
   public static final String MIM_LOCAL_DB = "MIM_LOCAL_DB";
   public static final String REF_FAIL_DEFER = "REF_FAIL_DEFER";

   // axon tables
   public static final String AXON_DOMAIN_EVENT_ENTRY_TABLE = "axon_domain_event_entry";

   public static final List<String> ACTUALS_LOADER_TABLES = Arrays.asList( AL_PROC_INV_KEYS,
         AL_PROC_HIST_TASK, AL_PROC_HIST_TASK_PARAMETER, AL_PROC_TASKS, AL_PROC_TASKS_ERROR,
         AL_PROC_TASKS_PARAMETER, AL_WF_CYCLE_LOG, C_RI_TASK, C_RI_TASK_SCHED, C_RI_INVENTORY,
         C_RI_INVENTORY_CAP_LEVELS, C_RI_INVENTORY_USAGE, C_RI_INVENTORY_SUB, C_RI_ATTACH,
         C_PROC_RI_INVENTORY, C_PROC_RI_INVENTORY_SUB, C_PROC_RI_ATTACH, C_PROC_RI_INVENTORY_USAGE,
         BLT_WF_ERROR_LOG, AL_PROC_TASKS_BIZ_LOG, AL_IOE_ASSMBL, AL_PROC_IOE_ASSMBL,
         INV_INV_OEM_ASSMBL, AL_OPEN_DEFERRED_FAULT, AL_WORK_PACKAGE, AL_WORK_PACKAGE_TASK,
         AL_WORK_PACKAGE_TASK_ATA, AL_HISTORICAL_USAGE, AL_PROC_RESULT, AL_MISSING_TASK,
         AL_MISSING_INVENTORY, AL_TEMP_INVENTORY_SUB );

   public static final List<String> BASELINE_LOADER_TABLES = Arrays.asList( BL_PROC_RESULT,
         C_FNC_ACCOUNT, C_FNC_TCODE, C_EQP_MANUFACT_MAP, C_ASSMBL_LIST, C_ATA_SYS, BL_KIT,
         BL_KIT_CONTENT, BL_INSTALL_KIT, C_JIC_PART, C_JIC, BL_WEIGHT_BALANCE,
         BL_PROC_WEIGHT_BALANCE, C_ACFT_SUBASSY, C_STOCK_HEADER, C_STOCK_ATTR, C_LOC_AIRPORT,
         C_LOC_BIN, C_LOC_DOCK, C_LOC_REPAIR, C_LOC_STORE, C_ASSIGN_PARTS_TO_STOCK,
         C_STOCK_LEVEL_ALLOC, C_STOCK_LEVEL_ATTR, C_DEPT, C_AIRPORT_DEPT, C_REF_FLOW,
         C_REF_FLOW_LEVEL, C_ORG_HR, C_ORG_HR_FLOW_LEVEL, C_ORG_HR_LIC, C_JIC_LABOR, C_JIC_STEP,
         C_JIC_STEP_SKILL, BL_TAIL_SPEC_SCHEDULING, C_FAIL_MODE, C_ASSMBL_PANEL, C_ASSMBL_ZONE,
         C_JIC_IETM_TOPIC, C_JIC_TOOL, C_COMP_JIC, C_COMP_JIC_ASSIGNED_PART, C_COMP_JIC_LABOUR,
         C_COMP_JIC_COMHW, C_COMP_JIC_TOOL, C_COMP_JIC_MEASUREMENT, C_COMP_JIC_STEP,
         C_COMP_JIC_IETM, C_REQ, C_REQ_DYNAMIC_DEADLINE, C_REQ_DYNAMIC_PART_DEADLINE, C_COMP_REQ,
         C_COMP_REQ_ASSIGNED_PART, C_COMP_REQ_DYNAMIC_DEADLINE, C_COMP_REQ_DYN_PART_DEADLINE,
         C_BLOCK, C_BLOCK_DYNAMIC_DEADLINE, C_BLOCK_DYNAMIC_PART_DEADLINE, C_REF, C_REF_TASK_LINK,
         C_REF_DYNAMIC_DEADLINE, C_REQ_IETM_TOPIC, C_REQ_JIC, C_REQ_ADVISORY, C_REQ_IMPACT,
         C_REQ_STANDARD_DEADLINE, C_REQ_REPL, C_REQ_PART_TRANSFORM, C_TOOL_DEF, BL_PG_PART_INCOMPAT,
         C_MAINT_PRGM, C_MAINT_PRGM_TASK, C_BLOCK_REQ, BL_FAIL_DEFER_REF, BL_FAIL_DEFER_CONFLICT,
         BL_FAIL_DEFER_REL, BL_FAIL_DEFER_INSP, BL_FAIL_DEFER_DEAD, BL_FAIL_DEFER_CAP_LEVEL,
         BL_CALC_INPUT, BL_CALC_PART_INPUT, C_OWNER, C_REQ_LABOUR, C_REQ_PART, C_REQ_TOOL,
         C_REQ_STEP, C_REQ_STEP_SKILL, BL_CALC, C_OWNER, C_LOC_TRACK, C_COMP_REQ_JIC,
         C_COMP_REQ_STANDARD_DEADLINE, C_COMP_REQ_IETM, C_COMP_REQ_ADVISORY, C_COMP_REQ_IMPACT,
         C_IETM_IETM, C_IETM_TOPIC_ATTACH, C_IETM_TOPIC_TECH_REF, C_BLOCK_CHAIN_TASK_DETAILS,
         C_BLOCK_STANDARD_DEADLINE, C_REF_STANDARD_DEADLINE, C_TASK_LINK, C_COMP_REF,
         C_COMP_REF_ASSIGNED_PART, C_COMP_REF_TASK_LINK, C_COMP_REF_DYNAMIC_DEADLINE,
         C_COMP_REF_STANDARD_DEADLINE, BL_SENSITIVITY_SYS, BL_SENSITIVITY_PG, C_DYN_USAGE_DEF,
         C_STD_USAGE_DEF, C_COMHW_DEF, C_PART_CONFIG );

   public static final List<String> BASELINE_LOADER_TABLES_REGION_D =
         Arrays.asList( C_VENDOR, C_ORG_ORG_VENDOR, C_ORG_VENDOR_PO_TYPE, C_ORG_VENDOR_SERVICE_TYPE,
               C_VENDOR_ACCOUNT, C_VENDOR_PURCHASE, C_VENDOR_REPAIR, C_VENDOR_EXCHANGE,
               C_VENDOR_PURCHASE, C_VENDOR_REPAIR, C_VENDOR_EXCHANGE );

   public static final List<String> SCHED_LABOUR_TABLES =
         Arrays.asList( SCHED_LABOUR, SCHED_LABOUR_ROLE, SCHED_LABOUR_ROLE_STATUS );

   public static final List<String> SCHED_TOOL_TABLES = Arrays.asList( TASK_TOOL_LIST, EVT_TOOL );
   public static final List<String> SCHED_STEP_TABLES = Arrays.asList( SCHED_STEP );
   public static final List<String> SCHED_ZONE_TABLES = Arrays.asList( SCHED_ZONE );
   public static final List<String> SCHED_PANEL_TABLES = Arrays.asList( SCHED_PANEL );
   public static final List<String> SCHED_WORKTYPE_TABLES = Arrays.asList( SCHED_WORK_TYPE );
   public static final List<String> SCHED_ASSMBLYMEASURES_TABLES = Arrays.asList( INV_PARM_DATA );
   public static final List<String> SCHED_IETM_TABLES = Arrays.asList( IETM_IETM, IETM_TOPIC );


   /**
    * SERIAL_NO_OEM VARCHAR2(40 CHAR) Y PART_NO_OEM VARCHAR2(40 CHAR) Y MANUFACT_CD VARCHAR2(16
    * CHAR) Y TASK_CD VARCHAR2(200 CHAR) Y COMPLETION_DT DATE Y COMPLETION_DUE_DT DATE Y
    * CUSTOM_START_DT DATE Y CUSTOM_DUE_DT DATE Y
    *
    * @return
    */
   public static String getInsertCRITaskString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + C_RI_TASK + "( " );
      lInsertStatement.append( "SERIAL_NO_OEM," );
      lInsertStatement.append( "PART_NO_OEM," );
      lInsertStatement.append( "MANUFACT_CD," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "COMPLETION_DT," );
      lInsertStatement.append( "COMPLETION_DUE_DT," );
      lInsertStatement.append( "CUSTOM_START_DT," );
      lInsertStatement.append( "CUSTOM_DUE_DT" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( ?,?,?,?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   public static String getInsertForTableByMap( String aTableName, Map<String, String> aDataMap ) {
      String lColumns = "";
      String lValues = "";

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         lValues += lEntry.getValue() + ",";
         lColumns += lEntry.getKey() + ",";
      }

      lValues = removeLastComma( lValues );

      lColumns = removeLastComma( lColumns );

      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO " + aTableName + " (" );
      lInsertStatement.append( lColumns + ")" );
      lInsertStatement.append( " values ( " + lValues + ")" );

      return lInsertStatement.toString();
   }


   /**
    * This is will generate sql statement used to find a particular record.
    *
    * @param aTableName
    *           - table you want to remove records
    * @param aDataMap
    *           - data values used to identify the record
    * @return sql statement
    */

   public static String findRecordInDatabase( String aTableName, Map<String, String> aDataMap ) {
      String lWhereClause = "";

      lWhereClause = whereFromTableByMap( aDataMap );

      StringBuilder lFindStatement = new StringBuilder();
      lFindStatement.append( "Select * from " + aTableName );
      lFindStatement.append( " " + lWhereClause );

      return lFindStatement.toString();
   }


   /**
    * This is will generate sql statement used to find a particular record.
    *
    * @param aTableName
    *           - table you want to remove records
    * @param aDataMap
    *           - data values used to identify the record
    * @return sql statement
    */

   public static String deleteRecordInDatabase( String aTableName, Map<String, String> aDataMap ) {
      String lWhereClause = "";

      lWhereClause = whereFromTableByMap( aDataMap );

      StringBuilder lFindStatement = new StringBuilder();
      lFindStatement.append( "Delete from " + aTableName );
      lFindStatement.append( " " + lWhereClause );

      return lFindStatement.toString();
   }


   public static String getInsertForTableByMap( Map<String, String> aMapTaskFirstTime,
         String aTableName ) {
      String lColumns = "";
      String lValues = "";

      for ( Entry<String, String> lEntry : aMapTaskFirstTime.entrySet() ) {
         lValues += lEntry.getValue() + ",";
         lColumns += lEntry.getKey() + ",";
      }

      if ( ( lValues.length() > 0 ) && ( lValues.charAt( lValues.length() - 1 ) == ',' ) ) {
         lValues = lValues.substring( 0, lValues.length() - 1 );
      }

      if ( ( lColumns.length() > 0 ) && ( lColumns.charAt( lColumns.length() - 1 ) == ',' ) ) {
         lColumns = lColumns.substring( 0, lColumns.length() - 1 );
      }

      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO " + aTableName + " (" );
      lInsertStatement.append( lColumns + ")" );
      lInsertStatement.append( " values ( " + lValues + ")" );

      return lInsertStatement.toString();
   }


   /**
    * This method returns a sql statement needed to insert a record into the
    * AL_PROC_HIST_TASK_PARAMETER table.
    *
    * @return {@link String}
    */
   public static String getInsertProcHistTaskParameterString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + AL_PROC_HIST_TASK_PARAMETER + "( " );
      lInsertStatement.append( "PARAMETER_ID," );
      lInsertStatement.append( "RECORD_ID," );
      lInsertStatement.append( "CYCLE_ID," );
      lInsertStatement.append( "SERIAL_NO_OEM," );
      lInsertStatement.append( "PART_NO_OEM," );
      lInsertStatement.append( "MANUFACT_CD," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "COMPLETION_DATE," );
      lInsertStatement.append( "COMPONENT_BASED_USAGE," );
      lInsertStatement.append( "DUE_DATE," );
      lInsertStatement.append( "SCHEDULING_PARAMETER," );
      lInsertStatement.append( "COMPLETED_VALUE," );
      lInsertStatement.append( "DUE_VALUE," );
      lInsertStatement.append( "CUSTOM_START_VALUE," );
      lInsertStatement.append( "CUSTOM_DUE_VALUE" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   /**
    * This method returns a sql statement needed to insert a record into the AL_PROC_HIST_TASK
    * table.
    *
    * @return {@link String}
    */
   public static String getInsertProcHistTaskString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + AL_PROC_HIST_TASK + "( " );
      lInsertStatement.append( "RECORD_ID," );
      lInsertStatement.append( "CYCLE_ID," );
      lInsertStatement.append( "SERIAL_NO_OEM," );
      lInsertStatement.append( "PART_NO_OEM," );
      lInsertStatement.append( "MANUFACT_CD," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "COMPLETION_DATE," );
      lInsertStatement.append( "DUE_DATE," );
      lInsertStatement.append( "INV_NO_DB_ID," );
      lInsertStatement.append( "INV_NO_ID," );
      lInsertStatement.append( "PART_NO_DB_ID," );
      lInsertStatement.append( "PART_NO_ID," );
      lInsertStatement.append( "ASSMBL_DB_ID," );
      lInsertStatement.append( "ASSMBL_BOM_ID," );
      lInsertStatement.append( "ASSMBL_CD," );
      lInsertStatement.append( "ASSMBL_INV_NO_DB_ID," );
      lInsertStatement.append( "ASSMBL_INV_NO_ID," );
      lInsertStatement.append( "ORIG_ASSMBL_CD," );
      lInsertStatement.append( "APPL_EFF_CD," );
      lInsertStatement.append( "TASK_DEFN_DB_ID," );
      lInsertStatement.append( "TASK_DEFN_ID," );
      lInsertStatement.append( "TASK_DB_ID," );
      lInsertStatement.append( "TASK_ID," );
      lInsertStatement.append( "TASK_APPL_EFF_LDESC," );
      lInsertStatement.append( "TASK_APPL_SQL_LDESC," );
      lInsertStatement.append( "CUSTOM_START_DT," );
      lInsertStatement.append( "CUSTOM_DUE_DATE" );
      lInsertStatement.append( " )" );
      lInsertStatement
            .append( " VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   /**
    * This method returns a sql statement needed to insert a record into the AL_PROC_TASKS_ERROR
    * table.
    *
    * @return {@link String}
    */
   public static String getInsertProcTaskErrorString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + AL_PROC_TASKS_ERROR + "( " );
      lInsertStatement.append( "ERROR_LOG_ID," );
      lInsertStatement.append( "RECORD_ID," );
      lInsertStatement.append( "CYCLE_ID," );
      lInsertStatement.append( "ERROR_CD," );
      lInsertStatement.append( "RULE_CD" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( ?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   public static String getInsertProcTaskParamString() {
      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO " + TableUtil.AL_PROC_TASKS_PARAMETER + " (" );

      lInsertStatement.append( "CYCLE_ID," );
      lInsertStatement.append( "RECORD_ID," );
      lInsertStatement.append( "SERIAL_NO_OEM," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "PARAMETER_CODE," );
      lInsertStatement.append( "COMPLETION_VALUE," );
      lInsertStatement.append( "LAST_DUE_VALUE," );
      lInsertStatement.append( "DEVIATION_VALUE," );
      lInsertStatement.append( "START_VALUE," );
      lInsertStatement.append( "NEXT_DUE_VALUE," );
      lInsertStatement.append( "MANUAL_INTERVAL_VALUE," );
      lInsertStatement.append( "DATA_TYPE_DB_ID," );
      lInsertStatement.append( "DATA_TYPE_ID," );
      lInsertStatement.append( "CUSTOM_START_VALUE," );
      lInsertStatement.append( "CUSTOM_DUE_VALUE" );

      lInsertStatement.append( ") VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" );

      return lInsertStatement.toString();
   }


   /**
    * Build a task data
    *
    * @return
    */
   public static String getInsertProcTaskString() {
      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( "INSERT INTO " + TableUtil.AL_PROC_TASKS + " (" );

      lInsertStatement.append( "CYCLE_ID," );
      lInsertStatement.append( "RECORD_ID," );
      lInsertStatement.append( "SERIAL_NO_OEM," );
      lInsertStatement.append( "PART_NO_OEM," );
      lInsertStatement.append( "MANUFACT_CD," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "COMPLETION_DATE," );
      lInsertStatement.append( "LAST_DUE_DATE," );
      lInsertStatement.append( "CALENDAR_DEVIATION_VALUE," );
      lInsertStatement.append( "START_DATE," );
      lInsertStatement.append( "NEXT_DUE_DATE," );
      lInsertStatement.append( "MANUAL_DATE_INTERVAL," );
      lInsertStatement.append( "INV_NO_DB_ID," );
      lInsertStatement.append( "INV_NO_ID," );
      lInsertStatement.append( "ACFT_NO_DB_ID," );
      lInsertStatement.append( "ACFT_NO_ID," );
      lInsertStatement.append( "PART_NO_DB_ID," );
      lInsertStatement.append( "PART_NO_ID," );
      lInsertStatement.append( "TASK_DB_ID," );
      lInsertStatement.append( "TASK_ID," );
      lInsertStatement.append( "TASK_DEFN_DB_ID," );
      lInsertStatement.append( "TASK_DEFN_ID," );
      lInsertStatement.append( "CUSTOM_START_DT," );
      lInsertStatement.append( "CUSTOM_DUE_DATE," );
      lInsertStatement.append( "FIRST_TIME_BOOL" );
      lInsertStatement.append( ") VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )" );

      return lInsertStatement.toString();
   }


   /**
    * This method returns a sql statement needed to insert a record into the TASK_DEFN table.
    *
    * @return {@link String}
    */
   public static String getInsertTaskDefnString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + TASK_DEFN + "( " );
      lInsertStatement.append( "TASK_DEFN_DB_ID," );
      lInsertStatement.append( "TASK_DEFN_ID," );
      lInsertStatement.append( "LAST_REVISION_ORD," );
      lInsertStatement.append( "NEW_REVISION_BOOL," );
      lInsertStatement.append( "ALT_ID" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( ?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   public static String getInsertTaskSchedRuleStatement() {

      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + TASK_SCHED_RULE + "( " );
      lInsertStatement.append( "TASK_DB_ID," );
      lInsertStatement.append( "TASK_ID," );
      lInsertStatement.append( "DATA_TYPE_DB_ID," );
      lInsertStatement.append( "DATA_TYPE_ID," );
      lInsertStatement.append( "DEF_INTERVAL_QT," );
      lInsertStatement.append( "DEF_NOTIFY_QT," );
      lInsertStatement.append( "DEF_DEVIATION_QT," );
      lInsertStatement.append( "DEF_PREFIXED_QT," );
      lInsertStatement.append( "DEF_POSTFIXED_QT," );
      lInsertStatement.append( "DEF_INITIAL_QT" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( ?,?,?,?,?,?,?,?,?,? ) " );

      return lInsertStatement.toString();
   }


   /**
    * This method returns a sql statement needed to insert a record into the TASK_TASK table.
    *
    * @return {@link String}
    */
   public static String getInsertTaskTaskString() {
      StringBuffer lInsertStatement = new StringBuffer();
      lInsertStatement.append( "INSERT INTO " + TASK_TASK + "( " );
      lInsertStatement.append( "TASK_DB_ID," );
      lInsertStatement.append( "TASK_ID," );
      lInsertStatement.append( "ACTV_HR_DB_ID," );
      lInsertStatement.append( "ACTV_HR_ID," );
      lInsertStatement.append( "TASK_DEFN_DB_ID," );
      lInsertStatement.append( "TASK_DEFN_ID," );
      lInsertStatement.append( "TASK_CLASS_DB_ID," );
      lInsertStatement.append( "TASK_CLASS_CD," );
      lInsertStatement.append( "TASK_PRIORITY_DB_ID," );
      lInsertStatement.append( "TASK_PRIORITY_CD," );
      lInsertStatement.append( "ASSMBL_DB_ID," );
      lInsertStatement.append( "ASSMBL_CD," );
      lInsertStatement.append( "ASSMBL_BOM_ID," );
      lInsertStatement.append( "REPL_ASSMBL_DB_ID," );
      lInsertStatement.append( "REPL_ASSMBL_CD," );
      lInsertStatement.append( "REPL_ASSMBL_BOM_ID," );
      lInsertStatement.append( "TASK_SUBCLASS_DB_ID," );
      lInsertStatement.append( "TASK_SUBCLASS_CD," );
      lInsertStatement.append( "TASK_ORIGINATOR_DB_ID," );
      lInsertStatement.append( "TASK_ORIGINATOR_CD," );
      lInsertStatement.append( "TASK_DEF_STATUS_DB_ID," );
      lInsertStatement.append( "TASK_DEF_STATUS_CD," );
      lInsertStatement.append( "BITMAP_DB_ID," );
      lInsertStatement.append( "BITMAP_TAG," );
      lInsertStatement.append( "TASK_CD," );
      lInsertStatement.append( "TASK_NAME," );
      lInsertStatement.append( "TASK_LDESC," );
      lInsertStatement.append( "INSTRUCTION_LDESC," );
      lInsertStatement.append( "ISSUE_ACCOUNT_DB_ID," );
      lInsertStatement.append( "ISSUE_ACCOUNT_ID," );
      lInsertStatement.append( "RECURRING_TASK_BOOL," );
      lInsertStatement.append( "SCHED_FROM_LATEST_BOOL," );
      lInsertStatement.append( "MANUAL_SCHEDULING_BOOL," );
      lInsertStatement.append( "TASK_APPL_LDESC," );
      lInsertStatement.append( "TASK_APPL_SQL_LDESC," );
      lInsertStatement.append( "TASK_REF_SDESC," );
      lInsertStatement.append( "EST_DURATION_QT," );
      lInsertStatement.append( "EFFECTIVE_DT," );
      lInsertStatement.append( "EFFECTIVE_GDT," );
      lInsertStatement.append( "ROUTINE_BOOL," );
      lInsertStatement.append( "FORECAST_RANGE_QT," );
      lInsertStatement.append( "MIN_PLAN_YIELD_PCT," );
      lInsertStatement.append( "RESOURCE_SUM_BOOL," );
      lInsertStatement.append( "REVISION_ORD," );
      lInsertStatement.append( "ACTV_DT," );
      lInsertStatement.append( "ACTV_LDESC," );
      lInsertStatement.append( "ACTV_REF_SDESC," );
      lInsertStatement.append( "UNIQUE_BOOL," );
      lInsertStatement.append( "WORKSCOPE_BOOL," );
      lInsertStatement.append( "AUTO_COMPLETE_BOOL," );
      lInsertStatement.append( "LAST_SCHED_DEAD_BOOL," );
      lInsertStatement.append( "PLANNING_TYPE_DB_ID," );
      lInsertStatement.append( "PLANNING_TYPE_ID," );
      lInsertStatement.append( "TASK_APPL_EFF_LDESC," );
      lInsertStatement.append( "ENGINEERING_LDESC," );
      lInsertStatement.append( "CANCEL_ON_AC_INST_BOOL," );
      lInsertStatement.append( "CANCEL_ON_ANY_INST_BOOL," );
      lInsertStatement.append( "CREATE_ON_AC_INST_BOOL," );
      lInsertStatement.append( "CREATE_ON_ANY_INST_BOOL," );
      lInsertStatement.append( "CANCEL_ON_AC_RMVL_BOOL," );
      lInsertStatement.append( "CANCEL_ON_ANY_RMVL_BOOL," );
      lInsertStatement.append( "CREATE_ON_AC_RMVL_BOOL," );
      lInsertStatement.append( "CREATE_ON_ANY_RMVL_BOOL," );
      lInsertStatement.append( "SOFT_DEADLINE_BOOL," );
      lInsertStatement.append( "HARD_DEAD_BOOL," );
      lInsertStatement.append( "EXT_KEY_SDESC," );
      lInsertStatement.append( "TASK_REV_REASON_DB_ID," );
      lInsertStatement.append( "TASK_REV_REASON_CD," );
      lInsertStatement.append( "REV_NOTE," );
      lInsertStatement.append( "REV_HR_DB_ID," );
      lInsertStatement.append( "REV_HR_ID," );
      lInsertStatement.append( "REV_DT," );
      lInsertStatement.append( "BLOCK_CHAIN_SDESC," );
      lInsertStatement.append( "BLOCK_ORD," );
      lInsertStatement.append( "INITIAL_BLOCK_BOOL," );
      lInsertStatement.append( "ON_CONDITION_BOOL," );
      lInsertStatement.append( "LOCKED_BOOL," );
      lInsertStatement.append( "LOCKED_HR_DB_ID," );
      lInsertStatement.append( "LOCKED_HR_ID," );
      lInsertStatement.append( "LOCKED_DT," );
      lInsertStatement.append( "ETOPS_BOOL," );
      lInsertStatement.append( "RESCHED_FROM_DB_ID," );
      lInsertStatement.append( "RESCHED_FROM_CD," );
      lInsertStatement.append( "ENG_CONTACT_HR_DB_ID," );
      lInsertStatement.append( "ENG_CONTACT_HR_ID," );
      lInsertStatement.append( "APPROVED_BOOL," );
      lInsertStatement.append( "MIN_USAGE_RELEASE_PCT," );
      lInsertStatement.append( "ORG_DB_ID," );
      lInsertStatement.append( "ORG_ID," );
      lInsertStatement.append( "ENFORCE_WORKSCOPE_BOOL," );
      lInsertStatement.append( "TASK_MUST_REMOVE_DB_ID," );
      lInsertStatement.append( "TASK_MUST_REMOVE_CD," );
      lInsertStatement.append( "ALT_ID," );
      lInsertStatement.append( "TASK_SCHED_FROM_DB_ID," );
      lInsertStatement.append( "TASK_SCHED_FROM_CD," );
      lInsertStatement.append( "SCHED_UNKNOWN_BOOL" );
      lInsertStatement.append( " )" );
      lInsertStatement.append( " VALUES ( " );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,?,?,?,?,?," );
      lInsertStatement.append( "?,?,?,?,?,? " );
      lInsertStatement.append( ")" );

      return lInsertStatement.toString();
   }


   /**
    * creates WHERE string to be used for deleting data from a table
    *
    * @param aDataMap
    *           Map of data fields to be used for delete
    *
    * @return string of WHERE filter inputs
    */
   public static String whereFromTableByMap( Map<String, String> aDataMap ) {
      String lColumn = "";
      String lValue = "";
      StringBuilder lInsertStatement = new StringBuilder();
      lInsertStatement.append( " WHERE " );

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         lValue = lEntry.getValue();
         lColumn = lEntry.getKey();
         if ( lValue == null )
            lInsertStatement.append( lColumn + " is " + lValue + " AND " );
         else
            lInsertStatement.append( lColumn + " = " + lValue + " AND " );
      }

      return removeLastAnd( lInsertStatement.toString() );
   }


   /**
    * Remove the last AND from the query string
    *
    * @param lQuery
    *
    * @return String
    */
   public static String removeLastAnd( String lQuery ) {

      String lResult = "";

      int lLength = lQuery.length();
      int lLastChar = lLength - 4;

      if ( ( lLength > 0 ) && ( lQuery.charAt( lLastChar ) == 'A' ) ) {
         lResult = lQuery.substring( 0, lLastChar );
      }

      System.out.println( "String: " + lResult );

      return lResult;
   }


   /**
    * Remove the Last from the string of comma separated values
    *
    * @param lValues
    *
    * @return String
    */
   private static String removeLastComma( String aValues ) {

      String lResult = "";

      int lLength = aValues.length();
      int lLastChar = lLength - 1;

      if ( ( lLength > 0 ) && ( aValues.charAt( lLastChar ) == ',' ) ) {
         lResult = aValues.substring( 0, lLastChar );
      }

      return lResult;
   }


   /**
    * Builds a table function query
    *
    * @param aTableName
    *           the table name
    * @param aFieldNames
    *           list of fields to be selected
    * @param aFieldWhereArgs
    *           the where clause
    *
    * @return the result of query string.
    *
    */

   public static String buildTableQuery( String aTableName, List<String> aFieldNames,
         WhereClause aFieldWhereArgs ) {

      // start constructing SQL query string
      StringBuffer lQueryStatement = new StringBuffer();
      lQueryStatement.append( "SELECT " );

      // Flatten the fields name to be selected to comma delimited
      if ( aFieldNames == null ) {
         lQueryStatement.append( " * " );
      } else {
         lQueryStatement.append(
               StringUtils.toDelimitedString( ", ", aFieldNames ).replaceAll( "^\\[|\\]$", "" ) );
      }

      // get the table function name based on package and class names
      lQueryStatement.append( " FROM " );
      lQueryStatement.append( aTableName + " where " );

      // Add all the Arguments in order
      Boolean lIsFirst = true;
      for ( Entry<String, String> aFieldWhereArg : aFieldWhereArgs.entrySet() ) {

         String lParmField = aFieldWhereArg.getKey();
         String lParmValue = aFieldWhereArg.getValue();

         // will add comma
         if ( !lIsFirst ) {
            lQueryStatement.append( " and " );
         }

         // Append the formatted data
         lQueryStatement.append( lParmField.toUpperCase() + "=\'" + lParmValue + "\'" );
         lIsFirst = false;
      }

      return lQueryStatement.toString();

   }


   /**
    * Builds a table function query
    *
    * @param aTableName
    *           the table name
    * @param aFieldNames
    *           list of fields to be selected
    * @param aFieldWhereArgs
    *           the where clause
    *
    * @return the result of query string.
    *
    */

   public static String buildTableQueryOrderBy( String aTableName, List<String> aFieldNames,
         WhereClause aFieldWhereArgs ) {

      // start constructing SQL query string
      StringBuffer lQueryStatement = new StringBuffer();
      lQueryStatement.append( "SELECT " );

      // Flatten the fields name to be selected to comma delimited
      if ( aFieldNames == null ) {
         lQueryStatement.append( " * " );
      } else {
         lQueryStatement.append(
               StringUtils.toDelimitedString( ", ", aFieldNames ).replaceAll( "^\\[|\\]$", "" ) );
      }

      // get the table function name based on package and class names
      lQueryStatement.append( " FROM " );
      lQueryStatement.append( aTableName + " where " );

      // Add all the Arguments in order
      Boolean lIsFirst = true;
      for ( Entry<String, String> aFieldWhereArg : aFieldWhereArgs.entrySet() ) {

         String lParmField = aFieldWhereArg.getKey();
         String lParmValue = aFieldWhereArg.getValue();

         // will add comma
         if ( !lIsFirst ) {
            lQueryStatement.append( " and " );
         }

         // Append the formatted data
         lQueryStatement.append( lParmField.toUpperCase() + "=\'" + lParmValue + "\'" );
         lIsFirst = false;
      }

      lQueryStatement.append( " ORDER BY creation_dt DESC" );

      return lQueryStatement.toString();

   }
}
