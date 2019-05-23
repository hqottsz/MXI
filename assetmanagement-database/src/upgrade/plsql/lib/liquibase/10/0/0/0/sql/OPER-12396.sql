--liquibase formatted sql
--changeSet OPER-12396:1 stripComments:false
 INSERT INTO
  utl_action_config_parm
  (
     parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
  )
  SELECT
   'ACTION_LAUNCH_WORK_PACKAGE_LOADER',
   'FALSE',
   0,
   'Permission to launch work package loader',
   'TRUE/FALSE',
   'FALSE',
   1,
   'Maint - Work Packages',
   '8.2-SP3',
   0
FROM
  DUAL
 WHERE
  NOT EXISTS (
     SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_LAUNCH_WORK_PACKAGE_LOADER'
  );

--changeSet OPER-12396:2 stripComments:false
INSERT INTO
   utl_todo_button(
   todo_button_id,
   button_name,
   icon,
   action,
   tooltip,
   parm_name,
   todo_button_ldesc,
   query_path,
   pocket_button_bool,
   utl_id
   )
SELECT
   10073,
   'web.todobutton.WORK_PACKAGE_LOADER_NAME',
   NULL,
   '/../wpl-web/ui/induction/SelectInductionPlans.html',
   'web.todobutton.WORK_PACKAGE_LOADER_TOOLTIP',
   'ACTION_LAUNCH_WORK_PACKAGE_LOADER',
   'web.todobutton.WORK_PACKAGE_LOADER_LDESC',
   NULL,
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT *
   FROM
      utl_todo_button
   WHERE
      todo_button_id = 10073
   );

--changeSet OPER-12396:3 stripComments:false
INSERT INTO
   utl_menu_item(
   menu_id,
   todo_list_id,
   menu_name,
   menu_link_url,
   new_window_bool,
   menu_ldesc,
   repl_approved,
   utl_id
   )
SELECT
   120949,
   NULL,
   'web.menuitem.WORK_PACKAGE_LOADER',
   '/wpl-web/ui/induction/SelectInductionPlans.html',
   0,
   'Work Package Loader',
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT *
   FROM
      utl_menu_item
   WHERE
      menu_id = 120949
   );
