--liquibase formatted sql
--changeSet 12008:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete config parm
BEGIN

   utl_migr_data_pkg.action_parm_delete(
    'API_ACTION_DELETE_DEFER_REF'
    );

END;
/
