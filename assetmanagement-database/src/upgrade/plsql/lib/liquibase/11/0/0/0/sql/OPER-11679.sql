--liquibase formatted sql

--changeSet OPER-11679:1 stripComments:false 
DELETE FROM
   utl_todo_list_button
WHERE
   todo_button_id = 10024;
   
--changeSet OPER-11679:2 stripComments:false 
DELETE FROM
   utl_todo_button
WHERE
   todo_button_id = 10024;