--liquibase formatted sql


--changeSet 0ref_lpa_issue_type:1 stripComments:false
/************************************************
** 0-Level INSERT SCRIPT FOR REF_LPA_ISSUE_TYPE
*************************************************/
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'ERROR_UNEXPECTED', 'ERROR_UNEXPECTED', 'Unexpected Error', 'An unexpected error occurred', 10, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:2 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'MODE_INVALID', 'MODE_INVALID', 'Invalid Mode', 'The line planning automation mode is not BASIC', 20, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:3 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'INV_LOCK',	'INV_LOCK', 'Locked Inventory', 'The aircraft is locked, which prevents automation', 30, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:4 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'INV_PREVENT_AUTO',	'INV_PREVENT_AUTO', 'Automation Prevented',	'Automation is prevented against the inventory', 40, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:5 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'CHECK_CONFLICT', 'CHECK_CONFLICT', 'Check Conflict', 'The check overlaps with another check', 50, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:6 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'FLIGHT_CONFLICT', 'FLIGHT_CONFLICT', 'Flight Conflict', 'The check occurs during a flight', 60, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:7 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'STOP_CONFLICT', 'STOP_CONFLICT', 'Stop Conflict', 'The check spans multiple stops', 70, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );

--changeSet 0ref_lpa_issue_type:8 stripComments:false
INSERT INTO REF_LPA_ISSUE_TYPE( LPA_ISSUE_TYPE_DB_ID, LPA_ISSUE_TYPE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'STOP_NO_SUITABLE',	'STOP_NO_SUITABLE', 'No Suitable Stop', 'No suitable stop exists in which to schedule the check', 80, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI' );