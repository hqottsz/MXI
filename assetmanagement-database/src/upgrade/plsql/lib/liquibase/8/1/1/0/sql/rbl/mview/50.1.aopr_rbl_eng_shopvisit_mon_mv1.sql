--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_shopvisit_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_ENG_SHOPVISIT_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_eng_shopvisit_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_eng_shopvisit_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   operator_id, 
   operator_code, 
   fleet_type, 
   engine_type, 
   shopvisits,
   year_code, 
   month_code,
   quantity
FROM 
   (
      SELECT
         shopvisit_mon.operator_id, 
         shopvisit_mon.operator_code, 
         shopvisit_mon.fleet_type, 
         shopvisit_mon.engine_type, 
         shopvisit_mon.year_code, 
         shopvisit_mon.month_code, 
         shopvisit_mon.quantity,
         CASE 
            WHEN usage_mon.quantity > 0 THEN
               (shopvisit_mon.quantity/usage_mon.quantity)*1000 
            ELSE 0
         END  AS rate_per_1k_hours
      FROM
         opr_rbl_eng_shopvisit_mon shopvisit_mon
         LEFT JOIN aopr_rbl_eng_usage_mon_mv1 usage_mon ON
            shopvisit_mon.operator_id = usage_mon.operator_id AND
            shopvisit_mon.fleet_type  = usage_mon.fleet_type AND
            shopvisit_mon.engine_type  = usage_mon.engine_type AND
            shopvisit_mon.year_code  = usage_mon.year_code AND
            shopvisit_mon.month_code  = usage_mon.month_code 
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
         shop_visit,
         rate_per_1k_hours
      FROM
         opr_rbl_hist_eng_shopvisit_mon
         INNER JOIN acor_operator_v1 operator ON 
            opr_rbl_hist_eng_shopvisit_mon.operator_code = operator.operator_code             
   )
     UNPIVOT ( (quantity) 
             FOR shopvisits IN ( 
                                     quantity               AS 'Number',
                                     rate_per_1k_hours      AS 'Rate per 1000 Engine Hours'
                                   )
           );