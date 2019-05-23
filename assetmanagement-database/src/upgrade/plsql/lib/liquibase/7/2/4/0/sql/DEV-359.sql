--liquibase formatted sql


--changeSet DEV-359:1 stripComments:false
INSERT INTO
   UTL_MENU_ITEM
   (
      MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID
   )
   SELECT 120931, NULL, 'web.menuitem.DRAFT_REGULATORY_REPORT', '/mxreport/servlet/report/generate?aTemplate=fault.DraftRegulatoryReport&aViewPDF=true', 0, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 120931 );          