--liquibase formatted sql


--changeSet 07-aopr_tdrl_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_TDRL_V1');
END;
/

--changeSet 07-aopr_tdrl_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_TDRL_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
      year_code,
      month_code,
      MIN(year_month)                              AS year_month,
      MIN(start_date)                              AS start_date,
      MIN(end_date)                                AS end_date,
      fleet_type,
      operator_code,
      '20:Technical Dispatch Reliability (TDRL)-%' AS column_name,
      CASE
         WHEN (sum(completed_departures) + sum(cancelled_departures)) = 0
         THEN 0
         ELSE
            round (( 1 - ( sum(delayed_departures_gt_15)
                          + sum(cancelled_departures)
                        ) /(sum(completed_departures) + sum(cancelled_departures)
                  )),4) * 100
      END column_value
   FROM
     opr_rbl_monthly_dispatch_mv
   GROUP BY
     year_code,
     month_code,
     fleet_type,
     operator_code;