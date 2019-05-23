--liquibase formatted sql


--changeSet oper-237-010:1 stripComments:false
--
--
-- fleet movement
--
--     by year, month, tail number, serial number, operator, fleet type
--
CREATE OR REPLACE FORCE VIEW aopr_fleet_movement_v1
AS 
SELECT 
   fleet_movement.operator_registration_code,
   fleet_movement.serial_number,
   fleet_movement.operator_code,
   fleet_movement.phase_in_date,
   fleet_movement.fleet_type,
   fleet_movement.source_system,
   fleet_movement.fin_number,
   fleet_movement.phase_out_date,
   calendar_month.year_code,
   calendar_month.month_code
FROM 
   opr_fleet_movement fleet_movement
   INNER JOIN opr_calendar_month calendar_month ON 
     fleet_movement.phase_in_date BETWEEN calendar_month.start_date AND calendar_month.end_date;