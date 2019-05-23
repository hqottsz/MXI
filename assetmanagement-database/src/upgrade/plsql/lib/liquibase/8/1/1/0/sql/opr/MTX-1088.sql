--liquibase formatted sql


--changeSet MTX-1088:1 stripComments:false
-- Manage AD Status button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
SELECT 10065, NULL, 'web.todobutton.MANAGE_AD_STATUS', NULL, '/../opr/ui/maint/exe/workpackage/ManageADStatusOptions.html', 'web.todobutton.MANAGE_AD_STATUS_TOOLTIP', 'web.todobutton.MANAGE_AD_STATUS_LDESC', 0
FROM dual
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10065
);

--changeSet MTX-1088:2 stripComments:false
-- Manage SB Status button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
SELECT 10066, NULL, 'web.todobutton.MANAGE_SB_STATUS', NULL, '/../opr/ui/maint/exe/workpackage/ManageSBStatusOptions.html', 'web.todobutton.MANAGE_SB_STATUS_TOOLTIP', 'web.todobutton.MANAGE_SB_STATUS_LDESC', 0
FROM dual
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10066
);