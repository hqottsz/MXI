--liquibase formatted sql
--changeSet OPER-19136:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'ALLOW_FUTURE_MANUFACT_DATE', 
         'Permission to set manufactured date in the future when create or edit inventory.' ,
         'TRUE/FALSE', 
         'FALSE', 
         1, 
         'Supply Inventory', 
         '8.2-SP3u13', 
         0,
         0,
         UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
      );
END;
/