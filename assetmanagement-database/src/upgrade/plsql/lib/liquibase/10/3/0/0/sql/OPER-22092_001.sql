--liquibase formatted sql

--changeSet OPER-22092:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_delete(
    'ENABLE_ADVANCED_SEARCH' -- parameter name
   );
 
END;
/

--changeSet OPER-22092:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete action_parm_delete
BEGIN
 
   utl_migr_data_pkg.action_parm_delete(
    'ACTION_ENABLE_ADVANCED_SEARCH' -- parameter name
   );
 
END;
/

--changeSet OPER-22092:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete action_parm_delete
BEGIN
 
   utl_migr_data_pkg.action_parm_delete(
    'ACTION_SEARCH_ADVANCED_SEARCH' -- parameter name
   );
 
END;
/

--changeSet OPER-22092:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete action_parm_delete
BEGIN
 
   utl_migr_data_pkg.action_parm_delete(
    'ACTION_JSP_WEB_SEARCH_ADVANCED_SEARCH' -- parameter name
   );
 
END;
/

--changeset OPER-22092:5 stripComments:false
--comment delete from table utl_job
DELETE FROM
   utl_job
WHERE
   job_cd = 'MX_CORE_GENERATE_SOLR_INDEX';


--changeset OPER-22092:6 stripComments:false
--comment delete from table utl_job
DELETE FROM
   utl_job
WHERE
   job_cd = 'MX_CORE_GENERATE_PART_SEARCH_SOLR_INDEX';   

--changeset OPER-22092:7 stripComments:false
--comment delete from table UTL_MENU_ITEM_ARG
DELETE FROM
   UTL_MENU_ITEM_ARG
WHERE
   MENU_ID = 10153;

--changeset OPER-22092:8 stripComments:false
--comment delete from table UTL_MENU_GROUP_ITEM
DELETE FROM
   UTL_MENU_GROUP_ITEM
WHERE
   MENU_ID = 10153;

--changeset OPER-22092:9 stripComments:false
--comment delete from table UTL_MENU_ITEM
DELETE FROM
   UTL_MENU_ITEM
WHERE
   MENU_ID = 10153;

   