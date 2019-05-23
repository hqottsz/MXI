--liquibase formatted sql


--changeSet MTX-1338:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add PreDraw Asset API config parm
***************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PREDRAW_ASSET_REQUEST',
      'Permission to allow pre-draw asset API call',
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