--liquibase formatted sql


--changeSet MTX-1112:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Correct ARC default allow_value_desc values 
-- Update ARC_DEFAULT_CREDIT_ACCOUNT 
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_CREDIT_ACCOUNT',
      'ARC',
      'The default credit account for new assets',
      'GLOBAL',
      'A credit account code',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet MTX-1112:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Update ARC_DEFAULT_FORECAST_MODEL
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_FORECAST_MODEL',
      'ARC',
      'The default forecast model for new assets',
      'GLOBAL',
      'A forecast model name',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet MTX-1112:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Update ARC_DEFAULT_ISSUE_TO_ACCOUNT
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_ISSUE_TO_ACCOUNT',
      'ARC',
      'The default issue account for new assets',
      'GLOBAL',
      'A issue account code',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet MTX-1112:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Update ARC_DEFAULT_LOCATION
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_LOCATION',
      'ARC',
      'The default location for new assets',
      'GLOBAL',
      'A location code',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/

--changeSet MTX-1112:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Update ARC_DEFAULT_OWNER
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ARC_DEFAULT_OWNER',
      'ARC',
      'The default owner for new assets',
      'GLOBAL',
      'A owner code',
      '',
      1,
      'ARC - Configuration',
      '8.1',
      0
   );
END;
/