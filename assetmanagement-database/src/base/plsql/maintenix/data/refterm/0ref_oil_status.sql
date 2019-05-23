--liquibase formatted sql


--changeSet 0ref_oil_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_OIL_STATUS"
** 0-Level
** DATE: 02-OCT-09
*********************************************/
INSERT INTO ref_oil_status (oil_status_db_id,oil_status_cd,oil_status_sdesc,oil_status_ord,ui_notify_bool,rstat_cd,creation_dt,revision_dt,revision_db_id,revision_user) 
VALUES (0, 'NORMAL', 'Normal', 0, 0, 0, '01-OCT-09', '01-OCT-09', 0, 'MXI');