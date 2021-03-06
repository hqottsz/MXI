--liquibase formatted sql


--changeSet 13-aopr_dispatch_metric_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_DISPATCH_METRIC_V1');
END;
/

--changeSet 13-aopr_dispatch_metric_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_DISPATCH_METRIC_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND 
AS
WITH rvw_dispatch_metric AS
(
  SELECT
     year_code,
     month_code,
     fleet_type,
     operator_code,
     column_name,
     column_value
  FROM
    (
       SELECT
          year_code,
          month_code,
          fleet_type,
          operator_code,
          SUM(tdir) AS tdir,
          SUM(srel) AS srel,
          SUM(sirt) AS sirt,
          SUM(tcpf) AS tcpf,
          SUM(tcnr) AS tcnr,
          SUM(tncr) AS tncr,
          SUM(dvrt) AS dvrt
       FROM
         aopr_reliability_v1
       GROUP BY
          year_code,
          month_code,
          fleet_type,
          operator_code
    )
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
    rvw_dispatch_metric.year_code,
    rvw_dispatch_metric.month_code,
    rvw_dispatch_metric.year_code || '-' || rvw_dispatch_metric.month_code AS year_month,
    rvw_dispatch_metric.fleet_type,
    rvw_dispatch_metric.operator_code,
    calendar_month.start_date,
    trunc(end_date,'DD')    AS end_date,
    SUBSTR(column_name,3)   AS column_name,
    SUBSTR(column_name,1,1) AS column_order,
    column_value as m1,
    LAG(column_value, 1,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m2,
    LAG(column_value, 2,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m3,
    LAG(column_value, 3,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m4,
    LAG(column_value, 4,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m5,
    LAG(column_value, 5,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m6,
    LAG(column_value, 6,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m7,
    LAG(column_value, 7,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m8,
    LAG(column_value, 8,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m9,
    LAG(column_value, 9,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m10,
    LAG(column_value,10,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m11,
    LAG(column_value,11,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m12,
    LAG(column_value,12,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m13,
    LAG(column_value,13,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m14,
    LAG(column_value,14,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m15
FROM 
   rvw_dispatch_metric
   INNER JOIN
      opr_calendar_month calendar_month ON
         calendar_month.year_code  = rvw_dispatch_metric.year_code AND
         calendar_month.month_code = rvw_dispatch_metric.month_code;