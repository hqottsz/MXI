--liquibase formatted sql


--changeSet 0utl_menu_item_arg:1 stripComments:false
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (140009,'aInventoryKey',0);

--changeSet 0utl_menu_item_arg:2 stripComments:false
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (140010,'aInventoryKey',0);

--changeSet 0utl_menu_item_arg:3 stripComments:false
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
   VALUES (140011,'aWorkPackageKey',0);