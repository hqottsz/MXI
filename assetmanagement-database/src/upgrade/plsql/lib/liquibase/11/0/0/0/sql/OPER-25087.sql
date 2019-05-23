--liquibase formatted sql

--changeSet OPER-25087:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment status filter for StockDistributionRequestsTab.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sStockDistReqStatus',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Stores the last selected value of the Status dropdown on the Stock Distribution Requests todo list.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'STRING',
      p_default_value    => 'ALL',
      p_mand_config_bool => 0,
      p_category         => 'SESSION',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/

--changeSet OPER-25087:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment where needed location filter for StockDistributionRequestsTab.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sStockDistReqWhereNeeded',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Stores the last selected value of the Where Needed location on the Stock Distribution Requests todo list.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'STRING',
      p_default_value    => '',
      p_mand_config_bool => 0,
      p_category         => 'SESSION',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/

--changeSet OPER-25087:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment show only hazmat filter for StockDistributionRequestsTab.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sStockDistReqOnlyHazmat',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Stores the last selected value of the Only Hazmat requests checkbox on the Stock Distribution Requests todo list.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 0,
      p_category         => 'SESSION',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/

--changeSet OPER-25087:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment show only non hazmat filter for StockDistributionRequestsTab.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sStockDistReqNonHazmat',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Stores the last selected value of the Non Hazmat requests checkbox on the Stock Distribution Requests todo list.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'TRUE/FALSE',
      p_default_value    => 'FALSE',
      p_mand_config_bool => 0,
      p_category         => 'SESSION',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/
