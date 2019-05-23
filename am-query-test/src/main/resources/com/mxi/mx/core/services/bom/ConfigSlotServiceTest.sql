/* The intended structure of this data is as follows:

   Configuration slot                      |  assmbl_bom_id
   ========================================================
   Assembly: B-737                         | 0
   +=== Chapter: 61                        | 1 with next highest = 0
      +=== Section: 61-10                  | 2 with next highest = 1
      +=== Tracked slot: TRK-slot          | 3 with next highest = 1
      +=== Sub-assembly slot: SUBASSY-slot | 4 with next highest = 1
      \=== System to delete: SYS-delete    | 5 with next highest = 1
*/

-- User record
INSERT INTO org_hr (hr_db_id, hr_id, user_id) SELECT 4650, 123, 9999999 FROM dual;

-- Assembly
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'B-737' FROM dual;

-- Assembly's config slot
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd, assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             0,            null,         null,             null,               0,       'ROOT',                     0,             'ACTV',               null,            null,       null,    null,          null,      1,  'BOEING-737',               null,               null,    'Boeing 737',              1,             0,         0,          0, '00000000000000000000000000000000' FROM dual;

-- Configuration slots
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd,           assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             1,            4650,      'B-737',                0,               0,        'SYS',                     0,             'ACTV',               null,            null,       null,    null,          null,      1,          '61',               null,               null, 'Propellers / Propulsion',              1,             0,         0,          0, '00000000000000000000000000000001' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd,      assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             2,            4650,      'B-737',                1,               0,        'SYS',                     0,             'ACTV',               null,            null,       null,    null,          null,      1,       '61-10',               null,               null, 'Propeller Assembly',              1,             0,         0,          0, '00000000000000000000000000000002' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd, assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             3,            4650,      'B-737',                1,               0,        'TRK',                     0,             'ACTV',               null,            null,       null,    null,          null,      1,    'TRK-slot',               null,               null,  'Tracked slot',              1,             0,         0,          0, '00000000000000000000000000000003' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd, assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             4,            4650,      'B-737',                1,               0,    'SUBASSY',                     0,             'ACTV',               null,            null,       null,    null,          null,      1, 'SUBASSY-slot',               null,               null, 'Sub-assembly',              1,             0,         0,          0, '00000000000000000000000000000004' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd, cfg_slot_status_db_id, cfg_slot_status_cd, logcard_form_db_id, logcard_form_cd, ietm_db_id, ietm_id, ietm_topic_id, pos_ct, assmbl_bom_cd, assmbl_bom_func_cd, assmbl_bom_zone_cd, assmbl_bom_name, mandatory_bool, software_bool, rvsm_bool, etops_bool, alt_id)
	SELECT                          4650,   'B-737',             5,            4650,      'B-737',                1,               0,        'SYS',                     0,             'ACTV',               null,            null,       null,    null,          null,      1,  'SYS-delete',               null,               null,     'to delete',              1,             0,         0,          0, '00000000000000000000000000000005' FROM dual;

-- IETM
INSERT INTO ietm_ietm (ietm_db_id, ietm_id, ietm_cd, ietm_name, ietm_ldesc, cmdline_app_ldesc, prefix_ldesc,                             alt_id)
   SELECT                    4650,       1, 'IETM1',      null,       null,              null,         null, '00000000000000000000000000000001' FROM DUAL;
INSERT INTO ietm_ietm (ietm_db_id, ietm_id, ietm_cd, ietm_name, ietm_ldesc, cmdline_app_ldesc, prefix_ldesc,                             alt_id)
   SELECT                    4650,       2, 'IETM2',      null,       null,              null,         null, '00000000000000000000000000000002' FROM DUAL;

-- IETM topic
INSERT INTO ietm_topic (ietm_db_id, ietm_id, ietm_topic_id, topic_sdesc, cmdline_parm_ldesc, print_bool, ietm_type_db_id, ietm_type_cd, desc_ldesc, topic_note, attach_type_db_id, attach_type_cd, attach_blob, attach_filename, attach_content_type, taskdefn_cxt_ldesc, task_cxt_ldesc, appl_eff_ldesc)
   SELECT                     4650,       1,             0,     'topic',               null,       null,            null,         null,       null,       null,              null,           null,        null,            null,                null,               null,           null,           null FROM DUAL;
INSERT INTO ietm_topic (ietm_db_id, ietm_id, ietm_topic_id, topic_sdesc, cmdline_parm_ldesc, print_bool, ietm_type_db_id, ietm_type_cd, desc_ldesc, topic_note, attach_type_db_id, attach_type_cd, attach_blob, attach_filename, attach_content_type, taskdefn_cxt_ldesc, task_cxt_ldesc, appl_eff_ldesc)
   SELECT                     4650,       2,             1,       'DNE',               null,       null,            null,         null,       null,       null,              null,           null,        null,            null,                null,               null,           null,           null FROM DUAL;


-- Attach IETM topic to assembly
INSERT INTO ietm_assmbl (ietm_db_id, ietm_id, assmbl_db_id, assmbl_cd)
   SELECT                      4650,       1,         4650,   'B-737' FROM dual;

-- Logcard form
INSERT INTO ref_logcard_form (logcard_form_db_id, logcard_form_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc)
   SELECT                                   4650,          'CODE',            0,          0,  'Logcard',   'Logcard' FROM dual;

-- Sensitivities
DELETE FROM ref_sensitivity;
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc,      desc_ldesc, ord_id, warning_ldesc, active_bool)
   SELECT                                    'SENS_1',   'SENS_1', 'Sensitivity 1',      0,   'Warning 1',           1  FROM dual;
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc,      desc_ldesc, ord_id, warning_ldesc, active_bool)
   SELECT                                    'SENS_2',   'SENS_2', 'Sensitivity 2',      1,   'Warning 2',           1  FROM dual;

-- Make sensitivities applicable on assembly
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd)
   SELECT                                   4650,   'B-737',            'SENS_1' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd)
   SELECT                                   4650,   'B-737',            'SENS_2' FROM dual;

-- Enable sensitivities on system
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
   SELECT                                       4650,   'B-737',             5,            'SENS_1' FROM dual;
