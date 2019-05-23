--liquibase formatted sql


--changeSet DEV-2076:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ACTION_PRINT_ORDERED_TALLY_SHEET
**************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_PRINT_ORDERED_TALLY_SHEET',
      'Required permission to print a ordered tally sheet for a in-work or completed check',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/