--liquibase formatted sql


--changeSet oper-237-006:1 stripComments:false
--
--
-- technical delay
--
--     by year, month, fleet type, tail number, serial number, operator, fleet type
--
--
CREATE OR REPLACE VIEW AOPR_TECHNICAL_DELAY_V1 AS
WITH tcv AS
(
   SELECT
     year_code,
     month_code,
     fleet_type,
     chapter_code,
     departure_airport_code,
     operator_code,
     delay_category_code,
     delayed_departures
  FROM
     opr_monthly_delay monthly_delay
)
SELECT
  year_code,
  fleet_type,
  delay.delay_category_code,
  delay_category_name,
  operator_code,
  january,
  february,
  march,
  april,
  may,
  june,
  july,
  august,
  september,
  october,
  november,
  december
FROM
  (
      SELECT
        year_code,
        fleet_type,
        delay_category_code,
        operator_code,
        SUM(january)         AS january,
        SUM(february)        AS february,
        SUM(march)           AS march,
        SUM(april)           AS april,
        SUM(may)             AS may,
        SUM(june)            AS june,
        SUM(july)            AS july,
        SUM(august)          AS august,
        SUM(september)       AS september,
        SUM(october)         AS october,
        SUM(november)        AS november,
        SUM(december)       AS december
      FROM
        tcv
      PIVOT
      (
         SUM(delayed_departures) FOR month_code IN
         (
            '01' AS january,
            '02' AS february,
            '03' AS march,
            '04' AS april,
            '05' AS may,
            '06' AS june,
            '07' AS july,
            '08' AS august,
            '09' AS september,
            '10' AS october,
            '11' AS november,
            '12' AS december
        )
  )
GROUP BY
  year_code,
  fleet_type,
  operator_code,
  delay_category_code
) delay
INNER JOIN opr_delay_category ON
   delay.delay_category_code = opr_delay_category.delay_category_code;