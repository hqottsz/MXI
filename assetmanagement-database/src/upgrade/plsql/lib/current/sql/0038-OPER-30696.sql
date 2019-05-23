--liquibase formatted sql

--changeSet OPER-30696:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control importing bin locations to Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_IMPORT_ROUTE_ORDER_TO_BIN_LOCATIONS',
	'Permission required to import the route order to bin locations in Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Org - Locations',
	'8.3-SP2',
	0,
	0
	);
END;
/