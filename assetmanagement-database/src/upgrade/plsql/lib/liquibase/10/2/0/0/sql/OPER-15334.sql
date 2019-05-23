--changeSet OPER-15334:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add config parameter MAX_USAGE_PARM_DISPLAY (If missing)
BEGIN
        -- Add the Config Parm
        UTL_MIGR_DATA_PKG.CONFIG_PARM_INSERT(
                   'MAX_USAGE_PARM_DISPLAY',
                   'MXWEB',
                   'The maximum amount of usage parameters used to display usage values on the Inventory Historic Usage tab. If this number is exceeded, then usage TSN/Deltas will not be displayed. If left as -1 (default) then ALL values will be displayed',
                   'GLOBAL',
                   'INTEGER',
                   -1,
                   0,
                   'Supply - Inventory',
                   '8.2-SP3u09',
                   0
        );
END;
/