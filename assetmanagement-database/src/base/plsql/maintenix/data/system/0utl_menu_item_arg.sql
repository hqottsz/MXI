--liquibase formatted sql


--changeSet 0utl_menu_item_arg:1 stripComments:false
-- Tag Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (120926,'aTagDbId',0);

--changeSet 0utl_menu_item_arg:2 stripComments:false
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (120926,'aTagId',0);

--changeSet 0utl_menu_item_arg:3 stripComments:false
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (120926,'aTagKey',0);