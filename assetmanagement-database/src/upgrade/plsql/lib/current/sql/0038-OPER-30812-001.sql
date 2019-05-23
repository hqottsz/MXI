--liquibase formatted sql

--changeSet OPER-30812:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert (or update) an action parameter for using the Assembly API
BEGIN
      utl_migr_data_pkg.action_parm_insert
      (
         'API_ASSEMBLY_PARM',
         'Permission to utilize the Assembly API',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - MAINTENANCE',
         '8.2-SP5',
         0,
         0
      );
END;
/

--changeSet OPER-30812:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Give the new parameter all the info from the old parameter
BEGIN
	utl_migr_data_pkg.action_parm_copy (
		'API_ASSEMBLY_PARM',
		'READ_ASSEMBLY'
	);
END;		   
/


--changeSet OPER-30812:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Delete the old parameter
BEGIN
	utl_migr_data_pkg.action_parm_delete (
		'READ_ASSEMBLY'
	);
END;		   
/