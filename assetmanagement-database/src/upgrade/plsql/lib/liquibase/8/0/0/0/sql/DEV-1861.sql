--liquibase formatted sql


--changeSet DEV-1861:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP
**************************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP',
      'LOGIC',
      'Defines availability of the user to assign/unassign LRP items to/from an INWORK work package',
      'USER',
      'TRUE/FALSE',
      'FALSE',
      0,
      'Maint - Long Range Planning',
      '8.0 SP1',
      0
   );
END;
/