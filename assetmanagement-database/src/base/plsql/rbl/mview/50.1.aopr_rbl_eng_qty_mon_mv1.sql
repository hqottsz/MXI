--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_qty_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_QTY_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_qty_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_qty_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   eng_condition,
   row_order,
   year_code,
   month_code,
   SUM(qty) qty
FROM
  (
    SELECT
         operator_id,
         operator_code,
         fleet_type,
         engine_type,
         year_code,
         month_code,
         in_fleet  AS qty,
         'In Fleet' eng_condition,
         '1' row_order
      FROM
         opr_rbl_eng_infleet_mon
      -- to in fleet quantity
      UNION ALL
      -- in service quantity
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         engine_type,
         year_code,
         month_code,
         in_service  AS qty,
         'In Service' eng_condition,
         '2' row_order
      FROM
         opr_rbl_eng_inservice_mon
      -- historic data
      UNION ALL
     SELECT
        operator.operator_id,
        operator.operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        in_fleet,
        'In Fleet' eng_condition,
        '1' row_order
     FROM
        opr_rbl_hist_eng_qty_mon
        INNER JOIN acor_operator_v1 operator ON
           opr_rbl_hist_eng_qty_mon.operator_code = operator.operator_code
     UNION ALL
     SELECT
        operator.operator_id,
        operator.operator_code,
        fleet_type,
        engine_type,
        year_code,
        month_code,
        in_service,
        'In Service' eng_condition,
        '2' row_order
     FROM
        opr_rbl_hist_eng_qty_mon
        INNER JOIN acor_operator_v1 operator ON
           opr_rbl_hist_eng_qty_mon.operator_code = operator.operator_code
  )
GROUP BY
   operator_id,
   operator_code,
   fleet_type,
   engine_type,
   eng_condition,
   row_order,
   year_code,
   month_code;