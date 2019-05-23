--liquibase formatted sql


--changeSet 0utl_menu_group_item:1 stripComments:false
-- User Details
/************************************************
** 0-LEVEL INSERT SCRIPT FOR UTL_MENU_GROUP_ITEM
*************************************************/
/***********************
** References (10010)
************************/
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10903, 10010, 1, 1, 0);

--changeSet 0utl_menu_group_item:2 stripComments:false
-- Mxi Website
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10901, 10010, 3, 0, 0);

--changeSet 0utl_menu_group_item:3 stripComments:false
-- Department Member List
/***********************
** Utility Reports (10106)
************************/
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11006, 10106, 1, 0, 0);

--changeSet 0utl_menu_group_item:4 stripComments:false
-- Function Access
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11001, 10106, 2, 0, 0);

--changeSet 0utl_menu_group_item:5 stripComments:false
-- Function Access Role
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11002, 10106, 3, 0, 0);

--changeSet 0utl_menu_group_item:6 stripComments:false
-- System Information
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11003, 10106, 4, 0, 0);

--changeSet 0utl_menu_group_item:7 stripComments:false
-- UI and Logic Settings
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11004, 10106, 5, 0, 0);

--changeSet 0utl_menu_group_item:8 stripComments:false
-- User Action
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11008, 10106, 6, 0, 0);

--changeSet 0utl_menu_group_item:9 stripComments:false
-- User Department List
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11007, 10106, 7, 0, 0);

--changeSet 0utl_menu_group_item:10 stripComments:false
-- User Role
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(11005, 10106, 8, 0, 0);

--changeSet 0utl_menu_group_item:11 stripComments:false
-- User Search 
/***********************
** Administration (19000)
************************/
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10114, 19000, 1, 0, 0);

--changeSet 0utl_menu_group_item:12 stripComments:false
-- Role Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10113, 19000, 2, 0, 0);

--changeSet 0utl_menu_group_item:13 stripComments:false
-- Department Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10115, 19000, 3, 0, 0);

--changeSet 0utl_menu_group_item:14 stripComments:false
-- Authority Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10116, 19000, 4, 0, 0);

--changeSet 0utl_menu_group_item:15 stripComments:false
-- Location Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10112, 19000, 5, 0, 0);

--changeSet 0utl_menu_group_item:16 stripComments:false
-- Owner Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10105, 19000, 6, 0, 0);

--changeSet 0utl_menu_group_item:17 stripComments:false
-- Role Security Search
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(13002, 19000, 7, 0, 0);

--changeSet 0utl_menu_group_item:18 stripComments:false
-- Perm Security Editor
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(13003, 19000, 8, 0, 0);

--changeSet 0utl_menu_group_item:19 stripComments:false
-- Alert Setup
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10138, 19000, 9, 0, 0);

--changeSet 0utl_menu_group_item:20 stripComments:false
-- Search License
insert into UTL_MENU_GROUP_ITEM (MENU_ID, GROUP_ID, MENU_ORDER, BREAK_BOOL, UTL_ID)
values(10144, 19000, 10, 0, 0);