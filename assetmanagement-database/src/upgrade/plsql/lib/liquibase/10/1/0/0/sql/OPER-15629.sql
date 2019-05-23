--liquibase formatted sql

--changeSet OPER-15629:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE REQ_PART ADD (UPDATED_ETA DATE)
   ');
END;
/


--changeSet OPER-15629:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add('
      ALTER TABLE REQ_PART ADD (DELIVERY_NOTE VARCHAR2(4000))
   ');
END;
/

--changeSet OPER-15629:3 stripComments:false
  COMMENT ON COLUMN REQ_PART.UPDATED_ETA
IS
  'The estimated time of arrival of the part to the organization''s network. This is set manually by the material controller so that the line planner can schedule the work. Not to be confused with EST_ARRIVAL_DATE, which is calculated by Maintenix, but not always available or accurate.' ;

--changeSet OPER-15629:4 stripComments:false
COMMENT ON COLUMN REQ_PART.DELIVERY_NOTE
IS
  'A note about the delivery ofthe part to the organization''s network. This is set manually by the material controller so that the line planner can schedule the work.' ;

--changeSet OPER-15629:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      p_parm_name         => 'ACTION_UPDATE_DELIVERY_STATUS',
      p_parm_desc         => 'Permission to see the Update Delivery Status button that is required to update the delivery status of part requests with a comment or a different date.',
      p_allow_value_desc  => 'TRUE/FALSE',
      p_default_value     => 'FALSE',
      p_mand_config_bool  => 1,
      p_category          => 'Supply - Part Requests',
      p_modified_in       => '8.2-SP4',
      p_session_auth_bool => 0,
      p_utl_id            => 0,
      p_db_types          => UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/