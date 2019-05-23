--liquibase formatted sql
 

--changeSet MX-26827:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************
 * Delete action config and role param  ACTION_CREATE_PRICE_BREAK
 ********************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_delete('ACTION_CREATE_PRICE_BREAK');
END;
/