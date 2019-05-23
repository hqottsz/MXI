--liquibase formatted sql


--changeSet 50.1.aopr_rbl_eng_delay_mon_mv1:1 stripComments:false
CREATE MATERIALIZED VIEW AOPR_RBL_ENG_DELAY_MON_MV1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   *
FROM
   (
      SELECT
         acor_operator_v1.operator_id,
         acor_operator_v1.operator_code,
         aopr_ata_delay_v1.fleet_type_code         AS fleet_type,
         ACOR_RBL_SUBASSY_TYPE_V1.ASSMBL_CD        AS engine_type,
         aopr_ata_delay_v1.year_code,
         aopr_ata_delay_v1.month_code,
         SUM(aopr_ata_delay_v1.delayed_departures) AS quantity,
         CASE
            WHEN engine_monthly_cycle.cycles > 0 THEN
               (SUM(aopr_ata_delay_v1.delayed_departures)/engine_monthly_cycle.cycles) * 100
            ELSE 0
         END AS rate_per_100_cycles
      FROM
         aopr_ata_delay_v1
      INNER JOIN acor_operator_v1 ON
         aopr_ata_delay_v1.operator_code  = acor_operator_v1.operator_code
      -- Get Engine Type
      INNER JOIN acor_rbl_subassy_type_v1 ON
         aopr_ata_delay_v1.fleet_type_code = acor_rbl_subassy_type_v1.fleet_type AND
         acor_rbl_subassy_type_v1.assmbl_class_cd = 'ENG'
      -- Get Engine  Monthly Cycles
      INNER JOIN
         (
             SELECT
                operator_code,
                fleet_type,
                year_code,
                month_code,
                engine_type,
                quantity     AS cycles
              FROM
                aopr_rbl_eng_usage_mon_mv1
              WHERE
                 data_type_code='CYCLES'
         ) engine_monthly_cycle
      ON aopr_ata_delay_v1.fleet_type_code  = engine_monthly_cycle.fleet_type    AND
         aopr_ata_delay_v1.Operator_Code    = engine_monthly_cycle.operator_code AND
         aopr_ata_delay_v1.Year_Code        = engine_monthly_cycle.year_code     AND
         aopr_ata_delay_v1.Month_Code       = engine_monthly_cycle.month_code    AND
         acor_rbl_subassy_type_v1.assmbl_cd = engine_monthly_cycle.engine_type
       -- Filter only on ATA 71 to 80 excluding 78 for delays above 15 mn
     WHERE
        not aopr_ata_delay_v1.chapter_code in ('78','N/A') AND
        ( aopr_ata_delay_v1.chapter_code > '70' AND
          aopr_ata_delay_v1.chapter_code <='80'
        )                                                  AND
        aopr_ata_delay_v1.delay_category_code <> '0-15'
     GROUP BY
        acor_operator_v1.operator_id,
        acor_operator_v1.operator_code,
        aopr_ata_delay_v1.fleet_type_code ,
        ACOR_RBL_SUBASSY_TYPE_V1.ASSMBL_CD,
        aopr_ata_delay_v1.year_code,
        aopr_ata_delay_v1.month_code,
        engine_monthly_cycle.cycles
   )
     UNPIVOT ( (quantity)
                FOR delays IN (
                                quantity               AS 'Number',
                                rate_per_100_cycles    AS 'Rate per 100 Engine Cycles'
                              )
             );