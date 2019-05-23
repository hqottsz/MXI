--liquibase formatted sql


--changeSet OPER-8001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- insert action parameter ALLOW_EDIT_RECEIVE_DATE_ON_INVENTORY into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY',
      'Controls if the received date or manufactured date can be modified by the user when editing inventory details. If the parameter is TRUE, then the user can modify the received or manufactured date. If it is FALSE, the user cannot modify the received or manufactured date.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Supply - Inventory',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/