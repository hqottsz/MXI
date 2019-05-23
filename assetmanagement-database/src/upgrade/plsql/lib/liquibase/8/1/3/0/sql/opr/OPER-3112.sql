--liquibase formatted sql


--changeSet OPER-3112:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 	   utl_migr_data_pkg.action_parm_insert(
 	      'API_FOB_REQUEST', 
 	          'Permission to allow API FOB request',
			  'TRUE/FALSE',           
 	          'FALSE',  
 	          1, 
 	          'API - MATERIALS', 
 	          '8.2', 
 	          0, 
 	          0,
 	      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
 	   );
END;
/