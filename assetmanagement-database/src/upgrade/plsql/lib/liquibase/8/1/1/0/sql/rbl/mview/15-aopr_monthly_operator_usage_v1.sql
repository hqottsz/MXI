--liquibase formatted sql


--changeSet 15-aopr_monthly_operator_usage_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_MONTHLY_OPERATOR_USAGE_V1');
END;
/

--changeSet 15-aopr_monthly_operator_usage_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_MONTHLY_OPERATOR_USAGE_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND 
AS
WITH calendar_month AS
(
    SELECT
       opr_calendar_month.year_code,
       opr_calendar_month.month_code,
       opr_calendar_month.start_date,
       opr_calendar_month.end_date,
       trunc(opr_calendar_month.end_date - opr_calendar_month.start_date) + 1 AS number_of_days,
       TO_CHAR(opr_calendar_month.start_date, 'Month')                        AS month_name
    FROM
       opr_calendar_month
)
SELECT
   monthly_usage.year_code                                           AS year_code,
   monthly_usage.month_code                                          AS month_code,
   monthly_usage.year_code || '-' || monthly_usage.month_code        AS year_month,
   trim(monthly_usage.operator_registration_code)                    AS operator_registration_code,
   monthly_usage.serial_number                                       AS serial_number,
   monthly_usage.operator_code                                       AS operator_code,
   monthly_usage.fleet_type                                          AS fleet_type,
   monthly_usage.flight_hours                                        AS flight_hours,
   monthly_usage.cycles                                              AS cycles,
   monthly_usage.revenue_flight_hours                                AS revenue_flight_hours,
   monthly_usage.revenue_cycles                                      AS revenue_cycles,
   monthly_usage.days_out_of_service                                 AS days_out_of_service,
   calendar_month.number_of_days - monthly_usage.days_out_of_service AS days_in_service,
   calendar_month.month_name                                         AS month_name,
   calendar_month.number_of_days                                     AS number_of_days,
   monthly_usage.flight_hours/calendar_month.number_of_days          AS daily_flight_hours,
   monthly_usage.cycles/calendar_month.number_of_days                AS daily_cycles,
   monthly_usage.revenue_flight_hours/calendar_month.number_of_days  AS daily_revenue_flight_hours,
   monthly_usage.revenue_cycles/calendar_month.number_of_days        AS daily_revenue_cycles,
   CASE monthly_usage.cycles
      WHEN 0 THEN 0
      ELSE monthly_usage.flight_hours/monthly_usage.cycles
   END                                                               AS flight_hours_per_cycle,
   CASE monthly_usage.revenue_cycles
      WHEN 0 THEN 0
      ELSE monthly_usage.revenue_flight_hours/monthly_usage.revenue_cycles
   END                                                               AS revenue_flight_hours_per_cycle,
   monthly_usage.aircraft_id                                         AS aircraft_id,
   operator.operator_id                                              AS operator_id,
   cast(monthly_usage.month_code as integer)                         As month,
   trunc(calendar_month.start_date)                                  AS start_date,
   trunc(calendar_month.end_date)                                    AS end_date,
   ADD_MONTHS(trunc(calendar_month.end_date),-3) +1                  AS previous_3months_startdate,
   ADD_MONTHS(trunc(calendar_month.end_date),-12)+1                  AS previous_12months_startdate,
   work_package_list                                                 AS work_package_list,
   assembly_subtype.assembly_sub_type_code                           AS subtype_code
FROM
   opr_rbl_monthly_usage_mv monthly_usage
   INNER JOIN acor_operator_v1 operator ON 
      monthly_usage.operator_code = operator.operator_code
   INNER JOIN calendar_month ON
     monthly_usage.year_code  = calendar_month.year_code AND
     monthly_usage.month_code = calendar_month.month_code
   LEFT JOIN acor_inv_aircraft_v1 assembly_subtype ON
     monthly_usage.aircraft_id = assembly_subtype.aircraft_id;