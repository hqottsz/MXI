--liquibase formatted sql


--changeSet QC-4467:1 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'sProdPlanSearchCode' AND
      utl_user_parm.parm_type = 'SESSION';

--changeSet QC-4467:2 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'sProdPlanSearchCode' AND
      utl_role_parm.parm_type = 'SESSION';

--changeSet QC-4467:3 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'sProdPlanSearchCode' AND
      utl_config_parm.parm_type = 'SESSION';      

--changeSet QC-4467:4 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'sProdPlanSearchAssyCd' AND
      utl_user_parm.parm_type = 'SESSION';

--changeSet QC-4467:5 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'sProdPlanSearchAssyCd' AND
      utl_role_parm.parm_type = 'SESSION';

--changeSet QC-4467:6 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'sProdPlanSearchAssyCd' AND
      utl_config_parm.parm_type = 'SESSION';

--changeSet QC-4467:7 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'sProdPlanSearchStatusCd' AND
      utl_user_parm.parm_type = 'SESSION';

--changeSet QC-4467:8 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'sProdPlanSearchStatusCd' AND
      utl_role_parm.parm_type = 'SESSION';

--changeSet QC-4467:9 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'sProdPlanSearchStatusCd' AND
      utl_config_parm.parm_type = 'SESSION';

--changeSet QC-4467:10 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'sProdPlanSearchWorkTypeCd' AND
      utl_user_parm.parm_type = 'SESSION';

--changeSet QC-4467:11 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'sProdPlanSearchWorkTypeCd' AND
      utl_role_parm.parm_type = 'SESSION';

--changeSet QC-4467:12 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'sProdPlanSearchWorkTypeCd' AND
      utl_config_parm.parm_type = 'SESSION';

--changeSet QC-4467:13 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'sProdPlanSearchMethod' AND
      utl_user_parm.parm_type = 'SESSION';

--changeSet QC-4467:14 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'sProdPlanSearchMethod' AND
      utl_role_parm.parm_type = 'SESSION';

--changeSet QC-4467:15 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'sProdPlanSearchMethod' AND
      utl_config_parm.parm_type = 'SESSION';      

--changeSet QC-4467:16 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_EDIT_PHASE_PRIORITY_ORDER' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:17 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_EDIT_PHASE_PRIORITY_ORDER' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:18 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_EDIT_PHASE_PRIORITY_ORDER' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:19 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_EDIT_PHASE_PRIORITY_ORDER' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:20 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_REMOVE_PHASES' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:21 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_REMOVE_PHASES' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:22 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_REMOVE_PHASES' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:23 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_REMOVE_PHASES' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:24 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_EDIT_PHASE_WORK_TYPE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:25 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_EDIT_PHASE_WORK_TYPE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:26 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_EDIT_PHASE_WORK_TYPE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:27 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_EDIT_PHASE_WORK_TYPE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:28 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ADD_WORK_AREA' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:29 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ADD_WORK_AREA' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:30 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ADD_WORK_AREA' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:31 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ADD_WORK_AREA' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:32 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_EDIT_WORK_AREA' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:33 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_EDIT_WORK_AREA' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:34 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_EDIT_WORK_AREA' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:35 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_EDIT_WORK_AREA' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:36 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_REMOVE_WORK_AREA' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:37 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_REMOVE_WORK_AREA' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:38 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_REMOVE_WORK_AREA' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:39 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_REMOVE_WORK_AREA' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:40 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ADD_WORK_AREA_ZONE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:41 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ADD_WORK_AREA_ZONE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:42 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ADD_WORK_AREA_ZONE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:43 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ADD_WORK_AREA_ZONE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:44 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_REMOVE_WORK_AREA_ZONE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:45 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_REMOVE_WORK_AREA_ZONE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:46 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_REMOVE_WORK_AREA_ZONE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:47 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_REMOVE_WORK_AREA_ZONE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:48 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_APPLY_PRODUCTION_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:49 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_APPLY_PRODUCTION_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:50 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_APPLY_PRODUCTION_PLAN' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:51 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_APPLY_PRODUCTION_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:52 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PUBLISH_PROD_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:53 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PUBLISH_PROD_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:54 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PUBLISH_PROD_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:55 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_OPEN_PROD_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:56 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_OPEN_PROD_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:57 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_OPEN_PROD_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:58 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_SAVE_PROD_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:59 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_SAVE_PROD_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:60 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_SAVE_PROD_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:61 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_LOAD_ACTUALS_PROD_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:62 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_LOAD_ACTUALS_PROD_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:63 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_LOAD_ACTUALS_PROD_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:64 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ADD_PHASES' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:65 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ADD_PHASES' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:66 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ADD_PHASES' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:67 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ADD_PHASES' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:68 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_REMOVE_PROD_PLAN_BLOCK' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:69 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_REMOVE_PROD_PLAN_BLOCK' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:70 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_REMOVE_PROD_PLAN_BLOCK' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:71 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_REMOVE_PROD_PLAN_BLOCK' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:72 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ADD_PROD_PLAN_BLOCK' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:73 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ADD_PROD_PLAN_BLOCK' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:74 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ADD_PROD_PLAN_BLOCK' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:75 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ADD_PROD_PLAN_BLOCK' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:76 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_ACTIVATE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:77 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_ACTIVATE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:78 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_ACTIVATE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:79 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_ACTIVATE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:80 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_BUILD' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:81 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_BUILD' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:82 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_BUILD' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:83 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_BUILD' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:84 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_CREATE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:85 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_CREATE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:86 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_CREATE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:87 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_CREATE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:88 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_EDIT' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:89 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_EDIT' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:90 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_EDIT' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:91 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_EDIT' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:92 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_OBSOLETE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:93 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_OBSOLETE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:94 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_OBSOLETE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:95 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_OBSOLETE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:96 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_DUPLICATE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:97 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_DUPLICATE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:98 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_DUPLICATE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:99 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_DUPLICATE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:100 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PROD_PLAN_SETUP' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:101 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PROD_PLAN_SETUP' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:102 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PROD_PLAN_SETUP' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:103 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PROD_PLAN_SETUP' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:104 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_SCHED_PROD_PLAN_SETUP' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:105 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_SCHED_PROD_PLAN_SETUP' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:106 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_SCHED_PROD_PLAN_SETUP' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:107 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_SCHED_PROD_PLAN_SETUP' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:108 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PHASE_TOGGLE_WATCH' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:109 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PHASE_TOGGLE_WATCH' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:110 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PHASE_TOGGLE_WATCH' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:111 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PHASE_TOGGLE_WATCH' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:112 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_PHASE_ASSIGN_TASKS' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:113 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_PHASE_ASSIGN_TASKS' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:114 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_PHASE_ASSIGN_TASKS' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:115 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_PHASE_ASSIGN_TASKS' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:116 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_TASK_SET_PHASE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:117 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_TASK_SET_PHASE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:118 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_TASK_SET_PHASE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:119 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_TASK_SET_PHASE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:120 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_TASK_SET_SCHED_PHASE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:121 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_TASK_SET_SCHED_PHASE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:122 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_TASK_SET_SCHED_PHASE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:123 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_TASK_SET_SCHED_PHASE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:124 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_TASK_TOGGLE_WATCH' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:125 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_TASK_TOGGLE_WATCH' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:126 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_TASK_TOGGLE_WATCH' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:127 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_TASK_TOGGLE_WATCH' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:128 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ASSIGN_MILESTONE' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:129 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ASSIGN_MILESTONE' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:130 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ASSIGN_MILESTONE' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:131 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ASSIGN_MILESTONE' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:132 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_SHOW_NR_TASKS' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:133 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_SHOW_NR_TASKS' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:134 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_SHOW_NR_TASKS' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:135 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_SHOW_NR_TASKS' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:136 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_EDIT_MILESTONE_ORDER' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:137 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_EDIT_MILESTONE_ORDER' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:138 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_EDIT_MILESTONE_ORDER' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:139 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_EDIT_MILESTONE_ORDER' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:140 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_EDIT_MILESTONE_CONDITIONS' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:141 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_EDIT_MILESTONE_CONDITIONS' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:142 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_EDIT_MILESTONE_CONDITIONS' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:143 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_EDIT_MILESTONE_CONDITIONS' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:144 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ADD_MILESTONES' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:145 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ADD_MILESTONES' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:146 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ADD_MILESTONES' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:147 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ADD_MILESTONES' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:148 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_REMOVE_MILESTONES' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:149 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_REMOVE_MILESTONES' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:150 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_REMOVE_MILESTONES' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:151 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_REMOVE_MILESTONES' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:152 stripComments:false
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_VIEW_EDIT_PROJECT_PLAN' AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:153 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_VIEW_EDIT_PROJECT_PLAN' AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet QC-4467:154 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_VIEW_EDIT_PROJECT_PLAN' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';      

--changeSet QC-4467:155 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_VIEW_EDIT_PROJECT_PLAN' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';