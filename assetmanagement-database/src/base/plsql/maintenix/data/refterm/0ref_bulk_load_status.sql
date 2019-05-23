--liquibase formatted sql


--changeSet 0ref_bulk_load_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ref_bulk_load_status"
** 0-Level
** DATE: 19-JAN-19 00:00:00
*********************************************/
INSERT INTO REF_BULK_LOAD_STATUS (STATUS_CD,STATUS_DB_ID,STATUS_SDESC, STATUS_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('QUEUED',0, 'Queued','In the UTL_FILE_IMPORT table, QUEUED is the default status of an unprocessed file, with all its rows in the QUEUED status. All rows of a QUEUED file in the BULK_LOAD_ELEMENT table will also be in this state by default.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI');
--changeSet 0ref_bulk_load_status:2 stripComments:false
INSERT INTO REF_BULK_LOAD_STATUS (STATUS_CD,STATUS_DB_ID,STATUS_SDESC, STATUS_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('ERROR',0, 'Errors Logged','The status of a file with one or more erroneous rows in the UTL_FILE_IMPORT table. If processing a row results in an error, the status of the row will change to ERROR in the BULK_LOAD_ELEMENT table.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI');
--changeSet 0ref_bulk_load_status:3 stripComments:false
INSERT INTO REF_BULK_LOAD_STATUS (STATUS_CD,STATUS_DB_ID,STATUS_SDESC, STATUS_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('PROCESSING',0, 'Processing','The status of a file being processed in Maintenix.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI');
--changeSet 0ref_bulk_load_status:4 stripComments:false
INSERT INTO REF_BULK_LOAD_STATUS (STATUS_CD,STATUS_DB_ID,STATUS_SDESC, STATUS_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('FINISHED',0, 'Imported','The status of a file which has been processed successfully.', 0, 1, 0, '19-Jan-19', '19-Jan-19', 0, 'MXI');