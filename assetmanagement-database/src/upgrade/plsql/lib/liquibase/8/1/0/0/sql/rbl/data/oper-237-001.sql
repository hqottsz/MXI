--liquibase formatted sql


--changeSet oper-237-001:1 stripComments:false
--
--
-- DATA POPULATION SCRIPT FOR TABLE OPR_DELAY_CATEGORY
--
--
MERGE INTO opr_delay_category delay_category
USING 
(
      SELECT
         '0-15'                        AS delay_category_code,
         0                             AS low_range,
         15                            AS high_range,
         'Between 0 and 15 minutes'    AS delay_category_name,
         1                             AS display_order
      FROM
         DUAL
      UNION
      SELECT
          '15-60'                       AS delay_category_code,
          15                            AS low_range,
          60                            AS high_range,
          'Between 15 and 60 minutes'   AS delay_category_name,
         2                              AS display_order
      FROM
          DUAL
      UNION
      SELECT
          '60-180'                      AS delay_category_code,
          60                            AS low_range,
          180                           AS high_range,
          'Between 60 and 180 minutes'  AS delay_category_name,
         3                              AS display_order
      FROM
         DUAL
      UNION
      SELECT
          '180+'                        AS delay_category_code,
          180                           AS low_range,
          NULL                          AS high_range,
          '> 180 minutes'               AS delay_category_name,
         4                              AS display_order
      FROM
        DUAL
) data
ON (
      data.delay_category_code = delay_category.delay_category_code
   )
WHEN NOT MATCHED THEN
   INSERT
   (
      delay_category.delay_category_code, 
      delay_category.low_range, 
      delay_category.high_range, 
      delay_category.delay_category_name,
      delay_category.display_order
   )
   VALUES
   (
      data.delay_category_code, 
      data.low_range, 
      data.high_range, 
      data.delay_category_name,
      data.display_order
   );