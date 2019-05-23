--liquibase formatted sql

--changeSet OPER-12338:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Delete action parameter
BEGIN
   utl_migr_data_pkg.action_parm_delete('ACTION_JSP_WEB_USER_ADD_USER_ATTACHMENT');
END;
/