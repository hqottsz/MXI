--liquibase formatted sql

--changeSet AEB-400:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add action parameter API_ORDER_EXCEPTION (If missing) 
BEGIN
       utl_migr_data_pkg.action_parm_insert(
               'API_ORDER_EXCEPTION',
               'Permission to read Order Exceptions.',
               'TRUE/FALSE',
               'FALSE',
               1,
               'API - PROCUREMENT',
               '8.2-SP5',
               0,
               0,
               UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
        );
END;
/
