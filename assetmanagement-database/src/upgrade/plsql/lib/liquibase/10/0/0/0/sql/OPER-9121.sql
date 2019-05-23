--liquibase formatted sql

--changeSet OPER-9121:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Delete action parameter
BEGIN
   utl_migr_data_pkg.action_parm_delete('ACTION_ADD_PURCHASE_VENDOR_PRICE');
END;
/