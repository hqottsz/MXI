--liquibase formatted sql


--changeSet DEV-2419:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_LOCATION',
      'ARC',
      'The default location for new assets',
      'GLOBAL',
      'The ALT_ID for the location',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet DEV-2419:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_CONDITION',
      'ARC',
      'The condition code for new assets',
      'GLOBAL',
      'A condition code',
      'INSPREQ',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet DEV-2419:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_FORECAST_MODEL',
      'ARC',
      'The default forecast model for new assets',
      'GLOBAL',
      'The ALT_ID for the forecast model',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet DEV-2419:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_ISSUE_TO_ACCOUNT',
      'ARC',
      'The default issue account for new assets',
      'GLOBAL',
      'The ALT_ID for the issue account',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet DEV-2419:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_CREDIT_ACCOUNT',
      'ARC',
      'The default credit account for new assets',
      'GLOBAL',
      'The ALT_ID for the credit account',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet DEV-2419:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_OWNER',
      'ARC',
      'The default owner for new assets',
      'GLOBAL',
      'The ALT_ID for the owner',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/