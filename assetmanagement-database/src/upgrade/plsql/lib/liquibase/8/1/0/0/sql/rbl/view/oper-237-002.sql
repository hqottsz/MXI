--liquibase formatted sql


--changeSet oper-237-002:1 stripComments:false
CREATE OR REPLACE VIEW AOPR_TDRL_V1 AS
WITH tcv AS
(
   SELECT
      year_code,
      month_code,
      fleet_type,
      operator_code,
      '9'                                     AS column_order,
      'Technical Dispatch Reliability (TDRL)' AS column_name,
      --column_name,
      --column_order,
      CASE
         WHEN (sum(completed_departures) + sum(cancelled_departures)) = 0
         THEN 0
         ELSE
            100 * ( 1 - ( sum(delayed_departures)
                          + sum(cancelled_departures)
                        ) /(sum(completed_departures) + sum(cancelled_departures)
                  ))
      END column_value
   FROM
     aopr_reliability_v1
   GROUP BY
     year_code,
     month_code,
     fleet_type,
     operator_code
)
SELECT
   year_code,
   fleet_type,
   operator_code,
   column_name,
   column_order,
   SUM(january)                            AS january,
   SUM(february)                           AS february,
   SUM(march)                              AS march,
   SUM(april)                              AS april,
   SUM(may)                                AS may,
   SUM(june)                               AS june,
   SUM(july)                               AS july,
   SUM(august)                             AS august,
   SUM(september)                          AS september,
   SUM(october)                            AS october,
   SUM(november)                           AS november,
   SUM(december)                           AS december
from tcv
   PIVOT
   (
       SUM(column_value) FOR month_code IN
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
   operator_code,
   fleet_type,
   column_name,
   column_order;