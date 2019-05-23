--liquibase formatted sql


--changeSet 0blt_ref_wf_log_status:1 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('PENDING','Pending','The record is currently pending and is awaiting the start of its parent workflow.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:2 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('STGQD','Stage Queued','The record is under the control of the parent workflow and is queued for staging.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:3 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('STGING','Staging','The record is under the control of the parent workflow and is in the process of transformation and validation.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:4 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('STGED','Staged','The record is under the control of the parent workflow and has successfully completed the transformation and validation process.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:5 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('INDCTQD','Induct Queued','The record is under the control of the parent workflow and is queued for induction.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:6 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('INDCTING','Inducting','The record is under the control of the parent workflow and is in the process of being inducted into Maintenix.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:7 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('INDCTED','Inducted','The record has successfully been inducted into Maintenix.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_log_status:8 stripComments:false
INSERT INTO blt_ref_wf_log_status(REF_WF_LOG_STATUS_CD,REF_WF_LOG_STATUS_NAME,REF_WF_LOG_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES ('ERROR','Error','The record has encountered an error while processing.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');