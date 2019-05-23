--liquibase formatted sql

--changeSet OPER-25907:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete config parm
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_delete('ACTION_JSP_WEB_ROLE_ROLE_SECURITY');
END;
/

--changeSet OPER-25907:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete action parm
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_delete('ACTION_JSP_WEB_ROLE_ROLE_SECURITY');
END;
/