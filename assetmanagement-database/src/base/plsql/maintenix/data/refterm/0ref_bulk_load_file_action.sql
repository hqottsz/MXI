--liquibase formatted sql


--changeSet 0ref_bulk_load_file_action:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ref_bulk_load_file_action"
** 0-Level
** DATE: 19-JAN-19 00:00:00
*********************************************/
INSERT INTO REF_BULK_LOAD_FILE_ACTION (FILE_ACTION_TYPE_CD,FILE_ACTION_TYPE_DB_ID,ACTION_SDESC, ACTION_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('WAREHOUSE_STOCK_LEVEL',0,'Warehouse locations stock levels','This action code is used to identify and process warehouse location stock level data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0ref_bulk_load_file_action:2 stripComments:false
INSERT INTO REF_BULK_LOAD_FILE_ACTION (FILE_ACTION_TYPE_CD,FILE_ACTION_TYPE_DB_ID,ACTION_SDESC, ACTION_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('SUPPLY_LOC_STOCK_LEVEL',0,'Supply locations stock levels','This action code is used to identify and process supply location stock level data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI');

--changeSet 0ref_bulk_load_file_action:3 stripComments:false
INSERT INTO REF_BULK_LOAD_FILE_ACTION (FILE_ACTION_TYPE_CD,FILE_ACTION_TYPE_DB_ID,ACTION_SDESC, ACTION_LDESC, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('BIN_ROUTE_ORDER',0,'Bin route order','This action code is used to identify and process bin location route order data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI');