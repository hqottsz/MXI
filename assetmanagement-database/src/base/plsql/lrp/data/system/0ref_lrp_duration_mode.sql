--liquibase formatted sql


--changeSet 0ref_lrp_duration_mode:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_LRP_DURATION_MODE"
** 0-Level
** DATE: 20-MAY-2014 TIME: 00:00:00
*********************************************/
INSERT INTO ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES ( 'MX_DEFINITION', 'DEFINITION', 'DEFINITION', 'Work package duration is calculated using the default durations configured on the block or requirement definition.', 1, 0, 1, 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, 'MXI');

--changeSet 0ref_lrp_duration_mode:2 stripComments:false
INSERT INTO ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES ( 'MX_LOCATION', 'LOCATION', 'Location', 'Work package duration is calculated using the location capacity configured on the Capacity tab.', 2 , 0, 1, 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 0, 'MXI');