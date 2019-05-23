--liquibase formatted sql

--changeSet OPER-25623:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ABSOLUTE_SESSION_TIMEOUT',
      'HTTP',
      'The maximum duration (in minutes) of a session, after which the session is terminated. A value less than one allows a session to remain active indefinitely.',
      'GLOBAL',
      'INTEGER',
      '480',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/

--changeSet OPER-25623:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'IDLE_SESSION_TIMEOUT',
      'HTTP',
      'The maximum duration (in minutes) allowed between requests made within the same session, after which the session is terminated. A value less than one allows an indefinite amount of time between requests.',
      'GLOBAL',
      'INTEGER',
      '15',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/
