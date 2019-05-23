--liquibase formatted sql


--changeSet 0utl_pb_app:1 stripComments:false
INSERT INTO UTL_PB_APP ("APP_CD", "APP_NAME", UTL_ID)
VALUES ('MXFC', 'MxFC', 0)
;

--changeSet 0utl_pb_app:2 stripComments:false
INSERT INTO UTL_PB_APP ("APP_CD", "APP_NAME", UTL_ID)
VALUES ('BL', 'Program Definition',0)
;