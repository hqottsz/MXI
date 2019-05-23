--liquibase formatted sql


--changeSet 0utl_pb_ref_term:1 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_bitmap', 'Icon', 'bitmap_db_id@@@bitmap_tag', 'd_ref_detail_bitmap', 'd_ref_list_bitmap', 0, null,0);

--changeSet 0utl_pb_ref_term:2 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_country', 'Country', 'country_db_id@@@country_cd', 'd_ref_detail_country', 'd_ref_list_country', 1, null,0);

--changeSet 0utl_pb_ref_term:3 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_data_source', 'Data Source', 'data_source_db_id@@@data_source_cd', 'd_ref_detail_datasource', 'd_ref_list_datasource', 1, 'desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:4 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_data_source_type', 'Data Source Type', 'data_source_type_db_id@@@data_source_type_cd', 'd_ref_detail_datasourcetype', 'd_ref_list_datasourcetype', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap@@@manual_bool',0);

--changeSet 0utl_pb_ref_term:5 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_domain_type', 'Domain Type', 'domain_type_db_id@@@domain_type_cd', 'd_ref_detail_domaintype', 'd_ref_list_domaintype', 0, 'desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:6 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_eng_unit', 'Engineering Unit', 'eng_unit_db_id@@@eng_unit_cd', 'd_ref_detail_engunit', 'd_ref_list_engunit', 1, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:7 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_event_type', 'Event Type', 'event_type_db_id@@@event_type_cd', 'd_ref_detail_eventtype', 'd_ref_list_eventtype', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:8 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fail_catgry', 'Failure Category', 'fail_catgry_db_id@@@fail_catgry_cd', 'd_ref_detail_failcatgry', 'd_ref_list_failcatgry', 1, null,0);

--changeSet 0utl_pb_ref_term:9 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fail_effect_type', 'Failure Effect Type', 'fail_effect_type_db_id@@@fail_effect_type_cd', 'd_ref_detail_faileffecttype', 'd_ref_list_faileffecttype', 1, 'desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:10 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fail_sev', 'Failure Severity', 'fail_sev_db_id@@@fail_sev_cd', 'd_ref_detail_failsev', 'd_ref_list_failsev', 1, 'desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@fail_sev_ord@@@refbutton_compkey_bitmap@@@compkey_sevtype@@@refbutton_compkey_sevtype',0);

--changeSet 0utl_pb_ref_term:11 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_flight_stage', 'Operation Stage', 'flight_stage_db_id@@@flight_stage_cd', 'd_ref_detail_flightstage', 'd_ref_list_flightstage', 1, null,0);

--changeSet 0utl_pb_ref_term:12 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_flight_type', 'Operation Type', 'flight_type_db_id@@@flight_type_cd', 'd_ref_detail_flighttype', 'd_ref_list_flighttype', 1, null,0);

--changeSet 0utl_pb_ref_term:13 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_inv_capability', 'Inventory Capability', 'inv_capability_db_id@@@inv_capability_cd', 'd_ref_detail_invcapability', 'd_ref_list_invcapability', 1, 'inv_capability_cd@@@desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:14 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_inv_class', 'Inventory Class', 'inv_class_db_id@@@inv_class_cd', 'd_ref_detail_invclass', 'd_ref_list_invclass', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:15 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_inv_cond', 'Inventory Condition', 'inv_cond_db_id@@@inv_cond_cd', 'd_ref_detail_invcond', 'd_ref_list_invcond', 0, null,0);

--changeSet 0utl_pb_ref_term:16 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_inv_oper', 'Inventory Operation', 'inv_oper_db_id@@@inv_oper_cd', 'd_ref_detail_invoper', 'd_ref_list_invoper', 0, null,0);

--changeSet 0utl_pb_ref_term:17 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_labour_skill', 'Labour Skill', 'labour_skill_db_id@@@labour_skill_cd', 'd_ref_detail_labourskill', 'd_ref_list_labourskill', 1, 'desc_sdesc@@@est_hourly_cost@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:18 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_loc_type', 'Location Type', 'loc_type_db_id@@@loc_type_cd', 'd_ref_detail_loctype', 'd_ref_list_loctype', 1, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:19 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_pay_method', 'Pay Method', 'pay_method_db_id@@@pay_method_cd', 'd_ref_detail_paymethod', 'd_ref_list_paymethod', 1, null,0);

--changeSet 0utl_pb_ref_term:20 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_prec_proc', 'Precautionary Procedure', 'prec_proc_db_id@@@prec_proc_cd', 'd_ref_detail_precproc', 'd_ref_list_precproc', 1, null,0);

--changeSet 0utl_pb_ref_term:21 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_ref_unit', 'Reference Unit', 'ref_unit_db_id@@@ref_unit_cd', 'd_ref_detail_refunit', 'd_ref_list_refunit', 1, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:22 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_sched_priority', 'Task Priority', 'sched_priority_db_id@@@sched_priority_cd', 'd_ref_detail_schedpriority', 'd_ref_list_schedpriority', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:23 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_sd_nature', 'Incident Nature', 'sd_nature_db_id@@@sd_nature_cd', 'd_ref_detail_sdnature', 'd_ref_list_sdnature', 1, 'sd_nature_cd@@@desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:24 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_sd_type', 'Incident Type', 'sd_type_db_id@@@sd_type_cd', 'd_ref_detail_sdtype', 'd_ref_list_sdtype', 1, null,0);

--changeSet 0utl_pb_ref_term:25 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_state', 'State/Province', 'country_db_id@@@country_cd@@@state_cd', 'd_ref_detail_state', 'd_ref_list_state', 1, null,0);

--changeSet 0utl_pb_ref_term:26 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_task_class', 'Task Class', 'task_class_db_id@@@task_class_cd', 'd_ref_detail_taskclass', 'd_ref_list_taskclass', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap@@@auto_complete_bool@@@unique_bool@@@workscope_bool',0);

--changeSet 0utl_pb_ref_term:27 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_task_def_status', 'Task Definition Status', 'task_def_status_db_id@@@task_def_status_cd', 'd_ref_detail_taskdefstatus', 'd_ref_list_taskdefstatus', 0, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@refbutton_compkey_bitmap',0);

--changeSet 0utl_pb_ref_term:28 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_task_originator', 'Task Originator', 'task_originator_db_id@@@task_originator_cd', 'd_ref_detail_taskoriginator', 'd_ref_list_taskoriginator', 1, null,0);

--changeSet 0utl_pb_ref_term:29 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_task_subclass', 'Task Subclass', 'task_subclass_db_id@@@task_subclass_cd', 'd_ref_detail_tasksubclass', 'd_ref_list_tasksubclass', 1, null,0);

--changeSet 0utl_pb_ref_term:30 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_work_type', 'Work Type', 'work_type_db_id@@@work_type_cd', 'd_ref_detail_worktype', 'd_ref_list_worktype', 1, null,0);

--changeSet 0utl_pb_ref_term:31 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_effect_sev', 'Effect Severity', 'effect_sev_db_id@@@effect_sev_cd', 'd_ref_detail_effectsev', 'd_ref_list_effectsev', 1, 'desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:32 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fail_type', 'Failure Type', 'fail_type_db_id@@@fail_type_cd', 'd_ref_detail_failtype', 'd_ref_list_failtype', 1, 'desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:33 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fail_priority', 'Failure Priority', 'fail_priority_db_id@@@fail_priority_cd', 'd_ref_detail_failpriority', 'd_ref_list_failpriority', 1, 'priority_ord@@@desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:34 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_part_type', 'Part Type', 'part_type_db_id@@@part_type_cd', 'd_ref_detail_parttype', 'd_ref_list_parttype', 1, 'part_type_cd@@@desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:35 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_part_use', 'Part Use', 'part_use_db_id@@@part_use_cd', 'd_ref_detail_partuse', 'd_ref_list_partuse', 0, null,0);

--changeSet 0utl_pb_ref_term:36 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_stage_reason', 'Stage Reason', 'stage_reason_db_id@@@stage_reason_cd', 'd_ref_detail_stagereason', 'd_ref_list_stagereason', 1, 'stage_reason_cd@@@desc_sdesc@@@compkey_eventstatus@@@compkey_bitmap@@@user_reason_cd@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:37 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_data_value', 'Data Value', 'data_value_db_id@@@data_value_cd', 'd_ref_detail_datavalue', 'd_ref_list_datavalue', 1, 'data_value_cd@@@desc_sdesc@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:38 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_ietm_type', 'IETM Type', 'ietm_type_db_id@@@ietm_type_cd', 'd_ref_detail_ietmtype', 'd_ref_list_ietmtype', 1, 'ietm_type_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc',0);

--changeSet 0utl_pb_ref_term:39 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_event_status', 'Event Status', 'event_status_db_id@@@event_status_cd', 'd_ref_detail_eventstatus', 'd_ref_list_eventstatus', 0, null, 0);

--changeSet 0utl_pb_ref_term:40 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_assmbl_class', 'Assembly Class', 'assmbl_class_db_id@@@assmbl_class_cd', 'd_ref_detail_assemblyclass', 'd_ref_list_assemblyclass', 0, null, 0);

--changeSet 0utl_pb_ref_term:41 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_bom_class', 'BOM Class', 'bom_class_db_id@@@bom_class_cd', 'd_ref_detail_bomclass', 'd_ref_list_bomclass', 0, null, 0);

--changeSet 0utl_pb_ref_term:42 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_receive_cond', 'Received Condition', 'receive_cond_db_id@@@receive_cond_cd', 'd_ref_detail_receivecond', 'd_ref_list_receivecond', 1, null, 0);

--changeSet 0utl_pb_ref_term:43 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_remove_reason', 'Remove Reason', 'remove_reason_db_id@@@remove_reason_cd', 'd_ref_detail_removereason', 'd_ref_list_removereason', 1, null, 0);

--changeSet 0utl_pb_ref_term:44 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_xfer_type', 'Transfer Type', 'xfer_type_db_id@@@xfer_type_cd', 'd_ref_detail_xfertype', 'd_ref_list_xfertype', 1, null, 0);

--changeSet 0utl_pb_ref_term:45 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_transport_type', 'Transport Type', 'transport_type_db_id@@@transport_type_cd', 'd_ref_detail_transporttype', 'd_ref_list_transporttype', 1, null, 0);

--changeSet 0utl_pb_ref_term:46 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_size_class', 'Size Class', 'size_class_db_id@@@size_class_cd', 'd_ref_detail_sizeclass', 'd_ref_list_sizeclass', 1, null, 0);

--changeSet 0utl_pb_ref_term:47 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_shipment_reason', 'Shipment Reason', 'shipment_reason_db_id@@@shipment_reason_cd', 'd_ref_detail_shipmentreason', 'd_ref_list_shipmentreason', 1, null, 0);

--changeSet 0utl_pb_ref_term:48 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_shipment_type', 'Shipment Type', 'shipment_type_db_id@@@shipment_type_cd', 'd_ref_detail_shipmenttype', 'd_ref_list_shipmenttype', 1, null, 0);

--changeSet 0utl_pb_ref_term:49 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_req_priority', 'Request Priority', 'req_priority_db_id@@@req_priority_cd', 'd_ref_detail_reqpriority', 'd_ref_list_reqpriority', 1, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:50 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_req_type', 'Request Type', 'req_type_db_id@@@req_type_cd', 'd_ref_detail_reqtype', 'd_ref_list_reqtype', 0, 'desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:51 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_stock_low_actn', 'Stock Low Action', 'stock_low_actn_db_id@@@stock_low_actn_cd', 'd_ref_detail_stocklowactn', 'd_ref_list_stocklowactn', 0, 'desc_sdesc@@@desc_ldesc@@@proc_name_ldesc@@@compkey_bitmap', 0);

--changeSet 0utl_pb_ref_term:52 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_terms_conditions', 'Terms and Conditions', 'terms_conditions_db_id@@@terms_conditions_cd', 'd_ref_detail_termsconditions', 'd_ref_list_termsconditions', 1, 'terms_conditions_cd@@@desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:53 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_po_type', 'Purchase Order Type', 'po_type_db_id@@@po_type_cd', 'd_ref_detail_potype', 'd_ref_list_potype', 0, 'desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:54 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_fob', 'Freight On Board', 'fob_db_id@@@fob_cd', 'd_ref_detail_fob', 'd_ref_list_fob', 1, 'fob_cd@@@desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:55 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_po_auth_lvl', 'Purchase Authority Level', 'po_auth_lvl_db_id@@@po_auth_lvl_cd', 'd_ref_detail_poauthlvl', 'd_ref_list_poauthlvl', 1, 'po_auth_lvl_cd@@@desc_sdesc@@@limit_price@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:56 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_xaction_type', 'Transaction Type', 'xaction_type_db_id@@@xaction_type_cd', 'd_ref_detail_xactiontype', 'd_ref_list_xactiontype', 0, 'desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:57 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_financial_class', 'Financial Class', 'financial_class_db_id@@@financial_class_cd', 'd_ref_detail_financialclass', 'd_ref_list_financialclass', 0, 'desc_sdesc@@@desc_ldesc@@@compkey_bitmap@@@finance_type_cd', 0);

--changeSet 0utl_pb_ref_term:58 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_account_type', 'Account Type', 'account_type_db_id@@@account_type_cd', 'd_ref_detail_accounttype', 'd_ref_list_accounttype', 0, 'desc_sdesc@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:59 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_currency', 'Currency', 'currency_db_id@@@currency_cd', 'd_ref_detail_currency', 'd_ref_list_currency', 1, 'currency_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc@@@default_bool@@@exchg_qt', 0);

--changeSet 0utl_pb_ref_term:60 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_hazmat', 'Hazmat', 'hazmat_db_id@@@hazmat_cd', 'd_ref_detail_hazmat', 'd_ref_list_hazmat', 1, 'hazmat_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:61 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_packaging_instr', 'Packaging Instructions', 'packaging_instr_db_id@@@packaging_instr_cd', 'd_ref_detail_packaginginstr', 'd_ref_list_packaginginstr', 1, 'packaging_instr_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:62 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_shipping_instr', 'Shipping Instructions', 'shipping_instr_db_id@@@shipping_instr_cd', 'd_ref_detail_shippinginstr', 'd_ref_list_shippinginstr', 1, 'shipping_instr_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:63 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_storage_instr', 'Storage Instructions', 'storage_instr_db_id@@@storage_instr_cd', 'd_ref_detail_storageinstr', 'd_ref_list_storageinstr', 1, 'storage_instr_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:64 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_qty_unit', 'Quantity Unit', 'qty_unit_db_id@@@qty_unit_cd', 'd_ref_detail_qtyunit', 'd_ref_list_qtyunit', 1, 'desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:65 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_attach_type', 'Attach Type', 'attach_type_db_id@@@attach_type_cd', 'd_ref_detail_attachtype', 'd_ref_list_attachtype', 1, 'attach_type_cd@@@desc_sdesc@@@compkey_bitmap@@@desc_ldesc', 0);

--changeSet 0utl_pb_ref_term:66 stripComments:false
insert into UTL_PB_REF_TERM (APP_CD, REF_TERM_NAME, REF_TERM_TITLE, PK_COLS, DETAIL_DW, LIST_DW, EDITABLE_BOOL, EDITABLE_COLS, UTL_ID)
values ('BL', 'ref_sev_type', 'Severity Type', 'sev_type_db_id@@@sev_type_cd', 'd_ref_detail_sevtype', 'd_ref_list_sevtype', 1, 'desc_sdesc@@@desc_ldesc', 0);