-- delete old data
DELETE FROM inv_loc WHERE loc_db_id = 777;
DELETE FROM lrp_loc;
delete from REF_LRP_CONFIG_SEV;
-- insert data

-- task defns
INSERT INTO task_defn (TASK_DEFN_DB_ID,TASK_DEFN_ID,LAST_REVISION_ORD,NEW_REVISION_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES (4650,238163,2,0,0,to_date('02-SEP-08','DD-MON-RR'),to_date('04-SEP-08','DD-MON-RR'),4650,'slevert');

-- task tasks
INSERT INTO task_task (TASK_DB_ID, TASK_ID, ACTV_HR_DB_ID, ACTV_HR_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, TASK_PRIORITY_DB_ID, TASK_PRIORITY_CD, ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, REPL_ASSMBL_DB_ID, REPL_ASSMBL_CD, REPL_ASSMBL_BOM_ID, TASK_SUBCLASS_DB_ID, TASK_SUBCLASS_CD, TASK_ORIGINATOR_DB_ID, TASK_ORIGINATOR_CD, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD, BITMAP_DB_ID, BITMAP_TAG, TASK_CD, TASK_NAME, TASK_LDESC, INSTRUCTION_LDESC, ISSUE_ACCOUNT_DB_ID, ISSUE_ACCOUNT_ID, RECURRING_TASK_BOOL, TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, TASK_APPL_LDESC, TASK_APPL_SQL_LDESC, TASK_REF_SDESC, EST_DURATION_QT, EFFECTIVE_DT, EFFECTIVE_GDT, ROUTINE_BOOL, FORECAST_RANGE_QT, MIN_PLAN_YIELD_PCT, RESOURCE_SUM_BOOL, TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, REVISION_ORD, ACTV_DT, ACTV_LDESC, ACTV_REF_SDESC, UNIQUE_BOOL, WORKSCOPE_BOOL, AUTO_COMPLETE_BOOL, LAST_SCHED_DEAD_BOOL, TASK_APPL_EFF_LDESC, ENGINEERING_LDESC, CANCEL_ON_AC_INST_BOOL, CANCEL_ON_ANY_INST_BOOL, CREATE_ON_AC_INST_BOOL, CREATE_ON_ANY_INST_BOOL, SOFT_DEADLINE_BOOL, EXT_KEY_SDESC, TASK_REV_REASON_DB_ID, TASK_REV_REASON_CD, REV_NOTE, REV_HR_DB_ID, REV_HR_ID, REV_DT, BLOCK_CHAIN_SDESC, BLOCK_ORD, INITIAL_BLOCK_BOOL, ON_CONDITION_BOOL, LOCKED_BOOL, LOCKED_HR_DB_ID, LOCKED_HR_ID, LOCKED_DT, PLANNING_TYPE_DB_ID, PLANNING_TYPE_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (4650, 238163, 4650, 6000032, 4650, 238163, 0, 'REQ', 10, 'HIGH', 5000000, 'A320', 0, null, null, null, null, null, 20, 'AD', 0, 'SUPRSEDE', 0, 1, 'REQ-SAL-1', 'Req-Sal-1', null, null, null, null, 0, 0, 'EFFECTIVE_DT', null, null, null, 10, to_date('03-SEP-08','DD-MON-RR'), to_date('03-SEP-08','DD-MON-RR'), 1, 30, 0.03, 0, 0, 'NA', 1, to_date('02-SEP-08', 'DD-MON-RR'), null, null, 1, 0, 1, 0, null, null, 0, 0, 0, 0, 0, null, null, null, null, 4650, 6000032, to_date('02-SEP-08', 'DD-MON-RR'), null, null, 0, 0, 0, null, null, null, 10, 100033, 0, to_date('02-SEP-08', 'DD-MON-RR'), to_date('04-SEP-08', 'DD-MON-RR'), 4650, 'slevert');

INSERT INTO task_task (TASK_DB_ID, TASK_ID, ACTV_HR_DB_ID, ACTV_HR_ID, TASK_DEFN_DB_ID, TASK_DEFN_ID, TASK_CLASS_DB_ID, TASK_CLASS_CD, TASK_PRIORITY_DB_ID, TASK_PRIORITY_CD, ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, REPL_ASSMBL_DB_ID, REPL_ASSMBL_CD, REPL_ASSMBL_BOM_ID, TASK_SUBCLASS_DB_ID, TASK_SUBCLASS_CD, TASK_ORIGINATOR_DB_ID, TASK_ORIGINATOR_CD, TASK_DEF_STATUS_DB_ID, TASK_DEF_STATUS_CD, BITMAP_DB_ID, BITMAP_TAG, TASK_CD, TASK_NAME, TASK_LDESC, INSTRUCTION_LDESC, ISSUE_ACCOUNT_DB_ID, ISSUE_ACCOUNT_ID, RECURRING_TASK_BOOL, TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, TASK_APPL_LDESC, TASK_APPL_SQL_LDESC, TASK_REF_SDESC, EST_DURATION_QT, EFFECTIVE_DT, EFFECTIVE_GDT, ROUTINE_BOOL, FORECAST_RANGE_QT, MIN_PLAN_YIELD_PCT, RESOURCE_SUM_BOOL, TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, REVISION_ORD, ACTV_DT, ACTV_LDESC, ACTV_REF_SDESC, UNIQUE_BOOL, WORKSCOPE_BOOL, AUTO_COMPLETE_BOOL, LAST_SCHED_DEAD_BOOL, TASK_APPL_EFF_LDESC, ENGINEERING_LDESC, CANCEL_ON_AC_INST_BOOL, CANCEL_ON_ANY_INST_BOOL, CREATE_ON_AC_INST_BOOL, CREATE_ON_ANY_INST_BOOL, SOFT_DEADLINE_BOOL, EXT_KEY_SDESC, TASK_REV_REASON_DB_ID, TASK_REV_REASON_CD, REV_NOTE, REV_HR_DB_ID, REV_HR_ID, REV_DT, BLOCK_CHAIN_SDESC, BLOCK_ORD, INITIAL_BLOCK_BOOL, ON_CONDITION_BOOL, LOCKED_BOOL, LOCKED_HR_DB_ID, LOCKED_HR_ID, LOCKED_DT, PLANNING_TYPE_DB_ID, PLANNING_TYPE_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (4650, 238168, 4650, 6000032, 4650, 238163, 0, 'REQ', 10, 'HIGH', 5000000, 'A320', 0, null, null, null, null, null, 20, 'AD', 0, 'ACTV', 0, 1, 'REQ-SAL-1', 'Req-Sal-1', null, null, null, null, 0, 0, 'EFFECTIVE_DT', null, null, null, 10, to_date('10-SEP-08','DD-MON-RR'), to_date('10-SEP-08','DD-MON-RR'), 1, 30, 0.03, 0, 0, 'NA', 2, to_date('04-SEP-08', 'DD-MON-RR'), null, null, 1, 0, 1, 0, null, null, 0, 0, 0, 0, 0, null, null, null, null, 4650, 6000032, to_date('04-SEP-08', 'DD-MON-RR'), null, null, 0, 0, 0, null, null, null, 10, 100033, 0, to_date('04-SEP-08', 'DD-MON-RR'), to_date('04-SEP-08', 'DD-MON-RR'), 4650, 'slevert');

INSERT INTO task_work_type(task_db_id, task_id, work_type_db_id, work_type_cd)
VALUES (4650,238163,10,'ADMIN');

INSERT INTO task_work_type(task_db_id, task_id, work_type_db_id, work_type_cd)
VALUES (4650,238168,10,'ADMIN');

-- forecast model
INSERT INTO fc_model (MODEL_DB_ID, MODEL_ID) VALUES (777, 1);

-- assemblies
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd)
VALUES (777, 'BOEING', 'LRP TEST BOEING ASSEMBLY', 0, 'ACFT');

INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd)
VALUES (777, 'AIRBUS', 'LRP TEST AIRBUS ASSEMBLY', 0, 'ACFT');

INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd)
VALUES (777, 'ASSMBL_1', 'LRP TEST ASSEMBLY', 0, 'ACFT');

-- operators
INSERT INTO org_org ( org_db_id, org_id, org_sdesc )
VALUES (777, 1, 'Test carrier 1');

INSERT INTO org_org ( org_db_id, org_id, org_sdesc )
VALUES (777, 2, 'Test carrier 2');

INSERT INTO org_carrier (carrier_db_id, carrier_id, org_db_id, org_id )
VALUES (777, 1, 777, 1);

INSERT INTO org_carrier (carrier_db_id, carrier_id, org_db_id, org_id )
VALUES (777, 2, 777, 2);

-- inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd)
VALUES (777, 1000, 777, 'BOEING');

-- inventory reg code
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd)
VALUES (777, 1000, 'ACTUAL');

-- locations
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 1, 0, 'REGION', 'CA', 'CANADA', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 2, 0, 'AIRPORT', 'CA/OTT', 'OTTAWA', 777, 1, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 3, 0, 'LINE', 'CA/OTT/LN', 'LINE', 777, 2, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 4, 0, 'TRACK', 'CA/OTT/LN/TRK', 'TRACK', 777, 3, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 5, 0, 'VENLINE', 'CA/OTT/LN/VLINE', 'VENDOR LINE', 777, 4, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (777, 6, 0, 'VENTRK', 'CA/OTT/LN/VLINE/VTRK', 'VENDOR TRACK', 777, 5, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

-- capabilities
INSERT INTO inv_loc_capability (loc_db_id, loc_id, assmbl_db_id, assmbl_cd, work_type_db_id, work_type_cd)
VALUES (777, 6, 777, 'ASSMBL_1', 0, 'TURN');

INSERT INTO lrp_loc_capability (loc_db_id, loc_id, assmbl_db_id, assmbl_cd, work_type_db_id, work_type_cd, lrp_db_id, lrp_id, lrp_priority_db_id, lrp_priority_cd)
VALUES (777, 6, 777, 'ASSMBL_1', 0, 'TURN', 777, 1, 777, 'MED');

-- ref terms for lrp
INSERT INTO ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) VALUES (777, 'HIGH');

INSERT INTO ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) VALUES (777, 'MED');

INSERT INTO ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) VALUES (777, 'LOW');

-- config ref terms
INSERT INTO REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (10, 'CRITICAL', 'Critical', 'Events that are critical', 'FF0000', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
INSERT INTO REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (10, 'IGNORE', 'Ignore', 'Events that can be ignored', 'FFFFFF', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
INSERT INTO REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (10, 'NONCRITC', 'Non-critical', 'Events that are non-critical', 'FFFF00', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
