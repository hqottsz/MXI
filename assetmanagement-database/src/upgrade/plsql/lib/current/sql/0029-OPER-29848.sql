--liquibase formatted sql

--changeSet OPER-29848 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to give permission to add new inventory lines to repair order

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_ADD_INV_RO_LINE',
	'Permission to add inventory lines to repair order.',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Purchasing - Repair Orders',
	'8.3-SP2',
	0,
	0
	);
END;
/