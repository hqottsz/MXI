--liquibase formatted sql


--changeSet QC-6202:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************************************
 * Delete the following records from the utl_action_config_parm table:
 *    ACTION_DUPLICATE_VENDOR_PART_PRICE
 ******************************************************************************************************/
 BEGIN
      utl_migr_data_pkg.action_parm_delete('ACTION_DUPLICATE_VENDOR_PART_PRICE');
 END;
 /

--changeSet QC-6202:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create new configuration parameter ACTION_CREATE_FROM_EXISTING_VENDOR_PART_PRICE
 **************************************************************************/
 BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_CREATE_FROM_EXISTING_VENDOR_PART_PRICE',
                    'Permission to create from existing vendor part price.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Purchasing - Prices',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/ 