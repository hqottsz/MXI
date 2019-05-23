-- JOB VIEWER MENU ITEM -------------------------------------------------------------------------------------------
-- the job viewer is not a default menu item
insert into utl_menu_item ( menu_id, menu_name, menu_link_url, new_window_bool, repl_approved, utl_id )
select 13005, 'Job Viewer', '/maintenix/common/job/JobViewer.jsp', 0, 0, 0
from dual
where not exists (
   select 1 from utl_menu_item where menu_id = 13005
   );

-- CONFIGURATOR MENU ITEM -------------------------------------------------------------------------------------------
-- the following information is all needed in order for the configurator to work
insert into utl_menu_item ( menu_id, menu_name, menu_link_url, new_window_bool, repl_approved, utl_id )
select 14000, 'Configurator', '/maintenix/web/configutils/ConfiguratorMain.jsp', 0, 0, 0
from dual
where not exists (
   select 1 from utl_menu_item where menu_id = 13005
   );

-- ADMIN ROLE -------------------------------------------------------------------------------------------
-- create the Administrator To Do List
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID)
   SELECT 10, 19000, 'To Do List', 'Administrator', null, null, 4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST where TODO_LIST_ID = 10
   );

insert into UTL_TODO_LIST_BUTTON (TODO_BUTTON_ID, TODO_LIST_ID, BUTTON_ORDER, UTL_ID )
   SELECT 10020, 10016, 1, 4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_BUTTON where TODO_BUTTON_ID = 10020
   );

insert into UTL_TODO_LIST_BUTTON (TODO_BUTTON_ID, TODO_LIST_ID, BUTTON_ORDER, UTL_ID )
   SELECT 10018, 10016, 2, 4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_BUTTON where TODO_BUTTON_ID = 10018
   );

INSERT INTO utl_menu_item ( menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, menu_ldesc, repl_approved, utl_id )
   SELECT 13006, 10016, 'To Do List', '/maintenix/common/ToDoList.jsp', 0, null, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 13006
   );

-- create the administrator menu
DELETE FROM utl_menu_group_item WHERE group_id = 19000;

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 13006, 1, 1, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 13006
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 10114, 2, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 10114
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 10113, 3, 1, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 10113
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 10138, 4, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 10138
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 13005, 6, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 13005
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 13002, 8, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 13002
   );

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 13003, 7, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 13003
   );
   
insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 19000, 13000, 9, 0, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 19000 AND MENU_ID = 13000
   );

-- LRP ROLE -------------------------------------------------------------------------------------------

-- LRPPLAN (10029) role already exist as part of 10 level data
insert into UTL_TODO_LIST (TODO_LIST_ID, ROLE_ID, TITLE_NAME, CONTEXT_NAME, TODO_LIST_LDESC, TODO_LIST_ORDER, UTL_ID )
   SELECT 10033,10029,'To Do List','Long Range Planner','','',4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST where TODO_LIST_ID = 10033
   );

insert into utl_menu_group (GROUP_ID,GROUP_NAME,GROUP_ORDER,ROLE_ID,ALL_USERS_BOOL,UTL_ID)
   SELECT 10026,'Long Range Planner',1,10029,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_group where GROUP_ID = 10026
   );

-- LRP menu details
insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10102,10033,'web.menuitem.TASK_SEARCH','/maintenix/web/task/TaskSearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10102
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)   
   SELECT 10103,10033,'web.menuitem.INVENTORY_SEARCH','/maintenix/web/inventory/InventorySearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10103
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)   
	SELECT 10104,10033,'web.menuitem.PART_SEARCH','/maintenix/web/part/PartSearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10104
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10107,10033,'web.menuitem.MANUFACTURER_SEARCH','/maintenix/web/manufacturer/ManufacturerSearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10107
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10112,10033,'web.menuitem.LOCATION_SEARCH','/maintenix/web/location/LocationSearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10112
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10121,10033,'web.menuitem.TASK_DEFINITION_SEARCH','/maintenix/web/taskdefn/TaskDefinitionSearch.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10121
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10127,10033,'web.menuitem.INVENTORY_SEARCH_BY_TYPE','/maintenix/web/inventory/InventorySearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10127
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10128,10033,'web.menuitem.TASK_SEARCH_BY_TYPE','/maintenix/web/task/TaskSearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10128
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10139,10033,'web.menuitem.BLOCK_SEARCH_BY_TYPE','/maintenix/web/taskdefn/BlockSearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10139
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10140,10033,'web.menuitem.REQ_SEARCH_BY_TYPE','/maintenix/web/taskdefn/ReqSearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10140
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10143,10033,'web.menuitem.PART_SEARCH_BY_TYPE','/maintenix/web/part/PartSearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10143
   );

insert into UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
	SELECT 10146,10033,'web.menuitem.FC_MODEL_SEARCH_BY_TYPE','/maintenix/web/forecast/FcModelSearchByType.jsp',0,'',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_item where menu_id = 10146
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10139,2,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10139
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10146,3,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10146
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10103,4,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10103
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10127,5,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10127
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10107,7,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10107
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10104,8,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10104
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10143,9,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10143
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10140,10,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10140
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10121,11,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10121
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10102,12,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10102
   );

insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10128,13,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10128
   );

-- location
insert into utl_menu_group_item (GROUP_ID,MENU_ID,MENU_ORDER,BREAK_BOOL,UTL_ID)
   SELECT 10026,10112,14,0,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10026 AND MENU_ID = 10112
   );

insert into UTL_TODO_BUTTON (TODO_BUTTON_ID,BUTTON_NAME,ICON,ACTION,TOOLTIP,PARM_NAME,TODO_BUTTON_LDESC,QUERY_PATH,POCKET_BUTTON_BOOL,UTL_ID)
   SELECT 10055,'web.todobutton.LAUNCH_LRP_NAME','/common/images/tracker/lrp_launch.gif','/web/lrp/LongRangePlannerJNLP.jsp','web.todobutton.LAUNCH_LRP_TOOLTIP','APP_LONG_RANGE_PLANNER','web.todobutton.LAUNCH_LRP_LDESC','',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_BUTTON where TODO_BUTTON_ID = 10055
   );

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   SELECT 10035,10033,2,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_TAB where TODO_TAB_ID = 10035 AND TODO_LIST_ID = 100005
   );

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   SELECT 10000,10033,1,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_TAB where TODO_TAB_ID = 10000 AND TODO_LIST_ID = 100005
   );

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   SELECT 11022,10033,5,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_TAB where TODO_TAB_ID = 11022 AND TODO_LIST_ID = 100005
   );
   
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   SELECT 10027,10015,20,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_TAB where TODO_TAB_ID = 10027 AND TODO_LIST_ID = 10015
   );   

insert into UTL_TODO_TAB (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID,REFRESH_INTERVAL)
   SELECT 10000,'idTabFleetList','web.todotab.FLEET_LIST','/web/todolist/FleetListTab.jsp','web.todotab.FLEET_LIST_TAB',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_TAB where TODO_TAB_ID = 10000
   );

insert into UTL_TODO_TAB (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID,REFRESH_INTERVAL)
   SELECT 10035,'idTabAssemblyList','web.todotab.ASSEMBLY_LIST','/web/todolist/AssemblyListTab.jsp','web.todotab.ASSEMBLY_LIST_TAB',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_TAB where TODO_TAB_ID = 10035
   );

insert into UTL_TODO_TAB (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID,REFRESH_INTERVAL)
   SELECT 10051,'idTabFcModelList','web.todotab.FORECAST_MODELS','/web/todolist/ForecastModelListTab.jsp','web.todotab.FORECAST_MODEL_LIST_TAB',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_TAB where TODO_TAB_ID = 10051
   );

insert into UTL_TODO_TAB (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID,REFRESH_INTERVAL)
   SELECT 11015,'idTabExtractionRuleList','web.todotab.EXTRACTION_RULES','/web/todolist/ExtractionRuleList.jsp','web.todotab.EXTRACTION_RULES',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_TAB where TODO_TAB_ID = 11015
   );

insert into UTL_TODO_TAB (TODO_TAB_ID,TODO_TAB_CD,TODO_TAB_NAME,PATH,TODO_TAB_LDESC,UTL_ID,REFRESH_INTERVAL)
   SELECT 11022,'idTabPlanningTypes','web.todotab.PLANNING_TYPES','/web/todolist/PlanningTypesTab.jsp','web.todotab.PLANNING_TYPES_TAB',0,0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_TAB where TODO_TAB_ID = 11022
   );

INSERT INTO utl_action_role_parm ( role_id, parm_name, parm_value, session_auth_bool, utl_id )
SELECT
   10029,
   parm_name,
   'true',
   0,
   0
FROM
   utl_action_config_parm
WHERE
   CATEGORY = 'Maint - Long Range Planning'
   AND
   NOT EXISTS (
                 SELECT
                    1
                 FROM
                    utl_action_role_parm
                 WHERE
                    utl_action_role_parm.role_id = 10029 AND
                    utl_action_role_parm.parm_name = utl_action_config_parm.parm_name
              );


-- FINANCE ROLE -------------------------------------------------------------------------------------------
-- create Finance menu

insert into utl_menu_group (GROUP_ID,GROUP_NAME,GROUP_ORDER,ROLE_ID,ALL_USERS_BOOL,UTL_ID)
   SELECT 10051,'Finance',1,10025,0,10
   FROM DUAL
   WHERE not exists (
      select 1 from utl_menu_group where GROUP_ID = 10025
   );

-- add items to the finance menu

insert into UTL_MENU_GROUP_ITEM ( GROUP_ID, MENU_ID, MENU_ORDER, BREAK_BOOL, UTL_ID )
   SELECT 10051, 10125, 1, 1, 0
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_MENU_GROUP_ITEM where GROUP_ID = 10025 AND MENU_ID = 10125
   );



-- TECH RECORDS ROLE -------------------------------------------------------------------------------------------
-- To Do List Updates

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   SELECT 10000,10011,6,4650
   FROM DUAL
   WHERE not exists (
      select 1 from UTL_TODO_LIST_TAB where TODO_TAB_ID = 10000 AND TODO_LIST_ID = 10011
   );



-- MXINTEGRATION USER -------------------------------------------------------------------------------------------
      
INSERT INTO utl_action_user_parm ( user_id, parm_name, parm_value, session_auth_bool, utl_id )
SELECT 15,'API_PART_REQUEST_REQUEST','true',0,0
FROM DUAL
WHERE not exists(
      SELECT 1 FROM utl_action_user_parm WHERE user_id = 15 and parm_name = 'API_PART_REQUEST_REQUEST');
      
INSERT INTO utl_user_role ( user_id, role_id, role_order, utl_id )
SELECT 15,19000,1,4650
FROM DUAL
WHERE not exists(
      SELECT 1 FROM utl_user_role WHERE user_id = 15 and role_id = 19000);