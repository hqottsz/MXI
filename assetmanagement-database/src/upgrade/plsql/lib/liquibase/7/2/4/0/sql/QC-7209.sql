--liquibase formatted sql


--changeSet QC-7209:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_delete('ACTION_SET_MANUAL_SCHEDULING');
END;
/