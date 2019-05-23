--liquibase formatted sql


--changeSet MX-26605:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert(
               'ACTION_CHECK_STOCK_LEVEL',
               'Permission to check the stock level.',
               'TRUE/FALSE',
               'FALSE',
               1,
               'Parts - Stock Numbers',
               '8.0',
               0,
               0,
               utl_migr_data_pkg.DbTypeCdList( 'OPER' )
      );
END;
/