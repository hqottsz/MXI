--liquibase formatted sql

--changeSet OPER-30030:1 stripComments:false
DELETE FROM utl_todo_list_tab WHERE TODO_TAB_ID = 10002;

--changeSet OPER-30030:2 stripComments:false
DELETE FROM utl_todo_tab WHERE TODO_TAB_ID = 10002;
