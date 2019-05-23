--liquibase formatted sql


--changeSet oper-237-014:1 stripComments:false
--
--
-- service difficulty
--
--     by year, month, fleet type, tail number, serial number, operator, fleet type
--
--
CREATE OR REPLACE VIEW AOPR_SERVICE_DIFFICULTY_V1 AS
WITH tcv AS
(
  SELECT
     year_code,
     month_code,
     operator_registration_code,
     serial_number,
     operator_code,
     fleet_type,
     column_name,
     column_value
  FROM
    aopr_reliability_v1
  UNPIVOT
  (
      column_value FOR column_name IN
      (
         --
         -- the numeric prefix is used to order the column value. This value is then placed in columnn_order
         -- where a sort can be applied if needed.
         --
         dvrt  as '1:Diversion Rate (DVRT)',
         atbr  as '2:Air Turnback Rate (ATBR)',
         abtr  as '3:Aborted Takeoff Rate (ABTR)',
         rtgr  as '4:Return to Gate Rate(RTGR)',
         abar  as '5:Aborted Approach Rate (ABAR)',
         emdr  as '6:Emergency Descent Rate (EMDR)',
         ifsdr as '7:In Flight Shutdown Rate (IFSDR)'
      )
   )
)
SELECT
   year_code,
   fleet_type,
   operator_code,
   substr(column_name,3)   AS column_name,
   substr(column_name,1,1) AS column_order,
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
   fleet_type,
   operator_registration_code,
   serial_number,
   operator_code,
   fleet_type,
   operator_code,
   column_name;