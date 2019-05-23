--liquibase formatted sql


--changeSet MX-4938:1 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ACTIVATE_TASK_DEFINITION';

--changeSet MX-4938:2 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ACTIVATE_TASK_DEFINITION';

--changeSet MX-4938:3 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ACTIVATE_TASK_DEFINITION';

--changeSet MX-4938:4 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ACTIVATE_TASK_DEFINITION';

--changeSet MX-4938:5 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_FAULT_DEFINITION';

--changeSet MX-4938:6 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_FAULT_DEFINITION';

--changeSet MX-4938:7 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_FAULT_DEFINITION';

--changeSet MX-4938:8 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_FAULT_DEFINITION';

--changeSet MX-4938:9 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_FOLLOWING_TASK';

--changeSet MX-4938:10 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_FOLLOWING_TASK';

--changeSet MX-4938:11 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_FOLLOWING_TASK';

--changeSet MX-4938:12 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_FOLLOWING_TASK';

--changeSet MX-4938:13 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_LINK_TO_TASK_DEFINITION';

--changeSet MX-4938:14 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_LINK_TO_TASK_DEFINITION';

--changeSet MX-4938:15 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_LINK_TO_TASK_DEFINITION';

--changeSet MX-4938:16 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_LINK_TO_TASK_DEFINITION';

--changeSet MX-4938:17 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:18 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:19 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:20 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:21 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_PART_TRANSFORMATION';

--changeSet MX-4938:22 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_PART_TRANSFORMATION';

--changeSet MX-4938:23 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_PART_TRANSFORMATION';

--changeSet MX-4938:24 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_PART_TRANSFORMATION';

--changeSet MX-4938:25 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_SCHEDULING_RULE';

--changeSet MX-4938:26 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_SCHEDULING_RULE';

--changeSet MX-4938:27 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_SCHEDULING_RULE';

--changeSet MX-4938:28 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_SCHEDULING_RULE';

--changeSet MX-4938:29 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:30 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:31 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:32 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:33 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:34 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:35 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:36 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:37 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:38 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:39 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:40 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:41 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PANEL';

--changeSet MX-4938:42 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PANEL';

--changeSet MX-4938:43 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PANEL';

--changeSet MX-4938:44 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PANEL';

--changeSet MX-4938:45 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:46 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:47 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:48 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:49 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_STEP';

--changeSet MX-4938:50 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_STEP';

--changeSet MX-4938:51 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_STEP';

--changeSet MX-4938:52 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_STEP';

--changeSet MX-4938:53 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:54 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:55 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:56 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:57 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ZONE';

--changeSet MX-4938:58 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ZONE';

--changeSet MX-4938:59 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ZONE';

--changeSet MX-4938:60 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_TASK_DEFINITION_ZONE';

--changeSet MX-4938:61 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_ATTACHMENT_ORDER';

--changeSet MX-4938:62 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_ATTACHMENT_ORDER';

--changeSet MX-4938:63 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_ATTACHMENT_ORDER';

--changeSet MX-4938:64 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_ATTACHMENT_ORDER';

--changeSet MX-4938:65 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_MEASUREMENT_ORDER';

--changeSet MX-4938:66 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_MEASUREMENT_ORDER';

--changeSet MX-4938:67 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_MEASUREMENT_ORDER';

--changeSet MX-4938:68 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_MEASUREMENT_ORDER';

--changeSet MX-4938:69 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_STEP_ORDER';

--changeSet MX-4938:70 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_STEP_ORDER';

--changeSet MX-4938:71 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_STEP_ORDER';

--changeSet MX-4938:72 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CHANGE_TASK_DEFINITION_STEP_ORDER';

--changeSet MX-4938:73 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CHANGE_TECHNICAL_REFERENCE_ORDER';

--changeSet MX-4938:74 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CHANGE_TECHNICAL_REFERENCE_ORDER';

--changeSet MX-4938:75 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CHANGE_TECHNICAL_REFERENCE_ORDER';

--changeSet MX-4938:76 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CHANGE_TECHNICAL_REFERENCE_ORDER';

--changeSet MX-4938:77 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CREATE_REVISION';

--changeSet MX-4938:78 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CREATE_REVISION';

--changeSet MX-4938:79 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_REVISION';

--changeSet MX-4938:80 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_REVISION';

--changeSet MX-4938:81 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CREATE_TASK_DEFINITION';

--changeSet MX-4938:82 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CREATE_TASK_DEFINITION';

--changeSet MX-4938:83 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_TASK_DEFINITION';

--changeSet MX-4938:84 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_TASK_DEFINITION';

--changeSet MX-4938:85 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_DELETE_TASK_DEFINITION';

--changeSet MX-4938:86 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_DELETE_TASK_DEFINITION';

--changeSet MX-4938:87 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_DELETE_TASK_DEFINITION';

--changeSet MX-4938:88 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_DELETE_TASK_DEFINITION';

--changeSet MX-4938:89 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_DUPLICATE_TASK_DEFINITION';

--changeSet MX-4938:90 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_DUPLICATE_TASK_DEFINITION';

--changeSet MX-4938:91 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_DUPLICATE_TASK_DEFINITION';

--changeSet MX-4938:92 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_DUPLICATE_TASK_DEFINITION';

--changeSet MX-4938:93 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:94 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:95 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:96 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:97 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_DETAILS';

--changeSet MX-4938:98 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_DETAILS';

--changeSet MX-4938:99 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_DETAILS';

--changeSet MX-4938:100 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_DETAILS';

--changeSet MX-4938:101 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_RULES';

--changeSet MX-4938:102 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_RULES';

--changeSet MX-4938:103 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_RULES';

--changeSet MX-4938:104 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_SCHEDULING_RULES';

--changeSet MX-4938:105 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_APPLICABILITY';

--changeSet MX-4938:106 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_APPLICABILITY';

--changeSet MX-4938:107 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_APPLICABILITY';

--changeSet MX-4938:108 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_APPLICABILITY';

--changeSet MX-4938:109 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_DETAILS';

--changeSet MX-4938:110 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_DETAILS';

--changeSet MX-4938:111 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_DETAILS';

--changeSet MX-4938:112 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_DETAILS';

--changeSet MX-4938:113 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:114 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:115 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:116 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:117 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:118 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:119 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:120 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:121 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_STEP';

--changeSet MX-4938:122 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_STEP';

--changeSet MX-4938:123 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_STEP';

--changeSet MX-4938:124 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_STEP';

--changeSet MX-4938:125 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:126 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:127 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:128 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:129 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_MOVE_TASK_DEFINITION';

--changeSet MX-4938:130 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_MOVE_TASK_DEFINITION';

--changeSet MX-4938:131 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_MOVE_TASK_DEFINITION';

--changeSet MX-4938:132 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_MOVE_TASK_DEFINITION';

--changeSet MX-4938:133 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_OBSOLETE_TASK_DEFINITION';

--changeSet MX-4938:134 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_OBSOLETE_TASK_DEFINITION';

--changeSet MX-4938:135 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_OBSOLETE_TASK_DEFINITION';

--changeSet MX-4938:136 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_OBSOLETE_TASK_DEFINITION';

--changeSet MX-4938:137 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_DEFINITION';

--changeSet MX-4938:138 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_DEFINITION';

--changeSet MX-4938:139 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_DEFINITION';

--changeSet MX-4938:140 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_DEFINITION';

--changeSet MX-4938:141 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_FOLLOWING_TASK';

--changeSet MX-4938:142 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_FOLLOWING_TASK';

--changeSet MX-4938:143 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_FOLLOWING_TASK';

--changeSet MX-4938:144 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_FOLLOWING_TASK';

--changeSet MX-4938:145 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_LINK_FROM_TASK_DEFINITION';

--changeSet MX-4938:146 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_LINK_FROM_TASK_DEFINITION';

--changeSet MX-4938:147 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_LINK_FROM_TASK_DEFINITION';

--changeSet MX-4938:148 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_LINK_FROM_TASK_DEFINITION';

--changeSet MX-4938:149 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:150 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:151 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:152 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_PART_SPECIFIC_SCHEDULING_RULE';

--changeSet MX-4938:153 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_PART_TRANSFORMATION';

--changeSet MX-4938:154 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_PART_TRANSFORMATION';

--changeSet MX-4938:155 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_PART_TRANSFORMATION';

--changeSet MX-4938:156 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_PART_TRANSFORMATION';

--changeSet MX-4938:157 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_SCHEDULING_RULE';

--changeSet MX-4938:158 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_SCHEDULING_RULE';

--changeSet MX-4938:159 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_SCHEDULING_RULE';

--changeSet MX-4938:160 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_SCHEDULING_RULE';

--changeSet MX-4938:161 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:162 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:163 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:164 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ATTACHMENT';

--changeSet MX-4938:165 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:166 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:167 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:168 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_LABOUR_REQUIREMENT';

--changeSet MX-4938:169 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:170 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:171 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:172 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_MEASUREMENT';

--changeSet MX-4938:173 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PANEL';

--changeSet MX-4938:174 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PANEL';

--changeSet MX-4938:175 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PANEL';

--changeSet MX-4938:176 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PANEL';

--changeSet MX-4938:177 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:178 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:179 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:180 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_PART_REQUIREMENT';

--changeSet MX-4938:181 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_STEP';

--changeSet MX-4938:182 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_STEP';

--changeSet MX-4938:183 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_STEP';

--changeSet MX-4938:184 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_STEP';

--changeSet MX-4938:185 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:186 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:187 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:188 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_TOOL_REQUIREMENT';

--changeSet MX-4938:189 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ZONE';

--changeSet MX-4938:190 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ZONE';

--changeSet MX-4938:191 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ZONE';

--changeSet MX-4938:192 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TASK_DEFINITION_ZONE';

--changeSet MX-4938:193 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_REMOVE_TECHNICAL_REFERENCE';

--changeSet MX-4938:194 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_REMOVE_TECHNICAL_REFERENCE';

--changeSet MX-4938:195 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_TECHNICAL_REFERENCE';

--changeSet MX-4938:196 stripComments:false
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_TECHNICAL_REFERENCE';