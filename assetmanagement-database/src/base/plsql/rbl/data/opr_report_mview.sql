--liquibase formatted sql


--changeSet opr_report_mview:1 stripComments:false
-- # pirepmarep
-- 
INSERT INTO opr_report_mview (
  category,
  mview_name,
  execution_order,
  refresh_frequency_code
)
SELECT
  category,
  mview_name,
  execution_order,
  frequency_code
FROM
   (
      SELECT
         'PIREPMAREP'                  AS category,
         'AOPR_RBL_FAULT_TAIL_UCL_MV1' AS mview_name,
         1                             AS execution_order,
         'YEARLY'                      AS frequency_code
      FROM
         dual
      UNION ALL
      SELECT 'PIREPMAREP', 'AOPR_RBL_FAULT_FLEET_UCL_MV1', 2, 'YEARLY' FROM dual UNION ALL
      SELECT 'PIREPMAREP', 'AOPR_RBL_FAULT_MON_MV1',       3,  NULL    FROM dual UNION ALL
      SELECT 'PIREPMAREP', 'AOPR_RBL_FAULT_MV1',           4,  NULL    FROM dual UNION ALL
      SELECT 'PIREPMAREP', 'AOPR_RBL_FAULT_FLEET_MON_MV1', 5,  NULL    FROM dual UNION ALL
      SELECT 'PIREPMAREP', 'AOPR_RBL_FAULT_FLEET_MV1',     6,  NULL    FROM dual
   ) src
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_mview
                WHERE
                   category    = src.category AND
                   mview_name  = src.mview_name
              );                                                                      

--changeSet opr_report_mview:2 stripComments:false
-- # component
--
INSERT INTO opr_report_mview (
  category,
  mview_name,
  execution_order,
  refresh_frequency_code
)
SELECT
  category,
  mview_name,
  execution_order,
  frequency_code
FROM
   (
      SELECT
         'COMPONENT'                      AS category,
         'AOPR_RBL_COMP_RMVL_FLT_UCL_MV1' AS mview_name,
         1                                AS execution_order,
         'YEARLY'                         AS frequency_code
      FROM
         dual
      UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_CURR_MV1',   2,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_Q1_MV1',     3,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_Q2_MV1',     4,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_Q3_MV1',     5,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_Q4_MV1',     6,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_6MON_MV1',   7,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_12MON_MV1',  8,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_24MON_MV1',  9,  NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'ACOR_RBL_COMP_ATA_LOWER_FH_MV1',10, NULL FROM dual UNION ALL
      SELECT 'COMPONENT', 'AOPR_RBL_COMP_RMVL_FLEET_MV1',  11, NULL FROM dual
   ) src
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_mview
                WHERE
                   category    = src.category AND
                   mview_name  = src.mview_name
              );

--changeSet opr_report_mview:3 stripComments:false
-- # engine
--
INSERT INTO opr_report_mview (
  category,
  mview_name,
  execution_order
)
SELECT
  category,
  mview_name,
  execution_order
FROM
   (
      -- number of engines
      SELECT
         'ENGINE'                   AS category,
         'AOPR_RBL_ENG_QTY_MON_MV1' AS mview_name,
         1                          AS execution_order
      FROM
         dual
      UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_QTY_MV1',           2  FROM dual UNION ALL
      -- usage
      SELECT  'ENGINE', 'AOPR_RBL_ENG_USAGE_MON_MV1',     3  FROM dual UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_USAGE_MV1',         4  FROM dual UNION ALL
      -- delay
      SELECT  'ENGINE', 'AOPR_RBL_ENG_DELAY_MON_MV1',     5  FROM dual UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_DELAY_MV1',         6  FROM dual UNION ALL
      -- indicator
      SELECT  'ENGINE', 'AOPR_RBL_ENG_INDICATOR_MON_MV1', 7  FROM dual UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_INDICATOR_MV1',     8  FROM dual UNION ALL
      -- shopvisits
      SELECT  'ENGINE', 'AOPR_RBL_ENG_SHOPVISIT_MON_MV1', 9 FROM dual UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_SHOPVISIT_MV1',     10 FROM dual UNION ALL
      -- unscheduled removals
      SELECT  'ENGINE', 'AOPR_RBL_ENG_URMVL_MON_MV1',     11 FROM dual UNION ALL
      SELECT  'ENGINE', 'AOPR_RBL_ENG_URMVL_MV1',         12 FROM dual
   ) src
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_mview
                WHERE
                   category    = src.category AND
                   mview_name  = src.mview_name
              );                                          

--changeSet opr_report_mview:4 stripComments:false
-- fleet stats  dispatch              
insert into opr_report_mview
(
   category,
   mview_name,
   execution_order
)
with data as
(
   SELECT
      -- category, mview_name,                 execution_order
      'USAGE' as category,  'OPR_RBL_DELAY_MV' as mview_name, 1 as execution_order
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'OPR_RBL_MONTHLY_USAGE_MV',        2
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'OPR_RBL_MONTHLY_INCIDENT_MV',     3
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'OPR_RBL_MONTHLY_DELAY_MV',        4
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'OPR_RBL_MONTHLY_DISPATCH_MV',     5
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_RELIABILITY_V1',             6
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_TDRL_V1',                    7
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_SERVICE_DIFFICULTY_V1',      8
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_STATION_DELAY_V1',           9
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_TECHNICAL_DELAY_V1',       10
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_AIRCRAFT_DELAY_V1',       11
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_ATA_DELAY_V1',            12
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_DISPATCH_METRIC_V1',       13
   from
      dual
   UNION ALL
   SELECT
      --  category, mview_name,                 execution_order
        'USAGE',  'AOPR_DISPATCH_RELIABILITY_V1',  14
   from dual
   UNION ALL
   SELECT
      -- report_code, category, mview_name,                 execution_order
      'USAGE',  'AOPR_MONTHLY_OPERATOR_USAGE_V1',15
   from
      dual
)
SELECT
    category,
    mview_name,
    execution_order
FROM
   data
WHERE
    ( category,mview_name) not in
     ( select  category,mview_name from opr_report_mview);                            

--changeSet opr_report_mview:5 stripComments:false
-- operator materialized views
INSERT INTO opr_report_mview (
  category,
  mview_name,
  execution_order,
  refresh_frequency_code
)
SELECT
  category,
  mview_name,
  execution_order,
  'DAILY' frequency_code
FROM
   (
      SELECT
         'OPR_MVIEW'          AS category,
         'AOPR_COMP_RMVL_MV1' AS mview_name,
         1                    AS execution_order
      FROM
         dual UNION ALL
      SELECT 'OPR_MVIEW', 'AOPR_LOGBOOK_FAULT_MV1',         2 FROM dual UNION ALL
      SELECT 'OPR_MVIEW', 'ACOR_REQ_LASTDONE_NEXTDUE_V1',   3 FROM dual UNION ALL
      SELECT 'OPR_MVIEW', 'ACOR_BLOCK_LASTDONE_NEXTDUE_V1', 4 FROM dual UNION ALL
      SELECT 'OPR_MVIEW', 'ACOR_ACFT_WP_PARTS_MV1',         5 FROM dual UNION ALL
      SELECT 'OPR_MVIEW', 'ACOR_NON_ROUTINE_FAULT_MV1',     6 FROM dual UNION ALL
      SELECT 'OPR_MVIEW', 'ACOR_ADSB_REF_DOC_DEFN_MV1',     7 FROM dual
   ) src
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_mview
                WHERE
                   category    = src.category AND
                   mview_name  = src.mview_name
              );                             