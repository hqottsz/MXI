--liquibase formatted sql


--changeSet MX-26847:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*****************************************************************************
 * Delete action configuration parameter ACTION_VIEW_VENDOR_PRICE_HISTORY
 *****************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_VIEW_VENDOR_PRICE_HISTORY');
END;
/  