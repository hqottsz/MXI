--liquibase formatted sql


--changeSet oper-237-011:1 stripComments:false
--
--
-- monthly operator usage
--
--     by year, month, tail number, serial number, operator, fleet type
--
--
CREATE OR REPLACE FORCE VIEW aopr_monthly_operator_usage_v1
AS 
SELECT
   monthly_usage.year_code                                           AS year_code,
   monthly_usage.month_code                                          AS month_code,
   monthly_usage.operator_registration_code                          AS operator_registration_code,
   monthly_usage.serial_number                                       AS serial_number,
   monthly_usage.operator_code                                       AS operator_code,
   monthly_usage.fleet_type                                          AS fleet_type,
   monthly_usage.flight_hours                                        AS flight_hours,
   monthly_usage.cycles                                              AS cycles,
   monthly_usage.days_out_of_service                                 AS days_out_of_service,
   calendar_month.number_of_days - monthly_usage.days_out_of_service AS days_in_service,
   calendar_month.month_name                                         AS month_name,
   calendar_month.number_of_days                                     AS number_of_days,
   monthly_usage.flight_hours/calendar_month.number_of_days          AS daily_flight_hours,
   monthly_usage.cycles/calendar_month.number_of_days                AS daily_cycles,
   case monthly_usage.cycles
      when 0 then 0
      else monthly_usage.flight_hours/monthly_usage.cycles
   end                                                               AS flight_hours_per_cycle,
   aircraft_id                                                       AS aircraft_id,
   operator_id                                                       AS operator_id
FROM
   opr_monthly_reliability monthly_usage
   INNER JOIN aopr_calendar_month_v1 calendar_month ON
     monthly_usage.year_code  = calendar_month.year_code AND
     monthly_usage.month_code = calendar_month.month_code;