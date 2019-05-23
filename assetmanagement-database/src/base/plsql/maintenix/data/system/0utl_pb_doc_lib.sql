--liquibase formatted sql


--changeSet 0utl_pb_doc_lib:1 stripComments:false
/*************************************
*  Insert script for ULT_PB_DOC_LIB  *
**************************************/
/* Baseliner */
INSERT INTO "UTL_PB_DOC_LIB" ("APP_CD", "ITEM_TYPE_CD", "LIB_ID", "LIB_FILE_NAME", "FORM_BOOL", UTL_ID)
VALUES ('BL', 'ASSY', 1000, 's_assmbl.exe', 0, 0)
;

--changeSet 0utl_pb_doc_lib:2 stripComments:false
INSERT INTO "UTL_PB_DOC_LIB" ("APP_CD", "ITEM_TYPE_CD", "LIB_ID", "LIB_FILE_NAME", "FORM_BOOL", UTL_ID)
VALUES ('BL', 'TASKDEF', 1000, 's_taskdef.exe', 0, 0)
;