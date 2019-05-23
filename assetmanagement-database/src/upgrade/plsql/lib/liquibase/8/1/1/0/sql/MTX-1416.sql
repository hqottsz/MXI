--liquibase formatted sql


--changeSet MTX-1416:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'MOBILE_WELCOME_PAGE',
 'MXCOMMONWEB',
 'This parameter is used to set the default start page for mobile Maintenix clients.  This parameter may be overridden by user and/or role values.',
 'USER',
 '',
 'mobile.jsp',
 1,
 'HTML Parameter',
 '8.1-SP1',
 0
);
 
END;
/