--liquibase formatted sql


--changeSet 0ref_threshold_mode:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_THRESHOLD_MODE"
** 0-Level
*********************************************/
INSERT INTO REF_THRESHOLD_MODE (THRESHOLD_MODE_DB_ID, THRESHOLD_MODE_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'FLEET', 'Fleet', 0, '13-AUG-10', '13-AUG-10', 0, 'MXI');

--changeSet 0ref_threshold_mode:2 stripComments:false
INSERT INTO REF_THRESHOLD_MODE (THRESHOLD_MODE_DB_ID, THRESHOLD_MODE_CD, DESC_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ASSMBL', 'Assembly', 0, '13-AUG-10', '13-AUG-10', 0, 'MXI');