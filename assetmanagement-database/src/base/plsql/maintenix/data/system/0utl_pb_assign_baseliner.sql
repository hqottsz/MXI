--liquibase formatted sql


--changeSet 0utl_pb_assign_baseliner:1 stripComments:false
-- ********************************************************
-- MIGRATED UTL_PB_ASSIGN ROWS                           *
-- ********************************************************
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'assign_usageparm', 'Usage Parm Assignment', 'd_assign_usageparm', 'usageparm@@@data_type_db_id@@@data_type_id@@@eng_unit_cd', 'usageparm',0);

--changeSet 0utl_pb_assign_baseliner:2 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'assmbl_usageparm', 'Usage Parm Assignment', 'd_assign_planusageparm', 'data_type_db_id@@@data_type_id@@@data_type_label@@@ref_bitmap_data_type', 'data_type_label',0);

--changeSet 0utl_pb_assign_baseliner:3 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'bom_usageparm', 'Parameter Assignment', 'd_assign_bomusageparms', 'data_type_db_id@@@data_type_id@@@data_type_cd@@@data_type_sdesc@@@ref_bitmap_data_type', 'data_type_label',0);

--changeSet 0utl_pb_assign_baseliner:4 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'fail_mode', 'Fault Definition Assignment', 'd_assign_failmode', 'fail_mode_db_id@@@fail_mode_id', 'label',0);

--changeSet 0utl_pb_assign_baseliner:5 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'faileff_failmode', 'Fault Definition Assignment', 'd_assign_failmode', 'fail_mode_db_id@@@fail_mode_id@@@ref_bitmap_name@@@failmode_label@@@assmbl_bom_cd@@@fail_catgry_cd@@@fail_sev_cd', 'failmode_label',0);

--changeSet 0utl_pb_assign_baseliner:6 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'failure_effect', 'Failure Effect Assignment', 'd_assign_faileffect', 'fail_effect_db_id@@@fail_effect_id@@@ref_bitmap_name@@@faileffect_label@@@fail_effect_type_cd@@@effect_sev_cd', 'faileffect_label',0);

--changeSet 0utl_pb_assign_baseliner:7 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'failure_mode', 'Fault Definition Assignment', 'd_assign_failuremode', 'fail_mode_db_id@@@fail_mode_id@@@label', 'label',0);

--changeSet 0utl_pb_assign_baseliner:8 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'full_manufact', 'Manufacturer Assignment', 'd_assign_fullmanufactlist', 'manufact_db_id@@@manufact_cd@@@manufact_label', 'manufact_label',0);

--changeSet 0utl_pb_assign_baseliner:9 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'full_usageparm', 'Usage Parm Assignment', 'd_assign_fullusageparm', 'data_type_db_id@@@data_type_id@@@data_type_cd@@@data_type_sdesc@@@ref_bitmap_name_datatype', 'data_type_label',0);

--changeSet 0utl_pb_assign_baseliner:10 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'lui_usageparm', 'LUI Parm Assignment ', 'd_assign_luiusageparm', 'data_type_db_id@@@data_type_id@@@data_type_label@@@bitmap_name', 'data_type_label',0);

--changeSet 0utl_pb_assign_baseliner:11 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'part_no_by_bom', 'Part No Assignment', 'd_assign_part_no_by_bom', 'part_no_db_id@@@part_no_id@@@part_no_sdesc@@@part_no_oem@@@manufact_cd@@@manufact_name@@@ref_bitmap_name_part', 'part_no_sdesc',0);

--changeSet 0utl_pb_assign_baseliner:12 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'part_no_by_invclass', 'Part No Assignment', 'd_assign_partlist_by_invclass', 'part_no_db_id@@@part_no_id@@@part_no_sdesc@@@part_no_oem@@@inv_class_cd@@@manufact_cd@@@manufact_name@@@ref_bitmap_name_part', 'part_no_sdesc',0);

--changeSet 0utl_pb_assign_baseliner:13 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'task_definition', 'Task Definition Assignment', 'd_assign_taskdefn', 'task_db_id@@@task_id@@@task_definition@@@task_def_status_cd@@@task_class_cd@@@assmbl_bom_cd', 'task_definition',0);

--changeSet 0utl_pb_assign_baseliner:14 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'task_task', 'Task Definition Assignment', 'd_assign_taskdef', 'task_db_id@@@task_id@@@label@@@task_def_status_cd', 'label',0);

--changeSet 0utl_pb_assign_baseliner:15 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'topic', 'Topic Assignment', 'd_assign_topic', 'ietm_db_id@@@ietm_id@@@ietm_topic_id@@@topic_sdesc@@@cmdline_parm_ldesc', 'topic_sdesc',0);

--changeSet 0utl_pb_assign_baseliner:16 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'part_list', 'Part No Assignment', 'd_assign_part_partlist', 'eqp_part_no_part_no_db_id@@@eqp_part_no_part_no_id@@@eqp_part_no_part_no_sdesc@@@eqp_part_no_part_no_oem@@@eqp_part_no_manufact_cd@@@eqp_manufact_manufact_name@@@ref_bitmap_name_part', 'eqp_part_no_part_no_sdesc',0);

--changeSet 0utl_pb_assign_baseliner:17 stripComments:false
insert into UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
values ('BL', 'bom_part_list', 'BOM Part Assignment', 'd_assign_bom_part', 'bom_part_db_id@@@bom_part_id@@@bom_part_name@@@bom_part_cd@@@inv_class_cd@@@assmbl_db_id@@@assmbl_cd@@@assmbl_bom_id', 'cf_bom_part_name',0);

--changeSet 0utl_pb_assign_baseliner:18 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'assmbl_ietm', 'IETM Assignment', 'd_assign_assmblietm', 'ietm_db_id@@@ietm_id@@@ietm_topic_id@@@cmdline_parm_ldesc@@@topic_sdesc@@@ietm_cd@@@ietm_name', 'ietm_book',0)
;

--changeSet 0utl_pb_assign_baseliner:19 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'assmbl_ietmtopic', 'Assembly IETM Assignment', 'd_assign_assmblietm', 'ietm_db_id@@@ietm_id@@@ietm_topic_id@@@ietm_topic_label', 'ietm_topic_label',0)
;

--changeSet 0utl_pb_assign_baseliner:20 stripComments:false
INSERT INTO UTL_PB_ASSIGN (APP_CD, ASSIGN_NAME, ASSIGN_TITLE, ASSIGN_DW, ASSIGN_COLS, SEARCH_COL, UTL_ID)
VALUES('BL', 'fault_suppression', 'Fault Definition Suppression Assignment', 'd_assign_faultdefnsuppression', 'fail_mode_db_id@@@fail_mode_id@@@fault_defn', 'fault_defn',0)
;

--changeSet 0utl_pb_assign_baseliner:21 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'part_ietm', 'Part IETM Assignment', 'd_assign_partietm', 'ietm_db_id@@@ietm_id@@@ietm_topic_id@@@topic_sdesc', 'ietm_topic_label',0)
;

--changeSet 0utl_pb_assign_baseliner:22 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'flight_measure', 'Parameter Assignment', 'd_assign_flightmeasures', 'data_type_db_id@@@data_type_id@@@label@@@eng_unit_cd', 'label',0)
;

--changeSet 0utl_pb_assign_baseliner:23 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'data_value', 'Data Value Assignment', 'd_assign_datavalue', 'data_value_db_id@@@data_value_cd@@@label@@@compkey_datavalue', 'label',0)
;

--changeSet 0utl_pb_assign_baseliner:24 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'bom_ietm', 'IETM Assignment', 'd_assign_assmblietm', 'ietm_db_id@@@ietm_id@@@ietm_topic_id@@@topic_sdesc', 'ietm_book',0)
;

--changeSet 0utl_pb_assign_baseliner:25 stripComments:false
INSERT INTO UTL_PB_ASSIGN ("APP_CD", "ASSIGN_NAME", "ASSIGN_TITLE", "ASSIGN_DW", "ASSIGN_COLS", "SEARCH_COL", UTL_ID )
VALUES ('BL', 'eqp_position', 'Position Assignment', 'd_assign_bltask_position', 'assmbl_pos_id@@@position', 'position',0)
;