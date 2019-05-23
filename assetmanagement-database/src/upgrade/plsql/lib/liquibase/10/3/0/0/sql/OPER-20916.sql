--liquibase formatted sql

--changeSet OPER-20916:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new user parameter
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'TIME_ZONE_DISPLAY_MODE_STATION_MONITORING',
      p_parm_type        => 'LOGIC',
      p_parm_desc        => 'Defines how dates and times appear in the Station Monitoring application, as server time zone, or localized to the aicraft arrival airport.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'SERVER/LOCAL',
      p_default_value    => 'LOCAL',
      p_mand_config_bool => 1,
      p_category         => 'Time zones',
      p_modified_in      => '8.2-SP3',
      p_utl_id           => 0
   );
END;
/

--changeSet OPER-20916:2 stripComments:false
--comment Update timezone of OPS to use Etc/UTC
UPDATE inv_loc
SET    timezone_cd = 'Etc/UTC'
WHERE  loc_db_id = 0
AND    loc_id = 1000;