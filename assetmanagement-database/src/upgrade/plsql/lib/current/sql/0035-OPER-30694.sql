--liquibase formatted sql

--changeSet OPER-30694:1 stripComments:false
INSERT INTO
	REF_BULK_LOAD_FILE_ACTION
	(
	FILE_ACTION_TYPE_CD,
	FILE_ACTION_TYPE_DB_ID,
	ACTION_SDESC,
	ACTION_LDESC,
	RSTAT_CD,
	REVISION_NO,
	CTRL_DB_ID,
	CREATION_DT,
	REVISION_DT,
	REVISION_DB_ID,
	REVISION_USER)
	SELECT
		'BIN_ROUTE_ORDER',0,'Bin route order','This action code is used to identify and process bin location route order data loaded into Maintenix.', 0, 1, 0, sysdate, sysdate, 0, 'MXI'
	FROM
		DUAL
	WHERE NOT EXISTS (SELECT 1 FROM REF_BULK_LOAD_FILE_ACTION WHERE FILE_ACTION_TYPE_CD = 'BIN_ROUTE_ORDER' AND FILE_ACTION_TYPE_DB_ID =0);