--liquibase formatted sql


--changeSet DEV-1371:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_HISTORY_CHARGE',
                    'Permission to review charge history.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Org - Financials',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/