--liquibase formatted sql


--changeSet aopr_aircraft_delay_v1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.view_drop('aopr_aircraft_delay_v1'); 
END;
/