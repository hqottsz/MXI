--liquibase formatted sql


--changeSet oper-415:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE


   l_start_date date;
   l_end_date   date;
   l_rowcount   integer;
   
BEGIN

   select min(phase_in_date), max(phase_in_date) 
   into l_start_date, l_end_date
   from opr_fleet_movement;
   
   l_rowcount := opr_report_period_pkg.create_calendar_month
                 (  
                     p_start_date => l_start_date,
                     p_end_date => l_end_date
                 );
                                                         
END;
/