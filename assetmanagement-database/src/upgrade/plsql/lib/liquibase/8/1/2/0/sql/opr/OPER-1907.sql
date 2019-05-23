--liquibase formatted sql


--changeSet OPER-1907:1 stripComments:false
-- Zero level data in UTL_MENU_ITEM uses menu_ids that are less than 200000 to
-- keep the zero level data from coliding with customer data.  The OPER-641.sql
-- upgrade script mistakenly created menu_id 5140016 when it should have been 
-- created as menu_id 140016.  Therefore, we need to migrate menu_id 5140016 to
-- menu_id 140016.
-- Insert the UTL_MENU_ITEM 140016 in case it was previously inserted as 5140016
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140016,NULL,'MTBUR Comparison', '/maintenix/servlet/report/generate?aTemplate=Reliability.RELIABILITYMTBURComparison'||chr(38)||'aViewPDF=true', 1,'Reliability Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140016);

--changeSet OPER-1907:2 stripComments:false
-- Update UTL_MENU_ITEM 140016 to the values from 5140016 if it exists
UPDATE 
   UTL_MENU_ITEM
SET 
   (TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID) =
      (
         SELECT 
            TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
         FROM 
            UTL_MENU_ITEM
         WHERE 
            MENU_ID = 5140016
      )
WHERE
   MENU_ID = 140016 AND
   EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 5140016)
;

--changeSet OPER-1907:3 stripComments:false
-- Update any child records in UTL_MENU_ITEM_ARG that reference menu_id 5140016 to 140016
UPDATE UTL_MENU_ITEM_ARG SET MENU_ID = 140016 WHERE MENU_ID = 5140016;

--changeSet OPER-1907:4 stripComments:false
-- Update any child records in UTL_MENU_GROUP_ITEM that reference menu_id 5140016 to 140016
UPDATE UTL_MENU_GROUP_ITEM SET MENU_ID = 140016 WHERE MENU_ID = 5140016;

--changeSet OPER-1907:5 stripComments:false
-- Delete UTL_MENU_ITEM 5140016 now that the existing data has been migrated to 140016
DELETE FROM UTL_MENU_ITEM WHERE MENU_ID = 5140016;