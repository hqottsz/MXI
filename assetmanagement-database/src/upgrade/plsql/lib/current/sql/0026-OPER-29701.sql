--liquibase formatted sql

--changeSet OPER-29701:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'API_VIEW_INVENTORY_COUNT',
         'Permission to access the Inventory Count API, which retrieves the bin, part, and inventory information required to record a manual inventory count.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - MATERIALS',
         '8.3-SP2',
         0,
         0
      );
END;
/
