--liquibase formatted sql
--changeSet OPER-21246:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment removal of ACTION_SEARCH_TOOL_W_BARCODE after the button was removed from UI
BEGIN
   utl_migr_data_pkg.action_parm_delete(
   'ACTION_SEARCH_TOOLS_W_BARCODE'
   );
END;
/