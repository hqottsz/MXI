--liquibase formatted sql


--changeSet 10-aopr_technical_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_TECHNICAL_DELAY_V1');
END;
/

--changeSet 10-aopr_technical_delay_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_TECHNICAL_DELAY_V1
REFRESH COMPLETE ON DEMAND
AS
SELECT
    delay.year_code,
    delay.month_code,
    delay.year_code || '-' || delay.month_code AS year_month,
    start_date,
    trunc(end_date,'DD')                       AS end_date,
    fleet_type,
    operator_code,
    delay.delay_category_code,
    delay_category.delay_category_name,
    delay_category.display_order,
    delayed_departures                                                                                                                               AS m1,
    LAG(delayed_departures, 1,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m2,
    LAG(delayed_departures, 2,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m3,
    LAG(delayed_departures, 3,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m4,
    LAG(delayed_departures, 4,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m5,
    LAG(delayed_departures, 5,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m6,
    LAG(delayed_departures, 6,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m7,
    LAG(delayed_departures, 7,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m8,
    LAG(delayed_departures, 8,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m9,
    LAG(delayed_departures, 9,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m10,
    LAG(delayed_departures,10,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m11,
    LAG(delayed_departures,11,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m12,
    LAG(delayed_departures,12,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m13,
    LAG(delayed_departures,13,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m14,
    LAG(delayed_departures,14,0) OVER (PARTITION BY fleet_type, operator_code, delay.delay_category_code ORDER BY delay.year_code, delay.month_code) AS m15
FROM
(
  SELECT
    year_code,
    month_code,
    fleet_type,
    delay_category_code,
    operator_code,
    SUM(delayed_departures) as     delayed_departures
  FROM
    opr_rbl_delay_mv
  GROUP BY
    year_code,
    month_code,
    fleet_type,
    operator_code,
    delay_category_code
) delay
INNER JOIN opr_rbl_delay_category delay_category ON
   delay.delay_category_code = delay_category.delay_category_code
INNER JOIN
   opr_calendar_month calendar_month ON
      calendar_month.year_code  = delay.year_code AND
      calendar_month.month_code = delay.month_code;