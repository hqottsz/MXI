--liquibase formatted sql


--changeSet DEV-1906:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_FAULT_DEFN_ON_RAISE_FAULT_PAGE',
	'MXWEB',
	'Controls the visibility of the Fault Definition field on Raise Fault page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
   
END;
/

--changeSet DEV-1906:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_FAULT_DETAILS_ON_RAISE_FAULT_PAGE',
	'MXWEB',
	'Controls the visibility of the Fault Details group box on Raise Fault page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
END;
/

--changeSet DEV-1906:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_CREATE_WORKPACKAGE_SUBTYPE',
	'MXWEB',
	'Controls the visibility of the Subtype field on Create Work Package page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
END;
/

--changeSet DEV-1906:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_CREATE_WORKPACKAGE_DOCREF',
	'MXWEB',
	'Controls the visibility of the Doc. Ref field on Create Work Package page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
   
END;
/

--changeSet DEV-1906:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_CREATE_WORKPACKAGE_HEAVY_MAINT',
	'MXWEB',
	'Controls the visibility of the Heavy Maintenance checkbox on Create Work Package page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
 
END;
/

--changeSet DEV-1906:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_CREATE_WORKPACKAGE_ISSUE_TO_ACCOUNT',
	'MXWEB',
	'Controls the visibility of the Issue To Account field on Create Work Package page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
 
END;
/ 

--changeSet DEV-1906:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'DEFAULT_ISSUE_TO_ACCOUNT_CREATE_WORKPACKAGE',
	'MXWEB',
	'The default value to be used for the Issue To Account field on Create Work Package page.',
	'GLOBAL',
	'STRING',
	null,
	1,
	'MXWEB',
	'8.0',
	0
   );
END;
/

--changeSet DEV-1906:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'DEFAULT_HEAVY_MAINTENANCE_CREATE_WORKPACKAGE',
	'MXWEB',
	'The default value to be used for the Heavy Maintenance checkbox on Create Work Package page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
  
END;
/

--changeSet DEV-1906:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
	'HIDE_INV_CREATE_OWNER',
	'MXWEB',
	'Controls the visibility of the Owner field on Create Inventory page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
   
END;
/

--changeSet DEV-1906:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'HIDE_INV_CREATE_CREDIT_ACCOUNT',
	'MXWEB',
	'Controls the visibility of the Credit Account field on Create Inventory page.',
	'GLOBAL',
	'TRUE/FALSE',
	'FALSE',
	1,
	'MXWEB',
	'8.0',
	0
   );
   
END;
/

--changeSet DEV-1906:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
	'DEFAULT_OWNER_INV_CREATE',
	'MXWEB',
	'The default value to be used in the Owner field on Create Inventory page.',
	'GLOBAL',
	'STRING',
	null,
	1,
	'MXWEB',
	'8.0',
	0
   );
   
END;
/

--changeSet DEV-1906:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
   	'DEFAULT_CREDIT_ACCOUNT_INV_CREATE',
   	'MXWEB',
   	'The default value to be used in the Credit Account field on Create Inventory page.',
   	'GLOBAL',
   	'STRING',
   	null,
   	1,
   	'MXWEB',
   	'8.0',
   	0
   );
   
END;
/