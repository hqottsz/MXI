--liquibase formatted sql


--changeSet MTX-1275:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add Complete Put Away API config parm
***************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_COMPLETE_PUTAWAY_REQUEST',
      'Permission to allow complete put away API call',
      'TRUE/FALSE',          
      'FALSE', 
       1,
      'API - MATERIALS',
      '8.1-SP2',
       0,
       0,
       UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
    );
END;
/