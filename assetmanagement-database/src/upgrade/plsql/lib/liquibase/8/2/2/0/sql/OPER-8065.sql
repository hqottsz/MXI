--liquibase formatted sql

--changeSet OPER-8065:1 stripComments:false
-- Updates the records in the UTL_TODO_BUTTON table to reference new button ldesc i18n tag for Create Borrow Order button

UPDATE
    utl_todo_button
SET
    todo_button_ldesc = 'web.todobutton.CREATE_BORROW_ORDER_LDESC'
WHERE
    todo_button_id = 10042 AND
    todo_button_ldesc = 'web.todobutton.CREATE_BORROW_ORDER';

--changeSet OPER-8065:2 stripComments:false
-- Updates the records in the UTL_TODO_BUTTON table to reference new button ldesc i18n tag for Create Consignment Order button

UPDATE
    utl_todo_button
SET
    todo_button_ldesc = 'web.todobutton.CREATE_CONSIGN_ORDER_LDESC'
WHERE
    todo_button_id = 10052 AND
    todo_button_ldesc = 'web.todobutton.CREATE_CONSIGN_ORDER';

--changeSet OPER-8065:3 stripComments:false
-- Updates the records in UTL_TODO_BUTTON table to reference new button ldesc i18n tag for Create Shipment button

UPDATE
    utl_todo_button
SET
    todo_button_ldesc = 'web.todobutton.CREATE_SHIPMENT_LDESC'
WHERE
    todo_button_id = 10044 AND
    todo_button_ldesc = 'web.todobutton.CREATE_SHIPMENT';