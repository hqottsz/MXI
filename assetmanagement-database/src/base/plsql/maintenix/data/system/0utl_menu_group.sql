--liquibase formatted sql


--changeSet 0utl_menu_group:1 stripComments:false
-- Administration menu groups
/*******************************************
** 0-Level INSERT SCRIPT FOR UTL_MENU_GROUP
********************************************/
INSERT INTO UTL_MENU_GROUP (GROUP_ID, GROUP_NAME, GROUP_ORDER, ROLE_ID, ALL_USERS_BOOL,UTL_ID)
   VALUES(19000, 'Administrator', 1, 19000, 0, 0);

--changeSet 0utl_menu_group:2 stripComments:false
INSERT INTO UTL_MENU_GROUP (GROUP_ID, GROUP_NAME, GROUP_ORDER, ROLE_ID, ALL_USERS_BOOL,UTL_ID)
   VALUES(10106, 'web.menugroup.UTILITY_REPORTS', 2, 19000, 0, 0);  

--changeSet 0utl_menu_group:3 stripComments:false
-- References menu group
INSERT INTO UTL_MENU_GROUP (GROUP_ID, GROUP_NAME, GROUP_ORDER, ROLE_ID, ALL_USERS_BOOL,UTL_ID)
   VALUES(10010, 'web.menugroup.REFERENCES', 99, NULL, 1, 0);