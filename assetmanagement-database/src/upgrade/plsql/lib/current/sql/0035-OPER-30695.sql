--liquibase formatted sql

--changeSet OPER-30695:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control exporting bin locations from Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_EXPORT_ROUTE_ORDER_FROM_BIN_LOCATIONS',
	'Permission required to export the route order from bin locations in Maintenix',
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