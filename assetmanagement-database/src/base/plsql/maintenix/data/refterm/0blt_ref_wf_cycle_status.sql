--liquibase formatted sql


--changeSet 0blt_ref_wf_cycle_status:1 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('PENDING','Pending','The cycle has been initiated and is awaiting the process to start.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_cycle_status:2 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('INPROG','In Progress','The cycle is currently processing.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_cycle_status:3 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('COMPLTD','Completed','The cycle is complete and all workflows completed successfully.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_cycle_status:4 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('ERROR','Error','The cycle failed as one or more workflows had errors.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');

--changeSet 0blt_ref_wf_cycle_status:5 stripComments:false
INSERT INTO blt_ref_wf_cycle_status(REF_WF_CYCLE_STATUS_CD,REF_WF_CYCLE_STATUS_NAME,REF_WF_CYCLE_STATUS_DESC,CTRL_DB_ID,REVISION_NO,CREATION_DB_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
VALUES('STAGED','Staged','The cycle has been staged.',0,1,0,0,'18-NOV-11','18-NOV-11',0,'MXI');