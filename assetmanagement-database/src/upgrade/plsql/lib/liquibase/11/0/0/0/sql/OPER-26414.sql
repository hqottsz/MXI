--liquibase formatted sql

--changeSet OPER-26414:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE REQ_PART ADD PRINTED_DT DATE'
   );
END;
/

--changeSet OPER-26414:2 stripComments:false
COMMENT ON COLUMN REQ_PART.PRINTED_DT
IS
  'The date that a part request issue transfer was last printed.' ;
  
--changeSet OPER-26414:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_PRINT_ISSUE_TRANSFERS',
      p_parm_desc         => 'Permission to background print issue transfers.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Inventory',
      p_modified_in       => '8.3-SP1',
	  p_session_auth_bool => 0,
      p_utl_id            => 0,
	  p_db_types          => utl_migr_data_pkg.dbtypecdlist('OPER','MASTER')
   );
END;
/

--changeSet OPER-26414:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'PRINT_OPEN_ISSUE_TRANSFERS',
      p_parm_type        => 'LOGIC',
      p_parm_desc        => 'If enabled, issue transfer tickets will be printed when the part request status is in OPEN or AVAIL.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Supply - Inventory',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/
