--liquibase formatted sql

--changeSet OPER-5253:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
   'ACTION_UNSCRAP_INVENTORY',
   'Permission to unscrap a piece of inventory in Maintenix.',
   'TRUE/FALSE',
   'FALSE',
   1,
   'Supply - Inventory',
   '8.2-SP5',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
  );
END;
/

--changeSet OPER-5253:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add JSP security action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_UNSCRAP_INVENTORY',
   'Permission to access the Unscrap Inventory page.',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP5',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
  );
END;
/

--changeSet OPER-5253:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- New Financial Transaction Type
BEGIN
  INSERT INTO ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
  VALUES (0, 'UNSCRAP', 'UNSCRAP', 'Transaction occurred when Inventory was Unscrapped.', 0, TO_DATE('2017-12-11', 'YYYY-MM-DD'),TO_DATE('2017-12-11', 'YYYY-MM-DD'), 0, 'MXI');
EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
END;
/