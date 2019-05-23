--liquibase formatted sql


--changeSet 0blt_ref_error_type:1 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('CRITICAL','Critical error that must be fixed.',1,0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_error_type:2 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('MAJOR','Major error that must be fixed but may still allow induction into Maintenix.',2,0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_error_type:3 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('MINOR','Minor error that should be fixed but may still allow induction into Maintenix.',3,0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_error_type:4 stripComments:false
INSERT INTO blt_ref_error_type(REF_ERROR_TYPE_CD,REF_ERROR_TYPE_NAME,REF_ERROR_SEVERITY,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('WARNING','Warning that won''t prevent induction into Maintenix but does present an issue.',4,0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');