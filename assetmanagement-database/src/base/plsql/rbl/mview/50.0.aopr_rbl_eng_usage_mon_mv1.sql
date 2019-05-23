--liquibase formatted sql


--changeSet 50.0.aopr_rbl_eng_usage_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_USAGE_MON_MV1');
END;
/

--changeSet 50.0.aopr_rbl_eng_usage_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_usage_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   data_type_code,
   year_code,
   month_code,
   SUM(quantity) quantity
FROM
   (
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         engine_type,
         data_type_code,
         year_code,
         month_code,
         quantity
      FROM
         opr_rbl_eng_usage_mon
      -- maintenix data
      UNION ALL
      -- legacy data
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         engine_type,
         data_type_code,
         year_code,
         month_code,
         quantity
      FROM
        (
            SELECT -- hours
               operator.operator_id,
               operator.operator_code,
               fleet_type,
               engine_type,
               year_code,
               month_code,
               hours,
               cycles
            FROM
               opr_rbl_hist_eng_usage_mon
            INNER JOIN acor_operator_v1 operator ON
               opr_rbl_hist_eng_usage_mon.operator_code = operator.operator_code
         )
         UNPIVOT ((quantity) FOR data_type_code IN (
                                                     hours   AS 'HOURS',
                                                     cycles  AS 'CYCLES'
                                                   )
                 )
   )
GROUP BY
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   data_type_code,
   year_code,
   month_code;                               