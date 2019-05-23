--liquibase formatted sql


--changeSet OPER-2444:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_data_pkg.config_parm_insert(
	 'SHOW_CHARGE_TO_ACCOUNT_FOR_NON_LOCAL_PO',
	 'LOGIC',
	 'This config parameter turns on logic that forces an expense account to be chosen when create purchase order or purchase order line for an organization with non-local owner.',
	 'GLOBAL',
	 'TRUE/FALSE',
	 'FALSE',
	 1,
	 'Core Logic',
	 '8.2',
	 0
	);
END;
/