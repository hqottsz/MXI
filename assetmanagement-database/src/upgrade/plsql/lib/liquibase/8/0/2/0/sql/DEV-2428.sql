--liquibase formatted sql


--changeSet DEV-2428:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_data_pkg.action_parm_insert(
		'API_GLOBAL_PARAMETER_REQUEST',
		'Permission to allow API retrieval call for global parameters.',
		'TRUE/FALSE',
		'FALSE',
		1,
		'API - SYSTEM',
		'8.1',
		0,
		0,
		UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
	);
END;
/

--changeSet DEV-2428:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_LOCALE_REQUEST',
      'API_GLOBAL_PARAMETER_REQUEST'
   );
END;
/