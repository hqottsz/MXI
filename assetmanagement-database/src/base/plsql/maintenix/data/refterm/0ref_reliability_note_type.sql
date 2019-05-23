--liquibase formatted sql


--changeSet 0ref_reliability_note_type:1 stripComments:false
-- -- -- --  Reliability Note Types -- -- -- -- 
/*****************************************************
** INSERT SCRIPT FOR TABLE "REF_RELIABILITY_NOTE_TYPE"
** 0-Level
** DATE: 18-JUN-09 TIME: 12:28:00
******************************************************/
INSERT INTO ref_reliability_note_type (RELIABILITY_NOTE_TYPE_DB_ID, RELIABILITY_NOTE_TYPE_CD, DESC_SDESC, DESC_LDESC, SHOW_WARNING_BOOL, WARNING_TEXT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ( 0, 'ROGUE', 'Rogue Part', 'Rogue part type reliability note.', 1, '*** THIS IS A ROGUE PART ***', 0, '18-JUN-09', '18-JUN-09', 0, 'MXI');

--changeSet 0ref_reliability_note_type:2 stripComments:false
INSERT INTO ref_reliability_note_type (RELIABILITY_NOTE_TYPE_DB_ID, RELIABILITY_NOTE_TYPE_CD, DESC_SDESC, DESC_LDESC, SHOW_WARNING_BOOL, WARNING_TEXT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ( 0, 'NOTE', 'Reliability Note', 'This is a generic reliability note.', 0, NULL, 0, '18-JUN-09', '18-JUN-09', 0, 'MXI');