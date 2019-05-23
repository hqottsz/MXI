--liquibase formatted sql


--changeSet 08-aopr_service_difficulty_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_SERVICE_DIFFICULTY_V1');
END;
/

--changeSet 08-aopr_service_difficulty_v1:2 stripComments:false
create materialized view AOPR_SERVICE_DIFFICULTY_V1
refresh complete on demand
as
WITH rvw_dispatch AS
(
  SELECT
     year_code,
     month_code,
     operator_code,
     fleet_type,
     column_name,
     column_value
  FROM
     (
        SELECT
           year_code,
           month_code,
           operator_code,
           fleet_type,
           SUM(ifsdr) AS ifsdr,
           SUM(emdr)  AS emdr,
           SUM(abar)  AS abar,
           SUM(rtgr)  AS rtgr,
           SUM(abtr)  AS abtr,
           SUM(atbr)  AS atbr,
           SUM(dvrt)  AS dvrt
        FROM
          aopr_reliability_v1
        GROUP BY
           year_code,
           month_code,
           operator_code,
           fleet_type
     )
  UNPIVOT
  (
      column_value FOR column_name IN
      (
         --
         -- the numeric prefix is used to order the column value. This value is then placed in columnn_order
         -- where a sort can be applied if needed.
         --
         dvrt  AS '1:Diversion Rate (DVRT)',
         atbr  AS '2:Air Turnback Rate (ATBR)',
         abtr  AS '3:Aborted Takeoff Rate (ABTR)',
         rtgr  AS '4:Return to Gate Rate(RTGR)',
         abar  AS '5:Aborted Approach Rate (ABAR)',
         emdr  AS '6:Emergency Descent Rate (EMDR)',
         ifsdr AS '7:In Flight Shutdown Rate (IFSDR)'
      )
   )
)
SELECT
   rvw_dispatch.year_code,
   rvw_dispatch.month_code,
   rvw_dispatch.year_code || '-' || rvw_dispatch.month_code AS year_month,
   calendar_month.start_date,
   trunc(calendar_month.end_date,'DD') AS end_date,
   fleet_type,
   operator_code,
   SUBSTR(column_name,3)   AS column_name,
   SUBSTR(column_name,1,1) AS column_order,
   column_value            AS m1,
   LAG(column_value, 1,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m2,
   LAG(column_value, 2,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m3,
   LAG(column_value, 3,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m4,
   LAG(column_value, 4,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m5,
   LAG(column_value, 5,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m6,
   LAG(column_value, 6,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m7,
   LAG(column_value, 7,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m8,
   LAG(column_value, 8,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m9,
   LAG(column_value, 9,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m10,
   LAG(column_value,10,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m11,
   LAG(column_value,11,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m12,
   LAG(column_value,12,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m13,
   LAG(column_value,13,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m14,
   LAG(column_value,14,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m15
FROM
   rvw_dispatch
   INNER JOIN opr_calendar_month calendar_month ON
      calendar_month.year_code  = rvw_dispatch.year_code AND
      calendar_month.month_code = rvw_dispatch.month_code;