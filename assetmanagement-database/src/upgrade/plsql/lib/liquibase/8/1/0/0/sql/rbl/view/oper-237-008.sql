--liquibase formatted sql


--changeSet oper-237-008:1 stripComments:false
--
--
-- dispatch metric
--
--     by year, month, fleet type, operator
--
--
CREATE OR REPLACE VIEW AOPR_DISPATCH_METRIC_V1 AS
WITH tcv AS
(
   SELECT
      year_code,
      month_code,
      fleet_type,
      operator_code,
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
          tdir as '1:Technical Dispatch Interuption Rate(TDIR per 100 cycles)',
          srel as '2:Scheduling Reliability (SREL)',
          sirt as '3:Scheduling Interuption Rate(SIRT per 100 Cycles)',
          tcpf as '4:Technical Cancellation Performance(TCPF)',
          tcnr as '5:Technical Cancellation Rate (TCNR per 100 cycles)',
          tncr as '6:Technical Completion Rate (TCRT)',
          dvrt as '7:Technical Non Completion Rate (TCNRT per 100 cycles)'
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
   operator_code,
   column_name;