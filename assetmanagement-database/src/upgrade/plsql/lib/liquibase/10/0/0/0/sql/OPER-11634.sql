--liquibase formatted sql
-- 
-- OPER-11634: This change replaces the upgrade script added in OPER-10642. It takes 
-- takes care of migrating the role and user action parm settings so that they are
-- preserved with respect to adding part requirement workflows.
-- See the item in JIRA for more details. Below are the original notes from OPER-10642.
--
-- OPER-10642: ACTION_ADD_PART_REQUIREMENT must be enabled for Advanced Part Search
-- The solution involved defining separate configuration parameters to allow legacy
-- and advanced part requirement search buttons to be visible on the UI.
-- It also involved removing the ACTION_ADD_PART_REQUIREMENT_ADVANCED parm as it was 
-- not necessary to have two action parameters around the same business action.
--

--changeSet OPER-11634:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADD_PART_REQUIREMENT_LEGACY_SEARCH',
      'Permission to use the legacy add part requirement search workflow',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-11634:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADD_PART_REQUIREMENT_SEARCH',
      'Permission to use the advanced add part requirement search workflow',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-11634:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter(
      'UTL_PART_SEARCH',
      'ACTION_ADD_PART_REQUIREMENT_SEARCH'
   );
END;
/

--changeSet OPER-11634:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_perm_set_v1_pkg.AssociateActionParameter(
      'UTL_PART_SEARCH',
      'ACTION_ADD_PART_REQUIREMENT'
   );
END;
/

--changeSet OPER-11634:5 stripComments:false
INSERT INTO 
   utl_action_role_parm (
      role_id,
      parm_name,
      parm_value,
      utl_id
   )
   SELECT
      p.role_id,
      'ACTION_ADD_PART_REQUIREMENT_SEARCH',
      p.parm_value,
      ( SELECT db_id FROM mim_local_db )
   FROM 
      utl_action_role_parm p
   WHERE
      p.parm_name = 'ACTION_ADD_PART_REQUIREMENT_ADVANCED'
      AND
      NOT EXISTS ( SELECT 1 FROM utl_action_role_parm p2 WHERE p2.role_id = p.role_id AND p2.parm_name = 'ACTION_ADD_PART_REQUIREMENT_SEARCH' );
   
--changeSet OPER-11634:6 stripComments:false
INSERT INTO 
   utl_action_role_parm (
      role_id,
      parm_name,
      parm_value,
      utl_id
   )
   SELECT
      p.role_id,
      'ACTION_ADD_PART_REQUIREMENT_LEGACY_SEARCH',
      p.parm_value,
      ( SELECT db_id FROM mim_local_db )
   FROM 
      utl_action_role_parm p
   WHERE
      p.parm_name = 'ACTION_ADD_PART_REQUIREMENT'
      AND
      NOT EXISTS ( SELECT 1 FROM utl_action_role_parm p2 WHERE p2.role_id = p.role_id AND p2.parm_name = 'ACTION_ADD_PART_REQUIREMENT_LEGACY_SEARCH' );
   
--changeSet OPER-11634:7 stripComments:false
INSERT INTO 
   utl_action_user_parm (
      user_id,
      parm_name,
      parm_value,
      utl_id
   )
   SELECT
      p.user_id,
      'ACTION_ADD_PART_REQUIREMENT_SEARCH',
      p.parm_value,
      ( SELECT db_id FROM mim_local_db )
   FROM 
      utl_action_user_parm p
   WHERE
      parm_name = 'ACTION_ADD_PART_REQUIREMENT_ADVANCED'
      AND
      NOT EXISTS ( SELECT 1 FROM utl_action_user_parm p2 WHERE p2.user_id = p.user_id AND p2.parm_name = 'ACTION_ADD_PART_REQUIREMENT_SEARCH' );

--changeSet OPER-11634:8 stripComments:false
INSERT INTO 
   utl_action_user_parm (
      user_id,
      parm_name,
      parm_value,
      utl_id
   )
   SELECT
      p.user_id,
      'ACTION_ADD_PART_REQUIREMENT_LEGACY_SEARCH',
      p.parm_value,
      ( SELECT db_id FROM mim_local_db )
   FROM 
      utl_action_user_parm p
   WHERE
      p.parm_name = 'ACTION_ADD_PART_REQUIREMENT'
      AND
      NOT EXISTS ( SELECT 1 FROM utl_action_user_parm p2 WHERE p2.user_id = p.user_id AND p2.parm_name = 'ACTION_ADD_PART_REQUIREMENT_LEGACY_SEARCH' );
      
--changeSet OPER-11634:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_perm_set_v1_pkg.DissociateActionParameter(
      'UTL_PART_SEARCH',
      'ACTION_ADD_PART_REQUIREMENT_ADVANCED'
   );
END;
/

--changeSet OPER-11634:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_migr_data_pkg.action_parm_delete(
      'ACTION_ADD_PART_REQUIREMENT_ADVANCED'
   );
END;
/