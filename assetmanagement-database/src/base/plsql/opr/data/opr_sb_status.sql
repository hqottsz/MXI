--liquibase formatted sql


--changeSet opr_sb_status:1 stripComments:false
/**********************************************
* INSERT SCRIPT FOR TABLE "OPR_SB_STATUS"
***********************************************/
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'OPEN', 'OPEN', 'Open', 'Open', 1 );

--changeSet opr_sb_status:2 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'NA', 'N/A', 'Not Applicable', 'Not Applicable', 2 );

--changeSet opr_sb_status:3 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'CLOSED', 'CLOSED', 'Closed', 'Closed', 3 );

--changeSet opr_sb_status:4 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'REPETITIVE', 'REPETITIVE', 'Repetitive', 'Repetitive', 4 );

--changeSet opr_sb_status:5 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'SUPERSEDED', 'SUPERSEDED', 'Superseded by another SB', 'Superseded by another SB', 5 );

--changeSet opr_sb_status:6 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
VALUES ( 'REJECTED', 'REJECTED', 'Rejected', 'Rejected', 6 );