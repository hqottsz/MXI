--liquibase formatted sql


--changeSet MX-23017:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
                'ACTION_ASSIGN_FLIGHT_MEASUREMENT',
                'SECURED_RESOURCE',
                'Permission to assign measurements to a flight.',
                'USER',
                'TRUE/FALSE',
                'FALSE',
                1,
                'Assembly - Flight Measurements',
                '8.0',
                0,
                utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
                );
END;
/

--changeSet MX-23017:2 stripComments:false
UPDATE utl_config_parm SET
   parm_desc = 'Permission to assign measurements to a task.',
   modified_in = '8.0'
WHERE
   parm_name = 'ACTION_ASSIGN_MEASUREMENT'
   AND
   (
      modified_in != '8.0'
      OR
      parm_desc != 'Permission to assign measurements to a task.'
   )
;