--liquibase formatted sql


--changeSet 0utl_pb_item_doc:1 stripComments:false
/* Report data */
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'PART', 'Part No', 0 ,0)
;

--changeSet 0utl_pb_item_doc:2 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'ASSY', 'Assembly', 0,0)
;

--changeSet 0utl_pb_item_doc:3 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'BOM', 'BOM Item', 0,0)
;

--changeSet 0utl_pb_item_doc:4 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'DS', 'Data Source', 0,0)
;

--changeSet 0utl_pb_item_doc:5 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'TASKDEF', 'Task Definition', 0,0)
;

--changeSet 0utl_pb_item_doc:6 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'FAILMODE', 'Fault Definition', 0,0)
;

--changeSet 0utl_pb_item_doc:7 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'FAILEFF', 'Failure Effect', 0,0)
;

--changeSet 0utl_pb_item_doc:8 stripComments:false
INSERT INTO "UTL_PB_ITEM_DOC" ("APP_CD", "ITEM_TYPE_CD", "ITEM_NAME", "VIEW_FORMS_BOOL", UTL_ID)
VALUES ('BL', 'IETM', 'IETM', 0,0)
;