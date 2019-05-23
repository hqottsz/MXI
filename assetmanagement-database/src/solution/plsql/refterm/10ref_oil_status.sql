/********************************************
** INSERT SCRIPT FOR TABLE "REF_OIL_STATUS"
** 10-Level
** DATE: 02-OCT-09
*********************************************/
INSERT INTO ref_oil_status (oil_status_db_id,oil_status_cd,oil_status_sdesc,oil_status_ord,ui_notify_bool,rstat_cd,creation_dt,revision_dt,revision_db_id,revision_user) 
VALUES (10, 'CAUTION', 'The oil consumption rate for the engine/APU is slightly worse then normal.', 1, 0, 0, '02-OCT-09', '02-OCT-09', 0, 'MXI');

-- Note: for WARNING, we are setting the ui_notify_bool to true
INSERT INTO ref_oil_status (oil_status_db_id,oil_status_cd,oil_status_sdesc,oil_status_ord,ui_notify_bool,rstat_cd,creation_dt,revision_dt,revision_db_id,revision_user) 
VALUES (10, 'WARNING', 'The oil consumption rate for the engine/APU is moderately worse then normal.', 2, 1, 0, '02-OCT-09', '02-OCT-09', 0, 'MXI');
