--liquibase formatted sql
--changeset OPER-8605:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment upgrade planning viewer button

/* Cursor to retreive all roles with planning viewer tab added */
DECLARE
   CURSOR tabs IS
      SELECT utl_todo_list.role_id,
             utl_todo_list.todo_list_id,
             utl_todo_tab.todo_tab_id,
             utl_todo_list.utl_id
      FROM utl_todo_list
      JOIN utl_todo_list_tab ON
         utl_todo_list.todo_list_id = utl_todo_list_tab.todo_list_id
      JOIN utl_todo_tab ON
         utl_todo_list_tab.todo_tab_id = utl_todo_tab.todo_tab_id AND
         utl_todo_tab.Todo_Tab_Name    = 'web.todotab.PLANNING_VIEWER';

     ln_next_btn_order NUMBER;
     ln_pv_btn_id NUMBER;

BEGIN

  ln_pv_btn_id := 10068;

  /* safety clean up of any previously failed attempt at doing the tab->button conversion */
  DELETE FROM utl_todo_list_button WHERE todo_button_id = ln_pv_btn_id;

  /*
  * remove any previous  button configurations that might have failed during a previous attempt;
  * these params are new and only related to planning viewer button
  */
  DELETE FROM utl_action_role_parm
  WHERE parm_name in ('APP_PLANNING_VIEWER', 'ACTION_LOAD_PLANNING_VIEWER', 'ACTION_PLANNINGVIEWER_LOAD_PLANNING_VIEWER');

  FOR tab IN tabs
  LOOP
    /* insert to todo list buttons for roles have the tab added */
    SELECT MAX(button_order)
    INTO ln_next_btn_order
    FROM utl_todo_list_button
    WHERE todo_list_id = tab.todo_list_id;

    INSERT INTO utl_todo_list_button (todo_button_id, todo_list_id, utl_id, button_order)
    VALUES (ln_pv_btn_id, tab.todo_list_id, tab.utl_id, ln_next_btn_order + 1);

    /* insert 'APP_PLANNING_VIEWER' permission to each role */
    INSERT INTO utl_action_role_parm (role_id, parm_name, parm_value, session_auth_bool, utl_id)
    VALUES (tab.role_id, 'APP_PLANNING_VIEWER', 'true', 0, tab.utl_id);

    /* insert 'ACTION_LOAD_PLANNING_VIEWER' permission to each role */
    INSERT INTO utl_action_role_parm (role_id, parm_name, parm_value, session_auth_bool, utl_id)
    VALUES (tab.role_id, 'ACTION_LOAD_PLANNING_VIEWER', 'true', 0, tab.utl_id);

    /* insert 'ACTION_PLANNINGVIEWER_LOAD_PLANNING_VIEWER' permission to each role */
    INSERT INTO utl_action_role_parm (role_id, parm_name, parm_value, session_auth_bool, utl_id)
    VALUES (tab.role_id, 'ACTION_PLANNINGVIEWER_LOAD_PLANNING_VIEWER', 'true', 0, tab.utl_id);

  END LOOP;

  COMMIT;
END;
/

--changeSet OPER-8605:2 stripComments:false
DELETE FROM UTL_TODO_LIST_TAB
   where TODO_TAB_ID = 10047;

--changeSet OPER-8605:3 stripComments:false
DELETE FROM UTL_TODO_TAB
   where TODO_TAB_ID = 10047;