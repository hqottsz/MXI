--liquibase formatted sql

--changeSet OPER-24653:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_EDIT_AIRCRAFT_DETAILS',
      p_parm_desc         => 'Permission to access the Edit Inventory button to edit general information for an aircraft such as serial number, applicability code, registration code, etc. There are four steps with links to pages for editing inventory. This permission is for Step 1. The parameters for the other three steps are:  Step 2: Edit subcomponent details -  ACTION_EDIT_AIRCRAFT_SUBITEMS.   Step 3: Edit inventory usage -  ACTION_EDIT_AIRCRAFT_USAGE.  Step 4: Edit task list for inventory - ACTION_EDIT_AIRCRAFT_TASKS.  Note, that on the Edit Inventory Step 1 page, to edit the received date and the manufactured date, users require another permission, ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Inventory',
      p_modified_in       => '8.3-SP1',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/

--changeSet OPER-24653:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_EDIT_AIRCRAFT_SUBITEMS',
      p_parm_desc         => 'Permission to edit the details of the subcomponents of an aircraft. With this permission you can edit part numbers, serial numbers, OEM info, and ICN numbers. There are four steps or links to pages for editing inventory. This permission is for Step 2 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_AIRCRAFT_DETAILS.  Step 3: Edit inventory usage -  ACTION_EDIT_AIRCRAFT_USAGE.  Step 4: Edit task list for inventory - ACTION_EDIT_AIRCRAFT_TASKS.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Inventory',
      p_modified_in       => '8.3-SP1',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/

--changeSet OPER-24653:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_EDIT_AIRCRAFT_USAGE',
      p_parm_desc         => 'Permission to edit the usage values of  the subcomponents of an aircraft. With this permission you can edit TSO (time since overhaul) and TSN (time since new) at install, TSO and TSN of highest component, and current TSO, TSI (time since install), and TSN. There are four steps or links to pages for editing inventory. This permission is for Step 3 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_AIRCRAFT_DETAILS.  Step 2: Edit subcomponent details -  ACTION_EDIT_AIRCRAFT_SUBITEMS.   Step 4: Edit task list for inventory - ACTION_EDIT_AIRCRAFT_TASKS.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Inventory',
      p_modified_in       => '8.3-SP1',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/

--changeSet OPER-24653:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_EDIT_AIRCRAFT_TASKS',
      p_parm_desc         => 'Permission to edit the details of the tasks of an aircraft. With this permission you can create tasks from task definitions or cancel  historical tasks (if applicable) and set tasks as not applicable. There are four steps or links to pages for editing inventory. This permission is for Step 4 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_AIRCRAFT_DETAILS.  Step 2: Edit subcomponent details -  ACTION_EDIT_AIRCRAFT_SUBITEMS.   Step 3: Edit inventory usage -  ACTION_EDIT_AIRCRAFT_USAGE.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Inventory',
      p_modified_in       => '8.3-SP1',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/