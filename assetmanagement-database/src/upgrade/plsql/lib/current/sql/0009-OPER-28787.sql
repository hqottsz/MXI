--liquibase formatted sql

--changeSet OPER-28787 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment inserts a new config parm to control bulk loading data from CSVs into Maintenix

BEGIN
	utl_migr_data_pkg.action_parm_insert(
	'ACTION_BULK_LOAD_DATA',
	'Permission required to load data from CSVs into Maintenix',
	'TRUE/FALSE',
	'FALSE',
	1,
	'Servlet Permission',
	'8.3-SP2',
	0,
	0
	);
END;
/