--liquibase formatted sql

--changeSet OPER-8065-2:1 stripComments:false
-- Updates the records in the UTL_TODO_BUTTON to reference existing button ldesc i18n tag for Create Exchange Order button

UPDATE
    utl_todo_button
SET
    todo_button_ldesc   = 'web.todobutton.CREATE_EXCHANGE_ORDER_LDESC'
WHERE
    todo_button_id      = 10041 AND
    todo_button_ldesc   = 'web.todobutton.CREATE_EXCH_ORDER';
