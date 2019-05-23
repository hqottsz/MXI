--liquibase formatted sql


--changeSet 04-opr_rbl_monthly_delay_mv:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('OPR_RBL_MONTHLY_DELAY_MV');
END;
/

--changeSet 04-opr_rbl_monthly_delay_mv:2 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_MONTHLY_DELAY_MV
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
      monthly_delay.year_code,
      monthly_delay.month_code,
      monthly_delay.fleet_type,
      monthly_delay.operator_registration_code,
      monthly_delay.serial_number,
      monthly_delay.operator_code,
      SUM(delayed_departures)                    AS delayed_departures,
      SUM(CASE
            WHEN NVL(opr_rbl_delay_category.high_range,16) > 15
            THEN
               delayed_departures
            ELSE
               0
          END
         )                        AS delayed_departures_gt_15,
      SUM(delay_time)             AS delay_time,
      SUM(maintenance_delay_time) AS maintenance_delay_time
   FROM
     opr_rbl_delay_mv monthly_delay
     INNER JOIN opr_rbl_delay_category ON
        opr_rbl_delay_category.delay_category_code = monthly_delay.delay_category_code
   GROUP BY
      monthly_delay.year_code,
      monthly_delay.month_code,
      monthly_delay.fleet_type,
      monthly_delay.operator_registration_code,
      monthly_delay.serial_number,
      monthly_delay.operator_code;