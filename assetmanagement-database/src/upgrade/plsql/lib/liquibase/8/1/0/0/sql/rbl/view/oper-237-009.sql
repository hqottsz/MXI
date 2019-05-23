--liquibase formatted sql


--changeSet oper-237-009:1 stripComments:false
--
--
-- dispatch reliability
--
--     by year, month, tail number, serial number, operator, fleet type
--
--
CREATE OR REPLACE VIEW AOPR_DISPATCH_RELIABILITY_V1 AS
WITH tcv AS
(
    --
    -- transform reliabilty numbers from rows into columns
    --
     SELECT
        year_code,
        month_code,
        operator_code,
        fleet_type,
        column_name,
        column_value
    FROM
       aopr_reliability_v1
    UNPIVOT
    (
          --
          -- the numeric prefix is used to order the column value. This value is then placed in columnn_order
          -- where a sort can be applied if needed.
          --
       column_value FOR column_name IN
       (
          flight_hours             AS '1:Total Hours Flown',
          cycles                   AS '2:Total Cycles Flown',
          completed_departures     AS '3:Total Completed Flights (incl delayed flights)',
          delayed_departures       AS '4:Total Number of technical delays',
          delayed_departures_gt_15 AS '5:Total Number of technical delays > 15 Minutes',
          delay_time               AS '6:Delays Duration (Total Hours)',
          cancelled_departures     AS '7:Number of Cancellations',
          diverted_departures      AS '8:Number of Diversions'
          --tdrl                     AS '9:Technical Dispatch Reliability (TDRL)'
       )
    )
)
SELECT
   year_code,
   operator_code,
   fleet_type,
   --column_name,
   SUBSTR(column_name,3)   AS column_name,
   SUBSTR(column_name,1,1) AS column_order,
   SUM(january)            AS january,
   SUM(february)           AS february,
   SUM(march)              AS march,
   SUM(april)              AS april,
   SUM(may)                AS may,
   SUM(june)               AS june,
   SUM(july)               AS july,
   SUM(august)             AS august,
   SUM(september)          AS september,
   SUM(october)            AS october,
   SUM(november)           AS november,
   SUM(december)           AS december
FROM
   tcv
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
   column_name
UNION ALL
SELECT 
   year_code, 
   operator_code, 
   fleet_type, 
   column_name , 
   column_order,
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
   aopr_tdrl_v1;