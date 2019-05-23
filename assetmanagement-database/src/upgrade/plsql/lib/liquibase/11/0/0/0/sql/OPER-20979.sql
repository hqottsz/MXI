--liquibase formatted sql

--changeSet OPER-20979:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- insert MESSAGE_SER_TRK_INVENTORY_IS_LOCKED to utl_config_parm table
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'MESSAGE_SER_TRK_INVENTORY_IS_LOCKED',
      p_parm_type        => 'SECURED_RESOURCE',
      p_parm_desc        => 'Permission to allow user to accept a warning when the serialized or tracked inventory is locked.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 1,
      p_category         => 'Supply - Inventory',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/