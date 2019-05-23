--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_urmvl_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_URMVL_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_urmvl_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_urmvl_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   removals,
   year_code,
   month_code,
   quantity
FROM
   (
      SELECT
         rmvl_mon.operator_id,
         rmvl_mon.operator_code,
         rmvl_mon.fleet_type,
         rmvl_mon.engine_type,
         rmvl_mon.year_code,
         rmvl_mon.month_code,
         rmvl_mon.quantity,
         CASE
            WHEN usage_mon.quantity > 0 THEN
               (rmvl_mon.quantity/usage_mon.quantity)*1000
            ELSE 0
         END AS rate_per_1k_hours
      FROM
         opr_rbl_eng_urmvl_mon rmvl_mon
         LEFT JOIN aopr_rbl_eng_usage_mon_mv1 usage_mon ON
            rmvl_mon.operator_id = usage_mon.operator_id AND
            rmvl_mon.fleet_type  = usage_mon.fleet_type AND
            rmvl_mon.engine_type  = usage_mon.engine_type AND
            rmvl_mon.year_code  = usage_mon.year_code AND
            rmvl_mon.month_code  = usage_mon.month_code
            AND
            usage_mon.data_type_code = 'HOURS'
      -- maintenix shopvisits
      UNION ALL
      -- to legacy shopvisits
      SELECT
         operator.operator_id,
         operator.operator_code,
         fleet_type,
         engine_type,
         year_code,
         month_code,
         removal,
         rate_per_1k_hours
      FROM
         opr_rbl_hist_eng_urmvl_mon
         INNER JOIN acor_operator_v1 operator ON
            opr_rbl_hist_eng_urmvl_mon.operator_code = operator.operator_code
   )
     UNPIVOT ( (quantity)
                FOR removals IN (
                                  quantity               AS 'Number',
                                  rate_per_1k_hours      AS 'Rate per 1000 Engine Hours'
                                )
             );