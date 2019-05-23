--liquibase formatted sql


--changeSet opr_ad_status:1 stripComments:false
/**********************************************
* INSERT SCRIPT FOR TABLE "OPR_AD_STATUS"
***********************************************/
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'OPEN', 'OPEN', 'Open', 'Open', 1 );

--changeSet opr_ad_status:2 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'NA', 'N/A', 'Not Applicable', 'Not Applicable', 2 );

--changeSet opr_ad_status:3 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'CLOSED', 'CLOSED', 'Closed', 'Closed', 3 );

--changeSet opr_ad_status:4 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'REPETITIVE', 'REPETITIVE', 'Repetitive', 'Repetitive', 4 );

--changeSet opr_ad_status:5 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'SUPERSEDED', 'SUPERSEDED', 'Superseded by another AD', 'Superseded by another AD', 5 );