--liquibase formatted sql


--changeSet 0ref_lpa_run_status:1 stripComments:false
/************************************************
** 0-Level INSERT SCRIPT FOR REF_LPA_RUN_STATUS
*************************************************/
INSERT INTO REF_LPA_RUN_STATUS( LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'QUEUED', 'QUEUED', 'Queued', 'The automation run is queued',	10, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_run_status:2 stripComments:false
INSERT INTO REF_LPA_RUN_STATUS( LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'RUNNING', 'RUNNING', 'Running', 'The automation run is in progress', 20, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_run_status:3 stripComments:false
INSERT INTO REF_LPA_RUN_STATUS( LPA_RUN_STATUS_DB_ID, LPA_RUN_STATUS_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'COMPLETED', 'COMPLETED', 'Completed', 'The automation has completed', 30, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );