--liquibase formatted sql


--changeSet opr_rbl_flight_disruption:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
begin
   utl_migr_schema_pkg.table_column_rename
   (
        'opr_rbl_flight_disruption',
        'actual_departure_date', 
        'scheduled_departure_date'
   );
end;
/