--liquibase formatted sql

--changeSet OPER-29433:1 stripComments:false
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
SELECT  11028, 'idTabBulkExportAndImport', 'web.todotab.FILE_STATUS', '/web/todolist/BulkExportAndImportTab.jsp', 'web.todotab.FILE_STATUS_TAB', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_TODO_TAB WHERE TODO_TAB_ID = 11028);

--changeSet OPER-29433:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control exporting supply location stock levels from Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_EXPORT_STOCK_LEVELS_FROM_SUPPLY_LOCATIONS',
	'Permission required to export supply location stock levels from Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Parts - Stock Numbers',
	'8.3-SP2',
	0,
	0
	);
END;
/

--changeSet OPER-29433:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control exporting warehouse stock levels from Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_EXPORT_STOCK_LEVELS_FROM_WAREHOUSE_LOCATIONS',
	'Permission required to export warehouse location stock levels from Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Parts - Stock Numbers',
	'8.3-SP2',
	0,
	0
	);
END;
/

--changeSet OPER-29433:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control importing supply location stock levels to Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_IMPORT_STOCK_LEVELS_TO_SUPPLY_LOCATIONS',
	'Permission required to import supply location stock levels to Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Parts - Stock Numbers',
	'8.3-SP2',
	0,
	0
	);
END;
/

--changeSet OPER-29433:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control importing warehouse stock levels to Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_IMPORT_STOCK_LEVELS_TO_WAREHOUSE_LOCATIONS',
	'Permission required to import warehouse location stock levels to Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Parts - Stock Numbers',
	'8.3-SP2',
	0,
	0
	);
END;
/